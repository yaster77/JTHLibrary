package se.hj.doelibs.mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.HttpException;
import org.apache.http.auth.UsernamePasswordCredentials;
import se.hj.doelibs.api.UserDao;
import se.hj.doelibs.mobile.codes.PreferencesKeys;
import se.hj.doelibs.model.User;

/**
 * @author Christoph
 */
public class LoginActivity extends BaseActivity {
    private EditText usernameField;
    private EditText passwordField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_login, null, false);
        drawerLayout.addView(contentView, 0);

        usernameField = (EditText)findViewById(R.id.login_username);
        passwordField = (EditText)findViewById(R.id.login_password);
    }

    public void onLogin(View view) {
        //at a login we do a simple request to a page which requires a logged in user and check the statuscodes

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        Log.i("Login", "Try to login as user: " + username);

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
                    Intent myLoanActivity = new Intent(LoginActivity.this, MyLoansActivity.class);
                    startActivity(myLoanActivity);
                } else {
                    passwordField.setText("");
                    Toast.makeText(LoginActivity.this, "Could not login. Please check your credentials", Toast.LENGTH_LONG).show();
                }
			}

            @Override
            protected void onPreExecute() {
                dialog.setMessage("checking credentials...");
                dialog.setCancelable(false);
                dialog.show();
            }
		}.execute();
    }
}