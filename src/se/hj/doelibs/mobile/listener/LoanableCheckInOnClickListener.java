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

/**
 * @author Christoph
 */
public class LoanableCheckInOnClickListener implements View.OnClickListener {

	private int loanableId;
	private ProgressDialog processDialog;
	private Context context;

	public LoanableCheckInOnClickListener(int loanableId, Context context) {
		this.loanableId = loanableId;
		this.context = context;
		processDialog = new ProgressDialog(context);
	}

	@Override
	public void onClick(final View v) {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
		dialogBuilder
				.setMessage(R.string.dialog_really_checkin_loanable)
				.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						checkInLoanable(v);
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
	 * @param view
	 */
	private void checkInLoanable(View view) {
		final Button btn = (Button)view.findViewById(R.id.btn_titledetails_loanable_checkInOrOut);

		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... voids) {
				LoanableDao loanableDao = new LoanableDao(CurrentUserUtils.getCredentials(context));
				return loanableDao.checkInByLoanableId(loanableId);
			}

			@Override
			protected void onPostExecute(Boolean checkInSuccess) {
				processDialog.hide();

				if(checkInSuccess) {
					btn.setText(R.string.btn_check_out);
					btn.setOnClickListener(new LoanableCheckOutOnClickListener(loanableId, context));
				} else {
					Toast.makeText(context, context.getResources().getText(R.string.loanable_checkin_error), Toast.LENGTH_LONG).show();
				}
			}

			@Override
			protected void onPreExecute() {
				processDialog.setMessage(context.getResources().getText(R.string.dialog_progress_checkin_loanable));
				processDialog.setCancelable(false);
				processDialog.show();
			}
		}.execute();
	}

}
