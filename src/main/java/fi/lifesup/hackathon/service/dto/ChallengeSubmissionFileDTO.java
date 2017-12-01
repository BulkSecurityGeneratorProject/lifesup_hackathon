package fi.lifesup.hackathon.service.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ChallengeSubmissionFileDTO {
	private long challengeSubmissionId;
	 private MultipartFile file;
	 public long getChallengeSubmissionId() {
		return challengeSubmissionId;
	}

	public void setChallengeSubmissionId(long challengeSubmissionId) {
		this.challengeSubmissionId = challengeSubmissionId;
	}

	

	 public MultipartFile getFile() {
	  return file;
	 }

	 public void setFile(MultipartFile file) {
	  this.file = file;
	 }

	 
}
