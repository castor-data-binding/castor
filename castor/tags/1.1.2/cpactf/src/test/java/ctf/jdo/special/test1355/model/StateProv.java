package ctf.jdo.special.test1355.model;

public final class StateProv extends BaseObject {
    private String _code;
    private String _name;
    private Country _country = new Country();

    public String getCode() {
        return _code;
    }
    
    public void setCode(final String code) {
        _code = code;
    }

    public String getName() {
        return _name;
    }
    
    public void setName(final String name) {
        _name = name;
    }

    public Country getCountry() {
        return _country;
    }

    public void setCountry(final Country country) {
        _country = country;
    }
}

