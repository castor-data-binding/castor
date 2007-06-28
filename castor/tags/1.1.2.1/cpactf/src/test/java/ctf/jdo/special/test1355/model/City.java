package ctf.jdo.special.test1355.model;

public final class City extends BaseObject {
    private String _name;
    private StateProv _state = new StateProv();
    
    public String getName() {
        return _name;
    }

    public void setName(final String name) {
        _name = name;
    }

    public StateProv getState() {
        return _state;
    }

    public void setState(final StateProv state) {
        _state = state;
    }
}

