package se.hj.doelibs.mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;
import org.apache.http.auth.UsernamePasswordCredentials;
import se.hj.doelibs.mobile.codes.PreferencesKeys;

/**
 * Helperclass to deal with the current user
 *
 * @author Christoph
 */
public class CurrentUserUtils {

	/**
	 * returns the users credentials if he is logged in. otherwise null
	 * @param context
	 * @return
	 */
	public static UsernamePasswordCredentials getCredentials(Context context) {
		UsernamePasswordCredentials credentials = null;

		SharedPreferences pref = context.getSharedPreferences(PreferencesKeys.NAME_MAIN_SETTINGS, Context.MODE_PRIVATE);

		if(pref.contains(PreferencesKeys.KEY_USER_USERNAME) && pref.contains(PreferencesKeys.KEY_USER_PASSWORD)) {
			credentials = new UsernamePasswordCredentials(
					pref.getString(PreferencesKeys.KEY_USER_USERNAME, ""),
					pref.getString(PreferencesKeys.KEY_USER_PASSWORD, ""));
		}

		return credentials;
	}

}
