package fi.lifesup.hackathon.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import fi.lifesup.hackathon.domain.enumeration.UserSex;

import fi.lifesup.hackathon.domain.enumeration.UserStatus;

/**
 * A UserList.
 */
@Entity
@Table(name = "user_list")
public class UserList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private UserSex sex;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "logo_url")
    private String logoUrl;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "nationality", nullable = false)
    private String nationality;

    @Column(name = "birthday")
    private ZonedDateTime birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @OneToOne
    @JoinColumn(unique = true)
    private UserInfo userInfo;

    @ManyToOne
    private Company company;


    @ManyToMany
    @JoinTable(name = "user_list_applications",
               joinColumns = @JoinColumn(name="user_lists_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="applications_id", referencedColumnName="ID"))
    private Set<Application> applications = new HashSet<>();
         
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public UserList email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public UserList password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public UserList fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public UserList phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserSex getSex() {
        return sex;
    }

    public UserList sex(UserSex sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(UserSex sex) {
        this.sex = sex;
    }

    public String getCompanyName() {
        return companyName;
    }

    public UserList companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public UserList jobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public UserList logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCountry() {
        return country;
    }

    public UserList country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public UserList city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNationality() {
        return nationality;
    }

    public UserList nationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public ZonedDateTime getBirthday() {
        return birthday;
    }

    public UserList birthday(ZonedDateTime birthday) {
        this.birthday = birthday;
        return this;
    }

    public void setBirthday(ZonedDateTime birthday) {
        this.birthday = birthday;
    }

    public UserStatus getStatus() {
        return status;
    }

    public UserList status(UserStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public UserList userInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Company getCompany() {
        return company;
    }

    public UserList company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public UserList applications(Set<Application> applications) {
        this.applications = applications;
        return this;
    }

    public UserList addApplications(Application application) {
        applications.add(application);
        application.getUsers().add(this);
        return this;
    }

    public UserList removeApplications(Application application) {
        applications.remove(application);
        application.getUsers().remove(this);
        return this;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserList userList = (UserList) o;
        if(userList.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userList.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserList{" +
            "id=" + id +
            ", email='" + email + "'" +
            ", password='" + password + "'" +
            ", fullName='" + fullName + "'" +
            ", phone='" + phone + "'" +
            ", sex='" + sex + "'" +
            ", companyName='" + companyName + "'" +
            ", jobTitle='" + jobTitle + "'" +
            ", logoUrl='" + logoUrl + "'" +
            ", country='" + country + "'" +
            ", city='" + city + "'" +
            ", nationality='" + nationality + "'" +
            ", birthday='" + birthday + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
