package org.castor.cpa.test.test1073;

import org.exolab.castor.types.Time;
import org.junit.Ignore;

@Ignore
public final class TimeEntity {
    private int          _id;
    private Time         _longTimeLocal;
    private Time         _longTimeUTC;
    private Time         _stringTimeLocal;
    private Time         _stringTimeUTC;

    public int getId() { return _id; }
    public void setId(final int id) { _id = id; }

    public Time getLongTimeLocal() { return _longTimeLocal; }
    public void setLongTimeLocal(final Time time) { _longTimeLocal = time; }

    public Time getLongTimeUTC() { return _longTimeUTC; }
    public void setLongTimeUTC(final Time time) { _longTimeUTC = time; }

    public Time getStringTimeLocal() { return _stringTimeLocal; }
    public void setStringTimeLocal(final Time time) { _stringTimeLocal = time; }

    public Time getStringTimeUTC() { return _stringTimeUTC; }
    public void setStringTimeUTC(final Time time) { _stringTimeUTC = time; }
}
