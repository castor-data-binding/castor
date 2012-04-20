

public class Set_Root {

    private String _name;
    private int    _value;


    //--
    public Set_Root(String name, int value) {
        _name  = name;
        _value = value;
    }

    //--
    public void setName(String name) {
        _name = name;
    }

    //--
    public void setValue(int value) {
        _value = value;
    }

    //--
    public String getAll() {
        return _name + _value;
    }
}
