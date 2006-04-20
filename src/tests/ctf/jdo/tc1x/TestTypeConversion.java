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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package ctf.jdo.tc1x;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Enumeration;

import harness.TestHarness;
import harness.CastorTestCase;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;

/**
 * Test on different type conversions.
 */
public final class TestTypeConversion extends CastorTestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestTypeConversion.class);
    
    private static final String REASON =
        "conversion failed because some value does not match what is expected";

    private JDOCategory     _category;

    private Database        _db;

    private OQLQuery        _oql;

    private Connection     _conn;
    
    /**
     * Constructor
     *
     * @param category The test suite of these tests
     */
    public TestTypeConversion(final TestHarness category) {
        super(category, "TC12", "Type Conversion tests");
        _category = (JDOCategory) category;
    }

    public void setUp() throws PersistenceException, SQLException {
        TypeConversion      types;
        Enumeration         enumeration;

        // Open transaction in order to perform JDO operations
        _db = _category.getDatabase();

        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        _db.begin();
        _oql = _db.getOQLQuery("SELECT types FROM "
                + TypeConversion.class.getName() + " types WHERE id = $1");
        // This one tests that bind performs type conversion
        _oql.bind(new Integer(TypeConversion.DEFAULT_ID));
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeConversion) enumeration.nextElement();
            // reset all value to default
            types.setBoolByte(false);
            types.setBoolShort(false);
            types.setBoolShortMinus(false);
            types.setBoolInt(false);
            types.setBoolIntMinus(false);
            types.setBoolBigdec(false);
            types.setBoolBigdecMinus(false);

            types.setByteInt((byte) 0);
            types.setShortInt((short) 0);
            types.setLongInt(0);
            types.setDoubleInt(0.0);
            types.setFloatInt(0.0f);

            types.setByteBigdec((byte) 0);
            types.setShortBigdec((short) 0);
            types.setIntBigdec(0);
            types.setFloatBigdec(0.0f);
            types.setDoubleBigdec(0.0);

            types.setByteString((byte) 0);
            types.setShortString((short) 0);
            types.setIntString(0);
            types.setLongString(0);
            types.setBigdecString(new BigDecimal(0));
            types.setFloatString(0.0f);
            types.setDoubleString(0.0d);

            LOG.debug("Updating object: " + types);
        } else {
            types = new TypeConversion();
            LOG.debug("Creating new object: " + types);
            types.setId(TypeConversion.DEFAULT_ID);
            _db.create(types);
        }
        _db.commit();
        
        _conn = _category.getJDBCConnection(); 
    }

    public void runTest()
    throws PersistenceException, ParseException, SQLException {
        testConversion();
        testValuesInDB();
    }

    private void testConversion() throws PersistenceException {
        TypeConversion      types;

        // This one tests that bind performs type conversion
        LOG.info("Testing date/time conversion");
        _db.begin();
        types = (TypeConversion) _db.load(TypeConversion.class,
                new Integer(TypeConversion.DEFAULT_ID));
        
        types.setBoolByte(true);
        types.setBoolShort(true);
        types.setBoolShortMinus(true);
        types.setBoolInt(true);
        types.setBoolIntMinus(true);
        types.setBoolBigdec(true);
        types.setBoolBigdecMinus(true);

        types.setByteInt((byte) 123);
        types.setShortInt((short) 123);
        types.setLongInt(123);
        types.setDoubleInt(123.0);
        types.setFloatInt(123.0f);

        types.setByteBigdec((byte) 123);
        types.setShortBigdec((short) 123);
        types.setIntBigdec(123);
        types.setFloatBigdec(123.0f);
        types.setDoubleBigdec(123.0);

        types.setByteString((byte) 123);
        types.setShortString((short) 123);
        types.setIntString(123);
        types.setLongString(123);
        types.setBigdecString(new BigDecimal(123));
        types.setFloatString(123.45f);
        types.setDoubleString(123.45);        
        _db.commit();

        _db.begin();
        types = (TypeConversion) _db.load(TypeConversion.class,
                new Integer(TypeConversion.DEFAULT_ID));

        if (types.getBoolByte()
                && types.getBoolShort()
                && types.getBoolShortMinus()
                && types.getBoolInt()
                && types.getBoolIntMinus()
                && types.getBoolBigdec()
                && types.getBoolBigdecMinus()
                && (types.getByteInt() != (byte) 123)
                && (types.getShortInt() != (short) 123)
                && (types.getLongInt() != 123)
                && (types.getDoubleInt() != 123.0d)
                && (types.getFloatInt() != 123.0)
                && (types.getByteBigdec() != (byte) 123)
                && (types.getShortBigdec() != (short) 123)
                && (types.getIntBigdec() != 123)
                && (types.getFloatBigdec() != 123.0)
                && (types.getDoubleBigdec() != 123.0d)
                && (types.getByteString() != (byte) 123)
                && (types.getShortString() != (short) 123)
                && (types.getIntString() != 123)
                && (types.getLongString() != 123)
                && (types.getBigdecString() != new BigDecimal(123))
                && (types.getFloatString() != 123.0)
                && (types.getDoubleString() != 123.0d)) {

            LOG.error(REASON);
            fail(REASON);
        }
        _db.commit();
        LOG.info("OK: Handled date/time types");
    }

    private void testValuesInDB() throws SQLException {
        LOG.info("Test values in database");
        // Create a statement and a resultset
        Statement stmt = _conn.createStatement ();
        ResultSet rset = stmt.executeQuery(
                "select bool_short, bool_short_minus, bool_int, "
                + "bool_int_minus, bool_bigdec, bool_bigdec_minus "
                + "from tc1x_conv where id = " + TypeConversion.DEFAULT_ID);
        rset.next();
        // Test values writen to db for boolean true
        // && (rset.getShort("bool_short") != (short) 1)
        // && (rset.getShort("bool_short_minus") != (short) -1)
        BigDecimal bigPlus = rset.getBigDecimal("bool_bigdec");
        BigDecimal bigMinus = rset.getBigDecimal("bool_bigdec_minus");
        if ((rset.getInt("bool_int") != 1)
                && (rset.getInt("bool_int_minus") != -1)
                && !bigPlus.equals(new BigDecimal(1))
                && !bigMinus.equals(new BigDecimal(-1))) {

            LOG.error(REASON);
            fail(REASON);
        }
        LOG.info("OK: Found the expected Values in database");
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
}
