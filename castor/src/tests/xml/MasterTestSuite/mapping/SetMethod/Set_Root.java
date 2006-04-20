

public class Set_Root {

    private String _name;
    private int    _value;

    //--
    public Set_Root() {}

    //--
    public Set_Root(String name, int value) {
        _name  = name;
        _value = value;
    }

    //--
    public void setName(String name) {
        _name = name;
    }

    // This method should be ignored as it is not specified in the mapping file
    public String getName() {
        return _name;
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
