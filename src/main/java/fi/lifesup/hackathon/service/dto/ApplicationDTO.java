package fi.lifesup.hackathon.service.dto;

import java.util.List;

import fi.lifesup.hackathon.domain.Application;
import fi.lifesup.hackathon.domain.enumeration.ApplicationStatus;

public class ApplicationDTO {
	
	private Long id;
	
	private String teamName;
	private String companyName;
	private String description;
	private String motivation;
	private String ideasDesscription;
	private ApplicationStatus status;
	private Long challengeId;
	private List<String> members;
	
	public ApplicationDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	public ApplicationDTO(Long id, String teamName, String companyteamName, String description, String motivation,
			String ideasDesscription, ApplicationStatus status, Long challengeId) {
		super();
		this.id = id;
		this.teamName = teamName;
		this.companyName = companyteamName;
		this.description = description;
		this.motivation = motivation;
		this.ideasDesscription = ideasDesscription;
		this.status = status;
		this.challengeId = challengeId;
	}
	
	

	public ApplicationDTO(Application application, List<String> members) {
		super();
		this.id = application.getId();
		this.teamName = application.getTeamName();
		this.companyName = application.getCompanyName();
		this.description = application.getDescription();
		this.motivation = application.getMotivation();
		this.ideasDesscription = application.getIdeasDescription();
		this.status = application.getStatus();
		this.challengeId = application.getChallenge().getId();
		this.members = members;
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

	public String getIdeasDesscription() {
		return ideasDesscription;
	}

	public void setIdeasDesscription(String ideasDesscription) {
		this.ideasDesscription = ideasDesscription;
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

}
