package se.hj.doelibs;

import java.util.ArrayList;

import se.hj.doelibs.api.NotificationDao;
import se.hj.doelibs.mobile.utils.CurrentUserUtils;
import se.hj.doelibs.model.Notification;
import android.R;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
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
	private static int INTERVAL_CHECK = 10000;
	private Handler handler;
	private Runnable notificationStatusChecker;
	
	public NotificationService() {
		super(TAG);
		this.handler = new Handler();
	}
	
	@Override	
	protected void onHandleIntent(Intent intent) {
		Log.i(TAG, "NotificationService started");
		
		CurrentUserUtils.getCredentials(getApplicationContext());
		
		this.notificationStatusChecker = new Runnable() {
			@Override
			public void run() {
				
				NotificationDao dao = new NotificationDao(CurrentUserUtils.getCredentials(getApplicationContext()));
				ArrayList<Notification> list = dao.getUnreadNotifications();
				
				Log.d(TAG, "New notifications !! " + list.size());
				
				if(list.size() > 0) {
					for(int i=0; i<list.size(); i++) {
						NotificationCompat.Builder nBuilder = new Builder(getApplicationContext())
							.setContentText(list.get(i).getMessage())
							.setSmallIcon(R.drawable.btn_dialog)
							.setContentTitle("DoeLibs");
					
						NotificationManager mNotificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
						mNotificationManager.notify(10, nBuilder.build());
					}
				}
				
				handler.postDelayed(notificationStatusChecker, INTERVAL_CHECK);
				
			}
		};
		
		this.notificationStatusChecker.run();
	}
	
}
