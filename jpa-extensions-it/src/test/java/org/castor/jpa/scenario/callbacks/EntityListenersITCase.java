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

    private static final Log LOG = LogFactory
            .getLog(EntityListenersITCase.class);

    @Autowired
    protected JDOManager jdoManager;

    private static final long ID = 1L;
    private List<String> desiredCallbacksExecutionOrder = new ArrayList<String>();

    private void initEntityListenerCallbacksTest() throws Exception {
        desiredCallbacksExecutionOrder.clear();
        desiredCallbacksExecutionOrder.add("postPersistPetListener");
        desiredCallbacksExecutionOrder.add("postPersistDogListener");
        desiredCallbacksExecutionOrder.add("postPersistDogListener2");
        desiredCallbacksExecutionOrder
                .add("postPersistGoldenRetrieverListener");
        desiredCallbacksExecutionOrder.add("postPersistAnimal2");
        desiredCallbacksExecutionOrder.add("postPersistPet");
        desiredCallbacksExecutionOrder
                .add("postPersistAnimalFromGoldenRetriever");
    }

    private void initExcludeListenerCallbacksTest() throws Exception {
        desiredCallbacksExecutionOrder.add("postPersistFoo");
        desiredCallbacksExecutionOrder.add("postPersistBar");
    }

    @Before
    public void init() throws PersistenceException {
        CallbacksExecutionOrderMemory.getOrderedCallbackNames().clear();
        desiredCallbacksExecutionOrder.clear();
        final Database db = jdoManager.getDatabase();
        assertNotNull(db);
    }

    @After
    public void cleanup() throws PersistenceException {
        final Database db = jdoManager.getDatabase();
        if (db.isActive()) {
            db.rollback();
        }
        db.close();
    }

    @Test
    public void entityListenerCallbacks() throws Exception {
        initEntityListenerCallbacksTest();
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
        assertEquals(desiredCallbacksExecutionOrder,
                CallbacksExecutionOrderMemory.getOrderedCallbackNames());
    }

    @Test
    public void excludeListenerCallbacks() throws Exception {
        initExcludeListenerCallbacksTest();
        final Database db = jdoManager.getDatabase();
        final Bar barToPersist = new Bar();
        barToPersist.setId(ID);
        db.begin();
        db.create(barToPersist);
        db.commit();
        db.begin();
        final Bar loadedBar = db.load(Bar.class, ID);
        db.commit();
        assertEquals(ID, loadedBar.getId());
        assertEquals(desiredCallbacksExecutionOrder,
                CallbacksExecutionOrderMemory.getOrderedCallbackNames());
    }

}
