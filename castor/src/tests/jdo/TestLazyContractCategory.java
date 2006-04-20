

package jdo;

import java.util.Collection;


public class TestLazyContractCategory {
    private int _id;
	private String _name;
	private Collection _contract;

    public void setId( int id ) {
        _id = id;
    }
    public int getId() {
        return _id;
    }
	public void setName( String name ) {
		_name = name;
	}
	public String getName() {
		return _name;
	}
	public void setContract( Collection contract ) {
		_contract = contract;
	}
	public Collection getContract() {
		return _contract;
	}
}