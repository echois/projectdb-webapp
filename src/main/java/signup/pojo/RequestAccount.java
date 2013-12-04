package signup.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import signup.validation.NotEmptyIfOtherFieldHasValue;

@NotEmptyIfOtherFieldHasValue.List({
  @NotEmptyIfOtherFieldHasValue(fieldName = "institution", fieldValue = "Other", dependFieldName = "otherInstitution", message="Affiliation is compulsory"),
  @NotEmptyIfOtherFieldHasValue(fieldName = "institutionalRoleId", fieldValue = "4", dependFieldName = "otherInstitutionalRole", message = "Institutional role is compulsory"),
})
public class RequestAccount {

	private String fullName;
	@Size(min = 1, max = 40, message = "Preferred name must contain of 1-40 characters")
	@Pattern(regexp = "^((?![<>{}\\[\\]\":\\.]).)*$", message = "Preferred name contains invalid characters")
	private String preferredName;
	@NotEmpty(message = "Affiliation is compulsory")
	private String institution;
	@Size(max = 200, message = "Affiliation must contain max 200 characters")
	@Pattern(regexp = "^[a-zA-Z0-9 \\(\\)_\\-]*$", message = "Other institution contains invalid characters")
	private String otherInstitution;
	private String division;
	private String department;
	@Size(min = 4, max = 30, message = "Phone number must contain of 4-30 characters")
	@Pattern(regexp = "^((?![<>{}\\[\\]\":\\.]).)*$", message = "Phone number contains invalid characters")
	private String phone;
	@NotEmpty(message = "E-mail address is compulsory")
	@Email(message = "Email Address is not a valid format")
	private String email;
	@NotNull(message = "Institutional role is compulsory")
	private Integer institutionalRoleId;
	@Size(max = 100, message = "Institutional role must contain max 100 characters")
	@Pattern(regexp = "^[a-zA-Z _\\-]*$", message = "Institutional role contains invalid characters")
	private String otherInstitutionalRole;

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

	public Integer getInstitutionalRoleId() {
		return institutionalRoleId;
	}

	public void setInstitutionalRoleId(Integer institutionalRoleId) {
		this.institutionalRoleId = institutionalRoleId;
	}

	public String getOtherInstitution() {
		return otherInstitution;
	}

	public void setOtherInstitution(String otherInstitution) {
		this.otherInstitution = otherInstitution;
	}

	public String getOtherInstitutionalRole() {
		return otherInstitutionalRole;
	}

	public void setOtherInstitutionalRole(String otherInstitutionalRole) {
		this.otherInstitutionalRole = otherInstitutionalRole;
	}

}
