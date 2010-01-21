package org.castor.cpa.test.test2860.ManyToMany_NullAccepted;

import java.util.Vector;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

@Ignore
public class Book implements TimeStampable {
	
    private int id;
    private Vector authors;
    private String name;
    private long timestamp;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
	public void setAuthors(Vector authors) {
		this.authors = authors;
	}
	public Vector getAuthors() {
		return authors;
	}
	public long jdoGetTimeStamp() {
		return this.timestamp;
	}
	public void jdoSetTimeStamp(long timestamp) {
		this.timestamp = timestamp;	
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public long getTimestamp() {
		return timestamp;
	}
}
