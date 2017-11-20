package fi.lifesup.hackathon.service.dto;

import fi.lifesup.hackathon.domain.enumeration.WorkspaceAnswerType;

public class ChallengeWorkspaceAnswerDTO {
	private Long id;
	private Long questionId;
	private String content;
	private WorkspaceAnswerType answerByType;
	
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
	
	
}
