package pm.pojo;

import java.io.Serializable;

public class ProjectProperty implements Serializable {

    private Integer facilityId;
    private String facilityName;
    private Integer id;
    private Integer projectId;
    private String propname;
    private String propvalue;
    private String timestamp;

    public Integer getFacilityId() {
        return facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public Integer getId() {
        return id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public String getPropname() {
        return propname;
    }

    public String getPropvalue() {
        return propvalue;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setFacilityId(final Integer facilityId) {
        this.facilityId = facilityId;
    }

    public void setFacilityName(final String facilityName) {
        this.facilityName = facilityName;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setProjectId(final Integer projectId) {
        this.projectId = projectId;
    }

    public void setPropname(final String propname) {
        this.propname = propname;
    }

    public void setPropvalue(final String propvalue) {
        this.propvalue = propvalue;
    }

    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

}
