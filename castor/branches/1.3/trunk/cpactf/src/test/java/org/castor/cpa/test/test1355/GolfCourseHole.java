package org.castor.cpa.test.test1355;

public final class GolfCourseHole extends BaseObject {
    private GolfCourseTees _courseTees = new GolfCourseTees();
    private Integer _number;
    private String _name;
    private Integer _par;
    private Integer _yardage;

    public GolfCourseTees getCourseTees() {
        return _courseTees;
    }

    public void setCourseTees(final GolfCourseTees courseTees) {
        _courseTees = courseTees;
    }

    public Integer getNumber() {
        return _number;
    }
    
    public void setNumber(final Integer number) {
        _number = number;
    }

    public String getName() {
        return _name;
    }
    
    public void setName(final String name) {
        _name = name;
    }

    public Integer getPar() {
        return _par;
    }
    
    public void setPar(final Integer par) {
        _par = par;
    }

    public Integer getYardage() {
        return _yardage;
    }
    
    public void setYardage(final Integer yardage) {
        _yardage = yardage;
    }
}
