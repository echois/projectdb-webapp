package nz.org.nesi.researchHub.control;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
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

import pm.pojo.Adviser;
import pm.pojo.AdviserRole;
import pm.pojo.Affiliation;
import pm.pojo.Change;
import pm.pojo.Project;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 5/12/13 Time: 11:33 AM
 */
public class AdviserControls extends AbstractControl {

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

        final AdviserControls ac = (AdviserControls) context
                .getBean("adviserControls");

        // Adviser adviser = ac.getAdviser(14444);
        //
        // System.out.println(adviser);

        for (final Adviser a : ac.getAllAdvisers()) {

            System.out.println(a.getId() + " : "
                    + ac.getProjectsForAdviser(a.getId()).size());
        }

    }

    // public AdviserControls(ProjectDao o) {
    // this.projectDao = o;
    // }

    /**
     * Creates adviser new adviser.
     * 
     * The Adviser object can't have an id specified, since that gets
     * auto-generated at adviser lower level.
     * 
     * @param adviser
     *            the new Adviser
     * @throws InvalidEntityException
     *             if the new Adviser object has already an id specified
     */
    public Integer createAdviser(final Adviser adviser)
            throws InvalidEntityException {
        validateAdviser(adviser);
        if (adviser.getId() != null) {
            throw new InvalidEntityException(
                    "Adviser can't have id, this property will be auto-generated.",
                    Adviser.class, "id");
        }
        try {
            if (StringUtils.isEmpty(adviser.getStartDate())) {
                adviser.setStartDate(df.format(new Date()));
            }
            return projectDao.createAdviser(adviser);
        } catch (final Exception e) {
            throw new DatabaseException("Can't create Adviser '"
                    + adviser.getFullName() + "'", e);
        }
    }

    /**
     * Delete the Adviser with the specified id.
     * 
     * @param id
     *            the advisers' id
     */
    public void delete(final Integer id) {
        try {
            projectDao.deleteAdviser(id);
        } catch (final Exception e) {
            throw new DatabaseException("Can't delete advisor with id " + id, e);
        }
    }

    /**
     * Replaces an existing adviser object with new properties.
     * 
     * The provided Adviser object needs to have an id, otherwise it can't be
     * matched with an existing one in the database.
     * 
     * @param adviser
     *            the updated adviser object
     * @throws NoSuchEntityException
     *             if no Adviser with the specified
     * @throws InvalidEntityException
     *             if updated Adviser object doesn't have an id specified
     * @throws OutOfDateException
     */
    public void editAdviser(final Adviser adviser)
            throws NoSuchEntityException, InvalidEntityException,
            OutOfDateException {
        validateAdviser(adviser);
        if (adviser.getId() != null) {
            // check whether an adviser with this id exists
            final Adviser temp = getAdviser(adviser.getId());
            // Compare timestamps to prevent accidental overwrite
            if (!adviser.getLastModified().equals(temp.getLastModified())) {
                throw new OutOfDateException("Incorrect timestamp");
            }
            projectDao.updateAdviser(adviser);
        } else {
            throw new InvalidEntityException(
                    "Can't edit adviser. No id provided.", Adviser.class, "id");
        }
    }

    /**
     * Edit one field of a adviser.
     * 
     * The provided Adviser object needs to have an id, otherwise it can't be
     * matched with an existing one in the database.
     * 
     * @param adviser
     *            the updated adviser object
     * @throws Exception
     */
    public void editAdviser(final Integer id, final String field,
            final String timestamp, String data) throws Exception {
        if (field.equals("PictureUrl") && (data.equals("") || data == null)) {
            data = "https://www.nesi.org.nz/sites/default/files/nesi_avatar.png";
        }
        if (field.equals("FullName")) {
            data = data.trim();
        }
        validateAdviser(field, data);
        if (id != null) {
            // check whether an researcher with this id exists
            final Adviser temp = getAdviser(id);
            Change ch = new Change();
            ch.setTbl_id(id);
            ch.setTbl("adviser");
            ch.setField(field);
            ch.setAdviserId(this.authzAspect.getAdviserId());
            ch.setNew_val(data);
            final Class<Adviser> c = Adviser.class;
            String method = "get" + field;
            try {
                final Method get = c.getDeclaredMethod(method);
                Object result = get.invoke(temp);
                ch.setOld_val(result.toString());
            } catch (Exception e) {

            }
            // great, no exception, means an researcher with this id does
            // already exist, now let's merge those two
            // Compare timestamps to prevent accidental overwrite
            final boolean force = timestamp.equals("force");
            if (!force && !timestamp.equals(temp.getLastModified())) {
                throw new OutOfDateException(
                        "Incorrect timestamp. Adviser has been modified since you last loaded it.");
            }
            method = "set" + field;

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
                    throw new DatabaseException("Can't update adviser with id "
                            + id, e);
                }
            }
            projectDao.updateAdviser(temp);
            projectDao.logChange(ch);
        } else {
            throw new InvalidEntityException(
                    "Can't edit adviser. No id provided.", Adviser.class, "id");
        }
    }

    /**
     * Returns the adviser with the specified id.
     * 
     * @param id
     *            the advisers' id
     * @return the advisor object
     * @throws NoSuchEntityException
     *             if the adviser or his projects can't be found
     * @throws DatabaseException
     *             if there is adviser problem with the database
     */
    public Adviser getAdviser(final Integer id) throws NoSuchEntityException {

        if (id == null) {
            throw new IllegalArgumentException("No adviser id provided");
        }

        Adviser a = null;
        try {
            a = projectDao.getAdviserById(id);
        } catch (final NullPointerException npe) {
            throw new NoSuchEntityException("Can't find advisor with id " + id,
                    Adviser.class, id, npe);
        } catch (final Exception e) {
            throw new DatabaseException("Can't find adviser with id " + id, e);
        }

        return a;
    }

    /**
     * Returns the adviser with the specified drupal id.
     * 
     * @param id
     *            the advisers' drupal id
     * @return the advisor object
     * @throws NumberFormatException
     * @throws Exception
     * @throws NoSuchEntityException
     *             if the adviser or his projects can't be found
     * @throws DatabaseException
     *             if there is adviser problem with the database
     */
    public Adviser getAdviserByDrupalId(final String drupalId)
            throws NumberFormatException, NoSuchEntityException {
        if (drupalId == null) {
            throw new IllegalArgumentException("No adviser id provided");
        }
        Adviser a = null;
        try {
            a = projectDao.getAdviserByDrupalId(drupalId);
            if (a == null) {
                throw new NullPointerException();
            }
        } catch (final NullPointerException npe) {
            throw new NoSuchEntityException(
                    "Can't find advisor with drupal id " + drupalId,
                    Adviser.class, Integer.valueOf(drupalId), npe);
        } catch (final Exception e) {
            throw new DatabaseException("Can't find adviser with drupal id "
                    + drupalId, e);
        }

        return a;
    }

    /**
     * Returns the adviser with the specified tuakiri id.
     * 
     * @param id
     *            the advisers' tuakiri id
     * @return the advisor object
     * @throws NumberFormatException
     * @throws Exception
     * @throws NoSuchEntityException
     *             if the adviser or his projects can't be found
     * @throws DatabaseException
     *             if there is adviser problem with the database
     */
    public Adviser getAdviserByTuakiriSharedToken(final String tuakiriId)
            throws NumberFormatException, NoSuchEntityException {
        if (tuakiriId == null) {
            throw new IllegalArgumentException("No adviser id provided");
        }
        Adviser a = null;
        try {
            a = projectDao.getAdviserByTuakiriSharedToken(tuakiriId);
            if (a == null) {
                throw new NullPointerException();
            }
        } catch (final NullPointerException npe) {
            throw new NoSuchEntityException(
                    "Can't find advisor with tuakiri id " + tuakiriId,
                    Adviser.class, Integer.valueOf(tuakiriId), npe);
        } catch (final Exception e) {
            throw new DatabaseException("Can't find adviser with tuakiri id "
                    + tuakiriId, e);
        }

        return a;
    }

    /**
     * Returns a list of AdviserRoles.
     * 
     * @return a list of AdviserRoles
     * @throws Exception
     */
    public List<AdviserRole> getAdviserRoles() throws Exception {
        return projectDao.getAdviserRoles();
    }

    /**
     * Returns a list of Affiliations.
     * 
     * @return a list of Affiliations
     * @throws Exception
     */
    public List<Affiliation> getAffiliations() throws Exception {
        return projectDao.getAffiliations();
    }

    /**
     * Returns adviser list of all advisers.
     * 
     * @return all advisors in the project database
     */
    public List<Adviser> getAllAdvisers() {

        List<Adviser> al = null;
        try {
            al = projectDao.getAdvisers();
        } catch (final Exception e) {
            throw new DatabaseException("Can't get advisers.", e);
        }

        return al;
    }

    /**
     * Returns a list of changes. If no id is given, it returns all changes.
     * 
     * @return a list of Changes
     * @throws Exception
     */
    public List<Change> getChanges(Integer id) throws Exception {
        List<Change> all = projectDao.getChangeLogForTable("adviser");
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
     * Returns the adviser with the specified drupal id.
     * 
     * @param id
     *            the adviser id
     * @return the drupal id
     * @throws NoSuchEntityException
     *             if the adviser or his projects can't be found
     * @throws DatabaseException
     *             if there is adviser problem with the database
     */

    public String getDrupalIdByAdviserId(final Integer id)
            throws NoSuchEntityException {
        if (id == null) {
            throw new IllegalArgumentException("No adviser id provided");
        }
        String d = null;
        try {
            d = projectDao.getDrupalIdByAdviserId(id);
            if (d == null) {
                throw new NullPointerException();
            }
        } catch (final NullPointerException npe) {
            throw new NoSuchEntityException(
                    "Can't find drupal id for advisor with id " + id,
                    Adviser.class, id, npe);
        } catch (final Exception e) {
            throw new DatabaseException("Can't find adviser with id " + id, e);
        }

        return d;
    }

    /**
     * Get the timestamp of the most recently modified adviser.
     * 
     * @return a timestamp
     * @throws Exception
     */
    public String getLastModified(Integer id) throws Exception {
        if (id == null) {
            return projectDao.getLastModifiedForTable("adviser");
        } else {
            return projectDao.getAdviserById(id).getLastModified();
        }
    }

    /**
     * Returns adviser list of all projects for this adviser.
     * 
     * Note: this method returns an empty List if the specified advisor does not
     * exist
     * 
     * @param advisorId
     *            the advisers' id
     * @return the list of projects
     * @throws DatabaseException
     *             if there is adviser problem retrieving the projects
     */
    public List<Project> getProjectsForAdviser(final int advisorId) {
        List<Project> ps = null;
        try {
            ps = projectDao.getProjectsForAdviserId(advisorId);
        } catch (final Exception e) {
            throw new DatabaseException(
                    "Can't get projects for adviser with id " + advisorId, e);
        }
        return ps;
    }

    /**
     * Rollback to a given change id.
     * 
     * @return a list of Changes
     * @throws Exception
     */
    public void rollback(Integer uid, Integer rid) throws Exception {
        List<Change> changes = this.getChanges(uid);
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
            this.editAdviser(uid, change.getField(), "force",
                    change.getOld_val());
            if (change.getId().equals(rid)) return; // Reached desired revision,
                                                    // stop here
        }
    }

    /**
     * Validates the adviser object.
     * 
     * @param a
     *            the adviser object
     * @throws InvalidEntityException
     *             if there is something wrong with the adviser object
     */
    private void validateAdviser(final Adviser a) throws InvalidEntityException {
        if (a.getFullName() == null || a.getFullName().trim().equals("")) {
            throw new InvalidEntityException("Adviser name cannot be empty",
                    Adviser.class, "name");
        }
        if (a.getFullName().equals("New Adviser")) {
            return;
        }
        if (a.getEmail() == null || a.getEmail().trim().equals("")
                || !a.getEmail().matches(".+@.+[.].+")) {
            throw new InvalidEntityException("A valid email is required",
                    Adviser.class, "email");
        }
        if (a.getPhone() == null || a.getPhone().trim().equals("")
                || !a.getPhone().matches(".+[0-9].+")) {
            throw new InvalidEntityException(
                    "Phone must contain at least one digit", Adviser.class,
                    "phone");
        }
        try {
            URL picture = new URL(a.getPictureUrl());
            HttpURLConnection connection = (HttpURLConnection) picture
                    .openConnection();
            connection.setRequestMethod("HEAD");
            int code = connection.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new InvalidEntityException("Not a valid picture",
                    Adviser.class, "picture");
        }

        for (final Adviser other : getAllAdvisers()) {
            if (a.getFullName().equals(other.getFullName())
                    && (a.getId() == null || !a.getId().equals(other.getId()))) {
                throw new InvalidEntityException(a.getFullName()
                        + " already exists in the database", Adviser.class,
                        "name");
            }
        }
    }

    /**
     * Validates an adviser object, by id.
     * 
     * @param a
     *            the adviser id
     * @throws InvalidEntityException
     *             if there is something wrong with the adviser object
     * @throws NoSuchEntityException
     */
    public void validateAdviser(final Integer id)
            throws InvalidEntityException, NoSuchEntityException {
        Adviser a = getAdviser(id);
        validateAdviser(a);
    }

    /**
     * Validates a field.
     * 
     * @param a
     *            the adviser object
     * @throws InvalidEntityException
     *             if there is something wrong with the adviser object
     */
    private void validateAdviser(String field, String data)
            throws InvalidEntityException {
        switch (field) {
        case "FullName":
            if (data.trim().equals("")) {
                throw new InvalidEntityException(
                        "Adviser name cannot be empty", Adviser.class, "name");
            }
            if (data.equals("New Adviser")) {
                return;
            }
            for (final Adviser other : getAllAdvisers()) {
                if (data.equals(other.getFullName())) {
                    throw new InvalidEntityException(data
                            + " already exists in the database", Adviser.class,
                            "name");
                }
            }
            break;
        case "Phone":
            if (data.trim().equals("") || !data.matches(".+[0-9].+")) {
                throw new InvalidEntityException(
                        "Phone must contain at least one digit", Adviser.class,
                        "phone");
            }
            break;
        case "Email":
            if (data.trim().equals("") || !data.matches(".+@.+[.].+")) {
                throw new InvalidEntityException("Not a valid email",
                        Adviser.class, "email");
            }
            break;
        case "PictureUrl":
            try {
                URL picture = new URL(data);
                HttpURLConnection connection = (HttpURLConnection) picture
                        .openConnection();
                connection.setRequestMethod("HEAD");
                int code = connection.getResponseCode();
                if (code != HttpURLConnection.HTTP_OK) {
                    throw new Exception();
                }
            } catch (Exception e) {
                throw new InvalidEntityException("Not a valid picture",
                        Adviser.class, "picture");
            }
            break;
        }
    }
}
