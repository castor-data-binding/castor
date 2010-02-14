package org.castor.cascading.one_to_many_bi;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

@Ignore
public class Book implements TimeStampable {
    private int id;
    private long timestamp;
    private String name;
    private Author author;

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
    
    public void setAuthor(Author author) {
	this.author = author;
    }

    public Author getAuthor() {
	return author;
    }
}
