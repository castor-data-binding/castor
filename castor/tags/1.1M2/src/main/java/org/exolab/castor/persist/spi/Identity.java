package org.exolab.castor.persist.spi;

public final class Identity {
    private final Object[] _all;

    private int _hashCode;

    public Identity(final Object o) {
        if (o instanceof Complex) {
            Complex complex = (Complex) o;
            _all = new Object[complex.size()];
            _hashCode = 0;
            for (int i = 0; i < complex.size(); i++) {
                _all[i] = complex.get(i);
                _hashCode += ((_all[i] == null) ? 0 : _all[i].hashCode());
            }
        } else if (o instanceof Identity) {
            Identity identity = (Identity) o;
            _all = new Object[identity.size()];
            _hashCode = 0;
            for (int i = 0; i < identity.size(); i++) {
                _all[i] = identity.get(i);
                _hashCode += ((_all[i] == null) ? 0 : _all[i].hashCode());
            }
        } else if (o instanceof Object[]) {
            Object[] array = (Object[]) o;
            _all = new Object[array.length];
            _hashCode = 0;
            for (int i = 0; i < array.length; i++) {
                _all[i] = array[i];
                _hashCode += ((array[i] == null) ? 0 : array[i].hashCode());
            }
        } else {
            _all = new Object[] { o };
            _hashCode = o.hashCode();
        }
    }

    public Identity(final Object[] array) {
        _all = new Object[array.length];
        _hashCode = 0;
        for (int i = 0; i < array.length; i++) {
            _all[i] = array[i];
            _hashCode += ((array[i] == null) ? 0 : array[i].hashCode());
        }
    }

    public Identity(final Object o1, final Object o2) {
        _all = new Object[] { o1, o2 };
        _hashCode = ((o1 == null) ? 0 : o1.hashCode());
        _hashCode += ((o2 == null) ? 0 : o2.hashCode());
    }

    public int size() { return _all.length; }

    public Object get(final int i) { return _all[i]; }
    
    public boolean equals(final Object other) {
        if (other == null) { return false; }
        if (!(other instanceof Identity)) { return false; }

        Identity id = (Identity) other;
        if (_all.length != id.size()) { return false; }

        for (int i = 0; i < _all.length; i++ ) {
            if (_all[i] == null) {
                if (id._all[i] != null) { return false; }
            } else if (!_all[i].equals(id._all[i])) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() { return _hashCode; }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<");
        for(int i = 0; i < _all.length; i++) {
        	if (i != 0) { sb.append(","); }
        	Object obj = _all[i];
            sb.append(obj);
            if (obj != null) {
                sb.append("(").append(obj.hashCode()).append(")");
            } else {
                sb.append ("(N/A)");
            }
        }
        sb.append(">");
        return sb.toString();
    }
}

