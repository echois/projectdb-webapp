package pm.pojo;

import java.io.Serializable;

public class ProjectFacility implements Serializable {

    private Integer facilityId;
    private String facilityName;
    private Integer projectId;

    public Integer getFacilityId() {
        return facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setFacilityId(final Integer facilityId) {
        this.facilityId = facilityId;
    }

    public void setFacilityName(final String facilityName) {
        this.facilityName = facilityName;
    }

    public void setProjectId(final Integer projectId) {
        this.projectId = projectId;
    }

}
