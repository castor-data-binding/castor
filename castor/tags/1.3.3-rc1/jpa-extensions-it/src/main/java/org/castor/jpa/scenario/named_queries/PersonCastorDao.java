package org.castor.jpa.scenario.named_queries;

import org.castor.spring.orm.CastorCallback;
import org.castor.spring.orm.support.CastorDaoSupport;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

public class PersonCastorDao extends CastorDaoSupport implements PersonDao {

	public void save(final Person person) {
		this.getCastorTemplate().create(person);
	}

	public Person get(final long id) {
		return (Person) this.getCastorTemplate().load(Person.class,
				new Long(id));
	}

	public void update(final Person person) {
		this.getCastorTemplate().update(person);
	}

	public void delete(final Person person) {
		this.getCastorTemplate().remove(person);
	}

	public Person getByName(final String name) {
		return (Person) this.getCastorTemplate().execute(new CastorCallback() {
			public Object doInCastor(final Database db)
					throws PersistenceException {
				db.begin();
				final OQLQuery query = db.getNamedQuery("findPersonByName");
				query.bind(name);
				final QueryResults queryResults = query.execute();
				final Person queriedPerson = (Person) queryResults.next();
				queryResults.close();
				db.commit();
				return queriedPerson;
			}
		});
	}

}
