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
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SQL_SERVER)
            || (engine == DatabaseEngineType.SAPDB);
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
        runCollection();
        runArrayList();
        runEnumeration();
        runHashtable();
        runIterator();
        runMap();
        runSet();
        runSortedSet();
        runVector();

        // TODO[WG]: Causes problems with missing setXXX(ArrayList) method. To re-enable,
        // TODO[WG]: please consult with http://jira.codehaus.org/browse/CASTOR-1147
        // runOnce( TestColAdd.class );

        runArray();
    }

    private void runArrayList() throws Exception {
        LOG.debug("Running...arrayList");
        
        deleteAll();

        // create new TestCol object with elements
        _db.begin();
        TestArrayList testCol = new TestArrayList();
        testCol.setId(1);
        _db.create(testCol);
        for (int i = 0; i < 5; i++) {
            ArrayListItem newItem = new ArrayListItem(100 + i);
            testCol.addItem(newItem);
            _db.create(newItem);
        }
        _db.commit();

        // test if object created properly
        _db.begin();
        testCol = _db.load(TestArrayList.class, new Integer(1));
        if (testCol == null) {
            fail("Object creation failed!");
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new ArrayListItem(100))
                || !testCol.containsItem(new ArrayListItem(101))
                || !testCol.containsItem(new ArrayListItem(102))
                || !testCol.containsItem(new ArrayListItem(103))
                || !testCol.containsItem(new ArrayListItem(104))) {
            fail("Related objects creation failed!");
        }

        testCol.removeItem(new ArrayListItem(100));
        testCol.removeItem(new ArrayListItem(103));
        ArrayListItem newItem = new ArrayListItem(106);
        testCol.addItem(newItem);
        _db.create(newItem);
        newItem = new ArrayListItem(107);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.commit();

        // test if add and remove work properly.
        _db.begin();
        testCol = _db.load(TestArrayList.class, new Integer(1));
        if (testCol == null) {
            fail("Object add/remove failed! " + testCol);
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new ArrayListItem(106))
                || !testCol.containsItem(new ArrayListItem(101))
                || !testCol.containsItem(new ArrayListItem(102))
                || !testCol.containsItem(new ArrayListItem(107))
                || !testCol.containsItem(new ArrayListItem(104))) {
            fail("Related add/remove failed!" + testCol);
        }

        // test if add and remove rollback properly.
        testCol.removeItem(new ArrayListItem(102));
        testCol.removeItem(new ArrayListItem(104));
        newItem = new ArrayListItem(108);
        testCol.addItem(newItem);
        newItem = new ArrayListItem(109);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.rollback();

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new ArrayListItem(106))
                || !testCol.containsItem(new ArrayListItem(101))
                || !testCol.containsItem(new ArrayListItem(102))
                || !testCol.containsItem(new ArrayListItem(107))
                || !testCol.containsItem(new ArrayListItem(104))) {
            fail("Related add/remove rollback failed!" + testCol);
        }
    }

    private void runCollection() throws Exception {
        LOG.debug("Running...collection");

        deleteAll();

        // create new TestCol object with elements
        _db.begin();
        TestCollection testCol = new TestCollection();
        testCol.setId(1);
        _db.create(testCol);
        for (int i = 0; i < 5; i++) {
            CollectionItem newItem = new CollectionItem(100 + i);
            testCol.addItem(newItem);
            _db.create(newItem);
        }
        _db.commit();

        // test if object created properly
        _db.begin();
        testCol = _db.load(TestCollection.class, new Integer(1));
        if (testCol == null) {
            fail("Object creation failed!");
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new CollectionItem(100))
                || !testCol.containsItem(new CollectionItem(101))
                || !testCol.containsItem(new CollectionItem(102))
                || !testCol.containsItem(new CollectionItem(103))
                || !testCol.containsItem(new CollectionItem(104))) {
            fail("Related objects creation failed!");
        }

        testCol.removeItem(new CollectionItem(100));
        testCol.removeItem(new CollectionItem(103));
        CollectionItem newItem = new CollectionItem(106);
        testCol.addItem(newItem);
        _db.create(newItem);
        newItem = new CollectionItem(107);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.commit();

        // test if add and remove work properly.
        _db.begin();
        testCol = _db.load(TestCollection.class, new Integer(1));
        if (testCol == null) {
            fail("Object add/remove failed! " + testCol);
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new CollectionItem(106))
                || !testCol.containsItem(new CollectionItem(101))
                || !testCol.containsItem(new CollectionItem(102))
                || !testCol.containsItem(new CollectionItem(107))
                || !testCol.containsItem(new CollectionItem(104))) {
            fail("Related add/remove failed!" + testCol);
        }

        // test if add and remove rollback properly.
        testCol.removeItem(new CollectionItem(102));
        testCol.removeItem(new CollectionItem(104));
        newItem = new CollectionItem(108);
        testCol.addItem(newItem);
        newItem = new CollectionItem(109);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.rollback();

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new CollectionItem(106))
                || !testCol.containsItem(new CollectionItem(101))
                || !testCol.containsItem(new CollectionItem(102))
                || !testCol.containsItem(new CollectionItem(107))
                || !testCol.containsItem(new CollectionItem(104))) {
            fail("Related add/remove rollback failed!" + testCol);
        }

        // shoud test for update too
    }

    private void runEnumeration() throws Exception {
        LOG.debug("Running...enumeration");

        deleteAll();

        // create new TestCol object with elements
        _db.begin();
        TestEnumeration testCol = new TestEnumeration();
        testCol.setId(1);
        _db.create(testCol);
        for (int i = 0; i < 5; i++) {
            EnumerationItem newItem = new EnumerationItem(100 + i);
            testCol.addItem(newItem);
            _db.create(newItem);
        }
        _db.commit();

        // test if object created properly
        _db.begin();
        testCol = _db.load(TestEnumeration.class, new Integer(1));
        if (testCol == null) {
            fail("Object creation failed!");
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new EnumerationItem(100))
                || !testCol.containsItem(new EnumerationItem(101))
                || !testCol.containsItem(new EnumerationItem(102))
                || !testCol.containsItem(new EnumerationItem(103))
                || !testCol.containsItem(new EnumerationItem(104))) {
            fail("Related objects creation failed!");
        }

        testCol.removeItem(new EnumerationItem(100));
        testCol.removeItem(new EnumerationItem(103));
        EnumerationItem newItem = new EnumerationItem(106);
        testCol.addItem(newItem);
        _db.create(newItem);
        newItem = new EnumerationItem(107);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.commit();

        // test if add and remove work properly.
        _db.begin();
        testCol = _db.load(TestEnumeration.class, new Integer(1));
        if (testCol == null) {
            fail("Object add/remove failed! " + testCol);
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new EnumerationItem(106))
                || !testCol.containsItem(new EnumerationItem(101))
                || !testCol.containsItem(new EnumerationItem(102))
                || !testCol.containsItem(new EnumerationItem(107))
                || !testCol.containsItem(new EnumerationItem(104))) {
            fail("Related add/remove failed!" + testCol);
        }

        // test if add and remove rollback properly.
        testCol.removeItem(new EnumerationItem(102));
        testCol.removeItem(new EnumerationItem(104));
        newItem = new EnumerationItem(108);
        testCol.addItem(newItem);
        newItem = new EnumerationItem(109);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.rollback();

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new EnumerationItem(106))
                || !testCol.containsItem(new EnumerationItem(101))
                || !testCol.containsItem(new EnumerationItem(102))
                || !testCol.containsItem(new EnumerationItem(107))
                || !testCol.containsItem(new EnumerationItem(104))) {
            fail("Related add/remove rollback failed!" + testCol);
        }

        // shoud test for update too
    }
    
    private void runHashtable() throws Exception {
        LOG.debug("Running...enumeration");

        deleteAll();

        // create new TestCol object with elements
        _db.begin();
        TestHashtable testCol = new TestHashtable();
        testCol.setId(1);
        _db.create(testCol);
        for (int i = 0; i < 5; i++) {
            HashtableItem newItem = new HashtableItem(100 + i);
            testCol.addItem(newItem);
            _db.create(newItem);
        }
        _db.commit();

        // test if object created properly
        _db.begin();
        testCol = _db.load(TestHashtable.class, new Integer(1));
        if (testCol == null) {
            fail("Object creation failed!");
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new HashtableItem(100))
                || !testCol.containsItem(new HashtableItem(101))
                || !testCol.containsItem(new HashtableItem(102))
                || !testCol.containsItem(new HashtableItem(103))
                || !testCol.containsItem(new HashtableItem(104))) {
            fail("Related objects creation failed!");
        }

        testCol.removeItem(new HashtableItem(100));
        testCol.removeItem(new HashtableItem(103));
        HashtableItem newItem = new HashtableItem(106);
        testCol.addItem(newItem);
        _db.create(newItem);
        newItem = new HashtableItem(107);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.commit();

        // test if add and remove work properly.
        _db.begin();
        testCol = _db.load(TestHashtable.class, new Integer(1));
        if (testCol == null) {
            fail("Object add/remove failed! " + testCol);
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new HashtableItem(106))
                || !testCol.containsItem(new HashtableItem(101))
                || !testCol.containsItem(new HashtableItem(102))
                || !testCol.containsItem(new HashtableItem(107))
                || !testCol.containsItem(new HashtableItem(104))) {
            fail("Related add/remove failed!" + testCol);
        }

        // test if add and remove rollback properly.
        testCol.removeItem(new HashtableItem(102));
        testCol.removeItem(new HashtableItem(104));
        newItem = new HashtableItem(108);
        testCol.addItem(newItem);
        newItem = new HashtableItem(109);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.rollback();

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new HashtableItem(106))
                || !testCol.containsItem(new HashtableItem(101))
                || !testCol.containsItem(new HashtableItem(102))
                || !testCol.containsItem(new HashtableItem(107))
                || !testCol.containsItem(new HashtableItem(104))) {
            fail("Related add/remove rollback failed!" + testCol);
        }

        // shoud test for update too
    }
    
    private void runIterator() throws Exception {
        LOG.debug("Running...iterator");

        deleteAll();

        // create new TestCol object with elements
        _db.begin();
        TestIterator testCol = new TestIterator();
        testCol.setId(1);
        _db.create(testCol);
        for (int i = 0; i < 5; i++) {
            IteratorItem newItem = new IteratorItem(100 + i);
            testCol.addItem(newItem);
            _db.create(newItem);
        }
        _db.commit();

        // test if object created properly
        _db.begin();
        testCol = _db.load(TestIterator.class, new Integer(1));
        if (testCol == null) {
            fail("Object creation failed!");
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new IteratorItem(100))
                || !testCol.containsItem(new IteratorItem(101))
                || !testCol.containsItem(new IteratorItem(102))
                || !testCol.containsItem(new IteratorItem(103))
                || !testCol.containsItem(new IteratorItem(104))) {
            fail("Related objects creation failed!");
        }

        testCol.removeItem(new IteratorItem(100));
        testCol.removeItem(new IteratorItem(103));
        IteratorItem newItem = new IteratorItem(106);
        testCol.addItem(newItem);
        _db.create(newItem);
        newItem = new IteratorItem(107);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.commit();

        // test if add and remove work properly.
        _db.begin();
        testCol = _db.load(TestIterator.class, new Integer(1));
        if (testCol == null) {
            fail("Object add/remove failed! " + testCol);
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new IteratorItem(106))
                || !testCol.containsItem(new IteratorItem(101))
                || !testCol.containsItem(new IteratorItem(102))
                || !testCol.containsItem(new IteratorItem(107))
                || !testCol.containsItem(new IteratorItem(104))) {
            fail("Related add/remove failed!" + testCol);
        }

        // test if add and remove rollback properly.
        testCol.removeItem(new IteratorItem(102));
        testCol.removeItem(new IteratorItem(104));
        newItem = new IteratorItem(108);
        testCol.addItem(newItem);
        newItem = new IteratorItem(109);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.rollback();

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new IteratorItem(106))
                || !testCol.containsItem(new IteratorItem(101))
                || !testCol.containsItem(new IteratorItem(102))
                || !testCol.containsItem(new IteratorItem(107))
                || !testCol.containsItem(new IteratorItem(104))) {
            fail("Related add/remove rollback failed!" + testCol);
        }

        // shoud test for update too
    }

    private void runMap() throws Exception {
        LOG.debug("Running...map");

        deleteAll();

        // create new TestCol object with elements
        _db.begin();
        TestMap testCol = new TestMap();
        testCol.setId(1);
        _db.create(testCol);
        for (int i = 0; i < 5; i++) {
            MapItem newItem = new MapItem(100 + i);
            testCol.addItem(newItem);
            _db.create(newItem);
        }
        _db.commit();

        // test if object created properly
        _db.begin();
        testCol = _db.load(TestMap.class, new Integer(1));
        if (testCol == null) {
            fail("Object creation failed!");
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new MapItem(100))
                || !testCol.containsItem(new MapItem(101))
                || !testCol.containsItem(new MapItem(102))
                || !testCol.containsItem(new MapItem(103))
                || !testCol.containsItem(new MapItem(104))) {
            fail("Related objects creation failed!");
        }

        testCol.removeItem(new MapItem(100));
        testCol.removeItem(new MapItem(103));
        MapItem newItem = new MapItem(106);
        testCol.addItem(newItem);
        _db.create(newItem);
        newItem = new MapItem(107);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.commit();

        // test if add and remove work properly.
        _db.begin();
        testCol = _db.load(TestMap.class, new Integer(1));
        if (testCol == null) {
            fail("Object add/remove failed! " + testCol);
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new MapItem(106))
                || !testCol.containsItem(new MapItem(101))
                || !testCol.containsItem(new MapItem(102))
                || !testCol.containsItem(new MapItem(107))
                || !testCol.containsItem(new MapItem(104))) {
            fail("Related add/remove failed!" + testCol);
        }

        // test if add and remove rollback properly.
        testCol.removeItem(new MapItem(102));
        testCol.removeItem(new MapItem(104));
        newItem = new MapItem(108);
        testCol.addItem(newItem);
        newItem = new MapItem(109);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.rollback();

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new MapItem(106))
                || !testCol.containsItem(new MapItem(101))
                || !testCol.containsItem(new MapItem(102))
                || !testCol.containsItem(new MapItem(107))
                || !testCol.containsItem(new MapItem(104))) {
            fail("Related add/remove rollback failed!" + testCol);
        }

        // shoud test for update too
    }
    
    private void runSet() throws Exception {
        LOG.debug("Running...set");

        deleteAll();

        // create new TestCol object with elements
        _db.begin();
        TestSet testCol = new TestSet();
        testCol.setId(1);
        _db.create(testCol);
        for (int i = 0; i < 5; i++) {
            SetItem newItem = new SetItem(100 + i);
            testCol.addItem(newItem);
            _db.create(newItem);
        }
        _db.commit();

        // test if object created properly
        _db.begin();
        testCol = _db.load(TestSet.class, new Integer(1));
        if (testCol == null) {
            fail("Object creation failed!");
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new SetItem(100))
                || !testCol.containsItem(new SetItem(101))
                || !testCol.containsItem(new SetItem(102))
                || !testCol.containsItem(new SetItem(103))
                || !testCol.containsItem(new SetItem(104))) {
            fail("Related objects creation failed!");
        }

        testCol.removeItem(new SetItem(100));
        testCol.removeItem(new SetItem(103));
        SetItem newItem = new SetItem(106);
        testCol.addItem(newItem);
        _db.create(newItem);
        newItem = new SetItem(107);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.commit();

        // test if add and remove work properly.
        _db.begin();
        testCol = _db.load(TestSet.class, new Integer(1));
        if (testCol == null) {
            fail("Object add/remove failed! " + testCol);
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new SetItem(106))
                || !testCol.containsItem(new SetItem(101))
                || !testCol.containsItem(new SetItem(102))
                || !testCol.containsItem(new SetItem(107))
                || !testCol.containsItem(new SetItem(104))) {
            fail("Related add/remove failed!" + testCol);
        }

        // test if add and remove rollback properly.
        testCol.removeItem(new SetItem(102));
        testCol.removeItem(new SetItem(104));
        newItem = new SetItem(108);
        testCol.addItem(newItem);
        newItem = new SetItem(109);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.rollback();

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new SetItem(106))
                || !testCol.containsItem(new SetItem(101))
                || !testCol.containsItem(new SetItem(102))
                || !testCol.containsItem(new SetItem(107))
                || !testCol.containsItem(new SetItem(104))) {
            fail("Related add/remove rollback failed!" + testCol);
        }

        // shoud test for update too
    }

    private void runSortedSet() throws Exception {
        LOG.debug("Running...runSortedSet");
        
        deleteAll();

        // create new TestCol object with elements
        _db.begin();
        TestSortedSet testCol = new TestSortedSet();
        testCol.setId(1);
        _db.create(testCol);
        for (int i = 0; i < 5; i++) {
            SortedSetItem newItem = new SortedSetItem(100 + i);
            testCol.addItem(newItem);
            _db.create(newItem);
        }
        _db.commit();

        // test if object created properly
        _db.begin();
        testCol = _db.load(TestSortedSet.class, new Integer(1));
        if (testCol == null) {
            fail("Object creation failed!");
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new SortedSetItem(100))
                || !testCol.containsItem(new SortedSetItem(101))
                || !testCol.containsItem(new SortedSetItem(102))
                || !testCol.containsItem(new SortedSetItem(103))
                || !testCol.containsItem(new SortedSetItem(104))) {
            fail("Related objects creation failed!");
        }

        testCol.removeItem(new SortedSetItem(100));
        testCol.removeItem(new SortedSetItem(103));
        SortedSetItem newItem = new SortedSetItem(106);
        testCol.addItem(newItem);
        _db.create(newItem);
        newItem = new SortedSetItem(107);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.commit();

        // test if add and remove work properly.
        _db.begin();
        testCol = _db.load(TestSortedSet.class, new Integer(1));
        if (testCol == null) {
            fail("Object add/remove failed! " + testCol);
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new SortedSetItem(106))
                || !testCol.containsItem(new SortedSetItem(101))
                || !testCol.containsItem(new SortedSetItem(102))
                || !testCol.containsItem(new SortedSetItem(107))
                || !testCol.containsItem(new SortedSetItem(104))) {
            fail("Related add/remove failed!" + testCol);
        }

        // test if add and remove rollback properly.
        testCol.removeItem(new SortedSetItem(102));
        testCol.removeItem(new SortedSetItem(104));
        newItem = new SortedSetItem(108);
        testCol.addItem(newItem);
        newItem = new SortedSetItem(109);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.rollback();

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new SortedSetItem(106))
                || !testCol.containsItem(new SortedSetItem(101))
                || !testCol.containsItem(new SortedSetItem(102))
                || !testCol.containsItem(new SortedSetItem(107))
                || !testCol.containsItem(new SortedSetItem(104))) {
            fail("Related add/remove rollback failed!" + testCol);
        }

        // shoud test for update too
    }
    
    private void runVector() throws Exception {
        LOG.debug("Running...vector");
        
        deleteAll();

        // create new TestCol object with elements
        _db.begin();
        TestVector testCol = new TestVector();
        testCol.setId(1);
        _db.create(testCol);
        for (int i = 0; i < 5; i++) {
            VectorItem newItem = new VectorItem(100 + i);
            testCol.addItem(newItem);
            _db.create(newItem);
        }
        _db.commit();

        // test if object created properly
        _db.begin();
        testCol = _db.load(TestVector.class, new Integer(1));
        if (testCol == null) {
            fail("Object creation failed!");
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new VectorItem(100))
                || !testCol.containsItem(new VectorItem(101))
                || !testCol.containsItem(new VectorItem(102))
                || !testCol.containsItem(new VectorItem(103))
                || !testCol.containsItem(new VectorItem(104))) {
            fail("Related objects creation failed!");
        }

        testCol.removeItem(new VectorItem(100));
        testCol.removeItem(new VectorItem(103));
        VectorItem newItem = new VectorItem(106);
        testCol.addItem(newItem);
        _db.create(newItem);
        newItem = new VectorItem(107);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.commit();

        // test if add and remove work properly.
        _db.begin();
        testCol = _db.load(TestVector.class, new Integer(1));
        if (testCol == null) {
            fail("Object add/remove failed! " + testCol);
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new VectorItem(106))
                || !testCol.containsItem(new VectorItem(101))
                || !testCol.containsItem(new VectorItem(102))
                || !testCol.containsItem(new VectorItem(107))
                || !testCol.containsItem(new VectorItem(104))) {
            fail("Related add/remove failed!" + testCol);
        }

        // test if add and remove rollback properly.
        testCol.removeItem(new VectorItem(102));
        testCol.removeItem(new VectorItem(104));
        newItem = new VectorItem(108);
        testCol.addItem(newItem);
        newItem = new VectorItem(109);
        testCol.addItem(newItem);
        _db.create(newItem);
        _db.rollback();

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new VectorItem(106))
                || !testCol.containsItem(new VectorItem(101))
                || !testCol.containsItem(new VectorItem(102))
                || !testCol.containsItem(new VectorItem(107))
                || !testCol.containsItem(new VectorItem(104))) {
            fail("Related add/remove rollback failed!" + testCol);
        }

        // shoud test for update too
    }

    /**
     * Special case for array
     */
    private void runArray() throws Exception {
        LOG.debug("Running...runArray");
        
        deleteAll();

        // create new TestCol object with elements
        _db.begin();
        TestArray testCol = new TestArray();
        testCol.setId(1);
        _db.create(testCol);
        ArrayItem[] items = new ArrayItem[5];
        for (int i = 0; i < 5; i++) {
            ArrayItem newItem = new ArrayItem(100 + i);
            newItem.setTestCol(testCol);
            items[i] = newItem;
            _db.create(newItem);
        }
        testCol.setItems(items);
        _db.commit();
        // test if object created properly
        _db.begin();
        testCol = _db.load(TestArray.class, new Integer(1));
        if (testCol == null) {
            fail("Object creation failed!");
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new ArrayItem(100))
                || !testCol.containsItem(new ArrayItem(101))
                || !testCol.containsItem(new ArrayItem(102))
                || !testCol.containsItem(new ArrayItem(103))
                || !testCol.containsItem(new ArrayItem(104))) {
            fail("Related objects creation failed!");
        }

        testCol.removeItem(new ArrayItem(100));
        testCol.removeItem(new ArrayItem(103));

        // update array
        ArrayItem[] oldItems = testCol.getItems();
        ArrayItem[] newItems = new ArrayItem[oldItems.length + 2];
        System.arraycopy(oldItems, 0, newItems, 0, oldItems.length);

        ArrayItem newItem = new ArrayItem(106);
        newItem.setTestCol(testCol);
        newItems[newItems.length - 2] = newItem;
        _db.create(newItem);

        newItem = new ArrayItem(107);
        newItem.setTestCol(testCol);
        newItems[newItems.length - 1] = newItem;
        _db.create(newItem);

        testCol.setItems(newItems);

        _db.commit();

        // test if add and remove work properly.
        _db.begin();
        testCol = _db.load(TestArray.class, new Integer(1));
        if (testCol == null) {
            fail("Object add/remove failed! " + testCol);
        }

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new ArrayItem(106))
                || !testCol.containsItem(new ArrayItem(101))
                || !testCol.containsItem(new ArrayItem(102))
                || !testCol.containsItem(new ArrayItem(107))
                || !testCol.containsItem(new ArrayItem(104))) {
            fail("Related add/remove failed!" + testCol);
        }

        // test if add and remove rollback properly.
        testCol.removeItem(new ArrayItem(102));
        testCol.removeItem(new ArrayItem(104));

        // update array
        oldItems = testCol.getItems();
        newItems = new ArrayItem[oldItems.length + 2];
        System.arraycopy(oldItems, 0, newItems, 0, oldItems.length);

        newItem = new ArrayItem(108);
        newItem.setTestCol(testCol);
        newItems[newItems.length - 2] = newItem;
        _db.create(newItem);

        newItem = new ArrayItem(109);
        newItem.setTestCol(testCol);
        newItems[newItems.length - 1] = newItem;
        _db.create(newItem);

        testCol.setItems(newItems);

        _db.rollback();

        if ((testCol.itemSize() != 5) || !testCol.containsItem(new ArrayItem(106))
                || !testCol.containsItem(new ArrayItem(101))
                || !testCol.containsItem(new ArrayItem(102))
                || !testCol.containsItem(new ArrayItem(107))
                || !testCol.containsItem(new ArrayItem(104))) {
            fail("Related add/remove rollback failed!" + testCol);
        }
        // shoud test for update too
    }

    private void deleteAll() throws Exception {
        _db.begin();
        Connection conn = _db.getJdbcConnection();
        // delete everything
        conn.createStatement().executeUpdate("DELETE FROM test70_col");
        conn.createStatement().executeUpdate("DELETE FROM test70_item");
        conn.createStatement().executeUpdate("DELETE FROM test70_comp_item");
        _db.commit();
    }
}
