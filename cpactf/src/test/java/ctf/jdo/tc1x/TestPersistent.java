/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 */
package ctf.jdo.tc1x;

import harness.TestHarness;
import harness.CastorTestCase;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Test for the behaviors the persistent interface. A data object
 * that implements the persistence interaface is notified by 
 * Castor for loading, creating and storing events.
 */
public final class TestPersistent extends CastorTestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestPersistent.class);
    
    private JDOCategory    _category;

    private Database       _db;

    public TestPersistent(final TestHarness category) {
        super(category, "TC18", "Persistence interface tests");
        _category = (JDOCategory) category;
    }

    public void setUp() 
            throws PersistenceException {
        _db = _category.getDatabase();
    }

    public void runTest() throws PersistenceException {
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
        parent = (PersistentObject) _db.load(PersistentObject.class,
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
        
        parent = (PersistentObject) _db.load(PersistentObject.class,
                                           new Integer(7));
        
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
        parent = (PersistentObject) _db.load(PersistentObject.class,
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
    
    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
}
