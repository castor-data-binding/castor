/**
 * Copyright(c) Intalio 2001  All rights reserved.
 */
package ctf.jdo.bugs.soak.model;

public final class Phone implements java.io.Serializable {
    private static final long serialVersionUID = 163281644008362093L;

    private short _countryCode;
    private short _areaCode;
    private long  _number;

    public Phone(final int countryCode, final int areaCode, final long number) {
        _countryCode = (short) countryCode;
        _areaCode = (short) areaCode;
        _number = number;
    }

    public boolean equals(final Object o) {
        if (!(o instanceof Phone)) { return false; }

        Phone pho = (Phone) o;
        return _countryCode  == pho._countryCode
            && _areaCode     == pho._areaCode
            && _number       == pho._number;
    }

    public int hashCode() {
        int hashCode = 0;
        hashCode ^= new Short(_countryCode).hashCode();
        hashCode ^= new Short(_areaCode).hashCode();
        hashCode ^= new Long(_number).hashCode();
        return hashCode;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (_countryCode != 0) {
            sb.append(_countryCode);
        }
        sb.append('(');
        if (_areaCode != 0) {
            sb.append(_areaCode);
        } else {
            sb.append("   ");
        }
        sb.append(')');
        return sb.toString();
    }
}
