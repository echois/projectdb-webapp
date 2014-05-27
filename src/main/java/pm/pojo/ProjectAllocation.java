package pm.pojo;

import java.io.Serializable;

public class ProjectAllocation implements Serializable {
	private Integer id;
	private Integer siteId;
	private String allocation;
	private Integer projectId;

	public Integer getId() {
		return id;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public String getAllocation() {
		return allocation;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public void setSiteId(final Integer siteId) {
		this.siteId = siteId;
	}

	public void setAllocation(final String allocation) {
		this.allocation = allocation;
	}

	public void setProjectId(final Integer projectId) {
		this.projectId = projectId;
	}

}
