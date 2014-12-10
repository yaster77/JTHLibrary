package se.hj.doelibs.mobile.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import se.hj.doelibs.api.LoanDao;
import se.hj.doelibs.api.ReservationDao;
import se.hj.doelibs.mobile.utils.CurrentUserUtils;
import se.hj.doelibs.model.Loan;
import se.hj.doelibs.model.Reservation;

/**
 * task to check if the reserve button should be displayed to the current user
 *
 * @author Christoph
 */
public class LoadReservationStatusAsyncTask extends AsyncTask<Void, Void, Boolean> {

	private int titleId;
	private TaskCallback<Boolean> taskCallbacks;
	private Context context;

	public LoadReservationStatusAsyncTask(Context context, int titleId, TaskCallback<Boolean> callback) {
		this.context = context;
		this.titleId = titleId;
		this.taskCallbacks = callback;
	}

	@Override
	protected Boolean doInBackground(Void... voids) {
		boolean activateReserveButton = false;

		if(CurrentUserUtils.getCredentials(this.context) == null) {
			//if user is not logged in
			return false;
		}

		ReservationDao reservationDao = new ReservationDao(CurrentUserUtils.getCredentials(this.context));
		LoanDao loanDao = new LoanDao(CurrentUserUtils.getCredentials(this.context));

		//check if user has title currently checked out
		boolean userHasALoanableOfThisTitle = false;
		for(Loan loan : loanDao.getCurrentUsersLoans()) {
			if (loan.getLoanable().getTitle().getTitleId() == this.titleId) {
				userHasALoanableOfThisTitle = true;
			}
		}

		//show title reserve button only if user has the title currently not borrowed or a reservation for it
		if(!userHasALoanableOfThisTitle) {
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