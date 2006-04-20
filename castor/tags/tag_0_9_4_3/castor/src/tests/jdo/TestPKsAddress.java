

package jdo;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;


public class TestPKsAddress {
    private int _id;
    private String _street;
    private String _city;
    private String _state;
    private String _zip;
	private TestPKsPerson _person;

    public void setId( int id ) {
        _id = id;
    }
    public int getId() {
        return _id;
    }
    public void setStreet( String street ) {
        _street = street;
    }
    public String getStreet() {
        return _street;
    }
    public void setCity( String city ) {
        _city = city;
    }
    public String getCity() {
        return _city;
    }
    public void setState( String state ) {
        _state = state;
    }
    public String getState() {
        return _state;
    }
    public void setZip( String zip ) {
        _zip = zip;
    }
    public String getZip() {
        return _zip;
    }
	public void setPerson( TestPKsPerson person ) {
		_person = person;
	}
	public TestPKsPerson getPerson() {
		return _person;
	}
}
