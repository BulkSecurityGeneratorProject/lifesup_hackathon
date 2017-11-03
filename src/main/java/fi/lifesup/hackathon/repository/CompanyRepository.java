package fi.lifesup.hackathon.repository;

import fi.lifesup.hackathon.domain.Challenge;
import fi.lifesup.hackathon.domain.Company;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Company entity.
 */
@SuppressWarnings("unused")
public interface CompanyRepository extends JpaRepository<Company,Long> {
	@Query("select c.name from Company c ")
	List<String> getNameCompany();

}
