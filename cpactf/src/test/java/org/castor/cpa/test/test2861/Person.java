package org.castor.cpa.test.test2861;

import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class Person extends BusinessObject {
    String _name;
    Person _creator;

    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }
    
    public Person getCreator() { return _creator; }
    public void setCreator(final Person creator) {
        _creator = creator;
    }
}
