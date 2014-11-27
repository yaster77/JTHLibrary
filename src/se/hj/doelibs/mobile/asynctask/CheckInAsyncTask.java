package se.hj.doelibs.mobile.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import se.hj.doelibs.api.LoanableDao;
import se.hj.doelibs.mobile.utils.CurrentUserUtils;

/**
 * task for checking a loanable back in
 *
 * @author Christoph
 */
public class CheckInAsyncTask extends AsyncTask<Void, Void, Boolean> {

	private Context context;
	private int loanableId;
	private TaskCallback<Boolean> callback;

	public CheckInAsyncTask(Context context, int loanableId, TaskCallback<Boolean> callback) {
		this.context = context;
		this.loanableId = loanableId;
		this.callback = callback;
	}

	@Override
	protected Boolean doInBackground(Void... voids) {
		LoanableDao loanableDao = new LoanableDao(CurrentUserUtils.getCredentials(context));
		return loanableDao.checkInByLoanableId(loanableId);
	}

	@Override
	protected void onPostExecute(Boolean checkInSuccess) {
		callback.onTaskCompleted(checkInSuccess);
	}

	@Override
	protected void onPreExecute() {
		callback.beforeTaskRun();
	}
}