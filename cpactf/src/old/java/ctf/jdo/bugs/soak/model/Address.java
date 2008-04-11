/**
 * Copyright(c) Intalio 2001  All rights reserved.
 */
package ctf.jdo.bugs.soak.model;

public final class Address implements java.io.Serializable {
    private static final long serialVersionUID = -6684426600908835407L;

    private String _poBox;
    private String _streetNumber;
    private String _street;
    private String _apt;
    private String _city;
    private String _state;
    private String _country;
    private String _zip;

    public Address(final String poBox, final String city, final String state,
            final String country, final String zip) {
        _poBox           = poBox;
        _city            = city;
        _state           = state;
        _country         = country;
        _zip             = zip;
    }
    
    public Address(final String streetNumber, final String street, final String apt,
            final String city, final String state, final String country,
            final String zip) {
        _streetNumber    = streetNumber;
        _street          = street;
        _apt             = apt;
        _city            = city;
        _state           = state;
        _country         = country;
        _zip             = zip;
    }

    public boolean equals(final Object other) {
        if (!(other instanceof Address)) { return false; }

        Address addr = (Address) other;
        return equals(_poBox,           addr._poBox)
            && equals(_streetNumber,    addr._streetNumber)
            && equals(_street,          addr._street)
            && equals(_apt,             addr._apt)
            && equals(_city,            addr._city)
            && equals(_state,           addr._state)
            && equals(_country,         addr._country)
            && equals(_zip,             addr._zip);
    }
    
    public int hashCode() {
        int hashCode = 0;
        if (_poBox != null) { hashCode ^= _poBox.hashCode(); }
        if (_streetNumber != null) { hashCode ^= _streetNumber.hashCode(); }
        if (_street != null) { hashCode ^= _street.hashCode(); }
        if (_apt != null) { hashCode ^= _apt.hashCode(); }
        if (_city != null) { hashCode ^= _city.hashCode(); }
        if (_state != null) { hashCode ^= _state.hashCode(); }
        if (_country != null) { hashCode ^= _country.hashCode(); }
        if (_zip != null) { hashCode ^= _zip.hashCode(); }
        return hashCode;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (_poBox != null) {
            sb.append(_poBox);
        } else {
            sb.append(_streetNumber);
            sb.append('\t');
            sb.append(_street);
            sb.append('\t');
            sb.append(_apt);
        }
        sb.append('\n');
        sb.append(_city);
        sb.append('\t');
        sb.append(_state);
        sb.append('\t');
        sb.append(_zip);
        sb.append('\n');
        sb.append(_country);
        return sb.toString();
    }

    private static boolean equals(final String s, final String t) {
        if (s == t) { return true; }
        if ((s == null) || (t == null)) { return true; }
        return s.equals(t);
    }
}
