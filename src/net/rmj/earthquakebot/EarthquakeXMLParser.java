package net.rmj.earthquakebot;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.util.Log;

public class EarthquakeXMLParser {

	private Context context;
	
	public EarthquakeXMLParser(Context context){
		this.context=context;
	}
	
	public List<EarthquakeBot> getResult() {
		
		List<EarthquakeBot> list=null;
		
		//
		//EarthquakeDataSsource eds=new EarthquakeDataSource(context);
		EarthquakeBotXMLHandler myXMLHandler = new EarthquakeBotXMLHandler(context);
		//if(eds.isNeedUpdate()){
		//	sSystem.out.println("Have new Data to update......");
		//	eds.refreshData();
			
		//}else{
		//	System.out.println("NOnew Data to update");
		//	myXMLHandler.setIsNeedUpdateData(false);
		//}
		
		URL url;
		try {
			// SAXPArserFactory .//
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			url = new URL(EarthquakeBotUtil.BOT_TWEET_XML);
			XMLReader xr = sp.getXMLReader();
			
			xr.setContentHandler(myXMLHandler);
			Log.i("executed here","XMLReader");
			
			//InputStream is=context.openFileInput(EarthquakeBotUtil.BOT_TWEET_XML);
			xr.parse(new InputSource(url.openStream()));
			
	        list= myXMLHandler.getNewestData();
	        
		} catch (Exception e) {
			//
			e.printStackTrace();
			Log.e("ParseError", "EarthquakeQueryError", e);
		} 
		
		return list; 
		
	}
	
	
}
