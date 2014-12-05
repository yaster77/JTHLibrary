package se.hj.doelibs.mobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import org.apache.http.HttpException;
import org.apache.http.auth.UsernamePasswordCredentials;
import se.hj.doelibs.api.UserDao;
import se.hj.doelibs.mobile.codes.PreferencesKeys;
import se.hj.doelibs.mobile.listener.LanguageSettingListener;
import se.hj.doelibs.mobile.utils.ConnectionUtils;
import se.hj.doelibs.mobile.utils.CurrentUserUtils;
import se.hj.doelibs.model.User;

import java.util.Arrays;

public class SettingsActivity extends BaseActivity {

	private EditText txtUsername;
	private EditText txtPassword;
	private Button btnLogin;
	private Button btnLogout;
	private TextView tvLoggedInAs;
	private LinearLayout loginPannel;
	private LinearLayout userDetailsPannel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
	    super.onCreate(savedInstanceState);
	    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View contentView = inflater.inflate(R.layout.activity_settings, null, false);
	    drawerLayout.addView(contentView, 0);

		userDetailsPannel = (LinearLayout)findViewById(R.id.settings_user_pannel);
		btnLogout = (Button)findViewById(R.id.btn_logout);
		txtUsername = (EditText)findViewById(R.id.login_username);
		txtPassword = (EditText)findViewById(R.id.login_password);
		loginPannel = (LinearLayout)findViewById(R.id.settings_login_pannel);
		btnLogin = (Button)findViewById(R.id.btn_login);
		tvLoggedInAs = (TextView)findViewById(R.id.settings_tv_loggedin_as);

		//depending of the loginstatus of the current user show him the login/logout panel
		if(getCredentials() == null) {
			loginPannel.setVisibility(View.VISIBLE);
			userDetailsPannel.setVisibility(View.GONE);

			//if user is not connected --> show message and disable login button
			if(!ConnectionUtils.isConnected(this)) {
				btnLogin.setEnabled(false);
				new AlertDialog.Builder(this).setMessage(R.string.login_error_internet_connection).show();
			}
		} else {
			CurrentUserUtils.UserModel um = CurrentUserUtils.getCurrentUser(this);
			tvLoggedInAs.setText(String.format(getResources().getString(R.string.loggedin_as), um.getFirstName(), um.getLastName(), um.geteMail()));

			loginPannel.setVisibility(View.GONE);
			userDetailsPannel.setVisibility(View.VISIBLE);
		}
		
		// Creating and filing the Spinner
		Spinner spinner = (Spinner) findViewById(R.id.language_spinner);
		
		LanguageSettingListener languageListener = new LanguageSettingListener(this);
		
		// Creating an ArrayAdapter
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(languageListener);
		
		// Setting the default value
		SharedPreferences prefs =  getApplicationContext().getSharedPreferences(PreferencesKeys.NAME_TMP_VALUES, MODE_PRIVATE);
		Resources res = getResources();
		String[] codes = res.getStringArray(R.array.languages_code);
		
		int position = Arrays.asList(codes).indexOf(prefs.getString("application_language", ""));
		spinner.setSelection(position);
	}

	public void onLogout(View view) {
		Log.i("Settings", "Logout");

		SharedPreferences sharedPreferences = getSharedPreferences(PreferencesKeys.NAME_MAIN_SETTINGS, MODE_PRIVATE);

		if(sharedPreferences.contains(PreferencesKeys.KEY_USER_USERNAME)) {
			sharedPreferences.edit().remove(PreferencesKeys.KEY_USER_USERNAME).commit();
		}
		if(sharedPreferences.contains(PreferencesKeys.KEY_USER_PASSWORD)) {
			sharedPreferences.edit().remove(PreferencesKeys.KEY_USER_PASSWORD).commit();
		}
		if(sharedPreferences.contains(PreferencesKeys.KEY_USER_FIRSTNAME)) {
			sharedPreferences.edit().remove(PreferencesKeys.KEY_USER_FIRSTNAME).commit();
		}
		if(sharedPreferences.contains(PreferencesKeys.KEY_USER_LASTNAME)) {
			sharedPreferences.edit().remove(PreferencesKeys.KEY_USER_LASTNAME).commit();
		}

		Toast.makeText(this, R.string.logged_out_now, Toast.LENGTH_SHORT).show();

		loginPannel.setVisibility(View.VISIBLE);
		userDetailsPannel.setVisibility(View.GONE);
	}

	public void onLogin(View view) {
		//at a login we do a simple request to a page which requires a logged in user and check the statuscodes

		String username = txtUsername.getText().toString();
		String password = txtPassword.getText().toString();

		final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);

		//make request in another thread
		final ProgressDialog dialog = new ProgressDialog(this);
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... voids) {
				UserDao userDao = new UserDao(credentials);
				Boolean loginSuccessFull = true;
				User user = null;

				try {
					user = userDao.getCurrentLoggedin(); //would throw an exception with invalid credentials

					//save credentials
					SharedPreferences.Editor editor = getSharedPreferences(PreferencesKeys.NAME_MAIN_SETTINGS, MODE_PRIVATE).edit();
					editor.putString(PreferencesKeys.KEY_USER_USERNAME, credentials.getUserName());
					editor.putString(PreferencesKeys.KEY_USER_PASSWORD, credentials.getPassword());
					editor.putString(PreferencesKeys.KEY_USER_FIRSTNAME, user.getFirstName());
					editor.putString(PreferencesKeys.KEY_USER_LASTNAME, user.getLastName());

					editor.commit();
				} catch (HttpException e) {
					Log.d("Login", "Login failed", e);
					loginSuccessFull = false;
				}

				return loginSuccessFull;
			}

			@Override
			protected void onPostExecute(Boolean loginSuccessfull) {
				dialog.dismiss();

				if(loginSuccessfull) {
					Intent myLoanActivity = new Intent(SettingsActivity.this, MyLoansListFragment.class);
					startActivity(myLoanActivity);
				} else {
					txtPassword.setText("");
					Toast.makeText(SettingsActivity.this, getResources().getText(R.string.login_error_credentials), Toast.LENGTH_LONG).show();
				}
			}

			@Override
			protected void onPreExecute() {
				dialog.setMessage(getResources().getText(R.string.login_checking_credentials));
				dialog.setCancelable(false);
				dialog.show();
			}
		}.execute();
	}
}
