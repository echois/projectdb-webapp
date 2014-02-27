package pm.pojo;

import java.io.Serializable;

public class Attachment implements Serializable {

	Integer id;
	Integer projectId;
	Integer followUpId;
	Integer reviewId;
	Integer adviserActionId;
	String description;
	String link;
	String date;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getDescription() {
		return description;
	}

	public Integer getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Integer followUpId) {
		this.followUpId = followUpId;
	}

	public Integer getReviewId() {
		return reviewId;
	}

	public void setReviewId(Integer reviewId) {
		this.reviewId = reviewId;
	}

	public Integer getAdviserActionId() {
		return adviserActionId;
	}

	public void setAdviserActionId(Integer adviserActionId) {
		this.adviserActionId = adviserActionId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
