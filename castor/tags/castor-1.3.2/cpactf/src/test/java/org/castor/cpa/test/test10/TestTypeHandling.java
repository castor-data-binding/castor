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
package org.castor.cpa.test.test10;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;

/**
 * Test on different Literal types support by Castor. This test detect any
 * malfunction set/get of a Literal types, type conversions and modification 
 * checks on diffenent literal types fields of data objects.
 */
public final class TestTypeHandling extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestTypeHandling.class);
    
    private static final String DBNAME = "test10";
    private static final String MAPPING = "/org/castor/cpa/test/test10/mapping.xml";
    private Database _db;
    private OQLQuery _oql;

    
    public TestTypeHandling(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SAPDB);
    }

    public void setUp() throws Exception {
        // Open transaction in order to perform JDO operations
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        
        // Delete all records to avoid problems with changed mapping
        _db.begin();
        Connection conn = _db.getJdbcConnection();
        conn.createStatement().execute("DELETE FROM test10_handling");
        _db.commit();
        
        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        _db.begin();
        _oql = _db.getOQLQuery("SELECT types FROM "
                + TypeHandling.class.getName() + " types WHERE id = $(integer)1");
        
        // This one tests that bind performs type conversion
        _oql.bind(TypeHandling.DEFAULT_ID);
        Enumeration<?> enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeHandling types = (TypeHandling) enumeration.nextElement();
            LOG.debug("Updating object: " + types);
        } else {
            TypeHandling types = new TypeHandling();
            LOG.debug("Creating new object: " + types);
            _db.create(types);
        }
        _db.commit();
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }

    public void testSimpleFloat() throws PersistenceException {
        LOG.debug("Testing null in float and double fields");
        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        Enumeration<?> enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeHandling types = (TypeHandling) enumeration.nextElement();
            types.setDoubleValue(11.1234d);
            types.setFloatValue(22.4681f);
        }
        _db.commit();

        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeHandling types = (TypeHandling) enumeration.nextElement();
            if ((Math.abs(types.getDoubleValue() - 11.1234d) > 1E-5)
                    || (Math.abs(types.getFloatValue() - 22.4681f) > 1E-5)) {
                
                LOG.error("double and float doesn't work");
                fail("double and float doesn't work");
            }
        }
        _db.commit();
    }

    public void testNullIntegerAndLong() throws PersistenceException {
        LOG.debug("Testing null in integer and long fields");
        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        Enumeration<?> enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeHandling types = (TypeHandling) enumeration.nextElement();
            types.setIntValue(5);
            types.deleteIntValue();
            types.setLongValue(null);
        }
        _db.commit();

        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeHandling types = (TypeHandling) enumeration.nextElement();
            if ((types.getIntValue() != 0) || types.hasIntValue()) {
                LOG.error("null integer value was not set");
                fail("null integer value was not set");
            }
            if (types.getLongValue() != null) {
                LOG.error("null long value was not set");
                fail("null long value was not set");
            }
            types.setIntValue(5);
            types.setLongValue(new Long(5));
        } else {
            LOG.error("failed to load object");
            fail("failed to load object");
        }
        _db.commit();
     
        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeHandling types = (TypeHandling) enumeration.nextElement();
            if ((types.getIntValue() != 5) || !types.hasIntValue()) {
                LOG.error("non-null integer value was not set");
                fail("non-null integer value was not set");
            }
            if (!types.getLongValue().equals(new Long(5))) {
                LOG.error("non-null long value was not set");
                fail("non-null long value was not set");
            }
        } else {
            LOG.error("failed to load object");
            fail("failed to load object");
        }
        _db.commit();

        LOG.debug("OK: null in integer and long field passed");
    }

    public void testSimpleChar() throws PersistenceException {
        LOG.debug("Testing value in char field");
        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        Enumeration<?> enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeHandling types = (TypeHandling) enumeration.nextElement();
            types.setCharValue('A');
        }
        _db.commit();
        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeHandling types = (TypeHandling) enumeration.nextElement();
            if (types.getCharValue() != 'A') {
                LOG.error("char value was not set");
                fail("char value was not set");
            }
        } else {
            LOG.error("failed to load object");
            fail("failed to load object");
        }
        _db.commit();
        LOG.debug("OK: value in character field passed");
    }

    public void testBooleanIsMethod() throws PersistenceException {
        LOG.debug("Testing boolean get/is methods.");
        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        Enumeration<?> enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeHandling types = (TypeHandling) enumeration.nextElement();
            types.setBoolValue(true);
            types.setBoolIsMethod(true);
        }
        _db.commit();

        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeHandling types = (TypeHandling) enumeration.nextElement();
            if (!types.getBoolValue()) {
                LOG.error("(get) bool value was not set");
                fail("(get) bool value was not set");
            }
            
            if (!types.isBoolIsMethod()) {
                LOG.error("(is) bool value was not set");
                fail("(is) bool value was not set");
            }
        } else {
            LOG.error("failed to load object");
            fail("failed to load object");
        }
        _db.commit();
        
        LOG.debug("OK: Boolean get/is methods passed");
    }
    
    public void testCharToBoolean() throws PersistenceException {
        LOG.debug("Testing the boolean->char[01] conversion");
        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        Enumeration<?> enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeHandling types = (TypeHandling) enumeration.nextElement();
            types.setBoolValue(true);
        }
        _db.commit();

        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeHandling types = (TypeHandling) enumeration.nextElement();
            if (!types.getBoolValue()) {
                LOG.error("bool value was not set");
                fail("bool value was not set");
            }
        } else {
            LOG.error("failed to load object");
            fail("failed to load object");
        }
        _db.commit();
        LOG.debug("OK: The boolean->char[01] conversion passed");
    }

    public void testDateParameterized() throws PersistenceException, ParseException {
        LOG.debug("Testing date->int/numeric/char parameterized conversion");
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("yyyy/MM/dd");
        Date date = df.parse("2000/05/27");
        df.applyPattern("yyyy/MM/dd HH:mm:ss.SSS");
        Date time = df.parse("2000/05/27 02:16:01.234");
        df.applyPattern("yyyy/MM/dd HH:mm:ss.SSS");
        Date timestamp = df.parse("2000/05/27 02:16:01.234");
        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        Enumeration<?> enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeHandling types = (TypeHandling) enumeration.nextElement();
            types.setDate2(date);
            types.setTime2(time);
            types.setTimestamp2(timestamp);
        }
        _db.commit();

        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeHandling types = (TypeHandling) enumeration.nextElement();
            if (!date.equals(types.getDate2())) {
                LOG.error("date/int value was not set");
                fail("date/int value was not set");
            }
            if (!time.equals(types.getTime2())) {
                LOG.error("time/string value was not set");
                fail("time/string vlaue was not set");
            }
            if (!timestamp.equals(types.getTimestamp2())) {
                LOG.error("timestamp/numeric value was not set");
                LOG.error("timestamp was set to: " + df.format(timestamp));
                LOG.error("with oid: " + timestamp.hashCode());
                LOG.error("timestamp is: " + df.format(types.getTimestamp2()));
                LOG.error("with oid: " + types.getTimestamp2().hashCode());
                fail("timestamp/numeric value was not set");
            }
        } else {
            LOG.error("failed to load object");
            fail("failed to load object");
        }
        _db.commit();
        LOG.debug("OK: date->int/numeric/char conversion passed");
    }
}
