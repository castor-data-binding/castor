

public class Get_Root {

    private String _id;
    private String _name  = "Default Value";
    private int    _value = 31337;

    //--
    public Get_Root() {}

    //--
    public Get_Root(String name, int value) {
        _name  = name;
        _value = value;
    }

    //--
    public String getId() {
        return _id;
    }


    public void setId(String id) {
        _id = id;
    }

    //--
    public String getName() {
        return _name;
    }

    // This functions should be ignored as we don't specify if in the mapping file
    public void setName(String name) {
        _name = name;
    }

    //--
    public int getValue() {
        return _value;
    }

}
