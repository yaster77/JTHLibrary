package se.hj.doelibs.api;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.hj.doelibs.model.Notification;
import se.hj.doelibs.model.Notification.NotificationType;
import android.os.AsyncTask;
import android.preference.PreferenceActivity.Header;
import android.util.Log;

public class NotificationDao extends BaseDao<NotificationDao>{
	
	private static final String TAG = "NotificationDao";

	public NotificationDao(UsernamePasswordCredentials credentials) {
		super(credentials);
	}

	@Override
	public NotificationDao getById(int id) throws HttpException {
		return null;
	}
	
	public ArrayList<Notification> getUnreadNotifications(){
		ArrayList<Notification> list = new ArrayList<Notification>();
		
		try {
			HttpResponse response = get("/Notification?read=false");
			
			try {
				checkResponse(response);
				String responseString = getResponseAsString(response);
				
				JSONArray result;
				try {
					result = new JSONArray(responseString);
					
					int resultLength = result.length();
					
					JSONObject object;
					
					for(int i=0; i<resultLength; i++ ) {
						object = (JSONObject) result.get(i);
						list.add(parseFromJSON(object));
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			} catch (HttpException e) {
				Log.d(TAG, e.toString());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		return list;
	}
	
	private Notification parseFromJSON(JSONObject object) throws JSONException {
		
		Notification n = new Notification();
		n.setMessage(object.getString("Message"));
		n.setNotificationId(object.getInt("NotificationId"));
		n.setRead(object.getBoolean("Read"));
		n.setType(NotificationType.getType(object.getInt("Type")));
		
		return n;
	}
	
	public void markNotificationAsRead(Notification notification) {
		
		new AsyncTask<Notification, Void, Void>() {
			
			@Override
			protected Void doInBackground(Notification... params) {
				
				if(params.length == 1) {
					try {
						HttpResponse m = put("/notification/" + params[0].getNotificationId() + "?read=true", null);
						checkResponse(m);
					}
					catch(IOException e) {
						e.printStackTrace();
					} catch (HttpException e) {
						e.printStackTrace();
					}					
				}
				
				return null;
			}
		}.execute(notification);
		
	}
	
}
