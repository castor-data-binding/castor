

package jdo;

import java.util.Collection;
import java.util.Date;



public class TestLazyPerson {
    private String _fname;
    private String _lname;
    private Date   _bday;
    private Collection _address;

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
    public Collection getAddress() {
        return _address;
    }
    public void setAddress( Collection address ) {
        _address = address;
    }
}
