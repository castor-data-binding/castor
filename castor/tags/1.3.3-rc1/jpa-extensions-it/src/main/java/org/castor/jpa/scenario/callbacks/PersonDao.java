package org.castor.jpa.scenario.callbacks;

public interface PersonDao {

    void save(Person person);

    Person getById(long id);

    void delete(Person person);

    void update(Person person);

}
