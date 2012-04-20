package org.castor.jpa.scenario.named_native_queries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
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
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class NamedNativeQueryITCase {

    public final Log LOG = LogFactory.getLog(getClass());

    @Autowired
    protected JDOManager jdoManager;
    private Database db;

    private static final long ID = 1L;
    private static final String FIRSTNAME = "Max";
    private static final String LASTNAME = "Mustermann";

    @Before
    public void initDb() throws PersistenceException {
        db = jdoManager.getDatabase();
        assertNotNull(db);
    }

    @After
    public void cleanDb() throws PersistenceException {
        if (db.isActive()) {
            db.rollback();
        }
        db.close();
    }

    private void cleanDBIfNeeded() throws Exception {

        db.begin();
        try {
            db.remove(db.load(StudentWithEmptyQuery.class, ID));
        } catch (Exception e) {
        }
        db.commit();
    }

    private <T extends Student> void createAndPersistStudent(Class<T> c)
            throws Exception {
        T student = c.newInstance();
        student.setId(ID);
        student.setFirstName(FIRSTNAME);
        student.setLastName(LASTNAME);

        db.begin();
        db.create(student);
        db.commit();
    }

    @Test
    public void existingAndvalidQueryReturnsMatchingEntityInstances()
            throws Exception {
        cleanDBIfNeeded();
        createAndPersistStudent(StudentWithValidQuery.class);

        db.begin();
        final OQLQuery query = db.getNamedQuery("nativeSelectAllStudents");
        assertNotNull(query);
        final QueryResults queryResults = query.execute();
        assertNotNull(queryResults);
        final StudentWithValidQuery queriedStudent = (StudentWithValidQuery) queryResults
                .next();
        queryResults.close();
        db.commit();

        assertNotNull(queriedStudent);
        assertEquals(FIRSTNAME, queriedStudent.getFirstName());
    }

    @Test(expected = PersistenceException.class)
    public void existingAndEmptyQueryThrowsSQLException() throws Exception {
        cleanDBIfNeeded();
        createAndPersistStudent(StudentWithValidQuery.class);

        db.begin();
        final OQLQuery query = db.getNamedQuery("emptyQuery");
        assertNotNull(query);
        query.execute();
        fail("Should have thrown a SQL Exception.");
        db.commit();
    }

    @Test(expected = QueryException.class)
    public void existingAndInvalidQueryThrowsSQLException() throws Exception {
        cleanDBIfNeeded();
        createAndPersistStudent(StudentWithInvalidQuery.class);

        db.begin();
        final OQLQuery query = db.getNamedQuery("invalidQuery");
        assertNotNull(query);
        query.execute();
        fail("Should have thrown a SQL Exception.");
        db.commit();
    }

    @Test(expected = QueryException.class)
    public void nonExistingQueryThrowsQueryException() throws Exception {
        cleanDBIfNeeded();
        createAndPersistStudent(StudentWithValidQuery.class);

        db.begin();
        final OQLQuery query = db.getNamedQuery("no query with this name");
        assertNotNull(query);
        query.execute();
        db.commit();
    }
}
