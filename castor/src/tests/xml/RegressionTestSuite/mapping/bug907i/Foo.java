

/**
 * Class used in regression of bug907 (Intalio CVS)
 */
public class Foo {
    
    private String _bar = null;
   
    public Foo() {
        super();
    }
    
    public String getBar() {
        return _bar;
    }

    public void setBar(String bar) {
        _bar = bar;
    }

} //-- Foo