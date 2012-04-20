package org.castor.jpa.scenario.named_queries;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

@Entity
@Table(name = "NamedQueries_employee")
@NamedQueries({
  @NamedQuery(name = "findEmployeeByName", query =
        "SELECT p FROM org.castor.jpa.scenario.named_queries.Employee p WHERE p.name = $1"
    ),
  @NamedQuery(name = "findEmployeeByAddress", query =
        "SELECT p FROM org.castor.jpa.scenario.named_queries.Employee p WHERE p.address = $1"
    )
})
public class Employee {

    private long id;
    private String name;
    private String address;

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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
