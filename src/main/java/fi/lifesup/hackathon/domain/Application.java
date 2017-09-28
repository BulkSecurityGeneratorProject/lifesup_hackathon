package fi.lifesup.hackathon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import fi.lifesup.hackathon.domain.enumeration.ApplicationStatus;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Column(name = "company_name")
    private String companyName;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "motivation", nullable = false)
    private String motivation;

    @NotNull
    @Column(name = "ideas_description", nullable = false)
    private String ideasDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApplicationStatus status;

    @ManyToOne
    private Challenge challenge;

    @ManyToMany(mappedBy = "applications")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public Application teamName(String teamName) {
        this.teamName = teamName;
        return this;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Application companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public Application description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMotivation() {
        return motivation;
    }

    public Application motivation(String motivation) {
        this.motivation = motivation;
        return this;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public String getIdeasDescription() {
        return ideasDescription;
    }

    public Application ideasDescription(String ideasDescription) {
        this.ideasDescription = ideasDescription;
        return this;
    }

    public void setIdeasDescription(String ideasDescription) {
        this.ideasDescription = ideasDescription;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public Application status(ApplicationStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public Application challenge(Challenge challenge) {
        this.challenge = challenge;
        return this;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Application users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Application addUsers(User user) {
        users.add(user);
        user.getApplications().add(this);
        return this;
    }

    public Application removeUsers(User user) {
        users.remove(user);
        user.getApplications().remove(this);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Application application = (Application) o;
        if(application.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, application.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Application{" +
            "id=" + id +
            ", teamName='" + teamName + "'" +
            ", companyName='" + companyName + "'" +
            ", description='" + description + "'" +
            ", motivation='" + motivation + "'" +
            ", ideasDescription='" + ideasDescription + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
