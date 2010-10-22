package org.castor.cpa.persistence.sql.engine.info;

public final class ColumnValue {
    //-----------------------------------------------------------------------------------    

	private final ColInfo _info;
    private final int _index;
    private Object _value;
    
    //-----------------------------------------------------------------------------------    

    public ColumnValue(final ColInfo info, final int index, final Object value) {
        _info = info;
        _index = index;
        
        setValue(value);
    }
    
    //-----------------------------------------------------------------------------------    

    protected void setValue(final Object value) { _value = _info.toSQL(value); }
    
    //-----------------------------------------------------------------------------------    

    public String getName() { return _info.getName(); }
    
    public int getType() { return _info.getSqlType(); }
    
    public boolean isStore() { return _info.isStore(); }
    
    public boolean isDirty() { return _info.isDirty(); }
    
    public int getIndex() { return _index; }
    
    public Object getValue() { return _value; }

    //-----------------------------------------------------------------------------------    
}
