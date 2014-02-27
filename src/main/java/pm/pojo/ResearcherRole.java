package pm.pojo;

import java.io.Serializable;

public class ResearcherRole implements Serializable {

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

}
