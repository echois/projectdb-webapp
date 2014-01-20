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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import pm.pojo.APLink;
import pm.pojo.AdviserAction;
import pm.pojo.Attachment;
import pm.pojo.Facility;
import pm.pojo.FollowUp;
import pm.pojo.Kpi;
import pm.pojo.KpiCode;
import pm.pojo.Project;
import pm.pojo.ProjectFacility;
import pm.pojo.ProjectKpi;
import pm.pojo.ProjectProperty;
import pm.pojo.ProjectStatus;
import pm.pojo.ProjectType;
import pm.pojo.ProjectWrapper;
import pm.pojo.RPLink;
import pm.pojo.ResearchOutput;
import pm.pojo.ResearchOutputType;
import pm.pojo.Review;

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
        if (pw.getRpLinks().isEmpty()) {
            throw new InvalidEntityException("There must be exactly 1 project owner on a project", ProjectWrapper.class, "rpLinks");
        }
        // Exactly one primary project?
        if (pw.getApLinks().isEmpty()) {
            throw new InvalidEntityException("There must be exactly 1 primary project adviser on a project", ProjectWrapper.class, "apLinks");
        }
        // At least one HPC
        if (pw.getProjectFacilities().isEmpty()) {
            throw new InvalidEntityException("There must be at least one HPC facility associated with the project", ProjectWrapper.class, "projectFacilities");
        }
        
        for (RPLink rp : pw.getRpLinks()) {
        	for (RPLink other :pw.getRpLinks()) {
        		if (rp.getResearcherId().equals(other.getResearcherId()) && !rp.getResearcherRoleId().equals(other.getResearcherRoleId())) {
        			throw new InvalidEntityException("A researcher can only have one role on a project", ProjectWrapper.class, "rpLinks");
        		}
        	}
        }
        
        for (APLink ap : pw.getApLinks()) {
        	for (APLink other :pw.getApLinks()) {
        		if (ap.getAdviserId().equals(other.getAdviserId()) && !ap.getAdviserRoleId().equals(other.getAdviserRoleId())) {
        			throw new InvalidEntityException("An adviser can only have one role on a project", ProjectWrapper.class, "apLinks");
        		}
        	}
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
     * Gets the project properties associated with the specified project id.
     *
     * @param projectId the project
     * @return the ProjectProperties
     */
    public List<ProjectProperty> getProjectProperties(Integer id) {
    	try {
    		return this.projectDao.getProjectProperties(id);
    	} catch (Exception e) {
            throw new DatabaseException("Could not retrieve properties", e);
        }
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
                    p.getHostInstitution().toLowerCase().contains(filter) || p.getNotes() != null && p.getNotes().toLowerCase().contains(filter) ||
                    p.getProjectCode().toLowerCase().contains(filter) || p.getProjectTypeName().toLowerCase().contains(filter) ||
                    p.getRequirements() != null && p.getRequirements().toLowerCase().contains(filter) || p.getTodo() != null && p.getTodo().toLowerCase().contains(filter))
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
        if (id != null) {
            // might throw database exception if project does not already exist
            ProjectWrapper pw = getProjectWrapper(id.toString());
            // great, no exception, means an project with this id does already exist,
            // Compare timestamps to prevent accidental overwrite
            boolean force = timestamp.equals("force");
            if (!force && timestamp.equals(pw.getProject().getLastModified())) {
            	throw new OutOfDateException("Incorrect timestamp. Project has been modified since you last loaded it.");
            }
            boolean deep = false;
            boolean attachment = object.contains("Attachments");
            String method = "get" + object;
            Class<?> pojoClass = null;
            if (object.contains("_")) {
            	deep = true;
            	method = "get" + object.split("_")[0];
            }
            try {
            	if (object.equals("projectFacilities")) {
            		List<ProjectFacility> projectFacilities = new LinkedList<ProjectFacility>();
            		for (String facId : data.split(",")) {
            			ProjectFacility pf = new ProjectFacility();
            			pf.setProjectId(id);
            			pf.setFacilityId(Integer.valueOf(facId));
            			projectFacilities.add(pf);
            		}
					pw.setProjectFacilities(projectFacilities);
            	} else {
	            	Class<ProjectWrapper> c = ProjectWrapper.class;
	            	Method getPojo = c.getDeclaredMethod (method);
	            	Object pojo = getPojo.invoke (pw);
	            	pojoClass = pojo.getClass();
	            	if (deep) {
	            		method = "get";
	            		try {
	            			Integer.parseInt(object.split("_")[1]);
	            			getPojo = pojoClass.getDeclaredMethod(method, int.class);
		            		pojo = getPojo.invoke(pojo, Integer.parseInt(object.split("_")[1]));
	            		} catch (NumberFormatException e) {
	            			method = "get" + object.split("_")[1];
	            			getPojo = pojoClass.getDeclaredMethod(method);
		            		pojo = getPojo.invoke(pojo);
	            		}
	            		pojoClass = pojo.getClass();
	            	}
	            	if (attachment) {
	            		method = "getAttachments";
	            		getPojo = pojoClass.getDeclaredMethod(method);
	            		pojo = getPojo.invoke(pojo);
	            		pojoClass = pojo.getClass();
	            		method = "get";
            			getPojo = pojoClass.getDeclaredMethod(method, int.class);
	            		pojo = getPojo.invoke(pojo, Integer.parseInt(object.split("_")[1]));
	            		pojoClass = pojo.getClass();
	            	}
		            method = "set" + field;
		            // Try integers first, floats if that fails, then fallback to string
		            try {
		            	try {
		            		Integer intData = Integer.valueOf(data);
		            		Method set = pojoClass.getDeclaredMethod (method, Integer.class);
				            set.invoke (pojo, intData);
		            	} catch (Exception e) {
		            		Float floatData = Float.valueOf(data);
		            		Method set = pojoClass.getDeclaredMethod (method, Float.class);
				            set.invoke (pojo, floatData);
		            	}
		            } catch (Exception e) {
		            	Method set = pojoClass.getDeclaredMethod (method, String.class);
			            set.invoke (pojo, data);
		            }
            	}
            	this.validateProject(pw);
	            projectDao.updateProjectWrapper(id, pw);
            } catch (NoSuchMethodException e) {
            	throw new InvalidEntityException(pojoClass.getName() + "." + method + " is not a valid method", ProjectWrapper.class, object);
            } catch (InvocationTargetException e) {
            	throw new InvalidEntityException("Unable to " + method + " for " + pw.getProject().getProjectCode(), ProjectWrapper.class, object);
            } catch (IllegalAccessException e) {
            	throw new InvalidEntityException("It is illegal to fetch " + object, ProjectWrapper.class, object);
            } catch (ClassNotFoundException e) {
            	throw new InvalidEntityException(object + " is not a valid POJO", ProjectWrapper.class, object);
            } catch (IllegalArgumentException e) {
            	throw new InvalidEntityException(data + " does not match " + pojoClass.getName() + "." + method + "'s expected parameter", ProjectWrapper.class, object);
			} catch (Exception e) {
                throw new DatabaseException(e.getMessage(), e);
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
     * Remove the specified adviser or researcher from this project
     *
     * @param id the id
     */
    public void removeObjectLink(Integer id, int oid, String type) {
    	try {
			ProjectWrapper pw = this.projectDao.getProjectWrapperById(id);
			if (type.equals("adviser")) {
				List<APLink> aTmp = new LinkedList<APLink>();
				for (APLink a : pw.getApLinks()) {
					if (!a.getAdviserId().equals(oid)) aTmp.add(a);
				}
				pw.setApLinks(aTmp);
			} else if (type.equals("researcher")){
				List<RPLink> rTmp = new LinkedList<RPLink>();
				for (RPLink r : pw.getRpLinks()) {
					if (!r.getResearcherId().equals(oid)) rTmp.add(r);
				}
				pw.setRpLinks(rTmp);
			} else if (type.equals("kpi")) {
				pw.getProjectKpis().remove(oid);
			} else if (type.equals("researchoutput")) {
				pw.getResearchOutputs().remove(oid);
			} else if (type.equals("review")) {
				pw.getReviews().remove(oid);
			} else if (type.equals("followup")) {
				pw.getFollowUps().remove(oid);
			} else if (type.equals("adviseraction")) {
				pw.getAdviserActions().remove(oid);
			} else if (type.equals("property")) {
				this.projectDao.deleteProjectProperty(Integer.valueOf(oid));
			} else if (type.contains("Attachments")) {
				//pw.getAdviserActions().get(0).getAttachments().remove(0);
				String obj = type.split("_")[0];
				int nid = Integer.parseInt(type.split("_")[2]);
				Class<?> c = ProjectWrapper.class;
            	Method m = c.getDeclaredMethod ("get" + obj);
            	//pw.getAdviserActions()
            	Object o = m.invoke (pw);
            	c =  o.getClass();
            	m = c.getDeclaredMethod("get", int.class);
            	//.get(oid)
            	o = m.invoke(o, oid);
            	c = o.getClass();
            	m = c.getDeclaredMethod("getAttachments");
            	//.getAttachments()
            	o = m.invoke(o);
            	c = o.getClass();
            	m = c.getDeclaredMethod("remove", int.class);
            	//.remove(nid)
            	m.invoke(o, nid);
			}
			
			this.validateProject(pw);
			this.projectDao.updateProjectWrapper(id, pw);
		} catch (Exception e) {
			throw new DatabaseException("Can't delete " + oid, e);
		}
    }
    
    /**
     * Add the specified adviser to this project
     *
     * @param id the id
     */
    public void addAdviser(APLink al) {
    	try {
			ProjectWrapper pw = this.projectDao.getProjectWrapperById(al.getProjectId());
			pw.getApLinks().add(al);
			this.validateProject(pw);
			this.projectDao.updateProjectWrapper(al.getProjectId(), pw);
		} catch (Exception e) {
			throw new DatabaseException("Can't fetch project with id " + al.getProjectId(), e);
		}
    }
    
    /**
     * Add the specified researcher to this project
     *
     * @param id the id
     */
    public void addResearcher(RPLink rl) {
    	try {
			ProjectWrapper pw = this.projectDao.getProjectWrapperById(rl.getProjectId());
			pw.getRpLinks().add(rl);
			this.validateProject(pw);
			this.projectDao.updateProjectWrapper(rl.getProjectId(), pw);
		} catch (Exception e) {
			throw new DatabaseException("Can't fetch project with id " + rl.getProjectId(), e);
		}
    }
    
    /**
     * Add the specified project_kpi to this project
     *
     * @param id the id
     * @throws InvalidEntityException 
     */
    public void addKpi(ProjectKpi pk) throws Exception {
		ProjectWrapper pw = this.projectDao.getProjectWrapperById(pk.getProjectId());
		pw.getProjectKpis().add(pk);
		this.validateProject(pw);
		try {
			this.projectDao.updateProjectWrapper(pk.getProjectId(), pw);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage(), e);
		}
    }
    
    /**
     * Add the specified research_output to this project
     *
     * @param id the id
     * @throws Exception 
     */
    public void addResearchOutput(ResearchOutput ro) throws Exception {
		ProjectWrapper pw = this.projectDao.getProjectWrapperById(ro.getProjectId());
		pw.getResearchOutputs().add(ro);
		this.validateProject(pw);
		this.projectDao.updateProjectWrapper(ro.getProjectId(), pw);
    }
    
    /**
     * Add the specified project_kpi to this project
     *
     * @param id the id
     * @throws Exception 
     */
    public void addReview(Review r) throws Exception {
		ProjectWrapper pw = this.projectDao.getProjectWrapperById(r.getProjectId());
		pw.getReviews().add(r);
		this.validateProject(pw);
		this.projectDao.updateProjectWrapper(r.getProjectId(), pw);
    }
    
    /**
     * Add the specified project_kpi to this project
     *
     * @param id the id
     * @throws Exception 
     */
    public void addFollowUp(FollowUp f) throws Exception {
		ProjectWrapper pw = this.projectDao.getProjectWrapperById(f.getProjectId());
		pw.getFollowUps().add(f);
		this.validateProject(pw);
		this.projectDao.updateProjectWrapper(f.getProjectId(), pw);
    }
    
    /**
     * Add the specified project_kpi to this project
     *
     * @param id the id
     * @throws Exception 
     */
    public void addAdviserAction(AdviserAction aa) throws Exception {
		ProjectWrapper pw = this.projectDao.getProjectWrapperById(aa.getProjectId());
		pw.getAdviserActions().add(aa);
		this.validateProject(pw);
		this.projectDao.updateProjectWrapper(aa.getProjectId(), pw);
    }
    
    /**
     * Add the specified attachment to the project
     *
     * @param id the id
     * @throws Exception 
     */
    public void addAttachment(Attachment a, Integer oid) throws Exception {
		ProjectWrapper pw = this.projectDao.getProjectWrapperById(a.getProjectId());
		if (a.getAdviserActionId()!=null) {
			pw.getAdviserActions().get(oid).getAttachments().add(a);
		} else if (a.getFollowUpId()!=null) {
			pw.getFollowUps().get(oid).getAttachments().add(a);
		} else if (a.getReviewId()!=null) {
			pw.getReviews().get(oid).getAttachments().add(a);
		}
		this.validateProject(pw);
		this.projectDao.updateProjectWrapper(a.getProjectId(), pw);
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
    
    /**
     * Returns a list of facilities.
     *
     * @return a list of facilities
     * @throws Exception 
     */
    public List<Facility> getFacilities() throws Exception {
    	return this.projectDao.getFacilities();
    }
    
    /**
     * Returns a list of ProjectStatuses.
     *
     * @return a list of ProjectStatuses
     * @throws Exception 
     */
    public List<ProjectStatus> getProjectStatuses() throws Exception {
    	return this.projectDao.getProjectStatuses();
    }
    
    /**
     * Returns a list of ProjectTypes.
     *
     * @return a list of ProjectTypes
     * @throws Exception 
     */
    public List<ProjectType> getProjectTypes() throws Exception {
    	return this.projectDao.getProjectTypes();
    }
    
    /**
     * Returns a list of Kpis.
     *
     * @return a list of Kpis
     * @throws Exception 
     */
    public List<Kpi> getKpis() throws Exception {
    	return this.projectDao.getKpis();
    }
    
    /**
     * Returns a list of KpiCodes.
     *
     * @return a list of KpiCodes
     * @throws Exception 
     */
    public List<KpiCode> getKpiCodes() throws Exception {
    	return this.projectDao.getKpiCodes();
    }
    
    /**
     * Returns a list of all KPIS reported for all projects.
     *
     * @return a list of ProjectKpis
     * @throws Exception 
     */
    public List<ProjectKpi> getProjectKpis() throws Exception {
    	return this.projectDao.getProjectKpis();
    }
    
    /**
     * Returns a list of possible Research Output types.
     *
     * @return a list of Research Output types
     * @throws Exception 
     */
    public List<ResearchOutputType> getROTypes() throws Exception {
    	return this.projectDao.getResearchOutputTypes();
    }
}
