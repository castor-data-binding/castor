package org.castor.jpa.scenario.inheritance;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "inheritance_flower")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Grass extends Plant{
    private long id;
	@Id
	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}
}
