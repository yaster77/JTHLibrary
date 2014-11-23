package se.hj.doelibs.mobile.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import se.hj.doelibs.mobile.R;
import se.hj.doelibs.mobile.asynctask.CheckInAsyncTask;
import se.hj.doelibs.mobile.asynctask.TaskCallback;

/**
 * @author Christoph
 */
public class LoanableCheckInOnClickListener implements View.OnClickListener {

	private int titleId;
	private int loanableId;
	private Context context;
	private TaskCallback<Boolean> checkInCallback;

	public LoanableCheckInOnClickListener(int titleId, int loanableId, Context context, TaskCallback<Boolean> checkInCallback) {
		this.titleId = titleId;
		this.loanableId = loanableId;
		this.context = context;
		this.checkInCallback = checkInCallback;
	}

	@Override
	public void onClick(final View v) {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
		dialogBuilder
				.setMessage(R.string.dialog_really_checkin_loanable)
				.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						checkInLoanable();
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
	 * checks the loanable in again
	 */
	private void checkInLoanable() {
		new CheckInAsyncTask(context, loanableId, checkInCallback).execute();
	}

}
