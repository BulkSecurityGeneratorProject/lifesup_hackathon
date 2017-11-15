package fi.lifesup.hackathon.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import fi.lifesup.hackathon.domain.enumeration.WorkspaceAnswerType;

/**
 * A ChallengeWorkspaceAnswer.
 */
@Entity
@Table(name = "challenge_workspace_answer")
public class ChallengeWorkspaceAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "answer_by_type", nullable = false)
    private WorkspaceAnswerType answerByType;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "created_by")
    private ZonedDateTime createdBy;

    @ManyToOne
    private ChallengeWorkspaceQuestion question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public ChallengeWorkspaceAnswer content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public WorkspaceAnswerType getAnswerByType() {
        return answerByType;
    }

    public ChallengeWorkspaceAnswer answerByType(WorkspaceAnswerType answerByType) {
        this.answerByType = answerByType;
        return this;
    }

    public void setAnswerByType(WorkspaceAnswerType answerByType) {
        this.answerByType = answerByType;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public ChallengeWorkspaceAnswer createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getCreatedBy() {
        return createdBy;
    }

    public ChallengeWorkspaceAnswer createdBy(ZonedDateTime createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(ZonedDateTime createdBy) {
        this.createdBy = createdBy;
    }

    public ChallengeWorkspaceQuestion getQuestion() {
        return question;
    }

    public ChallengeWorkspaceAnswer question(ChallengeWorkspaceQuestion challengeWorkspaceQuestion) {
        this.question = challengeWorkspaceQuestion;
        return this;
    }

    public void setQuestion(ChallengeWorkspaceQuestion challengeWorkspaceQuestion) {
        this.question = challengeWorkspaceQuestion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChallengeWorkspaceAnswer challengeWorkspaceAnswer = (ChallengeWorkspaceAnswer) o;
        if(challengeWorkspaceAnswer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, challengeWorkspaceAnswer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChallengeWorkspaceAnswer{" +
            "id=" + id +
            ", content='" + content + "'" +
            ", answerByType='" + answerByType + "'" +
            ", createdDate='" + createdDate + "'" +
            ", createdBy='" + createdBy + "'" +
            '}';
    }
}
