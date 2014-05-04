package nz.org.nesi.researchHub.model;

import pm.pojo.*;

import java.util.List;

/**
 * @author: Markus Binsteiner
 */
public class ProjectEnvironmentModel {

	private List<String> institutions = null;
	private List<Facility> faculties = null;
	private List<Site> sites = null;
	private List<ProjectType> types = null;
	private List<ProjectStatus> stats = null;
	private List<Kpi> kpis = null;
	private List<KpiCode> kpcs = null;
	private List<ResearchOutputType> rotypes = null;
	private List<ResearcherRole> roles = null;
	private List<Researcher> researchers = null;
	private List<AdviserRole> ad_roles = null;
	private List<Adviser> advisers = null;

	public ProjectEnvironmentModel() {
	}

	public List<String> getInstitutions() {
		return institutions;
	}

	public void setInstitutions(List<String> institutions) {
		this.institutions = institutions;
	}

	public List<Facility> getFaculties() {
		return faculties;
	}

	public void setFaculties(List<Facility> faculties) {
		this.faculties = faculties;
	}

	public List<Site> getSites() {
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

	public List<ProjectType> getTypes() {
		return types;
	}

	public void setTypes(List<ProjectType> types) {
		this.types = types;
	}

	public List<ProjectStatus> getStats() {
		return stats;
	}

	public void setStats(List<ProjectStatus> stats) {
		this.stats = stats;
	}

	public List<Kpi> getKpis() {
		return kpis;
	}

	public void setKpis(List<Kpi> kpis) {
		this.kpis = kpis;
	}

	public List<KpiCode> getKpcs() {
		return kpcs;
	}

	public void setKpcs(List<KpiCode> kpcs) {
		this.kpcs = kpcs;
	}

	public List<ResearchOutputType> getRotypes() {
		return rotypes;
	}

	public void setRotypes(List<ResearchOutputType> rotypes) {
		this.rotypes = rotypes;
	}

	public List<ResearcherRole> getRoles() {
		return roles;
	}

	public void setRoles(List<ResearcherRole> roles) {
		this.roles = roles;
	}

	public List<Researcher> getResearchers() {
		return researchers;
	}

	public void setResearchers(List<Researcher> researchers) {
		this.researchers = researchers;
	}

	public List<AdviserRole> getAd_roles() {
		return ad_roles;
	}

	public void setAd_roles(List<AdviserRole> ad_roles) {
		this.ad_roles = ad_roles;
	}

	public List<Adviser> getAdvisers() {
		return advisers;
	}

	public void setAdvisers(List<Adviser> advisers) {
		this.advisers = advisers;
	}
}
