package org.castor.cpa.test.test1044;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

public final class Test1044 extends CPATestCase {
    private static final String DBNAME = "test1044";
    private static final String MAPPING = "/org/castor/cpa/test/test1044/mapping.xml";

    public Test1044(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SQL_SERVER);
    }

    public void testRemoveThenCreateWithoutChanges() throws Exception {
        try {
            Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();
            try {
                BaseObject obj = db.load(BaseObject.class, new Integer(1));
                db.remove(obj);
                db.create(obj);
                obj = db.load(BaseObject.class, new Integer(1));
                assertNotNull(obj);
                db.commit();
                db.close();
            } catch (ClassCastException e) {
                db.rollback();
                db.close();
                fail();
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testRemoveThenCreateWithChanges() throws Exception {
        try {
            Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();
            try {
                BaseObject obj = db.load(BaseObject.class, new Integer(1));
                db.remove(obj);
                obj.setDescription("Description changed");
                db.create(obj);
                obj = db.load(BaseObject.class, new Integer(1));
                assertNotNull(obj);
                db.commit();
                db.close();
            } catch (ClassCastException e) {
                db.rollback();
                db.close();
                fail();
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
            fail();
        }
    }
}
