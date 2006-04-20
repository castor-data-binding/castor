/*
 * BeanObject.java
 *
 * Created on February 18, 2005, 11:36 AM
 */

package jdo.test1865;

/**
 * @author nstuart
 */
public final class BeanObject {
    private int _id;
    private String _field1;
    private String _field2;
    
    public int getId() { return _id; }
    public void setId(final int id) { _id = id; }

    public String getField1() { return _field1; }
    public void setField1(final String field1) { _field1 = field1; }

    public String getField2() { return _field2; }
    public void setField2(final String field2) { _field2 = field2; }
}
