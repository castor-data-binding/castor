

package jdo;

import java.util.Collection;
import java.util.Date;


public class TestLazyEmployee extends TestLazyPerson {
    private Date _startDate;
    private TestLazyPayRoll _payroll;
    private TestLazyContract _contract;
    private Collection _projects;

    public Date getStartDate() {
        return _startDate;
    }
    public void setStartDate( Date startDate ) {
        _startDate = startDate;
    }
    public TestLazyPayRoll getPayRoll() {
        return _payroll;
    }
    public void setPayRoll( TestLazyPayRoll payroll ) {
        _payroll = payroll;
    }
    public TestLazyContract getContract() {
        return _contract;
    }
    public void setContract( TestLazyContract contract ) {
        _contract = contract;
    }
    public void setProjects( Collection projects ) {
        _projects = projects;
    }
    public Collection getProjects() {
        return _projects;
    }
}
