package pm.db;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pm.pojo.Adviser;
import pm.pojo.AdviserRole;
import pm.pojo.Affiliation;
import pm.pojo.Change;
import pm.pojo.Facility;
import pm.pojo.FollowUp;
import pm.pojo.InstitutionalRole;
import pm.pojo.Kpi;
import pm.pojo.KpiCode;
import pm.pojo.Project;
import pm.pojo.ProjectAllocation;
import pm.pojo.ProjectKpi;
import pm.pojo.ProjectProperty;
import pm.pojo.ProjectStatus;
import pm.pojo.ProjectType;
import pm.pojo.ProjectWrapper;
import pm.pojo.ResearchOutput;
import pm.pojo.ResearchOutputType;
import pm.pojo.Researcher;
import pm.pojo.ResearcherProperty;
import pm.pojo.ResearcherRole;
import pm.pojo.ResearcherStatus;
import pm.pojo.Site;

public interface ProjectDao {

	public Integer createAdviser(Adviser a) throws Exception;

	public void createDepartment(Affiliation af) throws Exception;

	public void createDivision(Affiliation af) throws Exception;

	public void createInstitution(Affiliation af) throws Exception;

	public Integer createProjectWrapper(ProjectWrapper pw) throws Exception;

	public Integer createResearcher(Researcher r) throws Exception;

	public void deleteAdviser(Integer id) throws Exception;

	public void deleteProjectProperty(Integer id);

	public void deleteProjectWrapper(Integer projectId) throws Exception;

	public void deleteResearcher(Integer id) throws Exception;

	public void deleteResearcherProperty(Integer id);

	public Adviser getAdviserByDrupalId(String id) throws Exception;

	public Adviser getAdviserById(Integer id) throws Exception;

	Adviser getAdviserByTuakiriSharedToken(String id) throws Exception;

	public Adviser getAdviserByTuakiriUniqueId(String id) throws Exception;

	public AdviserRole getAdviserRoleById(Integer id) throws Exception;

	public List<AdviserRole> getAdviserRoles() throws Exception;

	public List<Adviser> getAdvisers() throws Exception;

	public List<Adviser> getAdvisersNotOnList(List<Integer> l) throws Exception;

	public List<Adviser> getAdvisersOnProject(Integer projectId)
			throws Exception;

	public List<Affiliation> getAffiliations() throws Exception;

	public List<Affiliation> getAffiliationsByDepartmentCode(
			String departmentCode) throws Exception;

	public List<Affiliation> getAffiliationsByDivisionCode(String divisionCode)
			throws Exception;

	public List<Affiliation> getAffiliationsByInstitutionCode(
			String institutionCode) throws Exception;

	public Map<String, Map<String, Set<String>>> getAllProjectsAndMembers()
			throws Exception;

	List<Change> getChangeLogForTable(String table) throws Exception;

	public String getDrupalIdByAdviserId(Integer id) throws Exception;

	public List<Facility> getFacilities() throws Exception;

	public List<Facility> getFacilitiesNotOnList(List<Integer> l)
			throws Exception;

	public Facility getFacilityById(Integer id) throws Exception;

	public List<InstitutionalRole> getInstitutionalRoles() throws Exception;

	public List<String> getInstitutions() throws Exception;

	public Kpi getKpiById(Integer id) throws Exception;

	public String getKpiCodeNameById(Integer codeId);

	public List<KpiCode> getKpiCodes();

	public List<Kpi> getKpis() throws Exception;

	public String getLastModifiedForTable(final String table);

	public String getNextProjectCode(String hostInstitution);

	public Integer getNumProjectsForAdviser(Integer adviserId) throws Exception;

	public List<Integer> getProjectIds() throws Exception;

	public List<ProjectKpi> getProjectKpis() throws Exception;

	public List<ProjectProperty> getProjectProperties(Integer id)
			throws Exception;

	public ProjectProperty getProjectProperty(Integer id);

	public List<Project> getProjects() throws Exception;

	public List<Project> getProjectsForAdviserId(Integer id) throws Exception;

	public List<Project> getProjectsForResearcherId(Integer id)
			throws Exception;

	public String getProjectStatusById(Integer id);

	public List<ProjectStatus> getProjectStatuses();

	public List<ProjectType> getProjectTypes() throws Exception;

	// public List<RPLink> getRPLinksForProject(final Integer pid)
	// throws Exception;

	public ProjectWrapper getProjectWrapperById(Integer id) throws Exception;

	public ProjectWrapper getProjectWrapperByProjectCode(String projectCode)
			throws Exception;

	public List<String> getPropnames();

	public Researcher getResearcherById(Integer id) throws Exception;

	public List<ResearcherProperty> getResearcherProperties(Integer id)
			throws Exception;

	ResearcherProperty getResearcherProperty(Integer id);

	public ResearcherRole getResearcherRoleById(Integer id) throws Exception;

	public List<ResearcherRole> getResearcherRoles() throws Exception;

	public List<Researcher> getResearchers() throws Exception;

	public List<Researcher> getResearchersNotOnList(List<Integer> l)
			throws Exception;

	public List<Researcher> getResearchersOnProject(Integer projectId)
			throws Exception;

	public String getResearcherStatusById(Integer id);

	public List<ResearcherStatus> getResearcherStatuses();

	public List<Researcher> getResearchersWhere(String field, Object data);

	public List<ResearchOutput> getResearchOutput() throws Exception;

	public ResearchOutputType getResearchOutputTypeById(Integer id)
			throws Exception;

	public List<ResearchOutputType> getResearchOutputTypes() throws Exception;

	public List<Site> getSites() throws Exception;

	void logChange(Change c) throws Exception;

	public void updateAdviser(Adviser a);

	public void updateProjectWrapper(int projectId, ProjectWrapper pw)
			throws Exception;

	public void updateResearcher(Researcher r);

	public void upsertFollowUp(FollowUp f);

	public void upsertProjectProperty(ProjectProperty p);

	public void upsertResearcherProperty(ResearcherProperty r);

	public void upsertResearchOutput(ResearchOutput ro);

	public void createProjectAllocation(Integer pid);

	public List<ProjectAllocation> getProjectAllocations();

	public ProjectAllocation getProjectAllocationByProjectCode(
			String projectCode) throws Exception;

}
