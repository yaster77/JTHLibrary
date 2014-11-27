package se.hj.doelibs.mobile.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import se.hj.doelibs.api.ReservationDao;
import se.hj.doelibs.mobile.utils.CurrentUserUtils;

/**
 * Task to reserve a title
 *
 * @author Christoph
 */
public class ReserveTitleAsyncTask extends AsyncTask<Void, Void, Boolean> {

	private int titleId;
	private TaskCallback<Boolean> callback;
	private Context context;

	public ReserveTitleAsyncTask(Context context, int titleId, TaskCallback<Boolean> callback) {
		this.context = context;
		this.titleId = titleId;
		this.callback = callback;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		ReservationDao reservationDao = new ReservationDao(CurrentUserUtils.getCredentials(this.context));
		return reservationDao.reserve(this.titleId);
	}

	@Override
	protected void onPostExecute(Boolean reservationSuccessfull) {
		callback.onTaskCompleted(reservationSuccessfull);
	}

	@Override
	protected void onPreExecute() {
		callback.beforeTaskRun();
	}
}