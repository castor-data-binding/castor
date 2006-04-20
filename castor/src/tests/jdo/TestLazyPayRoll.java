

package jdo;

import java.util.Collection;
import java.util.Date;



public class TestLazyPayRoll {
    private int _id;
    private int _holiday;
    private int _hourlyRate;
	private TestLazyEmployee _employee;

    public void setId( int id ) {
        _id = id;
    }
    public int getId() {
        return _id;
    }
    public void setHoliday( int holiday ) {
        _holiday = holiday;
    }
    public int getHoliday() {
        return _holiday;
    }
    public void setHourlyRate( int rate ) {
        _hourlyRate = rate;
    }
    public int getHourlyRate() {
        return _hourlyRate;
    }
	public void setEmployee( TestLazyEmployee employee ) {
		_employee = employee;
	}
	public TestLazyEmployee getEmployee() {
		return _employee;
	}
}