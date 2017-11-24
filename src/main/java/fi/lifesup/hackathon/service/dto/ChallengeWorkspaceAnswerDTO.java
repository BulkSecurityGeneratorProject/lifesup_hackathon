package fi.lifesup.hackathon.service.dto;

import java.time.ZonedDateTime;

import fi.lifesup.hackathon.domain.ChallengeWorkspaceAnswer;
import fi.lifesup.hackathon.domain.enumeration.WorkspaceAnswerType;

public class ChallengeWorkspaceAnswerDTO {
	private Long id;
	private Long questionId;
	private String content;
	private WorkspaceAnswerType answerByType;
	private String createdBy;
	private ZonedDateTime createdDate;
	
	
	public ChallengeWorkspaceAnswerDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public ChallengeWorkspaceAnswerDTO(Long id, Long questionId, String content, WorkspaceAnswerType answerByType) {
		super();
		this.id = id;
		this.questionId = questionId;
		this.content = content;
		this.answerByType = answerByType;
	}
	
		
	public ChallengeWorkspaceAnswerDTO(ChallengeWorkspaceAnswer answer){
		this.id = answer.getId();
		this.questionId = answer.getQuestion().getId();
		this.content = answer.getContent();
		this.answerByType = answer.getAnswerByType();
		this.createdBy = answer.getCreatedBy();
		this.createdDate = answer.getCreatedDate();
	}



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public WorkspaceAnswerType getAnswerByType() {
		return answerByType;
	}
	public void setAnswerByType(WorkspaceAnswerType answerByType) {
		this.answerByType = answerByType;
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
