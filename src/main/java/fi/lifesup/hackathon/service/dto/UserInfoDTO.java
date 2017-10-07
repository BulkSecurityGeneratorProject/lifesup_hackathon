package fi.lifesup.hackathon.service.dto;

import java.time.ZonedDateTime;

import fi.lifesup.hackathon.domain.UserInfo;
import fi.lifesup.hackathon.domain.enumeration.UserSex;

public class UserInfoDTO {
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
	
	public UserInfoDTO(){
		
	}
	public UserInfoDTO( String phone,UserSex sex,String companyName,String jobTitle,String logoUrl,String country,String city,String nationality,ZonedDateTime birthday,String introduction,String twitterUrl,String linkedInUrl,String skills,String workArea,String feedbackFrom, String websiteUrl)
	{
		this.phone=phone;
		this.sex=sex;
		this.companyName=companyName;
		this.jobTitle=jobTitle;
		this.logoUrl=logoUrl;
		this.country=country;
		this.city=city;
		this.nationality=nationality;
		this.linkedInUrl=linkedInUrl;
		this.twitterUrl=twitterUrl;
		this.workArea=workArea;
		this.feedbackFrom=feedbackFrom;
		this.skills=skills;
		this.birthday=birthday;
		this.nationality=nationality;
		this.introduction=introduction;
		this.websiteUrl=websiteUrl;
	
	}
	public UserInfoDTO(UserInfo userInfo) {
		this(userInfo.getPhone(), userInfo.getSex(), userInfo.getCompanyName(), userInfo.getJobTitle(),
				userInfo.getLogoUrl(), userInfo.getCountry(), userInfo.getCity(), userInfo.getNationality(),
				userInfo.getBirthday(), userInfo.getIntroduction(), userInfo.getTwitterUrl(), userInfo.getLinkedInUrl(),
				userInfo.getSkills(), userInfo.getWorkArea(), userInfo.getFeedbackFrom(), userInfo.getWebsiteUrl());
	}
	
	public UserInfoDTO( String email, String firstName, String lastName, UserInfo userInfo) {
		this(userInfo.getPhone(), userInfo.getSex(), userInfo.getCompanyName(), userInfo.getJobTitle(),
				userInfo.getLogoUrl(), userInfo.getCountry(), userInfo.getCity(), userInfo.getNationality(),
				userInfo.getBirthday(), userInfo.getIntroduction(), userInfo.getTwitterUrl(), userInfo.getLinkedInUrl(),
				userInfo.getSkills(), userInfo.getWorkArea(), userInfo.getFeedbackFrom(), userInfo.getWebsiteUrl());
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getfirstName() {
		return firstName;
	}
	public void setfirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setSex(UserSex sex) {
		this.sex = sex;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public void setBirthday(ZonedDateTime birthday) {
		this.birthday = birthday;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public void setTwitterUrl(String twitterUrl) {
		this.twitterUrl = twitterUrl;
	}
	public void setLinkedInUrl(String linkedInUrl) {
		this.linkedInUrl = linkedInUrl;
	}
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}
	public void setWorkArea(String workArea) {
		this.workArea = workArea;
	}
	public void setFeedbackFrom(String feedbackFrom) {
		this.feedbackFrom = feedbackFrom;
	}
	public String getPhone() {
		return phone;
	}

	public UserSex getSex() {
		return sex;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public String getCountry() {
		return country;
	}

	public String getCity() {
		return city;
	}

	public String getNationality() {
		return nationality;
	}

	public ZonedDateTime getBirthday() {
		return birthday;
	}

	public String getIntroduction() {
		return introduction;
	}

	public String getTwitterUrl() {
		return twitterUrl;
	}

	public String getLinkedInUrl() {
		return linkedInUrl;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public String getSkills() {
		return skills;
	}

	public String getWorkArea() {
		return workArea;
	}

	public String getFeedbackFrom() {
		return feedbackFrom;
	}
	@Override
    public String toString() {
        return "UserInfoDTO{" +
            "phone='" + phone + '\'' +
            ", sex='" + sex + '\'' +
            ", companyName='" + companyName + '\'' +
            ", jobTitle='" + jobTitle + '\'' +
            ", logoUrl=" + logoUrl +
            ", country='" + country + '\'' +
            ", city=" + city +
            ", nationality='" + nationality + '\'' +
            ", birthday='" + birthday + '\'' +
            ", introduction=" + introduction +
            ", twitterUrl='" + twitterUrl + '\'' +
            ", linkedInUrl=" + linkedInUrl +
            ", skills=" + skills +
            ", workArea='" + workArea + '\'' +
            ", feedbackFrom=" + feedbackFrom +
            "}";
    }

}
