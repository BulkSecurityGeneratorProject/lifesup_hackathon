package fi.lifesup.hackathon.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import fi.lifesup.hackathon.domain.enumeration.ChallengeStatus;

/**
 * A ChallengeInfo.
 */
@Entity
@Table(name = "challenge_info")
public class ChallengeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "event_start_time", nullable = false)
    private ZonedDateTime eventStartTime;

    @NotNull
    @Column(name = "event_end_time", nullable = false)
    private ZonedDateTime eventEndTime;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ChallengeStatus status;

    @NotNull
    @Column(name = "prize", nullable = false)
    private String prize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getEventStartTime() {
        return eventStartTime;
    }

    public ChallengeInfo eventStartTime(ZonedDateTime eventStartTime) {
        this.eventStartTime = eventStartTime;
        return this;
    }

    public void setEventStartTime(ZonedDateTime eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public ZonedDateTime getEventEndTime() {
        return eventEndTime;
    }

    public ChallengeInfo eventEndTime(ZonedDateTime eventEndTime) {
        this.eventEndTime = eventEndTime;
        return this;
    }

    public void setEventEndTime(ZonedDateTime eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getLocation() {
        return location;
    }

    public ChallengeInfo location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ChallengeStatus getStatus() {
        return status;
    }

    public ChallengeInfo status(ChallengeStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ChallengeStatus status) {
        this.status = status;
    }

    public String getPrize() {
        return prize;
    }

    public ChallengeInfo prize(String prize) {
        this.prize = prize;
        return this;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChallengeInfo challengeInfo = (ChallengeInfo) o;
        if(challengeInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, challengeInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChallengeInfo{" +
            "id=" + id +
            ", eventStartTime='" + eventStartTime + "'" +
            ", eventEndTime='" + eventEndTime + "'" +
            ", location='" + location + "'" +
            ", status='" + status + "'" +
            ", prize='" + prize + "'" +
            '}';
    }
}
