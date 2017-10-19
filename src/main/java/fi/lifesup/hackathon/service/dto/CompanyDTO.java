package fi.lifesup.hackathon.service.dto;

public class CompanyDTO {
	private String name;
	private String companyName;
	private String phone;
	private String email;
	private String additional;
	
	public CompanyDTO() {
		// TODO Auto-generated constructor stub
	}

	public CompanyDTO(String name, String companyName, String phone, String email, String additional) {
		super();
		this.name = name;
		this.companyName = companyName;
		this.phone = phone;
		this.email = email;
		this.additional = additional;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAdditional() {
		return additional;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}
	
}
