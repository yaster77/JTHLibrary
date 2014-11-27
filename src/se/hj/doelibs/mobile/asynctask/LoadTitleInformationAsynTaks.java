package se.hj.doelibs.mobile.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpException;
import se.hj.doelibs.api.LoanDao;
import se.hj.doelibs.api.ReservationDao;
import se.hj.doelibs.api.TitleDao;
import se.hj.doelibs.mobile.utils.CurrentUserUtils;
import se.hj.doelibs.model.Loan;
import se.hj.doelibs.model.Loanable;
import se.hj.doelibs.model.Title;

import java.util.ArrayList;
import java.util.List;

/**
 * task to load detailed information about the title
 *
 * @author Christoph
 */
public class LoadTitleInformationAsynTaks extends AsyncTask<Void, Void, Title> {
	private int titleId;
	private TaskCallback<Title> onTitleLoadTaskCallback;
	private Context context;

	public LoadTitleInformationAsynTaks(Context context, int titleId, TaskCallback<Title> callback) {
		this.context = context;
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
			LoanDao loanDao = new LoanDao(CurrentUserUtils.getCredentials(this.context));
			List<Loan> usersLoans = new ArrayList<Loan>();

			//check if user has title currently checked out
			boolean userHasALoanableOfThisTitle = false;
			boolean userHasAvailableReservationForTitle = false;

			if(CurrentUserUtils.getCredentials(this.context) != null) {
				usersLoans = loanDao.getCurrentUsersLoans();

				for(Loan loan : usersLoans) {
					if(loan.getLoanable().getTitle().getTitleId() == this.titleId){
						userHasALoanableOfThisTitle = true;
					}
				}

				ReservationDao reservationDao = new ReservationDao(CurrentUserUtils.getCredentials(this.context));
				userHasAvailableReservationForTitle = reservationDao.userHasAvailableReservationForTitle(this.titleId);
			}



			//decide on each status and the userinformation if the loanable will be added
			for(Loanable loanable : allLoanables) {
				if(loanable.getStatus() == Loanable.Status.AVAILABLE && (CurrentUserUtils.getCredentials(this.context) == null || !userHasALoanableOfThisTitle)) {
					//available and user has title currently not borrowed or is not logged in
					filteredLoanables.add(loanable);
				} else if(loanable.getStatus() == Loanable.Status.RESERVED && (CurrentUserUtils.getCredentials(this.context) == null || userHasAvailableReservationForTitle)) {
					//user has reservation or is not logged in
					filteredLoanables.add(loanable);
				} else if(loanable.getStatus() == Loanable.Status.RECALLED || loanable.getStatus() == Loanable.Status.BORROWED) {

					//add if user has borrowed this loanable
					for(Loan loan : usersLoans) {
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
		TitleDao titleDao = new TitleDao(CurrentUserUtils.getCredentials(this.context));
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