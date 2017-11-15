package fi.lifesup.hackathon.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ChallengeResult.
 */
@Entity
@Table(name = "challenge_result")
public class ChallengeResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "first_team", nullable = false)
    private Long firstTeam;

    @Column(name = "second_team")
    private Long secondTeam;

    @Column(name = "third_team")
    private Long thirdTeam;

    @Column(name = "additional_note")
    private String additionalNote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFirstTeam() {
        return firstTeam;
    }

    public ChallengeResult firstTeam(Long firstTeam) {
        this.firstTeam = firstTeam;
        return this;
    }

    public void setFirstTeam(Long firstTeam) {
        this.firstTeam = firstTeam;
    }

    public Long getSecondTeam() {
        return secondTeam;
    }

    public ChallengeResult secondTeam(Long secondTeam) {
        this.secondTeam = secondTeam;
        return this;
    }

    public void setSecondTeam(Long secondTeam) {
        this.secondTeam = secondTeam;
    }

    public Long getThirdTeam() {
        return thirdTeam;
    }

    public ChallengeResult thirdTeam(Long thirdTeam) {
        this.thirdTeam = thirdTeam;
        return this;
    }

    public void setThirdTeam(Long thirdTeam) {
        this.thirdTeam = thirdTeam;
    }

    public String getAdditionalNote() {
        return additionalNote;
    }

    public ChallengeResult additionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
        return this;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChallengeResult challengeResult = (ChallengeResult) o;
        if(challengeResult.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, challengeResult.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChallengeResult{" +
            "id=" + id +
            ", firstTeam='" + firstTeam + "'" +
            ", secondTeam='" + secondTeam + "'" +
            ", thirdTeam='" + thirdTeam + "'" +
            ", additionalNote='" + additionalNote + "'" +
            '}';
    }
}
