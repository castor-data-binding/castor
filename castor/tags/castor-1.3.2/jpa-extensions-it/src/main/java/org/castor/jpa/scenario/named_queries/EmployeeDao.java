package org.castor.jpa.scenario.named_queries;

public interface EmployeeDao {

	void save(Employee employee);

	Employee get(long id);

	void delete(Employee employee);

	void update(Employee employee);

	Employee getByName(String name);

  Employee getByAddress(String address);

}
