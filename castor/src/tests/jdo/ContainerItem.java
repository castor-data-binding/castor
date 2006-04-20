package jdo;

public class ContainerItem {
    private Integer _id;
    private Container _item;
    private String _value;

    public Integer getId() {
        return _id;
    }
    
    public void setId(final Integer id) {
        _id = id;
    }
    
    public Container getItem() {
        return _item;
    }
    
    public void setItem(Container item) {
        _item = item;
    }

    public String getValue() {
        return _value;
    }
    
    public void setValue(final String value) {
        _value = value;
    }

}
