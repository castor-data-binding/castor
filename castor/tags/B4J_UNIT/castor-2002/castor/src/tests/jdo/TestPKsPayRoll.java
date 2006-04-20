

package jdo;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;



public class TestPKsPayRoll {
    private int _id;
    private int _holiday;
    private int _hourlyRate;
	private TestPKsEmployee _employee;

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
	public void setEmployee( TestPKsEmployee employee ) {
		_employee = employee;
	}
	public TestPKsEmployee getEmployee() {
		return _employee;
	}
}