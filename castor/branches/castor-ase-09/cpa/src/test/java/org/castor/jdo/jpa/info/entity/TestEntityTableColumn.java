package org.castor.jdo.jpa.info.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.junit.Ignore;

@Entity
@Ignore
public class TestEntityTableColumn {
    private int _id;
    private String _data;
    
    @Id
    public int getId() {
        return _id;
    }
    public void setId(final int id) {
        this._id = id;
    }
    
    @Column
    public String getData() {
        return _data;
    }
    public void setData(final String data) {
        this._data = data;
    }
    
    
}
