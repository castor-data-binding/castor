package org.castor.cpa.test.test1355;

import java.util.ArrayList;
import java.util.Collection;

public final class GolfCourse extends BaseObject {
    private String _name;
    private Integer _holes;
    
    //Marked as transient because the castor persistent collection is not serializeable
    private transient Collection _tees = new ArrayList();

    private City _city = new City();
    
    public String getName() {
        return _name;
    }

    public void setName(final String name) {
        _name = name;
    }

    public Integer getHoles() {
        return _holes;
    }
    
    public void setHoles(final Integer holes) {
        _holes = holes;
    }

    public Collection getTees() {
        return _tees;
    }
    
    public void setTees(final Collection tees) {
        _tees = tees;
    }

    public City getCity() {
        return _city;
    }
    
    public void setCity(final City city) {
        _city = city;
    }
}

