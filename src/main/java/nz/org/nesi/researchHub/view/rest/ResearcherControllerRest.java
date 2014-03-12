package nz.org.nesi.researchHub.view.rest;

import java.util.List;

import nz.org.nesi.researchHub.control.ResearcherControls;
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

import pm.pojo.Change;
import pm.pojo.InstitutionalRole;
import pm.pojo.Project;
import pm.pojo.Researcher;
import pm.pojo.ResearcherProperty;
import pm.pojo.ResearcherRole;
import pm.pojo.ResearcherStatus;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 13/12/13 Time: 12:09 PM
 */
@Controller
@RequestMapping(value = "/researchers")
@Api(value = "/researchers",
     description = "Manage and display researchers and related lists")
public class ResearcherControllerRest {

    @Autowired
    private ResearcherControls researcherControls;

    @ApiOperation(
                  value = "Create researcher",
                  notes = "Create a researcher based on the given researcher object. Returns new id if successfull")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Integer createResearcher(
            @ApiParam(value = "Researcher object", required = true) @RequestBody final Researcher researcher)
            throws InvalidEntityException {
        return researcherControls.createResearcher(researcher);
    }

    @ApiOperation(
                  value = "Delete researcher",
                  notes = "Deletes the researcher with this id. This operation cannot be undone, without restoring a database backup")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@ApiParam(value = "Internal researcher id",
                                 required = true) @PathVariable final Integer id) {
        researcherControls.delete(id);
    }

    @ApiOperation(
                  value = "Edit researcher",
                  notes = "Update a single field in a researcher object. The timestamp included must match the last modified time of the adviser, otherwise an OutOfDateException will be thrown")
    @RequestMapping(value = "/{id}/{field}/{timestamp}/",
                    method = RequestMethod.PUT)
    @ResponseBody
    public void editResearcher(
            @ApiParam(value = "Researcher id", required = true) @PathVariable final Integer id,
            @ApiParam(
                      value = "Desired field to edit, with the first letter capitalised. For example, FullName, Notes, StartDate etc",
                      required = true) @PathVariable final String field,
            @ApiParam(
                      value = "A timestamp indicating the last time the researcher was modified. Used as a consistency check. Set to force to bypass (not recommended)",
                      required = true) @PathVariable final String timestamp,
            @ApiParam(value = "The new value for the field", required = true) @RequestBody final String data)
            throws Exception {
        researcherControls.editResearcher(id, field, timestamp, data);
    }

    @ApiOperation(
                  value = "Edit researcher",
                  notes = "Updates the researcher based on the posted researcher object")
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseBody
    public void editResearcher(
            @ApiParam(value = "Researcher object", required = true) @RequestBody final Researcher researcher)
            throws NoSuchEntityException, InvalidEntityException,
            OutOfDateException {
        researcherControls.editResearcher(researcher);
    }

    @ApiOperation(value = "Get all researchers that match the given filter",
                  notes = "Returns all matched researchers")
    @RequestMapping(value = "/filter/{filter}", method = RequestMethod.GET)
    @ResponseBody
    public List<Researcher> filterResearchers(
            @ApiParam(value = "Search string", required = true) @PathVariable final String filter) {
        return researcherControls.filterResearchers(filter);
    }

    @ApiOperation(
                  value = "Get affiliations",
                  notes = "Returns a list of strings that indicate all possible affiliations a researcher can have")
    @RequestMapping(value = "/affil", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getAffiliations() throws Exception {
        return researcherControls.getAffiliations();
    }

    @RequestMapping(value = "/changes", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get all changes",
                  notes = "Returns a list of all changes made",
                  responseClass = "Change")
    public List<Change> getAllChanges() throws Exception {
        return researcherControls.getChanges(null);
    }

    @ApiOperation(value = "Get all researchers",
                  notes = "Returns all known researchers")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public List<Researcher> getAllResearchers() {
        return researcherControls.getAllResearchers();
    }

    @RequestMapping(value = "/changes/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
                  value = "Get changes",
                  notes = "Returns a list of changes made filtered by researcher id",
                  responseClass = "Change")
    public List<Change> getChanges(
            @ApiParam(value = "Researcher id", required = true) @PathVariable final Integer id)
            throws Exception {
        return researcherControls.getChanges(id);
    }

    @ApiOperation(
                  value = "Get institutional roles",
                  notes = "Returns a list of possible researcher institutional roles")
    @RequestMapping(value = "/iroles", method = RequestMethod.GET)
    @ResponseBody
    public List<InstitutionalRole> getInstitutionalRoles() throws Exception {
        return researcherControls.getInstitutionalRoles();
    }

    @RequestMapping(value = "/last_modified", method = RequestMethod.GET)
    @ApiOperation(
                  value = "Get Timestamp",
                  notes = "Returns a timestamp indicating the most recently modified researcher. Useful for caching.")
    @ResponseBody
    public String getLastModified() throws Exception {
        return researcherControls.getLastModified(null);
    }

    @RequestMapping(value = "/{id}/last_modified", method = RequestMethod.GET)
    @ApiOperation(
                  value = "Get Timestamp",
                  notes = "Returns a timestamp indicating the age of this researcher object. Useful for caching.")
    @ResponseBody
    public String getLastModified(
            @ApiParam(value = "Internal researcher id", required = true) @PathVariable final Integer id)
            throws Exception {
        return researcherControls.getLastModified(id);
    }

    @ApiOperation(value = "Get project for researcher",
                  notes = "Returns the projects that this researcher works on")
    @RequestMapping(value = "/{id}/projects", method = RequestMethod.GET)
    @ResponseBody
    public List<Project> getProjectsForResearcher(
            @ApiParam(value = "Internal researcher id", required = true) @PathVariable final Integer id) {
        return researcherControls.getProjectsForResearcher(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
                  value = "Get researcher",
                  notes = "Returns the researcher object associated with this id")
    public Researcher getResearcher(
            @ApiParam(value = "Internal researcher id", required = true) @PathVariable final Integer id)
            throws NoSuchEntityException {
        return researcherControls.getResearcher(id);
    }

    @ApiOperation(
                  value = "Get researcher's properties",
                  notes = "Returns this researcher's linux username + other details")
    @RequestMapping(value = "/{id}/prop", method = RequestMethod.GET)
    @ResponseBody
    public List<ResearcherProperty> getResearcherProperties(
            @ApiParam(value = "Internal researcher id", required = true) @PathVariable final Integer id)
            throws Exception {
        return researcherControls.getResearcherProperties(id);
    }

    @ApiOperation(
                  value = "Get researcher roles",
                  notes = "Returns a list of all possible roles a researcher can have on a project")
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ResponseBody
    public List<ResearcherRole> getResearcherRoles() throws Exception {
        return researcherControls.getResearcherRoles();
    }

    @ApiOperation(value = "Get statuses",
                  notes = "Returns a list of possible researcher statuses")
    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    @ResponseBody
    public List<ResearcherStatus> getStatuses() throws Exception {
        return researcherControls.getStatuses();
    }

    @RequestMapping(value = "/rollback/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Rollback to some revision",
                  notes = "Reverts to the specified revision",
                  responseClass = "Void")
    public void rollback(
            @ApiParam(value = "Revision id", required = true) @PathVariable final Integer id)
            throws Exception {
        researcherControls.rollback(id);
    }

    @ApiOperation(value = "Upsert researcher property",
                  notes = "Replace or insert researcher property")
    @RequestMapping(value = "/{id}/prop", method = RequestMethod.PUT)
    @ResponseBody
    public void upsertResearcherProperty(@RequestBody final ResearcherProperty r)
            throws Exception {
        researcherControls.upsertProperty(r);
    }

}
