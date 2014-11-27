package se.hj.doelibs.mobile.utils;

import android.app.ProgressDialog;
import android.util.Log;

/**
 * utils for the progressDialog
 * @author Christoph
 */
public class ProgressDialogUtils {

	/**
	 * dismisses a ProgressDialog quitely.
	 * Sometimes it happens that (randomly) an IllegalArgument expection is thrown on a dismiss.
	 *
	 * http://stackoverflow.com/questions/2745061/java-lang-illegalargumentexception-view-not-attached-to-window-manager
	 * http://stackoverflow.com/questions/19538282/view-not-attached-to-window-manager-dialog-dismiss
	 *
	 * @param dialog
	 */
	public static void dismissQuitely(ProgressDialog dialog) {
		try {
			dialog.dismiss();
		} catch (IllegalArgumentException e) {
			//nothing
			Log.v(ProgressDialogUtils.class.getName(), "dialog dismiss threw an InvalidArgumentExpection");
		}
	}

}
