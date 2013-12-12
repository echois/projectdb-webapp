package nz.org.nesi.researchHub.view.rest;

import com.mangofactory.swagger.annotations.ApiErrors;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import nz.org.nesi.researchHub.control.AdviserControls;
import nz.org.nesi.researchHub.exceptions.DatabaseException;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
import nz.org.nesi.researchHub.exceptions.NoSuchEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pm.pojo.Adviser;
import pm.pojo.Project;

import java.util.List;

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

    @RequestMapping(value = "/{id}/projects", method = RequestMethod.GET)
    @ResponseBody
    public List<Project> getProjectsForAdviser(@PathVariable int advisorId) {
        return adviserControls.getProjectsForAdviser(advisorId);
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
	public void editAdviser(@PathVariable Integer id, Adviser adviser) throws NoSuchEntityException, InvalidEntityException {
        adviserControls.editAdviser(id, adviser);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public void createAdviser(Adviser adviser) throws InvalidEntityException {
        adviserControls.createAdviser(adviser);
    }

}
