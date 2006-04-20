

package jdo;

public class TestLazyNToNB {
    private String _id;
    private int _status;

    public void setIdB( String id ) {
        _id = id;
    }
    public String getIdB() {
        return _id;
    }
    public void setStatusB( int status ) {
        _status = status;
    }
    public int getStatusB() {
        return _status;
    }
    public boolean equals(Object otherObj) {
        try {
            TestLazyNToNB lb = (TestLazyNToNB) otherObj;
            return (lb.getIdB() == _id);
        } catch (ClassCastException e) {
            return false;
        }
    }
}
