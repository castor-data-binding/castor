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
package org.castor.cpa.test.test98;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;

/**
 * Tests that modification to read only objects are not persist in the database.
 */
public final class TestPolymorphismInAThreadedEnv extends CPATestCase {

    private static final Log LOG = LogFactory
            .getLog(TestPolymorphismInAThreadedEnv.class);

    private static final int REPS = 100;

    private static final String DBNAME = "test98";
    private static final String MAPPING = "/org/castor/cpa/test/test98/mapping.xml";

    public TestPolymorphismInAThreadedEnv(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL);
    }

    public static Container loadContainer() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        Container container;
        try {
            container = db.load(Container.class, "200");
            db.commit();
        } catch (Exception ex) {
            db.rollback();
            throw ex;
        } finally {
            db.close();
        }
        return container;
    }

    private Derived loadDerived() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        Derived derived;
        try {
            derived = db.load(Derived.class, "100");
            db.commit();
        } catch (Exception ex) {
            db.rollback();
            throw ex;
        } finally {
            db.close();
        }
        return derived;
    }

    private Container loadContainerThenDerived() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        Container container = null;
        try {
            container = db.load(Container.class, "200");
            db.load(Derived.class, "100");
            db.commit();
        } catch (Exception ex) {
            db.rollback();
            throw ex;
        } finally {
            db.close();
        }
        return container;
    }

    public void testLoadContainer() throws Exception {
        LOG.debug("First we load a Container in its own transaction");
        Object o = loadContainer();
        LOG.debug("loadContainer: " + o);
    }

    public void testLoadDerived() throws Exception {
        LOG.debug("Second we load a Derived in its own transaction");
        Object o = loadDerived();
        LOG.debug("loadDerived: " + o);
    }

    public void testLoadContainerThenDerived() throws Exception {
        LOG.debug("Third we load a Container and a Derived in one transaction");
        Object o = loadContainerThenDerived();
        LOG.debug("loadContainerThenDerived: " + o);
    }

    public void testLoadContainerAndDerivedThreaded() {
        int count = 0;
        try {
            LOG.debug("Forth we load Container and Derived in seperate "
                    + "threads in their own transactions");

            Thread thread = new Thread(new TreadedContainerLoader());
            thread.start();

            for (int i = 0; i < REPS; i++) {
                count = i;
                loadDerived();
            }
            LOG.debug("First thread successfully loaded " + (count + 1)
                    + " Derived");

            thread.join();
        } catch (Exception ex) {
            LOG.error("Exception on first thread loading Derived on "
                    + (count + 1) + "th try", ex);
        }
    }
}
