package se.hj.doelibs.mobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import se.hj.doelibs.mobile.asynctask.LoadReservationStatusAsyncTask;
import se.hj.doelibs.mobile.asynctask.LoadTitleInformationAsynTaks;
import se.hj.doelibs.mobile.asynctask.ReserveTitleAsyncTask;
import se.hj.doelibs.mobile.asynctask.TaskCallback;
import se.hj.doelibs.mobile.codes.ExtraKeys;
import se.hj.doelibs.mobile.listadapter.LoanablesListAdapter;
import se.hj.doelibs.mobile.utils.ListUtils;
import se.hj.doelibs.model.Author;
import se.hj.doelibs.model.Title;

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

	private ProgressDialog reserveProgressDialog;
	private ProgressDialog loadReservationDetailsProgressDialog;
	private ProgressDialog loadTitleDetailProgressDialog;

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
		reserveProgressDialog = new ProgressDialog(this);
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

		dialogBuilder
				.setMessage(R.string.dialog_really_reserve_title)
				.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//reserve title

						new ReserveTitleAsyncTask(TitleDetailsActivity.this, titleId, new TaskCallback<Boolean>() {
							@Override
							public void onTaskCompleted(Boolean success) {

								if (success) {
									TitleDetailsActivity.this.btn_reserve.setVisibility(View.INVISIBLE);
									Toast.makeText(TitleDetailsActivity.this, getResources().getText(R.string.title_reserve_successfull), Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(TitleDetailsActivity.this, getResources().getText(R.string.title_reserve_error), Toast.LENGTH_LONG).show();
								}
								reserveProgressDialog.dismiss();
							}

							@Override
							public void beforeTaskRun() {
								reserveProgressDialog.setMessage(getResources().getText(R.string.dialog_progress_reserve_title));
								reserveProgressDialog.setCancelable(false);
								reserveProgressDialog.show();
							}
						}).execute();

					}
				})
				.setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//nothing
					}
				});
		dialogBuilder.show();
	}

	/**
	 * loads all data into the viewfields.
	 */
	private void setupView(){
		setupReserveButton();
		setupData();
	}

	/**
	 * loads all the data of the book and puts it into the view
	 */
	private void setupData() {
		loadTitleDetailProgressDialog = new ProgressDialog(this);

		new LoadTitleInformationAsynTaks(TitleDetailsActivity.this, this.titleId, new TaskCallback<Title>() {
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
				loadTitleDetailProgressDialog.dismiss();
			}

			@Override
			public void beforeTaskRun() {
				//setup a progressdialog
				loadTitleDetailProgressDialog.setMessage(getResources().getText(R.string.dialog_progress_load_titleinformation));
				loadTitleDetailProgressDialog.setCancelable(false);
				loadTitleDetailProgressDialog.show();
			}
		}).execute();
	}

	/**
	 * sets up the reserve button: depending if the user is logged in, has no current loans with this title and no reservation for this title it will display the button
	 */
	private void setupReserveButton() {
		if(getCredentials() != null) {
			reserveProgressDialog = new ProgressDialog(this);

			new LoadReservationStatusAsyncTask(TitleDetailsActivity.this, this.titleId, new TaskCallback<Boolean>() {
				@Override
				public void onTaskCompleted(Boolean showButton) {
					if(showButton) {
						btn_reserve.setVisibility(View.VISIBLE);
					} else {
						btn_reserve.setVisibility(View.INVISIBLE);
					}
					reserveProgressDialog.dismiss();
				}

				@Override
				public void beforeTaskRun() {
					//setup a progressdialog
					reserveProgressDialog.setMessage(getResources().getText(R.string.dialog_progress_check_reservations));
					reserveProgressDialog.setCancelable(false);
					reserveProgressDialog.show();
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
		String editorText = getResources().getText(R.string.titledetails_editor_prefix).toString();

		String authorString = ListUtils.implode(authors, ", ");
		String editorString = ListUtils.implode(editors, " ("+editorText+"), ");

		if(editors != null && editors.size() > 0) {
			editorString += " ("+editorText+")";
		}

		return authorString + ((authorString.length()>0 && editorString.length() > 0)?", ":"") + editorString;
	}

}