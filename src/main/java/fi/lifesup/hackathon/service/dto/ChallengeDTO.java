package fi.lifesup.hackathon.service.dto;

import java.time.ZonedDateTime;

import fi.lifesup.hackathon.domain.enumeration.ChallengeStatus;

public class ChallengeDTO {
	private Long id;
	private String name;
	private String challengeText;
	private String resource;
	private String rewards;
	private String timeline;
	private String rules;
	private String bannerUrl;
	private String additional;
	private Integer minTeamNumber;
	private Integer maxTeamNumber;
	private ZonedDateTime eventStartTime;
	private ZonedDateTime eventEndTime;
	private String location;
	private ChallengeStatus challengeStatus;
	
	public ChallengeDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChallengeText() {
		return challengeText;
	}
	public void setChallengeText(String challengeText) {
		this.challengeText = challengeText;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getRewards() {
		return rewards;
	}
	public void setRewards(String rewards) {
		this.rewards = rewards;
	}
	public String getTimeline() {
		return timeline;
	}
	public void setTimeline(String timeline) {
		this.timeline = timeline;
	}
	public String getRules() {
		return rules;
	}
	public void setRules(String rules) {
		this.rules = rules;
	}
	public String getBannerUrl() {
		return bannerUrl;
	}
	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
	public String getAdditional() {
		return additional;
	}
	public void setAdditional(String additional) {
		this.additional = additional;
	}
	public Integer getMinTeamNumber() {
		return minTeamNumber;
	}
	public void setMinTeamNumber(Integer minTeamNumber) {
		this.minTeamNumber = minTeamNumber;
	}
	public Integer getMaxTeamNumber() {
		return maxTeamNumber;
	}
	public void setMaxTeamNumber(Integer maxTeamNumber) {
		this.maxTeamNumber = maxTeamNumber;
	}
	public ZonedDateTime getEventStartTime() {
		return eventStartTime;
	}
	public void setEventStartTime(ZonedDateTime eventStartTime) {
		this.eventStartTime = eventStartTime;
	}
	public ZonedDateTime getEventEndTime() {
		return eventEndTime;
	}
	public void setEventEndTime(ZonedDateTime eventEndTime) {
		this.eventEndTime = eventEndTime;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public ChallengeStatus getChallengeStatus() {
		return challengeStatus;
	}
	public void setChallengeStatus(ChallengeStatus challengeStatus) {
		this.challengeStatus = challengeStatus;
	}
	
	
	
}
