

package jdo;


public class TestLazyProject {
    private int _id;
    private String _name;
    private TestLazyEmployee _owner;

    public void setId( int id ) {
        _id = id;
    }
    public int getId() {
        return _id;
    }
    public void setName( String name ) {
        _name = name;
    }
    public String getName() {
        return _name;
    }
    public void setOwner( TestLazyEmployee owner ) {
        _owner = owner;
    }
    public TestLazyEmployee getOwner() {
        return _owner;
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<");
        sb.append(_id);
        sb.append(": ");
        sb.append(_name);
        sb.append(" of ");
        if ( _owner != null ) {
            sb.append(_owner.getLastName());
            sb.append(", ");
            sb.append(_owner.getFirstName());
        } else {
            sb.append("--nobody--");
        }
        sb.append(">");
        return sb.toString();
    }
}
