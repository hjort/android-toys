package co.hjort.apps.meeting.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Meeting {

	private long id;
	private Date timestamp;
	private String title;
	private String place;

	public Meeting(long id, Date timestamp, String title, String place) {
		this.id = id;
		this.timestamp = timestamp;
		this.title = title;
		this.place = place;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getDateTime() {
		if (getTimestamp() == null)
			return "";
		
		DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return fmt.format(getTimestamp());
	}
	
}
