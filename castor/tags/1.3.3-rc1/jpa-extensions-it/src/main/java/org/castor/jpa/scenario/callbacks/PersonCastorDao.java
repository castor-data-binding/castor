package org.castor.jpa.scenario.callbacks;

import org.castor.spring.orm.support.CastorDaoSupport;

public class PersonCastorDao extends CastorDaoSupport implements PersonDao {

    public void save(final Person person) {
        this.getCastorTemplate().create(person);
    }

    public Person getById(final long id) {
        return (Person) this.getCastorTemplate().load(Person.class,
                new Long(id));
    }

    public void update(final Person person) {
        this.getCastorTemplate().update(person);
    }

    public void delete(final Person person) {
        this.getCastorTemplate().remove(person);
    }

}
