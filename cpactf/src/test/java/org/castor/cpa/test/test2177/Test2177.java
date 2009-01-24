package org.castor.cpa.test.test2177;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.core.util.Configuration;
import org.castor.cpa.CPAConfiguration;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;

public final class Test2177 extends CPATestCase {
    private static final String DBNAME = "test2177";
    private static final String MAPPING = "/org/castor/cpa/test/test2177/mapping.xml";

    public static Test suite() {
        TestSuite suite = new TestSuite(Test2177.class.getName());

        suite.addTest(new Test2177("testLoadEntity1"));
        suite.addTest(new Test2177("testCreateEntity2"));
        suite.addTest(new Test2177("testUpdateEntity2"));
        suite.addTest(new Test2177("testDeleteEntity2"));

        return suite;
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL) 
            || (engine == DatabaseEngineType.DERBY) 
            || (engine == DatabaseEngineType.ORACLE);
    }

    private String _memConvertors;
    
    public Test2177(final String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();

        Configuration cfg = getConfiguration();
        _memConvertors = cfg.getString(CPAConfiguration.TYPE_CONVERTORS);
        
        StringBuffer convertors = new StringBuffer();
        convertors.append(_memConvertors);
        convertors.append(", ");
        convertors.append(FromCustomTypeConvertor.class.getName());
        convertors.append(", ");
        convertors.append(ToCustomTypeConvertor.class.getName());
        cfg.put(CPAConfiguration.TYPE_CONVERTORS, convertors.toString());
    }
    
    protected void tearDown() throws Exception {
        Configuration cfg = getConfiguration();
        cfg.put(CPAConfiguration.TYPE_CONVERTORS, _memConvertors);

        super.tearDown();
    }
    
    public void testLoadEntity1() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        Entity entity = (Entity) db.load(Entity.class, new Integer(1));

        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());
        assertNotNull(entity.getName());
        assertEquals("entity1", entity.getName().toString());
        
        db.commit();
        db.close();
    }

    public void testCreateEntity2() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        Entity entity = new Entity();
        entity.setId(new Integer(2));
        entity.setName(new CustomType("entity2 created"));
        
        db.create(entity);

        db.commit();
        db.close();
    }

    public void testUpdateEntity2() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        Entity entity = (Entity) db.load(Entity.class, new Integer(2));

        assertNotNull(entity);
        assertEquals(new Integer(2), entity.getId());
        assertNotNull(entity.getName());
        assertEquals("entity2 created", entity.getName().toString());

        entity.setName(new CustomType("entity2 updated"));
        
        db.commit();
        db.close();
    }

    public void testDeleteEntity2() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        Entity entity = (Entity) db.load(Entity.class, new Integer(2));

        assertNotNull(entity);
        assertEquals(new Integer(2), entity.getId());
        assertNotNull(entity.getName());
        assertEquals("entity2 updated", entity.getName().toString());
        
        db.remove(entity);
        
        db.commit();
        db.close();
    }
}