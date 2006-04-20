

package jdo;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;



public class TestPKsPerson {
    private String _fname;
    private String _lname;
    private Date   _bday;
    private ArrayList _address;

    public String getFirstName() {
        return _fname;
    }
    public void setFirstName( String fname ) {
        _fname = fname;
    }
    public String getLastName() {
        return _lname;
    }
    public void setLastName( String lname ) {
        _lname = lname;
    }
    public Date getBirthday() {
        return _bday;
    }
    public void setBirthday( Date date ) {
        _bday = date;
    }
    public ArrayList getAddress() {
        return _address;
    }
    public void setAddress( ArrayList address ) {
        _address = address;
    }
}
