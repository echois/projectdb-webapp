package nz.org.nesi.researchHub.control;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import nz.org.nesi.researchHub.exceptions.DatabaseException;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
import nz.org.nesi.researchHub.exceptions.OutOfDateException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pm.pojo.APLink;
import pm.pojo.Project;
import pm.pojo.ProjectWrapper;
import pm.pojo.RPLink;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 9/12/13
 * Time: 9:38 AM
 */
public class ProjectControls extends AbstractControl {

    public static void main(String[] args) throws Exception {

        ApplicationContext context =
                new ClassPathXmlApplicationContext("rest-servlet.xml", "pm-servlet.xml", "signup-servlet.xml", "root-context.xml");


        AdviserControls ac = (AdviserControls) context.getBean("adviserControls");

        ProjectControls pc = (ProjectControls) context.getBean("projectControls");

//        for (Project p : pc.getProjects()) {
//            System.out.println(p);
//        }

        System.out.println(pc.getProjectWrapper("uoa00155"));

    }


    /**
     * Validates the project wrapper object.
     *
     * @param pw the project wrapper
     * @throws InvalidEntityException if there is something wrong with the projectwrapper object or one of the associated objects
     */
    public static void validateProject(ProjectWrapper pw) throws InvalidEntityException {
    	if (pw.getProject().getName()==null) {
    		throw new InvalidEntityException("Project does not have a title", Project.class, "name");
    	}
        if (pw.getProject().getName().trim().equals("")) {
            pw.setErrorMessage("A project must have a title");
            throw new InvalidEntityException("Project does not have a title", Project.class, "name");
        }

        // Exactly one PI?
        int count = 0;
        for (RPLink rp : pw.getRpLinks()) {
            if (rp.getResearcherRoleId() == 1) {
                count += 1;
            }
        }
        if (count != 1) {
            throw new InvalidEntityException("There must be exactly 1 project owner on a project", ProjectWrapper.class, "rpLinks");
        }

        // Exactly one primary project?
        count = 0;
        for (APLink ap : pw.getApLinks()) {
            if (ap.getAdviserRoleId() == 1) {
                count += 1;
            }
        }
        if (count != 1) {
            throw new InvalidEntityException("There must be exactly 1 primary project adviser on a project", ProjectWrapper.class, "apLinks");
        }
        // At least one HPC
        if (pw.getProjectFacilities().isEmpty()) {
            throw new InvalidEntityException("There must be at least one HPC facility associated with the project", ProjectWrapper.class, "projectFacilities");
        }

    }

    /**
     * Gets the project (with associted objects) with the specified id or project code.
     *
     * @param projectIdOrCode the project id or project code
     * @return the Project
     */
    public ProjectWrapper getProjectWrapper(String projectIdOrCode) {

        try {
            int i = Integer.parseInt(projectIdOrCode);
            try {
                ProjectWrapper pw = projectDao.getProjectWrapperById(i);
                return pw;
            } catch (Exception e) {
                throw new DatabaseException("Could not retrieve project with id: " + projectIdOrCode, e);
            }
        } catch (Exception e) {
            try {
            ProjectWrapper pw = projectDao.getProjectWrapperByProjectCode(projectIdOrCode);
            return pw;
            } catch (Exception e2) {
                throw new DatabaseException("Could not retrieve project with code: "+ projectIdOrCode, e2);
            }
        }

    }

    /**
     * Utility method that forwards to {@link #getProjectWrapper(String)}.
     * @param id the id of the project
     * @return the projectWrapper object
     */
    public ProjectWrapper getProjectWrapper(Integer id) {
        return getProjectWrapper(id.toString());
    }

    /**
     * Get all projects in the database.
     *
     * Mind, this doesn't return project wrapper objects, just the plain objects. We could change that, not sure about
     * performance in that case.
     *
     * @return all projects
     */
    public List<Project> getProjects() {
        try {
            List<Project> ps = projectDao.getProjects();
            return ps;
        } catch (Exception e) {
            throw new DatabaseException("Could not retrieve projects", e);
        }
    }

    /**
     * Get all projects that contain the specified filter string (case-insensitive) in one or more of the project properties.
     *
     * Mind, this returns only the Project object, not the ProjectWrapper ones (see: {@link #getProjects()}.
     *
     * @param filter the filter string, can't be empty
     * @return all projects matching the filter
     */
    public List<Project> filterProjects(String filter) {

        if (StringUtils.isEmpty(filter)) {
            throw new IllegalArgumentException("Can't filter projects using empty string, use getProjects method instead.");
        }

        filter = filter.toLowerCase();
        List<Project> filtered = new LinkedList<Project>();

        for (Project p : getProjects()) {
            if (p.getName().toLowerCase().contains(filter) || p.getDescription().toLowerCase().contains(filter) ||
                    p.getHostInstitution().toLowerCase().contains(filter) || p.getNotes().toLowerCase().contains(filter) ||
                    p.getProjectCode().toLowerCase().contains(filter) || p.getProjectTypeName().toLowerCase().contains(filter) ||
                    p.getRequirements().toLowerCase().contains(filter) || p.getTodo() != null && p.getTodo().toLowerCase().contains(filter))
                filtered.add(p);
        }

        return filtered;

    }

    /**
     * Edit a project wrapper object.
     *
     * @param project the updated project wrapper
     * @throws InvalidEntityException if there is something wrong with either the projectwrapper or associated objects
     * @throws OutOfDateException 
     */
    public void editProjectWrapper(@PathVariable Integer id, @RequestBody ProjectWrapper project) throws InvalidEntityException, OutOfDateException {

        validateProject(project);
        if (project.getProject() != null) {
            // might throw database exception if project does not already exist
            ProjectWrapper temp = getProjectWrapper(id.toString());

            project.getProject().setId(id);

            // great, no exception, means an project with this id does already exist,
            // Compare timestamps to prevent accidental overwrite
            if (project.getProject().getLastModified() != temp.getProject().getLastModified() || !project.getProject().getLastModified().equals(temp.getProject().getLastModified())) {
            	throw new OutOfDateException("Incorrect timestamp");
            }
            project.getProject().setLastModified((int) (System.currentTimeMillis() / 1000));
            try {
                projectDao.updateProjectWrapper(id, project);
            } catch (Exception e) {
                throw new DatabaseException("Can't update project with id " + id, e);
            }
        } else {
            throw new InvalidEntityException("Can't edit project. No project object provided.", Project.class, "id");
        }

    }
    
    /**
     * Edit one field of a project wrapper object.
     *
     * @param project the updated project wrapper
     * @throws InvalidEntityException if there is something wrong with either the projectwrapper or associated objects
     * @throws OutOfDateException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws ClassNotFoundException 
     */
    public void editProjectWrapper(Integer id, String object, String field, String timestamp, String data) throws InvalidEntityException, OutOfDateException {
    	Integer ts = null;
    	if (!timestamp.equals("null") && !timestamp.equals("force")) ts = Integer.parseInt(timestamp);
        if (id != null) {
            // might throw database exception if project does not already exist
            ProjectWrapper pw = getProjectWrapper(id.toString());
            // great, no exception, means an project with this id does already exist,
            // Compare timestamps to prevent accidental overwrite
            // Covers the case where timestamps don't match, and where the correct timestamp is null, and force=true
            boolean nullMatch = pw.getProject().getLastModified()==null && ts==null;
            boolean match = pw.getProject().getLastModified()!=null && ts!=null && pw.getProject().getLastModified().equals(ts);
            boolean force = timestamp.equals("force");
            if (!force && !(nullMatch || match)) {
            	throw new OutOfDateException("Incorrect timestamp. Project has been modified since you last loaded it.");
            }
            pw.getProject().setLastModified((int) (System.currentTimeMillis() / 1000));
            try {
            	Class<ProjectWrapper> c = ProjectWrapper.class;
            	Method getPojo = c.getDeclaredMethod ("get" + object);
            	Object pojo = getPojo.invoke (pw);
	            Class<?> pojoClass = Class.forName("pm.pojo." + object);
	            Method set = pojoClass.getDeclaredMethod ("set" + field, String.class);
	            set.invoke (pojo, data);
	            projectDao.updateProjectWrapper(id, pw);
            } catch (NoSuchMethodException e) {
            	throw new InvalidEntityException(field + " does not exist within " + object, ProjectWrapper.class, object);
            } catch (InvocationTargetException e) {
            	throw new InvalidEntityException("Unable to fetch " + object + " for " + pw.getProject().getProjectCode(), ProjectWrapper.class, object);
            } catch (IllegalAccessException e) {
            	throw new InvalidEntityException("It is illegal to fetch " + object, ProjectWrapper.class, object);
            } catch (ClassNotFoundException e) {
            	throw new InvalidEntityException(object + " is not a valid POJO", ProjectWrapper.class, object);
			} catch (Exception e) {
                throw new DatabaseException("Can't update project with id " + id, e);
            }
        } else {
            throw new InvalidEntityException("Can't edit project. No id provided.", Project.class, "id");
        }

    }

    /**
     * Delete the project wrapper object with the specified id.
     *
     * @param id the id
     */
    public void delete( Integer id) {

        try {
            this.projectDao.deleteProjectWrapper(id);
        } catch (Exception e) {
            throw new DatabaseException("Can't delete ProjectWrapper with id " + id, e);
        }

    }

    /**
     * Creates a new project in the database.
     *
     * @param pw the projectWrapper object
     * @return the id of the new project
     */
    public synchronized Integer createProjectWrapper(ProjectWrapper pw) throws InvalidEntityException {

        Project p = pw.getProject();

        if (p.getId() != null) {
            throw new IllegalArgumentException("Can't create project that already has an id.");
        }

        this.validateProject(pw);

        String projectCode = this.projectDao.getNextProjectCode(p.getHostInstitution());
        pw.getProject().setProjectCode(projectCode);
        pw.getProject().setLastModified((int) (System.currentTimeMillis() / 1000));
        try {
            Integer pid = this.projectDao.createProjectWrapper(pw);
            return pid;
        } catch (Exception e) {
            throw new DatabaseException("Could not create Project in database.", e);
        }

    }
    
    /**
     * Returns a list of institutions.
     *
     * @return a list of institutions
     * @throws Exception 
     */
    public List<String> getInstitutions() throws Exception {
    	return this.projectDao.getInstitutions();
    }
}
