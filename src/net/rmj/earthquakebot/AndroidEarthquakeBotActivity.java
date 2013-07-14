package net.rmj.earthquakebot;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;
/**
 * This an app that loads tweets from earthquakeBot into a List.  
 * todo: better use of events onCreate, onPause
 * 		 be able to add a place based on the description
 * 		 add progress bar or messaging to wait.. 
 * Dialog based on this 
 * http://www.helloandroid.com/tutorials/using-threads-and-progressdialog
 * 		
 * Originally based on: 
 * 		 follow tutorial http://www.vogella.de/articles/AndroidServices/article.html#service
 * 		 chapter 5 could be used for this.
 * http://developer.android.com/guide/topics/ui/notifiers/notifications.html should be used for notifications
 * 
 * @author Ronaldo Johnson
 * February 7, 2012. 
 */
public class AndroidEarthquakeBotActivity extends ListActivity implements Runnable{
	private EarthquakeInfoService service;
	private final int REFRESH_ID=1;
	private final int PREFERENCES_ID=2;
	
	ArrayAdapter<String> adapter;
	protected ProgressDialog pd;
	EarthquakeBotResults eResults = new EarthquakeBotResults();
	
	ArrayList<String> tweets= new ArrayList<String>();
	private int hours = 48;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("AndroidEarthquakeBot","Creating app...");
        try {
        	//setContentView(R.layout.main);
        	bindService();
        	Log.i("AndroidEarthquakeBot","Starting app...");
        	retrieveData();
        	adapter = new ArrayAdapter<String>(this,R.layout.main,R.id.earthquakeTweets,tweets);
        	setListAdapter(adapter);
        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
      
    }
    
    private void retrieveData() {
    	pd = ProgressDialog.show(this, "Retrieving data", "Please wait...",true,false);
    	Thread thread = new Thread(this);
    	thread.start();
    }
    
    /**
     * when app starts
     */
    public void onStart() {
    	super.onStart();
    	Log.i("AndroidEarthquakeBot", "Starting app...");
    	
    	
    }
    /**
     * when it comes to the foreground
     */
    public void onResume() {
    	super.onResume();
    	Log.i("AndroidEarthquakeBot","Resuming app...");
    	//tweets = new ArrayList<String>();
    	//adapter = new ArrayAdapter<String>(this,R.layout.main,R.id.earthquakeTweets,tweets);
    	//setListAdapter(adapter);
    	    	
    	//this is extra because it seems it will be always false
    	//if (service!=null) {
    	//	this.getTweetsFromService(false);
    	//} else {
    	//	//ProgressDialog pd = ProgressDialog.show(AndroidEarthquakeBotActivity.this,"Loading","Please wait");
    	//	//pd.show();
    		
    		//pd.dismiss();
    	
    }
    
    /**
     * called when the Menu key of the device is clicked
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
    	//creates the option menu
    	menu.add(0, REFRESH_ID, 0, "Refresh");
    	menu.add(0, PREFERENCES_ID, 0, "Preferences");
    	
    	return true;
    }
    
    /**
     * when the option is selected from the menu
     */
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch(item.getItemId()) {
    	case REFRESH_ID:
    		this.retrieveData();
    		break;
    	case PREFERENCES_ID:
    		// Go to the Main screen
        	Intent intent = new Intent(AndroidEarthquakeBotActivity.this ,AndroidEarthquakeBotPreferences.class);
   			startActivity(intent);
   			break;
    		
    	}
    	return true;
    }
    
    public void onDestroy() {
    	super.onDestroy();
    	serviceConnection.onServiceDisconnected(null);
    	serviceConnection=null;
    	
    }
    
    
    private ServiceConnection serviceConnection = new ServiceConnection() {
    	
    	public void onServiceConnected(ComponentName className, IBinder binder) {
    		service = ((EarthquakeInfoService.TheBinder)binder).getService();
    		Toast.makeText(AndroidEarthquakeBotActivity.this, "Connected", Toast.LENGTH_SHORT).show();
    		
    	}
    	
    	public void onServiceDisconnected(ComponentName className) {
    		service =null;
    		
    	}
    	
    };
    
    /**
     * the service connection and binding is not triggered until after  onResume, onStart.
     */
    private void bindService() {
    	bindService(new Intent(this,EarthquakeInfoService.class),serviceConnection,Context.BIND_AUTO_CREATE);
    	
    }
    
    public void getTweetsFromService(boolean refresh) {
    	if (service==null) return;
    	
    	List l = service.getTweetsList(refresh);
    	if (l!=null && !l.isEmpty()) {
    		tweets.clear(); //clear the list, DO NOT reinstantiate
    		tweets.addAll(l);
    		adapter.notifyDataSetChanged();
    	}
    	
    }

    /**
     * it queries the earthquakeBot without using the service
     */
	public void refreshTweets() {
		//I could get this directly from the service or refresh directly from the tweetBot
		//i chose to directly refresh from the tweetBot
		
        EarthquakeXMLParser parser = new EarthquakeXMLParser(this);
        List<EarthquakeBot> list = parser.getResult();
        
        SharedPreferences settingsActivity = getSharedPreferences(EarthquakeBotUtil.PREFERENCE_FILE, MODE_PRIVATE);
        		//PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //getSharedPreferences(EarthquakeBotUtil.PREFERENCE_FILE, 0);
        String placesWatch = settingsActivity.getString(EarthquakeBotUtil.PREFERENCE_PLACES, "");
        int prefHrs = settingsActivity.getInt(EarthquakeBotUtil.PREFERENCE_HOURS, 0);
        if (prefHrs>0) hours = prefHrs;
        
        tweets.clear(); //clear the list, DO NOT reinstantiate
        
        try {
	       
        	eResults = new EarthquakeBotResults();
        	EarthquakeBotUtil.getQuakesInLastHours(hours, list,placesWatch, eResults);
        	
        	if (eResults.getEarthquakes()!=null && !eResults.getEarthquakes().isEmpty()) {
        		tweets.addAll((ArrayList<String>)eResults.getEarthquakes());
        	}
        	
        	
	        
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
       
	}

	
	public void run() {
		//run it here
		refreshTweets();
		handler.sendEmptyMessage(0);
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pd.dismiss();
			//now refresh the listView
	    	adapter.notifyDataSetChanged();
			
	    	if (eResults.getFoundPlaces()!=null && !eResults.getFoundPlaces().isEmpty()) {
	        	// notify here there are places found
	            Toast.makeText(AndroidEarthquakeBotActivity.this, "Earthquake in watched areas...",
	                    Toast.LENGTH_LONG).show();
	        }
			if (tweets.isEmpty()) {
    			tweets.add("No earthquake data found. Use Refresh to try again.") ;
    			adapter.notifyDataSetChanged();
    			
    		}
		}
	};
}