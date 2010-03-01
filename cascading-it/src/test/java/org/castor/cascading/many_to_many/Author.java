package org.castor.cascading.many_to_many;

import java.util.Collection;

import org.exolab.castor.jdo.TimeStampable;
import org.junit.Ignore;

@Ignore
public class Author implements TimeStampable {
	private int id;
	private long timestamp;
	private String name;
	private Collection<Book> books;

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

	public void setBooks(Collection<Book> books) {
		this.books = books;
	}

	public Collection<Book> getBooks() {
		return books;
	}
}
