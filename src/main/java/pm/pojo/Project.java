package pm.pojo;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Project implements Serializable {

    private String description;
    private String endDate;
    private String hostInstitution;
    private Integer id;
    private String lastModified;
    private String name;
    private String nextFollowUpDate;
    private String nextReviewDate;
    private String notes;
    private String projectCode;
    private Integer projectTypeId;
    private String projectTypeName;
    private String requirements;
    private String startDate;
    private Integer statusId;
    private String statusName;
    private String todo;

    public String getDescription() {
        return description;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getHostInstitution() {
        return hostInstitution;
    }

    public Integer getId() {
        return id;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getName() {
        return name;
    }

    public String getNextFollowUpDate() {
        return nextFollowUpDate;
    }

    public String getNextReviewDate() {
        return nextReviewDate;
    }

    public String getNotes() {
        return notes;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public Integer getProjectId() {
        return id;
    }

    public Integer getProjectTypeId() {
        return projectTypeId;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getStartDate() {
        return startDate;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public String getTodo() {
        return todo;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    public void setHostInstitution(final String hostInstitution) {
        this.hostInstitution = hostInstitution;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setLastModified(final String lastModified) {
        this.lastModified = lastModified;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setNextFollowUpDate(final String nextFollowUpDate) {
        this.nextFollowUpDate = nextFollowUpDate;
    }

    public void setNextReviewDate(final String nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }

    public void setProjectCode(final String projectCode) {
        this.projectCode = projectCode;
    }

    public void setProjectTypeId(final Integer projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    public void setProjectTypeName(final String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public void setRequirements(final String requirements) {
        this.requirements = requirements;
    }

    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    public void setStatusId(final Integer statusId) {
        this.statusId = statusId;
        if (statusId.equals(4)) {
            if (endDate.equals("")) {
                final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                endDate = df.format(new Date());
            }
            nextFollowUpDate = "";
            nextReviewDate = "";
        }
    }

    public void setStatusName(final String statusName) {
        this.statusName = statusName;
    }

    public void setTodo(final String todo) {
        this.todo = todo;
    }

    @Override
    public String toString() {
        return "Project{" + "id=" + id + ", projectCode='" + projectCode + '\''
                + ", projectTypeId=" + projectTypeId + ", statusId=" + statusId
                + ", statusName='" + statusName + '\'' + ", name='" + name
                + '\'' + ", description='" + description + '\''
                + ", hostInstitution='" + hostInstitution + '\''
                + ", startDate='" + startDate + '\'' + ", nextReviewDate='"
                + nextReviewDate + '\'' + ", nextFollowUpDate='"
                + nextFollowUpDate + '\'' + ", endDate='" + endDate + '\''
                + ", notes='" + notes + '\'' + ", todo='" + todo + '\''
                + ", requirements='" + requirements + '\''
                + ", projectTypeName='" + projectTypeName + '\'' + '}';
    }
}
