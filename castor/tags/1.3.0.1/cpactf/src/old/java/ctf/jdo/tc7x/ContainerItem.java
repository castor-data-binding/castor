package ctf.jdo.tc7x;

public final class ContainerItem {
    private Integer _id;
    private Container _item;
    private String _value;

    public Integer getId() { return _id; }
    public void setId(final Integer id) { _id = id; }
    
    public Container getItem() { return _item; }
    public void setItem(final Container item) { _item = item; }

    public String getValue() { return _value; }
    public void setValue(final String value) { _value = value; }
}
