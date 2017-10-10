package fi.lifesup.hackathon.service.dto;

import java.util.List;

import fi.lifesup.hackathon.domain.enumeration.ApplicationStatus;

public class ApplicationBasicDTO {
	private Long id;	
	private String teamName;
	private String companyName;
	private String description;
	private String motivation;
	private String ideasDescription;
	private ApplicationStatus status;
	private Long challengeId;
	private List<String> members;	
	
	public ApplicationBasicDTO() {
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMotivation() {
		return motivation;
	}
	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}
	public String getIdeasDescription() {
		return ideasDescription;
	}
	public void setIdeasDescription(String ideasDescription) {
		this.ideasDescription = ideasDescription;
	}
	public ApplicationStatus getStatus() {
		return status;
	}
	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
	public Long getChallengeId() {
		return challengeId;
	}
	public void setChallengeId(Long challengeId) {
		this.challengeId = challengeId;
	}
	public List<String> getMembers() {
		return members;
	}
	public void setMembers(List<String> members) {
		this.members = members;
	}
	
	
}
