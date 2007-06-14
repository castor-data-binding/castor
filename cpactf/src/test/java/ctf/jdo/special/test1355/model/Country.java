package ctf.jdo.special.test1355.model;

public final class Country extends BaseObject {
    private String _name;
    private String _iso3Code;

    public String getName() {
        return _name;
    }

    public void setName(final String name) {
        _name = name;
    }

    public String getIso3Code() {
        return _iso3Code;
    }

    public void setIso3Code(final String iso3Code) {
        _iso3Code = iso3Code;
    }
}
