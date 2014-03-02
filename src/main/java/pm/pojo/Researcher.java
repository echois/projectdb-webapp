package pm.pojo;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import common.util.AffiliationUtil;

public class Researcher implements Serializable {

    private String affiliation;
    private String department;
    private String division;
    private String email;
    private String endDate;
    private String fullName;
    private Integer id;
    private String institution;
    private Integer institutionalRoleId;
    private String institutionalRoleName;
    private String lastModified;
    private String notes;
    private String phone;
    private String pictureUrl;
    private String preferredName;
    private String startDate;
    private Integer statusId;
    private String statusName;

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

    public Integer getInstitutionalRoleId() {
        return institutionalRoleId;
    }

    public String getInstitutionalRoleName() {
        return institutionalRoleName;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getNotes() {
        return notes;
    }

    public String getPhone() {
        return phone;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getPreferredName() {
        return preferredName;
    }

    public String getStartDate() {
        return startDate;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String getStatusName() {
        return statusName;
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

    public void setInstitutionalRoleId(final Integer institutionalRoleId) {
        this.institutionalRoleId = institutionalRoleId;
    }

    public void setInstitutionalRoleName(final String institutionalRoleName) {
        this.institutionalRoleName = institutionalRoleName;
    }

    public void setLastModified(final String lastModified) {
        this.lastModified = lastModified;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public void setPictureUrl(final String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setPreferredName(final String preferredName) {
        this.preferredName = preferredName;
    }

    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    public void setStatusId(final Integer statusId) {
        this.statusId = statusId;
        if (statusId.equals(2) && endDate.equals("")) {
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            endDate = df.format(new Date());
        } else if (statusId.equals(7) && endDate.equals("")) {
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            endDate = df.format(new Date(System.currentTimeMillis() + 1000L
                    * 60 * 60 * 24 * 30));
        }
    }

    public void setStatusName(final String string) {
        statusName = string;
    }

}
