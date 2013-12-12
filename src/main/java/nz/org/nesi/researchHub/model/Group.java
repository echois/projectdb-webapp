package nz.org.nesi.researchHub.model;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 10/12/13
 * Time: 4:29 PM
 */
public class Group {

    private Integer id;
    private String groupName;
    private Set<Role> availableRoles = Sets.newHashSet();

    public Group() {
    }

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public boolean isAllowedRole(Role role) {
        if ( availableRoles.size() == 0 ) {
            return true;
        }
        return allowedRoles().contains(role);
    }

    private Set<Role> allowedRoles() {
        return availableRoles;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


}
