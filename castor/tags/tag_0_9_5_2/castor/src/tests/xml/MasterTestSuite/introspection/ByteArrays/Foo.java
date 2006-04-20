

public class Foo {
    
    private String _content = null;
    
    public Foo() {
        super();
    }
    
    public byte[] getContentAsBytes() {
        if (_content != null) {
            return _content.getBytes();
        }
        return null;
    } //-- getContent
    
    public String getContentAsString() {
        return _content;
    } //-- getContent
    
    public void setContentAsBytes(byte[] content) {
        _content = new String(content);
    }
    
    public void setContentAsString(String content) {
        _content = content;
    }
    
} //-- Foo