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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package ctf.jdo.tc7x;

import harness.CastorTestCase;
import harness.TestHarness;

import java.sql.Connection;

import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;

/**
 * Test for different collection types supported by Castor JDO.
 * This test creates data objects that each has a collection as
 * a field type.
 */
public final class TestCollections extends CastorTestCase {
    private Database       _db;
    private Connection     _conn;
    private JDOCategory    _category;

    public TestCollections(final TestHarness category) {
        super(category, "TC70", "Collections");
        _category = (JDOCategory) category;
    }

    public void setUp() throws Exception {
        _db = _category.getDatabase();
        _conn = _category.getJDBCConnection();
        _conn.setAutoCommit(false);
    }

    public void runTest() throws Exception {
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

    public void runOnce(final Class masterClass) throws Exception {
        stream.println("Running...");
        stream.println("");

        // delete everything
        _conn.createStatement().executeUpdate("DELETE FROM tc7x_col");
        _conn.createStatement().executeUpdate("DELETE FROM tc7x_item");
        _conn.createStatement().executeUpdate("DELETE FROM tc7x_comp_item");
        _conn.commit();

        // create new TestCol object with elements
        _db.begin();
        Col testCol = (Col) masterClass.newInstance();
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
        testCol = (Col) _db.load(masterClass, new Integer(1));
        if (testCol == null) { fail("Object creation failed!"); }
            
        if ((testCol.itemSize() != 5)
                || !testCol.containsItem(new Item(100))
                || !testCol.containsItem(new Item(101))
                || !testCol.containsItem(new Item(102))
                || !testCol.containsItem(new Item(103))
                || !testCol.containsItem(new Item(104))) {
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
        testCol = (Col) _db.load(masterClass, new Integer(1));
        if (testCol == null) { fail("Object add/remove failed! " + testCol); }

        if ((testCol.itemSize() != 5)
                || !testCol.containsItem(new Item(106))
                || !testCol.containsItem(new Item(101))
                || !testCol.containsItem(new Item(102))
                || !testCol.containsItem(new Item(107))
                || !testCol.containsItem(new Item(104))) {
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

        if ((testCol.itemSize() != 5)
                || !testCol.containsItem(new Item(106))
                || !testCol.containsItem(new Item(101))
                || !testCol.containsItem(new Item(102))
                || !testCol.containsItem(new Item(107))
                || !testCol.containsItem(new Item(104))) {
            fail("Related add/remove rollback failed!" + testCol);
        }

        // shoud test for update too
    } 

    public void runSortedSet(final Class masterClass) throws Exception {
        stream.println("Running...");
        stream.println("");
        
        // delete everything
        _conn.createStatement().executeUpdate("DELETE FROM tc7x_col");
        _conn.createStatement().executeUpdate("DELETE FROM tc7x_item");
        _conn.createStatement().executeUpdate("DELETE FROM tc7x_comp_item");
        _conn.commit();
        
        // create new TestCol object with elements
        _db.begin();
        ColSortedSet testCol = (ColSortedSet) masterClass.newInstance();
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
        testCol = (ColSortedSet) _db.load(masterClass, new Integer(1));
        if (testCol == null) { fail("Object creation failed!"); }
        
        if ((testCol.itemSize() != 5)
                || !testCol.containsItem(new ComparableItem(100))
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
        testCol = (ColSortedSet) _db.load(masterClass, new Integer(1));
        if (testCol == null) { fail("Object add/remove failed! " + testCol); }
        
        if ((testCol.itemSize() != 5)
                || !testCol.containsItem(new ComparableItem(106))
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
        
        if ((testCol.itemSize() != 5)
                || !testCol.containsItem(new ComparableItem(106))
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
    public void runArray() throws Exception {
        stream.println("Running...");
        stream.println("");

        // delete everything
        _conn.createStatement().executeUpdate("DELETE FROM tc7x_col");
        _conn.createStatement().executeUpdate("DELETE FROM tc7x_item");
        _conn.commit();

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
        testCol = (ColArray) _db.load(ColArray.class, new Integer(1));
        if (testCol == null) { fail("Object creation failed!"); }
            
        if ((testCol.itemSize() != 5)
                || !testCol.containsItem(new Item(100))
                || !testCol.containsItem(new Item(101))
                || !testCol.containsItem(new Item(102))
                || !testCol.containsItem(new Item(103))
                || !testCol.containsItem(new Item(104))) {
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
        testCol = (ColArray) _db.load(ColArray.class, new Integer(1));
        if (testCol == null) { fail("Object add/remove failed! " + testCol); }

        if ((testCol.itemSize() != 5)
                || !testCol.containsItem(new Item(106))
                || !testCol.containsItem(new Item(101))
                || !testCol.containsItem(new Item(102))
                || !testCol.containsItem(new Item(107))
                || !testCol.containsItem(new Item(104))) {
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

        if ((testCol.itemSize() != 5)
                || !testCol.containsItem(new Item(106))
                || !testCol.containsItem(new Item(101))
                || !testCol.containsItem(new Item(102))
                || !testCol.containsItem(new Item(107))
                || !testCol.containsItem(new Item(104))) {
            fail("Related add/remove rollback failed!" + testCol);
        }
        // shoud test for update too
    } 

    public void tearDown() throws Exception {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
        _conn.close();
    }
}


