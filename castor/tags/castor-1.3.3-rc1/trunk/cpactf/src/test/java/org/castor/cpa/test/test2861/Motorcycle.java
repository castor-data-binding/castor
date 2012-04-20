package org.castor.cpa.test.test2861;

import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class Motorcycle extends Product {
    String _chassisNumber;
    Person _holder;
    Person _reseller;

    public String getChassisNumber() { return _chassisNumber; }
    public void setChassisNumber(final String chassisNumber) {
        _chassisNumber = chassisNumber;
    }

    public Person getHolder() { return _holder; }
    public void setHolder(final Person holder) {  _holder = holder; }

    public Person getReseller() { return _reseller; }
    public void setReseller(final Person reseller) {  _reseller = reseller; }
}
