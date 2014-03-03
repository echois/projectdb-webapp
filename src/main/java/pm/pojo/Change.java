package pm.pojo;

import java.io.Serializable;

public class Change implements Serializable {

    private Integer adviserId;
    private String field;
    private Integer id;
    private String new_val;
    private String old_val;
    private String tbl;
    private Integer tbl_id;
    private String timestamp;

    public Integer getAdviserId() {
        return adviserId;
    }

    public String getField() {
        return field;
    }

    public Integer getId() {
        return id;
    }

    public String getNew_val() {
        return new_val;
    }

    public String getOld_val() {
        return old_val;
    }

    public String getTbl() {
        return tbl;
    }

    public Integer getTbl_id() {
        return tbl_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setAdviserId(Integer adviserId) {
        this.adviserId = adviserId;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNew_val(String new_val) {
        this.new_val = new_val;
    }

    public void setOld_val(String old_val) {
        this.old_val = old_val;
    }

    public void setTbl(String tbl) {
        this.tbl = tbl;
    }

    public void setTbl_id(Integer tbl_id) {
        this.tbl_id = tbl_id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
