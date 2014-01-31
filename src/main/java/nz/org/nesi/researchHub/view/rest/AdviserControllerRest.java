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
import pm.pojo.Project;

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
@RequestMapping(value = "/advisers")
@Api( value = "/advisers", description = "Manage and display advisers" )
public class AdviserControllerRest {

    @Autowired
    private AdviserControls adviserControls;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
    value = "Adviser id",
    notes = "Returns the adviser object associated with this id",
    responseClass = "Adviser"
    )
    @ApiErrors({NoSuchEntityException.class, DatabaseException.class})
    public Adviser getAdviser(@ApiParam( value = "Internal adviser id", required = true ) @PathVariable Integer id) throws NoSuchEntityException {
        return adviserControls.getAdviser(id);
    }
    
    @RequestMapping(value = "/drupal/{drupalId}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
    value = "Adviser drupal id",
    notes = "Returns the adviser object associated with this drupal id",
    responseClass = "Adviser"
    )
    @ApiErrors({NoSuchEntityException.class, DatabaseException.class})
    public Adviser getAdviserByDrupalId(@ApiParam( value = "Advisers drupal id", required = true ) @PathVariable String drupalId) throws NoSuchEntityException {
        return adviserControls.getAdviserByDrupalId(drupalId);
    }
    
    @RequestMapping(value = "/{id}/drupal", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(
    value = "Adviser id",
    notes = "Returns the drupal id associated with this adviser id",
    responseClass = "String"
    )
    @ApiErrors({NoSuchEntityException.class, DatabaseException.class})
    public String getDrupalIdByAdviserId(@ApiParam( value = "Adviser id", required = true ) @PathVariable Integer id) throws NoSuchEntityException {
        return adviserControls.getDrupalIdByAdviserId(id);
    }

    @RequestMapping(value = "/{id}/projects", method = RequestMethod.GET)
    @ResponseBody
    public List<Project> getProjectsForAdviser(@PathVariable Integer id) {
        return adviserControls.getProjectsForAdviser(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
	public List<Adviser> getAllAdvisers() {
        return adviserControls.getAllAdvisers();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
	public void delete(@PathVariable Integer id) {
        adviserControls.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseBody
	public void editAdviser(@PathVariable Integer id, Adviser adviser) throws NoSuchEntityException, InvalidEntityException, OutOfDateException {
        adviserControls.editAdviser(id, adviser);
    }
    
    @RequestMapping(value = "/{id}/{field}/{timestamp}/", method = RequestMethod.POST)
    @ResponseBody
	public void editAdviser(@PathVariable Integer id, @PathVariable String field, @PathVariable String timestamp, @RequestBody String data) throws NoSuchEntityException, InvalidEntityException, OutOfDateException {
    	adviserControls.editAdviser(id, field, timestamp, data);
    }

    @ApiOperation( value = "Create new adviser", notes = "Returns the generated adviser id, if successful" )
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public Integer createAdviser(Adviser adviser) throws InvalidEntityException {
        return adviserControls.createAdviser(adviser);
    }
    
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ResponseBody
	public List<AdviserRole> getAdviserRoles() throws Exception {
        return adviserControls.getAdviserRoles();
    }
    
    @RequestMapping(value = "/affil", method = RequestMethod.GET)
    @ResponseBody
	public List<Affiliation> getAffiliations() throws Exception {
        return adviserControls.getAffiliations();
    }

}
