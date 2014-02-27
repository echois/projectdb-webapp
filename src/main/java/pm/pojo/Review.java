package pm.pojo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Review implements Serializable {

	private Integer id;
	private Integer projectId;
	private Integer adviserId;
	private String date;
	private String notes;
	private String adviserName;
	private String attachmentDescription;
	private String attachmentLink;
	List<Attachment> attachments;

	public Review() {
		this.attachments = new LinkedList<Attachment>();
	}
	
	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getAdviserName() {
		return adviserName;
	}

	public void setAdviserName(String adviserName) {
		this.adviserName = adviserName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getAdviserId() {
		return adviserId;
	}

	public void setAdviserId(Integer adviserId) {
		this.adviserId = adviserId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAttachmentDescription() {
		return attachmentDescription;
	}

	public void setAttachmentDescription(String attachmentDescription) {
		this.attachmentDescription = attachmentDescription;
	}

	public String getAttachmentLink() {
		return attachmentLink;
	}

	public void setAttachmentLink(String attachmentLink) {
		this.attachmentLink = attachmentLink;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
	
}
