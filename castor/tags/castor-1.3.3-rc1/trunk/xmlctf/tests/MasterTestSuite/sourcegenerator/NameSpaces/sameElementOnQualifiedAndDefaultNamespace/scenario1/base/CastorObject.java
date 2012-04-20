package base;

import java.io.Serializable;

public class CastorObject implements Serializable {

    private static final long serialVersionUID = -8030501240931699130L;

    /**
     * Must be overriden because generated subclasses call following method :
     * ...
     * if (super.equals(obj)==false) return false;
     * ...
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof CastorObject;
    }

    @Override
    public int hashCode() {
        return 42;
    }
}
