package nz.org.nesi.researchHub.auth;

import nz.org.nesi.researchHub.control.ProjectControls;
import nz.org.nesi.researchHub.model.GroupMembership;
import nz.org.nesi.researchHub.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import pm.pojo.ProjectWrapper;

import java.io.Serializable;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 11/12/13
 * Time: 4:36 PM
 */
public class NeSIPermissionEvaluator implements PermissionEvaluator {


    @Autowired
    private ProjectControls projectControls;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {

        if ( "anonymousUser".equals(authentication.getPrincipal())) {
            return false;
        }
        NeSIUserDetailsImpl user = (NeSIUserDetailsImpl)authentication.getPrincipal();
        System.out.println("HAS PERMISSION: "+permission);
        Person person = user.getPerson();

        boolean allowed = personCanReadProject(person, (String)targetDomainObject);
        return allowed;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        throw new RuntimeException("Permission evaluator method not implemented yet");
    }

    public boolean personCanReadProject(Person p, String projectId) {

        ProjectWrapper pw = projectControls.getProjectWrapper(projectId);


        for ( GroupMembership gm : p.getGroups() ) {

            System.out.println("GROUP: "+gm.getGroup().getGroupName());
            String projectName = pw.getProject().getName();
            String groupName = gm.getGroup().toString();
            if ( projectName.equalsIgnoreCase(groupName)) {



                return true;
            }
        }

        return true;


    }

}
