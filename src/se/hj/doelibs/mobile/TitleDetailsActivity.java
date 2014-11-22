package se.hj.doelibs.mobile;

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
import se.hj.doelibs.mobile.codes.ExtraKeys;
import se.hj.doelibs.mobile.listadapter.LoanablesListAdapter;
import se.hj.doelibs.mobile.utils.ListUtils;
import se.hj.doelibs.model.Loan;
import se.hj.doelibs.model.Loanable;
import se.hj.doelibs.model.Reservation;
import se.hj.doelibs.model.Title;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christoph
 */
public class TitleDetailsActivity extends BaseActivity {
	private Title title;

	private TextView tv_title;
	private TextView tv_edition;

	private TextView tv_categories;
	private TextView tv_publisher;
	private TextView tv_authors;

	private Button btn_reserve;

	private List<Loanable> loanables;
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
		title = (Title) intent.getSerializableExtra(ExtraKeys.TITLE_OBJECT);

		setupView();
	}

	public void onReserve(View view) {

	}

	/**
	 * loads all data into the viewfields. If any data of the title object is missing (default author, loanables, etc) it loads them
	 */
	private void setupView(){
		setupNativeData();
		setupDetailData();
		setupReserveButton();
	}

	/**
	 * puts all the data from the given title object into the view
	 */
	private void setupNativeData() {
		tv_title.setText(title.getBookTitle());
		tv_edition.setText("Edition " + title.getEditionNumber());
		tv_publisher.setText(title.getPublisher().getName() + " (" + title.getEditionYear() + ")");
	}

	/**
	 * loads more detailed information about the title (author, loanables, etc) and puts it into the view
	 */
	private void setupDetailData() {
		new AsyncTask<Integer, Void, Title>() {
			@Override
			protected Title doInBackground(Integer... titleIds) {
				TitleDao titleDao = new TitleDao(getCredentials());
				Title extendedTitle = null;

				try {
					extendedTitle = titleDao.getById(titleIds[0]);

					//filter loanables
					ReservationDao reservationDao = new ReservationDao(getCredentials());
					LoanDao loanDao = new LoanDao(getCredentials());
					List<Loanable> filteredLoanables = null;
					List<Loan> usersLoans = loanDao.getCurrentUsersLoans();

					//check if user has title currently checked out
					boolean userHasALoanableOfThisTitle = false;
					if(getCredentials() != null) {
						for(Loan loan : usersLoans) {
							if(loan.getLoanable().getTitle().getTitleId() == titleIds[0]){
								userHasALoanableOfThisTitle = true;
							}
						}
					}

					if(extendedTitle.getLoanables() != null && extendedTitle.getLoanables().size() > 0) {
						filteredLoanables = new ArrayList<Loanable>();
						boolean userHasAvailableReservationForTitle = reservationDao.userHasAvailableReservationForTitle(extendedTitle.getLoanables().get(0).getTitle().getTitleId());

						for(Loanable loanable : extendedTitle.getLoanables()) {
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
					extendedTitle.setLoanables(filteredLoanables);
				} catch (HttpException e) {
					Log.e("TitleDetails", "could not load more details from title", e);
				}

				return extendedTitle;
			}

			@Override
			protected void onPostExecute(Title extendedTitle) {
				//now put all the extended titleinformation into the view

				tv_categories.setText(ListUtils.implode(extendedTitle.getTopics(), ", "));

				//add authors and editors in one textview:
				String authors = ListUtils.implode(extendedTitle.getAuthors(), ", ");
				String editors = ListUtils.implode(extendedTitle.getEditors(), " (Editor), ");
				if(extendedTitle.getEditors() != null && extendedTitle.getEditors().size() > 0) {
					editors += " (Editor)";
				}
				String fullAuthorList = authors + ((authors.length()>0 && editors.length() > 0)?", ":"") + editors;
				tv_authors.setText(fullAuthorList);

				lv_loanables.setAdapter(new LoanablesListAdapter(TitleDetailsActivity.this, extendedTitle.getLoanables()));

				loadingCompleateStatus++;
				if(loadingCompleateStatus == numberOfBackgroundLoadings) {
					setProgressBarIndeterminateVisibility(false);
				}
			}

			@Override
			protected void onPreExecute() {
				setProgressBarIndeterminateVisibility(true);
			}
		}.execute(title.getTitleId());
	}

	/**
	 * sets up the reserve button: depending if the user is logged in, has no current loans with this title and no reservation for this title it will display the button
	 */
	private void setupReserveButton() {
		new AsyncTask<Integer, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Integer... titleIds) {
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
						if(loan.getLoanable().getTitle().getTitleId() == titleIds[0]){
							userHasALoanableOfThisTitle = true;
						}
					}
				}

				//show title reserve button only if user is logged in and has the title currently not borrowed or a reservation for it
				if(getCredentials() != null && !userHasALoanableOfThisTitle) {
					boolean userHasReservationForTitle = false;
					for(Reservation reservation : reservationDao.getCurrentUsersReservations()) {
						if(reservation.getTitle().getTitleId() == titleIds[0]) {
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
				if(activateButton) {
					btn_reserve.setVisibility(View.VISIBLE);
				}
				loadingCompleateStatus++;

				if(loadingCompleateStatus == numberOfBackgroundLoadings) {
					setProgressBarIndeterminateVisibility(false);
				}
			}

			@Override
			protected void onPreExecute() {
				setProgressBarIndeterminateVisibility(true);
			}
		}.execute(title.getTitleId());

	}
}