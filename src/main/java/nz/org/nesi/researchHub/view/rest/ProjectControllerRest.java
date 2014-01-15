package nz.org.nesi.researchHub.view.rest;

import java.util.List;

import nz.org.nesi.researchHub.control.ProjectControls;
import nz.org.nesi.researchHub.exceptions.InvalidEntityException;
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
import pm.pojo.Facility;
import pm.pojo.FollowUp;
import pm.pojo.Kpi;
import pm.pojo.KpiCode;
import pm.pojo.Project;
import pm.pojo.ProjectKpi;
import pm.pojo.ProjectStatus;
import pm.pojo.ProjectType;
import pm.pojo.ProjectWrapper;
import pm.pojo.RPLink;
import pm.pojo.ResearchOutput;
import pm.pojo.Review;

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
    public ProjectWrapper getProjectWrapper(@PathVariable String projectIdOrCode) {
        return projectControls.getProjectWrapper(projectIdOrCode);
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
    public List<Project> filterProjects(@PathVariable String filter) {
        return projectControls.filterProjects(filter);
    }

//    DON'T IMPLEMENT EDIT PROJECT YET, EXPOSING THE PROJECTWRAPPER OBJECT IS A TAD
//    UGLY, LET'S THINK OF A BETTER WAY...
//    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
//    @ResponseBody
//    public void editProjectWrapper(@PathVariable Integer id, ProjectWrapper project) throws InvalidEntityException {
//        projectControls.editProjectWrapper(id, project);
//    }
    

    @RequestMapping(value = "/{id}/{object}/{field}/{timestamp}", method = RequestMethod.POST)
    @ApiOperation( value = "Edit field", notes = "Edits a single field in a project wrapper" )
    @ResponseBody
    public void editProjectWrapper(@PathVariable Integer id, @PathVariable String object, @PathVariable String field, @PathVariable String timestamp, @RequestBody String data) throws InvalidEntityException, OutOfDateException {
    	projectControls.editProjectWrapper(id, object, field, timestamp, data);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation( value = "Delete project", notes = "Deletes a project. This cannot be undone, unless the db is restored from backup" )
    @ResponseBody
    public void delete(@PathVariable Integer id) {
        projectControls.delete(id);
    }
    
    @RequestMapping(value = "/ap", method = RequestMethod.PUT)
    @ApiOperation( value = "Add APLink", notes = "Add APLink (adviser) to project" )
    @ResponseBody
    public void addAPLink(@RequestBody APLink al) {
        projectControls.addAdviser(al);
    }
    
    @RequestMapping(value = "/rp", method = RequestMethod.PUT)
    @ApiOperation( value = "Add RPLink", notes = "Add RPLink (researcher) to project" )
    @ResponseBody
    public void addRPLink(@RequestBody RPLink rl) {
        projectControls.addResearcher(rl);
    }
    
    @RequestMapping(value = "/kpi", method = RequestMethod.PUT)
    @ApiOperation( value = "Add project kpi", notes = "Add KPI to project" )
    @ResponseBody
    public void addKpi(@RequestBody ProjectKpi pk) {
        projectControls.addKpi(pk);
    }
    
    @RequestMapping(value = "/ro", method = RequestMethod.PUT)
    @ApiOperation( value = "Add Research Output", notes = "Add research output to project" )
    @ResponseBody
    public void addResearchOutput(@RequestBody ResearchOutput ro) {
        projectControls.addResearchOutput(ro);
    }
    
    @RequestMapping(value = "/review", method = RequestMethod.PUT)
    @ApiOperation( value = "Add Review", notes = "Add review to project" )
    @ResponseBody
    public void addReview(@RequestBody Review r) {
        projectControls.addReview(r);
    }
    
    @RequestMapping(value = "/followup", method = RequestMethod.PUT)
    @ApiOperation( value = "Add Follow Up", notes = "Add followup to project" )
    @ResponseBody
    public void addFollowUp(@RequestBody FollowUp f) {
        projectControls.addFollowUp(f);
    }
    
    @RequestMapping(value = "/adviseraction", method = RequestMethod.PUT)
    @ApiOperation( value = "Add Adviser Action", notes = "Add adviser action to project" )
    @ResponseBody
    public void addReview(@RequestBody AdviserAction aa) {
        projectControls.addAdviserAction(aa);
    }
    
    @RequestMapping(value = "/{id}/{oid}/{type}", method = RequestMethod.DELETE)
    @ApiOperation( value = "Remove object link from project", notes = "Removes someone/something from a project" )
    @ResponseBody
    public void remove(@ApiParam( value = "Internal project id", required = true ) @PathVariable Integer id, @ApiParam( value = "Object id. Context specific - is either adviserId, researcherId or array index", required = true ) @PathVariable Integer oid, @ApiParam( value = "Object type. adviser, researcher, kpi etc", required = true ) @PathVariable String type) {
        projectControls.removeObjectLink(id, oid, type);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ApiOperation( value = "Create project", notes = "Creates a new project from the given object" )
    @ResponseBody
    public synchronized Integer createProjectWrapper(ProjectWrapper pw) throws InvalidEntityException {
        return projectControls.createProjectWrapper(pw);
    }
    
    @RequestMapping(value = "/inst", method = RequestMethod.GET)
    @ApiOperation( value = "Get institutions", notes = "Returns a list of possible project institutions" )
    @ResponseBody
    public List<String> getInstitutions() throws Exception {
        return projectControls.getInstitutions();
    }
    
    @RequestMapping(value = "/fac", method = RequestMethod.GET)
    @ApiOperation( value = "Get facilities", notes = "Returns a list of possible project facilities" )
    @ResponseBody
    public List<Facility> getFacilities() throws Exception {
        return projectControls.getFacilities();
    }

    @RequestMapping(value = "/kpis", method = RequestMethod.GET)
    @ApiOperation( value = "Get KPIS", notes = "Returns a list of possible project KPIS" )
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
    @ApiOperation( value = "Get Project KPIS", notes = "Returns a list of reported KPIs" )
    @ResponseBody
    public List<ProjectKpi> getProjectKpis() throws Exception {
        return projectControls.getProjectKpis();
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
}
