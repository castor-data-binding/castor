package xml.c1342;

public class Bean {
    private Integer _attribute;
    private String _element;
    private Bean _reference;
    
    public Bean() { }
    
    public Bean(final Integer attribute, final String element, final Bean reference) {
        _attribute = attribute;
        _element = element;
        _reference = reference;
    }
    
    public Integer getAttribute() {
        return _attribute;
    }
    
    public void setAttribute(final Integer attribute) {
        _attribute = attribute;
    }
    
    public String getElement() {
        return _element;
    }
    
    public void setElement(final String element) {
        _element = element;
    }
    
    public Bean getReference() {
        return _reference;
    }
    
    public void setReference(final Bean reference) {
        _reference = reference;
    }
}
