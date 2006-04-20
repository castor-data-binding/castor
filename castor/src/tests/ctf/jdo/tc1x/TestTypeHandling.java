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
 * Copyright 1999-2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package ctf.jdo.tc1x;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * Test on different Literal types support by Castor. This test detect any
 * malfunction set/get of a Literal types, type conversions and modification 
 * checks on diffenent literal types fields of data objects.
 */
public final class TestTypeHandling extends CastorTestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestTypeHandling.class);
    
    private JDOCategory     _category;

    private Database        _db;

    private OQLQuery        _oql;

    /**
     * Constructor
     *
     * @param category The test suite of these tests
     */
    public TestTypeHandling(final TestHarness category) {
        super(category, "TC10", "Type handling tests");
        _category = (JDOCategory) category;
    }


    public void setUp() throws PersistenceException, SQLException {
        TypeHandling     types;
        Enumeration   enumeration;

        // Open transaction in order to perform JDO operations
        _db = _category.getDatabase();

        // Delete all records to avoid problems with changed mapping
        Connection conn = _category.getJDBCConnection();
        conn.createStatement().execute("DELETE FROM tc1x_handling");

        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        _db.begin();
        _oql = _db.getOQLQuery("SELECT types FROM "
                + TypeHandling.class.getName() + " types WHERE id = $(integer)1");
        // This one tests that bind performs type conversion
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeHandling) enumeration.nextElement();
            LOG.debug("Updating object: " + types);
        } else {
            types = new TypeHandling();
            LOG.debug("Creating new object: " + types);
            _db.create(types);
        }
        _db.commit();
    }

    private void testDateTimeConversion() throws PersistenceException {
        TypeHandling     types;
        Enumeration   enumeration;

        LOG.info("Testing date/time conversion");
        _db.begin();
        // This one tests that bind performs type conversion
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeHandling) enumeration.nextElement();
            LOG.debug("Date type: " + types.getDate().getClass());
            LOG.debug("Time type: " + types.getTime().getClass());
            LOG.debug("Deleting object: " + types);
            _db.remove(types);
        } else {
            LOG.error("Could not load types object");
            fail("Could not load types object");
        }
        _db.commit();

        _db.begin();
        types = new TypeHandling();
        LOG.debug("Creating new object: " + types);
        _db.create(types);
        _db.commit();
        LOG.info("OK: Handled date/time types");
    }

    private void testSimpleFloat() throws PersistenceException {
        TypeHandling    types;
        Enumeration   enumeration;

        LOG.info("Testing null in float and double fields");
        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeHandling) enumeration.nextElement();
            types.setDoubleValue(11.1234d);
            types.setFloatValue(22.4681f);
        }
        _db.commit();

        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeHandling) enumeration.nextElement();
            if ((Math.abs(types.getDoubleValue() - 11.1234d) > 1E-5)
                    || (Math.abs(types.getFloatValue() - 22.4681f) > 1E-5)) {
                
                LOG.error("double and float doesn't work");
                fail("double and float doesn't work");
            }
        }
        _db.commit();
    }

    private void testNullIntegerAndLong() throws PersistenceException {
        TypeHandling     types;
        Enumeration   enumeration;

        LOG.info("Testing null in integer and long fields");
        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeHandling) enumeration.nextElement();
            types.setIntValue(5);
            types.deleteIntValue();
            types.setLongValue(null);
        }
        _db.commit();

        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeHandling) enumeration.nextElement();
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
            types = (TypeHandling) enumeration.nextElement();
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

        LOG.info("OK: null in integer and long field passed");
    }

    private void testSimpleChar() throws PersistenceException {
        TypeHandling     types;
        Enumeration   enumeration;

        LOG.info("Testing value in char field");
        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeHandling) enumeration.nextElement();
            types.setCharValue('A');
        }
        _db.commit();
        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeHandling) enumeration.nextElement();
            if (types.getCharValue() != 'A') {
                LOG.error("char value was not set");
                fail("char value was not set");
            }
        } else {
            LOG.error("failed to load object");
            fail("failed to load object");
        }
        _db.commit();
        LOG.info("OK: value in character field passed");
    }

    private void testBooleanIsMethod() throws PersistenceException {
        TypeHandling       types;
        Enumeration     enumeration;
        
        LOG.info("Testing boolean get/is methods.");
        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeHandling) enumeration.nextElement();
            types.setBoolValue(true);
            types.setBoolIsMethod(true);
        }
        _db.commit();

        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeHandling) enumeration.nextElement();
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
        
        LOG.info("OK: Boolean get/is methods passed");
    }
    
    private void testCharToBoolean() throws PersistenceException {
        TypeHandling     types;
        Enumeration   enumeration;

        LOG.info("Testing the boolean->char[01] conversion");
        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeHandling) enumeration.nextElement();
            types.setBoolValue(true);
        }
        _db.commit();

        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeHandling) enumeration.nextElement();
            if (!types.getBoolValue()) {
                LOG.error("bool value was not set");
                fail("bool value was not set");
            }
        } else {
            LOG.error("failed to load object");
            fail("failed to load object");
        }
        _db.commit();
        LOG.info("OK: The boolean->char[01] conversion passed");
    }

    private void testDateParameterized()
    throws PersistenceException, ParseException {
        TypeHandling           types;
        Enumeration         enumeration;
        Date                date;
        Date                time;
        Date                timestamp;
        SimpleDateFormat    df;

        LOG.info("Testing date->int/numeric/char parameterized conversion");
        df = new SimpleDateFormat();
        df.applyPattern("yyyy/MM/dd");
        date = df.parse("2000/05/27");
        df.applyPattern("yyyy/MM/dd HH:mm:ss.SSS");
        time = df.parse("2000/05/27 02:16:01.234");
        df.applyPattern("yyyy/MM/dd HH:mm:ss.SSS");
        timestamp = df.parse("2000/05/27 02:16:01.234");
        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeHandling) enumeration.nextElement();
            types.setDate2(date);
            types.setTime2(time);
            types.setTimestamp2(timestamp);
        }
        _db.commit();

        _db.begin();
        _oql.bind(TypeHandling.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeHandling) enumeration.nextElement();
            if (!date.equals(types.getDate2())) {
                LOG.error("date/int value was not set");
                fail("date/int vlaue was not set");
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
        LOG.info("OK: date->int/numeric/char conversion passed");
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
    
    public void runTest() throws PersistenceException, ParseException {
        testDateTimeConversion();
        testNullIntegerAndLong();
        testSimpleChar();
        testCharToBoolean();
        testDateParameterized();
        testBooleanIsMethod();
        testSimpleFloat();
    }
}
