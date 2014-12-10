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

	/**
	 * returns more details about the current logged in user
	 * @param context
	 * @return
	 */
	public static UserModel getCurrentUser(Context context) {
		UserModel um = null;

		SharedPreferences pref = context.getSharedPreferences(PreferencesKeys.NAME_MAIN_SETTINGS, Context.MODE_PRIVATE);

		if(pref.contains(PreferencesKeys.KEY_USER_USERNAME)
				&& pref.contains(PreferencesKeys.KEY_USER_PASSWORD)
				&& pref.contains(PreferencesKeys.KEY_USER_USERNAME)) {
			um = new UserModel();

			um.firstName = pref.getString(PreferencesKeys.KEY_USER_FIRSTNAME, "");
			um.lastName = pref.getString(PreferencesKeys.KEY_USER_LASTNAME, "");
			um.eMail = pref.getString(PreferencesKeys.KEY_USER_USERNAME, "");
			um.isAdmin = pref.getBoolean(PreferencesKeys.KEY_USER_IS_ADMIN, false);
		}

		return um;
	}

	/**
	 * this class provides the information from the user we save from him, exept the password
	 */
	public static class UserModel {
		private String firstName;
		private String lastName;
		private String eMail;
		private boolean isAdmin;

		public boolean isAdmin() { return isAdmin; }

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public String geteMail() {
			return eMail;
		}
	}

}
