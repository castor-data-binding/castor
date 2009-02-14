package org.castor.cpa.test.test1217;

/**
 * @author cwichoski
 */
public final class ExtendedPart extends BasePart {
    private String _number;
    
    public ExtendedPart() { }
    public ExtendedPart(final String oid, final String name, final String number) {
        super(oid, name);
        _number = number;
    }
    
    public String getNumber() { return _number; }
    public void setNumber(final String number) { _number = number; }
}
