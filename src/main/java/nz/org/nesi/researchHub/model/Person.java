package nz.org.nesi.researchHub.model;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 10/12/13
 * Time: 3:30 PM
 */
public class Person {

    private Integer id;
    private String nesiUniqueId;

    private String firstName;
    private String middleNames;
    private String lastName;
	private String email;
	private String phone;
	private String institution;
	private String division;
	private String department;
	private String startDate;

    private List<GroupMembership> groups = Lists.newArrayList();

    public String getNesiUniqueId() {
        return nesiUniqueId;
    }

    public void setNesiUniqueId(String nesiUniqueId) {
        this.nesiUniqueId = nesiUniqueId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleNames() {
        return middleNames;
    }

    public void setMiddleNames(String middleNames) {
        this.middleNames = middleNames;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<GroupMembership> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupMembership> groups) {
        this.groups = groups;
    }

    public void addGroup(GroupMembership gm) {
        this.groups.add(gm);
    }
}
