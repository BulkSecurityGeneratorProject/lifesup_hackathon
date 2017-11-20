package fi.lifesup.hackathon.service.dto;

import java.util.List;

public class ChallengeWorkspaceDTO {
	private Long id;
	private Long challengeId;
	private String termsAndConditions;
	private List<ChallengeWorkspaceNewsDTO> workspaceNews;
	private List<ChallengeWorkspaceQuestionDTO> workspaceQuestions;
	
	public ChallengeWorkspaceDTO() {
		// TODO Auto-generated constructor stub
	}
	
	
	public List<ChallengeWorkspaceNewsDTO> getWorkspaceNews() {
		return workspaceNews;
	}public ChallengeWorkspaceDTO(Long id, Long challengeId, String termsAndConditions) {
		super();
		this.id = id;
		this.challengeId = challengeId;
		this.termsAndConditions = termsAndConditions;
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
	

}
