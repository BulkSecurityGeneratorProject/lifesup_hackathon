package fi.lifesup.hackathon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.lifesup.hackathon.domain.Experience;

/**
 * Spring Data JPA repository for the Experience entity.
 */
@SuppressWarnings("unused")
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

}
