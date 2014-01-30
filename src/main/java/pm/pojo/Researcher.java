package pm.pojo;

import common.util.AffiliationUtil;

public class Researcher {

	private Integer id;
	private String fullName;
	private String preferredName;
	private Integer statusId;
	private String statusName;
	private String email;
	private String phone;
	private String institution;
	private String division;
	private String department;
	private Integer institutionalRoleId;
	private String institutionalRoleName;
	private String pictureUrl;
	private String startDate;
	private String endDate;
	private String notes;
	private String lastModified;
	private String affiliation;

	public Integer getId() {
		return id;
	}

	public Integer getInstitutionalRoleId() {
		return institutionalRoleId;
	}

	public void setInstitutionalRoleId(Integer institutionalRoleId) {
		this.institutionalRoleId = institutionalRoleId;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPreferredName() {
		return preferredName;
	}

	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getInstitutionalRoleName() {
		return institutionalRoleName;
	}

	public void setInstitutionalRoleName(String institutionalRoleName) {
		this.institutionalRoleName = institutionalRoleName;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String string) {
		this.statusName = string;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	
	public String getAffiliation() {
		AffiliationUtil af = new AffiliationUtil();
		return af.createAffiliationString(institution, division, department);
	}
	
	public void setAffiliation(String a) {
		AffiliationUtil af = new AffiliationUtil();
		this.department = af.getDepartmentFromAffiliationString(a);
		this.division = af.getDivisionFromAffiliationString(a);
		this.institution = af.getInstitutionFromAffiliationString(a);
	}

}
