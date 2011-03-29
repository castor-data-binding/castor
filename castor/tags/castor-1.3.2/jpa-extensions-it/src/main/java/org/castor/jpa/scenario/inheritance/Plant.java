package org.castor.jpa.scenario.inheritance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "inheritance_plant")
public class Plant {
    protected  long id;
    protected String name;

	@Id
	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

    @Column
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
