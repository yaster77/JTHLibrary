package se.hj.doelibs;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Notification service
 * 
 * @author Adrien SAUNIER
 */
public class NotificationThread extends Thread{

	private static String TAG = "NotificationThread";
	private static int INTERVAL_CHECK = 5000;
	private Handler handler;
	private Runnable notificationStatusChecker;
	
	public NotificationThread() {
		this.handler = new Handler();
		Log.d(TAG, "Thread created.");
	}
	
	 @Override
	public void run() {
		 this.notificationStatusChecker = new Runnable() {
			@Override
			public void run() {

				
				
				// Waiting for a delay before checking notifications again
				handler.postDelayed(notificationStatusChecker, INTERVAL_CHECK);
			}
		};
		
		this.notificationStatusChecker.run();
	}
	
}
