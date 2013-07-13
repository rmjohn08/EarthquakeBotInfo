package net.rmj.earthquakebot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.EditText;

public class EarthquakeBotUtil {
	
	public static String PREFERENCE_HOURS= "PAST_HOURS";
	public static String PREFERENCE_PLACES="WATCH_PLACES";
	public static String PREFERENCE_FILE = "EarthquakeBotPreferences";

	public static String BOT_TWEET_XML = "http://10.0.2.2/user_timeline.xml";
			//"https://api.twitter.com/1/statuses/user_timeline.xml?screen_name=earthquakeBot";
			//
			//
			//"https://api.twitter.com/1/statuses/user_timeline.xml?screen_name=earthquakeBot";
			//
	public static String BOT_TWEET_JSON = "https://api.twitter.com/1/statuses/user_timeline.json?screen_name=earthquakeBot";
	public static int hours=48;  //<== this could be obtained from a PreferencesSettings
	
	//protected static String[] places = {"Nepal","Brazil","Tonga"};  //<== this could be obtained from a PreferencesSettings 

	
	public static void getQuakesInLastHours(List<EarthquakeBot> bots,String placeWatch, EarthquakeBotResults eResults) {
		getQuakesInLastHours(hours,bots,placeWatch,eResults);
		
	}	
	
	private static String[] setPlacesToWatch(String placeWatch) {
		
		String[] places=  {""};
		if (placeWatch!=null && !placeWatch.equals("")) {
			StringTokenizer tok = new StringTokenizer(placeWatch,",");
			List<String> l = new ArrayList<String>();
			while (tok.hasMoreElements()) {
				l.add(tok.nextToken().toString());
			}
			if (!l.isEmpty()) {
				//Object[] o = l.toArray();
				places = new String[l.size()];// (String[])o;
				int idx=0;
				for (String s : l) {
					places[idx++]=s;
				}
			}
			
		}
		return places;
	}
	
	
	
	public static void getQuakesInLastHours(int hours, List<EarthquakeBot> bots,String placeWatch, EarthquakeBotResults eResults) {
		List<String> found = new ArrayList<String>();
		List<String> tweets = new ArrayList<String>();
		
		String[] places=setPlacesToWatch(placeWatch);
		
		//Calendar cal = Calendar.getInstance();
    	Date now = new Date();
    	
    	for (EarthquakeBot bot : bots) {
    		if (bot.getDateQuakeCreated()==null) continue;
    		
    		long diff = (now.getTime() - bot.getDateQuakeCreated().getTime());
    		if ((diff/(1000*60*60))>hours) continue;
    		tweets.add(bot.getDescription());
    		
    		if (places!=null) {
	    		for (String place : places) {
	    			if (bot.getDescription().toUpperCase().indexOf(place.toUpperCase())>0) {
	    				if (!found.contains(place)) {
	    					found.add(place);
	    					
	    				}
	    				
	    			}
	    		}
    		}
    		
    	}   
    	eResults.setEarthquakes(tweets);
    	eResults.setFoundPlaces(found);
    	
	}
}
