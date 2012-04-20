package org.castor.jpa.scenario.cascading;

import static junit.framework.Assert.assertEquals;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.sql.Statement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class CascadingITCase {

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    protected JDOManager jdoManager;

    private static final long ID = 1L;

    @Before
    public void initDb() throws Exception {
        final Database db = jdoManager.getDatabase();
        assertNotNull(db);

        db.begin();
        final Statement statement = db.getJdbcConnection().createStatement();
        statement.executeUpdate("DELETE FROM Cascading_parent");
        statement.executeUpdate("DELETE FROM Cascading_child");
        db.commit();
    }

    @After
    public void cleanDb() throws PersistenceException {
        final Database db = jdoManager.getDatabase();
        if (db.isActive()) {
            db.rollback();
        }
        db.close();
    }

    @Test
    public void oneToOnePersistenceCascadesAsExpected() throws Exception {
        final Database db = jdoManager.getDatabase();
        final Parent parent = new Parent();
        parent.setId(ID);
        final Child child = new Child();
        child.setId(ID);
        parent.setChild(child);

        db.begin();
        db.create(parent);
        db.commit();

        db.begin();
        final Parent loadedParent = db.load(Parent.class, ID);
        db.commit();

        db.begin();
        final Child loadedChild = db.load(Child.class, ID);
        db.commit();

        assertNotNull(loadedParent);
        assertNotNull(loadedChild);
        assertNotNull(loadedParent.getChild());
        assertEquals(loadedChild.getId(), loadedParent.getChild().getId());
    }

    @Test
    public void oneToOneRemovalCascadesAsExcpected() throws Exception {
        final Database db = jdoManager.getDatabase();
        final Parent parent = new Parent();
        parent.setId(ID);
        final Child child = new Child();
        child.setId(ID);
        parent.setChild(child);

        db.begin();
        db.create(parent);
        db.commit();

        db.begin();
        final Parent loadedParent = db.load(Parent.class, ID);
        db.remove(loadedParent);
        db.commit();

        try {
            db.begin();
            db.load(Parent.class, ID);
            fail("Loading parent should fail because it was deleted.");
        } catch (ObjectNotFoundException ex) {
            log.debug("Loading parent from DB failed as expected.");
        } finally {
            db.commit();
        }

        try {
            db.begin();
            db.load(Child.class, ID);
            fail("Loading child should fail because of cascading.");
        } catch (ObjectNotFoundException ex) {
            log.debug("Loading child from DB failed as expected.");
        } finally {
            db.commit();
        }
    }

}
