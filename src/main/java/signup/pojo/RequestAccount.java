package signup.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import signup.validation.NotEmptyIfOtherFieldHasValue;

@NotEmptyIfOtherFieldHasValue.List({
        @NotEmptyIfOtherFieldHasValue(fieldName = "institution",
                                      fieldValue = "Other",
                                      dependFieldName = "otherInstitution",
                                      message = "Affiliation is compulsory"),
        @NotEmptyIfOtherFieldHasValue(
                                      fieldName = "institutionalRoleId",
                                      fieldValue = "4",
                                      dependFieldName = "otherInstitutionalRole",
                                      message = "Institutional role is compulsory") })
public class RequestAccount {

    private String department;
    private String division;
    @NotEmpty(message = "E-mail address is compulsory")
    @Email(message = "Email Address is not a valid format")
    private String email;
    private String fullName;
    @NotEmpty(message = "Affiliation is compulsory")
    private String institution;
    @NotNull(message = "Institutional role is compulsory")
    private Integer institutionalRoleId;
    @Size(max = 200, message = "Affiliation must contain max 200 characters")
    @Pattern(regexp = "^[a-zA-Z0-9 \\(\\)_\\-]*$",
             message = "Other institution contains invalid characters")
    private String otherInstitution;
    @Size(max = 100,
          message = "Institutional role must contain max 100 characters")
    @Pattern(regexp = "^[a-zA-Z _\\-]*$",
             message = "Institutional role contains invalid characters")
    private String otherInstitutionalRole;
    @Size(min = 4, max = 30,
          message = "Phone number must contain of 4-30 characters")
    @Pattern(regexp = "^((?![<>{}\\[\\]\":\\.]).)*$",
             message = "Phone number contains invalid characters")
    private String phone;
    @Size(min = 1, max = 40,
          message = "Preferred name must contain of 1-40 characters")
    @Pattern(regexp = "^((?![<>{}\\[\\]\":\\.]).)*$",
             message = "Preferred name contains invalid characters")
    private String preferredName;

    public String getDepartment() {
        return department;
    }

    public String getDivision() {
        return division;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getInstitution() {
        return institution;
    }

    public Integer getInstitutionalRoleId() {
        return institutionalRoleId;
    }

    public String getOtherInstitution() {
        return otherInstitution;
    }

    public String getOtherInstitutionalRole() {
        return otherInstitutionalRole;
    }

    public String getPhone() {
        return phone;
    }

    public String getPreferredName() {
        return preferredName;
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

    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    public void setInstitution(final String institution) {
        this.institution = institution;
    }

    public void setInstitutionalRoleId(final Integer institutionalRoleId) {
        this.institutionalRoleId = institutionalRoleId;
    }

    public void setOtherInstitution(final String otherInstitution) {
        this.otherInstitution = otherInstitution;
    }

    public void setOtherInstitutionalRole(final String otherInstitutionalRole) {
        this.otherInstitutionalRole = otherInstitutionalRole;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public void setPreferredName(final String preferredName) {
        this.preferredName = preferredName;
    }

}
