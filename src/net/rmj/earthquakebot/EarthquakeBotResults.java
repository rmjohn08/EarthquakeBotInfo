package net.rmj.earthquakebot;

import java.io.Serializable;
import java.util.List;

public class EarthquakeBotResults implements Serializable {
	private List<String> foundPlaces;
	private List<String> earthquakes;
	public List<String> getFoundPlaces() {
		return foundPlaces;
	}
	public void setFoundPlaces(List<String> foundPlaces) {
		this.foundPlaces = foundPlaces;
	}
	public List<String> getEarthquakes() {
		return earthquakes;
	}
	public void setEarthquakes(List<String> earthquakes) {
		this.earthquakes = earthquakes;
	}
	
	
}
