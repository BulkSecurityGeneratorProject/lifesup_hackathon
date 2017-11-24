package fi.lifesup.hackathon.service.dto;

import java.time.ZonedDateTime;
import java.util.List;

import fi.lifesup.hackathon.domain.ChallengeWorkspaceQuestion;

public class ChallengeWorkspaceQuestionDTO {
	private Long id;
	private Long applicationId;
	private Long workspaceId;
	private String subject;
	private String content;
	private String createdBy;
	private ZonedDateTime createdDate;
	private List<ChallengeWorkspaceAnswerDTO> answers;	
	
	public ChallengeWorkspaceQuestionDTO() {
		// TODO Auto-generated constructor stub
	}
	
	

	public ChallengeWorkspaceQuestionDTO(Long id, Long applicationId, Long workspaceId, String subject, String content,
			String createdBy, ZonedDateTime createdDate) {
		super();
		this.id = id;
		this.applicationId = applicationId;
		this.workspaceId = workspaceId;
		this.subject = subject;
		this.content = content;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
	}



	public ChallengeWorkspaceQuestionDTO(ChallengeWorkspaceQuestion question) {
		this.id = question.getId();
		this.applicationId = question.getApplicationId();
		this.workspaceId = question.getWorkspace().getId();
		this.subject = question.getSubject();
		this.content = question.getContent();
		this.createdBy = question.getCreatedBy();
		this.createdDate = question.getCreatedDate();
				
	}

	public List<ChallengeWorkspaceAnswerDTO> getAnswers() {
		return answers;
	}
	public void setAnswers(List<ChallengeWorkspaceAnswerDTO> answers) {
		this.answers = answers;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}
	public Long getWorkspaceId() {
		return workspaceId;
	}
	public void setWorkspaceId(Long workSpaceId) {
		this.workspaceId = workSpaceId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}
	
	
	
}
