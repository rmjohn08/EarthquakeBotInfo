package net.rmj.earthquakebot;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AndroidEarthquakeBotPreferences extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.preferences_app);
    	//bindService();
		
		// Spit out the current preferences
        SharedPreferences settingsActivity = getSharedPreferences(EarthquakeBotUtil.PREFERENCE_FILE, MODE_PRIVATE); 
       // SharedPreferences settingsActivity = getPreferences(MODE_PRIVATE); 
		//SharedPreferences settingsActivity = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

		EditText phours = (EditText)findViewById(R.id.prefPastHours);
        phours.setText(String.valueOf(settingsActivity.getInt(EarthquakeBotUtil.PREFERENCE_HOURS, 24)));
        
        EditText pplaces = (EditText)findViewById(R.id.prefWatchPlaces);
        pplaces.setText(String.valueOf(settingsActivity.getString(EarthquakeBotUtil.PREFERENCE_PLACES, "")));
        
        Button button = (Button)this.findViewById(R.id.ButtonGo);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final EditText prefHours = (EditText)findViewById(R.id.prefPastHours);
        		final EditText prefPlaces = (EditText)findViewById(R.id.prefWatchPlaces);
        		
        		// Get the application settings and open the editor
                SharedPreferences settings = getSharedPreferences(EarthquakeBotUtil.PREFERENCE_FILE, MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = settings.edit();
                
                String strHours = prefHours.getText().toString();
                String strPlaces = prefPlaces.getText().toString();
                               
                // Add the preference and commit the changes
                prefEditor.putInt(EarthquakeBotUtil.PREFERENCE_HOURS, Integer.parseInt(strHours));
                prefEditor.putString(EarthquakeBotUtil.PREFERENCE_PLACES, strPlaces);
                prefEditor.commit();
                
                // Go to the Main screen
            	//Intent intent = new Intent(AndroidEarthquakeBotPreferences.this ,AndroidEarthquakeBotActivity.class);
       			//startActivity(intent);
                Toast.makeText(AndroidEarthquakeBotPreferences.this, "Preferences changed...",
	                    Toast.LENGTH_SHORT).show();
                
			}
		});
	}
}
