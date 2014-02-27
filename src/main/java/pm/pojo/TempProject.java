package pm.pojo;

import java.io.Serializable;

public class TempProject implements Serializable {

	private Integer id;
	private Long lastVisited;
	private String owner;
	private String projectString;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getLastVisited() {
		return lastVisited;
	}

	public void setLastVisited(Long lastVisited) {
		this.lastVisited = lastVisited;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getProjectString() {
		return projectString;
	}

	public void setProjectString(String projectString) {
		this.projectString = projectString;
	}

}
