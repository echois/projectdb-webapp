package signup.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class RequestAccount {

	@Size(min = 5, max = 50, message="Full name must contain of 5-50 characters")
	private String fullName;
	private String preferredName;
	@NotNull
	@NotEmpty
	private String institution;
	private String division;
	private String department;
	@Size(min = 4, max = 30, message="Contact phone number must contain between 4-30 characters")
	private String phone;
	@NotNull
	@NotEmpty
	@Email
	private String email;
	@NotNull
	@NotEmpty
	private String institutionalRole;

	public String getPreferredName() {
		return preferredName;
	}

	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
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

	public String getInstitutionalRole() {
		return institutionalRole;
	}

	public void setInstitutionalRole(String institutionalRole) {
		this.institutionalRole = institutionalRole;
	}

}
