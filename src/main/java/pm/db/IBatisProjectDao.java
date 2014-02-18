package pm.db;

import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import pm.authz.annotation.RequireAdmin;
import pm.authz.annotation.RequireAdviser;
import pm.authz.annotation.RequireAdviserOnProject;
import pm.pojo.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IBatisProjectDao extends SqlSessionDaoSupport implements ProjectDao {

    @RequireAdviser
	public synchronized Integer createProjectWrapper(final ProjectWrapper pw) throws Exception {
    	this.createProject(pw.getProject());
    	Integer pid = pw.getProject().getId();
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
    	getSqlSession().insert("pm.db.createResearcher", r);
		return r.getId();
	}

    @RequireAdviser
	public Integer createAdviser(final Adviser a) throws Exception {
    	getSqlSession().insert("pm.db.createAdviser", a);
    	return a.getId();
	}

	public List<Project> getProjects() throws Exception {
		List<Project> ps =  getSqlSession().selectList("pm.db.getProjects");
		for (Project p: ps) {
			ProjectType t = (ProjectType) getSqlSession().selectOne("pm.db.getProjectTypeById", p.getProjectTypeId());
			if (t==null) System.err.println(p.getProjectId() + " is invalid");
			p.setProjectTypeName(t.getName());
			p.setStatusName(getProjectStatusById(p.getStatusId()));
		}
		return ps;
	}

	public List<Researcher> getResearchers() throws Exception {
		List<Researcher> l = getSqlSession().selectList("pm.db.getResearchers");
		if (l != null) {
			for (Researcher r: l) {
				InstitutionalRole ir = (InstitutionalRole) getSqlSession().selectOne("pm.db.getInstitutionalRoleById", r.getInstitutionalRoleId());
				r.setInstitutionalRoleName(ir.getName());
				r.setStatusName(getResearcherStatusById(r.getStatusId()));
			}
		}
		return l;
	}

	public List<Adviser> getAdvisers() throws Exception {
		List<Adviser> list = getSqlSession().selectList("pm.db.getAdvisers");
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

    public synchronized Project getProjectByProjectCode(String projectCode) {

        Project p = (Project) getSqlSession().selectOne("getProjectByProjectCode", projectCode);
		ProjectType t = (ProjectType) getSqlSession().selectOne("getProjectTypeById", p.getProjectTypeId());
		p.setProjectTypeName(t.getName());
		p.setStatusName(getProjectStatusById(p.getStatusId()));
		return p;

    }

    public synchronized ProjectWrapper getProjectWrapperByProjectCode(String projectCode) throws Exception {

        Project p = getProjectByProjectCode(projectCode);

        return getProjectWrapperById(p.getId());
	}

	@RequireAdviserOnProject
	public synchronized void deleteProjectWrapper(Integer projectId) throws Exception {
		getSqlSession().update("deleteProject", projectId);
	}

	public Project getProjectById(final Integer id) throws Exception {
		Project p = (Project) getSqlSession().selectOne("pm.db.getProjectById", id);
		ProjectType t = (ProjectType) getSqlSession().selectOne("pm.db.getProjectTypeById", p.getProjectTypeId());
		p.setProjectTypeName(t.getName());
		p.setStatusName(getProjectStatusById(p.getStatusId()));
		return p;
	}

	public Researcher getResearcherById(final Integer id) throws Exception {
		Researcher r = (Researcher) getSqlSession().selectOne("pm.db.getResearcherById", id);
		InstitutionalRole ir = (InstitutionalRole) getSqlSession().selectOne("pm.db.getInstitutionalRoleById", r.getInstitutionalRoleId());
		r.setInstitutionalRoleName(ir.getName());
		r.setStatusName(getResearcherStatusById(r.getStatusId()));
		return r;
	}

	public Adviser getAdviserById(final Integer id) throws Exception {
		Adviser a = (Adviser) getSqlSession().selectOne("pm.db.getAdviserById", id);
		a.setNumProjects(this.getNumProjectsForAdviser(a.getId()));
		return a;
	}

	public Adviser getAdviserByTuakiriUniqueId(final String id) throws Exception {
		Adviser a = (Adviser) getSqlSession().selectOne("pm.db.getAdviserByTuakiriUniqueId", id);
		return a;
	}
	
	public Adviser getAdviserByDrupalId(final String id) throws Exception {
		Adviser a = (Adviser) getSqlSession().selectOne("pm.db.getAdviserByDrupalId", id);
		return a;
	}
	
	public String getDrupalIdByAdviserId(final Integer id) throws Exception {
		return (String) getSqlSession().selectOne("pm.db.getDrupalIdByAdviserId", id);
	}

	public Integer getNumProjectsForAdviser(Integer adviserId) throws Exception {
		return (Integer) getSqlSession().selectOne("pm.db.getNumProjectsForAdviser", adviserId);
	}

	public List<ResearchOutputType> getResearchOutputTypes() throws Exception {
		return getSqlSession().selectList("pm.db.getResearchOutputTypes");
	}

	public ResearchOutputType getResearchOutputTypeById(Integer id) throws Exception {
		return (ResearchOutputType) getSqlSession().selectOne("pm.db.getResearchOutputTypeById", id);
	}

	public List<Site> getSites() throws Exception {
		return getSqlSession().selectList("pm.db.getSites");
	}

	public List<Affiliation> getAffiliations() throws Exception {
		return getSqlSession().selectList("pm.db.getAffiliations");
	}

	public List<String> getInstitutions() throws Exception {
		return getSqlSession().selectList("pm.db.getInstitutions");
	}

	public List<Kpi> getKpis() throws Exception {
		return getSqlSession().selectList("pm.db.getKpis");
	}

	public List<ProjectKpi> getProjectKpis() throws Exception {
		List<ProjectKpi> l = getSqlSession().selectList("pm.db.getProjectKpis");
		for (ProjectKpi pk: l) {
			Kpi kpi = (Kpi) getSqlSession().selectOne("pm.db.getKpiById", pk.getKpiId());
			Adviser tmp = (Adviser) getSqlSession().selectOne("pm.db.getAdviserById", pk.getAdviserId());
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
		List<ResearchOutput> l = getSqlSession().selectList("pm.db.getResearchOutput");
		for (ResearchOutput ro: l) {
			Adviser a = (Adviser) getSqlSession().selectOne("pm.db.getAdviserById", ro.getAdviserId());
			ResearchOutputType tmp = (ResearchOutputType) getSqlSession().selectOne("pm.db.getResearchOutputTypeById", ro.getTypeId());
			ro.setType(tmp.getName());
			ro.setAdviserName(a.getFullName());
		}
		return l;
	}

	public List<ProjectType> getProjectTypes() throws Exception {
		return getSqlSession().selectList("pm.db.getProjectTypes");
	}

	public List<Researcher> getResearchersOnProject(Integer pid) throws Exception {
		List<Researcher> l = getSqlSession().selectList("pm.db.getResearchersOnProject", pid);
		if (l != null) {
			for (Researcher r: l) {
				InstitutionalRole ir = (InstitutionalRole) getSqlSession().selectOne("pm.db.getInstitutionalRoleById", r.getInstitutionalRoleId());
				r.setInstitutionalRoleName(ir.getName());
				r.setStatusName(getResearcherStatusById(r.getStatusId()));
			}
		}
		return l;
	}

	public List<Adviser> getAdvisersOnProject(Integer pid) throws Exception {
		List<Adviser> list = getSqlSession().selectList("pm.db.getAdvisersOnProject", pid);
		for (Adviser a: list) {
			a.setNumProjects(this.getNumProjectsForAdviser(a.getId()));
		}
		return list;
	}

	public Facility getFacilityById(Integer id) throws Exception {
		return (Facility) getSqlSession().selectOne("pm.db.getFacilityById", id);
	}

	public List<Facility> getFacilities() throws Exception {
		return getSqlSession().selectList("pm.db.getFacilities");
	}

	public List<ProjectFacility> getFacilitiesOnProject(Integer pid) throws Exception {
		List<Facility> fs = getSqlSession().selectList("pm.db.getFacilitiesOnProject", pid);
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
			tmp = getSqlSession().selectList("pm.db.getResearchers");
		} else {
			tmp = getSqlSession().selectList("pm.db.getResearchersNotOnList", l);
		}
		if (tmp != null) {
			for (Researcher r: tmp) {
				InstitutionalRole ir = (InstitutionalRole) getSqlSession().selectOne("pm.db.getInstitutionalRoleById", r.getInstitutionalRoleId());
				r.setInstitutionalRoleName(ir.getName());
				r.setStatusName(getResearcherStatusById(r.getStatusId()));
			}
		}
		return tmp;
	}

	public List<Adviser> getAdvisersNotOnList(List<Integer> l) throws Exception {
		List<Adviser> tmp = null;
		if (l.size() == 0) {
			tmp = getSqlSession().selectList("pm.db.getAdvisers");
		} else {
			tmp = getSqlSession().selectList("pm.db.getAdvisersNotOnList", l);
		}
		return tmp;
	}

	public List<Facility> getFacilitiesNotOnList(List<Integer> l) throws Exception {
		List<Facility> tmp = null;
		if (l.size() == 0) {
			tmp = getSqlSession().selectList("pm.db.getFacilities");
		} else {
			tmp = getSqlSession().selectList("pm.db.getFacilitiesNotOnList", l);
		}
		return tmp;
	}

	public List<ResearcherRole> getResearcherRoles() throws Exception {
		return getSqlSession().selectList("pm.db.getResearcherRoles");
	}

	public List<AdviserRole> getAdviserRoles() throws Exception {
		return getSqlSession().selectList("pm.db.getAdviserRoles");
	}

	public List<InstitutionalRole> getInstitutionalRoles() throws Exception {
		return getSqlSession().selectList("pm.db.getInstitutionalRoles");
	}

	public InstitutionalRole getInstitutionalRoleById(final Integer id) throws Exception {
		return (InstitutionalRole) getSqlSession().selectOne("pm.db.getInstitutionalRoleById", id);
	}

    @RequireAdviser
	public void updateResearcher(final Researcher r) {
		getSqlSession().update("pm.db.updateResearcher", r);
	}

    @RequireAdviser
	public void updateAdviser(final Adviser a) {
		getSqlSession().update("pm.db.updateAdviser", a);
	}

    @RequireAdmin
	public void deleteResearcher(final Integer id) throws Exception {
		getSqlSession().update("pm.db.deleteResearcher", id);
	}

    @RequireAdmin
	public void deleteAdviser(final Integer id) throws Exception {
		getSqlSession().update("pm.db.deleteAdviser", id);
	}

	public Kpi getKpiById(Integer id) throws Exception {
		return (Kpi) getSqlSession().selectOne("pm.db.getKpiById", id);
	}

    public List<Project> getProjectsForResearcherId(Integer id) throws Exception {
    	List<Project> ps = getSqlSession().selectList("pm.db.getProjectsForResearcherId", id);
		for (Project p: ps) {
			ProjectType t = (ProjectType) getSqlSession().selectOne("pm.db.getProjectTypeById", p.getProjectTypeId());
			p.setProjectTypeName(t.getName());
			p.setStatusName(getProjectStatusById(p.getStatusId()));
		}
		return ps;
    }

    public List<Project> getProjectsForAdviserId(Integer id) throws Exception {
    	List<Project> ps = getSqlSession().selectList("pm.db.getProjectsForAdviserId", id);
		for (Project p: ps) {
			ProjectType t = (ProjectType) getSqlSession().selectOne("pm.db.getProjectTypeById", p.getProjectTypeId());
			p.setProjectTypeName(t.getName());
			p.setStatusName(getProjectStatusById(p.getStatusId()));
		}
		return ps;
    }

    public AdviserRole getAdviserRoleById(Integer id) throws Exception {
    	return (AdviserRole) getSqlSession().selectOne("pm.db.getAdviserRoleById", id);
    }

    public ResearcherRole getResearcherRoleById(Integer id) throws Exception {
    	return (ResearcherRole) getSqlSession().selectOne("pm.db.getResearcherRoleById", id);
    }

	private List<RPLink> getRPLinksForProject(Integer pid) throws Exception {
		List<RPLink> l = getSqlSession().selectList("pm.db.getRPLinksForProjectId", pid);
		if (l != null) {
			for (RPLink rp: l) {
				rp.setResearcher(this.getResearcherById(rp.getResearcherId()));
				ResearcherRole rr = (ResearcherRole) getSqlSession().selectOne("pm.db.getResearcherRoleById", rp.getResearcherRoleId());
				rp.setResearcherRoleName(rr.getName());
			}
		}
		return l;
	}

	private List<APLink> getAPLinksForProject(Integer pid) throws Exception {
		List<APLink> l = getSqlSession().selectList("pm.db.getAPLinksForProjectId", pid);
		if (l != null) {
			for (APLink ap: l) {
				ap.setAdviser(this.getAdviserById(ap.getAdviserId()));
				AdviserRole ar = (AdviserRole) getSqlSession().selectOne("pm.db.getAdviserRoleById", ap.getAdviserRoleId());
				ap.setAdviserRoleName(ar.getName());
			}
		}
		return l;
	}

	private List<Review> getReviewsForProjectId(Integer id) throws Exception {
		List<Review> l = getSqlSession().selectList("pm.db.getReviewsForProjectId", id);
		for (Review r: l) {
			Adviser tmp = (Adviser) getSqlSession().selectOne("pm.db.getAdviserById", r.getAdviserId());
			r.setAdviserName(tmp.getFullName());
			r.setAttachments(this.getAttachmentsForReviewId(r.getId()));
		}
		return l;
	}

	private List<FollowUp> getFollowUpsForProjectId(Integer id) throws Exception {
		List<FollowUp> l = getSqlSession().selectList("pm.db.getFollowUpsForProjectId", id);
		for (FollowUp f: l) {
			Adviser tmp = (Adviser) getSqlSession().selectOne("pm.db.getAdviserById", f.getAdviserId());
			f.setAdviserName(tmp.getFullName());
			f.setAttachments(this.getAttachmentsForFollowUpId(f.getId()));
		}
		return l;
	}


	private List<ResearchOutput> getResearchOutputsForProjectId(Integer id) throws Exception {
		List<ResearchOutput> l = getSqlSession().selectList("pm.db.getResearchOutputsForProjectId", id);
		for (ResearchOutput ro: l) {
			Adviser a = (Adviser) getSqlSession().selectOne("pm.db.getAdviserById", ro.getAdviserId());
			ResearchOutputType tmp = (ResearchOutputType) getSqlSession().selectOne("pm.db.getResearchOutputTypeById", ro.getTypeId());
			ro.setType(tmp.getName());
			ro.setAdviserName(a.getFullName());
		}
		return l;
	}

	private List<ProjectKpi> getKpisForProjectId(Integer id) throws Exception {
		List<ProjectKpi> l = getSqlSession().selectList("pm.db.getKpisForProjectId", id);
		for (ProjectKpi pk: l) {
			Adviser tmp = (Adviser) getSqlSession().selectOne("pm.db.getAdviserById", pk.getAdviserId());
			Kpi kpi = (Kpi) getSqlSession().selectOne("pm.db.getKpiById", pk.getKpiId());
			pk.setAdviserName(tmp.getFullName());
			pk.setKpiType(kpi.getType());
			pk.setKpiTitle(kpi.getTitle());
			pk.setCodeName(getKpiCodeNameById(pk.getCode()));
		}
		return l;
	}

	private List<AdviserAction> getAdviserActionsForProjectId(Integer id) throws Exception {
		List<AdviserAction> l = getSqlSession().selectList("pm.db.getAdviserActionsForProjectId", id);
		for (AdviserAction aa: l) {
			Adviser tmp = (Adviser) getSqlSession().selectOne("pm.db.getAdviserById", aa.getAdviserId());
		    aa.setAdviserName(tmp.getFullName());
		    aa.setAttachments(this.getAttachmentsForAdviserActionId(aa.getId()));
		}
		return l;
	}

	private List<Attachment> getAttachmentsForReviewId(Integer rid) throws Exception {
		return getSqlSession().selectList("pm.db.getAttachmentsForReviewId", rid);
	}

	private List<Attachment> getAttachmentsForFollowUpId(Integer rid) throws Exception {
		return getSqlSession().selectList("pm.db.getAttachmentsForFollowUpId", rid);
	}

	private List<Attachment> getAttachmentsForAdviserActionId(Integer rid) throws Exception {
		return getSqlSession().selectList("pm.db.getAttachmentsForAdviserActionId", rid);
	}

	private Integer createProject(Project p) throws Exception {
		getSqlSession().insert("pm.db.createProject", p);
		return p.getId();
	}

	private void createRPLink(RPLink r) throws Exception {
		getSqlSession().insert("pm.db.createRPLink", r);
	}

    private void createAPLink(APLink a) throws Exception {
		getSqlSession().insert("pm.db.createAPLink", a);
	}

	private Integer createReview(Review r) throws Exception {
		getSqlSession().insert("pm.db.createReview", r);
		Integer rid = r.getId();
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
		getSqlSession().insert("pm.db.createFollowUp", f);
		Integer fid = f.getId();
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
		getSqlSession().insert("pm.db.createResearchOutput", o);
	}

	private void createAttachment(Attachment a) throws Exception {
		getSqlSession().insert("pm.db.createAttachment", a);
	}

	private Integer createAdviserAction(AdviserAction aa) throws Exception {
		getSqlSession().insert("pm.db.createAdviserAction", aa);
		Integer aaid = aa.getId();
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
		getSqlSession().insert("pm.db.createProjectFacility", f);
	}

	private void createProjectKpi(ProjectKpi pk) throws Exception {
		getSqlSession().insert("pm.db.createProjectKpi", pk);
	}

	private void updateProject(Integer projectId, Project p) throws Exception {
		getSqlSession().update("pm.db.updateProject", p);
	}

	private void deleteRPLinks(Integer projectId) throws Exception {
        getSqlSession().update("pm.db.deleteRPLinks", projectId);
	}

	private void deleteRPLink(Integer projectId, Integer researcherId) throws Exception {
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("researcherId", researcherId);
        params.put("projectId", projectId);
        getSqlSession().update("pm.db.deleteRPLink", params);
	}

	private void deleteAPLinks(Integer projectId) throws Exception {
        getSqlSession().update("pm.db.deleteAPLinks", projectId);
	}

	private void deleteAPLink(Integer projectId, Integer adviserId) throws Exception {
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("adviserId", adviserId);
        params.put("projectId", projectId);
        getSqlSession().update("pm.db.deleteAPLink", params);
	}

	private void deleteReviews(Integer pid) throws Exception {
		getSqlSession().update("pm.db.deleteReviews", pid);
	}

	private void deleteReview(Integer id) throws Exception {
		getSqlSession().update("pm.db.deleteReview", id);
	}

	private void deleteFollowUps(Integer pid) throws Exception {
		getSqlSession().update("pm.db.deleteFollowUps", pid);
	}

	private void deleteFollowUp(Integer id) throws Exception {
		getSqlSession().update("pm.db.deleteFollowUp", id);
	}

	private void deleteResearchOutputs(Integer id) throws Exception {
		getSqlSession().update("pm.db.deleteResearchOutputs", id);
	}

	private void deleteResearchOutput(Integer id) throws Exception {
		getSqlSession().update("pm.db.deleteResearchOutput", id);
	}

	private void deleteAdviserActions(Integer pid) throws Exception {
		getSqlSession().update("pm.db.deleteAdviserActions", pid);
	}

	private void deleteProjectFacilities(Integer pid) throws Exception {
		getSqlSession().update("pm.db.deleteProjectFacilities", pid);
	}

	private void deleteAdviserAction(Integer id) throws Exception {
		getSqlSession().update("pm.db.deleteAdviserAction", id);
	}

	private void deleteProjectKpis(Integer id) throws Exception {
		getSqlSession().update("pm.db.deleteProjectKpis", id);
	}

	private void deleteProjectKpi(Integer id) throws Exception {
		getSqlSession().update("pm.db.deleteProjectKpi", id);
	}

	private void deleteFacilityFromProject(Integer projectId, Integer facilityId) throws Exception {
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("projectId", projectId);
        params.put("facilityId", facilityId);
		getSqlSession().update("pm.db.deleteFacilityFromProject", params);
	}

	public String getNextProjectCode(String name) {
		String instCode = (String)getSqlSession().selectOne("pm.db.getInstitutionCodeFromName", name);
		if (instCode==null) instCode = name;
		String last = (String)getSqlSession().selectOne("pm.db.getLastProjectCode", instCode);
		if (last==null) return instCode + StringUtils.leftPad("1",5,"0"); // First ever for this inst
		Integer lastNum = Integer.valueOf(last.replace(instCode, ""));
		return instCode + StringUtils.leftPad(lastNum+1+"",5,"0");
	}

	public List<KpiCode> getKpiCodes() {
		return getSqlSession().selectList("pm.db.getKpiCodes");
	}

	public String getKpiCodeNameById(Integer codeId) {
		return (String) getSqlSession().selectOne("pm.db.getKpiCodeNameById", codeId);
	}

	public List<ProjectStatus> getProjectStatuses() {
		return getSqlSession().selectList("pm.db.getProjectStatuses");
	}

	public String getResearcherStatusById(Integer id) {
		return (String) getSqlSession().selectOne("pm.db.getResearcherStatusById", id);
	}

	public List<ResearcherStatus> getResearcherStatuses() {
		return getSqlSession().selectList("pm.db.getResearcherStatuses");
	}

	public String getProjectStatusById(Integer id) {
		return (String) getSqlSession().selectOne("pm.db.getProjectStatusById", id);
	}

	public List<ResearcherProperty> getResearcherProperties(Integer id) throws Exception {
		List<ResearcherProperty> props = getSqlSession().selectList("pm.db.getResearcherProperties", id);
		for (int i=0;i<props.size();i++) {
			for (Site s : this.getSites()) {
				if (s.getId().equals(props.get(i).getSiteId())) {
					props.get(i).setSiteName(s.getName());
				}
			}
		}
		return props;
	}
	
	public void upsertResearcherProperty(ResearcherProperty p) {
		getSqlSession().update("upsertResearcherProperty", p);
	}
	
	public List<ProjectProperty> getProjectProperties(Integer id) throws Exception {
		List<ProjectProperty> props = getSqlSession().selectList("getPropertiesForProjectId", id);
		for (int i=0;i<props.size();i++) {
			props.get(i).setFacilityName(this.getFacilityById(props.get(i).getFacilityId()).getName());
		}
		return props;
	}
	
	public List<String> getPropnames() {
		return getSqlSession().selectList("getPropnames");
	}
	
	public void upsertProjectProperty(ProjectProperty p) {
		getSqlSession().update("upsertProjectProperty", p);
	}
	
	public void deleteProjectProperty(Integer id) {
		getSqlSession().delete("deleteProjectProperty", id);
	}

	public ProjectProperty getProjectProperty(Integer id) {
		return (ProjectProperty) getSqlSession().selectOne("getProjectProperty", id); 
	}
}
