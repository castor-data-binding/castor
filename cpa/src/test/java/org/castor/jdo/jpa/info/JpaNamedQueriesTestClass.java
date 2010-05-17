package org.castor.jdo.jpa.info;

import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;


@NamedQueries({
  @NamedQuery(name = "name", query = "query"),
  @NamedQuery(name = "name2", query = "query2")
})
public class JpaNamedQueriesTestClass {
	public JpaNamedQueriesTestClass() {
	}
}
