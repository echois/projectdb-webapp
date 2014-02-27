package pm.pojo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class AdviserAction implements Serializable {

	public AdviserAction() {
		this.attachments = new LinkedList<Attachment>();
	}
	
	private Integer id;
	private Integer projectId;
	private String date;
	private Integer adviserId;
	private String action;
	private String adviserName;
	private String attachmentDescription;
	private String attachmentLink;
    private List<Attachment> attachments;

	public Integer getAdviserId() {
		return adviserId;
	}

	public void setAdviserId(Integer adviserId) {
		this.adviserId = adviserId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAdviserName() {
		return adviserName;
	}

	public void setAdviserName(String adviserName) {
		this.adviserName = adviserName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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
