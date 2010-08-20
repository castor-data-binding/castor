package org.castor.jpa.scenario.enumerated;

import static org.castor.jpa.scenario.enumerated.OrdinalEnum.ZERO;
import static org.castor.jpa.scenario.enumerated.StringEnum.FOO;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class EnumeratedITCase {

    private static final long ID = 1L;

    @Autowired
    private JDOManager jdoManager;

    @Before
    public void initDb() throws Exception {
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
    public void enumsAreConvertedAsExpected() throws Exception {
        final Database db = jdoManager.getDatabase();
        final EnumEntity entityToPersist = new EnumEntity();
        entityToPersist.setId(ID);
        entityToPersist.setStringEnum(FOO);
        entityToPersist.setOrdinalEnum(ZERO);

        db.begin();
        db.create(entityToPersist);
        db.commit();

        db.begin();
        final EnumEntity loadedEntity = db.load(EnumEntity.class, ID);
        db.commit();

        assertNotNull(loadedEntity);
        assertEquals(FOO, loadedEntity.getStringEnum());
        assertEquals(ZERO, loadedEntity.getOrdinalEnum());
    }

}
