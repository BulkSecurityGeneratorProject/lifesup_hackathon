package fi.lifesup.hackathon.searchCriteria;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import fi.lifesup.hackathon.domain.enumeration.ChallengeStatus;

public class ChallengeSearch {
	private String name;
	private String eventStartTime;
	private String eventEndTime;
	private String applicationCloseDate;
	private ChallengeStatus status;
	
	public ChallengeSearch() {
		super();
	}

	public String getName() {
		if(name == null)
			name = "";
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public ZonedDateTime getEventStartTime() {
		return ZonedDateTime.parse(eventStartTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault()));
	}

	public void setEventStartTime(String eventStartTime) {
		this.eventStartTime = eventStartTime;
	}

	public ZonedDateTime getApplicationCloseDate() {
		return ZonedDateTime.parse(applicationCloseDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault()));
				
	}

	public void setApplicationCloseDate(String applicationCloseDate) {
		this.applicationCloseDate = applicationCloseDate;
	}

	public ZonedDateTime getEventEndTime() {
		return ZonedDateTime.parse(eventEndTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault()));
	}

	public void setEventEndTime(String eventEndTime) {
		this.eventEndTime = eventEndTime;
	}

	public ChallengeStatus getStatus() {
		return status;
	}

	public void setStatus(ChallengeStatus status) {
		this.status = status;
	}
	
	
}
