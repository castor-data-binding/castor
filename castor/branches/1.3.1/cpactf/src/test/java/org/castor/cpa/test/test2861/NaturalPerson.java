package org.castor.cpa.test.test2861;

import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class NaturalPerson extends Person {
    String _socialSecurityNumber;

    public String getSocialSecurityNumber() { return _socialSecurityNumber; }
    public void setSocialSecurityNumber(final String socialSecurityNumber) {
        _socialSecurityNumber = socialSecurityNumber;
    }
}
