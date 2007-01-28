import org.exolab.castor.xml.handlers.DateFieldHandler;
import java.util.TimeZone;

public class Foo {
    
    private java.util.Date _content = null;
    private java.util.Date[] _contentArray = null;
    
    static {
        DateFieldHandler.setAllowTimeZoneSuppression(true);
    }
    public Foo() {
        super();
    }
    
    public java.util.Date getDateValue() {
        return _content;
    } //-- getContent
    
    public void setDateValue(java.util.Date content) {
        _content = content;
    }
    public java.util.Date[] getDateArray() {
        return _contentArray;
    } //-- getContent
    
    public void setDateArray(java.util.Date[] contentArray) {
        _contentArray = contentArray;
    }
     
} //-- Foo
