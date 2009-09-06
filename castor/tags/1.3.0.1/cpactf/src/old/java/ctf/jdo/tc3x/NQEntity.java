package ctf.jdo.tc3x;

public final class NQEntity {
    private Integer _id;
    private String _name;
    
    public NQEntity(){        
    }
    
    public NQEntity(Integer id){
        _id = id;
    }
    
    public NQEntity(Integer id, String name){
        _id   = id;
        _name = name; 
    }
    
    public Integer getId() {
        return _id;
    }
    
    public void setId(final Integer id) {
        _id = id;
    }
    
    public String getName() {
        return _name;
    }
    
    public void setName(final String name) {
        _name = name;
    }
}
