package org.castor.jpa.scenario.named_queries;

import org.castor.spring.orm.CastorCallback;
import org.castor.spring.orm.support.CastorDaoSupport;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

public class EmployeeCastorDao extends CastorDaoSupport implements EmployeeDao {

	public void save(final Employee employee) {
		this.getCastorTemplate().create(employee);
	}

	public Employee get(final long id) {
		return (Employee) this.getCastorTemplate().load(Employee.class,
				new Long(id));
	}

	public void update(final Employee employee) {
		this.getCastorTemplate().update(employee);
	}

	public void delete(final Employee employee) {
		this.getCastorTemplate().remove(employee);
	}

  private Employee getEmployeeFromQuery(final String queryName, final String value) {
		return (Employee) this.getCastorTemplate().execute(new CastorCallback() {
			public Object doInCastor(final Database db)
					throws PersistenceException {
				db.begin();
				final OQLQuery query = db.getNamedQuery(queryName);
				query.bind(value);
				final QueryResults queryResults = query.execute();
				final Employee queriedEmployee = (Employee) queryResults.next();
				queryResults.close();
				db.commit();
				return queriedEmployee;
			}
		});
	}

	public Employee getByName(final String name) {
		return getEmployeeFromQuery("findEmployeeByName", name);
	}

	public Employee getByAddress(final String address) {
    return getEmployeeFromQuery("findEmployeeByAddress", address);
  }
}
