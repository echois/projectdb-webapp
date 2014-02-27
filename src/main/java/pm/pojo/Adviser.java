package pm.pojo;

import java.io.Serializable;

import common.util.AffiliationUtil;

public class Adviser implements Serializable {

    private String affiliation;
    private String department;
    private String division;
    private String email;
    private String endDate;
    private String fullName;
    private Integer id;
    private String institution;
    private Integer isAdmin;
    private String lastModified;
    private String notes;
    private Integer numProjects;
    private String phone;
    private String pictureUrl;
    private String startDate;
    private String tuakiriUniqueId;

    public String getAffiliation() {
        final AffiliationUtil af = new AffiliationUtil();
        return af.createAffiliationString(institution, division, department);
    }

    public String getDepartment() {
        return department;
    }

    public String getDivision() {
        return division;
    }

    public String getEmail() {
        return email;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getId() {
        return id;
    }

    public String getInstitution() {
        return institution;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getNotes() {
        return notes;
    }

    public Integer getNumProjects() {
        return numProjects;
    }

    public String getPhone() {
        return phone;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getTuakiriUniqueId() {
        return tuakiriUniqueId;
    }

    public void setAffiliation(final String a) {
        final AffiliationUtil af = new AffiliationUtil();
        department = af.getDepartmentFromAffiliationString(a);
        division = af.getDivisionFromAffiliationString(a);
        institution = af.getInstitutionFromAffiliationString(a);
    }

    public void setDepartment(final String department) {
        this.department = department;
    }

    public void setDivision(final String division) {
        this.division = division;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setInstitution(final String institution) {
        this.institution = institution;
    }

    public void setIsAdmin(final Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setLastModified(final String lastModified) {
        this.lastModified = lastModified;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }

    public void setNumProjects(final Integer numProjects) {
        this.numProjects = numProjects;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public void setPictureUrl(final String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    public void setTuakiriUniqueId(final String tuakiriUniqueId) {
        this.tuakiriUniqueId = tuakiriUniqueId;
    }

    @Override
    public String toString() {
        return "Adviser{" + "numProjects=" + numProjects + ", fullName='"
                + fullName + '\'' + ", email='" + email + '\'' + ", phone='"
                + phone + '\'' + ", institution='" + institution + '\''
                + ", division='" + division + '\'' + ", department='"
                + department + '\'' + ", pictureUrl='" + pictureUrl + '\''
                + ", startDate='" + startDate + '\'' + ", endDate='" + endDate
                + '\'' + ", notes='" + notes + '\'' + ", tuakiriUniqueId='"
                + tuakiriUniqueId + '\'' + ", isAdmin=" + isAdmin + ", id="
                + id + '}';
    }

}
