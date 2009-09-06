package org.castor.cpa.test.test2527;

public final class ReferingLogEntry extends LogEntry {
    private String _type;
    private String _value;

    public String getType() { return _type; }
    public void setType(final String type) { _type = type; }
    
    public String getValue() { return _value; }
    public void setValue(final String value) { _value = value; }

    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode = 17 * hashCode + ((_type == null) ? 0 : _type.hashCode());
        hashCode = 17 * hashCode + ((_value == null) ? 0 : _value.hashCode());
        return hashCode;
    }
    
    public boolean equals(final Object other) {
        if (other == this) { return true; }
        if (other == null) { return false; }
        if (other.getClass() != this.getClass()) { return false; }
        
        ReferingLogEntry entry = (ReferingLogEntry) other;
        
        return super.equals(entry) 
            && isEqual(_type, entry._type)
            && isEqual(_value, entry._value);
    }
}
