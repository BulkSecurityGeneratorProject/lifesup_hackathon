package fi.lifesup.hackathon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Challenge.
 */
@Entity
@Table(name = "challenge")
public class Challenge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "introduction", nullable = false)
    private String introduction;

    @NotNull
    @Column(name = "challenge_text", nullable = false)
    private String challengeText;

    @NotNull
    @Column(name = "resource_text", nullable = false)
    private String resourceText;

    @NotNull
    @Column(name = "rewards_text", nullable = false)
    private String rewardsText;

    @NotNull
    @Column(name = "timeline_text", nullable = false)
    private String timelineText;

    @NotNull
    @Column(name = "rules_text", nullable = false)
    private String rulesText;

    @NotNull
    @Column(name = "banner_url", nullable = false)
    private String bannerUrl;

    @Column(name = "additional_text")
    private String additionalText;

    @NotNull
    @Column(name = "max_team_number", nullable = false)
    private Integer maxTeamNumber;

    @NotNull
    @Column(name = "min_team_number", nullable = false)
    private Integer minTeamNumber;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(unique = true)
    private ChallengeInfo info;

    @OneToMany(mappedBy = "challenge")
    @JsonIgnore
    private Set<Application> applications = new HashSet<>();

    @ManyToOne
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Challenge name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public Challenge introduction(String introduction) {
        this.introduction = introduction;
        return this;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getChallengeText() {
        return challengeText;
    }

    public Challenge challengeText(String challengeText) {
        this.challengeText = challengeText;
        return this;
    }

    public void setChallengeText(String challengeText) {
        this.challengeText = challengeText;
    }

    public String getResourceText() {
        return resourceText;
    }

    public Challenge resourceText(String resourceText) {
        this.resourceText = resourceText;
        return this;
    }

    public void setResourceText(String resourceText) {
        this.resourceText = resourceText;
    }

    public String getRewardsText() {
        return rewardsText;
    }

    public Challenge rewardsText(String rewardsText) {
        this.rewardsText = rewardsText;
        return this;
    }

    public void setRewardsText(String rewardsText) {
        this.rewardsText = rewardsText;
    }

    public String getTimelineText() {
        return timelineText;
    }

    public Challenge timelineText(String timelineText) {
        this.timelineText = timelineText;
        return this;
    }

    public void setTimelineText(String timelineText) {
        this.timelineText = timelineText;
    }

    public String getRulesText() {
        return rulesText;
    }

    public Challenge rulesText(String rulesText) {
        this.rulesText = rulesText;
        return this;
    }

    public void setRulesText(String rulesText) {
        this.rulesText = rulesText;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public Challenge bannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
        return this;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public Challenge additionalText(String additionalText) {
        this.additionalText = additionalText;
        return this;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText = additionalText;
    }

    public Integer getMaxTeamNumber() {
        return maxTeamNumber;
    }

    public Challenge maxTeamNumber(Integer maxTeamNumber) {
        this.maxTeamNumber = maxTeamNumber;
        return this;
    }

    public void setMaxTeamNumber(Integer maxTeamNumber) {
        this.maxTeamNumber = maxTeamNumber;
    }

    public Integer getMinTeamNumber() {
        return minTeamNumber;
    }

    public Challenge minTeamNumber(Integer minTeamNumber) {
        this.minTeamNumber = minTeamNumber;
        return this;
    }

    public void setMinTeamNumber(Integer minTeamNumber) {
        this.minTeamNumber = minTeamNumber;
    }

    public ChallengeInfo getInfo() {
        return info;
    }

    public Challenge info(ChallengeInfo challengeInfo) {
        this.info = challengeInfo;
        return this;
    }

    public void setInfo(ChallengeInfo challengeInfo) {
        this.info = challengeInfo;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public Challenge applications(Set<Application> applications) {
        this.applications = applications;
        return this;
    }

    public Challenge addApplications(Application application) {
        applications.add(application);
        application.setChallenge(this);
        return this;
    }

    public Challenge removeApplications(Application application) {
        applications.remove(application);
        application.setChallenge(null);
        return this;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    public Company getCompany() {
        return company;
    }

    public Challenge company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Challenge challenge = (Challenge) o;
        if(challenge.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, challenge.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Challenge{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", introduction='" + introduction + "'" +
            ", challengeText='" + challengeText + "'" +
            ", resourceText='" + resourceText + "'" +
            ", rewardsText='" + rewardsText + "'" +
            ", timelineText='" + timelineText + "'" +
            ", rulesText='" + rulesText + "'" +
            ", bannerUrl='" + bannerUrl + "'" +
            ", additionalText='" + additionalText + "'" +
            ", maxTeamNumber='" + maxTeamNumber + "'" +
            ", minTeamNumber='" + minTeamNumber + "'" +
            '}';
    }
}
