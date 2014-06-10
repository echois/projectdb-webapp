package pm.pojo;

import java.io.Serializable;

public class ProjectAllocation implements Serializable {
	private Integer id;
	private String projectCode;
	private String project;
	private String allocation;
	private String facility;
	private String lastModified;

	public Integer getId() {
		return id;
	}

	public String getFacility() {
		return facility;
	}

	public String getAllocation() {
		return allocation;
	}

	public String getProject() {
		return project;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public void setFacility(final String facility) {
		this.facility = facility;
	}

	public void setAllocation(final String allocation) {
		this.allocation = allocation;
	}

	public void setProject(final String project) {
		this.project = project;
	}

	public void setProjectCode(final String projectCode) {
		this.projectCode = projectCode;
	}

	public void setLastModified() {
		this.lastModified = lastModified;
	}

}
