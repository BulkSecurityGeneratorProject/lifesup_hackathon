package fi.lifesup.hackathon.service.dto;

import java.time.ZonedDateTime;

import fi.lifesup.hackathon.domain.enumeration.ChallengeUserApplicationStatus;
import fi.lifesup.hackathon.domain.enumeration.UserSex;

public class MemberDTO {
	private String email;
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
	private ChallengeUserApplicationStatus status;
	public MemberDTO(String email, String firstName, String lastName, String phone, UserSex sex, String companyName,
			String jobTitle, String logoUrl, String country, String city, String nationality, ZonedDateTime birthday,
			String introduction, String twitterUrl, String linkedInUrl, String websiteUrl, String skills,
			String workArea, String feedbackFrom, ChallengeUserApplicationStatus status) {
		super();
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.sex = sex;
		this.companyName = companyName;
		this.jobTitle = jobTitle;
		this.logoUrl = logoUrl;
		this.country = country;
		this.city = city;
		this.nationality = nationality;
		this.birthday = birthday;
		this.introduction = introduction;
		this.twitterUrl = twitterUrl;
		this.linkedInUrl = linkedInUrl;
		this.websiteUrl = websiteUrl;
		this.skills = skills;
		this.workArea = workArea;
		this.feedbackFrom = feedbackFrom;
		this.status = status;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public ChallengeUserApplicationStatus getStatus() {
		return status;
	}
	public void setStatus(ChallengeUserApplicationStatus status) {
		this.status = status;
	}
	public MemberDTO(String emai, String phone, UserSex sex,
			ChallengeUserApplicationStatus status) {
		super();
		this.email = email;
	
		this.phone = phone;
		this.sex = sex;
		
		this.status = status;
	}
	
	
}
