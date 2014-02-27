package nz.org.nesi.researchHub.control;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import nz.org.nesi.researchHub.exceptions.DatabaseException;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
import nz.org.nesi.researchHub.exceptions.NoSuchEntityException;
import nz.org.nesi.researchHub.exceptions.OutOfDateException;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pm.pojo.InstitutionalRole;
import pm.pojo.Project;
import pm.pojo.Researcher;
import pm.pojo.ResearcherProperty;
import pm.pojo.ResearcherRole;
import pm.pojo.ResearcherStatus;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 5/12/13 Time: 11:33 AM
 */
public class ResearcherControls extends AbstractControl {

    public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(final String[] args) throws Exception {

        final ApplicationContext context = new ClassPathXmlApplicationContext(
                "rest-servlet.xml", "pm-servlet.xml", "signup-servlet.xml",
                "root-context.xml");

        // for ( String bean : context.getBeanDefinitionNames() ) {
        // if ( bean.contains("ntrols") ) {
        // System.out.println(bean);
        // }
        // }

        final ResearcherControls rc = (ResearcherControls) context
                .getBean("researcherControls");

        for (final Researcher r : rc.getAllResearchers()) {

            System.out.println(r.getId() + " : "
                    + rc.getProjectsForResearcher(r.getId()).size());
        }

    }

    /**
     * Creates researcher new researcher.
     * 
     * The Researcher object can't have an id specified, since that gets
     * auto-generated at researcher lower level.
     * 
     * @param researcher
     *            the new Researcher
     * @throws InvalidEntityException
     *             if the new Researcher object has already an id specified
     */
    public Integer createResearcher(final Researcher researcher)
            throws InvalidEntityException {
        validateResearcher(researcher);
        if (researcher.getId() != null) {
            throw new InvalidEntityException(
                    "Researcher can't have id, this property will be auto-generated.",
                    Researcher.class, "id");
        }
        try {
            if (StringUtils.isEmpty(researcher.getStartDate())) {
                researcher.setStartDate(df.format(new Date()));
            }
            return projectDao.createResearcher(researcher);
        } catch (final Exception e) {
            throw new DatabaseException("Can't create Researcher '"
                    + researcher.getFullName() + "'", e);
        }
    }

    /**
     * Delete the Researcher with the specified id.
     * 
     * @param id
     *            the researchers' id
     */
    public void delete(final Integer id) {
        try {
            projectDao.deleteResearcher(id);
        } catch (final Exception e) {
            throw new DatabaseException(
                    "Can't delete researcher with id " + id, e);
        }
    }

    /**
     * Edit one field of a researcher.
     * 
     * The provided Researcher object needs to have an id, otherwise it can't be
     * matched with an existing on in the database.
     * 
     * @param researcher
     *            the updated researcher object
     * @throws OutOfDateException
     *             , Exception
     * @throws NoSuchEntityException
     * @throws InvalidEntityException
     */
    public void editResearcher(final Integer id, final String field,
            final String timestamp, final String data)
            throws OutOfDateException, NoSuchEntityException,
            InvalidEntityException {
        if (id != null) {
            // check whether an researcher with this id exists
            final Researcher temp = getResearcher(id);
            // great, no exception, means an researcher with this id does
            // already exist, now let's merge those two
            // Compare timestamps to prevent accidental overwrite
            final boolean force = timestamp.equals("force");
            if (!force && !timestamp.equals(temp.getLastModified())) {
                throw new OutOfDateException(
                        "Incorrect timestamp. Researcher has been modified since you last loaded it.");
            }
            final String method = "set" + field;
            final Class<Researcher> c = Researcher.class;
            try {
                // Try use the parameter as an integer
                final Integer intData = Integer.valueOf(data);
                final Method set = c.getDeclaredMethod(method, Integer.class);
                set.invoke(temp, intData);
            } catch (final Exception e) {
                // String fallback
                try {
                    final Method set = c
                            .getDeclaredMethod(method, String.class);
                    set.invoke(temp, data);
                } catch (final Exception ex) {
                    throw new InvalidEntityException("Can't edit researcher. "
                            + method + " is not valid", Researcher.class, "id");
                }
            }
            validateResearcher(temp);
            projectDao.updateResearcher(temp);
        } else {
            throw new InvalidEntityException(
                    "Can't edit researcher. No id provided.", Researcher.class,
                    "id");
        }
    }

    /**
     * Replaces an existing researcher object with new properties.
     * 
     * The provided Researcher object needs to have an id, otherwise it can't be
     * matched with an existing on in the database.
     * 
     * @param researcher
     *            the updated researcher object
     * @throws NoSuchEntityException
     *             if no Researcher with the specified
     * @throws InvalidEntityException
     *             if updated Researcher object doesn't have an id specified
     * @throws OutOfDateException
     */
    public void editResearcher(final Researcher researcher)
            throws NoSuchEntityException, InvalidEntityException,
            OutOfDateException {
        validateResearcher(researcher);
        if (researcher.getId() != null) {
            // check whether an researcher with this id exists
            final Researcher temp = getResearcher(researcher.getId());
            // Compare timestamps to prevent accidental overwrite
            if (!researcher.getLastModified().equals(temp.getLastModified())) {
                throw new OutOfDateException("Incorrect timestamp");
            }
            projectDao.updateResearcher(researcher);
        } else {
            throw new InvalidEntityException(
                    "Can't edit researcher. No id provided.", Researcher.class,
                    "id");
        }
    }

    /**
     * Get all researchers that contain the specified filter string
     * (case-insensitive) in one or more of the project properties.
     * 
     * @param filter
     *            the filter string, can't be empty
     * @return all projects matching the filter
     */
    public List<Researcher> filterResearchers(String filter) {

        if (StringUtils.isEmpty(filter)) {
            throw new IllegalArgumentException(
                    "Can't filter researchers using empty string, use getProjects method instead.");
        }

        filter = filter.toLowerCase();
        final List<Researcher> filtered = new LinkedList<Researcher>();

        for (final Researcher r : getAllResearchers()) {
            if (r.getAffiliation().toLowerCase().contains(filter)
                    || r.getEmail().toLowerCase().contains(filter)
                    || r.getFullName().toLowerCase().contains(filter)
                    || r.getInstitutionalRoleName().toLowerCase()
                            .contains(filter) || r.getNotes() != null
                    && r.getNotes().toLowerCase().contains(filter)
                    || r.getPreferredName() != null
                    && r.getPreferredName().toLowerCase().contains(filter)
                    || r.getStatusName().toLowerCase().contains(filter)) {
                filtered.add(r);
            }
        }

        return filtered;

    }

    /**
     * Returns a list of Affiliations.
     * 
     * @return a list of Affiliations
     * @throws Exception
     */
    public List<String> getAffiliations() throws Exception {
        return affiliationUtil.getAffiliationStrings();
    }

    /**
     * Returns list of all researchers.
     * 
     * @return all researchers in the project database
     */
    public List<Researcher> getAllResearchers() {

        List<Researcher> rl = null;
        try {
            rl = projectDao.getResearchers();
        } catch (final Exception e) {
            throw new DatabaseException("Can't get researchers.", e);
        }

        return rl;
    }

    /**
     * Returns a list of InstitutionalRoles.
     * 
     * @return a list of InstitutionalRoles
     * @throws Exception
     */
    public List<InstitutionalRole> getInstitutionalRoles() throws Exception {
        return projectDao.getInstitutionalRoles();
    }

    /**
     * Returns researcher list of all projects for this researcher.
     * 
     * Note: this method returns an empty List if the specified researcher does
     * not exist
     * 
     * @param researcherId
     *            the researchers' id
     * @return the list of projects
     * @throws DatabaseException
     *             if there is researcher problem retrieving the projects
     */
    public List<Project> getProjectsForResearcher(final int researcherId) {
        List<Project> ps = null;
        try {
            ps = projectDao.getProjectsForResearcherId(researcherId);
        } catch (final Exception e) {
            throw new DatabaseException(
                    "Can't get projects for researcher with id " + researcherId,
                    e);
        }
        return ps;
    }

    /**
     * Returns the researcher with the specified id.
     * 
     * @param id
     *            the researchers' id
     * @return the researcher object
     * @throws NoSuchEntityException
     *             if the researcher or his projects can't be found
     * @throws DatabaseException
     *             if there is researcher problem with the database
     */
    public Researcher getResearcher(final Integer id)
            throws NoSuchEntityException {

        if (id == null) {
            throw new IllegalArgumentException("No researcher id provided");
        }

        Researcher r = null;
        try {
            r = projectDao.getResearcherById(id);
        } catch (final NullPointerException npe) {
            throw new NoSuchEntityException("Can't find researcher with id "
                    + id, Researcher.class, id);
        } catch (final Exception e) {
            throw new DatabaseException("Can't find researcher with id " + id,
                    e);
        }

        return r;
    }

    /**
     * Returns the user's linux username + other details.
     * 
     * @return a string
     * @throws Exception
     */
    public List<ResearcherProperty> getResearcherProperties(final Integer id)
            throws Exception {
        return projectDao.getResearcherProperties(id);
    }

    /**
     * Returns a list of ResearcherRoles.
     * 
     * @return a list of ResearcherRoles
     * @throws Exception
     */
    public List<ResearcherRole> getResearcherRoles() throws Exception {
        return projectDao.getResearcherRoles();
    }

    /**
     * Returns a list of Researcher Statuses.
     * 
     * @return a list of Researcher Statuses
     * @throws Exception
     */
    public List<ResearcherStatus> getStatuses() throws Exception {
        return projectDao.getResearcherStatuses();
    }

    /**
     * Add/Edit the specified researcher property
     * 
     * @param id
     *            the id
     * @throws Exception
     */

    public void upsertProperty(final ResearcherProperty r) throws Exception {
        projectDao.upsertResearcherProperty(r);
    }

    /**
     * Validates the researcher object.
     * 
     * @param a
     *            the project wrapper
     * @throws InvalidEntityException
     *             if there is something wrong with the researcher object
     */
    private void validateResearcher(final Researcher r)
            throws InvalidEntityException {
        if (r.getFullName().trim().equals("")) {
            throw new InvalidEntityException("Researcher name cannot be empty",
                    Researcher.class, "name");
        }
        if (r.getPhone().trim() != "" && !r.getPhone().matches(".+[0-9].+")) {
            throw new InvalidEntityException(
                    "Phone must contain at least one digit", Researcher.class,
                    "phone");
        }
        if (r.getEmail().trim() != "" && !r.getEmail().matches(".+@.+[.].+")) {
            throw new InvalidEntityException("Not a valid email",
                    Researcher.class, "email");
        }
        if (r.getFullName().equals("New Researcher")) {
            return;
        }
        for (final Researcher other : getAllResearchers()) {
            if (r.getFullName().equals(other.getFullName())
                    && (r.getId() == null || !r.getId().equals(other.getId()))) {
                throw new InvalidEntityException(r.getFullName()
                        + " already exists in the database", Researcher.class,
                        "name");
            }
        }
    }
}
