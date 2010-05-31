package org.castor.jpa.scenario.callbacks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class EntityListenersITCase {

    private static final Log LOG = LogFactory.getLog(EntityListenersITCase.class);
    
    @Autowired
    protected JDOManager jdoManager;

    private static final long ID = 1L;
    private static final List<String> DESIRED_CALLBACKS_EXECUTION_ORDER = new ArrayList<String>();

    @BeforeClass
    public static void initCallbackNamesList() throws Exception {
        DESIRED_CALLBACKS_EXECUTION_ORDER.add("postPersistPetListener");
        DESIRED_CALLBACKS_EXECUTION_ORDER.add("postPersistDogListener");
        DESIRED_CALLBACKS_EXECUTION_ORDER.add("postPersistDogListener2");
        DESIRED_CALLBACKS_EXECUTION_ORDER
                .add("postPersistGoldenRetrieverListener");
        DESIRED_CALLBACKS_EXECUTION_ORDER.add("postPersistAnimal2");
        DESIRED_CALLBACKS_EXECUTION_ORDER.add("postPersistPet");
        DESIRED_CALLBACKS_EXECUTION_ORDER
                .add("postPersistAnimalFromGoldenRetriever");
    }

    @Before
    public void initDb() throws PersistenceException {
        final Database db = jdoManager.getDatabase();
        assertNotNull(db);
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
    public void postPersistCallbacks() throws Exception {
        final Database db = jdoManager.getDatabase();
        final Dog dogToPersist = new GoldenRetriever();
        dogToPersist.setId(ID);
        db.begin();
        db.create(dogToPersist);
        db.commit();
        db.begin();
        final GoldenRetriever loadedDog = db.load(GoldenRetriever.class, ID);
        db.commit();
        assertEquals(ID, loadedDog.getId());
        assertEquals(DESIRED_CALLBACKS_EXECUTION_ORDER,
                CallbacksExecutionOrderMemory.getOrderedCallbackNames());
    }

}
