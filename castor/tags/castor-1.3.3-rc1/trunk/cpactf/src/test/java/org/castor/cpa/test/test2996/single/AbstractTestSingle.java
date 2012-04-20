/*
 * Copyright 2009 Tobias Hochwallner, Ralf Joachim
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
package org.castor.cpa.test.test2996.single;

import java.io.FileInputStream;
import java.sql.Connection;

import org.castor.cpa.test.framework.CPATestCase;
import org.dbunit.Assertion;
import org.dbunit.DefaultDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.XmlDataSet;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.MappingException;

/**
 * Abstract base class for testing XML and class mapping with a single class. 
 */
public abstract class AbstractTestSingle extends CPATestCase {
    private static final String DATA_SET_FILE_SETUP = "dbunit-setup.xml";
    private static final String DATA_SET_FILE_INSERTED = "dbunit-inserted.xml";
    private static final String DATA_SET_FILE_UPDATED = "dbunit-updated.xml";
    private static final String DATA_SET_FILE_EMTY = "dbunit-empty.xml";

    private JDOManager _jdo;

    public AbstractTestSingle(final String name) {
        super(name);
    }

    protected abstract JDOManager getJDOManager() throws MappingException;

    /**
     * Creates data objects used by these tests
     */
    public final void setUp() throws Exception {
        // Open transaction in order to perform JDO operations
        _jdo = getJDOManager();

        Connection conn = _jdo.getConnectionFactory().createConnection();
        String filename = getClass().getResource(DATA_SET_FILE_SETUP).getFile();
        IDatabaseTester dbtester = new DefaultDatabaseTester(new DatabaseConnection(conn));
        dbtester.setDataSet(new XmlDataSet(new FileInputStream(filename)));
        dbtester.onSetup();
    }

    public final void testLoadBook() throws Exception {
        Database db = _jdo.getDatabase();
        db.begin();

        Book lookup = db.load(Book.class, new Long(1234));
        assertNotNull(lookup);

        assertEquals(1234, lookup.getIsbn());
        assertEquals("Heart of Darkness", lookup.getTitle());

        db.commit();
        db.close();
    }

    public final void testCreateBook() throws Exception {
        Book myBook = new Book();
        myBook.setIsbn(1235);
        myBook.setTitle("My Definition");

        Database db = _jdo.getDatabase();
        db.begin();
        db.create(myBook);
        db.commit();

        db.begin();
        Book lookup = db.load(Book.class, new Long(1235));
        assertNotNull(lookup);
        assertEquals(1235, lookup.getIsbn());
        assertEquals("My Definition", lookup.getTitle());
        db.commit();
        db.close();
        
        assertDataset(DATA_SET_FILE_INSERTED);
    }

    public final void testRemoveBook() throws Exception {
        Database db = _jdo.getDatabase();
        db.begin();

        Book lookup = db.load(Book.class, new Long(1234));
        assertNotNull(lookup);
        assertEquals(1234, lookup.getIsbn());
        assertEquals("Heart of Darkness", lookup.getTitle());
        db.remove(lookup);
        
        Book lookup2 = null;
        try {
            lookup2 = db.load(Book.class, new Long(1234));
            fail("object not found excetion expected!");
        } catch (ObjectNotFoundException e) {
            assertNull(lookup2);
        } catch (Exception e) {
            fail();
        }
        db.commit();
        
        assertDataset(DATA_SET_FILE_EMTY);
        
        db.begin();
        try {
            lookup2 = db.load(Book.class, new Long(1234));
            fail("object not found excetion expected!");
        } catch (ObjectNotFoundException e) {
            assertNull(lookup2);
        } catch (Exception e) {
            fail();
        }
        db.commit();

        db.close();
    }

    public final void testSelectQuery() throws Exception {
        Database db = _jdo.getDatabase();
        db.begin();

        String oql = "SELECT b FROM " + Book.class.getName() + " b WHERE b.title = $1";
        OQLQuery query = db.getOQLQuery(oql);
        query.bind("Heart of Darkness");
        QueryResults result = query.execute();
        
        int count = 0;
        Book b = null;
        while (result.hasMore()) {
            b = (Book) result.next();
            count++;
        }
        
        assertEquals(1, count);
        assertEquals(1234, b.getIsbn());
        assertEquals("Heart of Darkness", b.getTitle());
        
        db.commit();
        db.close();
    }

    public final void testUpdateBook() throws Exception {
        Database db = _jdo.getDatabase();

        db.begin();
        Book lookup = db.load(Book.class, new Long(1234));
        assertNotNull(lookup);
        assertEquals(1234, lookup.getIsbn());
        assertEquals("Heart of Darkness", lookup.getTitle());
        lookup.setTitle("Heart of Light");
        db.commit();
        
        assertDataset(DATA_SET_FILE_UPDATED);
        
        db.begin();
        Book lookup2 = db.load(Book.class, new Long(1234));
        assertEquals("Heart of Light", lookup2.getTitle());
        db.commit();
        
        db.close();
    }

    private void assertDataset(final String datasetlocation) throws Exception {
        String[] tables = new String[] {"test2996_single_book"};

        Connection conn = _jdo.getConnectionFactory().createConnection();
        String filename = getClass().getResource(datasetlocation).getFile();
        IDataSet actualDataSet = new DefaultDatabaseTester(
                new DatabaseConnection(conn)).getConnection().createDataSet(tables);
        IDataSet expectedDataSet = new XmlDataSet(new FileInputStream(filename));
        Assertion.assertEquals(expectedDataSet, actualDataSet);

        for (int i = 0; i < tables.length; i++) {
            ITable actualTable = actualDataSet.getTable(tables[i]);
            ITable expectedTable = expectedDataSet.getTable(tables[i]);
            Assertion.assertEquals(expectedTable, actualTable);
        }
    }
}
