package fi.lifesup.hackathon.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A ChallengeWorkspace.
 */
@Entity
@Table(name = "challenge_workspace")
public class ChallengeWorkspace implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "created_date")
	private ZonedDateTime createdDate;

	@Column(name = "terms_and_conditions")
	private String termsAndConditions;

	@OneToOne
	@JoinColumn(unique = true)
	private Challenge challenge;

	@OneToMany(mappedBy = "workspace")
	@JsonIgnore
	@OrderBy("createdDate ASC")
	private Set<ChallengeWorkspaceNews> workspaceNews = new HashSet<>();

	@OneToMany(mappedBy = "workspace")
	@JsonIgnore
	@OrderBy("createdDate ASC")
	private Set<ChallengeWorkspaceQuestion> workspaceQuestions = new HashSet<>();

	@OneToMany(mappedBy = "workspace")
	@JsonIgnore
	@OrderBy("createdDate ASC")
	private Set<ChallengeSubmission> submissions = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}

	public ChallengeWorkspace createdDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
		return this;
	}

	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	public ChallengeWorkspace termsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
		return this;
	}

	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

	public Challenge getChallenge() {
		return challenge;
	}

	public ChallengeWorkspace challenge(Challenge challenge) {
		this.challenge = challenge;
		return this;
	}

	public void setChallenge(Challenge challenge) {
		this.challenge = challenge;
	}

	public Set<ChallengeWorkspaceNews> getWorkspaceNews() {
		return workspaceNews;
	}

	public ChallengeWorkspace workspaceNews(Set<ChallengeWorkspaceNews> challengeWorkspaceNews) {
		this.workspaceNews = challengeWorkspaceNews;
		return this;
	}

	public ChallengeWorkspace addWorkspaceNews(ChallengeWorkspaceNews challengeWorkspaceNews) {
		workspaceNews.add(challengeWorkspaceNews);
		challengeWorkspaceNews.setWorkspace(this);
		return this;
	}

	public ChallengeWorkspace removeWorkspaceNews(ChallengeWorkspaceNews challengeWorkspaceNews) {
		workspaceNews.remove(challengeWorkspaceNews);
		challengeWorkspaceNews.setWorkspace(null);
		return this;
	}

	public void setWorkspaceNews(Set<ChallengeWorkspaceNews> challengeWorkspaceNews) {
		this.workspaceNews = challengeWorkspaceNews;
	}

	public Set<ChallengeWorkspaceQuestion> getWorkspaceQuestions() {
		return workspaceQuestions;
	}

	public ChallengeWorkspace workspaceQuestions(Set<ChallengeWorkspaceQuestion> challengeWorkspaceQuestions) {
		this.workspaceQuestions = challengeWorkspaceQuestions;
		return this;
	}

	public ChallengeWorkspace addWorkspaceQuestions(ChallengeWorkspaceQuestion challengeWorkspaceQuestion) {
		workspaceQuestions.add(challengeWorkspaceQuestion);
		challengeWorkspaceQuestion.setWorkspace(this);
		return this;
	}

	public ChallengeWorkspace removeWorkspaceQuestions(ChallengeWorkspaceQuestion challengeWorkspaceQuestion) {
		workspaceQuestions.remove(challengeWorkspaceQuestion);
		challengeWorkspaceQuestion.setWorkspace(null);
		return this;
	}

	public void setWorkspaceQuestions(Set<ChallengeWorkspaceQuestion> challengeWorkspaceQuestions) {
		this.workspaceQuestions = challengeWorkspaceQuestions;
	}

	public Set<ChallengeSubmission> getSubmissions() {
		return submissions;
	}

	public ChallengeWorkspace submissions(Set<ChallengeSubmission> challengeSubmissions) {
		this.submissions = challengeSubmissions;
		return this;
	}

	public ChallengeWorkspace addSubmissions(ChallengeSubmission challengeSubmission) {
		submissions.add(challengeSubmission);
		challengeSubmission.setWorkspace(this);
		return this;
	}

	public ChallengeWorkspace removeSubmissions(ChallengeSubmission challengeSubmission) {
		submissions.remove(challengeSubmission);
		challengeSubmission.setWorkspace(null);
		return this;
	}

	public void setSubmissions(Set<ChallengeSubmission> challengeSubmissions) {
		this.submissions = challengeSubmissions;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ChallengeWorkspace challengeWorkspace = (ChallengeWorkspace) o;
		if (challengeWorkspace.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, challengeWorkspace.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "ChallengeWorkspace{" + "id=" + id + ", createdDate='" + createdDate + "'" + ", termsAndConditions='"
				+ termsAndConditions + "'" + '}';
	}
}
