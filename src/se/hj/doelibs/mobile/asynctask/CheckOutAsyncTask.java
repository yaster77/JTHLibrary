package se.hj.doelibs.mobile.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import se.hj.doelibs.api.LoanableDao;
import se.hj.doelibs.mobile.utils.CurrentUserUtils;
import se.hj.doelibs.model.Loan;

/**
 * runs the checkout progress
 *
 * @author Christoph
 */
public class CheckOutAsyncTask extends AsyncTask<Void, Void, Loan> {

	private int loanableId;
	private TaskCallback<Loan> callback;
	private Context context;

	public CheckOutAsyncTask(Context context, int loanableId, TaskCallback<Loan> callback) {
		this.context = context;
		this.loanableId = loanableId;
		this.callback = callback;
	}

	@Override
	protected Loan doInBackground(Void... voids) {
		LoanableDao loanableDao = new LoanableDao(CurrentUserUtils.getCredentials(context));
		return loanableDao.checkOut(this.loanableId);
	}

	@Override
	protected void onPostExecute(Loan loan) {
		callback.onTaskCompleted(loan);
	}

	@Override
	protected void onPreExecute() {
		callback.beforeTaskRun();
	}
}