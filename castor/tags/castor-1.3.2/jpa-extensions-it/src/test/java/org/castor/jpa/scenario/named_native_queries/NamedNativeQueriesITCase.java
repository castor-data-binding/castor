package org.castor.jpa.scenario.named_native_queries;

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
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class NamedNativeQueriesITCase {

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
        db = jdoManager.getDatabase();

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
    public void existingAndvalidQueriesReturnMatchingEntityInstances()
            throws Exception {
        OQLQuery query;
        QueryResults results;
        StudentWithValidQueries queriedStudent;

        cleanDBIfNeeded();
        createAndPersistStudent(StudentWithValidQueries.class);

        db.begin();
        query = db.getNamedQuery("nativeFetchAllStudents");
        assertNotNull(query);
        results = query.execute();
        assertNotNull(results);
        queriedStudent = (StudentWithValidQueries) results
                .next();
        results.close();
        db.commit();

        assertNotNull(queriedStudent);
        assertEquals(FIRSTNAME, queriedStudent.getFirstName());

        queriedStudent = null;

        db.begin();
        query = db.getNamedQuery("nativeSelectMax");
        assertNotNull(query);
        results = query.execute();
        assertNotNull(results);
        queriedStudent = (StudentWithValidQueries) results
                .next();
        results.close();
        db.commit();

        assertNotNull(queriedStudent);
        assertEquals(FIRSTNAME, queriedStudent.getFirstName());
    }

    @Test
    public void existingButEmptyNamedNativeQueriesAnnotationDoesNotCauseTrouble() throws Exception{
        cleanDBIfNeeded();
        createAndPersistStudent(StudentWithEmptyNNQueriesAnnotation.class);

        db.begin();
        final StudentWithEmptyNNQueriesAnnotation loadedStudent =
                db.load(StudentWithEmptyNNQueriesAnnotation.class, ID);
        assertEquals(FIRSTNAME, loadedStudent.getFirstName());
        db.commit();
    }

    /*
     * Tests for possible query failures see NamedNativeQueryITCase
     */
    }
