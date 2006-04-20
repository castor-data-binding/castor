

public class Foo {
    
    private String _value1 = null;
    private String _value2 = null;
    
    public Foo() {
        super();
    }
    
    public String getValue1() {
        return _value1;
    } //-- getValue1
    
    public String getValue2() {
        return _value2;
    } //-- getValue2
    
    public void setValue1(String value) {
        _value1 = value;
    } //-- setValue1

    
    public void setValue2(String value) {
        _value2 = value;
    } //-- setValue2
    
} //-- Foo