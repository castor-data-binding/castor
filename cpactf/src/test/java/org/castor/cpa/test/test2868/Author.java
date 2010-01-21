package org.castor.cpa.test.test2868;

import java.util.Collection;

import org.junit.Ignore;

@Ignore
public class Author {
    private int id;
    private Collection books;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setBooks(Collection books) {
    	this.books = books;
        }

        public Collection getBooks() {
    	return books;
        }
}

