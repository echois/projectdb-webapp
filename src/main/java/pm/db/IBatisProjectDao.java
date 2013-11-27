package pm.db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import pm.authz.annotation.RequireAdmin;
import pm.authz.annotation.RequireAdviser;
import pm.authz.annotation.RequireAdviserOnProject;
import pm.pojo.APLink;
import pm.pojo.Adviser;
import pm.pojo.AdviserAction;
import pm.pojo.AdviserRole;
import pm.pojo.Affiliation;
import pm.pojo.Attachment;
import pm.pojo.Facility;
import pm.pojo.FollowUp;
import pm.pojo.InstitutionalRole;
import pm.pojo.Kpi;
import pm.pojo.KpiCode;
import pm.pojo.Project;
import pm.pojo.ProjectFacility;
import pm.pojo.ProjectKpi;
import pm.pojo.ProjectStatus;
import pm.pojo.ProjectType;
import pm.pojo.ProjectWrapper;
import pm.pojo.RPLink;
import pm.pojo.ResearchOutput;
import pm.pojo.ResearchOutputType;
import pm.pojo.Researcher;
import pm.pojo.ResearcherRole;
import pm.pojo.ResearcherStatus;
import pm.pojo.Review;
import pm.pojo.Site;

public class IBatisProjectDao extends SqlMapClientDaoSupport implements ProjectDao {

    @RequireAdviser
	public synchronized Integer createProjectWrapper(final ProjectWrapper pw) throws Exception {
    	Integer pid = this.createProject(pw.getProject());
    	List<RPLink> rpLinks = pw.getRpLinks();
    	List<APLink> apLinks = pw.getApLinks();
    	List<ResearchOutput> ros = pw.getResearchOutputs();
    	List<ProjectKpi> kpis = pw.getProjectKpis();
    	List<Review> reviews = pw.getReviews();
    	List<FollowUp> fus = pw.getFollowUps();
    	List<AdviserAction> aas = pw.getAdviserActions();
    	List<ProjectFacility> pfs = pw.getProjectFacilities();
    	
		for (RPLink l : rpLinks) {
			l.setProjectId(pid);
			this.createRPLink(l);
		}
		for (APLink l : apLinks) {
			l.setProjectId(pid);
			this.createAPLink(l);
		}
		for (ResearchOutput ro : ros) {
			ro.setProjectId(pid);
			this.createResearchOutput(ro);
		}
		for (ProjectKpi pk : kpis) {
			pk.setProjectId(pid);
			this.createProjectKpi(pk);
		}
		for (Review r : reviews) {
			r.setProjectId(pid);
			this.createReview(r);
		}
		for (FollowUp fu : fus) {
			fu.setProjectId(pid);
			this.createFollowUp(fu);
		}
		for (AdviserAction aa : aas) {
			aa.setProjectId(pid);
			this.createAdviserAction(aa);
		}
		for (ProjectFacility pf : pfs) {
			pf.setProjectId(pid);
			this.createProjectFacility(pf);
		}
    	return pid;
	}

    // TODO: ugly implementation. Fix it
    @RequireAdviserOnProject
	public synchronized void updateProjectWrapper(int projectId, ProjectWrapper pw) throws Exception {
    	Integer pid = pw.getProject().getId();
    	this.updateProject(pid, pw.getProject());
    	
    	List<RPLink> rpLinks = pw.getRpLinks();
    	List<APLink> apLinks = pw.getApLinks();
    	List<ResearchOutput> ros = pw.getResearchOutputs();
    	List<ProjectKpi> kpis = pw.getProjectKpis();
    	List<Review> reviews = pw.getReviews();
    	List<FollowUp> fus = pw.getFollowUps();
    	List<AdviserAction> aas = pw.getAdviserActions();
    	List<ProjectFacility> pfs = pw.getProjectFacilities();

		this.deleteRPLinks(pid);
		for (RPLink rpLink : rpLinks) {
			this.createRPLink(rpLink);
		}

		this.deleteAPLinks(pid);
		for (APLink apLink : apLinks) {
			this.createAPLink(apLink);
		}

		this.deleteResearchOutputs(pid);
		for (ResearchOutput ro : ros) {
			this.createResearchOutput(ro);
		}
    	
    	this.deleteProjectKpis(pid);
		for (ProjectKpi pk : kpis) {
			this.createProjectKpi(pk);
		}
    	
    	this.deleteReviews(pid);
		for (Review r : reviews) {
			this.createReview(r);
		}
    	
    	this.deleteFollowUps(pid);
		for (FollowUp fu : fus) {
			this.createFollowUp(fu);
		}
    	
    	this.deleteAdviserActions(pid);
		for (AdviserAction aa : aas) {
			this.createAdviserAction(aa);
		}
		
		this.deleteProjectFacilities(pid);
		for (ProjectFacility pf: pfs) {
			this.createProjectFacility(pf);
		}
	}

    @RequireAdviser
	public Integer createResearcher(final Researcher r) throws Exception {
		return (Integer) getSqlMapClientTemplate().insert("createResearcher", r);
	}

    @RequireAdviser
	public Integer createAdviser(final Adviser a) throws Exception {
		return (Integer) getSqlMapClientTemplate().insert("createAdviser", a);
	}

	public List<Project> getProjects() throws Exception {
		List<Project> ps = (List<Project>) getSqlMapClientTemplate().queryForList("getProjects");
		for (Project p: ps) {
			ProjectType t = (ProjectType) getSqlMapClientTemplate().queryForObject("getProjectTypeById", p.getProjectTypeId());
			p.setProjectTypeName(t.getName());
			p.setStatusName(getProjectStatusById(p.getStatusId()));
		}
		return ps;
	}

	public List<Researcher> getResearchers() throws Exception {
		List<Researcher> l = (List<Researcher>) getSqlMapClientTemplate().queryForList("getResearchers");
		if (l != null) {
			for (Researcher r: l) {
				InstitutionalRole ir = (InstitutionalRole) getSqlMapClientTemplate().queryForObject("getInstitutionalRoleById", r.getInstitutionalRoleId());
				r.setInstitutionalRoleName(ir.getName());
				r.setStatusName(getResearcherStatusById(r.getStatusId()));
			}
		}
		return l;
	}
	
	public List<Adviser> getAdvisers() throws Exception {
		List<Adviser> list = (List<Adviser>) getSqlMapClientTemplate().queryForList("getAdvisers");
		for (Adviser a: list) {
			a.setNumProjects(this.getNumProjectsForAdviser(a.getId()));
		}
		return list;
	}

	public synchronized ProjectWrapper getProjectWrapperById(Integer projectId) throws Exception {
		ProjectWrapper pw = new ProjectWrapper();
		pw.setProject(this.getProjectById(projectId));
		pw.setRpLinks(this.getRPLinksForProject(projectId));
		pw.setApLinks(this.getAPLinksForProject(projectId));
		pw.setResearchOutputs(this.getResearchOutputsForProjectId(projectId));
		pw.setProjectKpis(this.getKpisForProjectId(projectId));
		pw.setReviews(this.getReviewsForProjectId(projectId));
		pw.setFollowUps(this.getFollowUpsForProjectId(projectId));
		pw.setAdviserActions(this.getAdviserActionsForProjectId(projectId));
		pw.setProjectFacilities(this.getFacilitiesOnProject(projectId));
		return pw;
	}

	@RequireAdviserOnProject
	public synchronized void deleteProjectWrapper(Integer projectId) throws Exception {
		getSqlMapClientTemplate().update("deleteProject", projectId);
	}

	public Project getProjectById(final Integer id) throws Exception {
		Project p = (Project) getSqlMapClientTemplate().queryForObject("getProjectById", id);
		ProjectType t = (ProjectType) getSqlMapClientTemplate().queryForObject("getProjectTypeById", p.getProjectTypeId());
		p.setProjectTypeName(t.getName());
		p.setStatusName(getProjectStatusById(p.getStatusId()));
		return p;
	}

	public Researcher getResearcherById(final Integer id) throws Exception {
		Researcher r = (Researcher) getSqlMapClientTemplate().queryForObject("getResearcherById", id);
		InstitutionalRole ir = (InstitutionalRole) getSqlMapClientTemplate().queryForObject("getInstitutionalRoleById", r.getInstitutionalRoleId());
		r.setInstitutionalRoleName(ir.getName());
		r.setStatusName(getResearcherStatusById(r.getStatusId()));
		return r;
	}

	public Adviser getAdviserById(final Integer id) throws Exception {
		Adviser a = (Adviser) getSqlMapClientTemplate().queryForObject("getAdviserById", id);
		a.setNumProjects(this.getNumProjectsForAdviser(a.getId()));
		return a;
	}

	public Adviser getAdviserByTuakiriUniqueId(final String id) throws Exception {
		Adviser a = (Adviser) getSqlMapClientTemplate().queryForObject("getAdviserByTuakiriUniqueId", id);
		return a;
	}

	public Integer getNumProjectsForAdviser(Integer adviserId) throws Exception {
		return (Integer) getSqlMapClientTemplate().queryForObject("getNumProjectsForAdviser", adviserId);		
	}
	
	public List<ResearchOutputType> getResearchOutputTypes() throws Exception {
		return (List<ResearchOutputType>) getSqlMapClientTemplate().queryForList("getResearchOutputTypes");	
	}

	public ResearchOutputType getResearchOutputTypeById(Integer id) throws Exception {
		return (ResearchOutputType) getSqlMapClientTemplate().queryForObject("getResearchOutputTypeById", id);
	}

	public List<Site> getSites() throws Exception {
		return (List<Site>) getSqlMapClientTemplate().queryForList("getSites");
	}

	public List<Affiliation> getAffiliations() throws Exception {
		return (List<Affiliation>) getSqlMapClientTemplate().queryForList("getAffiliations");		
	}

	public List<String> getInstitutions() throws Exception {
		return (List<String>) getSqlMapClientTemplate().queryForList("getInstitutions");		
	}

	public List<Kpi> getKpis() throws Exception {
		return (List<Kpi>) getSqlMapClientTemplate().queryForList("getKpis");
	}
	
	public List<ProjectKpi> getProjectKpis() throws Exception {
		List<ProjectKpi> l = getSqlMapClientTemplate().queryForList("getProjectKpis");
		for (ProjectKpi pk: l) {
			Kpi kpi = (Kpi) getSqlMapClientTemplate().queryForObject("getKpiById", pk.getKpiId());
			Adviser tmp = (Adviser) getSqlMapClientTemplate().queryForObject("getAdviserById", pk.getAdviserId());
			if (tmp != null) {
				pk.setAdviserName(tmp.getFullName());				
			}
			pk.setKpiType(kpi.getType());
			pk.setKpiTitle(kpi.getTitle());
			pk.setCodeName(getKpiCodeNameById(pk.getCode()));
		}
		return l;
	}
	
	public List<ResearchOutput> getResearchOutput() throws Exception {
		List<ResearchOutput> l = (List<ResearchOutput>) getSqlMapClientTemplate().queryForList("getResearchOutput");
		for (ResearchOutput ro: l) {
			Adviser a = (Adviser) getSqlMapClientTemplate().queryForObject("getAdviserById", ro.getAdviserId());
			ResearchOutputType tmp = (ResearchOutputType) getSqlMapClientTemplate().queryForObject("getResearchOutputTypeById", ro.getTypeId());
			ro.setType(tmp.getName());
			ro.setAdviserName(a.getFullName());
		}
		return l;
	}

	public List<ProjectType> getProjectTypes() throws Exception {
		return (List<ProjectType>) getSqlMapClientTemplate().queryForList("getProjectTypes");
	}

	public List<Researcher> getResearchersOnProject(Integer pid) throws Exception {
		List<Researcher> l = (List<Researcher>) getSqlMapClientTemplate().queryForList("getResearchersOnProject", pid);		
		if (l != null) {
			for (Researcher r: l) {
				InstitutionalRole ir = (InstitutionalRole) getSqlMapClientTemplate().queryForObject("getInstitutionalRoleById", r.getInstitutionalRoleId());
				r.setInstitutionalRoleName(ir.getName());
				r.setStatusName(getResearcherStatusById(r.getStatusId()));
			}
		}
		return l;
	}

	public List<Adviser> getAdvisersOnProject(Integer pid) throws Exception {
		List<Adviser> list = (List<Adviser>) getSqlMapClientTemplate().queryForList("getAdvisersOnProject", pid);		
		for (Adviser a: list) {
			a.setNumProjects(this.getNumProjectsForAdviser(a.getId()));
		}
		return list;
	}

	public Facility getFacilityById(Integer id) throws Exception {
		return (Facility) getSqlMapClientTemplate().queryForObject("getFacilityById", id);		
	}

	public List<Facility> getFacilities() throws Exception {
		return (List<Facility>) getSqlMapClientTemplate().queryForList("getFacilities");		
	}

	public List<ProjectFacility> getFacilitiesOnProject(Integer pid) throws Exception {
		List<Facility> fs = (List<Facility>) getSqlMapClientTemplate().queryForList("getFacilitiesOnProject", pid);		
	    List<ProjectFacility> pfs = new LinkedList<ProjectFacility>();
	    for (Facility f: fs) {
	    	ProjectFacility pf = new ProjectFacility();
	    	pf.setProjectId(pid);
	    	pf.setFacilityId(f.getId());
	    	pf.setFacilityName(f.getName());
	    	pfs.add(pf);
	    }
	    return pfs;
	}

	public List<Researcher> getResearchersNotOnList(List<Integer> l) throws Exception {
		List<Researcher> tmp = null;
		if (l.size() == 0) {
			tmp = (List<Researcher>) getSqlMapClientTemplate().queryForList("getResearchers");
		} else {
			tmp = (List<Researcher>) getSqlMapClientTemplate().queryForList("getResearchersNotOnList", l);			
		}
		if (tmp != null) {
			for (Researcher r: tmp) {
				InstitutionalRole ir = (InstitutionalRole) getSqlMapClientTemplate().queryForObject("getInstitutionalRoleById", r.getInstitutionalRoleId());
				r.setInstitutionalRoleName(ir.getName());
				r.setStatusName(getResearcherStatusById(r.getStatusId()));
			}
		}
		return tmp;
	}

	public List<Adviser> getAdvisersNotOnList(List<Integer> l) throws Exception {
		List<Adviser> tmp = null;
		if (l.size() == 0) {
			tmp = (List<Adviser>) getSqlMapClientTemplate().queryForList("getAdvisers");
		} else {
			tmp = (List<Adviser>) getSqlMapClientTemplate().queryForList("getAdvisersNotOnList", l);
		}
		return tmp;
	}

	public List<Facility> getFacilitiesNotOnList(List<Integer> l) throws Exception {
		List<Facility> tmp = null;
		if (l.size() == 0) {
			tmp = (List<Facility>) getSqlMapClientTemplate().queryForList("getFacilities");
		} else {
			tmp = (List<Facility>) getSqlMapClientTemplate().queryForList("getFacilitiesNotOnList", l);
		}
		return tmp;
	}

	public List<ResearcherRole> getResearcherRoles() throws Exception {
		return (List<ResearcherRole>) getSqlMapClientTemplate().queryForList("getResearcherRoles");		
	}

	public List<AdviserRole> getAdviserRoles() throws Exception {
		return (List<AdviserRole>) getSqlMapClientTemplate().queryForList("getAdviserRoles");		
	}

	public List<InstitutionalRole> getInstitutionalRoles() throws Exception {
		return (List<InstitutionalRole>) getSqlMapClientTemplate().queryForList("getInstitutionalRoles");		
	}

	public InstitutionalRole getInstitutionalRoleById(final Integer id) throws Exception {
		return (InstitutionalRole) getSqlMapClientTemplate().queryForObject("getInstitutionalRoleById", id);
	}

    @RequireAdviser
	public void updateResearcher(final Researcher r) throws Exception {
		getSqlMapClientTemplate().update("updateResearcher", r);
	}

    @RequireAdviser
	public void updateAdviser(final Adviser a) throws Exception {
		getSqlMapClientTemplate().update("updateAdviser", a);
	}
 
    @RequireAdmin
	public void deleteResearcher(final Integer id) throws Exception {
		getSqlMapClientTemplate().update("deleteResearcher", id);
	}

    @RequireAdmin
	public void deleteAdviser(final Integer id) throws Exception {
		getSqlMapClientTemplate().update("deleteAdviser", id);
	}

	public Kpi getKpiById(Integer id) throws Exception {
		return (Kpi) getSqlMapClientTemplate().queryForObject("getKpiById", id);	
	}

    public List<Project> getProjectsForResearcherId(Integer id) throws Exception {
    	List<Project> ps = getSqlMapClientTemplate().queryForList("getProjectsForResearcherId", id);
		for (Project p: ps) {
			ProjectType t = (ProjectType) getSqlMapClientTemplate().queryForObject("getProjectTypeById", p.getProjectTypeId());
			p.setProjectTypeName(t.getName());
			p.setStatusName(getProjectStatusById(p.getStatusId()));
		}
		return ps;
    }

    public List<Project> getProjectsForAdviserId(Integer id) throws Exception {
    	List<Project> ps = getSqlMapClientTemplate().queryForList("getProjectsForAdviserId", id);
		for (Project p: ps) {
			ProjectType t = (ProjectType) getSqlMapClientTemplate().queryForObject("getProjectTypeById", p.getProjectTypeId());
			p.setProjectTypeName(t.getName());
			p.setStatusName(getProjectStatusById(p.getStatusId()));
		}
		return ps;
    }
	
    public AdviserRole getAdviserRoleById(Integer id) throws Exception {
    	return (AdviserRole) getSqlMapClientTemplate().queryForObject("getAdviserRoleById", id);
    }

    public ResearcherRole getResearcherRoleById(Integer id) throws Exception {
    	return (ResearcherRole) getSqlMapClientTemplate().queryForObject("getResearcherRoleById", id);
    }
    
	private List<RPLink> getRPLinksForProject(Integer pid) throws Exception {
		List<RPLink> l = (List<RPLink>) getSqlMapClientTemplate().queryForList("getRPLinksForProjectId", pid);
		if (l != null) {
			for (RPLink rp: l) {
				rp.setResearcher(this.getResearcherById(rp.getResearcherId()));
				ResearcherRole rr = (ResearcherRole) getSqlMapClientTemplate().queryForObject("getResearcherRoleById", rp.getResearcherRoleId());
				rp.setResearcherRoleName(rr.getName());
			}
		}
		return l;
	}

	private List<APLink> getAPLinksForProject(Integer pid) throws Exception {
		List<APLink> l = (List<APLink>) getSqlMapClientTemplate().queryForList("getAPLinksForProjectId", pid);
		if (l != null) {
			for (APLink ap: l) {
				ap.setAdviser(this.getAdviserById(ap.getAdviserId()));
				AdviserRole ar = (AdviserRole) getSqlMapClientTemplate().queryForObject("getAdviserRoleById", ap.getAdviserRoleId());
				ap.setAdviserRoleName(ar.getName());
			}
		}
		return l;
	}

	private List<Review> getReviewsForProjectId(Integer id) throws Exception {
		List<Review> l = (List<Review>) getSqlMapClientTemplate().queryForList("getReviewsForProjectId", id);
		for (Review r: l) {
			Adviser tmp = (Adviser) getSqlMapClientTemplate().queryForObject("getAdviserById", r.getAdviserId());
			r.setAdviserName(tmp.getFullName());
			r.setAttachments(this.getAttachmentsForReviewId(r.getId()));
		}
		return l;
	}

	private List<FollowUp> getFollowUpsForProjectId(Integer id) throws Exception {
		List<FollowUp> l = (List<FollowUp>) getSqlMapClientTemplate().queryForList("getFollowUpsForProjectId", id);
		for (FollowUp f: l) {
			Adviser tmp = (Adviser) getSqlMapClientTemplate().queryForObject("getAdviserById", f.getAdviserId());
			f.setAdviserName(tmp.getFullName());
			f.setAttachments(this.getAttachmentsForFollowUpId(f.getId()));
		}
		return l;
	}

	
	private List<ResearchOutput> getResearchOutputsForProjectId(Integer id) throws Exception {
		List<ResearchOutput> l = (List<ResearchOutput>) getSqlMapClientTemplate().queryForList("getResearchOutputsForProjectId", id);
		for (ResearchOutput ro: l) {
			Adviser a = (Adviser) getSqlMapClientTemplate().queryForObject("getAdviserById", ro.getAdviserId());
			ResearchOutputType tmp = (ResearchOutputType) getSqlMapClientTemplate().queryForObject("getResearchOutputTypeById", ro.getTypeId());
			ro.setType(tmp.getName());
			ro.setAdviserName(a.getFullName());
		}
		return l;
	}

	private List<ProjectKpi> getKpisForProjectId(Integer id) throws Exception {
		List<ProjectKpi> l = (List<ProjectKpi>) getSqlMapClientTemplate().queryForList("getKpisForProjectId", id);
		for (ProjectKpi pk: l) {
			Adviser tmp = (Adviser) getSqlMapClientTemplate().queryForObject("getAdviserById", pk.getAdviserId());
			Kpi kpi = (Kpi) getSqlMapClientTemplate().queryForObject("getKpiById", pk.getKpiId());
			pk.setAdviserName(tmp.getFullName());
			pk.setKpiType(kpi.getType());
			pk.setKpiTitle(kpi.getTitle());
			pk.setCodeName(getKpiCodeNameById(pk.getCode()));
		}
		return l;
	}
	
	private List<AdviserAction> getAdviserActionsForProjectId(Integer id) throws Exception {
		List<AdviserAction> l = (List<AdviserAction>) getSqlMapClientTemplate().queryForList("getAdviserActionsForProjectId", id);
		for (AdviserAction aa: l) {
			Adviser tmp = (Adviser) getSqlMapClientTemplate().queryForObject("getAdviserById", aa.getAdviserId());
		    aa.setAdviserName(tmp.getFullName());
		    aa.setAttachments(this.getAttachmentsForAdviserActionId(aa.getId()));
		}
		return l;
	}

	private List<Attachment> getAttachmentsForReviewId(Integer rid) throws Exception {
		return (List<Attachment>) getSqlMapClientTemplate().queryForList("getAttachmentsForReviewId", rid);		
	}

	private List<Attachment> getAttachmentsForFollowUpId(Integer rid) throws Exception {
		return (List<Attachment>) getSqlMapClientTemplate().queryForList("getAttachmentsForFollowUpId", rid);		
	}

	private List<Attachment> getAttachmentsForAdviserActionId(Integer rid) throws Exception {
		return (List<Attachment>) getSqlMapClientTemplate().queryForList("getAttachmentsForAdviserActionId", rid);		
	}

	private Integer createProject(Project p) throws Exception {
		return (Integer) getSqlMapClientTemplate().insert("createProject", p);
	}

	private void createRPLink(RPLink r) throws Exception {
		getSqlMapClientTemplate().insert("createRPLink", r);
	}
	
    private void createAPLink(APLink a) throws Exception {
		getSqlMapClientTemplate().insert("createAPLink", a);
	}
	
	private Integer createReview(Review r) throws Exception {
		Integer rid = (Integer) getSqlMapClientTemplate().insert("createReview", r);
		List<Attachment> attachments = r.getAttachments();
		if ((r.getAttachmentDescription() != null && r.getAttachmentDescription() != "") ||
			(r.getAttachmentLink() != null && r.getAttachmentLink() != "")) {
			Attachment a = new Attachment();
			a.setDate(r.getDate());
			a.setDescription(r.getAttachmentDescription());
			a.setLink(r.getAttachmentLink());
			attachments.add(a);
		}
		for (Attachment a : attachments) {
			a.setProjectId(r.getProjectId());
			a.setReviewId(rid);
			this.createAttachment(a);
		}
		return rid;
	}
	
	private Integer createFollowUp(FollowUp f) throws Exception {
		Integer fid = (Integer) getSqlMapClientTemplate().insert("createFollowUp", f);
		List<Attachment> attachments = f.getAttachments();
		if ((f.getAttachmentDescription() != null && f.getAttachmentDescription() != "") ||
			(f.getAttachmentLink() != null && f.getAttachmentLink() != "")) {
			Attachment a = new Attachment();
			a.setDate(f.getDate());
			a.setDescription(f.getAttachmentDescription());
			a.setLink(f.getAttachmentLink());
			attachments.add(a);
		}
		for (Attachment a : attachments) {
			a.setProjectId(f.getProjectId());
			a.setFollowUpId(fid);
			this.createAttachment(a);
		}
		return fid;
	}
	
	private void createResearchOutput(ResearchOutput o) throws Exception {
		getSqlMapClientTemplate().insert("createResearchOutput", o);
	}
	
	private void createAttachment(Attachment a) throws Exception {
		getSqlMapClientTemplate().insert("createAttachment", a);
	}
	
	private Integer createAdviserAction(AdviserAction aa) throws Exception {
		Integer aaid = (Integer) getSqlMapClientTemplate().insert("createAdviserAction", aa);
		List<Attachment> attachments = aa.getAttachments();
		if ((aa.getAttachmentDescription() != null && aa.getAttachmentDescription() != "") ||
			(aa.getAttachmentLink() != null && aa.getAttachmentLink() != "")) {
			Attachment a = new Attachment();
			a.setDate(aa.getDate());
			a.setDescription(aa.getAttachmentDescription());
			a.setLink(aa.getAttachmentLink());
		    attachments.add(a);
		}
		for (Attachment a : attachments) {
			a.setProjectId(aa.getProjectId());
			a.setAdviserActionId(aaid);
			this.createAttachment(a);
		}
		return aaid;
	}
	
	// TODO: handle facilities on project
	private void createProjectFacility(ProjectFacility f) throws Exception {
		getSqlMapClientTemplate().insert("createProjectFacility", f);
	}
	
	private void createProjectKpi(ProjectKpi pk) throws Exception {
		getSqlMapClientTemplate().insert("createProjectKpi", pk);
	}
	
	private void updateProject(Integer projectId, Project p) throws Exception {
		getSqlMapClientTemplate().update("updateProject", p);
	}

	private void deleteRPLinks(Integer projectId) throws Exception {
        getSqlMapClientTemplate().update("deleteRPLinks", projectId);
	}

	private void deleteRPLink(Integer projectId, Integer researcherId) throws Exception {
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("researcherId", researcherId);
        params.put("projectId", projectId);
        getSqlMapClientTemplate().update("deleteRPLink", params);
	}

	private void deleteAPLinks(Integer projectId) throws Exception {
        getSqlMapClientTemplate().update("deleteAPLinks", projectId);
	}

	private void deleteAPLink(Integer projectId, Integer adviserId) throws Exception {
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("adviserId", adviserId);
        params.put("projectId", projectId);
        getSqlMapClientTemplate().update("deleteAPLink", params);
	}
	
	private void deleteReviews(Integer pid) throws Exception {
		getSqlMapClientTemplate().update("deleteReviews", pid);
	}

	private void deleteReview(Integer id) throws Exception {
		getSqlMapClientTemplate().update("deleteReview", id);
	}

	private void deleteFollowUps(Integer pid) throws Exception {
		getSqlMapClientTemplate().update("deleteFollowUps", pid);
	}

	private void deleteFollowUp(Integer id) throws Exception {
		getSqlMapClientTemplate().update("deleteFollowUp", id);
	}

	private void deleteResearchOutputs(Integer id) throws Exception {
		getSqlMapClientTemplate().update("deleteResearchOutputs", id);
	}
	
	private void deleteResearchOutput(Integer id) throws Exception {
		getSqlMapClientTemplate().update("deleteResearchOutput", id);
	}
	
	private void deleteAdviserActions(Integer pid) throws Exception {
		getSqlMapClientTemplate().update("deleteAdviserActions", pid);		
	}

	private void deleteProjectFacilities(Integer pid) throws Exception {
		getSqlMapClientTemplate().update("deleteProjectFacilities", pid);		
	}

	private void deleteAdviserAction(Integer id) throws Exception {
		getSqlMapClientTemplate().update("deleteAdviserAction", id);		
	}

	private void deleteProjectKpis(Integer id) throws Exception {
		getSqlMapClientTemplate().update("deleteProjectKpis", id);		
	}

	private void deleteProjectKpi(Integer id) throws Exception {
		getSqlMapClientTemplate().update("deleteProjectKpi", id);		
	}

	private void deleteFacilityFromProject(Integer projectId, Integer facilityId) throws Exception {
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("projectId", projectId);
        params.put("facilityId", facilityId);
		getSqlMapClientTemplate().update("deleteFacilityFromProject", params);		
	}

	public String getNextProjectCode(String name) {
		String instCode = (String)getSqlMapClientTemplate().queryForObject("getInstitutionCodeFromName", name);
		String last = (String)getSqlMapClientTemplate().queryForObject("getLastProjectCode", instCode);
		if (last==null) return instCode + StringUtils.leftPad("1",5,"0"); // First ever for this inst
		Integer lastNum = Integer.valueOf(last.replace(instCode, ""));
		return instCode + StringUtils.leftPad(lastNum+1+"",5,"0");
	}

	public List<KpiCode> getKpiCodes() {
		return (List<KpiCode>) getSqlMapClientTemplate().queryForList("getKpiCodes");
	}

	public String getKpiCodeNameById(Integer codeId) {
		return (String) getSqlMapClientTemplate().queryForObject("getKpiCodeNameById", codeId);
	}
	
	public List<ProjectStatus> getProjectStatuses() {
		return (List<ProjectStatus>) getSqlMapClientTemplate().queryForList("getProjectStatuses");
	}
	
	public String getResearcherStatusById(Integer id) {
		return (String) getSqlMapClientTemplate().queryForObject("getResearcherStatusById", id);
	}
	
	public List<ResearcherStatus> getResearcherStatuses() {
		return (List<ResearcherStatus>) getSqlMapClientTemplate().queryForList("getResearcherStatuses");
	}
	
	public String getProjectStatusById(Integer id) {
		return (String) getSqlMapClientTemplate().queryForObject("getProjectStatusById", id);
	}
	
	public String getLinuxUsername(Integer id) {
		return (String) getSqlMapClientTemplate().queryForObject("getLinuxUsername", id); 
	}
}
