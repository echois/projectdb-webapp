package pm.pojo;

import common.util.AffiliationUtil;

import java.io.Serializable;

public class Adviser implements Serializable {

	private Integer id;
	private Integer numProjects;
	private String fullName;
	private String email;
	private String phone;
	private String institution;
	private String division;
	private String department;
	private String pictureUrl;
	private String startDate;
	private String endDate;
	private String notes;
	private String tuakiriUniqueId;
	private Integer isAdmin;
	private String lastModified;
	private String affiliation; 

	public Integer getId() {
		return id;
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

	public Integer getNumProjects() {
		return numProjects;
	}

	public void setNumProjects(Integer numProjects) {
		this.numProjects = numProjects;
	}

	public String getTuakiriUniqueId() {
		return tuakiriUniqueId;
	}

	public void setTuakiriUniqueId(String tuakiriUniqueId) {
		this.tuakiriUniqueId = tuakiriUniqueId;
	}

	public Integer getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
	}


    @Override
    public String toString() {
        return "Adviser{" +
                "numProjects=" + numProjects +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", institution='" + institution + '\'' +
                ", division='" + division + '\'' +
                ", department='" + department + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", notes='" + notes + '\'' +
                ", tuakiriUniqueId='" + tuakiriUniqueId + '\'' +
                ", isAdmin=" + isAdmin +
                ", id=" + id +
                '}';
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
