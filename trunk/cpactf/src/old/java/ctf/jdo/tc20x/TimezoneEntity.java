package ctf.jdo.tc20x;

import java.util.Date;

public final class TimezoneEntity {
    private Integer _id;
    private String _name;
    private Date _startDate;
    private Date _startTime;
    private Date _startTimestamp;
    
    public Integer getId() {
        return _id;
    }
    
    public void setId(final Integer id) {
        _id = id;
    }
    
    public String getName() {
        return _name;
    }
    
    public void setName(final String name) {
        _name = name;
    }

    public Date getStartDate() {
        return _startDate;
    }

    public void setStartDate(final Date startDate) {
        _startDate = startDate;
    }

    public Date getStartTime() {
        return _startTime;
    }

    public void setStartTime(final Date startTime) {
        _startTime = startTime;
    }

    public Date getStartTimestamp() {
        return _startTimestamp;
    }

    public void setStartTimestamp(final Date startTimestamp) {
        _startTimestamp = startTimestamp;
    }
}
