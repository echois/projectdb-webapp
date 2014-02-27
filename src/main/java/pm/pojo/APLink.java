package pm.pojo;

import java.io.Serializable;

public class APLink implements Serializable {

	private Integer adviserId;
	private Integer projectId;
	private Integer adviserRoleId;
	private String notes;
	private String adviserRoleName;
	private Adviser adviser;

	public Integer getAdviserRoleId() {
		return adviserRoleId;
	}

	public void setAdviserRoleId(Integer adviserRoleId) {
		this.adviserRoleId = adviserRoleId;
	}

	public Integer getAdviserId() {
		return adviserId;
	}

	public void setAdviserId(Integer adviserId) {
		this.adviserId = adviserId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getAdviserRoleName() {
		return adviserRoleName;
	}

	public void setAdviserRoleName(String adviserRoleName) {
		this.adviserRoleName = adviserRoleName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Adviser getAdviser() {
		return adviser;
	}

	public void setAdviser(Adviser adviser) {
		this.adviser = adviser;
	}

}
