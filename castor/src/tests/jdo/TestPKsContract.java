

package jdo;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;



public class TestPKsContract {
    private int _policyNo;
    private int _contractNo;
    private String _comment;
	private TestPKsEmployee _employee;
	private ArrayList _category;

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
	public void setEmployee( TestPKsEmployee employee ) {
		_employee = employee;
	}
	public TestPKsEmployee getEmployee() {
		return _employee;
	}
	public void setCategory( ArrayList category ) {
		_category = category;
	}
	public ArrayList getCategory() {
		return _category;
	}
}