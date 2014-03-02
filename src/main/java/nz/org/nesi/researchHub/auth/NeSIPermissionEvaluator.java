package nz.org.nesi.researchHub.auth;

import java.io.Serializable;

import nz.org.nesi.researchHub.control.ProjectControls;
import nz.org.nesi.researchHub.model.GroupMembership;
import nz.org.nesi.researchHub.model.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import pm.pojo.ProjectWrapper;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 11/12/13 Time: 4:36 PM
 */
public class NeSIPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private ProjectControls projectControls;

    @Override
    public boolean hasPermission(final Authentication authentication,
            final Object targetDomainObject, final Object permission) {

        if ("anonymousUser".equals(authentication.getPrincipal())) {
            return false;
        }
        final NeSIUserDetailsImpl user = (NeSIUserDetailsImpl) authentication
                .getPrincipal();
        System.out.println("HAS PERMISSION: " + permission);
        final Person person = user.getPerson();

        final boolean allowed = personCanReadProject(person,
                (String) targetDomainObject);
        return allowed;
    }

    @Override
    public boolean hasPermission(final Authentication authentication,
            final Serializable targetId, final String targetType,
            final Object permission) {
        throw new RuntimeException(
                "Permission evaluator method not implemented yet");
    }

    public boolean personCanReadProject(final Person p, final String projectId) {

        final ProjectWrapper pw = projectControls.getProjectWrapper(projectId);

        for (final GroupMembership gm : p.getGroups()) {

            System.out.println("GROUP: " + gm.getGroup().getGroupName());
            final String projectName = pw.getProject().getName();
            final String groupName = gm.getGroup().toString();
            if (projectName.equalsIgnoreCase(groupName)) {

                return true;
            }
        }

        return true;

    }

}
