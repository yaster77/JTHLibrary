package se.hj.doelibs.mobile;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import se.hj.doelibs.api.LoanDao;
import se.hj.doelibs.api.ReservationDao;
import se.hj.doelibs.mobile.listadapter.LoanListAdapter;
import se.hj.doelibs.mobile.listadapter.ReservationListAdapter;
import se.hj.doelibs.model.Loan;
import se.hj.doelibs.model.Reservation;

import java.util.List;

public class MyLoansActivity extends BaseActivity {

	ListView lv_myLoans;
	ListView lv_myReservations;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View contentView = inflater.inflate(R.layout.activity_my_loans, null, false);
	    drawerLayout.addView(contentView, 0);

		lv_myLoans = (ListView) findViewById(R.id.loans_list);
		lv_myReservations = (ListView) findViewById(R.id.reservations_list);


		//check if user is logged in
		if(getCredentials() == null) {
			Log.d("MyLoans", "user is not logged in");
			Intent loginActivity = new Intent(this, LoginActivity.class);
			startActivity(loginActivity);
		} else {
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
					lv_myLoans.setAdapter(new LoanListAdapter(MyLoansActivity.this, loans));
				}
			}.execute();


		}
		//lv_myReservations.setAdapter(new ReservationListAdapter(MyLoansActivity.this, reservationDaoDao.getCurrentUsersReservations()));
	}

}