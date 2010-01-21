package org.castor.cpa.test.test2860.ManyToOne_NullAccepted;

import java.util.Collection;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

@Ignore
public class Author implements TimeStampable {
    private int id;
    private long timestamp;
    private String name;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public long jdoGetTimeStamp() {
	return timestamp;
    }

    public void jdoSetTimeStamp(long timestamp) {
	this.timestamp = timestamp;
    }

    public long getTimestamp() {
	return timestamp;
    }

    public void setTimestamp(long timestamp) {
	this.timestamp = timestamp;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }
}

