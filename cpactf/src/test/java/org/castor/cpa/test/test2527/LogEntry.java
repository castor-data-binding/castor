package org.castor.cpa.test.test2527;

import java.util.Date;

public class LogEntry {
    private Integer _id;
    private Date _timestamp;
    private String _source;
    private String _level;
    private String _message;
    private LogExceptionEntry _exception;

    public final Integer getId() { return _id; }
    public final void setId(final Integer id) { _id = id; }

    public final Date getTimestamp() { return _timestamp; }
    public final void setTimestamp(final Date timestamp) { _timestamp = timestamp; }
    
    public final String getSource() { return _source; }
    public final void setSource(final String source) { _source = source; }
    
    public final String getLevel() { return _level; }
    public final void setLevel(final String level) { _level = level; }
    
    public final String getMessage() { return _message; }
    public final void setMessage(final String message) { _message = message; }
    
    public final LogExceptionEntry getException() { return _exception; }
    public final void setException(final LogExceptionEntry exception) {
        _exception = exception;
    }

    public String toString() {
        return _timestamp + " " + _source + " " + _level + " " + _message;
    }
    
    public int hashCode() {
        int hashCode = ((_source == null) ? 0 : _source.hashCode());
        hashCode = 17 * hashCode + ((_level == null) ? 0 : _level.hashCode());
        hashCode = 17 * hashCode + ((_message == null) ? 0 : _message.hashCode());
        return hashCode;
    }
    
    public boolean equals(final Object other) {
        if (other == this) { return true; }
        if (other == null) { return false; }
        if (other.getClass() != this.getClass()) { return false; }
        
        LogEntry entry = (LogEntry) other;
        
        return isEqual(_source, entry._source)
            && isEqual(_level, entry._level)
            && isEqual(_message, entry._message);
    }
    
    protected final boolean isEqual(final Object o1, final Object o2) {
        if ((o1 == null) && (o2 == null)) { return true; }
        if ((o1 == null) || (o2 == null)) { return false; }
        return o1.equals(o2);
    }
}
