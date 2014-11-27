package se.hj.doelibs.mobile.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Utils for working with network access
 *
 * @author Christoph
 */
public class ConnectionUtils {

	/**
	 * returns if the device is connected to the internet
	 *
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		return netInfo != null && netInfo.isConnected();
	}

}
