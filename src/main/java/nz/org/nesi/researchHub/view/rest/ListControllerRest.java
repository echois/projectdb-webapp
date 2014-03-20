package nz.org.nesi.researchHub.view.rest;

import java.util.List;

import nz.org.nesi.researchHub.control.ListControls;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
import nz.org.nesi.researchHub.exceptions.NoSuchEntityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pm.pojo.Adviser;
import pm.pojo.Affiliation;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * @author echoi
 */
@Controller
@RequestMapping(value = "/lists")
@Api(value = "/lists",	description = "Manage and display affiliations and related lists")
public class ListControllerRest {

	@Autowired
	private ListControls listControls;

	@ApiOperation(value = "Get all affiliations", notes = "Returns a list of strings that indicate all affiliations")
	@RequestMapping(value = "/affil", method = RequestMethod.GET)
	@ResponseBody
	public final List<Affiliation> getAffiliations() throws Exception {
		return listControls.getAllAffiliations();
	}
	
    @RequestMapping(value="/affil/create", method=RequestMethod.POST)
	@ApiOperation(value="Create list", notes="Creates a new affiliation from the given object.")
	@ResponseBody
	public final void createAffiliation(
            @ApiParam(value = "Affiliation object", required = true) @RequestBody final Affiliation affiliation)
            throws InvalidEntityException {
        listControls.createAffiliation(affiliation);
    }       
}
