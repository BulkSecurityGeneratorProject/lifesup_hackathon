package fi.lifesup.hackathon.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ChallengeSubmission.
 */
@Entity
@Table(name = "challenge_submission")
public class ChallengeSubmission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "application_id", nullable = false)
    private Long applicationId;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "additional_note")
    private String additionalNote;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @OneToOne
    @JoinColumn(unique = true)
    private ChallengeSubmissionFeedback feedback;

    @ManyToOne
    private ChallengeWorkspace workspace;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public ChallengeSubmission applicationId(Long applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getFilePath() {
        return filePath;
    }

    public ChallengeSubmission filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAdditionalNote() {
        return additionalNote;
    }

    public ChallengeSubmission additionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
        return this;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public ChallengeSubmission modifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public ChallengeSubmission modifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public ChallengeSubmissionFeedback getFeedback() {
        return feedback;
    }

    public ChallengeSubmission feedback(ChallengeSubmissionFeedback challengeSubmissionFeedback) {
        this.feedback = challengeSubmissionFeedback;
        return this;
    }

    public void setFeedback(ChallengeSubmissionFeedback challengeSubmissionFeedback) {
        this.feedback = challengeSubmissionFeedback;
    }

    public ChallengeWorkspace getWorkspace() {
        return workspace;
    }

    public ChallengeSubmission workspace(ChallengeWorkspace challengeWorkspace) {
        this.workspace = challengeWorkspace;
        return this;
    }

    public void setWorkspace(ChallengeWorkspace challengeWorkspace) {
        this.workspace = challengeWorkspace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChallengeSubmission challengeSubmission = (ChallengeSubmission) o;
        if(challengeSubmission.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, challengeSubmission.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChallengeSubmission{" +
            "id=" + id +
            ", applicationId='" + applicationId + "'" +
            ", filePath='" + filePath + "'" +
            ", additionalNote='" + additionalNote + "'" +
            ", modifiedDate='" + modifiedDate + "'" +
            ", modifiedBy='" + modifiedBy + "'" +
            '}';
    }
}
