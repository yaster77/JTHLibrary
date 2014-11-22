package se.hj.doelibs.mobile.listener;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;
import se.hj.doelibs.api.LoanableDao;
import se.hj.doelibs.mobile.R;
import se.hj.doelibs.mobile.TitleDetailsActivity;
import se.hj.doelibs.mobile.asynctask.TaskCallback;
import se.hj.doelibs.mobile.codes.ExtraKeys;
import se.hj.doelibs.mobile.utils.CurrentUserUtils;
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

	public LoanableCheckOutOnClickListener(int titleId, int loanableId, Context context) {
		this.loanableId = loanableId;
		this.context = context;
		this.titleId = titleId;
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
		final ProgressDialog progressDialog = new ProgressDialog(context);

		new CheckOutAsyncTask(this.loanableId, new TaskCallback<Loan>() {
			@Override
			public void onTaskCompleted(Loan loan) {
				progressDialog.hide();

				if(loan != null) {
					Toast.makeText(context, context.getResources().getText(R.string.loanable_checkout_successfull), Toast.LENGTH_SHORT).show();
					//reload title activity
					Intent titleActivity = new Intent(context, TitleDetailsActivity.class);
					titleActivity.putExtra(ExtraKeys.TITLE_ID, titleId);
					context.startActivity(titleActivity);
				} else {
					Toast.makeText(context, context.getResources().getText(R.string.loanable_checkout_error), Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void beforeTaskRun() {
				progressDialog.setMessage(context.getResources().getText(R.string.dialog_progress_checkout_loanable));
				progressDialog.setCancelable(false);
				progressDialog.show();
			}
		}).execute();
	}

	/**
	 * runs the checkout progress
	 */
	private class CheckOutAsyncTask extends AsyncTask<Void, Void, Loan> {

		private int loanableId;
		private TaskCallback<Loan> callback;

		public CheckOutAsyncTask(int loanableId, TaskCallback<Loan> callback) {
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
}
