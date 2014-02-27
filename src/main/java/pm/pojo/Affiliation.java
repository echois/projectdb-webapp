package pm.pojo;

import java.io.Serializable;

public class Affiliation implements Serializable {

    private String department;
    private String division;
    private String institution;

    public String getDepartment() {
        return department;
    }

    public String getDivision() {
        return division;
    }

    public String getInstitution() {
        return institution;
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

}
