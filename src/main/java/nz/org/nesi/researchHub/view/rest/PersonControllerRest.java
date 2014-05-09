package nz.org.nesi.researchHub.view.rest;

import java.util.List;

import nz.org.nesi.researchHub.control.PersonControls;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
import nz.org.nesi.researchHub.exceptions.NoSuchEntityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pm.pojo.Person;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/persons")
@Api(value = "/persons",
		description = "Manage and display persons and related lists")
public class PersonControllerRest {

	@Autowired
	private PersonControls personControls;

	@ApiOperation(value = "Create new person",
					notes = "Returns the generated person id, if successful",
					responseClass = "Integer")
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public Integer createPerson(
			@ApiParam(value = "Person object", required = true) @RequestBody final Person person)
			throws InvalidEntityException {
		return personControls.createPerson(person);
	}

	@ApiOperation(value = "Get all persons",
					notes = "Returns a list of all known persons",
					responseClass = "List<Person>")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<Person> getAllPersons() {
		return personControls.getAllPersons();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(
					value = "Get an person by their id",
					notes = "Returns the person object associated with this id",
					responseClass = "Person")
	public Person getPerson(
			@ApiParam(value = "Person id", required = true) @PathVariable final Integer id)
			throws NoSuchEntityException {
		return personControls.getPerson(id);
	}

}