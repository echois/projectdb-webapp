package nz.org.nesi.researchHub.model;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 11/12/13 Time: 9:34 AM
 */
public class GroupMembership {

    private Group group;
    private final List<Role> roles = Lists.newArrayList();

    public GroupMembership() {
    }

    public void addRole(final Role role) {
        roles.add(role);
    }

    public Group getGroup() {
        return group;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setGroup(final Group group) {
        this.group = group;
    }

    public void setRoles(final List<Role> roles) {
        this.roles.clear();
        this.roles.addAll(roles);
    }
}
