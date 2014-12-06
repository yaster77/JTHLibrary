package se.hj.doelibs.mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
<<<<<<< HEAD
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import se.hj.doelibs.api.LoanDao;
import se.hj.doelibs.api.ReservationDao;
import se.hj.doelibs.mobile.asynctask.TaskCallback;
import se.hj.doelibs.mobile.codes.ExtraKeys;
import se.hj.doelibs.mobile.listadapter.LoanListAdapter;
import se.hj.doelibs.mobile.listadapter.ReservationListAdapter;
import se.hj.doelibs.mobile.utils.ProgressDialogUtils;
import se.hj.doelibs.model.Loan;
import se.hj.doelibs.model.Reservation;

import java.util.List;

public class MyLoansActivity extends BaseActivity {

	ListView lv_myLoans;
	ListView lv_myReservations;
=======
import se.hj.doelibs.mobile.codes.ExtraKeys;
import se.hj.doelibs.mobile.listener.OnTitleItemSelectedListener;
>>>>>>> upstream/master

/**
 * @author Alexander
 */
public class MyLoansActivity extends BaseActivity implements OnTitleItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View contentView = inflater.inflate(R.layout.activity_my_loans, null, false);
		drawerLayout.addView(contentView, 0);
	}

	@Override
	public void onTitleItemSelected(int titleId) {
		TitleDetailsFragment fragment = (TitleDetailsFragment) getFragmentManager().findFragmentById(R.id.detailFragment);

		if (fragment != null && fragment.isInLayout()) {
			fragment.setTitleId(titleId);
			fragment.setupView();
		} else {
<<<<<<< HEAD
			final TextView tv = (TextView)findViewById(R.id.myLoanText);
			final LoanDao loanDao = new LoanDao(getCredentials());
			final ReservationDao reservationDaoDao = new ReservationDao(getCredentials());

			//tv.setText("loading users loans/reservations");

			new AsyncTask<Void, Void, List<Reservation>>() {

				@Override
				protected List<Reservation> doInBackground(Void... params) {
					//return loanDao.getCurrentUsersLoans();
					return  reservationDaoDao.getCurrentUsersReservations();

				}

				@Override
				protected void onPostExecute(List<Reservation> reservations) {
					//tv.setText("user has " + reservations.size() + " reservations");
					lv_myReservations.setAdapter(new ReservationListAdapter(MyLoansActivity.this, reservations ));
				}
			}.execute();

			new AsyncTask<Void, Void, List<Loan>>() {

				@Override
				protected List<Loan> doInBackground(Void... params) {
					return loanDao.getCurrentUsersLoans();
				}

				@Override
				protected void onPostExecute(List<Loan> loans) {
					//tv.setText("user has " + loans.size() + " reservations");
					lv_myLoans.setAdapter(new LoanListAdapter(MyLoansActivity.this, loans, getAfterCheckInTaskCallback()));
				}
			}.execute();


		}

	}
	private TaskCallback<Boolean> getAfterCheckInTaskCallback() {
		return new TaskCallback<Boolean>() {

			@Override
			public void onTaskCompleted(Boolean checkInSuccess) {
				if(checkInSuccess) {
					Intent titleActivity = new Intent(MyLoansActivity.this, MyLoansActivity.class);
					startActivity(titleActivity);
				} else {

				}
			}

			@Override
			public void beforeTaskRun() {

			}
		};
=======
			//fragment was not found in current layout --> it is not on a large device --> start activity with title details
			Intent titleDetailsActivity = new Intent(this, TitleDetailsActivity.class);
			titleDetailsActivity.putExtra(ExtraKeys.TITLE_ID, titleId);
			startActivity(titleDetailsActivity);
		}
>>>>>>> upstream/master
	}
}