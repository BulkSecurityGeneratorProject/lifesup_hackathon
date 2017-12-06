package fi.lifesup.hackathon.service.dto;

import java.time.ZonedDateTime;

import org.springframework.web.multipart.MultipartFile;

public class ChallengeSubmissionDTO {
	private Long id; 
	private Long applicationId;
	private String filePath;
	private String additionalNote;
	private ZonedDateTime modifiedDate;
	private String modifiedBy;
	private Long feedbackId;
	private Long workspaceId;
	private MultipartFile multipartFile;
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
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getAdditionalNote() {
		return additionalNote;
	}
	public void setAdditionalNote(String additionalNote) {
		this.additionalNote = additionalNote;
	}
	public ZonedDateTime getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(ZonedDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	public MultipartFile getMultipartFile() {
		return multipartFile;
	}
	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}
	
	
	public Long getFeedbackId() {
		return feedbackId;
	}
	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
	}
	public Long getWorkspaceId() {
		return workspaceId;
	}
	public void setWorkspaceId(Long workspaceId) {
		this.workspaceId = workspaceId;
	}
	public ChallengeSubmissionDTO() {
		super();
	}
	public ChallengeSubmissionDTO(Long id, Long applicationId, String filePath, String additionalNote,
			ZonedDateTime modifiedDate, String modifiedBy, Long feedback, Long workspace, MultipartFile multipartFile) {
		super();
		this.id = id;
		this.applicationId = applicationId;
		this.filePath = filePath;
		this.additionalNote = additionalNote;
		this.modifiedDate = modifiedDate;
		this.modifiedBy = modifiedBy;
		this.feedbackId = feedback;
		this.workspaceId = workspace;
		this.multipartFile = multipartFile;
	}
	
	

}
