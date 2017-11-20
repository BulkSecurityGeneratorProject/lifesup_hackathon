package fi.lifesup.hackathon.service.dto;

import java.time.ZonedDateTime;

import org.springframework.web.multipart.MultipartFile;

import fi.lifesup.hackathon.domain.ChallengeSubmissionFeedback;
import fi.lifesup.hackathon.domain.ChallengeWorkspace;

public class ChallengeSubmissionDTO {
	private Long id;
	private Long applicationId;
	private String filePath;
	private String additionalNote;
	private ZonedDateTime modifiedDate;
	private String modifiedBy;
	private ChallengeSubmissionFeedback feedback;
	private ChallengeWorkspace workspace;
	private MultipartFile multipartFile;

	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
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

	public ChallengeSubmissionFeedback getFeedback() {
		return feedback;
	}

	public void setFeedback(ChallengeSubmissionFeedback feedback) {
		this.feedback = feedback;
	}

	public ChallengeWorkspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(ChallengeWorkspace workspace) {
		this.workspace = workspace;
	}

	public ChallengeSubmissionDTO() {
		super();
	}

	public ChallengeSubmissionDTO(Long id, Long applicationId, String filePath, String additionalNote,
			ZonedDateTime modifiedDate, String modifiedBy, ChallengeSubmissionFeedback feedback,
			ChallengeWorkspace workspace, MultipartFile multipartFile) {
		super();
		this.id = id;
		this.applicationId = applicationId;
		this.filePath = filePath;
		this.additionalNote = additionalNote;
		this.modifiedDate = modifiedDate;
		this.modifiedBy = modifiedBy;
		this.feedback = feedback;
		this.workspace = workspace;
		this.multipartFile = multipartFile;
	}

	

}
