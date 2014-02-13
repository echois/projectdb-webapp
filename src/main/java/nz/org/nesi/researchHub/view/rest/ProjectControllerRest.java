package nz.org.nesi.researchHub.view.rest;

import java.util.List;

import nz.org.nesi.researchHub.control.ProjectControls;
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

import pm.pojo.APLink;
import pm.pojo.AdviserAction;
import pm.pojo.Attachment;
import pm.pojo.Facility;
import pm.pojo.FollowUp;
import pm.pojo.Kpi;
import pm.pojo.KpiCode;
import pm.pojo.Project;
import pm.pojo.ProjectKpi;
import pm.pojo.ProjectProperty;
import pm.pojo.ProjectStatus;
import pm.pojo.ProjectType;
import pm.pojo.ProjectWrapper;
import pm.pojo.RPLink;
import pm.pojo.ResearchOutput;
import pm.pojo.ResearchOutputType;
import pm.pojo.Review;
import pm.pojo.Site;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 13/12/13
 * Time: 12:01 PM
 */
@Controller
@RequestMapping(value = "/projects")
@Api( value = "/projects", description = "Manage and display projects" )
public class ProjectControllerRest {

    @Autowired
    private ProjectControls projectControls;

    @RequestMapping(value = "/{projectIdOrCode}", method = RequestMethod.GET)
    // this is an example how we'll do authorization later on, at the moment it won't actually enforce anything
    //@PreAuthorize("hasPermission(#projectIdOrCode, 'read_project' )")
    @ApiOperation( value = "Get project wrapper", notes = "Returns a complete project wrapper object upon input of a id or code" )
    @ResponseBody
    public ProjectWrapper getProjectWrapper(@ApiParam( value = "Project id or code", required = true ) @PathVariable String projectIdOrCode) {
        return projectControls.getProjectWrapper(projectIdOrCode);
    }
    
    @RequestMapping(value = "/{id}/prop", method = RequestMethod.GET)
    @ApiOperation( value = "Get project properties", notes = "Returns a list of project properties for a given project id" )
    @ResponseBody
    public List<ProjectProperty> getProjectProperties(@ApiParam( value = "Project id", required = true ) @PathVariable Integer id) {
        return projectControls.getProjectProperties(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation( value = "Get all projects", notes = "Returns every project in the database" )
    @ResponseBody
    public List<Project> getProjects() {
        return projectControls.getProjects();
    }

    @RequestMapping(value = "/filter/{filter}", method = RequestMethod.GET)
    @ApiOperation( value = "Get a filtered list of projects", notes = "Searches for the given string in any of the project's fields" )
    @ResponseBody
    public List<Project> filterProjects(@ApiParam( value = "Search string", required = true ) @PathVariable String filter) {
        return projectControls.filterProjects(filter);
    }

//    DON'T IMPLEMENT EDIT PROJECT YET, EXPOSING THE PROJECTWRAPPER OBJECT IS A TAD
//    UGLY, LET'S THINK OF A BETTER WAY...
//    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
//    @ResponseBody
//    public void editProjectWrapper(@PathVariable Integer id, ProjectWrapper project) throws InvalidEntityException {
//        projectControls.editProjectWrapper(id, project);
//    }
    

    @RequestMapping(value = "/{id}/{object}/{field}/{timestamp}/", method = RequestMethod.POST)
    @ApiOperation( value = "Edit field", notes = "Edits a single field in a project wrapper. The timestamp included must match the last modified time of the adviser, otherwise an OutOfDateException will be thrown" )
    @ResponseBody
    public void editProjectWrapper(@ApiParam( value = "Project id", required = true ) @PathVariable Integer id, 
    		@ApiParam( value = "Desired object in project wrapper to edit, with the first letter capitalised. Use underscores to separate indexes/attachments. For example, Project, APLinks_0, FollowUps_0_Attachments_0 etc", required = true ) @PathVariable String object, 
    		@ApiParam( value = "Desired field to edit, with the first letter capitalised. For example, FullName, Description, StartDate etc", required = true ) @PathVariable String field, 
    		@ApiParam( value = "A timestamp indicating the last time the adviser was modified. Used as a consistency check. Set to force to bypass (not recommended)", required = true ) @PathVariable String timestamp, 
    		@ApiParam( value = "The new value for the field", required = true ) @RequestBody String data) throws InvalidEntityException, OutOfDateException {
    	projectControls.editProjectWrapper(id, object, field, timestamp, data);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation( value = "Delete project", notes = "Deletes a project. This cannot be undone, unless the database is restored from backup" )
    @ResponseBody
    public void delete(@ApiParam( value = "Project id", required = true ) @PathVariable Integer id) {
        projectControls.delete(id);
    }
    
    @RequestMapping(value = "/ap", method = RequestMethod.PUT)
    @ApiOperation( value = "Add APLink", notes = "Add APLink (adviser) to project" )
    @ResponseBody
    public void addAPLink(@ApiParam( value = "APLink object", required = true ) @RequestBody APLink al) throws InvalidEntityException {
        projectControls.addAdviser(al);
    }
    
    @RequestMapping(value = "/rp", method = RequestMethod.PUT)
    @ApiOperation( value = "Add RPLink", notes = "Add RPLink (researcher) to project" )
    @ResponseBody
    public void addRPLink(@ApiParam( value = "RPLink object", required = true ) @RequestBody RPLink rl) throws InvalidEntityException {
        projectControls.addResearcher(rl);
    }
    
    @RequestMapping(value = "/kpi", method = RequestMethod.PUT)
    @ApiOperation( value = "Add Project KPI", notes = "Add KPI to project" )
    @ResponseBody
    public void addKpi(@ApiParam( value = "ProjectKpi object", required = true ) @RequestBody ProjectKpi pk) throws Exception {
        projectControls.addKpi(pk);
    }
    
    @RequestMapping(value = "/ro", method = RequestMethod.PUT)
    @ApiOperation( value = "Add Research Output", notes = "Add research output to project" )
    @ResponseBody
    public void addResearchOutput(@ApiParam( value = "ResearchOutput object", required = true ) @RequestBody ResearchOutput ro) throws Exception {
        projectControls.addResearchOutput(ro);
    }
    
    @RequestMapping(value = "/review", method = RequestMethod.PUT)
    @ApiOperation( value = "Add Review", notes = "Add review to project" )
    @ResponseBody
    public void addReview(@ApiParam( value = "Review object", required = true ) @RequestBody Review r) throws Exception {
        projectControls.addReview(r);
    }
    
    @RequestMapping(value = "/followup", method = RequestMethod.PUT)
    @ApiOperation( value = "Add Follow Up", notes = "Add followup to project" )
    @ResponseBody
    public void addFollowUp(@ApiParam( value = "FollowUp object", required = true ) @RequestBody FollowUp f) throws Exception {
        projectControls.addFollowUp(f);
    }
    
    @RequestMapping(value = "/prop", method = RequestMethod.PUT)
    @ApiOperation( value = "Upsert Project Property", notes = "Add or edit project property" )
    @ResponseBody
    public void upsertProperty(@ApiParam( value = "ProjectProperty object", required = true ) @RequestBody ProjectProperty p) throws Exception {
        projectControls.upsertProperty(p);
    }
    
    @RequestMapping(value = "/adviseraction", method = RequestMethod.PUT)
    @ApiOperation( value = "Add Adviser Action", notes = "Add adviser action to project" )
    @ResponseBody
    public void addAdviserAction(@ApiParam( value = "AdviserAction object", required = true ) @RequestBody AdviserAction aa) throws Exception {
        projectControls.addAdviserAction(aa);
    }
    
    @RequestMapping(value = "/attachment", method = RequestMethod.PUT)
    @ApiOperation( value = "Add Attachment", notes = "Add attachment to object" )
    @ResponseBody
    public void addAttachment(@ApiParam( value = "Attachment object", required = true ) @RequestBody Attachment a) throws Exception {
        projectControls.addAttachment(a);
    }
    
    @RequestMapping(value = "/{id}/{oid}/{type}", method = RequestMethod.DELETE)
    @ApiOperation( value = "Remove object link from project", notes = "Removes someone (Adviser or Researcher)/something (FollowUp etc) from a project. Possible object types: adviser, researcher, kpi, researchoutput, review, followup, adviseraction, property, Attachments_#" )
    @ResponseBody
    public void remove(@ApiParam( value = "Internal project id", required = true ) @PathVariable Integer id, @ApiParam( value = "Object id. Context specific - is either adviserId, researcherId or array index", required = true ) @PathVariable Integer oid, @ApiParam( value = "Object type. adviser, researcher, kpi etc", required = true ) @PathVariable String type) {
        projectControls.removeObjectLink(id, oid, type);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ApiOperation( value = "Create project", notes = "Creates a new project from the given object. Returns the new project id if successfull" )
    @ResponseBody
    public synchronized Integer createProjectWrapper(@ApiParam( value = "ProjectWrapper object. Note that not all parts (FollowUps etc) are required.", required = true ) @RequestBody ProjectWrapper pw) throws InvalidEntityException {
        return projectControls.createProjectWrapper(pw);
    }
    
    @RequestMapping(value = "/inst", method = RequestMethod.GET)
    @ApiOperation( value = "Get institutions", notes = "Returns a list of possible project institutions" )
    @ResponseBody
    public List<String> getInstitutions() throws Exception {
        return projectControls.getInstitutions();
    }
    
    @RequestMapping(value = "/sites", method = RequestMethod.GET)
    @ApiOperation( value = "Get sites", notes = "Returns a list of possible cluster sites" )
    @ResponseBody
    public List<Site> getSites() throws Exception {
        return projectControls.getSites();
    }
    
    @RequestMapping(value = "/fac", method = RequestMethod.GET)
    @ApiOperation( value = "Get facilities", notes = "Returns a list of possible project facilities" )
    @ResponseBody
    public List<Facility> getFacilities() throws Exception {
        return projectControls.getFacilities();
    }

    @RequestMapping(value = "/kpis", method = RequestMethod.GET)
    @ApiOperation( value = "Get KPIS", notes = "Returns a list of possible project KPIs" )
    @ResponseBody
    public List<Kpi> getKpis() throws Exception {
        return projectControls.getKpis();
    }
    
    @RequestMapping(value = "/kpic", method = RequestMethod.GET)
    @ApiOperation( value = "Get KPI Codes", notes = "Returns a list of possible project KPI codes" )
    @ResponseBody
    public List<KpiCode> getKpiCodes() throws Exception {
        return projectControls.getKpiCodes();
    }
    
    @RequestMapping(value = "/akpis", method = RequestMethod.GET)
    @ApiOperation( value = "Get Project KPIS", notes = "Returns a list of all reported KPIs" )
    @ResponseBody
    public List<ProjectKpi> getProjectKpis() throws Exception {
        return projectControls.getProjectKpis();
    }
    
    @RequestMapping(value = "/aro", method = RequestMethod.GET)
    @ApiOperation( value = "Get Research Outputs", notes = "Returns a list of reported Research Outputs" )
    @ResponseBody
    public List<ResearchOutput> getResearchOutput() throws Exception {
        return projectControls.getResearchOutput();
    }
    
    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    @ApiOperation( value = "Get Project Statuses", notes = "Returns a list of possible project statuses" )
    @ResponseBody
    public List<ProjectStatus> getProjectStatuses() throws Exception {
        return projectControls.getProjectStatuses();
    }
    
    @RequestMapping(value = "/type", method = RequestMethod.GET)
    @ApiOperation( value = "Get Project Types", notes = "Returns a list of possible project types" )
    @ResponseBody
    public List<ProjectType> getProjectTypes() throws Exception {
        return projectControls.getProjectTypes();
    }
    
    @RequestMapping(value = "/rotype", method = RequestMethod.GET)
    @ApiOperation( value = "Get Research Output Types", notes = "Returns a list of possible research output types" )
    @ResponseBody
    public List<ResearchOutputType> getROTypes() throws Exception {
        return projectControls.getROTypes();
    }
}
