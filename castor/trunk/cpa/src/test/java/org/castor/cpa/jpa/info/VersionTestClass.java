package org.castor.cpa.jpa.info;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public final class VersionTestClass {
    private long _id;
    private String _name;
    private long _version;
    
    @Id
    public long getId() {
        return _id;
    }
    
    public void setId(final long id) {
        _id = id;
    }
    
    public String getName() {
        return _name;
    }
    
    public void setName(final String name) {
        _name = name;
    }
    
    @Version
    public long getVersion() {
        return _version;
    }
    
    public void setVersion(final long version) {
        _version = version;
    }
}
