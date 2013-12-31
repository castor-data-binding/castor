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
package org.castor.cpa.test.test20;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

/**
 * Abstract test class for key generators with integer values.
 */
public abstract class AbstractTestKeyGenInteger extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(AbstractTestKeyGenInteger.class);
    
    private static final String DBNAME = "test20";
    private static final String MAPPING = "/org/castor/cpa/test/test20/mapping.xml";
    private Database _db;

    public AbstractTestKeyGenInteger(final String name) {
        super(name);
    }

    public final void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public final void tearDown() throws Exception {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
    
    /**
     * The main goal of the test is to verify key generators in the case of "extends" relation
     * between two classes. For each key generator we have a pair of classes: TestXXXObject and
     * TestXXXExtends which use a specific key generator.
     */
    protected final void testOneKeyGen(
            final Class<? extends AbstractKeyGenObjectInteger> objClass,
            final Class<? extends AbstractKeyGenObjectInteger> extClass)
    throws Exception {
        AbstractKeyGenObjectInteger object;
        AbstractKeyGenObjectInteger ext;

        // Open transaction in order to perform JDO operations
        _db.begin();

        // Create first object
        object = objClass.newInstance();
        LOG.debug("Creating first object: " + object);
        _db.create(object);
        LOG.debug("Created first object: " + object);

        // Create second object
        ext = extClass.newInstance();
        LOG.debug("Creating second object: " + ext);
        _db.create(ext);
        LOG.debug("Created second object: " + ext);

        _db.commit();

        _db.begin();

        // Find the first object and remove it 
        //object = (TestKeyGenObject) _db.load( objClass, object.getId() );
        OQLQuery oqlObj = _db.getOQLQuery();
        oqlObj.create("SELECT object FROM " + objClass.getName() + " object WHERE id = $1");
        oqlObj.bind(object.getId());
        QueryResults resObj = oqlObj.execute();
        LOG.debug("Removing first object: " + object);
        if (resObj.hasMore()) {
            object = (AbstractKeyGenObjectInteger) resObj.next();
            _db.remove(object);
            LOG.debug("OK: Removed");
        } else {
            LOG.error("first object not found");
            fail("first object not found");
        }

        // Find the second object and remove it
        //ext = (TestKeyGenObject) _db.load( extClass, ext.getId() );
        OQLQuery oqlExt = _db.getOQLQuery();
        oqlExt.create("SELECT ext FROM " + extClass.getName()
                + " ext WHERE id = $1");
        oqlExt.bind(ext.getId());
        QueryResults resExt = oqlExt.execute();
        LOG.debug("Removing second object: " + ext);
        if (resExt.hasMore()) {
            ext = (AbstractKeyGenObjectInteger) resExt.next();
            _db.remove(ext);
            LOG.debug("OK: Removed");
        } else {
            LOG.error("second object not found");
            fail("second object not found");
        }

        _db.commit();
    }
}
