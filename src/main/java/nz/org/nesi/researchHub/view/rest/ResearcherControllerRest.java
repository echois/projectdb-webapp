package nz.org.nesi.researchHub.view.rest;

import java.util.List;

import nz.org.nesi.researchHub.control.ResearcherControls;
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

import pm.pojo.InstitutionalRole;
import pm.pojo.Project;
import pm.pojo.Researcher;
import pm.pojo.ResearcherRole;

import com.mangofactory.swagger.annotations.ApiErrors;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 13/12/13
 * Time: 12:09 PM
 */
@Controller
@RequestMapping(value = "/researchers")
@Api( value = "/researchers", description = "Manage and display researchers and related lists" )
public class ResearcherControllerRest {

    @Autowired
    private ResearcherControls researcherControls;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
    value = "Researcher id",
    notes = "Returns the researcher object associated with this id",
    responseClass = "Researcher"
    )
    @ApiErrors({NoSuchEntityException.class, DatabaseException.class})
    public Researcher getResearcher(@ApiParam( value = "Internal researcher id", required = true ) @PathVariable Integer id) throws NoSuchEntityException {
        return researcherControls.getResearcher(id);
    }

    @RequestMapping(value = "/{id}/projects", method = RequestMethod.GET)
    @ResponseBody
    public List<Project> getProjectsForResearcher(@PathVariable Integer id) {
        return researcherControls.getProjectsForResearcher(id);
    }
    
    @RequestMapping(value = "/{id}/linux", method = RequestMethod.GET)
    @ResponseBody
    public String getLinuxUsernameForResearcher(@PathVariable Integer id) throws Exception {
        return researcherControls.getLinuxUsernameForResearcher(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
	public List<Researcher> getAllResearchers() {
        return researcherControls.getAllResearchers();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
	public void delete(@PathVariable Integer id) {
        researcherControls.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseBody
	public void editResearcher(@PathVariable Integer id, @RequestBody Researcher researcher) throws NoSuchEntityException, InvalidEntityException, OutOfDateException {
        researcherControls.editResearcher(id, researcher);
    }
    
    @RequestMapping(value = "/{id}/{field}/{timestamp}/", method = RequestMethod.POST)
    @ResponseBody
	public void editResearcher(@PathVariable Integer id, @PathVariable String field, @PathVariable String timestamp, @RequestBody String data) throws NoSuchEntityException, InvalidEntityException, OutOfDateException {
        researcherControls.editResearcher(id, field, timestamp, data);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public void createResearcher(@RequestBody Researcher researcher) throws InvalidEntityException {
        researcherControls.createResearcher(researcher);
    }
    
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ResponseBody
	public List<ResearcherRole> getResearcherRoles() throws Exception {
        return researcherControls.getResearcherRoles();
    }
    
    @RequestMapping(value = "/iroles", method = RequestMethod.GET)
    @ResponseBody
	public List<InstitutionalRole> getInstitutionalRoles() throws Exception {
        return researcherControls.getInstitutionalRoles();
    }

}
