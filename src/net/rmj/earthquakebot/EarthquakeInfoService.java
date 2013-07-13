package net.rmj.earthquakebot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;

public class EarthquakeInfoService extends Service {

		protected final String DEBUG_TAG=this.getClass().getName();
		private Timer timer = new Timer();
		private long DELAY = 0;
		private long UPDATE = 1000 * 60;// * 30;
		private int hours = 24;
		private String placesToWatch="";
		
		// {"San Jose","Costa Rica","Panama","David","US"};
		
		List<String> tweets;
		
		
		/* (non-Javadoc)
		 * @see android.app.sService#onBind(android.content.Intent)
		 */
		@Override
		public IBinder onBind(Intent arg0) {
			// TODO Auto-generated method stub
			
			return null;
		}
		
		@Override 
		public void onCreate() {
			  super.onCreate();
			  Log.i(DEBUG_TAG, DEBUG_TAG + " has been created");
			  //runService();
			  
			  //dont want to put it in a timer yet
			  timer.schedule(new TimerTweetBot(), 0, UPDATE);
		}

		@Override 
		public void onDestroy() {
			
			super.onDestroy();
			if (timer!=null) timer.cancel();
			Log.i(DEBUG_TAG, DEBUG_TAG +" has been destroyed and timer stopped");
		}
		
		
		/**
		 * takes care of refreshing the tweets and alerting if neccesary
		 */
		protected void refreshTweets() {
			
			//timer.scheduleAtFixedRate(new ServiceTask(), DELAY, UPDATE);
			Log.i(DEBUG_TAG, DEBUG_TAG + " service started....");
			
			EarthquakeXMLParser parser = new EarthquakeXMLParser(this);
	        List<EarthquakeBot> list = parser.getResult();
	        try {
		        if (list!=null && !list.isEmpty()) {
		        	tweets = new ArrayList<String>();
		        	EarthquakeBotResults eResults = new EarthquakeBotResults();
		        	EarthquakeBotUtil.getQuakesInLastHours(hours,list,placesToWatch, eResults);
		        	
			        if (eResults.getFoundPlaces()!=null && !eResults.getFoundPlaces().isEmpty()) {
			        	// notify here there are places found
			        	//alert the user somehow
		        		notifyUser();
		        		
		        	}
		        	
		        		
		        }
		        
	        } catch (Exception ex) {
	        	ex.printStackTrace();
	        }
		}
		
		/**
		 * timer class when the service is repeated in intervals
		 * @author ronaldo
		 *
		 */
		protected class TimerTweetBot extends TimerTask {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				SharedPreferences settings = getSharedPreferences(EarthquakeBotUtil.PREFERENCE_FILE, MODE_PRIVATE); 
		        //SharedPreferences settingsActivity = getPreferences(MODE_PRIVATE); 
		        //EditText phours = (EditText)findViewById(R.id.prefPastHours);
		        int hrs = settings.getInt(EarthquakeBotUtil.PREFERENCE_HOURS, 24);
		        String place = settings.getString(EarthquakeBotUtil.PREFERENCE_PLACES,"");
		        if (hrs>0) {
		        	hours = hrs;
		        }
		        if (place!=null && !place.equals("")) {
		        	placesToWatch = place;
		        }
					
				
				refreshTweets();
			}
			
		}
		
		
		/**
		 * binder to this service.
		 * @author ronaldo
		 *
		 */
		public class TheBinder extends Binder {
			EarthquakeInfoService getService() {
				return EarthquakeInfoService.this;
				
			}
		}
		
		/**
		 * gets the most recent tweets
		 * @return
		 */
		public List<String> getTweetsList() {
			return tweets;
			
		}
		
		public List<String> getTweetsList(boolean refresh) {
			if (refresh) refreshTweets(); 
				
			return tweets;
			
		}
		
		
		/**
		 * notifyUser with a Notification Object
		 */
		protected void notifyUser() {
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager notifManager = (NotificationManager)getSystemService(ns);
			CharSequence tickerText = "Earthquake on one of watched areas...";
			int icon = R.drawable.ic_launcher;
			long when = System.currentTimeMillis();
			Notification notification = new Notification(icon,tickerText,when);
			
			Context context = getApplicationContext();
			CharSequence contentTitle = "EarthquakeBot";
			CharSequence contentText = "Earthquake on area";
			Intent notificationIntent = new Intent(this,AndroidEarthquakeBotActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			
			notifManager.notify(1,notification);
				
		}
		
}
