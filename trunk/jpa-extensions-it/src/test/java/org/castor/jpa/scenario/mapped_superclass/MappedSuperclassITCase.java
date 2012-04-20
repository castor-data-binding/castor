package org.castor.jpa.scenario.mapped_superclass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.util.JDOClassDescriptorResolver;
import org.castor.cpa.util.JDOClassDescriptorResolverImpl;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

public class MappedSuperclassITCase {

    public final Log LOG = LogFactory.getLog(getClass());

    protected JDOManager jdoManager;
    private Database db;

    private static final long ID = 1L;
    private static final String NAME = "German Keyboard";
    private static final int NUMBEROFKEYS = 104;

    @Before
    public void initDb() throws Exception {
        JDOClassDescriptorResolver resolver = new JDOClassDescriptorResolverImpl();
        resolver.addClass(org.castor.jpa.scenario.mapped_superclass.Hardware.class);
        resolver.addClass(org.castor.jpa.scenario.mapped_superclass.Keyboard.class);

        InputSource configuration = 
            new InputSource(getClass().getResource("derby-jdo-conf.xml").toExternalForm());
        JDOManager.loadConfiguration(configuration, null, getClass().getClassLoader(), resolver);
        jdoManager = JDOManager.createInstance("testSimple");

        db = jdoManager.getDatabase();
        assertNotNull(db);
    }

    @After
    public void cleanDb() throws Exception {
        if(db != null) {
            if (db.isActive()) {
                db.rollback();
            }
            db.close();
        }
    }

    private void cleanDBIfNeeded() throws Exception {
        db = jdoManager.getDatabase();

        db.begin();
        try {
            db.remove(db.load(Keyboard.class, ID));
        } catch (Exception e) {
        }
        db.commit();
    }

    private <T extends Keyboard> void createAndPersistProduct(Class<T> c)
            throws Exception {
        T hardware = c.newInstance();
        hardware.setId(ID);
        hardware.setName(NAME);

        db.begin();
        db.create(hardware);
        db.commit();
    }

    @Test
    public void create() throws Exception {
        cleanDBIfNeeded();
        createAndPersistProduct(Keyboard.class);

        db.begin();
        final Keyboard keyboard = db.load(Keyboard.class, ID);
        db.commit();

        assertNotNull(keyboard);
        assertEquals(NAME, keyboard.getName());
        assertEquals(ID, keyboard.getId());
    }

    @Test
    public void update() throws Exception {
        cleanDBIfNeeded();
        createAndPersistProduct(Keyboard.class);

        db.begin();
        final Keyboard keyboard = db.load(Keyboard.class, ID);
        assertEquals(0, keyboard.getNumberOfKeys());
        keyboard.setNumberOfKeys(NUMBEROFKEYS);
        db.commit();
        
        assertNotNull(keyboard);
        assertEquals(NUMBEROFKEYS, keyboard.getNumberOfKeys());

        db.begin();
        final Keyboard keyboardLoaded = db.load(Keyboard.class, ID);
        db.commit();
        
        assertNotNull(keyboardLoaded);
        assertEquals(NUMBEROFKEYS, keyboardLoaded.getNumberOfKeys());
    }
}
