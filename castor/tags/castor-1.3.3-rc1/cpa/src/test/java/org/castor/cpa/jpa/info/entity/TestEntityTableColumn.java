package org.castor.cpa.jpa.info.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.junit.Ignore;

@Entity
@Ignore
public final class TestEntityTableColumn {
    private int _id;
    private String _data;
    
    @Id
    public int getId() {
        return _id;
    }
    public void setId(final int id) {
        _id = id;
    }
    
    @Column
    public String getData() {
        return _data;
    }
    public void setData(final String data) {
        _data = data;
    }
}
