package org.castor.jpa.scenario.inheritance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Table;

@Entity
@Table(name = "inheritance_flower")
@Inheritance
public class Flower extends Plant{
    private long id;
    protected String color;

    @Column
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
	@Id
	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

}
