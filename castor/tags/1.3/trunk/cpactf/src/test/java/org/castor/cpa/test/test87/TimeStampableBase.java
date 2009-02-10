package org.castor.cpa.test.test87;

import org.exolab.castor.jdo.TimeStampable;

public class TimeStampableBase implements TimeStampable {
    public static final Integer DEFAULT_ID = new Integer(1);
    public static final String  DEFAULT_NAME = "default base name";
    public static final String  ALTERNATE_NAME = "alternate base name";
    public static final long    DEFAULT_TIMESTAMP = 0;
    
    private Integer _id;
    private String _name;
    private long _timeStamp;
    
    public TimeStampableBase() { }
    public TimeStampableBase(final Integer id, final String name) {
        _id = id;
        _name = name;
        _timeStamp = DEFAULT_TIMESTAMP;
    }
    
    public final Integer getId() { return _id; }
    public final void setId(final Integer id) { _id = id; }
    
    public final String getName() { return _name; }
    public final void setName(final String name) { _name = name; }
    
    public final long getTimeStamp() { return _timeStamp; }
    public final void setTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }
    
    public final long jdoGetTimeStamp() { return _timeStamp; }
    public final void jdoSetTimeStamp(final long timestamp) {
        _timeStamp = timestamp;
    }
}
