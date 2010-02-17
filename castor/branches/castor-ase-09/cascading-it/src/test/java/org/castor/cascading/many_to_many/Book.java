package org.castor.cascading.many_to_many;

import java.util.Collection;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

@Ignore
public class Book implements TimeStampable {
    private int id;
    private Collection<Author> authors;
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

    public void setAuthors(Collection<Author> authors) {
	this.authors = authors;
    }

    public Collection<Author> getAuthors() {
	return authors;
    }
}
