package org.castor.cpa.test.test2550;

public final class Entity {
    public static final Integer DEFAULT_ID = new Integer(1);
    public static final Integer ALTERNATE_ID = new Integer(2);
    public static final String  DEFAULT_NAME = "default entity";
    public static final String  ALTERNATE_NAME = "alternate entity";
    
    private Integer _id;
    private String _name;
    
    public Entity() { }
    public Entity(final Integer id, final String name) {
        _id = id;
        _name = name;
    }
    
    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }
    
    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }
}
