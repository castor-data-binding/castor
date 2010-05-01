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
@TransactionConfiguration(transactionManager = "transactionManager",  defaultRollback = true)
public class NamedQueriesITCase {

    public final Log LOG = LogFactory.getLog(getClass());

    @Autowired
    protected JDOManager jdoManager;

    @Autowired
    private PersonDao personDao;

    private static final String NAME = "Hans Wurst";

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
    	
        final Person personToPersist = new Person();
        personToPersist.setId(1L);
        personToPersist.setName(NAME);
        
        db.begin();
        db.create(personToPersist);
        db.commit();
        
        db.begin();
        final OQLQuery query = db.getNamedQuery("findPersonByName");
        query.bind(NAME);
        assertNotNull(query);
        final QueryResults queryResults = query.execute();
        assertNotNull(queryResults);
        final Person queriedPerson = (Person) queryResults.next();
        queryResults.close();
        db.commit();
        
        assertNotNull(queriedPerson);
        assertEquals(NAME, queriedPerson.getName());
    }

    @Test
    public void executeNamedQueryViaDao() throws Exception {
    	Database db = jdoManager.getDatabase();
    	
        final Person personToPersist = new Person();
        personToPersist.setId(2L);
        personToPersist.setName(NAME);
        
        db.begin();
        db.create(personToPersist);
        db.commit();
        
        final Person queriedPerson = personDao.getByName(NAME);
        assertNotNull(queriedPerson);
        assertEquals(NAME, queriedPerson.getName());
    }

}
