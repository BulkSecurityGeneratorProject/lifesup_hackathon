package fi.lifesup.hackathon.service.dto;

import java.time.ZonedDateTime;

public class ChallengeTimeDTO {
	private ZonedDateTime timeServer;
	
	public ChallengeTimeDTO() {
		// TODO Auto-generated constructor stub
	}

	public ChallengeTimeDTO(ZonedDateTime timeServer) {
		super();
		this.timeServer = timeServer;
	}

	public ZonedDateTime getTimeServer() {
		return timeServer;
	}

	public void setTimeServer(ZonedDateTime timeServer) {
		this.timeServer = timeServer;
	}
	
}
