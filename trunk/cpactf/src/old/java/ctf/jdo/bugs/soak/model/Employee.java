/**
 * Copyright(c) Intalio 2001  All rights reserved.
 */
package ctf.jdo.bugs.soak.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

public final class Employee {
    private Integer _id;
    private String _firstName;
    private String _lastName;
    private char _middleNameInitial;
    private char _gender;
    private boolean _isPerm;
    private BigDecimal _hourlyRate;
    private BigDecimal _totalHours;
    private String _ssn;
    private float _holiday;
    private Date _startDate;
    private Date _recordDate;
    private int _roomNumber;
    private Address _homeAddress;
    private Phone _homePhone;
    private Vector _skills;
    
    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }
    
    public String getFirstName() { return _firstName; }
    public void setFirstName(final String firstName) {
        _firstName = firstName;
    }
    
    public String getLastName() { return _lastName; }
    public void setLastName(final String lastName) {
        _lastName = lastName;
    }
    
    public char getMiddleNameInitial() { return _middleNameInitial; }
    public void setMiddleNameInitial(final char middleNameInitial) {
        _middleNameInitial = middleNameInitial;
    }
    
    public char getGender() { return _gender; }
    public void setGender(final char gender) { _gender = gender; }
    
    public boolean isPerm() { return _isPerm; }
    public void setPerm(final boolean isPerm) { _isPerm = isPerm; }
    
    public BigDecimal getHourlyRate() { return _hourlyRate; }
    public void setHourlyRate(final BigDecimal hourlyRate) {
        _hourlyRate = hourlyRate;
    }
    
    public BigDecimal getTotalHours() { return _totalHours; }
    public void setTotalHours(final BigDecimal totalHours) {
        _totalHours = totalHours;
    }
    
    public String getSSN() { return _ssn; }
    public void setSSN(final String ssn) { _ssn = ssn; }
    
    public float getHoliday() { return _holiday; }
    public void setHoliday(final float holiday) { _holiday = holiday; }
    
    public Date getStartDate() { return _startDate; }
    public void setStartDate(final Date startDate) {
        _startDate = startDate;
    }
    
    public Date getRecordDate() { return _recordDate; }
    public void setRecordDate(final Date recordDate) {
        _recordDate = recordDate;
    }
    
    public int getRoomNumber() { return _roomNumber; }
    public void setRoomNumber(final int roomNumber) {
        _roomNumber = roomNumber;
    }
    
    public Address getHomeAddress() { return _homeAddress; }
    public void setHomeAddress(final Address homeAddress) {
        _homeAddress = homeAddress;
    }
    
    public Phone getHomePhone() { return _homePhone; }
    public void setHomePhone(final Phone homePhone) {
        _homePhone = homePhone;
    }
    
    public Vector getSkills() { return _skills; }
    public void setSkills(final Vector skills) { _skills = skills; }
}
