package org.castor.cpa.test.test1044;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

@Ignore
public class BaseObject implements TimeStampable {
    private int _id;
    private String _description;
    private boolean _saved;
    private long _timestamp;

    public final int getId() {
        return _id;
    }

    public final void setId(final int id) {
        _id = id;
    }

    public final String getDescription() {
        return _description;
    }

    public final void setDescription(final String description) {
        _description = description;
    }

    public final boolean isSaved() {
        return _saved;
    }

    public final void setSaved(final boolean saved) {
        _saved = saved;
    }

    public final long getTimestamp() {
        return _timestamp;
    }

    public final void setTimestamp(final long timestamp) {
        _timestamp = timestamp;
    }

    public final void jdoSetTimeStamp(final long timestamp) {
        setTimestamp(timestamp);
    }

    public final long jdoGetTimeStamp() {
        return getTimestamp();
    }
}
