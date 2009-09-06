package org.castor.cpa.test.test87;

import org.exolab.castor.jdo.TimeStampable;

public final class TimeStampableGroup implements TimeStampable {
    public static final Integer DEFAULT_ID = new Integer(1);
    public static final String  DEFAULT_NAME = "default group";
    public static final String  ALTERNATE_NAME = "alternate group";
    public static final long    DEFAULT_TIMESTAMP = 0;
    
    private Integer _id;
    private String _name;
    private long _timeStamp;
    
    public TimeStampableGroup() { }
    public TimeStampableGroup(final Integer id, final String name) {
        _id = id;
        _name = name;
        _timeStamp = DEFAULT_TIMESTAMP;
    }
    
    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }
    
    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }
    
    public long getTimeStamp() { return _timeStamp; }
    public void setTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }
    
    public long jdoGetTimeStamp() { return _timeStamp; }
    public void jdoSetTimeStamp(final long timestamp) {
        _timeStamp = timestamp;
    }
}
