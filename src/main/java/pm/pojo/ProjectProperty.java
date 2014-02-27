package pm.pojo;

import java.io.Serializable;

public class ProjectProperty implements Serializable {

	private Integer id;
	private Integer projectId;
	private Integer facilityId;
	private String facilityName;
	private String propname;
	private String propvalue;
	private String timestamp;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public Integer getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}
	public String getPropname() {
		return propname;
	}
	public void setPropname(String propname) {
		this.propname = propname;
	}
	public String getPropvalue() {
		return propvalue;
	}
	public void setPropvalue(String propvalue) {
		this.propvalue = propvalue;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

}
