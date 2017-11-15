package fi.lifesup.hackathon.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ChallengeWorkspaceNews.
 */
@Entity
@Table(name = "challenge_workspace_news")
public class ChallengeWorkspaceNews implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @ManyToOne
    private ChallengeWorkspace workspace;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public ChallengeWorkspaceNews title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public ChallengeWorkspaceNews content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public ChallengeWorkspaceNews createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ChallengeWorkspaceNews createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ChallengeWorkspace getWorkspace() {
        return workspace;
    }

    public ChallengeWorkspaceNews workspace(ChallengeWorkspace challengeWorkspace) {
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
        ChallengeWorkspaceNews challengeWorkspaceNews = (ChallengeWorkspaceNews) o;
        if(challengeWorkspaceNews.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, challengeWorkspaceNews.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChallengeWorkspaceNews{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", content='" + content + "'" +
            ", createdDate='" + createdDate + "'" +
            ", createdBy='" + createdBy + "'" +
            '}';
    }
}
