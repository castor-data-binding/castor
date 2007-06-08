/**
 * Copyright(c) Intalio 2001  All rights reserved.
 */
package ctf.jdo.special.soak.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

public class Employee {

    public Integer      id;

    public String       firstName;

    public String       lastName;

    public char         middleNameInitial;

    public char         gender;

    public boolean      isPerm;

    public BigDecimal   hourlyRate;

    public BigDecimal   totalHours;

    public String       SSN;

    public float        holiday;

    public Date         startDate;

    public Date         recordDate;

    public int          roomNumber;

    public Address      homeAddress;

    public Phone        homePhone;

    public Vector       skills;

}
