package org.castor.jpa.scenario.inheritance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
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
public class InheritanceITCase {

    public final Log LOG = LogFactory.getLog(getClass());

    @Autowired
    protected JDOManager jdoManager;
    private Database db;

    private static final long ID = 1L;
    private static final String NAME = "Generic Test Plant";
    private static final int HEIGHT = 500;

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
            db.remove(db.load(Tree.class, ID));
        } catch (Exception e) {
        }
        db.commit();
    }

    private <T extends Plant> void createAndPersistPlant(Class<T> c)
            throws Exception {
        T plant = c.newInstance();
        plant.setId(ID);
        plant.setName(NAME);
        db.begin();
        db.create(plant);
        db.commit();
    }

    @Test
    public void joinedInheritanceWorks() throws Exception {
        cleanDBIfNeeded();
        createAndPersistPlant(Tree.class);

        db.begin();
        Tree updateTree = db.load(Tree.class, ID);
        assertNotNull(updateTree);
        updateTree.setHeight(HEIGHT);
        db.commit();

        db.begin();
        Tree queriedTree = db.load(Tree.class, ID);
        db.commit();
        assertNotNull(queriedTree);
        LOG.warn(queriedTree.getClass().getCanonicalName()+queriedTree.getId()+queriedTree.getName()+queriedTree.getHeight());
        assertEquals(NAME, queriedTree.getName());
        assertEquals(HEIGHT, queriedTree.getHeight());
    }

    @Test(expected = ClassNotPersistenceCapableException.class)
    public void defaultInheritanceNotSupported() throws Exception {
        cleanDBIfNeeded();
        createAndPersistPlant(Flower.class);

        db.begin();
        Flower queriedFlower = db.load(Flower.class, ID);
        db.commit();
        assertNotNull(queriedFlower);
        assertEquals(NAME, queriedFlower.getName());
    }

    @Test(expected = ClassNotPersistenceCapableException.class)
    public void nonJoinedInheritanceNotSupported() throws Exception {
        cleanDBIfNeeded();
        createAndPersistPlant(Grass.class);

        db.begin();
        Grass queriedGrass = db.load(Grass.class, ID);
        db.commit();
        assertNotNull(queriedGrass);
        assertEquals(NAME, queriedGrass.getName());
    }
}
