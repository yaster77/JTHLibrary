	package se.hj.doelibs.mobile;

import se.hj.doelibs.LanguageManager;
import se.hj.doelibs.NotificationService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import se.hj.doelibs.mobile.utils.CurrentUserUtils;

/**
 * @author Adrien SAUNIER
 * Entry class for the application. This screen initializes the settings.
 */
public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		LanguageManager.initLanguagePreferences(this.getApplicationContext());		
		Intent i = new Intent(this, NotificationService.class);
		startService(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {
	   return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onStart() {		
		super.onStart();
		
		/* New Handler to start the Menu-Activity 
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

            	/* Create an Intent that will start the Main-Activity. */
				Intent mainIntent = null;

				//if user is not logged in, goto search activity otherwise goto MyLoans
				if(CurrentUserUtils.getCurrentUser(SplashScreenActivity.this) == null) {
					mainIntent = new Intent(SplashScreenActivity.this, SearchActivity.class);
				} else {
					mainIntent = new Intent(SplashScreenActivity.this, MyLoansActivity.class);
				}

				SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, 2000);
	}
}
