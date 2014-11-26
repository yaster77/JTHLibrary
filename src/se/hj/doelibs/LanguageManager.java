package se.hj.doelibs;

import java.util.Locale;

import se.hj.doelibs.mobile.codes.PreferencesKeys;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.widget.Toast;


public class LanguageManager {

	/**
	 * Set the application language
	 * @param context 
	 * @param code
	 */
	public static void setApplicationLanguage(Context context, String code) {
		Resources res = context.getResources();
		Configuration androidConfiguration = res.getConfiguration();
		
		androidConfiguration.locale = new Locale(code);
		res.updateConfiguration(androidConfiguration, res.getDisplayMetrics());
	}
	
	public static void initLanguagePreferences(Context context) {
		
		String languageCode = LanguageManager.getPreferedLanguage(context);
		
		if( ! languageCode.isEmpty())
			LanguageManager.setApplicationLanguage(context, languageCode);
	}
	
	public static void setPreferedLanguage(Context context, String code) {
		SharedPreferences prefs = context.getSharedPreferences(PreferencesKeys.NAME_TMP_VALUES, Activity.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putString("application_language", code);
		prefEditor.commit();
	}
	
	public static String getPreferedLanguage(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PreferencesKeys.NAME_TMP_VALUES, Activity.MODE_PRIVATE);
		return prefs.getString("application_language", "");
	}
		
}
