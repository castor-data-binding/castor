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
 *
 * $Id$
 */

package ctf.jdo.tc3x;

import harness.CastorTestCase;
import harness.TestHarness;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.jdo.util.JDOUtils;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

public final class TestOqlExtends extends CastorTestCase {
    private static final Log LOG = LogFactory.getLog(TestOqlExtends.class);
    
    private JDOCategory    _category;
    private Database       _db;

    public TestOqlExtends(final TestHarness category) {
        super(category, "TC31", "OQL queries for extends");
        _category = (JDOCategory) category;
    }

    public void setUp() throws PersistenceException {
        _db = _category.getDatabase();
        
        // XXX [SMH]: We need to clear some tables because "TC23 ManyToMany"
        // is doing something unwise, see bug 1445. 
        Connection conn = null;
        try {
            conn = _category.getJDBCConnection();
            conn.setAutoCommit(false);
            conn.createStatement().executeUpdate("DELETE FROM tc3x_extends2");
            conn.createStatement().executeUpdate("DELETE FROM tc3x_extends1");
            conn.createStatement().executeUpdate("DELETE FROM tc3x_persistent");
            conn.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDOUtils.closeConnection (conn);
        }
    }

    public void runTest() throws Exception {
        OQLQuery         oqlAll;
        OQLQuery         oql;
        QueryResults     res;
        int              cnt;

        // remove old test objects
        _db.begin();
        oqlAll = _db.getOQLQuery(
                "SELECT t FROM " + ExtendsEntity1.class.getName() + " t");
        res = oqlAll.execute();
        while (res.hasMore()) { _db.remove(res.next()); }
        oqlAll.close();
        
        oql = _db.getOQLQuery(
                "SELECT t FROM " + GroupEntity.class.getName() + " t");
        res = oql.execute();
        while (res.hasMore()) { _db.remove(res.next()); }
        oql.close();
        
        oql = _db.getOQLQuery(
                "SELECT t FROM " + ExtendsEntity2.class.getName() + " t");
        res = oql.execute();
        while (res.hasMore()) { _db.remove(res.next()); }
        oql.close();

        oql = _db.getOQLQuery(
                "SELECT t FROM " + PersistentEntity.class.getName() + " t");
        res = oql.execute();
        while (res.hasMore()) { _db.remove(res.next()); }
        oql.close();
        _db.commit();

        // create data objects for test
        _db.begin();
        
        GroupEntity group1 = new GroupEntity();
        LOG.debug("Creating group: " + group1);
        _db.create(group1);
        
        GroupEntity group2 = new GroupEntity();
        group2.setId(GroupEntity.DEFAULT_ID + 1);
        LOG.debug("Creating group: " + group2);
        _db.create(group2);
        
        ExtendsEntity2 entity2 = new ExtendsEntity2();
        entity2.setId(ExtendsEntity2.DEFAULT_ID + 20);
        entity2.setGroup(group2);
        LOG.debug("Creating new object: " + entity2);
        _db.create(entity2);
        
        Date date = new Date();
        Thread.sleep(2000);
        
        ExtendsEntity1 entity1 = new ExtendsEntity1();
        entity1.setId(ExtendsEntity1.DEFAULT_ID + 10);
        entity1.setGroup(group1);
        entity1.getList().add(entity2);
        LOG.debug("Creating new object: " + entity1);
        _db.create(entity1);
        
        entity2 = new ExtendsEntity2();
        entity2.setId(ExtendsEntity2.DEFAULT_ID + 21);
        entity2.setGroup(group2);
        LOG.debug("Creating new object: " + entity2);
        _db.create(entity2);
        
        entity1 = new ExtendsEntity1();
        entity1.setId(ExtendsEntity1.DEFAULT_ID + 11);
        entity1.setGroup(group2);
        entity1.getList().add(entity2);
        LOG.debug("Creating new object: " + entity1);
        _db.create(entity1);

        _db.commit();

        // query on extends object
        _db.begin();
        
        oql = _db.getOQLQuery("SELECT t FROM " + ExtendsEntity1.class.getName()
                + " t WHERE t.group.id=$1");
        oql.bind(group1.getId());
        res = oql.execute();
        for (cnt = 0; res.hasMore(); cnt++) {
            entity1 = (ExtendsEntity1) res.next();
            LOG.debug("Retrieved object: " + entity1);
            if (entity1.getExt() != 0) {
                LOG.debug("Error: ext field = " + entity1.getExt());
                fail("ext field retrieval failed");
            }
        }
        oql.close();
        if (cnt == 1) {
            LOG.info("OK");
        } else {
            LOG.info("Error: retrieved " + cnt + " objects");
            fail("result size mismatch in the query on extends object");
        }
        
        oql = _db.getOQLQuery("SELECT t FROM " + PersistentEntity.class.getName()
                + " t WHERE t.creationTime<=$1");
        oql.bind(date);
        res = oql.execute();
        for (cnt = 0; res.hasMore(); cnt++) {
            PersistentEntity p = (PersistentEntity) res.next();
            LOG.debug("Retrieved object: " + p);
        }
        oql.close();
        if (cnt == 1) {
            LOG.info("OK");
        } else {
            LOG.info("Error: retrieved " + cnt + " objects");
            fail("result size mismatch in the query on base object");
        }

        _db.commit();

        // query on many-to-many relation
        _db.begin();
        
        oql = _db.getOQLQuery("SELECT t FROM " + ExtendsEntity1.class.getName()
                + " t WHERE t.list.id=$1");
        oql.bind(entity2.getId());
        res = oql.execute();
        for (cnt = 0; res.hasMore(); cnt++) {
            entity1 = (ExtendsEntity1) res.next();
            LOG.debug("Retrieved object: " + entity1);
        }
        oql.close();
        if (cnt == 1) {
            LOG.info("OK");
        } else {
            LOG.info("Error: retrieved " + cnt + " objects");
            fail("result size mismatch in the query on many-to-many");
        }

        res = oqlAll.execute();
        while (res.hasMore()) { _db.remove(res.next()); }
        oqlAll.close();

        _db.commit();
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
}
