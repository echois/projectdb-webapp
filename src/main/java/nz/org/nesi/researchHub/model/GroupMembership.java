package nz.org.nesi.researchHub.model;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 11/12/13
 * Time: 9:34 AM
 */
public class GroupMembership {

    private Group group;
    private final List<Role> roles = Lists.newArrayList();

    public GroupMembership() {
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles.clear();
        this.roles.addAll(roles);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }
}
