package fi.lifesup.hackathon.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ChallengeFeedback.
 */
@Entity
@Table(name = "challenge_feedback")
public class ChallengeFeedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "application_id", nullable = false)
    private Long applicationId;

    @Column(name = "feedback_for_us")
    private String feedbackForUs;

    @Column(name = "feedback_for_challenge")
    private String feedbackForChallenge;

    @Column(name = "public_feedback")
    private String publicFeedback;

    @NotNull
    @Column(name = "rating", nullable = false)
    private Integer rating;
    
    @ManyToOne
    private Challenge challenge;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public ChallengeFeedback applicationId(Long applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getFeedbackForUs() {
        return feedbackForUs;
    }

    public ChallengeFeedback feedbackForUs(String feedbackForUs) {
        this.feedbackForUs = feedbackForUs;
        return this;
    }

    public void setFeedbackForUs(String feedbackForUs) {
        this.feedbackForUs = feedbackForUs;
    }

    public String getFeedbackForChallenge() {
        return feedbackForChallenge;
    }

    public ChallengeFeedback feedbackForChallenge(String feedbackForChallenge) {
        this.feedbackForChallenge = feedbackForChallenge;
        return this;
    }

    public void setFeedbackForChallenge(String feedbackForChallenge) {
        this.feedbackForChallenge = feedbackForChallenge;
    }

    public String getPublicFeedback() {
        return publicFeedback;
    }

    public ChallengeFeedback publicFeedback(String publicFeedback) {
        this.publicFeedback = publicFeedback;
        return this;
    }

    public void setPublicFeedback(String publicFeedback) {
        this.publicFeedback = publicFeedback;
    }

    public Integer getRating() {
        return rating;
    }

    public ChallengeFeedback rating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public Challenge getChallenge() {
		return challenge;
	}

	public void setChallenge(Challenge challenge) {
		this.challenge = challenge;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChallengeFeedback challengeFeedback = (ChallengeFeedback) o;
        if(challengeFeedback.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, challengeFeedback.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChallengeFeedback{" +
            "id=" + id +
            ", applicationId='" + applicationId + "'" +
            ", feedbackForUs='" + feedbackForUs + "'" +
            ", feedbackForChallenge='" + feedbackForChallenge + "'" +
            ", publicFeedback='" + publicFeedback + "'" +
            ", rating='" + rating + "'" +
            '}';
    }
}
