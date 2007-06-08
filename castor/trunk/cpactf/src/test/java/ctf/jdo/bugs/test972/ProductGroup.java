package ctf.jdo.bugs.test972;

public final class ProductGroup {
    private int       _id;
    private String    _name;

    public int getId() { return _id; }
    public void setId(final int id) { _id = id; }

    public String getName() { return _name; }
    public void setName(final String name) { _name = name; }

    public String toString() {
        return "<id: " + _id + " name: " + _name + ">";
    }
}
