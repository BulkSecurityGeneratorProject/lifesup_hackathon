package fi.lifesup.hackathon.domain;

import javax.persistence.*;

import fi.lifesup.hackathon.domain.enumeration.ChallengeUserApplicationStatus;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ChallengeUserApplication.
 */
@Entity
@Table(name = "challenge_user_application")
public class ChallengeUserApplication implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "challenge_id")
	private Long challengeId;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "application_id")
	private Long applicationId;

	@Column(name = "accept_key")
	private String acceptKey;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private ChallengeUserApplicationStatus status;

	public String getAcceptKey() {
		return acceptKey;
	}

	public void setAcceptKey(String acceptKey) {
		this.acceptKey = acceptKey;
	}

	public ChallengeUserApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ChallengeUserApplicationStatus status) {
		this.status = status;
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

	public ChallengeUserApplication challengeId(Long challengeId) {
		this.challengeId = challengeId;
		return this;
	}

	public void setChallengeId(Long challengeId) {
		this.challengeId = challengeId;
	}

	public Long getUserId() {
		return userId;
	}

	public ChallengeUserApplication userId(Long userId) {
		this.userId = userId;
		return this;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getApplicationId() {
		return applicationId;
	}

	public ChallengeUserApplication applicationId(Long applicationId) {
		this.applicationId = applicationId;
		return this;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ChallengeUserApplication challengeUserApplication = (ChallengeUserApplication) o;
		if (challengeUserApplication.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, challengeUserApplication.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "ChallengeUserApplication{" + "id=" + id + ", challengeId='" + challengeId + "'" + ", userId='" + userId
				+ "'" + ", applicationId='" + applicationId + "'" + '}';
	}
}
