package nz.org.nesi.researchHub.view.rest;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import nz.org.nesi.researchHub.control.AdviserControls;
import nz.org.nesi.researchHub.control.ProjectControls;
import nz.org.nesi.researchHub.control.ResearcherControls;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
import nz.org.nesi.researchHub.exceptions.NoSuchEntityException;
import nz.org.nesi.researchHub.model.ProjectEnvironmentModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pm.pojo.APLink;
import pm.pojo.Adviser;
import pm.pojo.AdviserAction;
import pm.pojo.AdviserRole;
import pm.pojo.Attachment;
import pm.pojo.Change;
import pm.pojo.Facility;
import pm.pojo.FollowUp;
import pm.pojo.Kpi;
import pm.pojo.KpiCode;
import pm.pojo.Project;
import pm.pojo.ProjectAllocation;
import pm.pojo.ProjectKpi;
import pm.pojo.ProjectProperty;
import pm.pojo.ProjectStatus;
import pm.pojo.ProjectType;
import pm.pojo.ProjectWrapper;
import pm.pojo.RPLink;
import pm.pojo.ResearchOutput;
import pm.pojo.ResearchOutputType;
import pm.pojo.Researcher;
import pm.pojo.ResearcherRole;
import pm.pojo.Review;
import pm.pojo.Site;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 13/12/13 Time: 12:01 PM
 */
@Controller
@RequestMapping(value = "/projects")
@Api(value = "/projects", description = "Manage and display projects")
public class ProjectControllerRest {

	@Autowired
	private AdviserControls adviserControls;

	@Autowired
	private ProjectControls projectControls;

	@Autowired
	private ResearcherControls researcherControls;

	@RequestMapping(value = "/adviseraction", method = RequestMethod.PUT)
	@ApiOperation(value = "Add Adviser Action",
					notes = "Add adviser action to project")
	@ResponseBody
	public void addAdviserAction(
			@ApiParam(value = "AdviserAction object", required = true) @RequestBody final AdviserAction aa)
			throws Exception {
		projectControls.addAdviserAction(aa);
	}

	@RequestMapping(value = "/ap", method = RequestMethod.PUT)
	@ApiOperation(value = "Add APLink",
					notes = "Add APLink (adviser) to project")
	@ResponseBody
	public void addAPLink(
			@ApiParam(value = "APLink object", required = true) @RequestBody final APLink al)
			throws Exception {
		projectControls.addAdviser(al);
	}

	@RequestMapping(value = "/attachment", method = RequestMethod.PUT)
	@ApiOperation(value = "Add Attachment", notes = "Add attachment to object")
	@ResponseBody
	public void addAttachment(
			@ApiParam(value = "Attachment object", required = true) @RequestBody final Attachment a)
			throws Exception {
		projectControls.addAttachment(a);
	}

	@RequestMapping(value = "/kpi", method = RequestMethod.PUT)
	@ApiOperation(value = "Add Project KPI", notes = "Add KPI to project")
	@ResponseBody
	public void addKpi(
			@ApiParam(value = "ProjectKpi object", required = true) @RequestBody final ProjectKpi pk)
			throws Exception {
		projectControls.addKpi(pk);
	}

	// DON'T IMPLEMENT EDIT PROJECT YET, EXPOSING THE PROJECTWRAPPER OBJECT IS A
	// TAD
	// UGLY, LET'S THINK OF A BETTER WAY...
	// @RequestMapping(value = "/{id}", method = RequestMethod.POST)
	// @ResponseBody
	// public void editProjectWrapper(@PathVariable Integer id, ProjectWrapper
	// project) throws InvalidEntityException {
	// projectControls.editProjectWrapper(id, project);
	// }

	@RequestMapping(value = "/review", method = RequestMethod.PUT)
	@ApiOperation(value = "Add Review", notes = "Add review to project")
	@ResponseBody
	public void addReview(
			@ApiParam(value = "Review object", required = true) @RequestBody final Review r)
			throws Exception {
		projectControls.addReview(r);
	}

	@RequestMapping(value = "/rp", method = RequestMethod.PUT)
	@ApiOperation(value = "Add RPLink",
					notes = "Add RPLink (researcher) to project")
	@ResponseBody
	public void addRPLink(
			@ApiParam(value = "RPLink object", required = true) @RequestBody final RPLink rl)
			throws InvalidEntityException {
		projectControls.addResearcher(rl);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ApiOperation(
					value = "Create project",
					notes = "Creates a new project from the given object. Returns the new project id if successfull")
	@ResponseBody
	public synchronized Integer createProjectWrapper(
			@ApiParam(
						value = "ProjectWrapper object. Note that not all parts (FollowUps etc) are required.",
						required = true) @RequestBody final ProjectWrapper pw)
			throws InvalidEntityException {
		return projectControls.createProjectWrapper(pw);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(
					value = "Delete project",
					notes = "Deletes a project. This cannot be undone, unless the database is restored from backup")
	@ResponseBody
	public void delete(
			@ApiParam(value = "Project id", required = true) @PathVariable final Integer id) {
		projectControls.delete(id);
	}

	@RequestMapping(value = "/{id}/{object}/{field}/{timestamp}/",
					method = RequestMethod.POST)
	@ApiOperation(
					value = "Edit field",
					notes = "Edits a single field in a project wrapper. The timestamp included must match the last modified time of the adviser, otherwise an OutOfDateException will be thrown")
	@ResponseBody
	public void editProjectWrapper(
			@ApiParam(value = "Project id", required = true) @PathVariable final Integer id,
			@ApiParam(
						value = "Desired object in project wrapper to edit, with the first letter capitalised. Use underscores to separate indexes/attachments. For example, Project, APLinks_0, FollowUps_0_Attachments_0 etc",
						required = true) @PathVariable final String object,
			@ApiParam(
						value = "Desired field to edit, with the first letter capitalised. For example, FullName, Description, StartDate etc",
						required = true) @PathVariable final String field,
			@ApiParam(
						value = "A timestamp indicating the last time the adviser was modified. Used as a consistency check. Set to force to bypass (not recommended)",
						required = true) @PathVariable final String timestamp,
			@ApiParam(value = "The new value for the field", required = true) @RequestBody final String data)
			throws Exception {
		projectControls.editProjectWrapper(id, object, field, timestamp, data);
	}

	@RequestMapping(value = "/filter/{filter}", method = RequestMethod.GET)
	@ApiOperation(
					value = "Get a filtered list of projects",
					notes = "Searches for the given string in any of the project's fields")
	@ResponseBody
	public List<Project> filterProjects(
			@ApiParam(value = "Search string", required = true) @PathVariable final String filter) {
		return projectControls.filterProjects(filter);
	}

	@RequestMapping(value = "/missingdetails", method = RequestMethod.GET)
	@ApiOperation(value = "Get a list of projects missing details",
					notes = "Searches the list of projects missing details")
	@ResponseBody
	public List<Project> getProjectsMissingDetails() throws Exception {
		return projectControls.getProjectsMissingDetails();
	}

	@RequestMapping(value = "/changes", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "Get all changes",
					notes = "Returns a list of all changes made",
					responseClass = "Change")
	public List<Change> getAllChanges() throws Exception {
		return projectControls.getChanges(null);
	}

	@RequestMapping(value = "/changes/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(
					value = "Get changes",
					notes = "Returns a list of changes made filtered by project id",
					responseClass = "Change")
	public List<Change> getChanges(
			@ApiParam(value = "Adviser id", required = true) @PathVariable final Integer id)
			throws Exception {
		return projectControls.getChanges(id);
	}

	@RequestMapping(value = "/fac", method = RequestMethod.GET)
	@ApiOperation(value = "Get facilities",
					notes = "Returns a list of possible project facilities")
	@ResponseBody
	public List<Facility> getFacilities() throws Exception {
		return projectControls.getFacilities();
	}

	@RequestMapping(value = "/inst", method = RequestMethod.GET)
	@ApiOperation(value = "Get institutions",
					notes = "Returns a list of possible project institutions")
	@ResponseBody
	public List<String> getInstitutions() throws Exception {
		return projectControls.getInstitutions();
	}

	@RequestMapping(value = "/kpic", method = RequestMethod.GET)
	@ApiOperation(value = "Get KPI Codes",
					notes = "Returns a list of possible project KPI codes")
	@ResponseBody
	public List<KpiCode> getKpiCodes() throws Exception {
		return projectControls.getKpiCodes();
	}

	@RequestMapping(value = "/kpis", method = RequestMethod.GET)
	@ApiOperation(value = "Get KPIS",
					notes = "Returns a list of possible project KPIs")
	@ResponseBody
	public List<Kpi> getKpis() throws Exception {
		return projectControls.getKpis();
	}

	@RequestMapping(value = "/last_modified", method = RequestMethod.GET)
	@ApiOperation(
					value = "Get Timestamp",
					notes = "Returns a timestamp indicating the most recently modified project. Useful for caching.")
	@ResponseBody
	public String getLastModified() throws Exception {
		return projectControls.getLastModified(null);
	}

	@RequestMapping(value = "/{id}/last_modified", method = RequestMethod.GET)
	@ApiOperation(
					value = "Get Timestamp",
					notes = "Returns a timestamp the age of this project. Useful for caching.")
	@ResponseBody
	public String getLastModified(
			@ApiParam(value = "Project id", required = true) @PathVariable final Integer id)
			throws Exception {
		return projectControls.getLastModified(id);
	}

	@RequestMapping(value = "/akpis", method = RequestMethod.GET)
	@ApiOperation(value = "Get Project KPIS",
					notes = "Returns a list of all reported KPIs")
	@ResponseBody
	public List<ProjectKpi> getProjectKpis() throws Exception {
		return projectControls.getProjectKpis();
	}

	@RequestMapping(value = "/{id}/prop", method = RequestMethod.GET)
	@ApiOperation(
					value = "Get project properties",
					notes = "Returns a list of project properties for a given project id")
	@ResponseBody
	public List<ProjectProperty> getProjectProperties(
			@ApiParam(value = "Project id", required = true) @PathVariable final Integer id) {
		return projectControls.getProjectProperties(id);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ApiOperation(value = "Get all projects",
					notes = "Returns every project in the database")
	@ResponseBody
	public List<Project> getProjects() {
		return projectControls.getProjects();
	}

	@RequestMapping(value = "/full", method = RequestMethod.GET)
	@ApiOperation(value = "Get all projects, including members per role",
					notes = "Returns every project in the database")
	@ResponseBody
	public java.util.Map<String, Map<String, Set<String>>> getProjectsWithMembers()
			throws Exception {
		return projectControls.getAllProjectsAndMembers();
	}

	@RequestMapping(value = "/stat", method = RequestMethod.GET)
	@ApiOperation(value = "Get Project Statuses",
					notes = "Returns a list of possible project statuses")
	@ResponseBody
	public List<ProjectStatus> getProjectStatuses() throws Exception {
		return projectControls.getProjectStatuses();
	}

	@RequestMapping(value = "/type", method = RequestMethod.GET)
	@ApiOperation(value = "Get Project Types",
					notes = "Returns a list of possible project types")
	@ResponseBody
	public List<ProjectType> getProjectTypes() throws Exception {
		return projectControls.getProjectTypes();
	}

	@RequestMapping(value = "/{projectIdOrCode}", method = RequestMethod.GET)
	// this is an example how we'll do authorization later on, at the moment it
	// won't actually enforce anything
	// @PreAuthorize("hasPermission(#projectIdOrCode, 'read_project' )")
	@ApiOperation(
					value = "Get project wrapper",
					notes = "Returns a complete project wrapper object upon input of a id or code")
	@ResponseBody
	public ProjectWrapper getProjectWrapper(
			@ApiParam(value = "Project id or code", required = true) @PathVariable final String projectIdOrCode) {
		return projectControls.getProjectWrapper(projectIdOrCode);
	}

	@RequestMapping(value = "/aro", method = RequestMethod.GET)
	@ApiOperation(value = "Get Research Outputs",
					notes = "Returns a list of reported Research Outputs")
	@ResponseBody
	public List<ResearchOutput> getResearchOutput() throws Exception {
		return projectControls.getResearchOutput();
	}

	@RequestMapping(value = "/rotype", method = RequestMethod.GET)
	@ApiOperation(value = "Get Research Output Types",
					notes = "Returns a list of possible research output types")
	@ResponseBody
	public List<ResearchOutputType> getROTypes() throws Exception {
		return projectControls.getROTypes();
	}

	@RequestMapping(value = "/sites", method = RequestMethod.GET)
	@ApiOperation(value = "Get sites",
					notes = "Returns a list of possible cluster sites")
	@ResponseBody
	public List<Site> getSites() throws Exception {
		return projectControls.getSites();
	}

	@RequestMapping(value = "/environment", method = RequestMethod.GET)
	@ApiOperation(
					value = "Get project environment",
					notes = "Returns a map of all relevant up-to-date helper objects/lists, convenience method.")
	@ResponseBody
	public ProjectEnvironmentModel getUpToDateEnivironment() throws Exception {

		final ProjectEnvironmentModel pem = new ProjectEnvironmentModel();

		ExecutorService executor = Executors.newFixedThreadPool(12);

		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					List<String> institutions = getInstitutions();
					pem.setInstitutions(institutions);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executor.execute(t);
		t = new Thread() {
			@Override
			public void run() {
				try {
					List<Facility> faculties = getFacilities();
					pem.setFaculties(faculties);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executor.execute(t);
		t = new Thread() {
			@Override
			public void run() {
				try {
					List<Site> sites = getSites();
					pem.setSites(sites);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executor.execute(t);
		t = new Thread() {
			@Override
			public void run() {
				try {
					List<ProjectType> types = getProjectTypes();
					pem.setTypes(types);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executor.execute(t);
		t = new Thread() {
			@Override
			public void run() {
				try {
					List<ProjectStatus> stats = getProjectStatuses();
					pem.setStats(stats);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executor.execute(t);
		t = new Thread() {
			@Override
			public void run() {
				try {
					List<Kpi> kpis = getKpis();
					pem.setKpis(kpis);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executor.execute(t);
		t = new Thread() {
			@Override
			public void run() {
				try {
					List<KpiCode> kpcs = getKpiCodes();
					pem.setKpcs(kpcs);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executor.execute(t);
		t = new Thread() {
			@Override
			public void run() {
				try {
					List<ResearchOutputType> rotypes = getROTypes();
					pem.setRotypes(rotypes);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executor.execute(t);
		t = new Thread() {
			@Override
			public void run() {
				try {
					List<ResearcherRole> roles = researcherControls
							.getResearcherRoles();
					pem.setRoles(roles);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executor.execute(t);
		t = new Thread() {
			@Override
			public void run() {
				try {
					List<Researcher> researchers = researcherControls
							.getAllResearchers();
					pem.setResearchers(researchers);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executor.execute(t);
		t = new Thread() {
			@Override
			public void run() {
				try {
					List<AdviserRole> ad_roles = adviserControls
							.getAdviserRoles();
					pem.setAd_roles(ad_roles);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executor.execute(t);
		t = new Thread() {
			@Override
			public void run() {
				try {
					List<Adviser> advisers = adviserControls.getAllAdvisers();
					pem.setAdvisers(advisers);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		executor.execute(t);

		executor.shutdown();

		executor.awaitTermination(10, TimeUnit.SECONDS);

		return pem;

	}

	@RequestMapping(value = "/{id}/{oid}/{type}", method = RequestMethod.DELETE)
	@ApiOperation(
					value = "Remove object link from project",
					notes = "Removes someone (Adviser or Researcher)/something (FollowUp etc) from a project. Possible object types: adviser, researcher, kpi, researchoutput, review, followup, adviseraction, property, Attachments_#")
	@ResponseBody
	public void remove(
			@ApiParam(value = "Internal project id", required = true) @PathVariable final Integer id,
			@ApiParam(
						value = "Object id. Context specific - is either adviserId, researcherId or array index",
						required = true) @PathVariable final Integer oid,
			@ApiParam(value = "Object type. adviser, researcher, kpi etc",
						required = true) @PathVariable final String type) {
		projectControls.removeObjectLink(id, oid, type);
	}

	@RequestMapping(value = "/rollback/{uid}/{rid}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(
					value = "Rollback to some revision",
					notes = "Reverts the specified project to the specified revision",
					responseClass = "Void")
	public void rollback(
			@ApiParam(value = "Project id", required = true) @PathVariable final Integer uid,
			@ApiParam(value = "Revision id", required = true) @PathVariable final Integer rid)
			throws Exception {
		projectControls.rollback(uid, rid);
	}

	@RequestMapping(value = "/followup", method = RequestMethod.PUT)
	@ApiOperation(value = "Add/Edit Follow Up",
					notes = "Add/Edit followup on project")
	@ResponseBody
	public void upsertFollowUp(
			@ApiParam(value = "FollowUp object", required = true) @RequestBody final FollowUp f)
			throws Exception {
		projectControls.upsertFollowUp(f);
	}

	@RequestMapping(value = "/prop", method = RequestMethod.PUT)
	@ApiOperation(value = "Upsert Project Property",
					notes = "Add or edit project property")
	@ResponseBody
	public void upsertProperty(
			@ApiParam(value = "ProjectProperty object", required = true) @RequestBody final ProjectProperty p)
			throws Exception {
		projectControls.upsertProperty(p);
	}

	@RequestMapping(value = "/alloc/project/{projectIdOrCode}",
					method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(
					value = "Get project allocations by its project id or code",
					notes = "Returns the project allocation object associated with project id or code")
	public List<ProjectAllocation> getAllocationsByProject(
			@ApiParam(value = "Project id or code", required = true) @PathVariable final String projectIdOrCode)
			throws NoSuchEntityException {
		return projectControls.getAllocationsByProject(projectIdOrCode);
	}

	@RequestMapping(value = "/alloc", method = RequestMethod.PUT)
	@ApiOperation(value = "Upsert Project Allocation",
					notes = "Add or edit project allocation")
	@ResponseBody
	public void upsertProjectAllocation(
			@ApiParam(value = "ProjectAllocation object", required = true) @RequestBody final ProjectAllocation pa)
			throws Exception {
		projectControls.upsertProjectAllocation(pa);
	}

	@ApiOperation(
					value = "Get all project allocations",
					notes = "Returns a list of objects that indicate all project allocations")
	@RequestMapping(value = "/all/alloc", method = RequestMethod.GET)
	@ResponseBody
	public List<ProjectAllocation> getAllProjectAllocations() throws Exception {
		return projectControls.getAllProjectAllocations();
	}

	@ResponseBody
	@ApiOperation(
					value = "Get an project allcoation by id",
					notes = "Returns the project allocation object associated with this id",
					responseClass = "ProjectAllocation")
	@RequestMapping(value = "/alloc/{id}", method = RequestMethod.GET)
	public ProjectAllocation getProjectAllocationById(
			@ApiParam(value = "ProjectAllocation id", required = true) @PathVariable final Integer id)
			throws NoSuchEntityException {
		return projectControls.getProjectAllocationById(id);
	}

	@RequestMapping(value = "/alloc/{facilityId}/facility",
					method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(
					value = "Get project allocation by its facility",
					notes = "Returns the project allocation object associated with facility")
	public List<ProjectAllocation> getProjectAllocationByFacility(
			@ApiParam(value = "facility name", required = true) @PathVariable final Integer facilityId)
			throws NoSuchEntityException {
		return projectControls.getProjectAllocationsByFacility(facilityId);
	}

	@ApiOperation(
					value = "Delete project allocation",
					notes = "Deletes project allocation. This operation cannot be undone, unless the database is restored",
					responseClass = "void")
	@RequestMapping(value = "/alloc/delete/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteProjectAllocation(
			@ApiParam(value = "Project Allocation id", required = true) @PathVariable final Integer id) {
		projectControls.deleteProjectAllocation(id);
	}

	@RequestMapping(value = "/ro", method = RequestMethod.PUT)
	@ApiOperation(value = "Add/Edit Research Output",
					notes = "Add/Edit research output on project")
	@ResponseBody
	public void upsertResearchOutput(
			@ApiParam(value = "ResearchOutput object", required = true) @RequestBody final ResearchOutput ro)
			throws Exception {
		projectControls.upsertResearchOutput(ro);
	}

	@RequestMapping(value = "/{id}/validate", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(
					value = "Validate",
					notes = "Validates the project wrapper object (check mandatory fields set etc)",
					responseClass = "void")
	public void validateProject(
			@ApiParam(value = "Project id", required = true) @PathVariable final Integer id)
			throws InvalidEntityException, NoSuchEntityException {
		projectControls.validateProject(id);
	}

}
