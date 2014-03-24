package nz.org.nesi.researchHub.view.rest;

import java.util.List;
import java.util.Map;

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

import pm.pojo.Affiliation;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * @author echoi
 */
@Controller
@RequestMapping(value = "/lists")
@Api(value = "/lists",
		description = "Manage and display affiliations and related lists")
public class ListControllerRest {

	@Autowired
	private ListControls listControls;

	@ApiOperation(
					value = "Get all affiliations",
					notes = "Returns a list of strings that indicate all affiliations")
	@RequestMapping(value = "/affil", method = RequestMethod.GET)
	@ResponseBody
	public final List<Affiliation> getAffiliations() throws Exception {
		return listControls.getAllAffiliations();
	}

	@ApiOperation(
					value = "Get all affiliations",
					notes = "Returns a list of strings that indicate affiliations")
	@RequestMapping(value = "/affil/strings", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getAffiliationStrings() throws Exception {

		try {
			return listControls.getAffiliationStrings();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@ApiOperation(
					value = "Get all affiliations on specific institution",
					notes = "Returns a list of objects that indicate affiliations by institution code")
	@RequestMapping(value = "/affil/inst/{institutionCode}",
					method = RequestMethod.GET)
	@ResponseBody
	public List<Affiliation> getAffiliationsByInstitutionCode(
			@ApiParam(value = "Institution code", required = true) @PathVariable final String institutionCode)
			throws NoSuchEntityException {

		return listControls.getAffiliationsByInstitutionCode(institutionCode);
	}

	@RequestMapping(value = "/affil/create", method = RequestMethod.POST)
	@ApiOperation(value = "Create affiliation",
					notes = "Creates a new affiliation from the given object.")
	@ResponseBody
	public final void createAffiliation(
			@ApiParam(value = "Affiliation object", required = true) @RequestBody final Affiliation affiliation)
			throws InvalidEntityException {
		listControls.createAffiliation(affiliation);
	}

	@ApiOperation(
					value = "Get all divisions on specific institution",
					notes = "Returns a list of strings that indicate divisions by institution code")
	@RequestMapping(value = "/affil/inst/dilist/{institutionCode}",
					method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> getDivisionsByInstitutionCode(

			@ApiParam(value = "Institution code", required = true) @PathVariable final String institutionCode)
			throws NoSuchEntityException {

		return listControls.getDivisionsByInstitutionCode(institutionCode);
	}

	@ApiOperation(
					value = "Get all departments on specific division",
					notes = "Returns a list of strings that indicate departments by division code")
	@RequestMapping(value = "/affil/divi/delist/{divisionCode}",
					method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> getDepartmentsByDivisionCode(

			@ApiParam(value = "Division code", required = true) @PathVariable final String divisionCode)
			throws NoSuchEntityException {

		return listControls.getDepartmentsByDivisionCode(divisionCode);
	}

	@ApiOperation(
					value = "Get all affiliations on specific division",
					notes = "Returns a list of objects that indicate affiliations by division code")
	@RequestMapping(value = "/affil/divi/{divisionCode}",
					method = RequestMethod.GET)
	@ResponseBody
	public List<Affiliation> getAffiliationsByDivisionCode(

			@ApiParam(value = "Division code", required = true) @PathVariable final String divisionCode)
			throws NoSuchEntityException {

		return listControls.getAffiliationsByDivisionCode(divisionCode);
	}

	// TODO: To add validation to generate unique code
}
