package fi.lifesup.hackathon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ChallengeWorkspaceQuestion.
 */
@Entity
@Table(name = "challenge_workspace_question")
public class ChallengeWorkspaceQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "application_id", nullable = false)
    private Long applicationId;

    @NotNull
    @Column(name = "subject", nullable = false)
    private String subject;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<ChallengeWorkspaceAnswer> answers = new HashSet<>();

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

    public ChallengeWorkspaceQuestion applicationId(Long applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getSubject() {
        return subject;
    }

    public ChallengeWorkspaceQuestion subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public ChallengeWorkspaceQuestion content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public ChallengeWorkspaceQuestion createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ChallengeWorkspaceQuestion createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Set<ChallengeWorkspaceAnswer> getAnswers() {
        return answers;
    }

    public ChallengeWorkspaceQuestion answers(Set<ChallengeWorkspaceAnswer> challengeWorkspaceAnswers) {
        this.answers = challengeWorkspaceAnswers;
        return this;
    }

    public ChallengeWorkspaceQuestion addAnswers(ChallengeWorkspaceAnswer challengeWorkspaceAnswer) {
        answers.add(challengeWorkspaceAnswer);
        challengeWorkspaceAnswer.setQuestion(this);
        return this;
    }

    public ChallengeWorkspaceQuestion removeAnswers(ChallengeWorkspaceAnswer challengeWorkspaceAnswer) {
        answers.remove(challengeWorkspaceAnswer);
        challengeWorkspaceAnswer.setQuestion(null);
        return this;
    }

    public void setAnswers(Set<ChallengeWorkspaceAnswer> challengeWorkspaceAnswers) {
        this.answers = challengeWorkspaceAnswers;
    }

    public ChallengeWorkspace getWorkspace() {
        return workspace;
    }

    public ChallengeWorkspaceQuestion workspace(ChallengeWorkspace challengeWorkspace) {
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
        ChallengeWorkspaceQuestion challengeWorkspaceQuestion = (ChallengeWorkspaceQuestion) o;
        if(challengeWorkspaceQuestion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, challengeWorkspaceQuestion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChallengeWorkspaceQuestion{" +
            "id=" + id +
            ", applicationId='" + applicationId + "'" +
            ", subject='" + subject + "'" +
            ", content='" + content + "'" +
            ", createdDate='" + createdDate + "'" +
            ", createdBy='" + createdBy + "'" +
            '}';
    }
}
