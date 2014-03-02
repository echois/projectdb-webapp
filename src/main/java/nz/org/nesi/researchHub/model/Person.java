package nz.org.nesi.researchHub.model;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 10/12/13 Time: 3:30 PM
 */
public class Person {

    private String department;
    private String division;

    private String email;
    private String firstName;
    private List<GroupMembership> groups = Lists.newArrayList();
    private Integer id;
    private String institution;
    private String lastName;
    private String middleNames;
    private String nesiUniqueId;
    private String phone;

    private String startDate;

    public void addGroup(final GroupMembership gm) {
        groups.add(gm);
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

    public String getFirstName() {
        return firstName;
    }

    public List<GroupMembership> getGroups() {
        return groups;
    }

    public String getInstitution() {
        return institution;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleNames() {
        return middleNames;
    }

    public String getNesiUniqueId() {
        return nesiUniqueId;
    }

    public String getPhone() {
        return phone;
    }

    public String getStartDate() {
        return startDate;
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

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public void setGroups(final List<GroupMembership> groups) {
        this.groups = groups;
    }

    public void setInstitution(final String institution) {
        this.institution = institution;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public void setMiddleNames(final String middleNames) {
        this.middleNames = middleNames;
    }

    public void setNesiUniqueId(final String nesiUniqueId) {
        this.nesiUniqueId = nesiUniqueId;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }
}
