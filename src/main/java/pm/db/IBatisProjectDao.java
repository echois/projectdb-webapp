package pm.db;

import java.util.*;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import pm.authz.annotation.RequireAdmin;
import pm.authz.annotation.RequireAdviser;
import pm.authz.annotation.RequireAdviserOnProject;
import pm.pojo.APLink;
import pm.pojo.Adviser;
import pm.pojo.AdviserAction;
import pm.pojo.AdviserRole;
import pm.pojo.Affiliation;
import pm.pojo.Attachment;
import pm.pojo.Change;
import pm.pojo.Facility;
import pm.pojo.FollowUp;
import pm.pojo.InstitutionalRole;
import pm.pojo.Kpi;
import pm.pojo.KpiCode;
import pm.pojo.Project;
import pm.pojo.ProjectFacility;
import pm.pojo.ProjectKpi;
import pm.pojo.ProjectProperty;
import pm.pojo.ProjectStatus;
import pm.pojo.ProjectType;
import pm.pojo.ProjectWrapper;
import pm.pojo.RPLink;
import pm.pojo.ResearchOutput;
import pm.pojo.ResearchOutputType;
import pm.pojo.Researcher;
import pm.pojo.ResearcherProperty;
import pm.pojo.ResearcherRole;
import pm.pojo.ResearcherStatus;
import pm.pojo.Review;
import pm.pojo.Site;

public class IBatisProjectDao extends SqlSessionDaoSupport implements
        ProjectDao {

    @Override
    @RequireAdviser
    public Integer createAdviser(final Adviser a) throws Exception {
        getSqlSession().insert("pm.db.createAdviser", a);
        return a.getId();
    }

    private Integer createAdviserAction(final AdviserAction aa)
            throws Exception {
        getSqlSession().insert("pm.db.createAdviserAction", aa);
        final Integer aaid = aa.getId();
        final List<Attachment> attachments = aa.getAttachments();
        if ((aa.getAttachmentDescription() != null && aa
                .getAttachmentDescription() != "")
                || (aa.getAttachmentLink() != null && aa.getAttachmentLink() != "")) {
            final Attachment a = new Attachment();
            a.setDate(aa.getDate());
            a.setDescription(aa.getAttachmentDescription());
            a.setLink(aa.getAttachmentLink());
            attachments.add(a);
        }
        for (final Attachment a : attachments) {
            a.setProjectId(aa.getProjectId());
            a.setAdviserActionId(aaid);
            createAttachment(a);
        }
        return aaid;
    }

    private void createAPLink(final APLink a) throws Exception {
        getSqlSession().insert("pm.db.createAPLink", a);
    }

    private void createAttachment(final Attachment a) throws Exception {
        getSqlSession().insert("pm.db.createAttachment", a);
    }

    @Override
    public void createDepartment(final Affiliation af) throws Exception {
        getSqlSession().insert("pm.db.createDepartment", af);
    }

    @Override
    public void createDivision(final Affiliation af) throws Exception {
        getSqlSession().insert("pm.db.createDivision", af);
    }

    private Integer createFollowUp(final FollowUp f) throws Exception {
        getSqlSession().insert("pm.db.upsertFollowUp", f);
        final Integer fid = f.getId();
        final List<Attachment> attachments = f.getAttachments();
        if ((f.getAttachmentDescription() != null && f
                .getAttachmentDescription() != "")
                || (f.getAttachmentLink() != null && f.getAttachmentLink() != "")) {
            final Attachment a = new Attachment();
            a.setDate(f.getDate());
            a.setDescription(f.getAttachmentDescription());
            a.setLink(f.getAttachmentLink());
            attachments.add(a);
        }
        for (final Attachment a : attachments) {
            a.setProjectId(f.getProjectId());
            a.setFollowUpId(fid);
            createAttachment(a);
        }
        return fid;
    }

    @Override
    public void createInstitution(final Affiliation af) throws Exception {
        getSqlSession().insert("pm.db.createInstitution", af);
    }

    private Integer createProject(final Project p) throws Exception {
        getSqlSession().insert("pm.db.createProject", p);
        return p.getId();
    }

    // TODO: handle facilities on project
    private void createProjectFacility(final ProjectFacility f)
            throws Exception {
        getSqlSession().insert("pm.db.createProjectFacility", f);
    }

    private void createProjectKpi(final ProjectKpi pk) throws Exception {
        getSqlSession().insert("pm.db.createProjectKpi", pk);
    }

    @Override
    public synchronized Integer createProjectWrapper(final ProjectWrapper pw)
            throws Exception {
        createProject(pw.getProject());
        final Integer pid = pw.getProject().getId();
        final List<RPLink> rpLinks = pw.getRpLinks();
        final List<APLink> apLinks = pw.getApLinks();
        final List<ResearchOutput> ros = pw.getResearchOutputs();
        final List<ProjectKpi> kpis = pw.getProjectKpis();
        final List<Review> reviews = pw.getReviews();
        final List<FollowUp> fus = pw.getFollowUps();
        final List<AdviserAction> aas = pw.getAdviserActions();
        final List<ProjectFacility> pfs = pw.getProjectFacilities();

        for (final RPLink l : rpLinks) {
            l.setProjectId(pid);
            createRPLink(l);
        }
        for (final APLink l : apLinks) {
            l.setProjectId(pid);
            createAPLink(l);
        }
        for (final ResearchOutput ro : ros) {
            ro.setProjectId(pid);
            createResearchOutput(ro);
        }
        for (final ProjectKpi pk : kpis) {
            pk.setProjectId(pid);
            createProjectKpi(pk);
        }
        for (final Review r : reviews) {
            r.setProjectId(pid);
            createReview(r);
        }
        for (final FollowUp fu : fus) {
            fu.setProjectId(pid);
            createFollowUp(fu);
        }
        for (final AdviserAction aa : aas) {
            aa.setProjectId(pid);
            createAdviserAction(aa);
        }
        for (final ProjectFacility pf : pfs) {
            pf.setProjectId(pid);
            createProjectFacility(pf);
        }
        return pid;
    }

    @Override
    public Integer createResearcher(final Researcher r) throws Exception {
        getSqlSession().insert("pm.db.createResearcher", r);
        return r.getId();
    }

    private void createResearchOutput(final ResearchOutput o) throws Exception {
        getSqlSession().insert("pm.db.upsertResearchOutput", o);
    }

    private Integer createReview(final Review r) throws Exception {
        getSqlSession().insert("pm.db.createReview", r);
        final Integer rid = r.getId();
        final List<Attachment> attachments = r.getAttachments();
        if ((r.getAttachmentDescription() != null && r
                .getAttachmentDescription() != "")
                || (r.getAttachmentLink() != null && r.getAttachmentLink() != "")) {
            final Attachment a = new Attachment();
            a.setDate(r.getDate());
            a.setDescription(r.getAttachmentDescription());
            a.setLink(r.getAttachmentLink());
            attachments.add(a);
        }
        for (final Attachment a : attachments) {
            a.setProjectId(r.getProjectId());
            a.setReviewId(rid);
            createAttachment(a);
        }
        return rid;
    }

    private void createRPLink(final RPLink r) throws Exception {
        getSqlSession().insert("pm.db.createRPLink", r);
    }

    @Override
    @RequireAdmin
    public void deleteAdviser(final Integer id) throws Exception {
        getSqlSession().update("pm.db.deleteAdviser", id);
    }

    private void deleteAdviserAction(final Integer id) throws Exception {
        getSqlSession().update("pm.db.deleteAdviserAction", id);
    }

    private void deleteAdviserActions(final Integer pid) throws Exception {
        getSqlSession().update("pm.db.deleteAdviserActions", pid);
    }

    private void deleteAPLink(final Integer projectId, final Integer adviserId)
            throws Exception {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("adviserId", adviserId);
        params.put("projectId", projectId);
        getSqlSession().update("pm.db.deleteAPLink", params);
    }

    private void deleteAPLinks(final Integer projectId) throws Exception {
        getSqlSession().update("pm.db.deleteAPLinks", projectId);
    }

    private void deleteFacilityFromProject(final Integer projectId,
            final Integer facilityId) throws Exception {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", projectId);
        params.put("facilityId", facilityId);
        getSqlSession().update("pm.db.deleteFacilityFromProject", params);
    }

    private void deleteFollowUp(final Integer id) throws Exception {
        getSqlSession().update("pm.db.deleteFollowUp", id);
    }

    private void deleteFollowUps(final Integer pid) throws Exception {
        getSqlSession().update("pm.db.deleteFollowUps", pid);
    }

    private void deleteProjectFacilities(final Integer pid) throws Exception {
        getSqlSession().update("pm.db.deleteProjectFacilities", pid);
    }

    private void deleteProjectKpi(final Integer id) throws Exception {
        getSqlSession().update("pm.db.deleteProjectKpi", id);
    }

    private void deleteProjectKpis(final Integer id) throws Exception {
        getSqlSession().update("pm.db.deleteProjectKpis", id);
    }

    @Override
    public void deleteProjectProperty(final Integer id) {
        getSqlSession().delete("deleteProjectProperty", id);
    }

    @Override
    @RequireAdviserOnProject
    public synchronized void deleteProjectWrapper(final Integer projectId)
            throws Exception {
        getSqlSession().update("deleteProject", projectId);
    }

    @Override
    @RequireAdmin
    public void deleteResearcher(final Integer id) throws Exception {
        getSqlSession().update("pm.db.deleteResearcher", id);
    }

    private void deleteResearchOutput(final Integer id) throws Exception {
        getSqlSession().update("pm.db.deleteResearchOutput", id);
    }

    private void deleteResearchOutputs(final Integer id) throws Exception {
        getSqlSession().update("pm.db.deleteResearchOutputs", id);
    }

    private void deleteReview(final Integer id) throws Exception {
        getSqlSession().update("pm.db.deleteReview", id);
    }

    private void deleteReviews(final Integer pid) throws Exception {
        getSqlSession().update("pm.db.deleteReviews", pid);
    }

    private void deleteRPLink(final Integer projectId,
            final Integer researcherId) throws Exception {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("researcherId", researcherId);
        params.put("projectId", projectId);
        getSqlSession().update("pm.db.deleteRPLink", params);
    }

    private void deleteRPLinks(final Integer projectId) throws Exception {
        getSqlSession().update("pm.db.deleteRPLinks", projectId);
    }

    private List<AdviserAction> getAdviserActionsForProjectId(final Integer id)
            throws Exception {
        final List<AdviserAction> l = getSqlSession().selectList(
                "pm.db.getAdviserActionsForProjectId", id);
        for (final AdviserAction aa : l) {
            final Adviser tmp = (Adviser) getSqlSession().selectOne(
                    "pm.db.getAdviserById", aa.getAdviserId());
            aa.setAdviserName(tmp.getFullName());
            aa.setAttachments(getAttachmentsForAdviserActionId(aa.getId()));
        }
        return l;
    }

    @Override
    public Adviser getAdviserByDrupalId(final String id) throws Exception {
        final Adviser a = (Adviser) getSqlSession().selectOne(
                "pm.db.getAdviserByDrupalId", id);
        return a;
    }

    @Override
    public Adviser getAdviserById(final Integer id) throws Exception {
        final Adviser a = (Adviser) getSqlSession().selectOne(
                "pm.db.getAdviserById", id);
        a.setNumProjects(getNumProjectsForAdviser(a.getId()));
        return a;
    }

    @Override
    public Adviser getAdviserByTuakiriSharedToken(final String id)
            throws Exception {
        final Adviser a = (Adviser) getSqlSession().selectOne(
                "pm.db.getAdviserByTuakiriSharedToken", id);
        return a;
    }

    @Override
    public Adviser getAdviserByTuakiriUniqueId(final String id)
            throws Exception {
        final Adviser a = (Adviser) getSqlSession().selectOne(
                "pm.db.getAdviserByTuakiriUniqueId", id);
        return a;
    }

    @Override
    public AdviserRole getAdviserRoleById(final Integer id) throws Exception {
        return (AdviserRole) getSqlSession().selectOne(
                "pm.db.getAdviserRoleById", id);
    }

    @Override
    public List<AdviserRole> getAdviserRoles() throws Exception {
        return getSqlSession().selectList("pm.db.getAdviserRoles");
    }

    @Override
    public List<Adviser> getAdvisers() throws Exception {
        final List<Adviser> list = getSqlSession().selectList(
                "pm.db.getAdvisers");
        for (final Adviser a : list) {
            a.setNumProjects(getNumProjectsForAdviser(a.getId()));
        }
        return list;
    }

    @Override
    public List<Adviser> getAdvisersNotOnList(final List<Integer> l)
            throws Exception {
        List<Adviser> tmp = null;
        if (l.size() == 0) {
            tmp = getSqlSession().selectList("pm.db.getAdvisers");
        } else {
            tmp = getSqlSession().selectList("pm.db.getAdvisersNotOnList", l);
        }
        return tmp;
    }

    @Override
    public List<Adviser> getAdvisersOnProject(final Integer pid)
            throws Exception {
        final List<Adviser> list = getSqlSession().selectList(
                "pm.db.getAdvisersOnProject", pid);
        for (final Adviser a : list) {
            a.setNumProjects(getNumProjectsForAdviser(a.getId()));
        }
        return list;
    }

    public List<Affiliation> getAffiliationByInstitutionCode(
            final String institutionCode) {
        return getSqlSession().selectOne(
                "pm.db.getAffiliationByInstitutionCode", institutionCode);

    }

    @Override
    public List<Affiliation> getAffiliations() throws Exception {
        return getSqlSession().selectList("pm.db.getAffiliations");
    }

    @Override
    public List<Affiliation> getAffiliationsByDepartmentCode(
            String departmentCode) throws Exception {
        final List<Affiliation> afs = getSqlSession().selectList(
                "pm.db.getAffiliationsByDepartmentCode", departmentCode);
        return afs;
    }

    @Override
    public List<Affiliation> getAffiliationsByDivisionCode(String divisionCode)
            throws Exception {
        final List<Affiliation> afs = getSqlSession().selectList(
                "pm.db.getAffiliationsByDivisionCode", divisionCode);
        return afs;
    }

    @Override
    public List<Affiliation> getAffiliationsByInstitutionCode(
            String institutionCode) throws Exception {
        final List<Affiliation> afs = getSqlSession().selectList(
                "pm.db.getAffiliationsByInstitutionCode", institutionCode);
        return afs;
    }

    private List<APLink> getAPLinksForProject(final Integer pid)
            throws Exception {
        final List<APLink> l = getSqlSession().selectList(
                "pm.db.getAPLinksForProjectId", pid);
        if (l != null) {
            for (final APLink ap : l) {
                ap.setAdviser(getAdviserById(ap.getAdviserId()));
                final AdviserRole ar = (AdviserRole) getSqlSession().selectOne(
                        "pm.db.getAdviserRoleById", ap.getAdviserRoleId());
                ap.setAdviserRoleName(ar.getName());
            }
        }
        return l;
    }

    private List<Attachment> getAttachmentsForAdviserActionId(final Integer rid)
            throws Exception {
        return getSqlSession().selectList(
                "pm.db.getAttachmentsForAdviserActionId", rid);
    }

    private List<Attachment> getAttachmentsForFollowUpId(final Integer rid)
            throws Exception {
        return getSqlSession().selectList("pm.db.getAttachmentsForFollowUpId",
                rid);
    }

    private List<Attachment> getAttachmentsForReviewId(final Integer rid)
            throws Exception {
        return getSqlSession().selectList("pm.db.getAttachmentsForReviewId",
                rid);
    }

    @Override
    public List<Change> getChangeLogForTable(String table) throws Exception {
        return getSqlSession().selectList("pm.db.getChangeLogForTable", table);
    }

    @Override
    public String getDrupalIdByAdviserId(final Integer id) throws Exception {
        return (String) getSqlSession().selectOne(
                "pm.db.getDrupalIdByAdviserId", id);
    }

    @Override
    public List<Facility> getFacilities() throws Exception {
        return getSqlSession().selectList("pm.db.getFacilities");
    }

    @Override
    public List<Facility> getFacilitiesNotOnList(final List<Integer> l)
            throws Exception {
        List<Facility> tmp = null;
        if (l.size() == 0) {
            tmp = getSqlSession().selectList("pm.db.getFacilities");
        } else {
            tmp = getSqlSession().selectList("pm.db.getFacilitiesNotOnList", l);
        }
        return tmp;
    }

    public List<ProjectFacility> getFacilitiesOnProject(final Integer pid)
            throws Exception {
        final List<Facility> fs = getSqlSession().selectList(
                "pm.db.getFacilitiesOnProject", pid);
        final List<ProjectFacility> pfs = new LinkedList<ProjectFacility>();
        for (final Facility f : fs) {
            final ProjectFacility pf = new ProjectFacility();
            pf.setProjectId(pid);
            pf.setFacilityId(f.getId());
            pf.setFacilityName(f.getName());
            pfs.add(pf);
        }
        return pfs;
    }

    @Override
    public Facility getFacilityById(final Integer id) throws Exception {
        return (Facility) getSqlSession()
                .selectOne("pm.db.getFacilityById", id);
    }

    private List<FollowUp> getFollowUpsForProjectId(final Integer id)
            throws Exception {
        final List<FollowUp> l = getSqlSession().selectList(
                "pm.db.getFollowUpsForProjectId", id);
        for (final FollowUp f : l) {
            if (f.getAdviserId() != null) {
                final Adviser tmp = (Adviser) getSqlSession().selectOne(
                        "pm.db.getAdviserById", f.getAdviserId());
                f.setAdviserName(tmp.getFullName());
            } else if (f.getResearcherId() != null) {
                final Researcher tmp = (Researcher) getSqlSession().selectOne(
                        "pm.db.getResearcherById", f.getResearcherId());
                f.setResearcherName(tmp.getFullName());
            }

            f.setAttachments(getAttachmentsForFollowUpId(f.getId()));
        }
        return l;
    }

    public InstitutionalRole getInstitutionalRoleById(final Integer id)
            throws Exception {
        return (InstitutionalRole) getSqlSession().selectOne(
                "pm.db.getInstitutionalRoleById", id);
    }

    @Override
    public List<InstitutionalRole> getInstitutionalRoles() throws Exception {
        return getSqlSession().selectList("pm.db.getInstitutionalRoles");
    }

    @Override
    public List<String> getInstitutions() throws Exception {
        return getSqlSession().selectList("pm.db.getInstitutions");
    }

    @Override
    public Kpi getKpiById(final Integer id) throws Exception {
        return (Kpi) getSqlSession().selectOne("pm.db.getKpiById", id);
    }

    @Override
    public String getKpiCodeNameById(final Integer codeId) {
        return (String) getSqlSession().selectOne("pm.db.getKpiCodeNameById",
                codeId);
    }

    @Override
    public List<KpiCode> getKpiCodes() {
        return getSqlSession().selectList("pm.db.getKpiCodes");
    }

    @Override
    public List<Kpi> getKpis() throws Exception {
        return getSqlSession().selectList("pm.db.getKpis");
    }

    private List<ProjectKpi> getKpisForProjectId(final Integer id)
            throws Exception {
        final List<ProjectKpi> l = getSqlSession().selectList(
                "pm.db.getKpisForProjectId", id);
        for (final ProjectKpi pk : l) {
            final Adviser tmp = (Adviser) getSqlSession().selectOne(
                    "pm.db.getAdviserById", pk.getAdviserId());
            final Kpi kpi = (Kpi) getSqlSession().selectOne("pm.db.getKpiById",
                    pk.getKpiId());
            pk.setAdviserName(tmp.getFullName());
            pk.setKpiType(kpi.getType());
            pk.setKpiTitle(kpi.getTitle());
            pk.setCodeName(getKpiCodeNameById(pk.getCode()));
        }
        return l;
    }

    @Override
    public String getLastModifiedForTable(final String table) {
        return getSqlSession()
                .selectOne("pm.db.getLastModifiedForTable", table);
    }

    @Override
    public String getNextProjectCode(final String name) {
        String instCode = (String) getSqlSession().selectOne(
                "pm.db.getInstitutionCodeFromName", name);
        if (instCode == null) {
            instCode = name;
        }
        final String last = (String) getSqlSession().selectOne(
                "pm.db.getLastProjectCode", instCode);
        if (last == null) {
            return instCode + StringUtils.leftPad("1", 5, "0"); // First ever
                                                                // for this inst
        }
        final Integer lastNum = Integer.valueOf(last.replace(instCode, ""));
        return instCode + StringUtils.leftPad(lastNum + 1 + "", 5, "0");
    }

    @Override
    public Integer getNumProjectsForAdviser(final Integer adviserId)
            throws Exception {
        return (Integer) getSqlSession().selectOne(
                "pm.db.getNumProjectsForAdviser", adviserId);
    }

    public Project getProjectById(final Integer id) throws Exception {
        final Project p = (Project) getSqlSession().selectOne(
                "pm.db.getProjectById", id);
        final ProjectType t = (ProjectType) getSqlSession().selectOne(
                "pm.db.getProjectTypeById", p.getProjectTypeId());
        p.setProjectTypeName(t.getName());
        p.setStatusName(getProjectStatusById(p.getStatusId()));
        return p;
    }

    public synchronized Project getProjectByProjectCode(final String projectCode) {

        final Project p = (Project) getSqlSession().selectOne(
                "pm.db.getProjectByCode", projectCode);
        final ProjectType t = (ProjectType) getSqlSession().selectOne(
                "pm.db.getProjectTypeById", p.getProjectTypeId());
        p.setProjectTypeName(t.getName());
        p.setStatusName(getProjectStatusById(p.getStatusId()));
        return p;

    }

    @Override
    public List<ProjectKpi> getProjectKpis() throws Exception {
        final List<ProjectKpi> l = getSqlSession().selectList(
                "pm.db.getProjectKpis");
        for (final ProjectKpi pk : l) {
            final Kpi kpi = (Kpi) getSqlSession().selectOne("pm.db.getKpiById",
                    pk.getKpiId());
            final Adviser tmp = (Adviser) getSqlSession().selectOne(
                    "pm.db.getAdviserById", pk.getAdviserId());
            final Project p = (Project) getSqlSession().selectOne(
                    "pm.db.getProjectById", pk.getProjectId());
            if (tmp != null) {
                pk.setAdviserName(tmp.getFullName());
            }
            pk.setKpiType(kpi.getType());
            pk.setKpiTitle(kpi.getTitle());
            pk.setCodeName(getKpiCodeNameById(pk.getCode()));
            pk.setProjectCode(p.getProjectCode());
        }
        return l;
    }

    @Override
    public List<ProjectProperty> getProjectProperties(final Integer id)
            throws Exception {
        final List<ProjectProperty> props = getSqlSession().selectList(
                "getPropertiesForProjectId", id);
        for (int i = 0; i < props.size(); i++) {
            props.get(i).setFacilityName(
                    getFacilityById(props.get(i).getFacilityId()).getName());
        }
        return props;
    }

    @Override
    public ProjectProperty getProjectProperty(final Integer id) {
        return (ProjectProperty) getSqlSession().selectOne(
                "getProjectProperty", id);
    }

    @Override
    public List<Project> getProjects() throws Exception {
        final List<Project> ps = getSqlSession()
                .selectList("pm.db.getProjects");
        for (final Project p : ps) {
            final ProjectType t = (ProjectType) getSqlSession().selectOne(
                    "pm.db.getProjectTypeById", p.getProjectTypeId());
            if (t == null) {
                System.err.println(p.getProjectId() + " is invalid");
            }
            p.setProjectTypeName(t.getName());
            p.setStatusName(getProjectStatusById(p.getStatusId()));
        }
        return ps;
    }

    @Override
    public List<Integer> getProjectIds() throws Exception {
        final List<Integer> ps = getSqlSession()
                .selectList("pm.db.getProjectIds");
        return ps;
    }


    @Override
    public List<Project> getProjectsForAdviserId(final Integer id)
            throws Exception {
        final List<Project> ps = getSqlSession().selectList(
                "pm.db.getProjectsForAdviserId", id);
        for (final Project p : ps) {
            final ProjectType t = (ProjectType) getSqlSession().selectOne(
                    "pm.db.getProjectTypeById", p.getProjectTypeId());
            p.setProjectTypeName(t.getName());
            p.setStatusName(getProjectStatusById(p.getStatusId()));
        }
        return ps;
    }

    @Override
    public List<Project> getProjectsForResearcherId(final Integer id)
            throws Exception {
        final List<Project> ps = getSqlSession().selectList(
                "pm.db.getProjectsForResearcherId", id);
        for (final Project p : ps) {
            final ProjectType t = (ProjectType) getSqlSession().selectOne(
                    "pm.db.getProjectTypeById", p.getProjectTypeId());
            p.setProjectTypeName(t.getName());
            p.setStatusName(getProjectStatusById(p.getStatusId()));
        }
        return ps;
    }

    @Override
    public String getProjectStatusById(final Integer id) {
        return (String) getSqlSession().selectOne("pm.db.getProjectStatusById",
                id);
    }

    @Override
    public List<ProjectStatus> getProjectStatuses() {
        return getSqlSession().selectList("pm.db.getProjectStatuses");
    }

    @Override
    public List<ProjectType> getProjectTypes() throws Exception {
        return getSqlSession().selectList("pm.db.getProjectTypes");
    }

    @Override
    public synchronized ProjectWrapper getProjectWrapperById(
            final Integer projectId) throws Exception {
        final ProjectWrapper pw = new ProjectWrapper();
        pw.setProject(getProjectById(projectId));
        pw.setRpLinks(getRPLinksForProject(projectId));
        pw.setApLinks(getAPLinksForProject(projectId));
        pw.setResearchOutputs(getResearchOutputsForProjectId(projectId));
        pw.setProjectKpis(getKpisForProjectId(projectId));
        pw.setReviews(getReviewsForProjectId(projectId));
        pw.setFollowUps(getFollowUpsForProjectId(projectId));
        pw.setAdviserActions(getAdviserActionsForProjectId(projectId));
        pw.setProjectFacilities(getFacilitiesOnProject(projectId));
        return pw;
    }

    @Override
    public synchronized ProjectWrapper getProjectWrapperByProjectCode(
            final String projectCode) throws Exception {

        final Project p = getProjectByProjectCode(projectCode);

        return getProjectWrapperById(p.getId());
    }

    @Override
    public List<String> getPropnames() {
        return getSqlSession().selectList("getPropnames");
    }

    @Override
    public Researcher getResearcherById(final Integer id) throws Exception {
        final Researcher r = (Researcher) getSqlSession().selectOne(
                "pm.db.getResearcherById", id);
        final InstitutionalRole ir = (InstitutionalRole) getSqlSession()
                .selectOne("pm.db.getInstitutionalRoleById",
                        r.getInstitutionalRoleId());
        r.setInstitutionalRoleName(ir.getName());
        r.setStatusName(getResearcherStatusById(r.getStatusId()));
        return r;
    }

    @Override
    public List<ResearcherProperty> getResearcherProperties(final Integer id)
            throws Exception {
        final List<ResearcherProperty> props = getSqlSession().selectList(
                "pm.db.getResearcherProperties", id);
        for (int i = 0; i < props.size(); i++) {
            for (final Site s : getSites()) {
                if (s.getId().equals(props.get(i).getSiteId())) {
                    props.get(i).setSiteName(s.getName());
                }
            }
        }
        return props;
    }

    @Override
    public ResearcherRole getResearcherRoleById(final Integer id)
            throws Exception {
        return (ResearcherRole) getSqlSession().selectOne(
                "pm.db.getResearcherRoleById", id);
    }

    @Override
    public List<ResearcherRole> getResearcherRoles() throws Exception {
        return getSqlSession().selectList("pm.db.getResearcherRoles");
    }

    @Override
    public List<Researcher> getResearchers() throws Exception {
        final List<Researcher> l = getSqlSession().selectList(
                "pm.db.getResearchers");
        if (l != null) {
            for (final Researcher r : l) {
                final InstitutionalRole ir = (InstitutionalRole) getSqlSession()
                        .selectOne("pm.db.getInstitutionalRoleById",
                                r.getInstitutionalRoleId());
                r.setInstitutionalRoleName(ir.getName());
                r.setStatusName(getResearcherStatusById(r.getStatusId()));
            }
        }
        return l;
    }

    @Override
    public List<Researcher> getResearchersNotOnList(final List<Integer> l)
            throws Exception {
        List<Researcher> tmp = null;
        if (l.size() == 0) {
            tmp = getSqlSession().selectList("pm.db.getResearchers");
        } else {
            tmp = getSqlSession()
                    .selectList("pm.db.getResearchersNotOnList", l);
        }
        if (tmp != null) {
            for (final Researcher r : tmp) {
                final InstitutionalRole ir = (InstitutionalRole) getSqlSession()
                        .selectOne("pm.db.getInstitutionalRoleById",
                                r.getInstitutionalRoleId());
                r.setInstitutionalRoleName(ir.getName());
                r.setStatusName(getResearcherStatusById(r.getStatusId()));
            }
        }
        return tmp;
    }

    @Override
    public List<Researcher> getResearchersOnProject(final Integer pid)
            throws Exception {
        final List<Researcher> l = getSqlSession().selectList(
                "pm.db.getResearchersOnProject", pid);
        if (l != null) {
            for (final Researcher r : l) {
                final InstitutionalRole ir = (InstitutionalRole) getSqlSession()
                        .selectOne("pm.db.getInstitutionalRoleById",
                                r.getInstitutionalRoleId());
                r.setInstitutionalRoleName(ir.getName());
                r.setStatusName(getResearcherStatusById(r.getStatusId()));
            }
        }
        return l;
    }

    @Override
    public String getResearcherStatusById(final Integer id) {
        return (String) getSqlSession().selectOne(
                "pm.db.getResearcherStatusById", id);
    }

    @Override
    public List<ResearcherStatus> getResearcherStatuses() {
        return getSqlSession().selectList("pm.db.getResearcherStatuses");
    }

    @Override
    public List<Researcher> getResearchersWhere(String field, Object data) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("field", field);
        params.put("data", data);
        final List<Researcher> l = getSqlSession().selectList(
                "pm.db.getResearchersWhere", params);
        if (l != null) {
            for (final Researcher r : l) {
                final InstitutionalRole ir = (InstitutionalRole) getSqlSession()
                        .selectOne("pm.db.getInstitutionalRoleById",
                                r.getInstitutionalRoleId());
                r.setInstitutionalRoleName(ir.getName());
                r.setStatusName(getResearcherStatusById(r.getStatusId()));
            }
        }
        return l;
    }

    @Override
    public List<ResearchOutput> getResearchOutput() throws Exception {
        final List<ResearchOutput> l = getSqlSession().selectList(
                "pm.db.getResearchOutput");
        for (final ResearchOutput ro : l) {
            final Adviser a = (Adviser) getSqlSession().selectOne(
                    "pm.db.getAdviserById", ro.getAdviserId());
            final Project p = (Project) getSqlSession().selectOne(
                    "pm.db.getProjectById", ro.getProjectId());
            final ResearchOutputType tmp = (ResearchOutputType) getSqlSession()
                    .selectOne("pm.db.getResearchOutputTypeById",
                            ro.getTypeId());
            ro.setType(tmp.getName());
            ro.setProjectCode(p.getProjectCode());
            ro.setAdviserName(a.getFullName());
        }
        return l;
    }

    private List<ResearchOutput> getResearchOutputsForProjectId(final Integer id)
            throws Exception {
        final List<ResearchOutput> l = getSqlSession().selectList(
                "pm.db.getResearchOutputsForProjectId", id);
        for (final ResearchOutput ro : l) {
            if (ro.getAdviserId() != null) {
                final Adviser tmp = (Adviser) getSqlSession().selectOne(
                        "pm.db.getAdviserById", ro.getAdviserId());
                ro.setAdviserName(tmp.getFullName());
            } else if (ro.getResearcherId() != null) {
                final Researcher tmp = (Researcher) getSqlSession().selectOne(
                        "pm.db.getResearcherById", ro.getResearcherId());
                ro.setResearcherName(tmp.getFullName());
            }
            final ResearchOutputType tmp = (ResearchOutputType) getSqlSession()
                    .selectOne("pm.db.getResearchOutputTypeById",
                            ro.getTypeId());
            ro.setType(tmp.getName());
        }
        return l;
    }

    @Override
    public ResearchOutputType getResearchOutputTypeById(final Integer id)
            throws Exception {
        return (ResearchOutputType) getSqlSession().selectOne(
                "pm.db.getResearchOutputTypeById", id);
    }

    @Override
    public List<ResearchOutputType> getResearchOutputTypes() throws Exception {
        return getSqlSession().selectList("pm.db.getResearchOutputTypes");
    }

    private List<Review> getReviewsForProjectId(final Integer id)
            throws Exception {
        final List<Review> l = getSqlSession().selectList(
                "pm.db.getReviewsForProjectId", id);
        for (final Review r : l) {
            final Adviser tmp = (Adviser) getSqlSession().selectOne(
                    "pm.db.getAdviserById", r.getAdviserId());
            r.setAdviserName(tmp.getFullName());
            r.setAttachments(getAttachmentsForReviewId(r.getId()));
        }
        return l;
    }

    private List<RPLink> getRPLinksForProject(final Integer pid)
            throws Exception {
        final List<RPLink> l = getSqlSession().selectList(
                "pm.db.getRPLinksForProjectId", pid);
        if (l != null) {
            for (final RPLink rp : l) {
                rp.setResearcher(getResearcherById(rp.getResearcherId()));
                final ResearcherRole rr = (ResearcherRole) getSqlSession()
                        .selectOne("pm.db.getResearcherRoleById",
                                rp.getResearcherRoleId());
                rp.setResearcherRoleName(rr.getName());
            }
        }
        return l;
    }

    public Map<String, Map<String, Set<String>>> getAllProjectsAndMembers() throws Exception {

        List<Researcher> allResearchers = getResearchers();
        Map<Integer, Researcher> researcherById = Maps.newHashMap();
        for (Researcher r : allResearchers) {
            researcherById.put(r.getId(), r);
        }

        List<ResearcherRole> allRoles = getResearcherRoles();
        Map<Integer, ResearcherRole> rolesById = Maps.newHashMap();
        for (ResearcherRole r : allRoles ) {
            rolesById.put(r.getId(), r);
        }

        List<Project> allProjects = getProjects();

        Map<String, Map<String, Set<String>>> all = Maps.newTreeMap();

        for ( Project p : allProjects ) {

            final List<RPLink> l = getSqlSession().selectList(
                    "pm.db.getRPLinksForProjectId", p.getId());

            final Map<String, Set<String>> allMembers = Maps.newTreeMap();
            if ( l != null ) {
                for ( final RPLink rp : l ) {

                    Researcher researcher = researcherById.get(rp.getResearcherId());
                    ResearcherRole role = rolesById.get(rp.getResearcherRoleId());

                    String rolename = rolesById.get(role.getId()).getName();

                    Set<String> roleMembers = allMembers.get(rolename);
                    if ( roleMembers == null ) {
                        roleMembers = Sets.newTreeSet();
                        allMembers.put(rolename, roleMembers);
                    }
                    roleMembers.add(researcher.getFullName());
                }

                all.put(p.getProjectCode(), allMembers);

            }


        }
        return all;

    }


    @Override
    public List<Site> getSites() throws Exception {
        return getSqlSession().selectList("pm.db.getSites");
    }

    @Override
    public void logChange(final Change c) throws Exception {
        getSqlSession().insert("pm.db.logChange", c);
    }

    @Override
    @RequireAdviser
    public void updateAdviser(final Adviser a) {
        getSqlSession().update("pm.db.updateAdviser", a);
    }

    private void updateProject(final Integer projectId, final Project p)
            throws Exception {
        getSqlSession().update("pm.db.updateProject", p);
    }

    // TODO: ugly implementation. Fix it
    @Override
    @RequireAdviserOnProject
    public synchronized void updateProjectWrapper(final int projectId,
            final ProjectWrapper pw) throws Exception {
        final Integer pid = pw.getProject().getId();
        updateProject(pid, pw.getProject());

        final List<RPLink> rpLinks = pw.getRpLinks();
        final List<APLink> apLinks = pw.getApLinks();
        final List<ResearchOutput> ros = pw.getResearchOutputs();
        final List<ProjectKpi> kpis = pw.getProjectKpis();
        final List<Review> reviews = pw.getReviews();
        final List<FollowUp> fus = pw.getFollowUps();
        final List<AdviserAction> aas = pw.getAdviserActions();
        final List<ProjectFacility> pfs = pw.getProjectFacilities();

        deleteRPLinks(pid);
        for (final RPLink rpLink : rpLinks) {
            createRPLink(rpLink);
        }

        deleteAPLinks(pid);
        for (final APLink apLink : apLinks) {
            createAPLink(apLink);
        }

        deleteResearchOutputs(pid);
        for (final ResearchOutput ro : ros) {
            createResearchOutput(ro);
        }

        deleteProjectKpis(pid);
        for (final ProjectKpi pk : kpis) {
            createProjectKpi(pk);
        }

        deleteReviews(pid);
        for (final Review r : reviews) {
            createReview(r);
        }

        deleteFollowUps(pid);
        for (final FollowUp fu : fus) {
            createFollowUp(fu);
        }

        deleteAdviserActions(pid);
        for (final AdviserAction aa : aas) {
            createAdviserAction(aa);
        }

        deleteProjectFacilities(pid);
        for (final ProjectFacility pf : pfs) {
            createProjectFacility(pf);
        }
    }

    @Override
    @RequireAdviser
    public void updateResearcher(final Researcher r) {
        getSqlSession().update("pm.db.updateResearcher", r);
    }

    @Override
    @RequireAdviserOnProject
    public void upsertFollowUp(final FollowUp f) {
        getSqlSession().insert("pm.db.upsertFollowUp", f);
    }

    @Override
    public void upsertProjectProperty(final ProjectProperty p) {
        getSqlSession().insert("pm.db.upsertProjectProperty", p);
    }

    @Override
    public void upsertResearcherProperty(final ResearcherProperty p) {
        getSqlSession().insert("pm.db.upsertResearcherProperty", p);
    }

    @Override
    @RequireAdviserOnProject
    public void upsertResearchOutput(ResearchOutput ro) {
        getSqlSession().insert("pm.db.upsertResearchOutput", ro);
    }
}
