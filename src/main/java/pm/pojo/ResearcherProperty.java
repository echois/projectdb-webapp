package pm.pojo;

import java.io.Serializable;

public class ResearcherProperty implements Serializable {

	private Integer researcherId;
	private Integer siteId;
	private String siteName;
	private String propname;
	private String propvalue;
	private String timestamp;
	
	public Integer getResearcherId() {
		return researcherId;
	}
	public void setResearcherId(Integer researcherId) {
		this.researcherId = researcherId;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
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
}
