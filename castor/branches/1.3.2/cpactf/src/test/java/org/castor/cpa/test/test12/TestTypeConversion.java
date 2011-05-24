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
package org.castor.cpa.test.test12;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;

/**
 * Test on different type conversions.
 */
public final class TestTypeConversion extends CPATestCase {

    private static final Log LOG = LogFactory.getLog(TestTypeConversion.class);
    
    private static final String DBNAME = "test12";
    private static final String MAPPING = "/org/castor/cpa/test/test12/mapping.xml";
    private Database _db;
    private OQLQuery _oql;
    
    private static final String REASON =
        "Conversion failed because some value does not match what is expected.";
    
    /**
     * Constructor
     *
     * @param category The test suite of these tests
     */
    public TestTypeConversion(final String name) {
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

        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        _db.begin();
        _oql = _db.getOQLQuery("SELECT types FROM "
                + TypeConversion.class.getName() + " types WHERE id = $1");
        // This one tests that bind performs type conversion
        _oql.bind(new Integer(TypeConversion.DEFAULT_ID));
        Enumeration<?> enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeConversion types = (TypeConversion) enumeration.nextElement();
            // reset all value to default
            types.setBoolInt(false);
            types.setBoolIntMinus(false);
            types.setBoolBigdec(false);
            types.setBoolBigdecMinus(false);

            types.setByteInt((byte) 0);
            types.setShortInt((short) 0);
            types.setLongInt(0);
            types.setDoubleInt(0.0);
            types.setFloatInt(0.0f);

            types.setIntBigdec(0);
            types.setFloatBigdec(0.0f);
            types.setDoubleBigdec(0.0);

            types.setIntString(0);
            types.setLongString(0);
            types.setBigdecString(new BigDecimal(0));
            types.setFloatString(0.0f);
            types.setDoubleString(0.0d);

            LOG.debug("Updating object: " + types);
        } else {
            TypeConversion types = new TypeConversion();
            LOG.debug("Creating new object: " + types);
            types.setId(TypeConversion.DEFAULT_ID);
            _db.create(types);
        }
        _db.commit();
    }

    public void runTest() throws Exception {
        testConversion();
        testValuesInDB();
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }

    public void testConversion() throws PersistenceException {
        // This one tests that bind performs type conversion
        LOG.debug("Testing date/time conversion");
        _db.begin();
        TypeConversion types = _db.load(TypeConversion.class,
                new Integer(TypeConversion.DEFAULT_ID));
        
        types.setBoolInt(true);
        types.setBoolIntMinus(true);
        types.setBoolBigdec(true);
        types.setBoolBigdecMinus(true);

        types.setByteInt((byte) 123);
        types.setShortInt((short) 123);
        types.setLongInt(123);
        types.setDoubleInt(123.0);
        types.setFloatInt(123.0f);

        types.setIntBigdec(123);
        types.setFloatBigdec(123.0f);
        types.setDoubleBigdec(123.0);

        types.setIntString(123);
        types.setLongString(123);
        types.setBigdecString(new BigDecimal(123));
        types.setFloatString(123.45f);
        types.setDoubleString(123.45);        
        _db.commit();

        _db.begin();
        types = _db.load(TypeConversion.class,
                new Integer(TypeConversion.DEFAULT_ID));

        if (types.getBoolInt()
                && types.getBoolIntMinus()
                && types.getBoolBigdec()
                && types.getBoolBigdecMinus()
                && (types.getByteInt() != (byte) 123)
                && (types.getShortInt() != (short) 123)
                && (types.getLongInt() != 123)
                && (types.getDoubleInt() != 123.0d)
                && (types.getFloatInt() != 123.0)
                && (types.getIntBigdec() != 123)
                && (types.getFloatBigdec() != 123.0)
                && (types.getDoubleBigdec() != 123.0d)
                && (types.getIntString() != 123)
                && (types.getLongString() != 123)
                && (types.getBigdecString() != new BigDecimal(123))
                && (types.getFloatString() != 123.0)
                && (types.getDoubleString() != 123.0d)) {

            LOG.error(REASON);
            fail(REASON);
        }
        _db.commit();
        LOG.debug("OK: Handled date/time types");
    }

    public void testValuesInDB() throws Exception {
        LOG.debug("Test values in database");
        // Create a statement and a resultset
        _db.begin();
        Connection conn = _db.getJdbcConnection();
        Statement stmt = conn.createStatement ();
        ResultSet rs = stmt.executeQuery(
                "select bool_int, "
                + "bool_int_minus, bool_bigdec, bool_bigdec_minus "
                + "from test12_conv where id = " + TypeConversion.DEFAULT_ID);
        rs.next();
        BigDecimal bigPlus = rs.getBigDecimal("bool_bigdec");
        BigDecimal bigMinus = rs.getBigDecimal("bool_bigdec_minus");
        if ((rs.getInt("bool_int") != 1)
                && (rs.getInt("bool_int_minus") != -1)
                && !bigPlus.equals(new BigDecimal(1))
                && !bigMinus.equals(new BigDecimal(-1))) {

            LOG.error(REASON);
            fail(REASON);
        }
        _db.commit();
        LOG.debug("OK: Found the expected Values in database");
    }
}
