package org.castor.cpa.test.test2861;

import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class Parameter extends BusinessObject {
    Person _person;
    String _idSys;
    Integer _intValue;

    public Person getPerson() { return _person; }
    public void setPerson(final Person person) { _person = person; }
    
    public String getIdSys() { return _idSys; }
    public void setIdSys(final String idSys) { _idSys = idSys; }
    
    public Integer getIntValue() { return _intValue; }
    public void setIntValue(final Integer intValue) { _intValue = intValue; }
}