package fi.lifesup.hackathon.service.dto;

import fi.lifesup.hackathon.domain.Challenge;

public class ChallengeFeedbackDTO {
	private Long id;
	private Long applicationId;
	private String feedbackForUs;
	private String feedbackForChallenge;
	private String publicFeedback;
	private Integer rating;
	private Long challengeId;

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

	public String getFeedbackForUs() {
		return feedbackForUs;
	}

	public void setFeedbackForUs(String feedbackForUs) {
		this.feedbackForUs = feedbackForUs;
	}

	public String getFeedbackForChallenge() {
		return feedbackForChallenge;
	}

	public void setFeedbackForChallenge(String feedbackForChallenge) {
		this.feedbackForChallenge = feedbackForChallenge;
	}

	public String getPublicFeedback() {
		return publicFeedback;
	}

	public void setPublicFeedback(String publicFeedback) {
		this.publicFeedback = publicFeedback;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Long getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(Long challengeId) {
		this.challengeId = challengeId;
	}

	public ChallengeFeedbackDTO() {
		super();
	}

	public ChallengeFeedbackDTO(Long id, Long applicationId, String feedbackForUs, String feedbackForChallenge,
			String publicFeedback, Integer rating, Long challengeId) {
		super();
		this.id = id;
		this.applicationId = applicationId;
		this.feedbackForUs = feedbackForUs;
		this.feedbackForChallenge = feedbackForChallenge;
		this.publicFeedback = publicFeedback;
		this.rating = rating;
		this.challengeId = challengeId;
	}

}
