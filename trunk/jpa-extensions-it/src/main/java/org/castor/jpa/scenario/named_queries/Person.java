package org.castor.jpa.scenario.named_queries;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "NamedQueries_person")
@NamedQuery(name = "findPersonByName", query =
        "SELECT p FROM org.castor.jpa.scenario.named_queries.Person p WHERE p.name = $1"
)
public class Person {

    private long id;
    private String name;

	@Id
	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
