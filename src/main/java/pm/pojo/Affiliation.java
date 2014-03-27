package pm.pojo;

import java.io.Serializable;

public class Affiliation implements Serializable {

	private String department;
	private String division;
	private String institution;
	private String departmentCode;
	private String divisionCode;
	private String institutionCode;

	public String getDepartment() {
		return department;
	}

	public String getDivision() {
		return division;
	}

	public String getInstitution() {
		return institution;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public String getDivisionCode() {
		return divisionCode;
	}

	public String getInstitutionCode() {
		return institutionCode;
	}

	public void setDepartment(final String department) {
		this.department = department;
	}

	public void setDivision(final String division) {
		this.division = division;
	}

	public void setInstitution(final String institution) {
		this.institution = institution;
	}

	public void setDepartmentCode(final String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public void setDivisionCode(final String divisionCode) {
		this.divisionCode = divisionCode;
	}

	public void setInstitutionCode(final String institutionCode) {
		this.institutionCode = institutionCode;
	}
}
