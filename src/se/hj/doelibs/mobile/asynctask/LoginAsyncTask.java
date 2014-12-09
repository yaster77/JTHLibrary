package se.hj.doelibs.mobile.asynctask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpException;
import org.apache.http.auth.UsernamePasswordCredentials;
import se.hj.doelibs.api.UserDao;
import se.hj.doelibs.mobile.codes.PreferencesKeys;
import se.hj.doelibs.model.User;
import se.hj.doelibs.model.UserCategory;

/**
 * Task to do the login procedure
 *
 * @author Christoph
 */
public class LoginAsyncTask extends AsyncTask<Void, Void, Boolean> {

	private TaskCallback<Boolean> callback;
	private Context context;
	private UsernamePasswordCredentials credentials;

	public LoginAsyncTask(Context context, String username, String password, TaskCallback<Boolean> callback) {
		this.context = context;
		this.callback = callback;
		credentials = new UsernamePasswordCredentials(username, password);
	}

	@Override
	protected Boolean doInBackground(Void... voids) {
		UserDao userDao = new UserDao(credentials);
		Boolean loginSuccessFull = true;
		User user = null;

		try {
			user = userDao.getCurrentLoggedin(); //would throw an exception with invalid credentials

			//save credentials
			SharedPreferences.Editor editor = context.getSharedPreferences(PreferencesKeys.NAME_MAIN_SETTINGS, context.MODE_PRIVATE).edit();
			editor.putString(PreferencesKeys.KEY_USER_USERNAME, credentials.getUserName());
			editor.putString(PreferencesKeys.KEY_USER_PASSWORD, credentials.getPassword());
			editor.putString(PreferencesKeys.KEY_USER_FIRSTNAME, user.getFirstName());
			editor.putString(PreferencesKeys.KEY_USER_LASTNAME, user.getLastName());
			editor.putBoolean(PreferencesKeys.KEY_USER_IS_ADMIN, (user.getCategory().getCategoryId() == UserCategory.ADMIN_CATEGORY_ID));

			editor.commit();
		} catch (HttpException e) {
			Log.d("Login", "Login failed", e);
			loginSuccessFull = false;
		}

		return loginSuccessFull;
	}

	@Override
	protected void onPostExecute(Boolean loginSuccessfull) {
		callback.onTaskCompleted(loginSuccessfull);
	}

	@Override
	protected void onPreExecute() {
		callback.beforeTaskRun();
	}
}
