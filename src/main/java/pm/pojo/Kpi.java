package pm.pojo;

import java.io.Serializable;

public class Kpi implements Serializable {

    private Integer id;
    private String measures;
    private String title;
    private String type;

    public Integer getId() {
        return id;
    }

    public String getMeasures() {
        return measures;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setMeasures(final String measures) {
        this.measures = measures;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
