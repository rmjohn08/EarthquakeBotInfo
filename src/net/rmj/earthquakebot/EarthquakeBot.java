package net.rmj.earthquakebot;

import java.io.Serializable;
import java.util.Date;

public class EarthquakeBot implements Serializable {
	private String description;
	private String created;
	private String source;
	private String id;
	private Date dateQuakeCreated;
	
	public Date getDateQuakeCreated() {
		return dateQuakeCreated;
	}
	public void setDateQuakeCreated(Date dateQuakeCreated) {
		this.dateQuakeCreated = dateQuakeCreated;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	

	
}
