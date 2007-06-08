package jdo.c1379;

import org.exolab.castor.jdo.TimeStampable;

public final class Computer extends Product implements TimeStampable {
    private String  _cpu;
    private long    _timeStamp;

    public String getCpu() { return _cpu; }
    public void setCpu(final String cpu) { _cpu = cpu; }

    public void jdoSetTimeStamp(final long timeStamp) { _timeStamp = timeStamp; }
    public long jdoGetTimeStamp() { return _timeStamp; }

    public String toString() {
        return "<id: " + getId() + " name: " + getName() + " price: " + getPrice() + " cpu: " + _cpu + ">";
    }
}
