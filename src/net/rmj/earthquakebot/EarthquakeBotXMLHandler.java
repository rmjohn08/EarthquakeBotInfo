package net.rmj.earthquakebot;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.util.Log;

public class EarthquakeBotXMLHandler extends DefaultHandler {


	private boolean entry=false;
	private boolean statuses = false; //<== entry point
	private boolean status = false;
	private boolean created = false;
	private boolean id = false;
	private boolean text=false;
	private boolean source=false;
	
	private String quakeText;
	private String quakeSource;
	private String quakeCreated;
	private Date dateQuakeCreated;
	private String quakeId;
	private boolean user=false;
	
		
	//Earthquake data
	private EarthquakeBot ebot;
    //private EarthquakeDAO medDAO;
    //private boolean isNeedUpdateDB=true;
    
    private List<EarthquakeBot> list;
    Format formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");

	private Context context;
	
    public EarthquakeBotXMLHandler(Context context){
    	this.context=context;
    }

	@Override
	public void startDocument() throws SAXException {
		//
		this.list=new ArrayList<EarthquakeBot>();
	}

	@Override
	public void endDocument() throws SAXException {
		Log.i("Finished document...","EBot Document");
		/*
		Log.i("Insert med into Database.","begin to...");
		if(isNeedUpdateDB){
		    this.medDAO=new EarthquakeDAO(context);
		    
		    medDAO.insertEarthquake(med);
		    Log.i("Insert med into Database.","Success...");
		    this.medDAO.close();
		}
		*/
		
		
	}

	/*
	 * 
	 */
	public List<EarthquakeBot> getNewestData(){
		return list;
	}
	
	
	/**
	 * Gets be called on opening tags like: <tag> Can provide attribute(s), when
	 * xml was like: <tag attribute="attributeValue">
	 */
	@Override
	public void startElement(String uri, String localName, String name,
			org.xml.sax.Attributes attributes) throws SAXException {

		if (localName.equals("status")) {
			status = true;
			ebot=new EarthquakeBot();

		} else if (localName.equals("title") && this.entry) {
			// handles some <tag attribute="attributevalue"
			//title = true;

		} else if (localName.equals("created_at")) {
			created = true;
			//Log.i("start to summary", "summary");
			
		}  else if (localName.equals("id")) {
			id = true;
			//Log.i("start to summary", "summary");
			
		} else if ((localName + name).equals("point")) {
			
			//georss = true;
			// Log.i("111111 111111 111","point");
		} else if ((localName + name).equals("source")) {
			source = true;
		} else if(localName.equals("text")){
			text=true;
			
		} else if (localName.equals("user")) {
			user =true;
		}
		
	}

	/**
	 * Gets be called on closing tags like: </tag>
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("status")) {
			status = false;
			list.add(ebot);

		} else if (localName.equals("text")) {
			ebot.setDescription(this.quakeText);
			text=false;
		}	else if (localName.equals("created_at") && !user) {
			ebot.setCreated(quakeCreated);
			try {
			ebot.setDateQuakeCreated((Date)formatter.parseObject(quakeCreated));
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			created=false;
		} else if (localName.equals("source")) {
			ebot.setSource(quakeSource);
			source=false;
		} else if (localName.equals("id")) {
			ebot.setId(quakeId);
			this.id=false;
		}  else if (localName.equals("user")) {
			user=false;
		}
			/* else if (localName.equals("text")) {
		}
			title = false;

		} else if (localName.equals("summary")) {
			summary = false;
			//Log.i("start to summary", "summary");
			
		} else if ((localName + qName).equals("point")) {
			
			georss = false;
			// Log.i("111111 111111 111","point");
		} else if ((localName + qName).equals("elev")) {
			
			depth = false;
			
		}else if(localName.equals("name")){
			
			author=false;
			
		}else if(localName.equals("updated")){
			
			update=false;
		} */
	}

	/**
	 * Gets be called on the following structure: <tag>characters</tag>
	 */
	@Override
	public void characters(char ch[], int start, int length) {
		//
		String str[]=null;
		
	    if(this.created){
	        this.quakeCreated = new String(ch,start,length);
	        //try {
			// Date date = (Date) formatter.parseObject(quakeCreated);
			 
	        ////<created_at>Thu Feb 16 02:40:55 +0000 2012</created_at>
	    	////Log.i("title",new String(ch,start,length));
	        //} catch(Exception ex) {
	       // 	ex.printStackTrace();
	       // }
	    } else if (this.text) {
	    	
			this.quakeText=new String(ch,start,length);
			//Log.i("the content of summary.", new String(ch,start,length));
		} else if (this.source) {
			//Log.i("the content of geopoint.", new String(ch,start,length));
			this.quakeSource=new String(ch,start,length);
			
		} else if (this.id) {
			this.quakeId = new String(ch,start,length);
		} 
	    
	    
	    /* else if(this.depth){
			//Log.i("depth",new String(ch,start,length));
			med.setSourceDepth(new String(ch,start,length));
		}else if(this.update){
			//
			updateDate=c
		}else if(this.author){
			//
			authorName=new String(ch,start,length);
		} */
	    
	}
	
	
}
