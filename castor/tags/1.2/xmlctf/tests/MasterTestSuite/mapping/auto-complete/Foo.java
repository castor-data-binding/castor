
public class Foo {

    private String _name = null;
    private String _type = null;

    public Foo() {
        super();
    }

    public String getName() {
        return _name;
    }

    public String getType() {
        return _type;
    }

    public void setName(String name) {
       _name = name;
    }

    public void setType(String type) {
        _type = type;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof Foo)) {
            return false;
        }
        Foo foo = (Foo) object;
        if (foo._name == null ^ this._name == null) {
            return false;
        }
        if (this._name != null && !this._name.equals(foo._name)) {
            return false;
        }
        if (foo._type == null ^ this._type == null) {
            return false;
        }
        if (this._type != null && !this._type.equals(foo._type)) {
            return false;
        }

        return true;
    }

} //-- Foo