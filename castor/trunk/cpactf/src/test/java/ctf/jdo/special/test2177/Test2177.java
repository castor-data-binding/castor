package ctf.jdo.special.test2177;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.castor.core.util.Configuration;
import org.castor.cpa.CPAConfiguration;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.mapping.MappingException;

public final class Test2177 extends TestCase {
    public static void main(final String[] args) throws Exception {
        Test2177 test = new Test2177();
        test.setUp();
        test.testLoadEntity1();
        test.tearDown();
        test.setUp();
        test.testCreateEntity2();
        test.tearDown();
        test.setUp();
        test.testUpdateEntity2();
        test.tearDown();
        test.setUp();
        test.testDeleteEntity2();
        test.tearDown();
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite("Test2177 Tests");

        suite.addTest(new Test2177("testLoadEntity1"));
        suite.addTest(new Test2177("testCreateEntity2"));
        suite.addTest(new Test2177("testUpdateEntity2"));
        suite.addTest(new Test2177("testDeleteEntity2"));

        return suite;
    }

    private static JDOManager _manager;

    private String _memConvertors;
    
    public Test2177() { super(); }

    public Test2177(final String name) { super(name); }

    protected void setUp() throws Exception {
        super.setUp();

        Configuration cfg = CPAConfiguration.getInstance();
        _memConvertors = cfg.getString(CPAConfiguration.TYPE_CONVERTORS);
        
        StringBuffer convertors = new StringBuffer();
        convertors.append(_memConvertors);
        convertors.append(", ");
        convertors.append(FromCustomTypeConvertor.class.getName());
        convertors.append(", ");
        convertors.append(ToCustomTypeConvertor.class.getName());
        cfg.put(CPAConfiguration.TYPE_CONVERTORS, convertors.toString());

        if (_manager == null) {
            try {
                String config = getClass().getResource("jdo-conf.xml").toString();
                JDOManager.loadConfiguration(config);
                _manager = JDOManager.createInstance("test2177");
            } catch (MappingException pe) {
                pe.printStackTrace();
                fail();
            }
        }
    }
    
    protected void tearDown() throws Exception {
        Configuration cfg = CPAConfiguration.getInstance();
        cfg.put(CPAConfiguration.TYPE_CONVERTORS, _memConvertors);

        super.tearDown();
    }
    
    public void testLoadEntity1() throws Exception {
        Database db = _manager.getDatabase();
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
        Database db = _manager.getDatabase();
        db.begin();
        
        Entity entity = new Entity();
        entity.setId(new Integer(2));
        entity.setName(new CustomType("entity2 created"));
        
        db.create(entity);

        db.commit();
        db.close();
    }

    public void testUpdateEntity2() throws Exception {
        Database db = _manager.getDatabase();
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
        Database db = _manager.getDatabase();
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