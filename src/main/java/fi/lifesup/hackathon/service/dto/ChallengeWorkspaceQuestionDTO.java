package fi.lifesup.hackathon.service.dto;

public class ChallengeWorkspaceQuestionDTO {
	private Long id;
	private Long applicationId;
	private Long workSpaceId;
	private String subject;
	private String content;
	
	
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
	public Long getWorkSpaceId() {
		return workSpaceId;
	}
	public void setWorkSpaceId(Long workSpaceId) {
		this.workSpaceId = workSpaceId;
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
	
	
}
