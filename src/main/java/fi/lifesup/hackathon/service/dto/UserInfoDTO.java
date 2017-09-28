package fi.lifesup.hackathon.service.dto;

import java.time.ZonedDateTime;

import fi.lifesup.hackathon.domain.UserInfo;
import fi.lifesup.hackathon.domain.enumeration.UserSex;

public class UserInfoDTO {
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
				userInfo.getWebsiteUrl(), userInfo.getSkills(), userInfo.getWorkArea(), userInfo.getFeedbackFrom());
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
