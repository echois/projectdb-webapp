package pm.pojo;

import java.io.Serializable;

public class KpiCode implements Serializable {

    private String code;
    private Integer id;

    public String getCode() {
        return code;
    }

    public Integer getId() {
        return id;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

}
