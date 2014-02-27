package pm.pojo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ProjectWrapper implements Serializable {

	private Project project;
	private List<RPLink> rpLinks;
	private List<APLink> apLinks;
	private List<ResearchOutput> researchOutputs;
	private List<ProjectKpi> projectKpis;
	private List<Review> reviews;
	private List<FollowUp> followUps;
	private List<AdviserAction> adviserActions;
	private List<ProjectFacility> projectFacilities;
	// helpers
	private String operation;
	private String errorMessage;
	private String redirect;
	private Integer secondsLeft;

	public ProjectWrapper() {
		this.project = new Project();
		this.rpLinks = new LinkedList<RPLink>();
		this.apLinks = new LinkedList<APLink>();
		this.researchOutputs = new LinkedList<ResearchOutput>();
		this.projectKpis = new LinkedList<ProjectKpi>();
		this.reviews = new LinkedList<Review>();
		this.followUps = new LinkedList<FollowUp>();
		this.adviserActions = new LinkedList<AdviserAction>();
		this.projectFacilities = new LinkedList<ProjectFacility>();
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getRedirect() {
		return redirect;
	}

	public Integer getSecondsLeft() {
		return secondsLeft;
	}

	public void setSecondsLeft(Integer secondsLeft) {
		this.secondsLeft = secondsLeft;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<RPLink> getRpLinks() {
		return rpLinks;
	}

	public void setRpLinks(List<RPLink> rpLinks) {
		this.rpLinks = rpLinks;
	}

	public List<APLink> getApLinks() {
		return apLinks;
	}

	public void setApLinks(List<APLink> apLinks) {
		this.apLinks = apLinks;
	}

	public List<ResearchOutput> getResearchOutputs() {
		return researchOutputs;
	}

	public void setResearchOutputs(List<ResearchOutput> researchOutputs) {
		this.researchOutputs = researchOutputs;
	}

	public List<ProjectKpi> getProjectKpis() {
		return projectKpis;
	}

	public void setProjectKpis(List<ProjectKpi> projectKpis) {
		this.projectKpis = projectKpis;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public List<FollowUp> getFollowUps() {
		return followUps;
	}

	public void setFollowUps(List<FollowUp> followUps) {
		this.followUps = followUps;
	}

	public List<AdviserAction> getAdviserActions() {
		return adviserActions;
	}

	public void setAdviserActions(List<AdviserAction> adviserActions) {
		this.adviserActions = adviserActions;
	}

	public List<ProjectFacility> getProjectFacilities() {
		return projectFacilities;
	}

	public void setProjectFacilities(List<ProjectFacility> projectFacilities) {
		this.projectFacilities = projectFacilities;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
