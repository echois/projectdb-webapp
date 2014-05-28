package pm.pojo;

import java.io.Serializable;

public class ProjectAllocation implements Serializable {
	private Integer id;
	private String project;
	private String allocation;
	private String site;
	private String lastModified;

	public Integer getId() {
		return id;
	}

	public String getSite() {
		return site;
	}

	public String getAllocation() {
		return allocation;
	}

	public String getProject() {
		return project;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public void setSite(final String site) {
		this.site = site;
	}

	public void setAllocation(final String allocation) {
		this.allocation = allocation;
	}

	public void setProject(final String project) {
		this.project = project;
	}

	public void setLastModified() {
		this.lastModified = lastModified;
	}

}
