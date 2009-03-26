package org.castor.cpa.test.test1073;

import org.exolab.castor.types.Duration;
import org.junit.Ignore;

@Ignore
public final class DurationEntity {
    private int          _id;
    private Duration     _longDuration;
    private Duration     _stringDuration;

    public int getId() { return _id; }
    public void setId(final int id) { _id = id; }

    public Duration getLongDuration() { return _longDuration; }
    public void setLongDuration(final Duration duration) { _longDuration = duration; }

    public Duration getStringDuration() { return _stringDuration; }
    public void setStringDuration(final Duration duration) { _stringDuration = duration; }
}
