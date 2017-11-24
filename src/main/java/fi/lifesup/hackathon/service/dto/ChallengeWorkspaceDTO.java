package fi.lifesup.hackathon.service.dto;

import java.time.ZonedDateTime;
import java.util.List;

import fi.lifesup.hackathon.domain.ChallengeWorkspace;

public class ChallengeWorkspaceDTO {
	private Long id;
	private Long challengeId;
	private String termsAndConditions;
	private ZonedDateTime createdDate;
	private List<ChallengeWorkspaceNewsDTO> workspaceNews;
	private List<ChallengeWorkspaceQuestionDTO> workspaceQuestions;
	
	public ChallengeWorkspaceDTO() {
		// TODO Auto-generated constructor stub
	}
	public ChallengeWorkspaceDTO(Long id, Long challengeId, String termsAndConditions) {
		super();
		this.id = id;
		this.challengeId = challengeId;
		this.termsAndConditions = termsAndConditions;
	}
	public ChallengeWorkspaceDTO(ChallengeWorkspace workspace) {
	
		this.id = workspace.getId();
		this.challengeId = workspace.getChallenge().getId();
		this.termsAndConditions = workspace.getTermsAndConditions();
		this.createdDate = workspace.getCreatedDate(); 
	}
	
	public List<ChallengeWorkspaceNewsDTO> getWorkspaceNews() {
		return workspaceNews;
	}
	


	public void setWorkspaceNews(List<ChallengeWorkspaceNewsDTO> workspaceNews) {
		this.workspaceNews = workspaceNews;
	}
	public List<ChallengeWorkspaceQuestionDTO> getWorkspaceQuestions() {
		return workspaceQuestions;
	}
	public void setWorkspaceQuestions(List<ChallengeWorkspaceQuestionDTO> workspaceQuestions) {
		this.workspaceQuestions = workspaceQuestions;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getChallengeId() {
		return challengeId;
	}
	public void setChallengeId(Long challengeId) {
		this.challengeId = challengeId;
	}
	public String getTermsAndConditions() {
		return termsAndConditions;
	}
	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}


	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}
	
	

}
