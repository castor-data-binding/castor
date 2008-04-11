package org.castor.cpa.test.test1355;

import java.util.ArrayList;
import java.util.Collection;

public final class GolfCourseTees extends BaseObject {
    private GolfCourse _course = new GolfCourse();
    private String _name;
    private String _color;
    private Integer _mensSlope;
    private Double _mensRating;
    private Integer _womensSlope;
    private Double _womensRating;
    private Integer _yardage;

    //Marked as transient because the castor persistent collection is not serializeable
    private transient Collection _holes = new ArrayList();

    public GolfCourse getCourse() {
        return _course;
    }
    
    public void setCourse(final GolfCourse course) {
        _course = course;
    }

    public String getName() {
        return _name;
    }
    
    public void setName(final String name) {
        _name = name;
    }

    public String getColor() {
        return _color;
    }

    public void setColor(final String color) {
        _color = color;
    }

    public Integer getMensSlope() {
        return _mensSlope;
    }

    public void setMensSlope(final Integer mensSlope) {
        _mensSlope = mensSlope;
    }

    public Double getMensRating() {
        return _mensRating;
    }
    
    public void setMensRating(final Double mensRating) {
        _mensRating = mensRating;
    }

    public Integer getWomensSlope() {
        return _womensSlope;
    }
    
    public void setWomensSlope(final Integer womensSlope) {
        _womensSlope = womensSlope;
    }

    public Double getWomensRating() {
        return _womensRating;
    }
    
    public void setWomensRating(final Double womensRating) {
        _womensRating = womensRating;
    }

    public Integer getYardage() {
        return _yardage;
    }
    
    public void setYardage(final Integer yardage) {
        _yardage = yardage;
    }

    public Collection getHoles() {
        return _holes;
    }
    
    public void setHoles(final Collection holes) {
        _holes = holes;
    }
}

