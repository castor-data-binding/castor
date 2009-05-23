/*
 * Copyright 2009 Dan Daugherty, Ralf Joachim
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
package org.castor.cpa.test.test303;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.mapping.MappingException;

public final class TestPersistentObject extends CPATestCase {
    private static final String DBNAME = "test303";
    private static final String MAPPING = "/org/castor/cpa/test/test303/mapping.xml";

    public static Test suite() {
        TestSuite suite = new TestSuite(TestPersistentObject.class.getName());
        
        suite.addTest(new TestPersistentObject("testSave"));
        suite.addTest(new TestPersistentObject("testDelete"));
        suite.addTest(new TestPersistentObject("testSaveWithRelatedObject"));

        return suite;
    }
    
    public TestPersistentObject(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL) 
            || (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.SAPDB)
            || (engine == DatabaseEngineType.SQL_SERVER);
    }
    
    public void testSave()
    throws PersistenceException, MappingException, SQLException {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        Entity2 tc2 = new Entity2();
        tc2.setName("bob");
        
        Database db1 = jdo.getDatabase();
        try {
            db1.begin();
            db1.create(tc2);
            db1.commit();
            tc2.jdoSetTimeStamp(0);
        } catch (PersistenceException e) {
            try {
                db1.rollback();
            } catch (TransactionNotInProgressException e1) {
                // we can't rollback since there is no transaction
            }
            throw e;
        } finally {
            db1.close();
        }

        String sql = "select id, name from test303_entity2 where name = 'bob'";
        Connection con = jdo.getConnectionFactory().createConnection();
        ResultSet results = con.prepareStatement(sql).executeQuery();
        results.next();
        assertNotNull("Id was null", results.getObject(1));
        results.close();
        con.close();

        Database db2 = jdo.getDatabase();
        try {
            db2.begin();
            try {
                db2.remove(db2.load(tc2.getClass(), tc2.getId()));
                db2.commit();
            } catch (ObjectNotFoundException e) {
                db2.rollback();
            }
        } catch (PersistenceException e) {
            db2.rollback();
            throw e;
        } finally {
            db2.close();
        }
    }

    public void testDelete()
    throws PersistenceException, MappingException, SQLException {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        Entity2 tc2 = new Entity2();
        tc2.setName("bob");
        
        Database db1 = jdo.getDatabase();
        try {
            db1.begin();
            db1.create(tc2);
            db1.commit();
            tc2.jdoSetTimeStamp(0);
        } catch (PersistenceException e) {
            try {
                db1.rollback();
            } catch (TransactionNotInProgressException e1) {
                // we can't rollback since there is no transaction
            }
            throw e;
        } finally {
            db1.close();
        }
        
        Database db2 = jdo.getDatabase();
        try {
            db2.begin();
            try {
                db2.remove(db2.load(tc2.getClass(), tc2.getId()));
                db2.commit();
            } catch (ObjectNotFoundException e) {
                db2.rollback();
            }
        } catch (PersistenceException e) {
            db2.rollback();
            throw e;
        } finally {
            db2.close();
        }

        String sql = "select id, name from test303_entity2 where name = 'bob'";
        Connection con = jdo.getConnectionFactory().createConnection();
        ResultSet results = con.prepareStatement(sql).executeQuery();
        assertFalse("There shouldn't be anything in the result set", results.next());
        results.close();
        con.close();
    }

    public void testSaveWithRelatedObject()
    throws PersistenceException, MappingException, SQLException {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        Entity2 tc2 = new Entity2();
        tc2.setName("bob");

        Database db1 = jdo.getDatabase();
        try {
            db1.begin();
            db1.create(tc2);
            db1.commit();
            tc2.jdoSetTimeStamp(0);
        } catch (PersistenceException e) {
            try {
                db1.rollback();
            } catch (TransactionNotInProgressException e1) {
                // we can't rollback since there is no transaction
            }
            throw e;
        } finally {
            db1.close();
        }

        Entity1 tc1 = new Entity1();
        tc1.setRelated(tc2);

        Database db2 = jdo.getDatabase();
        try {
            db2.begin();
            // tc1.setRelated((Entity2) db2.load(tc2.getClass(), tc2.getId()));
            db2.create(tc1);
            db2.commit();
            tc1.jdoSetTimeStamp(0);
        } catch (PersistenceException e) {
            try {
                db2.rollback();
            } catch (TransactionNotInProgressException e1) {
                // we can't rollback since there is no transaction
            }
            throw e;
        } finally {
            db2.close();
        }

        String sql = "select related from test303_entity1 where id = " + tc1.getId().longValue();
        Connection con = jdo.getConnectionFactory().createConnection();
        ResultSet results = con.prepareStatement(sql).executeQuery();
        results.next();
        assertEquals(tc2.getId().longValue(), tc1.getRelated().getId().longValue());
        assertEquals(tc2.getId().longValue(), results.getLong(1));
        results.close();
        con.close();

        Database db3 = jdo.getDatabase();
        try {
            db3.begin();
            try {
                db3.remove(db3.load(tc1.getClass(), tc1.getId()));
                db3.commit();
            } catch (ObjectNotFoundException e) {
                db3.rollback();
            }
        } catch (PersistenceException e) {
            db3.rollback();
            throw e;
        } finally {
            db3.close();
        }
        
        Database db4 = jdo.getDatabase();
        try {
            db4.begin();
            try {
                db4.remove(db4.load(tc2.getClass(), tc2.getId()));
                db4.commit();
            } catch (ObjectNotFoundException e) {
                db4.rollback();
            }
        } catch (PersistenceException e) {
            db4.rollback();
            throw e;
        } finally {
            db4.close();
        }
    }
}

