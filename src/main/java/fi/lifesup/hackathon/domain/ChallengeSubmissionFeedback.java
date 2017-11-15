package fi.lifesup.hackathon.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ChallengeSubmissionFeedback.
 */
@Entity
@Table(name = "challenge_submission_feedback")
public class ChallengeSubmissionFeedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "feedback_text")
    private String feedbackText;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public ChallengeSubmissionFeedback feedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
        return this;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public ChallengeSubmissionFeedback createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ChallengeSubmissionFeedback createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChallengeSubmissionFeedback challengeSubmissionFeedback = (ChallengeSubmissionFeedback) o;
        if(challengeSubmissionFeedback.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, challengeSubmissionFeedback.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChallengeSubmissionFeedback{" +
            "id=" + id +
            ", feedbackText='" + feedbackText + "'" +
            ", createdDate='" + createdDate + "'" +
            ", createdBy='" + createdBy + "'" +
            '}';
    }
}
