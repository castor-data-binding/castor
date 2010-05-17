package org.castor.jpa.scenario.named_queries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TransactionConfiguration(transactionManager = "transactionManager",
    defaultRollback = true)
public class NamedQueriesITCase {

    public final Log LOG = LogFactory.getLog(getClass());

    @Autowired
    protected JDOManager jdoManager;

    @Autowired
    private EmployeeDao employeeDao;

    private static final String NAME = "Hans Wurst";
    private static final String ADDRESS = "1313 Mocking Lane";

    @Before
    public void initDb() throws PersistenceException {
        Database db = jdoManager.getDatabase();
        assertNotNull(db);
    }

    @After
    public void cleanDb() throws PersistenceException {
        Database db = jdoManager.getDatabase();
		if (db.isActive()) {
            db.rollback();
        }
        db.close();
    }

    @Test
    public void executeNamedQueryDirectly() throws Exception {
    	Database db = jdoManager.getDatabase();

        final Employee employeeToPersist = new Employee();
        employeeToPersist.setId(1L);
        employeeToPersist.setName(NAME);
        employeeToPersist.setAddress(ADDRESS);

        db.begin();
        db.create(employeeToPersist);
        db.commit();

        db.begin();
        final OQLQuery query = db.getNamedQuery("findEmployeeByName");
        query.bind(NAME);
        assertNotNull(query);
        final QueryResults queryResults = query.execute();
        assertNotNull(queryResults);
        final Employee queriedEmployee = (Employee) queryResults.next();
        queryResults.close();
        db.commit();

        assertNotNull(queriedEmployee);
        assertEquals(NAME, queriedEmployee.getName());
    }

    @Test
    public void executeNamedQueryViaDao() throws Exception {
    	Database db = jdoManager.getDatabase();

        final Employee employeeToPersist = new Employee();
        employeeToPersist.setId(2L);
        employeeToPersist.setName(NAME);
        employeeToPersist.setAddress(ADDRESS);

        db.begin();
        db.create(employeeToPersist);
        db.commit();

        final Employee queriedEmployee = employeeDao.getByAddress(ADDRESS);
        assertNotNull(queriedEmployee);
        assertEquals(ADDRESS, queriedEmployee.getAddress());
    }

}
