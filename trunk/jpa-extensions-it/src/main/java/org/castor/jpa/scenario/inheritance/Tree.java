package org.castor.jpa.scenario.inheritance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "inheritance_tree")
@Inheritance(strategy = InheritanceType.JOINED)
public class Tree extends Plant{
    protected int height;
    private long id;
    
	@Id
	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

    @Column
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
