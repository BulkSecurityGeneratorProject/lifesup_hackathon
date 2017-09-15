package fi.lifesup.hackathon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.lifesup.hackathon.domain.Skill;

/**
 * Spring Data JPA repository for the Skill entity.
 */
@SuppressWarnings("unused")
public interface SkillRepository extends JpaRepository<Skill, Long> {

}
