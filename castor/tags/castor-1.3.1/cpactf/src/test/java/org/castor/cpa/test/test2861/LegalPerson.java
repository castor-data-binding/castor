package org.castor.cpa.test.test2861;

import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class LegalPerson extends Person {
    String _federalTaxNumber;

    public String getFederalTaxNumber() { return _federalTaxNumber; }
    public void setFederalTaxNumber(final String federalTaxNumber) {
        _federalTaxNumber = federalTaxNumber;
    }
}
