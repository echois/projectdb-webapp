package pm.pojo;

import java.io.Serializable;

public class RPLink implements Serializable {

	private Integer researcherId;
	private Integer projectId;
	private Integer researcherRoleId;
	private String notes;
	private String researcherRoleName;
	private Researcher researcher;

	public Integer getResearcherRoleId() {
		return researcherRoleId;
	}

	public void setResearcherRoleId(Integer researcherRoleId) {
		this.researcherRoleId = researcherRoleId;
	}

	public Integer getResearcherId() {
		return researcherId;
	}

	public void setResearcherId(Integer researcherId) {
		this.researcherId = researcherId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getResearcherRoleName() {
		return researcherRoleName;
	}

	public void setResearcherRoleName(String researcherRoleName) {
		this.researcherRoleName = researcherRoleName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Researcher getResearcher() {
		return researcher;
	}

	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}

}
