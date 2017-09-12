package fi.lifesup.hackathon.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UserInfo.
 */
@Entity
@Table(name = "user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "introduction", nullable = false)
    private String introduction;

    @Column(name = "twitter_url")
    private String twitterUrl;

    @Column(name = "linked_in_url")
    private String linkedInUrl;

    @Column(name = "website_url")
    private String websiteUrl;

    @NotNull
    @Column(name = "skills", nullable = false)
    private String skills;

    @NotNull
    @Column(name = "work_area", nullable = false)
    private String workArea;

    @NotNull
    @Column(name = "feedback_from", nullable = false)
    private String feedbackFrom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntroduction() {
        return introduction;
    }

    public UserInfo introduction(String introduction) {
        this.introduction = introduction;
        return this;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public UserInfo twitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
        return this;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public UserInfo linkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
        return this;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public UserInfo websiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
        return this;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getSkills() {
        return skills;
    }

    public UserInfo skills(String skills) {
        this.skills = skills;
        return this;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getWorkArea() {
        return workArea;
    }

    public UserInfo workArea(String workArea) {
        this.workArea = workArea;
        return this;
    }

    public void setWorkArea(String workArea) {
        this.workArea = workArea;
    }

    public String getFeedbackFrom() {
        return feedbackFrom;
    }

    public UserInfo feedbackFrom(String feedbackFrom) {
        this.feedbackFrom = feedbackFrom;
        return this;
    }

    public void setFeedbackFrom(String feedbackFrom) {
        this.feedbackFrom = feedbackFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;
        if(userInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
            "id=" + id +
            ", introduction='" + introduction + "'" +
            ", twitterUrl='" + twitterUrl + "'" +
            ", linkedInUrl='" + linkedInUrl + "'" +
            ", websiteUrl='" + websiteUrl + "'" +
            ", skills='" + skills + "'" +
            ", workArea='" + workArea + "'" +
            ", feedbackFrom='" + feedbackFrom + "'" +
            '}';
    }
}
