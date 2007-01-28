
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

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Bar)) {
            return false;
        }
        Bar bar = (Bar) object;
        if (bar._data == null ^ this._data == null) {
            return false;
        }
        if (this._data != null) {
            return this._data.equals(bar._data);
        }
        return true;
    }

} //-- Bar
