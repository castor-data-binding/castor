package ctf.jdo.tc8x;

public class ParentWithCompoundId {
    private Integer _id1;
    private Integer _id2;
    private String _name;
    
    public final Integer getId1() {
        return _id1;
    }
    
    public final void setId1(final Integer id) {
        _id1 = id;
    }
    
    public final Integer getId2() {
        return _id2;
    }

    public final void setId2(final Integer id2) {
        _id2 = id2;
    }
    
    public final String getName() {
        return _name;
    }
    
    public final void setName(final String name) {
        _name = name;
    }
}
