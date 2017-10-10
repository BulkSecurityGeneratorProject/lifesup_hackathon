package fi.lifesup.hackathon.service.dto;

import java.time.ZonedDateTime;

import fi.lifesup.hackathon.domain.enumeration.ChallengeUserApplicationStatus;
import fi.lifesup.hackathon.domain.enumeration.UserSex;
import fi.lifesup.hackathon.domain.enumeration.UserStatus;

public class ApplicationMemberDTO {

	private Long id;
	private Long userId;
	

	private String invitedMail;
	private String firstName;
	private String lastName;
	private String phone;
	private UserSex sex;
	private String companyName;
	private String jobTitle;
	private String logoUrl;
	private String country;
	private String city;
	private String nationality;
	private ZonedDateTime birthday;
	private String introduction;
	private String twitterUrl;
	private String linkedInUrl;
	private String websiteUrl;
	private String skills;
	private String workArea;
	private String feedbackFrom;
		
	public ApplicationMemberDTO(Long id, String invitedMail) {
		super();
		this.id = id;
		this.invitedMail = invitedMail;
	}

	public void setUserInfo(UserInfoDTO ui){
		this.firstName = ui.getfirstName();
		this.lastName = ui.getLastName();
		this.phone = ui.getPhone();
		this.sex = ui.getSex();
		this.companyName = ui.getCompanyName();
		this.jobTitle = ui.getJobTitle();
		this.logoUrl = ui.getLogoUrl();
		this.country = ui.getCountry();
		this.city = ui.getCity();
		this.nationality = ui.getNationality();
		this.birthday = ui.getBirthday();
		this.introduction = ui.getIntroduction();
		this.twitterUrl = ui.getTwitterUrl();
		this.linkedInUrl = ui.getLinkedInUrl();
		this.websiteUrl = ui.getWebsiteUrl();
		this.skills = ui.getSkills();
		this.workArea = ui.getWorkArea();
		this.feedbackFrom = ui.getFeedbackFrom();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public UserSex getSex() {
		return sex;
	}

	public void setSex(UserSex sex) {
		this.sex = sex;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public ZonedDateTime getBirthday() {
		return birthday;
	}

	public void setBirthday(ZonedDateTime birthday) {
		this.birthday = birthday;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getTwitterUrl() {
		return twitterUrl;
	}

	public void setTwitterUrl(String twitterUrl) {
		this.twitterUrl = twitterUrl;
	}

	public String getLinkedInUrl() {
		return linkedInUrl;
	}

	public void setLinkedInUrl(String linkedInUrl) {
		this.linkedInUrl = linkedInUrl;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getWorkArea() {
		return workArea;
	}

	public void setWorkArea(String workArea) {
		this.workArea = workArea;
	}

	public String getFeedbackFrom() {
		return feedbackFrom;
	}

	public void setFeedbackFrom(String feedbackFrom) {
		this.feedbackFrom = feedbackFrom;
	}

	public ApplicationMemberDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getInvitedMail() {
		return invitedMail;
	}

	public void setInvitedMail(String invitedMail) {
		this.invitedMail = invitedMail;
	}

	

}
