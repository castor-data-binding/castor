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
package org.castor.cpa.test.test11;

import java.io.StringReader;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.engine.ClobImpl;

/**
 * Test on BLOB and CLOB as a field type of data objects.
 */
public final class TestTypeLOB extends CPATestCase {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestTypeLOB.class);
    
    private static final byte[] BLOB_VALUE = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private static final String BLOB_STRING = new String(BLOB_VALUE);
    private static final String CLOB_VALUE = "0123456789";

    private static final String DBNAME = "test11";
    private static final String MAPPING = "/org/castor/cpa/test/test11/mapping.xml";
    private Database _db;
    private OQLQuery _oql;
    
    /**
     * Constructor
     *
     * @param category The test suite of these tests
     */
    public TestTypeLOB(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SAPDB)
            || (engine == DatabaseEngineType.SQL_SERVER);
    }
    
    public void setUp() throws Exception {
        // Open transaction in order to perform JDO operations
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();

        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        _db.begin();
        _oql = _db.getOQLQuery("SELECT types FROM " + TypeLOB.class.getName()
                + " types WHERE id = $(integer)1");
        // This one tests that bind performs type conversion
        _oql.bind(TypeLOB.DEFAULT_ID);
        Enumeration<?> enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeLOB types = (TypeLOB) enumeration.nextElement();
            LOG.debug("Updating object: " + types);
        } else {
            TypeLOB types = new TypeLOB();
            types.setId(TypeLOB.DEFAULT_ID);
            LOG.debug("Creating new object: " + types);
            _db.create(types);
        }
        _db.commit();
    }

    public void testTypeLOB() throws Exception {
        char[]        cbuf = new char[CLOB_VALUE.length() + 1];

        LOG.debug("Testing BLOB and CLOB fields");
        _db.begin();
        _oql.bind(TypeLOB.DEFAULT_ID);
        Enumeration<?> enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeLOB types = (TypeLOB) enumeration.nextElement();
            types.setBlob(BLOB_VALUE);
            types.setClob(CLOB_VALUE);
            types.setClob2(new ClobImpl(new StringReader(CLOB_VALUE), CLOB_VALUE.length()));
        }
        _db.commit();

        _db.begin();
        _oql.bind(TypeLOB.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            TypeLOB types = (TypeLOB) enumeration.nextElement();

            if ((types.getBlob() == null)
                    || !BLOB_STRING.equals(new String(types.getBlob()))) {
                
                LOG.error("BLOB value was not set");
                fail("BLOB value was not set");
            }
            if (!CLOB_VALUE.equals(types.getClob())) {
                LOG.error("CLOB value was not set");
                fail("CLOB value was not set");
            }
            if (types.getClob2() == null) {
                LOG.error("Clob value was not set");
                fail("Clob value was not set");
            } else {
                long clobLen = types.getClob2().length();
                int len = types.getClob2().getCharacterStream().read(cbuf);
                if ((clobLen != CLOB_VALUE.length())
                        || !(new String(cbuf, 0, len)).equals(CLOB_VALUE)) {
                    LOG.error("Clob value is wrong");
                    fail("Clob value mismatched!");
                }
            }
        } else {
            LOG.error("failed to load object");
            fail("failed to load object");
        }
        _db.commit();
        LOG.debug("OK: BLOB and CLOB fields passed");
    }
}
