package se.hj.doelibs.mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import org.apache.http.HttpException;
import se.hj.doelibs.api.LoanDao;
import se.hj.doelibs.api.ReservationDao;
import se.hj.doelibs.api.TitleDao;
import se.hj.doelibs.mobile.asynctask.TaskCallback;
import se.hj.doelibs.mobile.codes.ExtraKeys;
import se.hj.doelibs.mobile.listadapter.LoanablesListAdapter;
import se.hj.doelibs.mobile.utils.ListUtils;
import se.hj.doelibs.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christoph
 */
public class TitleDetailsActivity extends BaseActivity {
	//private Title title;
	private int titleId;

	private TextView tv_title;
	private TextView tv_edition;

	private TextView tv_categories;
	private TextView tv_publisher;
	private TextView tv_authors;

	private Button btn_reserve;
	private ListView lv_loanables;

	private int loadingCompleateStatus = 0;
	private final int numberOfBackgroundLoadings = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//setup navigation
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.activity_title_details, null, false);
		drawerLayout.addView(contentView, 0);

		tv_title = (TextView)findViewById(R.id.tv_titledetails_titlename);
		tv_edition = (TextView)findViewById(R.id.tv_titledetails_edition);
		tv_categories = (TextView)findViewById(R.id.tv_titledetails_categories);
		tv_publisher = (TextView)findViewById(R.id.tv_titledetails_publisher);
		tv_authors = (TextView)findViewById(R.id.tv_titledetails_authors);
		btn_reserve = (Button)findViewById(R.id.btn_titledetails_reserve);
		lv_loanables = (ListView)findViewById(R.id.lv_titledetails_loanableslist);

		Intent intent = getIntent();
		titleId = intent.getIntExtra(ExtraKeys.TITLE_ID, 0);

		setupView();
	}

	public void onReserve(View view) {

	}

	/**
	 * loads all data into the viewfields.
	 */
	private void setupView(){
		setupData();
		setupReserveButton();
	}

	/**
	 * loads all the data of the book and puts it into the view
	 */
	private void setupData() {
		final ProgressDialog progressDialog = new ProgressDialog(this);

		new LoadTitleInformationAsynTaks(this.titleId, new TaskCallback<Title>() {
			@Override
			public void onTaskCompleted(Title title) {
				//setup the views
				tv_title.setText(title.getBookTitle());
				tv_edition.setText("Edition " + title.getEditionNumber());
				tv_publisher.setText(title.getPublisher().getName() + " (" + title.getEditionYear() + ")");
				tv_categories.setText(ListUtils.implode(title.getTopics(), ", "));
				tv_authors.setText(createAuthorEditorString(title.getAuthors(), title.getEditors()));
				lv_loanables.setAdapter(new LoanablesListAdapter(TitleDetailsActivity.this, title.getLoanables()));

				//hide progressbar
				progressDialog.hide();
			}

			@Override
			public void beforeTaskRun() {
				//setup a progressdialog
				progressDialog.setMessage(getResources().getText(R.string.dialog_progress_load_titleinformation));
				progressDialog.setCancelable(false);
				progressDialog.show();
			}
		}).execute();
	}

	/**
	 * sets up the reserve button: depending if the user is logged in, has no current loans with this title and no reservation for this title it will display the button
	 */
	private void setupReserveButton() {
		if(getCredentials() != null) {
			final ProgressDialog progressDialog = new ProgressDialog(this);

			new LoadReservationStatusAsyncTask(this.titleId, new TaskCallback<Boolean>() {
				@Override
				public void onTaskCompleted(Boolean showButton) {
					if(showButton) {
						btn_reserve.setVisibility(View.VISIBLE);
					} else {
						btn_reserve.setVisibility(View.INVISIBLE);
					}
					progressDialog.hide();
				}

				@Override
				public void beforeTaskRun() {
					//setup a progressdialog
					progressDialog.setMessage(getResources().getText(R.string.dialog_progress_check_reservations));
					progressDialog.setCancelable(false);
					progressDialog.show();
				}
			}).execute();

		} else {
			//user is not logged in so hide button
			btn_reserve.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * concats all authors and editors in a string. adds behind each author the postfix "Editor"
	 * @param authors
	 * @param editors
	 * @return
	 */
	private String createAuthorEditorString(List<Author> authors, List<Author> editors) {
		//TODO: don't hardcode Editor - read from resource

		String authorString = ListUtils.implode(authors, ", ");
		String editorString = ListUtils.implode(editors, " (Editor), ");

		if(editors != null && editors.size() > 0) {
			editorString += " (Editor)";
		}

		return authors + ((authorString.length()>0 && editorString.length() > 0)?", ":"") + editors;
	}


	/**
	 * task to load detailed information about the title and setup the view
	 */
	private class LoadTitleInformationAsynTaks extends AsyncTask<Void, Void, Title> {
		private int titleId;
		private TaskCallback<Title> onTitleLoadTaskCallback;

		public LoadTitleInformationAsynTaks(int titleId, TaskCallback<Title> callback) {
			this.titleId = titleId;
			this.onTitleLoadTaskCallback = callback;
		}

		/**
		 * filters the loanables so only the available ones will be displayed or if the user has a reservation the reserved ones
		 * If he has currently borrowed one loanable only this one will be displayed with a checkin button
		 *
		 * @param allLoanables list of all loanables
		 * @return filtered loanables
		 */
		private List<Loanable> filterLoanables(List<Loanable> allLoanables) {
			List<Loanable> filteredLoanables = new ArrayList<Loanable>();

			if(allLoanables != null && allLoanables.size() > 0) {
				ReservationDao reservationDao = new ReservationDao(getCredentials());
				LoanDao loanDao = new LoanDao(getCredentials());
				List<Loan> usersLoans = loanDao.getCurrentUsersLoans();

				//check if user has title currently checked out
				boolean userHasALoanableOfThisTitle = false;
				if(getCredentials() != null) {
					for(Loan loan : usersLoans) {
						if(loan.getLoanable().getTitle().getTitleId() == this.titleId){
							userHasALoanableOfThisTitle = true;
						}
					}
				}

				boolean userHasAvailableReservationForTitle = reservationDao.userHasAvailableReservationForTitle(this.titleId);

				//decide on each status and the userinformation if the loanable will be added
				for(Loanable loanable : allLoanables) {
					if(loanable.getStatus() == Loanable.Status.AVAILABLE && (getCredentials() == null || !userHasALoanableOfThisTitle)) {
						//available and user has title currently not borrowed or is not logged in
						filteredLoanables.add(loanable);
					} else if(loanable.getStatus() == Loanable.Status.RESERVED && (getCredentials() == null || userHasAvailableReservationForTitle)) {
						//user has reservation or is not logged in
						filteredLoanables.add(loanable);
					} else if(loanable.getStatus() == Loanable.Status.RECALLED || loanable.getStatus() == Loanable.Status.BORROWED) {

						//add if user has borrowed this loanable
						for(Loan loan : loanDao.getCurrentUsersLoans()) {
							if(loan.getLoanable().getLoanableId() == loanable.getLoanableId()){
								filteredLoanables.add(loanable);
							}
						}
					}
				}
			}

			return filteredLoanables;
		}

		@Override
		protected Title doInBackground(Void... voids) {
			TitleDao titleDao = new TitleDao(getCredentials());
			Title title = null;

			try {
				title = titleDao.getById(this.titleId);

				//filter loanables
				title.setLoanables(filterLoanables(title.getLoanables()));
			} catch (HttpException e) {
				Log.e("TitleDetails", "could not load details from title", e);
			}

			return title;
		}

		@Override
		protected void onPostExecute(Title title) {
			//now pass all the information to the callback
			onTitleLoadTaskCallback.onTaskCompleted(title);
		}

		@Override
		protected void onPreExecute() {
			onTitleLoadTaskCallback.beforeTaskRun();
		}
	}


	/**
	 * checks if the reserve button should be displayed to the current user
	 */
	private class LoadReservationStatusAsyncTask extends AsyncTask<Void, Void, Boolean> {

		private int titleId;
		private TaskCallback<Boolean> taskCallbacks;

		public LoadReservationStatusAsyncTask(int titleId, TaskCallback<Boolean> callback) {
			this.titleId = titleId;
			this.taskCallbacks = callback;
		}

		@Override
		protected Boolean doInBackground(Void... voids) {
			boolean activateReserveButton = false;

			if(getCredentials() == null) {
				//if use is not logged in
				return false;
			}

			ReservationDao reservationDao = new ReservationDao(getCredentials());
			LoanDao loanDao = new LoanDao(getCredentials());

			//check if user has title currently checked out
			boolean userHasALoanableOfThisTitle = false;
			if(getCredentials() != null) {
				for(Loan loan : loanDao.getCurrentUsersLoans()) {
					if(loan.getLoanable().getTitle().getTitleId() == this.titleId){
						userHasALoanableOfThisTitle = true;
					}
				}
			}

			//show title reserve button only if user is logged in and has the title currently not borrowed or a reservation for it
			if(getCredentials() != null && !userHasALoanableOfThisTitle) {
				boolean userHasReservationForTitle = false;
				for(Reservation reservation : reservationDao.getCurrentUsersReservations()) {
					if(reservation.getTitle().getTitleId() == this.titleId) {
						userHasReservationForTitle = true;
					}
				}

				if(!userHasReservationForTitle) {
					activateReserveButton = true;
				}
			}

			return activateReserveButton;
		}

		@Override
		protected void onPostExecute(Boolean activateButton) {
			taskCallbacks.onTaskCompleted(activateButton);
		}

		@Override
		protected void onPreExecute() {
			taskCallbacks.beforeTaskRun();
		}
	}
}