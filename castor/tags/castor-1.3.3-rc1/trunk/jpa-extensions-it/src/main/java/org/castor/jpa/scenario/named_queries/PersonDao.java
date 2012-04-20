package org.castor.jpa.scenario.named_queries;

public interface PersonDao {

	void save(Person person);

	Person get(long id);

	void delete(Person person);

	void update(Person person);

	Person getByName(String name);

}
