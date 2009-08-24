/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test70;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;

/**
 * Test for different collection types supported by Castor JDO. This test
 * creates data objects that each has a collection as a field type.
 */
public final class TestCollections extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestCollections.class);
    private static final String DBNAME = "test70";
    private static final String MAPPING = "/org/castor/cpa/test/test70/mapping.xml";
    private Database _db;

    public TestCollections(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE);
    }

    public boolean exclude(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.POSTGRESQL);
    }

    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public void tearDown() throws Exception {
        if (_db.isActive()) {
            _db.rollback();
        }
        _db.close();
    }

    public void testRun() throws Exception {
        runOnce(ColCollection.class);
        runOnce(ColArrayList.class);
        runOnce(ColVector.class);
        runOnce(ColSet.class);
        runOnce(ColMap.class);
        runOnce(ColHashtable.class);
        runSortedSet(ColSortedSet.class);
        runOnce(ColIterator.class);
        runOnce(ColEnumeration.class);

        // TODO[WG]: Causes problems with missing setXXX(ArrayList) method. To re-enable,
        // TODO[WG]: please consult with http://jira.codehaus.org/browse/CASTOR-1147
        // runOnce( TestColAdd.class );

        runArray();
    }

    private void runOnce(final Class < ? extends Col > masterClass) throws Exception {
        LOG.debug("Running...runOnce");

        _db.begin();
        Connection conn = _db.getJdbcConnection();
        // delete everything
        conn.createStatement().executeUpdate("DELETE FROM test70_col");
        conn.createStatement().executeUpdate("DELETE FROM test70_item");
        conn.createStatement().executeUpdate("DELETE FROM test70_comp_item");
        _db.commit();

        // create new TestCol object with elements
        _db.begin();
        Col testCol = masterClass.newInstance();
        testCol.setId(1);
        _db.create(testCol);
        for (int i = 0; i < 5; i++) {
            Item newItem = new Item(100 + i);
            testCol.addItem(newItem);
            _db.create(newItem);
        }
        _db.commit();

        // test if object created properly
        _db.begin();
        testCol = _db.load(masterClass, new Integer(1));
        if (testCol == null) {
            fail("Object creation failed!");
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new Item(100))
                || !testCol.containsItem(new Item(101)) || !testCol.containsItem(new Item(102))
                || !testCol.containsItem(new Item(103)) || !testCol.containsItem(new Item(104))) {
            fail("Related objects creation failed!");
        }

        testCol.removeItem(new Item(100));
        testCol.removeItem(new Item(103));
        Item newItem = new Item(106);
        testCol.addItem(newItem);
        _db.create(newItem);
        newItem = new Item(107);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.commit();

        // test if add and remove work properly.
        _db.begin();
        testCol = _db.load(masterClass, new Integer(1));
        if (testCol == null) {
            fail("Object add/remove failed! " + testCol);
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new Item(106))
                || !testCol.containsItem(new Item(101)) || !testCol.containsItem(new Item(102))
                || !testCol.containsItem(new Item(107)) || !testCol.containsItem(new Item(104))) {
            fail("Related add/remove failed!" + testCol);
        }

        // test if add and remove rollback properly.
        testCol.removeItem(new Item(102));
        testCol.removeItem(new Item(104));
        newItem = new Item(108);
        testCol.addItem(newItem);
        newItem = new Item(109);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.rollback();

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new Item(106))
                || !testCol.containsItem(new Item(101)) || !testCol.containsItem(new Item(102))
                || !testCol.containsItem(new Item(107)) || !testCol.containsItem(new Item(104))) {
            fail("Related add/remove rollback failed!" + testCol);
        }

        // shoud test for update too
    }

    private void runSortedSet(final Class < ColSortedSet > masterClass) throws Exception {
        LOG.debug("Running...runSortedSet");
        LOG.debug("");
        _db.begin();
        Connection conn = _db.getJdbcConnection();
        // delete everything
        conn.createStatement().executeUpdate("DELETE FROM test70_col");
        conn.createStatement().executeUpdate("DELETE FROM test70_item");
        conn.createStatement().executeUpdate("DELETE FROM test70_comp_item");
        _db.commit();

        // create new TestCol object with elements
        _db.begin();
        ColSortedSet testCol = masterClass.newInstance();
        testCol.setId(1);
        _db.create(testCol);
        for (int i = 0; i < 5; i++) {
            ComparableItem newItem = new ComparableItem(100 + i);
            testCol.addItem(newItem);
            _db.create(newItem);
        }
        _db.commit();

        // test if object created properly
        _db.begin();
        testCol = _db.load(masterClass, new Integer(1));
        if (testCol == null) {
            fail("Object creation failed!");
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new ComparableItem(100))
                || !testCol.containsItem(new ComparableItem(101))
                || !testCol.containsItem(new ComparableItem(102))
                || !testCol.containsItem(new ComparableItem(103))
                || !testCol.containsItem(new ComparableItem(104))) {
            fail("Related objects creation failed!");
        }

        testCol.removeItem(new ComparableItem(100));
        testCol.removeItem(new ComparableItem(103));
        ComparableItem newItem = new ComparableItem(106);
        testCol.addItem(newItem);
        _db.create(newItem);
        newItem = new ComparableItem(107);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.commit();

        // test if add and remove work properly.
        _db.begin();
        testCol = _db.load(masterClass, new Integer(1));
        if (testCol == null) {
            fail("Object add/remove failed! " + testCol);
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new ComparableItem(106))
                || !testCol.containsItem(new ComparableItem(101))
                || !testCol.containsItem(new ComparableItem(102))
                || !testCol.containsItem(new ComparableItem(107))
                || !testCol.containsItem(new ComparableItem(104))) {
            fail("Related add/remove failed!" + testCol);
        }

        // test if add and remove rollback properly.
        testCol.removeItem(new ComparableItem(102));
        testCol.removeItem(new ComparableItem(104));
        newItem = new ComparableItem(108);
        testCol.addItem(newItem);
        newItem = new ComparableItem(109);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.rollback();

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new ComparableItem(106))
                || !testCol.containsItem(new ComparableItem(101))
                || !testCol.containsItem(new ComparableItem(102))
                || !testCol.containsItem(new ComparableItem(107))
                || !testCol.containsItem(new ComparableItem(104))) {
            fail("Related add/remove rollback failed!" + testCol);
        }

        // shoud test for update too
    }

    /**
     * Special case for array
     */
    private void runArray() throws Exception {
        LOG.debug("Running...runArray");
        LOG.debug("");

        _db.begin();
        Connection conn = _db.getJdbcConnection();
        // delete everything
        conn.createStatement().executeUpdate("DELETE FROM test70_col");
        conn.createStatement().executeUpdate("DELETE FROM test70_item");
        _db.commit();

        // create new TestCol object with elements
        _db.begin();
        ColArray testCol = new ColArray();
        testCol.setId(1);
        _db.create(testCol);
        Item[] items = new Item[5];
        for (int i = 0; i < 5; i++) {
            Item newItem = new Item(100 + i);
            newItem.setTestCol(testCol);
            items[i] = newItem;
            _db.create(newItem);
        }
        testCol.setItems(items);
        _db.commit();
        // test if object created properly
        _db.begin();
        testCol = _db.load(ColArray.class, new Integer(1));
        if (testCol == null) {
            fail("Object creation failed!");
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new Item(100))
                || !testCol.containsItem(new Item(101)) || !testCol.containsItem(new Item(102))
                || !testCol.containsItem(new Item(103)) || !testCol.containsItem(new Item(104))) {
            fail("Related objects creation failed!");
        }

        testCol.removeItem(new Item(100));
        testCol.removeItem(new Item(103));

        // update array
        Item[] oldItems = testCol.getItems();
        Item[] newItems = new Item[oldItems.length + 2];
        System.arraycopy(oldItems, 0, newItems, 0, oldItems.length);

        Item newItem = new Item(106);
        newItem.setTestCol(testCol);
        newItems[newItems.length - 2] = newItem;
        _db.create(newItem);

        newItem = new Item(107);
        newItem.setTestCol(testCol);
        newItems[newItems.length - 1] = newItem;
        _db.create(newItem);

        testCol.setItems(newItems);

        _db.commit();

        // test if add and remove work properly.
        _db.begin();
        testCol = _db.load(ColArray.class, new Integer(1));
        if (testCol == null) {
            fail("Object add/remove failed! " + testCol);
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new Item(106))
                || !testCol.containsItem(new Item(101)) || !testCol.containsItem(new Item(102))
                || !testCol.containsItem(new Item(107)) || !testCol.containsItem(new Item(104))) {
            fail("Related add/remove failed!" + testCol);
        }

        // test if add and remove rollback properly.
        testCol.removeItem(new Item(102));
        testCol.removeItem(new Item(104));

        // update array
        oldItems = testCol.getItems();
        newItems = new Item[oldItems.length + 2];
        System.arraycopy(oldItems, 0, newItems, 0, oldItems.length);

        newItem = new Item(108);
        newItem.setTestCol(testCol);
        newItems[newItems.length - 2] = newItem;
        _db.create(newItem);

        newItem = new Item(109);
        newItem.setTestCol(testCol);
        newItems[newItems.length - 1] = newItem;
        _db.create(newItem);

        testCol.setItems(newItems);

        _db.rollback();

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new Item(106))
                || !testCol.containsItem(new Item(101)) || !testCol.containsItem(new Item(102))
                || !testCol.containsItem(new Item(107)) || !testCol.containsItem(new Item(104))) {
            fail("Related add/remove rollback failed!" + testCol);
        }
        // shoud test for update too
    }
}
