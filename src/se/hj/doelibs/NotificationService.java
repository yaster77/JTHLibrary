package se.hj.doelibs;

import java.util.ArrayList;

import se.hj.doelibs.api.NotificationDao;
import se.hj.doelibs.mobile.R;
import se.hj.doelibs.mobile.codes.PreferencesKeys;
import se.hj.doelibs.mobile.utils.CurrentUserUtils;
import se.hj.doelibs.model.Notification;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

/**
 * This particular class is extending the Service class, and will run on background
 * the process to pull the notifications from the API and create a notification.
 * 
 * Specific cases:
 * 	- No network connection
 *  - No access to server or short timeout
 *  - The application is on focus
 * 
 * @author Adrien SAUNIER
 */
public class NotificationService extends IntentService {
	
	private final static String TAG = "NotificationService";
	private static int INTERVAL_CHECK = 5000;
	private static int DISABLE_INTERVAL_CHECK = 5000;
	private Handler handler;
	private Runnable notificationChecker;
	private Runnable notificationStatusChecker;
	private SharedPreferences sharedpreferences;
	
	public NotificationService() {
		super(TAG);
		this.handler = new Handler();
	}
	
	@Override	
	protected void onHandleIntent(Intent intent) {
		Log.i(TAG, "NotificationService started");
		
		this.sharedpreferences = getSharedPreferences(PreferencesKeys.NAME_MAIN_SETTINGS, Context.MODE_PRIVATE);
		
		// Check notifications
		this.notificationChecker = new Runnable() {
			@Override
			public void run() {
				
				new AsyncTask<String, Void, ArrayList<Notification>>() {
					
					NotificationDao dao;

					@Override
					protected ArrayList<Notification> doInBackground(String... params) {
						this.dao = new NotificationDao(CurrentUserUtils.getCredentials(getApplicationContext()));
						return dao.getUnreadNotifications();
					}
					
					@Override
					protected void onPostExecute(ArrayList<Notification> list) {
						if(list.size() > 0) {
							for(int i=0; i<list.size(); i++) {
								NotificationCompat.Builder nBuilder = new Builder(getApplicationContext())
									.setContentText(list.get(i).getMessage())
									.setSmallIcon(R.drawable.bookshelf)
									.setContentTitle("DoeLibs")
									.setStyle(new NotificationCompat.BigTextStyle().bigText(list.get(i).getMessage()));
							
								NotificationManager mNotificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
								mNotificationManager.notify(10, nBuilder.build());
								
								this.dao.markNotificationAsRead(list.get(i));
							}
						}
					
						if(sharedpreferences.getBoolean(PreferencesKeys.KEY_NOTIFICATIONS, false) == true)
							handler.postDelayed(notificationChecker, INTERVAL_CHECK);
						else
							handler.postDelayed(notificationStatusChecker, DISABLE_INTERVAL_CHECK);
					}
				}.execute();
			}
		};
		
		// Wait for the notification activation
		this.notificationStatusChecker = new Runnable() {
			@Override
			public void run() {
				if(sharedpreferences.getBoolean(PreferencesKeys.KEY_NOTIFICATIONS, false) == true)
					handler.postDelayed(notificationChecker, INTERVAL_CHECK);
				else
					handler.postDelayed(notificationStatusChecker, DISABLE_INTERVAL_CHECK);
				
			}
		};
		
		
		this.notificationChecker.run();
	}
	
}
