
public class Bar extends Foo {
    
	private String _data = null;
	
	public Bar() {
		super();
		setType("bar");
	}
	
	public String getData() {
	    return _data;
	}
	
	public void setData(String data) {
	    _data = data;
	}

} //-- Bar
