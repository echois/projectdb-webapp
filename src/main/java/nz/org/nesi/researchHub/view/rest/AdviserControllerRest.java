package nz.org.nesi.researchHub.view.rest;

import java.util.List;

import nz.org.nesi.researchHub.control.AdviserControls;
import nz.org.nesi.researchHub.exceptions.DatabaseException;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
import nz.org.nesi.researchHub.exceptions.NoSuchEntityException;
import nz.org.nesi.researchHub.exceptions.OutOfDateException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pm.pojo.Adviser;
import pm.pojo.AdviserRole;
import pm.pojo.Affiliation;
import pm.pojo.Change;
import pm.pojo.Project;

import com.mangofactory.swagger.annotations.ApiErrors;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 13/12/13 Time: 12:09 PM
 */
@Controller
@RequestMapping(value = "/advisers")
@Api(value = "/advisers",
     description = "Manage and display advisers and related lists")
public class AdviserControllerRest {

    @Autowired
    private AdviserControls adviserControls;

    @ApiOperation(value = "Create new adviser",
                  notes = "Returns the generated adviser id, if successful",
                  responseClass = "Integer")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Integer createAdviser(
            @ApiParam(value = "Adviser object", required = true) @RequestBody final Adviser adviser)
            throws InvalidEntityException {
        return adviserControls.createAdviser(adviser);
    }

    @ApiOperation(
                  value = "Delete adviser",
                  notes = "Deletes an adviser. This operation cannot be undone, unless the database is restored",
                  responseClass = "void")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(
            @ApiParam(value = "Adviser id", required = true) @PathVariable final Integer id) {
        adviserControls.delete(id);
    }

    @ApiOperation(
                  value = "Edit adviser",
                  notes = "Update an adviser by posting the entire adviser object",
                  responseClass = "void")
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public void editAdviser(
            @ApiParam(value = "Adviser object", required = true) @PathVariable final Adviser adviser)
            throws NoSuchEntityException, InvalidEntityException,
            OutOfDateException {
        adviserControls.editAdviser(adviser);
    }

    @ApiOperation(
                  value = "Edit adviser",
                  notes = "Update a single field in an adviser. The timestamp included must match the last modified time of the adviser, otherwise an OutOfDateException will be thrown",
                  responseClass = "void")
    @RequestMapping(value = "/{id}/{field}/{timestamp}/",
                    method = RequestMethod.POST)
    @ResponseBody
    public void editAdviser(
            @ApiParam(value = "Adviser id", required = true) @PathVariable final Integer id,
            @ApiParam(
                      value = "Desired field to edit, with the first letter capitalised. For example, FullName, Notes, StartDate etc",
                      required = true) @PathVariable final String field,
            @ApiParam(
                      value = "A timestamp indicating the last time the adviser was modified. Used as a consistency check. Set to force to bypass (not recommended)",
                      required = true) @PathVariable final String timestamp,
            @ApiParam(value = "The new value for the field", required = true) @RequestBody final String data)
            throws Exception {
        adviserControls.editAdviser(id, field, timestamp, data);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get an adviser by their id",
                  notes = "Returns the adviser object associated with this id",
                  responseClass = "Adviser")
    public Adviser getAdviser(
            @ApiParam(value = "Adviser id", required = true) @PathVariable final Integer id)
            throws NoSuchEntityException {
        return adviserControls.getAdviser(id);
    }

    @RequestMapping(value = "/drupal/{drupalId}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
                  value = "Get adviser by their drupal id",
                  notes = "Returns the adviser object associated with this drupal id",
                  responseClass = "Adviser")
    @ApiErrors({ NoSuchEntityException.class, DatabaseException.class })
    public Adviser getAdviserByDrupalId(
            @ApiParam(value = "Adviser's drupal id", required = true) @PathVariable final String drupalId)
            throws NoSuchEntityException {
        return adviserControls.getAdviserByDrupalId(drupalId);
    }

    @RequestMapping(value = "/tuakiri/{tuakiriId}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
                  value = "Get adviser by their tuakiri id",
                  notes = "Returns the adviser object associated with this tuakiri shared token",
                  responseClass = "Adviser")
    @ApiErrors({ NoSuchEntityException.class, DatabaseException.class })
    public Adviser getAdviserByTuakiriId(
            @ApiParam(value = "Adviser's tuakiri shared token", required = true) @PathVariable final String tuakiriId)
            throws NoSuchEntityException {
        return adviserControls.getAdviserByTuakiriSharedToken(tuakiriId);
    }

    @ApiOperation(value = "Get roles",
                  notes = "Returns a list of possible roles on a project",
                  responseClass = "List<AdviserRole>")
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ResponseBody
    public List<AdviserRole> getAdviserRoles() throws Exception {
        return adviserControls.getAdviserRoles();
    }

    @ApiOperation(
                  value = "Get affiliations",
                  notes = "Returns a list of possible affiliations, as objects with 3 fields",
                  responseClass = "List<Affiliation>")
    @RequestMapping(value = "/affil", method = RequestMethod.GET)
    @ResponseBody
    public List<Affiliation> getAffiliations() throws Exception {
        return adviserControls.getAffiliations();
    }

    @ApiOperation(value = "Get all advisers",
                  notes = "Returns a list of all known advisers",
                  responseClass = "List<Adviser>")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public List<Adviser> getAllAdvisers() {
        return adviserControls.getAllAdvisers();
    }

    @RequestMapping(value = "/changes", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get all changes",
                  notes = "Returns a list of all changes made",
                  responseClass = "Change")
    public List<Change> getAllChanges() throws Exception {
        return adviserControls.getChanges(null);
    }

    @RequestMapping(value = "/changes/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
                  value = "Get changes",
                  notes = "Returns a list of changes made filtered by adviser id",
                  responseClass = "Change")
    public List<Change> getChanges(
            @ApiParam(value = "Adviser id", required = true) @PathVariable final Integer id)
            throws Exception {
        return adviserControls.getChanges(id);
    }

    @RequestMapping(value = "/{id}/drupal", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
                  value = "Get adviser's drupal id",
                  notes = "Returns the drupal id associated with this adviser id",
                  responseClass = "String")
    @ApiErrors({ NoSuchEntityException.class, DatabaseException.class })
    public String getDrupalIdByAdviserId(
            @ApiParam(value = "Adviser id", required = true) @PathVariable final Integer id)
            throws NoSuchEntityException {
        return adviserControls.getDrupalIdByAdviserId(id);
    }

    @RequestMapping(value = "/last_modified", method = RequestMethod.GET)
    @ApiOperation(
                  value = "Get Timestamp",
                  notes = "Returns a timestamp indicating the most recently modified adviser. Useful for caching.")
    @ResponseBody
    public String getLastModified() throws Exception {
        return adviserControls.getLastModified(null);
    }

    @RequestMapping(value = "/{id}/last_modified", method = RequestMethod.GET)
    @ApiOperation(
                  value = "Get Timestamp",
                  notes = "Returns a timestamp indicating the age of this adviser object. Useful for caching.")
    @ResponseBody
    public String getLastModified(
            @ApiParam(value = "Adviser id", required = true) @PathVariable final Integer id)
            throws Exception {
        return adviserControls.getLastModified(id);
    }

    @RequestMapping(value = "/{id}/projects", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
                  value = "Get adviser's projects",
                  notes = "Returns the projects associated with this adviser (regardless of their role)",
                  responseClass = "List<Project>")
    public List<Project> getProjectsForAdviser(
            @ApiParam(value = "Adviser id", required = true) @PathVariable final Integer id) {
        return adviserControls.getProjectsForAdviser(id);
    }

    @RequestMapping(value = "/rollback/{uid}/{rid}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
                  value = "Rollback to some revision",
                  notes = "Reverts the specified adviser to the specified revision",
                  responseClass = "Void")
    public void rollback(
            @ApiParam(value = "Adviser id", required = true) @PathVariable final Integer uid,
            @ApiParam(value = "Revision id", required = true) @PathVariable final Integer rid)
            throws Exception {
        adviserControls.rollback(uid, rid);
    }

    @RequestMapping(value = "/{id}/validate", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
                  value = "Validate",
                  notes = "Validates the adviser object (check mandatory fields set etc)",
                  responseClass = "void")
    public void validateAdviser(
            @ApiParam(value = "Adviser id", required = true) @PathVariable final Integer id)
            throws InvalidEntityException, NoSuchEntityException {
        adviserControls.validateAdviser(id);
    }
}
