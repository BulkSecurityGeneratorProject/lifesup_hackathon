package fi.lifesup.hackathon.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import fi.lifesup.hackathon.domain.enumeration.UserSex;

import java.io.Serializable;
import java.time.ZonedDateTime;
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
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private UserSex sex;

    @Column(name = "company_name")
    private String companyName;

    
    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "logo_url")
    private String logoUrl;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "nationality", nullable = false)
    private String nationality;
    
    @Column(name = "birthday")
    private ZonedDateTime birthday;

    @NotNull
    @Column(name = "introduction", nullable = false)
    private String introduction;

    @Pattern(regexp = "^(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?$")
    //@Pattern(regexp = "^(https?://)?([a-z-]+).([a-z.]{2,6})([w .-]*)*/?$")
    @Column(name = "twitter_url")
    private String twitterUrl;

    @Pattern(regexp = "^(https?://)?([a-z-]+).([a-z.]{2,6})([w .-]*)*/?$")
    @Column(name = "linked_in_url")
    private String linkedInUrl;

    @Pattern(regexp = "^(https?://)?([a-z-]+).([a-z.]{2,6})([w .-]*)*/?$")
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

    public String getPhone() {
        return phone;
    }

    public UserInfo phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserSex getSex() {
        return sex;
    }

    public UserInfo sex(UserSex sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(UserSex sex) {
        this.sex = sex;
    }

    public String getCompanyName() {
        return companyName;
    }

    public UserInfo companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public UserInfo jobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public UserInfo logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCountry() {
        return country;
    }

    public UserInfo country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public UserInfo city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNationality() {
        return nationality;
    }

    public UserInfo nationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public ZonedDateTime getBirthday() {
        return birthday;
    }

    public UserInfo birthday(ZonedDateTime birthday) {
        this.birthday = birthday;
        return this;
    }
    
    public void setBirthday(ZonedDateTime birthday) {
        this.birthday = birthday;
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
