package org.castor.cpa.test.test2860.ManyToMany_NullAccepted;

import java.util.Vector;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

@Ignore
public class Author implements TimeStampable {
    private int id;
    private String name;
    private long timestamp;
    private Vector books;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public long jdoGetTimeStamp() {
		return this.timestamp;
	}

	public void jdoSetTimeStamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setBooks(Vector books) {
		this.books = books;
	}

	public Vector getBooks() {
		return books;
	}
}

