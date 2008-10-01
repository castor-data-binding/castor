package org.castor.cpa.test.test87;

public final class TimeStampableExtended extends TimeStampableBase {
    public static final Integer DEFAULT_ID = new Integer(2);
    public static final String  DEFAULT_NAME = "default extended name";
    public static final String  ALTERNATE_NAME = "alternate extended name";
    public static final String  DEFAULT_NOTE = "default note";
    public static final String  ALTERNATE_NOTE = "alternate note";
    public static final long    DEFAULT_TIMESTAMP = 0;
    
    private String _note;
    
    public TimeStampableExtended() { }
    public TimeStampableExtended(final Integer id, final String name, final String note) {
        super(id, name);
        _note = note;
    }
    
    public String getNote() { return _note; }
    public void setNote(final String note) { _note = note; }
}
