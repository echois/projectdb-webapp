package nz.org.nesi.researchHub.model;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 10/12/13 Time: 4:29 PM
 */
public class Group {

    private final Set<Role> availableRoles = Sets.newHashSet();
    private String groupName;
    private Integer id;

    public Group() {
    }

    public Group(final String groupName) {
        this.groupName = groupName;
    }

    private Set<Role> allowedRoles() {
        return availableRoles;
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean isAllowedRole(final Role role) {
        if (availableRoles.size() == 0) {
            return true;
        }
        return allowedRoles().contains(role);
    }

    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }

}
