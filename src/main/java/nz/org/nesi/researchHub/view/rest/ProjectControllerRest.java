package nz.org.nesi.researchHub.view.rest;

import com.wordnik.swagger.annotations.Api;
import nz.org.nesi.researchHub.control.ProjectControls;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pm.pojo.Project;
import pm.pojo.ProjectWrapper;

import java.util.List;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 13/12/13
 * Time: 12:01 PM
 */
@Controller
@RequestMapping(value = "/projects")
@Api( value = "/projects", description = "Manage and display projects" )
public class ProjectControllerRest {

    @Autowired
    private ProjectControls projectControls;

    @RequestMapping(value = "/{projectIdOrCode}", method = RequestMethod.GET)
    // this is an example how we'll do authorization later on, at the moment it won't actually enforce anything
    @PreAuthorize("hasPermission(#projectIdOrCode, 'read_project' )")
    @ResponseBody
    public ProjectWrapper getProjectWrapper(@PathVariable String projectIdOrCode) {
        return projectControls.getProjectWrapper(projectIdOrCode);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public List<Project> getProjects() {
        return projectControls.getProjects();
    }

    @RequestMapping(value = "/filter/{filter}", method = RequestMethod.GET)
    @ResponseBody
    public List<Project> filterProjects(@PathVariable String filter) {
        return projectControls.filterProjects(filter);
    }

//    DON'T IMPLEMENT EDIT PROJECT YET, EXPOSING THE PROJECTWRAPPER OBJECT IS A TAD
//    UGLY, LET'S THINK OF A BETTER WAY...
//    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
//    @ResponseBody
//    public void editProjectWrapper(@PathVariable Integer id, ProjectWrapper project) throws InvalidEntityException {
//        projectControls.editProjectWrapper(id, project);
//    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable Integer id) {
        projectControls.delete(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public synchronized Integer createProjectWrapper(ProjectWrapper pw) throws InvalidEntityException {
        return projectControls.createProjectWrapper(pw);
    }
}
