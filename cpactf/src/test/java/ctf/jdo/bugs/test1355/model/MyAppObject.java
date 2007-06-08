package jdo.c1355.app;

import java.sql.Timestamp;
import org.apache.log4j.Logger;
import org.exolab.castor.jdo.*;

public class MyAppObject implements TimeStampable {
	protected Long id;
    protected Long createdById;
    protected Long lastModifiedById;
    protected Timestamp createDate;
    protected Timestamp lastModifiedDate;
	protected long timeStamp;
	protected Logger log = Logger.getLogger(getClass());

    public void jdoSetTimeStamp(long timestamp) {
        log.debug("jdoSetTimeStamp - timestamp " + timestamp);
        this.timeStamp = timestamp;
    }

    public long jdoGetTimeStamp() {
        log.debug("jdoGetTimeStamp");
        return timeStamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long value) {
        id = value;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public Long getLastModifiedById() {
        return lastModifiedById;
    }

    public void setLastModifiedById(Long lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
