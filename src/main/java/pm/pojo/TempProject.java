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

    public Long getLastVisited() {
        return lastVisited;
    }

    public String getOwner() {
        return owner;
    }

    public String getProjectString() {
        return projectString;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setLastVisited(final Long lastVisited) {
        this.lastVisited = lastVisited;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public void setProjectString(final String projectString) {
        this.projectString = projectString;
    }

}
