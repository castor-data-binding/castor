/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.cpa.test.test18;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Test for the behaviors the persistent interface. A data object
 * that implements the persistence interaface is notified by 
 * Castor for loading, creating and storing events.
 */
public final class TestPersistent extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestPersistent.class);
    
    private static final String DBNAME = "test18";
    private static final String MAPPING = "/org/castor/cpa/test/test18/mapping.xml";
    private Database _db;

    public TestPersistent(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
    
    public void testRun() throws PersistenceException {
        PersistentObject      parent;

        delete();
        create();
        parent = loadCreated();
        update(parent);
        loadUpdated();
    }

    private void delete() throws PersistenceException {
        OQLQuery            oql;
        QueryResults        qres;

        LOG.info("Delete everything");
        _db.begin();
        oql = _db.getOQLQuery("SELECT p FROM "
                + PersistentObject.class.getName() + " p WHERE id=$1");
        oql.bind(PersistentObject.DEFAULT_ID);
        qres = oql.execute();
        while (qres.hasMore()) {
            _db.remove(qres.next());
        }
        oql.close();
        
        oql = _db.getOQLQuery("SELECT g FROM "
                + PersistentGroup.class.getName() + " g");
        qres = oql.execute();
        while (qres.hasMore()) {
            _db.remove(qres.next());
        }
        oql.close();
        
        oql = _db.getOQLQuery("SELECT r FROM "
                + PersistentRelated.class.getName() + " r");
        qres = oql.execute();
        while (qres.hasMore()) {
            _db.remove(qres.next());
        }
        oql.close();
        _db.commit();
    }
    
    private void create() throws PersistenceException {
        PersistentObject      parent;
        PersistentObject      child;
        PersistentRelated     related;

        LOG.info("Attempt to create parent with children");
        _db.begin();
        parent = new PersistentObject();
        parent.setGroup(new PersistentGroup());
        parent.addChild(new PersistentObject(71));
        parent.addChild(new PersistentObject(72));
        child = new PersistentObject(73);
        child.addChild(new PersistentObject(731));
        child.addChild(new PersistentObject(732));
        parent.addChild(child);
        related = new PersistentRelated();
        parent.setRelated(related);
        _db.create(parent);
        _db.commit();
        LOG.info("Created parent with children: " + parent);
    }
    
    private PersistentObject loadCreated() throws PersistenceException {
        PersistentObject      parent;
        PersistentObject      child;
        PersistentGroup       group;

        _db.begin();
        parent = _db.load(PersistentObject.class,
                new Integer(PersistentObject.DEFAULT_ID));
        
        if (parent != null) {
            group = parent.getGroup();
            if ((group == null)
                    || (group.getId() != PersistentGroup.DEFAULT_ID)) {
                
                LOG.error("loaded parent without group: " + parent);
                fail("group not found");
            }
            
            if (parent.getRelated() == null) {
                LOG.error("loaded parent without related: " + parent);
                fail("related not found");
            }
            
            if ((parent.getChildren() == null)
                    || (parent.getChildren().size() != 3)
                    || (parent.findChild(71) == null)
                    || (parent.findChild(72) == null)
                    || (parent.findChild(73) == null)) {
                
                LOG.error("loaded parent without three children: " + parent);
                fail("children size mismatched");
            }
            
            child = parent.findChild(73);
            if ((child == null)
                    || (child.getChildren() == null)
                    || (child.getChildren().size() != 2)
                    || (child.findChild(731) == null)
                    || (child.findChild(732) == null)) {
                
                LOG.error("loaded child without two grandchildren: " + child);
                fail("garndchildren not found");
            }
            child.setValue1("new value");
        } else {
            LOG.error("failed to create parent with children");
            fail("failed to create parent with children");
        }

        long beforeModTime = System.currentTimeMillis() / 1000;
        _db.commit();
        long afterModTime = System.currentTimeMillis() / 1000;
        
        _db.begin();
        
        parent = _db.load(PersistentObject.class, new Integer(7));
        
        child = parent.findChild(73);
        if (child == null) {
            LOG.error("child not loaded");
            fail("child load failed");
        } else if (child.getModificationTime() == null) {
            LOG.error("wrong modification time: " + child);
            fail("modification time incorrect");
        } else {
            long modTime = child.getModificationTime().getTime() / 1000;
            if ((modTime < beforeModTime) || (modTime > afterModTime)) {
                LOG.error("wrong modification time: " + child);
                fail("modificationo time incorrect");
            }
        }
        _db.commit();
        
        return parent;
    }

    private void update(final PersistentObject parent)
    throws PersistenceException {
        PersistentObject      child;

        LOG.info("Long transaction test");
        parent.setValue1("long transaction parent");
        parent.getChildren().removeElement(parent.findChild(71));
        child = new PersistentObject(74);
        child.setValue1("long transaction child");
        child.addChild(new PersistentObject(741));
        parent.addChild(child);
        parent.findChild(73).getChildren().removeElement(
                parent.findChild(73).findChild(731));
        parent.findChild(73).addChild(new PersistentObject(733));
        
        _db.begin();
        _db.update(parent);
        _db.commit();
        LOG.info("Updated parent with children: " + parent);
    }
    
    public void loadUpdated() throws PersistenceException {
        PersistentObject      parent;
        PersistentObject      child;

        _db.begin();
        parent = _db.load(PersistentObject.class,
                new Integer(PersistentObject.DEFAULT_ID));
        
        if (parent != null) {
            if ((parent.getChildren() == null)
                    || (parent.getChildren().size() != 3)
                    || !"long transaction parent".equals(parent.getValue1())
                    || (parent.findChild(71) != null)
                    || (parent.findChild(72) == null)
                    || (parent.findChild(73) == null)
                    || (parent.findChild(74) == null)) {
                
                LOG.error("loaded parent without three children: " + parent);
                fail("children size mismatched");
            }
            
            child = parent.findChild(73);
            if ((child == null)
                    || (child.getChildren() == null)
                    || (child.getChildren().size() != 2)
                    || (child.findChild(731) != null)
                    || (child.findChild(732) == null)
                    || (child.findChild(733) == null)) {
                
                LOG.error("loaded child without two grandchildren: " + child);
                fail("grandchildren size mismatched");
            }
            
            child = parent.findChild(74);
            if ((child == null)
                    || (child.getChildren() == null)
                    || (child.getChildren().size() != 1)
                    || !"long transaction child".equals(child.getValue1())
                    || (child.findChild(741) == null)) {
                
                LOG.error("loaded child without one grandchildren: " + child);
                fail("grandchildren size mismatched");
            }
        } else {
            LOG.error("failed to create parent with children");
            fail("failed to create parent with children");
        }

        _db.commit();
    }
}
