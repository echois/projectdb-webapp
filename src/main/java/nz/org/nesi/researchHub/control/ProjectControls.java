package nz.org.nesi.researchHub.control;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeMultimap;
import nz.org.nesi.researchHub.exceptions.DatabaseException;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
import nz.org.nesi.researchHub.exceptions.NoSuchEntityException;
import nz.org.nesi.researchHub.exceptions.OutOfDateException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import pm.pojo.APLink;
import pm.pojo.Adviser;
import pm.pojo.AdviserAction;
import pm.pojo.Attachment;
import pm.pojo.Change;
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
import pm.pojo.Researcher;
import pm.pojo.Review;
import pm.pojo.Site;

import common.util.CustomException;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 9/12/13 Time: 9:38 AM
 */
public class ProjectControls extends AbstractControl {

    public static void main(final String[] args) throws Exception {

        final ApplicationContext context = new ClassPathXmlApplicationContext(
                "rest-servlet.xml", "pm-servlet.xml", "signup-servlet.xml",
                "root-context.xml");

        context.getBean("adviserControls");

        final ProjectControls pc = (ProjectControls) context
                .getBean("projectControls");

        // for (Project p : pc.getProjects()) {
        // System.out.println(p);
        // }

        System.out.println(pc.getProjectWrapper("uoa00155"));

    }

    /**
     * Validates the project wrapper object.
     *
     * @param pw
     *            the project wrapper
     * @throws InvalidEntityException
     *             if there is something wrong with the projectwrapper object or
     *             one of the associated objects
     */
    public static void validateProject(final ProjectWrapper pw)
            throws InvalidEntityException {
        if (pw.getProject().getName() == null
                || pw.getProject().getName().trim().equals("")) {
            throw new InvalidEntityException("Project does not have a title",
                    Project.class, "name");
        }

        if (pw.getProject().getName().equals("New Project")) {
            return; // Don't check HPC until the project has a real name
        }

        if (pw.getProject().getDescription() == null
                || pw.getProject().getDescription().isEmpty()) {
            throw new InvalidEntityException("There must be a description",
                    Project.class, "description");
        }

        if (pw.getProject().getHostInstitution() == null
                || pw.getProject().getHostInstitution().isEmpty()) {
            throw new InvalidEntityException(
                    "There must be a host institution", Project.class,
                    "hostInstitution");
        }
        // At least one HPC
        if (pw.getProjectFacilities().isEmpty()) {
            throw new InvalidEntityException(
                    "There must be at least one HPC facility associated with the project",
                    Project.class, "facility");
        }

        if (pw.getProject().getProjectCode() == null
                || pw.getProject().getProjectCode().isEmpty()
                || !pw.getProject().getProjectCode().matches("[a-z]+\\d{3,}")) {
            throw new InvalidEntityException(
                    "There must be a valid project code", Project.class,
                    "projectCode");
        }

        int POCount = 0, PACount = 0;

        for (final RPLink rp : pw.getRpLinks()) {
            if (rp.getResearcherRoleId().equals(1)) POCount++;
            for (final RPLink other : pw.getRpLinks()) {
                if (rp.getResearcherId().equals(other.getResearcherId())
                        && !rp.getResearcherRoleId().equals(
                                other.getResearcherRoleId())) {
                    throw new InvalidEntityException(
                            "A researcher can only have one role on a project",
                            ProjectWrapper.class, "rpLinks");
                }
            }
        }

        if (POCount == 0 || POCount > 1) throw new InvalidEntityException(
                "A project must have exactly one Project Owner",
                ProjectWrapper.class, "rpLinks");

        for (final APLink ap : pw.getApLinks()) {
            if (ap.getAdviserRoleId().equals(1)) PACount++;
            for (final APLink other : pw.getApLinks()) {
                if (ap.getAdviserId().equals(other.getAdviserId())
                        && !ap.getAdviserRoleId().equals(
                                other.getAdviserRoleId())) {
                    throw new InvalidEntityException(
                            "An adviser can only have one role on a project",
                            ProjectWrapper.class, "apLinks");
                }
            }
        }

        if (PACount == 0 || PACount > 1) throw new InvalidEntityException(
                "A project must have exactly one Primary Adviser",
                ProjectWrapper.class, "apLinks");

    }

    /**
     * Add the specified adviser to this project
     *
     * @param id
     *            the id
     * @throws Exception
     */
    public void addAdviser(final APLink al) throws Exception {
        if (al.getAdviserId() == null || al.getAdviserId().equals(0)) {
            throw new InvalidEntityException("Not a valid adviser",
                    Adviser.class, "APLink");
        }
        try {
            final ProjectWrapper pw = projectDao.getProjectWrapperById(al
                    .getProjectId());
            pw.getApLinks().add(al);
            projectDao.updateProjectWrapper(al.getProjectId(), pw);
        } catch (final CustomException e) {
            throw new DatabaseException(e.getCustomMsg() + al.getProjectId(), e);
        } catch (final Exception e) {
            throw new DatabaseException(e.getMessage() + al.getProjectId(), e);
        }
    }

    /**
     * Add the specified project_kpi to this project
     *
     * @param id
     *            the id
     * @throws Exception
     */
    public void addAdviserAction(final AdviserAction aa) throws Exception {
        final ProjectWrapper pw = projectDao.getProjectWrapperById(aa
                .getProjectId());
        pw.getAdviserActions().add(aa);
        validateProject(pw);
        projectDao.updateProjectWrapper(aa.getProjectId(), pw);
    }

    /**
     * Add the specified attachment to the project
     *
     * @param id
     *            the id
     * @throws Exception
     */
    public void addAttachment(final Attachment a) throws Exception {
        final ProjectWrapper pw = projectDao.getProjectWrapperById(a
                .getProjectId());
        Integer oid = 0;
        if (a.getAdviserActionId() != null) {
            oid = a.getAdviserActionId();
            a.setAdviserActionId(pw.getAdviserActions().get(oid).getId());
            pw.getAdviserActions().get(oid).getAttachments().add(a);
        } else if (a.getFollowUpId() != null) {
            oid = a.getFollowUpId();
            a.setFollowUpId(pw.getFollowUps().get(oid).getId());
            pw.getFollowUps().get(oid).getAttachments().add(a);
        } else if (a.getReviewId() != null) {
            oid = a.getReviewId();
            a.setReviewId(pw.getReviews().get(oid).getId());
            pw.getReviews().get(oid).getAttachments().add(a);
        }
        validateProject(pw);
        projectDao.updateProjectWrapper(a.getProjectId(), pw);
    }

    /**
     * Add the specified project_kpi to this project
     *
     * @param id
     *            the id
     * @throws InvalidEntityException
     */
    public void addKpi(final ProjectKpi pk) throws Exception {
        final ProjectWrapper pw = projectDao.getProjectWrapperById(pk
                .getProjectId());
        pw.getProjectKpis().add(pk);
        validateProject(pw);
        try {
            projectDao.updateProjectWrapper(pk.getProjectId(), pw);
        } catch (final DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    /**
     * Add the specified researcher to this project
     *
     * @param id
     *            the id
     * @throws InvalidEntityException
     */
    public void addResearcher(final RPLink rl) throws InvalidEntityException {
        if (rl.getResearcherId() == null || rl.getResearcherId().equals(0)) {
            throw new InvalidEntityException("Not a valid researcher",
                    Researcher.class, "RPLink");
        }
        try {
            final ProjectWrapper pw = projectDao.getProjectWrapperById(rl
                    .getProjectId());
            pw.getRpLinks().add(rl);
            projectDao.updateProjectWrapper(rl.getProjectId(), pw);
        } catch (final CustomException e) {
            throw new DatabaseException(e.getCustomMsg() + rl.getProjectId(), e);
        } catch (final Exception e) {
            throw new DatabaseException("Can't fetch project with id "
                    + rl.getProjectId(), e);
        }
    }

    /**
     * Add the specified project_kpi to this project
     *
     * @param id
     *            the id
     * @throws Exception
     */
    public void addReview(final Review r) throws Exception {
        final ProjectWrapper pw = projectDao.getProjectWrapperById(r
                .getProjectId());
        pw.getReviews().add(r);
        validateProject(pw);
        projectDao.updateProjectWrapper(r.getProjectId(), pw);
    }

    /**
     * Creates a new project in the database.
     *
     * @param pw
     *            the projectWrapper object
     * @return the id of the new project
     */
    public synchronized Integer createProjectWrapper(final ProjectWrapper pw)
            throws InvalidEntityException {
        final Project p = pw.getProject();

        if (p.getId() != null) {
            throw new IllegalArgumentException(
                    "Can't create project that already has an id.");
        }

        if (pw.getProject().getHostInstitution() != null
                && !p.getHostInstitution().trim().equals("")
                && (p.getProjectCode() == null || p.getProjectCode().trim()
                        .equals(""))) {
            final String projectCode = projectDao.getNextProjectCode(p
                    .getHostInstitution());
            pw.getProject().setProjectCode(projectCode);
        }
        if (pw.getProject().getProjectCode().equals("nesi")) {
            pw.getProject().setProjectCode(
                    projectDao.getNextProjectCode("nesi"));
        }

        validateProject(pw);
        try {
            final Integer pid = projectDao.createProjectWrapper(pw);
            return pid;
        } catch (final Exception e) {
            throw new DatabaseException("Could not create Project in database."
                    + e.getMessage(), e);
        }

    }

    /**
     * Delete the project wrapper object with the specified id.
     *
     * @param id
     *            the id
     */
    public void delete(final Integer id) {

        try {
            projectDao.deleteProjectWrapper(id);
        } catch (final Exception e) {
            throw new DatabaseException("Can't delete ProjectWrapper with id "
                    + id, e);
        }

    }

    /**
     * Edit a project wrapper object.
     *
     * @param project
     *            the updated project wrapper
     * @throws InvalidEntityException
     *             if there is something wrong with either the projectwrapper or
     *             associated objects
     * @throws OutOfDateException
     */
    public void editProjectWrapper(@PathVariable final Integer id,
            @RequestBody final ProjectWrapper project)
            throws InvalidEntityException, OutOfDateException {

        validateProject(project);
        if (project.getProject() != null) {
            // might throw database exception if project does not already exist
            final ProjectWrapper temp = getProjectWrapper(id.toString());

            project.getProject().setId(id);

            // great, no exception, means an project with this id does already
            // exist,
            // Compare timestamps to prevent accidental overwrite
            if (project.getProject().getLastModified() != temp.getProject()
                    .getLastModified()
                    || !project.getProject().getLastModified()
                            .equals(temp.getProject().getLastModified())) {
                throw new OutOfDateException("Incorrect timestamp");
            }
            try {
                projectDao.updateProjectWrapper(id, project);
            } catch (final Exception e) {
                throw new DatabaseException("Can't update project with id "
                        + id, e);
            }
        } else {
            throw new InvalidEntityException(
                    "Can't edit project. No project object provided.",
                    Project.class, "id");
        }

    }

    /**
     * Edit one field of a project wrapper object.
     *
     * @param project
     *            the updated project wrapper
     * @throws Exception
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws ClassNotFoundException
     */
    public void editProjectWrapper(final Integer id, final String object,
            final String field, final String timestamp, final String data)
            throws Exception {
        validateProject(object + "_" + field, data);
        if (id != null) {
            // might throw database exception if project does not already exist
            final ProjectWrapper pw = getProjectWrapper(id.toString());
            Change ch = new Change();
            ch.setTbl_id(id);
            ch.setTbl("project");
            ch.setField(object + "_" + field);
            ch.setAdviserId(this.authzAspect.getAdviserId());
            ch.setNew_val(data);
            // great, no exception, means an project with this id does already
            // exist,
            // Compare timestamps to prevent accidental overwrite
            final boolean force = timestamp.equals("force");
            if (!force && !timestamp.equals(pw.getProject().getLastModified())) {
                throw new OutOfDateException(
                        "Incorrect timestamp. Project has been modified since you last loaded it.");
            }
            boolean deep = false;
            final boolean attachment = object.contains("Attachments");
            String method = "get" + object;
            Class<?> pojoClass = null;
            if (object.contains("_")) {
                deep = true;
                method = "get" + object.split("_")[0];
            }
            try {
                if (object.equals("projectFacilities")) {
                    String facs = "";
                    for (ProjectFacility pf : pw.getProjectFacilities()) {
                        facs += pf.getFacilityId() + ",";
                    }
                    ch.setOld_val(facs);
                    final List<ProjectFacility> projectFacilities = new LinkedList<ProjectFacility>();
                    for (final String facId : data.split(",")) {
                        final ProjectFacility pf = new ProjectFacility();
                        pf.setProjectId(id);
                        pf.setFacilityId(Integer.valueOf(facId));
                        projectFacilities.add(pf);
                    }
                    pw.setProjectFacilities(projectFacilities);
                } else {
                    pojoClass = ProjectWrapper.class;
                    Method getPojo = pojoClass.getDeclaredMethod(method);
                    Object pojo = getPojo.invoke(pw);
                    pojoClass = pojo.getClass();
                    if (deep) {
                        method = "get";
                        try {
                            Integer.parseInt(object.split("_")[1]);
                            getPojo = pojoClass.getDeclaredMethod(method,
                                    int.class);
                            pojo = getPojo.invoke(pojo,
                                    Integer.parseInt(object.split("_")[1]));
                        } catch (final NumberFormatException e) {
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
                        getPojo = pojoClass
                                .getDeclaredMethod(method, int.class);
                        pojo = getPojo.invoke(pojo,
                                Integer.parseInt(object.split("_")[3]));
                        pojoClass = pojo.getClass();
                    }
                    method = "get" + field;
                    try {
                        Method get = pojoClass.getDeclaredMethod(method);
                        ch.setOld_val(get.invoke(pojo).toString());
                    } catch (Exception e) {

                    }
                    method = "set" + field;
                    // Try integers first, floats if that fails, then fallback
                    // to string
                    try {
                        try {
                            final Integer intData = Integer.valueOf(data);
                            final Method set = pojoClass.getDeclaredMethod(
                                    method, Integer.class);
                            set.invoke(pojo, intData);
                        } catch (final Exception e) {
                            final Float floatData = Float.valueOf(data);
                            final Method set = pojoClass.getDeclaredMethod(
                                    method, Float.class);
                            set.invoke(pojo, floatData);
                        }
                    } catch (final Exception e) {
                        final Method set = pojoClass.getDeclaredMethod(method,
                                String.class);
                        set.invoke(pojo, data);
                    }
                }

                // Auto project code logic
                if (pw.getProject().getProjectCode().equals("nesi")) {
                    pw.getProject().setProjectCode(
                            projectDao.getNextProjectCode("nesi"));
                }
                if (!pw.getProject().getHostInstitution().trim().equals("")
                        && (pw.getProject().getProjectCode() == null || pw
                                .getProject().getProjectCode().trim()
                                .equals(""))) {
                    final String projectCode = projectDao.getNextProjectCode(pw
                            .getProject().getHostInstitution());
                    pw.getProject().setProjectCode(projectCode);
                }

                // Auto Date logic
                final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                if (pw.getProject().getStartDate().trim().equals("")) {
                    final Date startDate = new Date();
                    pw.getProject().setStartDate(df.format(startDate));
                }
                if (pw.getProject().getNextReviewDate().trim().equals("")) {
                    final Date nextReview = new Date();
                    nextReview.setYear(nextReview.getYear() + 1);
                    pw.getProject().setNextReviewDate(df.format(nextReview));
                }
                if (pw.getProject().getNextFollowUpDate().trim().equals("")) {
                    final Date nextFollowUp = new Date();
                    nextFollowUp.setMonth(nextFollowUp.getMonth() + 6);
                    pw.getProject()
                            .setNextFollowUpDate(df.format(nextFollowUp));
                }
                projectDao.updateProjectWrapper(id, pw);
                projectDao.logChange(ch);
            } catch (final NoSuchMethodException e) {
                throw new InvalidEntityException(pojoClass.getName() + "."
                        + method + " is not a valid method",
                        ProjectWrapper.class, object);
            } catch (final InvocationTargetException e) {
                throw new InvalidEntityException("Unable to " + method
                        + " for " + pw.getProject().getProjectCode(),
                        ProjectWrapper.class, object);
            } catch (final IllegalAccessException e) {
                throw new InvalidEntityException("It is illegal to fetch "
                        + object, ProjectWrapper.class, object);
            } catch (final ClassNotFoundException e) {
                throw new InvalidEntityException(object
                        + " is not a valid POJO", ProjectWrapper.class, object);
            } catch (final IllegalArgumentException e) {
                throw new InvalidEntityException(data + " does not match "
                        + pojoClass.getName() + "." + method
                        + "'s expected parameter", ProjectWrapper.class, object);
            } catch (final Exception e) {
                throw new DatabaseException(e.getMessage(), e);
            }
        } else {
            throw new InvalidEntityException(
                    "Can't edit project. No id provided.", Project.class, "id");
        }

    }

    /**
     * Get all projects that contain the specified filter string
     * (case-insensitive) in one or more of the project properties.
     *
     * Mind, this returns only the Project object, not the ProjectWrapper ones
     * (see: {@link #getProjects()}.
     *
     * @param filter
     *            the filter string, can't be empty
     * @return all projects matching the filter
     */
    public List<Project> filterProjects(String filter) {

        if (StringUtils.isEmpty(filter)) {
            throw new IllegalArgumentException(
                    "Can't filter projects using empty string, use getProjects method instead.");
        }

        filter = filter.toLowerCase();
        final List<Project> filtered = new LinkedList<Project>();

        for (final Project p : getProjects()) {
            if (p.getName().toLowerCase().contains(filter)
                    || p.getDescription().toLowerCase().contains(filter)
                    || p.getHostInstitution().toLowerCase().contains(filter)
                    || p.getNotes() != null
                    && p.getNotes().toLowerCase().contains(filter)
                    || p.getProjectCode().toLowerCase().contains(filter)
                    || p.getProjectTypeName().toLowerCase().contains(filter)
                    || p.getRequirements() != null
                    && p.getRequirements().toLowerCase().contains(filter)
                    || p.getTodo() != null
                    && p.getTodo().toLowerCase().contains(filter)) {
                filtered.add(p);
            }
        }

        return filtered;

    }

    /**
     * Returns a map of all projects, with all researcher members for every project, and their roles.
     *
     * @return all project map
     */
    public Map<String, Map<String, Set<String>>> getAllProjectsAndMembers() throws Exception {

        Map<String, Map<String, Set<String>>> allProjects = projectDao.getAllProjectsAndMembers();

        return allProjects;
    }

    /**
     * Returns a list of changes. If no id is given, it returns all changes.
     *
     * @return a list of Changes
     * @throws Exception
     */
    public List<Change> getChanges(Integer id) throws Exception {
        List<Change> all = projectDao.getChangeLogForTable("project");
        if (id == null) return all;
        List<Change> filtered = new LinkedList<Change>();
        for (Change c : all) {
            if (c.getTbl_id().equals(id)) {
                filtered.add(c);
            }
        }
        return filtered;
    }

    /**
     * Returns a list of facilities.
     *
     * @return a list of facilities
     * @throws Exception
     */
    public List<Facility> getFacilities() throws Exception {
        return projectDao.getFacilities();
    }

    /**
     * Returns a list of institutions.
     *
     * @return a list of institutions
     * @throws Exception
     */
    public List<String> getInstitutions() throws Exception {
        return projectDao.getInstitutions();
    }

    /**
     * Returns a list of KpiCodes.
     *
     * @return a list of KpiCodes
     * @throws Exception
     */
    public List<KpiCode> getKpiCodes() throws Exception {
        return projectDao.getKpiCodes();
    }

    /**
     * Returns a list of Kpis.
     *
     * @return a list of Kpis
     * @throws Exception
     */
    public List<Kpi> getKpis() throws Exception {
        return projectDao.getKpis();
    }

    /**
     * Get the timestamp of the most recently modified project, or the specified
     * project
     *
     * @return a timestamp
     * @throws Exception
     */
    public String getLastModified(Integer id) throws Exception {
        if (id == null) {
            return projectDao.getLastModifiedForTable("project");
        } else {
            return projectDao.getProjectWrapperById(id).getProject()
                    .getLastModified();
        }
    }

    /**
     * Returns a list of all KPIS reported for all projects.
     *
     * @return a list of ProjectKpis
     * @throws Exception
     */
    public List<ProjectKpi> getProjectKpis() throws Exception {
        return projectDao.getProjectKpis();
    }

    /**
     * Gets the project properties associated with the specified project id.
     *
     * @param projectId
     *            the project
     * @return the ProjectProperties
     */
    public List<ProjectProperty> getProjectProperties(final Integer id) {
        try {
            return projectDao.getProjectProperties(id);
        } catch (final Exception e) {
            throw new DatabaseException("Could not retrieve properties", e);
        }
    }

    /**
     * Get all projects in the database.
     *
     * Mind, this doesn't return project wrapper objects, just the plain
     * objects. We could change that, not sure about performance in that case.
     *
     * @return all projects
     */
    public List<Project> getProjects() {
        try {
            final List<Project> ps = projectDao.getProjects();
            return ps;
        } catch (final Exception e) {
            throw new DatabaseException("Could not retrieve projects", e);
        }
    }

    /**
     * Returns a list of ProjectStatuses.
     *
     * @return a list of ProjectStatuses
     * @throws Exception
     */
    public List<ProjectStatus> getProjectStatuses() throws Exception {
        return projectDao.getProjectStatuses();
    }

    /**
     * Returns a list of ProjectTypes.
     *
     * @return a list of ProjectTypes
     * @throws Exception
     */
    public List<ProjectType> getProjectTypes() throws Exception {
        return projectDao.getProjectTypes();
    }

    /**
     * Utility method that forwards to {@link #getProjectWrapper(String)}.
     *
     * @param id
     *            the id of the project
     * @return the projectWrapper object
     */
    public ProjectWrapper getProjectWrapper(final Integer id) {
        return getProjectWrapper(id.toString());
    }

    /**
     * Gets the project (with associated objects) with the specified id or
     * project code.
     *
     * @param projectIdOrCode
     *            the project id or project code
     * @return the Project
     */
    public ProjectWrapper getProjectWrapper(final String projectIdOrCode) {

        try {
            final int i = Integer.parseInt(projectIdOrCode);
            try {
                final ProjectWrapper pw = projectDao.getProjectWrapperById(i);
                return pw;
            } catch (final Exception e) {
                throw new DatabaseException(
                        "Could not retrieve project with id: "
                                + projectIdOrCode, e);
            }
        } catch (final Exception e) {
            try {
                final ProjectWrapper pw = projectDao
                        .getProjectWrapperByProjectCode(projectIdOrCode);
                return pw;
            } catch (final Exception e2) {
                throw new DatabaseException(
                        "Could not retrieve project with code: "
                                + projectIdOrCode, e2);
            }
        }

    }

    /**
     * Returns a list of all Research Output reported for all projects.
     *
     * @return a list of ResearchOutput
     * @throws Exception
     */
    public List<ResearchOutput> getResearchOutput() throws Exception {
        return projectDao.getResearchOutput();
    }

    /**
     * Returns a list of possible Research Output types.
     *
     * @return a list of Research Output types
     * @throws Exception
     */
    public List<ResearchOutputType> getROTypes() throws Exception {
        return projectDao.getResearchOutputTypes();
    }

    /**
     * Returns a list of sites.
     *
     * @return a list of sites
     * @throws Exception
     */
    public List<Site> getSites() throws Exception {
        return projectDao.getSites();
    }

    /**
     * Remove the specified adviser or researcher from this project
     *
     * @param id
     *            the id
     */
    public void removeObjectLink(final Integer id, final int oid,
            final String type) {
        try {
            final ProjectWrapper pw = projectDao.getProjectWrapperById(id);
            if (type.equals("adviser")) {
                final List<APLink> aTmp = new LinkedList<APLink>();
                for (final APLink a : pw.getApLinks()) {
                    if (!a.getAdviserId().equals(oid)) {
                        aTmp.add(a);
                    }
                }
                pw.setApLinks(aTmp);
            } else if (type.equals("researcher")) {
                final List<RPLink> rTmp = new LinkedList<RPLink>();
                for (final RPLink r : pw.getRpLinks()) {
                    if (!r.getResearcherId().equals(oid)) {
                        rTmp.add(r);
                    }
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
                projectDao.deleteProjectProperty(Integer.valueOf(oid));
            } else if (type.contains("Attachments")) {
                // pw.getAdviserActions().get(0).getAttachments().remove(0);
                final String obj = type.split("_")[0];
                final int nid = Integer.parseInt(type.split("_")[2]);
                Class<?> c = ProjectWrapper.class;
                Method m = c.getDeclaredMethod("get" + obj);
                // pw.getAdviserActions()
                Object o = m.invoke(pw);
                c = o.getClass();
                m = c.getDeclaredMethod("get", int.class);
                // .get(oid)
                o = m.invoke(o, oid);
                c = o.getClass();
                m = c.getDeclaredMethod("getAttachments");
                // .getAttachments()
                o = m.invoke(o);
                c = o.getClass();
                m = c.getDeclaredMethod("remove", int.class);
                // .remove(nid)
                m.invoke(o, nid);
            }

            // validateProject(pw);
            projectDao.updateProjectWrapper(id, pw);
        } catch (final Exception e) {
            throw new DatabaseException("Can't delete " + oid, e);
        }
    }

    /**
     * Rollback to a given change id.
     *
     * @return a list of Changes
     * @throws Exception
     */
    public void rollback(Integer pid, Integer rid) throws Exception {
        List<Change> changes = this.getChanges(pid);
        boolean validRid = false;
        for (Change change : changes) {
            if (change.getId().equals(rid)) {
                validRid = true;
            }
        }
        if (!validRid) {
            throw new InvalidEntityException("Not a valid revision id",
                    Change.class, "id");
        }
        for (Change change : changes) {
            String[] bits = change.getField().split("_");
            String obj = bits[0];
            String field = change.getField().replace(obj + "_", "");
            this.editProjectWrapper(pid, obj, field, "force",
                    change.getOld_val());
            if (change.getId().equals(rid)) return; // Reached desired revision
        }
    }

    /**
     * Upsert the specified followup
     *
     * @param id
     *            the id
     * @throws Exception
     */
    public void upsertFollowUp(final FollowUp f) throws Exception {
        projectDao.upsertFollowUp(f);
    }

    /**
     * Add/Edit the specified project property
     *
     * @param id
     *            the id
     * @throws Exception
     */

    public void upsertProperty(ProjectProperty p) throws Exception {
        if (p.getId() != null) {
            final ProjectProperty old = projectDao
                    .getProjectProperty(p.getId());
            if (p.getPropname() != null) {
                old.setPropname(p.getPropname());
            }
            if (p.getPropvalue() != null) {
                old.setPropvalue(p.getPropvalue());
            }
            if (p.getFacilityId() != null) {
                old.setFacilityId(p.getFacilityId());
            }
            p = old;
        }
        projectDao.upsertProjectProperty(p);
    }

    /**
     * Add the specified research_output to this project
     *
     * @param id
     *            the id
     * @throws Exception
     */
    public void upsertResearchOutput(final ResearchOutput ro) throws Exception {
        projectDao.upsertResearchOutput(ro);
    }

    /**
     * Validates an project object, by id.
     *
     * @param a
     *            the project id
     * @throws InvalidEntityException
     *             if there is something wrong with the project object
     * @throws NoSuchEntityException
     */
    public void validateProject(final Integer id)
            throws InvalidEntityException, NoSuchEntityException {
        ProjectWrapper pw = getProjectWrapper(id);
        validateProject(pw);
    }

    /**
     * Validates a field.
     *
     * @param field
     *            , data
     * @throws InvalidEntityException
     *             if there is something wrong with the project object
     */
    private void validateProject(String field, String data)
            throws InvalidEntityException {
        switch (field) {
        case "Project_Name":
            if (data == null || data.isEmpty()) {
                throw new InvalidEntityException(
                        "Project does not have a title", Project.class, "name");
            }
            break;
        case "projectFacilities_all":
            // At least one HPC
            if (data == null || data.isEmpty()) {
                throw new InvalidEntityException(
                        "There must be at least one HPC facility associated with the project",
                        Project.class, "facility");
            }
            break;
        case "Project_ProjectCode":
            if (data == null || data.isEmpty()) {
                throw new InvalidEntityException(
                        "There must be a project code", Project.class,
                        "projectCode");
            }
            break;
        case "Project_Description":
            if (data == null || data.isEmpty()) {
                throw new InvalidEntityException("There must be a description",
                        Project.class, "description");
            }
            break;
        case "Project_HostInstitution":
            if (data == null || data.isEmpty()) {
                throw new InvalidEntityException(
                        "There must be a host institution", Project.class,
                        "hostInstitution");
            }
            break;
        }
        if (field.matches("ProjectKpis_\\d+_Value")) {
            try {
                Float.parseFloat(data);
            } catch (NumberFormatException e) {
                throw new InvalidEntityException(
                        "Kpi value must be a valid float (decimal number)",
                        ProjectKpi.class, "value");
            }
        }
    }
}
