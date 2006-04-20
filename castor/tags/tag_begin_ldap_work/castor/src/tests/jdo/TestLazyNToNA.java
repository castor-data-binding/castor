

package jdo;

import java.util.Collection;

public class TestLazyNToNA {
    private String _id;
    private int _status;
    private Collection _refs;

    public void setIdA( String id ) {
        _id = id;
    }
    public String getIdA() {
        return _id;
    }
    public void setStatusA( int status ) {
        _status = status;
    }
    public int getStatusA() {
        return _status;
    }
    public void setRefs( Collection refs ) {
        _refs = refs;
    }
    public Collection getRefs() {
        return _refs;
    }
    public boolean equals(Object otherObj) {
        try {
            TestLazyNToNA la = (TestLazyNToNA) otherObj;
            return (la.getIdA() == _id);
        } catch (ClassCastException e) {
            return false;
        }
    }
}
