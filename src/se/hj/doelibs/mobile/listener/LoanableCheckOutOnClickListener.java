package se.hj.doelibs.mobile.listener;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import se.hj.doelibs.api.LoanableDao;
import se.hj.doelibs.mobile.R;
import se.hj.doelibs.mobile.utils.CurrentUserUtils;
import se.hj.doelibs.model.Loan;

/**
 * onclick listener for a checkout button. Checks a loanable out and changes the text of the button again to checkIn
 *
 * @author Christoph
 */
public class LoanableCheckOutOnClickListener implements View.OnClickListener {

	private int loanableId;
	private ProgressDialog processDialog;
	private Context context;

	public LoanableCheckOutOnClickListener(int loanableId, Context context) {
		this.loanableId = loanableId;
		this.context = context;
		processDialog = new ProgressDialog(context);
	}

	@Override
	public void onClick(final View v) {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
		dialogBuilder
				.setMessage(R.string.dialog_really_checkout_loanable)
				.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						checkOutLoanable(v);
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
	private void checkOutLoanable(View view) {
		final Button btn = (Button)view.findViewById(R.id.btn_titledetails_loanable_checkInOrOut);

		new AsyncTask<Void, Void, Loan>() {
			@Override
			protected Loan doInBackground(Void... voids) {
				LoanableDao loanableDao = new LoanableDao(CurrentUserUtils.getCredentials(context));
				return loanableDao.checkOut(loanableId);
			}

			@Override
			protected void onPostExecute(Loan loan) {
				processDialog.hide();

				if(loan != null) {
					btn.setText(R.string.btn_check_in);
					btn.setOnClickListener(new LoanableCheckInOnClickListener(loanableId, context));
				} else {
					Toast.makeText(context, context.getResources().getText(R.string.loanable_checkout_error), Toast.LENGTH_LONG).show();
				}
			}

			@Override
			protected void onPreExecute() {
				processDialog.setMessage(context.getResources().getText(R.string.dialog_progress_checkout_loanable));
				processDialog.setCancelable(false);
				processDialog.show();
			}
		}.execute();
	}
}
