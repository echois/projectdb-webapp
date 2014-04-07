package pm.pojo;

import java.io.Serializable;

public class ResearcherProperty implements Serializable {

    private Integer id;
    private String propname;
    private String propvalue;
    private Integer researcherId;
    private Integer siteId;
    private String siteName;
    private String timestamp;

    public String getPropname() {
        return propname;
    }

    public String getPropvalue() {
        return propvalue;
    }

    public Integer getResearcherId() {
        return researcherId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setPropname(final String propname) {
        this.propname = propname;
    }

    public void setPropvalue(final String propvalue) {
        this.propvalue = propvalue;
    }

    public void setResearcherId(final Integer researcherId) {
        this.researcherId = researcherId;
    }

    public void setSiteId(final Integer siteId) {
        this.siteId = siteId;
    }

    public void setSiteName(final String siteName) {
        this.siteName = siteName;
    }

    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
