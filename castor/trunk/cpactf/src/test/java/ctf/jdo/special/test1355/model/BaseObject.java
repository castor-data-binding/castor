package ctf.jdo.special.test1355.model;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.TimeStampable;

public class BaseObject implements TimeStampable {
    private static final Log LOG = LogFactory.getLog(BaseObject.class);
    
    private Long _id;
    private Long _createdById;
    private Long _lastModifiedById;
    private Timestamp _createDate;
    private Timestamp _lastModifiedDate;
    private long _timeStamp;

    public final Long getId() {
        return _id;
    }

    public final void setId(final Long id) {
        _id = id;
    }

    public final Long getCreatedById() {
        return _createdById;
    }

    public final void setCreatedById(final Long createdById) {
        _createdById = createdById;
    }

    public final Long getLastModifiedById() {
        return _lastModifiedById;
    }

    public final void setLastModifiedById(final Long lastModifiedById) {
        _lastModifiedById = lastModifiedById;
    }

    public final Timestamp getCreateDate() {
        return _createDate;
    }

    public final void setCreateDate(final Timestamp createDate) {
        _createDate = createDate;
    }

    public final Timestamp getLastModifiedDate() {
        return _lastModifiedDate;
    }

    public final void setLastModifiedDate(final Timestamp lastModifiedDate) {
        _lastModifiedDate = lastModifiedDate;
    }
    
    public final long jdoGetTimeStamp() {
        LOG.debug("jdoGetTimeStamp");
        return _timeStamp;
    }

    public final void jdoSetTimeStamp(final long timestamp) {
        LOG.debug("jdoSetTimeStamp - timestamp " + timestamp);
        _timeStamp = timestamp;
    }
}
