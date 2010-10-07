package org.castor.cpa.persistence.sql.engine.info;

public class ColumnValue {
    private final ColInfo _info;
    private Object _value;
    
    public ColumnValue(final ColInfo info, final Object value) {
        this(info);
        setValue(value);
    }
    
    public ColumnValue(final ColInfo info) {
        _info = info;
    }
    
    public String getName() { return _info.getName(); }
    
    public int getType() { return _info.getSqlType(); }
    
    public int getFieldIndex() { return _info.getFieldIndex(); }
    
    public boolean isStore() { return _info.isStore(); }
    
    public boolean isDirty() { return _info.isDirty(); }
    
    public void setValue(final Object value) { _value = _info.toSQL(value); }
    
    public Object getValue() { return _value; }
}
