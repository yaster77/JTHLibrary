package se.hj.doelibs.mobile;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import se.hj.doelibs.api.LoanDao;
import se.hj.doelibs.api.ReservationDao;
import se.hj.doelibs.model.Reservation;

import java.util.List;

public class MyLoansActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View contentView = inflater.inflate(R.layout.activity_my_loans, null, false);
	    drawerLayout.addView(contentView, 0);

		//check if user is logged in
		if(getCredentials() == null) {
			Log.d("MyLoans", "user is not logged in");
			Intent loginActivity = new Intent(this, LoginActivity.class);
			startActivity(loginActivity);
		} else {
			final TextView tv = (TextView)findViewById(R.id.myLoanText);
			final LoanDao loanDao = new LoanDao(getCredentials());
			final ReservationDao reservationDaoDao = new ReservationDao(getCredentials());

			tv.setText("loading users loans/reservations");

			new AsyncTask<Void, Void, List<Reservation>>() {

				@Override
				protected List<Reservation> doInBackground(Void... params) {
					//return loanDao.getCurrentUsersLoans();
					return  reservationDaoDao.getCurrentUsersReservations();
				}

				@Override
				protected void onPostExecute(List<Reservation> reservations) {
					tv.setText("user has " + reservations.size() + " reservations");
				}
			}.execute();
		}
	}
}