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
package ctf.jdo.tc3x;

import harness.CastorTestCase;
import harness.TestHarness;

import java.sql.SQLException;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

public final class TestCallSql extends CastorTestCase {
    private static final Log LOG = LogFactory.getLog(TestCallSql.class);
    
    private JDOCategory    _category;

    private Database       _db;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestCallSql(final TestHarness category) {
        super(category, "TC38", "CALL SQL with parameters");
        _category = (JDOCategory) category;
    }

    /**
     * Get a JDO database
     */
    public void setUp() throws PersistenceException, SQLException {
        _db = _category.getDatabase();
    }

    public void runTest() throws PersistenceException, SQLException {
        OQLQuery      oql;
        QueryResults  enumeration;
        Entity        object;

        _db.begin();

        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        oql = _db.getOQLQuery(
                "SELECT object FROM " + Entity.class.getName() + " object WHERE id = $1");
        oql.bind(50);
        enumeration = oql.execute();
        if (enumeration.hasMore()) {
            object = (Entity) enumeration.next();
            LOG.debug("Retrieved object: " + object);
            object.setValue1(Entity.DEFAULT_VALUE_1);
            object.setValue2(Entity.DEFAULT_VALUE_2);
        } else {
            object = new Entity();
            object.setId(50);
            LOG.debug("Creating new object: " + object);
            _db.create(object);
        }
        oql.close();
        _db.commit();

        try {
            LOG.debug("CALL SQL query with object part of an extend hierarchy");
            _db.begin();
            oql = _db.getOQLQuery("CALL SQL "
                    + "SELECT tc3x_entity.id,tc3x_entity.value1,tc3x_entity.value2,"
                    + "tc3x_extends.id,tc3x_extends.value3,tc3x_extends.value4 "
                    + "FROM tc3x_entity LEFT OUTER JOIN tc3x_extends "
                    + "ON tc3x_entity.id=tc3x_extends.id "
                    + "WHERE (tc3x_entity.id = $1) AS " + Entity.class.getName());
            oql.bind(50);
            enumeration = oql.execute();
            if (enumeration.hasMore()) {
                object = (Entity) enumeration.next();
                LOG.debug("Retrieved object: " + object);
            } else {
                fail("test object not found");
            }
            oql.close();
            _db.commit();
        } catch (Exception ex) {
            fail("Exception thrown " + ex);
        }

        LOG.debug("CALL SQL query with simple (stand-alone) object");
        _db.begin();
        CallEntity test = new CallEntity();
        test.setId(55);
        test.setValue1("value1");
        test.setValue2("value2");
        _db.create(test);
        _db.commit();
        
        _db.begin();
        oql = _db.getOQLQuery(
                "CALL SQL SELECT id, value1 , value2 "
                + "FROM tc3x_call WHERE (id = $1) AS " + CallEntity.class.getName());
        oql.bind(55);
        enumeration = oql.execute();
        CallEntity objectEx = null;
        if (enumeration.hasMore()) {
            objectEx = (CallEntity) enumeration.next();
            LOG.debug("Retrieved object: " + objectEx);
        } else {
            fail("test object not found");
        }
        oql.close();
        _db.commit();
        
        _db.begin();
        test = (CallEntity) _db.load(CallEntity.class, new Integer(55));
        _db.remove(test);
        _db.commit();
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
}
