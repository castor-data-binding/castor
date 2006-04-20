

package jdo;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;


public class TestPKsEmployee extends TestPKsPerson {
    private Date _startDate;
    private TestPKsPayRoll _payroll;
    private TestPKsContract _contract;

    public Date getStartDate() {
        return _startDate;
    }
    public void setStartDate( Date startDate ) {
        _startDate = startDate;
    }
    public TestPKsPayRoll getPayRoll() {
        return _payroll;
    }
    public void setPayRoll( TestPKsPayRoll payroll ) {
        _payroll = payroll;
    }
    public TestPKsContract getContract() {
        return _contract;
    }
    public void setContract( TestPKsContract contract ) {
        _contract = contract;
    }
}
