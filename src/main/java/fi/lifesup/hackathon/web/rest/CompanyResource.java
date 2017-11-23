package fi.lifesup.hackathon.web.rest;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import fi.lifesup.hackathon.domain.Company;
import fi.lifesup.hackathon.domain.User;
import fi.lifesup.hackathon.repository.CompanyRepository;
import fi.lifesup.hackathon.repository.UserRepository;
import fi.lifesup.hackathon.security.SecurityUtils;
import fi.lifesup.hackathon.service.MailService;
import fi.lifesup.hackathon.service.UserService;
import fi.lifesup.hackathon.service.dto.CompanyDTO;
import fi.lifesup.hackathon.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Company.
 */
@RestController
@RequestMapping("/api")
public class CompanyResource {

	private final Logger log = LoggerFactory.getLogger(CompanyResource.class);

	@Inject
	private CompanyRepository companyRepository;

	@Inject
	private UserService userService;

	@Inject
	private MailService mailService;
	@Inject
	private UserRepository userRepository;

	/**
	 * POST /companies : Create a new company.
	 *
	 * @param company
	 *            the company to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new company, or with status 400 (Bad Request) if the company has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/companies")
	@Timed
	public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) throws URISyntaxException {
		log.debug("REST request to save Company : {}", company);
		if (company.getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert("company", "idexists", "A new company cannot already have an ID"))
					.body(null);
		}
		Company result = companyRepository.save(company);
		return ResponseEntity.created(new URI("/api/companies/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("company", result.getId().toString())).body(result);
	}

	/**
	 * PUT /companies : Updates an existing company.
	 *
	 * @param company
	 *            the company to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         company, or with status 400 (Bad Request) if the company is not
	 *         valid, or with status 500 (Internal Server Error) if the company
	 *         couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/companies")
	@Timed
	public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) throws URISyntaxException {
		log.debug("REST request to update Company : {}", company);
		if (company.getId() == null) {
			return createCompany(company);
		}
		User user = userRepository.getUserByAuthority(SecurityUtils.getCurrentUserLogin(), "ROLE_ADMIN");
		if (user != null) {
			Company result = companyRepository.save(company);
			return ResponseEntity.ok()
					.headers(HeaderUtil.createEntityUpdateAlert("company", company.getId().toString())).body(result);
		}
		user = userRepository.getUserByAuthority(SecurityUtils.getCurrentUserLogin(), "ROLE_HOST");
		if (user == null || user.getCompany().getId().longValue() != company.getId().longValue()) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert("company", "idexists", "A new company cannot already have an ID"))
					.body(null);
		}
		Company result = companyRepository.save(company);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("company", company.getId().toString()))
				.body(result);
	}

	/**
	 * GET /companies : get all the companies.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of companies
	 *         in body
	 */
	@GetMapping("/companies")
	@Timed
	public List<Company> getAllCompanies() {
		log.debug("REST request to get all Companies");
		List<Company> companies = companyRepository.findAll();
		return companies;
	}

	/**
	 * GET /companies/:id : get the "id" company.
	 *
	 * @param id
	 *            the id of the company to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         company, or with status 404 (Not Found)
	 */
	@GetMapping("/companies/{id}")
	@Timed
	public ResponseEntity<Company> getCompany(@PathVariable Long id) {
		log.debug("REST request to get Company : {}", id);
		Company company = companyRepository.findOne(id);
		return Optional.ofNullable(company).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /companies/:id : delete the "id" company.
	 *
	 * @param id
	 *            the id of the company to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/companies/{id}")
	@Timed
	public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
		log.debug("REST request to delete Company : {}", id);
		companyRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("company", id.toString())).build();
	}

	@GetMapping("/companies/user")
	@Timed
	public Company getCompanyByUser() {
		log.debug("REST request to get Company  by User ");
		User u = userService.getUserWithAuthorities();
		return u.getCompany();
	}

	@PostMapping("/companies/send-mail")
	@Timed
	public ResponseEntity<?> sendMail(@Valid @RequestBody CompanyDTO company, HttpServletRequest request)
			throws URISyntaxException {
		log.debug("REST request to save Application : {}", company);

		String baseUrl = request.getScheme() + // "http"
				"://" + // "://"
				request.getServerName() + // "myhost"
				":" + // ":"
				request.getServerPort() + // "80"
				request.getContextPath(); // "/myContextPath" or "" if deployed
											// in root context
		mailService.sendCompanyMail(company, baseUrl);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/companies/name")
	@Timed
	public List<String> getNameCompany() {
		log.debug("REST request to get Name Company ");
		List<String> names = companyRepository.getNameCompany();
		return names;
	}
	
	
}
