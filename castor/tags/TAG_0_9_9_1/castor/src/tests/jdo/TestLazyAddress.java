

package jdo;


public class TestLazyAddress {
    private int _id;
    private String _street;
    private String _city;
    private String _state;
    private String _zip;
	private TestLazyPerson _person;

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
	public void setPerson( TestLazyPerson person ) {
		_person = person;
	}
	public TestLazyPerson getPerson() {
		return _person;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<");
		sb.append(_id);
		sb.append(": ");
		sb.append(_street);
		sb.append("  ");
		sb.append(_city);
		sb.append("  ");
		sb.append(_state);
		sb.append("  ");
		sb.append(_zip);
		sb.append(" of ");
		if ( _person != null ) {
			sb.append(_person.getLastName());
			sb.append(", ");
			sb.append(_person.getFirstName());
		} else {
			sb.append("--nobody--");
		}
		sb.append(">");
		return sb.toString();
	}
    public boolean equals( Object o ) {
        if ( this == o )
            return true;

        if ( o instanceof TestLazyAddress ) {
            TestLazyAddress other = (TestLazyAddress) o;
            if ( other._id == _id )
                return true;
            else
                return false;
        } else
            return false;
    }
}
