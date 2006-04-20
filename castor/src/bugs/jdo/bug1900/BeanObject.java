package jdo.bug1900;

public final class BeanObject {
    private int     _id;
    private String  _field1;
    private String  _field2;
    
    public BeanObject() { }

    public int getId() {
        return _id;
    }

    public void setId(final int id) {
        this._id = id;
    }

    public String getField1() {
        return _field1;
    }

    public void setField1(final String field1) {
        this._field1 = field1;
    }

    public String getField2() {
        return _field2;
    }

    public void setField2(final String field2) {
        this._field2 = field2;
    }
}
