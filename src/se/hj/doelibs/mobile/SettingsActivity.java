package se.hj.doelibs.mobile;

import java.util.Arrays;

import se.hj.doelibs.mobile.codes.PreferencesKeys;
import se.hj.doelibs.mobile.listener.LanguageSettingListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends BaseActivity {

	private Button logoutBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
	    super.onCreate(savedInstanceState);
	    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View contentView = inflater.inflate(R.layout.activity_settings, null, false);
	    drawerLayout.addView(contentView, 0);

		logoutBtn = (Button)findViewById(R.id.btn_logout);

		if(getCredentials() == null) {
			logoutBtn.setVisibility(View.GONE);
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

		Toast.makeText(this, "you are logged out now", Toast.LENGTH_SHORT).show();
		logoutBtn.setVisibility(View.GONE);
	}
}
