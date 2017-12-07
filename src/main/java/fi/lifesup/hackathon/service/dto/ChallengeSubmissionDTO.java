package fi.lifesup.hackathon.service.dto;

import java.time.ZonedDateTime;

import org.springframework.web.multipart.MultipartFile;

import fi.lifesup.hackathon.domain.ChallengeSubmission;
import fi.lifesup.hackathon.domain.ChallengeSubmissionFeedback;

public class ChallengeSubmissionDTO {
	private Long id;
	private Long applicationId;
	private String filePath;
	private String additionalNote;
	private ZonedDateTime modifiedDate;
	private String modifiedBy;
	private ChallengeSubmissionFeedback feedback;
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
	//

	public ChallengeSubmissionFeedback getFeedback() {
		return feedback;
	}

	public void setFeedback(ChallengeSubmissionFeedback feedback) {
		this.feedback = feedback;
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

	public ChallengeSubmissionDTO(ChallengeSubmission challengeSubmission) {
		super();
		this.id = challengeSubmission.getId();
		this.applicationId = challengeSubmission.getApplicationId();
		this.filePath = challengeSubmission.getFilePath();
		this.additionalNote = challengeSubmission.getAdditionalNote();
		this.modifiedDate = challengeSubmission.getModifiedDate();
		this.modifiedBy = challengeSubmission.getModifiedBy();
		if (challengeSubmission != null) {
			this.feedback = challengeSubmission.getFeedback();
		}

		this.workspaceId = challengeSubmission.getWorkspace().getId();
	}

}
