package fi.lifesup.hackathon.service.dto;

import fi.lifesup.hackathon.domain.enumeration.ChallengeUserApplicationStatus;
import fi.lifesup.hackathon.domain.enumeration.UserStatus;

public class ApplicationMemberDTO {

	private Long id;
	private Long applicationId;
	private Long challengeId;
	private String userEmail;
	private String userName;
	private UserStatus userStatus;
	private ChallengeUserApplicationStatus status;

	public ApplicationMemberDTO() {
		// TODO Auto-generated constructor stub
	}

	public ApplicationMemberDTO(Long id, Long applicationId, Long challengeId, String userEmail) {
		super();
		this.id = id;
		this.applicationId = applicationId;
		this.challengeId = challengeId;
		this.userEmail = userEmail;
	}

	public ApplicationMemberDTO(Long id, Long applicationId, Long challengeId, String userEmail, String userName) {
		super();
		this.id = id;
		this.applicationId = applicationId;
		this.challengeId = challengeId;
		this.userEmail = userEmail;
		this.userName = userName;
	}

	public ApplicationMemberDTO(Long id, UserStatus userStatus, ChallengeUserApplicationStatus status) {
		super();
		this.id = id;
		this.userStatus = userStatus;
		this.status = status;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public ChallengeUserApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ChallengeUserApplicationStatus status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public Long getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(Long challengeId) {
		this.challengeId = challengeId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

}
