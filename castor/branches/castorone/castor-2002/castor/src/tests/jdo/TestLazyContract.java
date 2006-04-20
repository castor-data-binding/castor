

package jdo;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;



public class TestLazyContract {
    private int _policyNo;
    private int _contractNo;
    private String _comment;
	private TestLazyEmployee _employee;
	private Collection _category;

    public void setPolicyNo( int policy ) {
        _policyNo = policy;
    }
    public int getPolicyNo() {
        return _policyNo;
    }
    public void setContractNo( int no ) {
        _contractNo = no;
    }
    public int getContractNo() {
        return _contractNo;
    }
    public String getComment() {
        return _comment;
    }
    public void setComment( String s ) {
        _comment = s;
    }
	public void setEmployee( TestLazyEmployee employee ) {
		_employee = employee;
	}
	public TestLazyEmployee getEmployee() {
		return _employee;
	}
	public void setCategory( Collection category ) {
		_category = category;
	}
	public Collection getCategory() {
		return _category;
	}
}