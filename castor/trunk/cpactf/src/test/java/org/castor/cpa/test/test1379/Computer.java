package org.castor.cpa.test.test1379;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

@Ignore
public final class Computer extends Product implements TimeStampable {
    private String  _cpu;

    public String getCpu() { return _cpu; }
    public void setCpu(final String cpu) { _cpu = cpu; }
}
