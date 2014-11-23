package se.hj.doelibs.mobile.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import se.hj.doelibs.mobile.R;
import se.hj.doelibs.mobile.asynctask.CheckOutAsyncTask;
import se.hj.doelibs.mobile.asynctask.TaskCallback;
import se.hj.doelibs.model.Loan;

/**
 * onclick listener for a checkout button. Checks a loanable out and changes the text of the button again to checkIn
 *
 * @author Christoph
 */
public class LoanableCheckOutOnClickListener implements View.OnClickListener {

	private int titleId;
	private int loanableId;
	private Context context;
	private TaskCallback<Loan> checkOutCallback;

	public LoanableCheckOutOnClickListener(int titleId, int loanableId, Context context, TaskCallback<Loan> checkOutCallback) {
		this.loanableId = loanableId;
		this.context = context;
		this.titleId = titleId;
		this.checkOutCallback = checkOutCallback;
	}

	@Override
	public void onClick(final View v) {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
		dialogBuilder
				.setMessage(R.string.dialog_really_checkout_loanable)
				.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						checkOutLoanable();
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
	 * checks a loanable out
	 */
	private void checkOutLoanable() {
		new CheckOutAsyncTask(context, this.loanableId, checkOutCallback).execute();
	}
}
