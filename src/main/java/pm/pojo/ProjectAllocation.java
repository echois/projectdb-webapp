package pm.pojo;

import java.io.Serializable;

public class ProjectAllocation implements Serializable {
    private Long allocationSeconds;
    private String facilityId;
    private String facilityName;
    private Integer id;
    private String lastModified;
    private String projectCode;
    private Integer projectId;
    private String projectName;

    public Long getAllocationSeconds() {
        return allocationSeconds;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public Integer getId() {
        return id;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setAllocationSeconds(Long allocationSeconds) {
        this.allocationSeconds = allocationSeconds;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
