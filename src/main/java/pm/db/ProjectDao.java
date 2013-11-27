package pm.db;

import java.util.List;

import pm.pojo.Adviser;
import pm.pojo.AdviserRole;
import pm.pojo.Affiliation;
import pm.pojo.Facility;
import pm.pojo.InstitutionalRole;
import pm.pojo.Kpi;
import pm.pojo.KpiCode;
import pm.pojo.Project;
import pm.pojo.ProjectKpi;
import pm.pojo.ProjectStatus;
import pm.pojo.ProjectType;
import pm.pojo.ProjectWrapper;
import pm.pojo.ResearchOutput;
import pm.pojo.ResearchOutputType;
import pm.pojo.Researcher;
import pm.pojo.ResearcherRole;
import pm.pojo.ResearcherStatus;
import pm.pojo.Site;

public interface ProjectDao {

	public ProjectWrapper getProjectWrapperById(Integer id) throws Exception;
	public Researcher getResearcherById(Integer id) throws Exception;
	public Adviser getAdviserById(Integer id) throws Exception;
	public Adviser getAdviserByTuakiriUniqueId(String id) throws Exception;
	public List<Researcher> getResearchers() throws Exception;
	public List<ResearchOutputType> getResearchOutputTypes() throws Exception;
	public List<Affiliation> getAffiliations() throws Exception;
	public List<String> getInstitutions() throws Exception;
	public List<Site> getSites() throws Exception;
	public List<Kpi> getKpis() throws Exception;
	public List<ProjectKpi> getProjectKpis() throws Exception;
	public List<ResearchOutput> getResearchOutput() throws Exception;
	public List<ProjectType> getProjectTypes() throws Exception;
	public List<Project> getProjects() throws Exception;
	public List<Adviser> getAdvisers() throws Exception;
	public List<Facility> getFacilities() throws Exception;
	public List<Researcher> getResearchersOnProject(Integer projectId) throws Exception;
	public List<Adviser> getAdvisersOnProject(Integer projectId) throws Exception;
	public List<Researcher> getResearchersNotOnList(List<Integer> l) throws Exception;
	public List<Adviser> getAdvisersNotOnList(List<Integer> l) throws Exception;
	public List<Facility> getFacilitiesNotOnList(List<Integer> l) throws Exception;
	public List<ResearcherRole> getResearcherRoles() throws Exception;
	public List<AdviserRole> getAdviserRoles() throws Exception;
	public List<InstitutionalRole> getInstitutionalRoles() throws Exception;
    public List<Project> getProjectsForResearcherId(Integer id) throws Exception;
    public List<Project> getProjectsForAdviserId(Integer id) throws Exception;
    public Integer getNumProjectsForAdviser(Integer adviserId) throws Exception;
    public AdviserRole getAdviserRoleById(Integer id) throws Exception;
    public ResearcherRole getResearcherRoleById(Integer id) throws Exception;
    public Kpi getKpiById(Integer id) throws Exception;
	public ResearchOutputType getResearchOutputTypeById(Integer id) throws Exception;
	public Facility getFacilityById(Integer id) throws Exception;
    
	public Integer createProjectWrapper(ProjectWrapper pw) throws Exception;
	public Integer createResearcher(Researcher r) throws Exception;
	public Integer createAdviser(Adviser a) throws Exception;
	
	public void updateProjectWrapper(int projectId, ProjectWrapper pw) throws Exception;
	public void updateResearcher(Researcher r) throws Exception;
	public void updateAdviser(Adviser a) throws Exception;
	
	public void deleteProjectWrapper(Integer projectId) throws Exception;
	public void deleteResearcher(Integer id) throws Exception;
	public void deleteAdviser(Integer id) throws Exception;
	
	public String getNextProjectCode(String hostInstitution);
	public List<KpiCode> getKpiCodes();
	public String getKpiCodeNameById(Integer codeId);
	
	public List<ProjectStatus> getProjectStatuses();
	public String getProjectStatusById(Integer id);
	public List<ResearcherStatus> getResearcherStatuses();
	public String getResearcherStatusById(Integer id);
	public String getLinuxUsername(Integer id);
	
}
