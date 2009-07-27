/*
 * Copyright 2008 Tobias Hochwallner
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
package org.castor.cpa.functional.single;

import java.io.FileInputStream;

import org.castor.cpa.functional.BaseSpringTestCase;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Base Class for Single Entity tests.
 * 
 * @author Tobias Hochwallner
 */
public abstract class BaseSingleTest extends BaseSpringTestCase {

    /**
     * Spring config file.
     */
    private static final String SPRING_CONFIG = "spring-config.xml";

    /**
     * JDOManager instance, for connecting to database.
     */
    protected JDOManager _jdo = null;

    /**
     * The DBUnit {@link IDatabaseTester} to use.
     */
    private IDatabaseTester _dbtester = null;

    /**
     * Name of the initial dataset file.
     */
    private static final String DATA_SET_FILE = "Single.xml";
    private static final String DATA_SET_FILE_INSERTED_KEYGEN = "SingleInsertedKeygen.xml";
    private static final String DATA_SET_FILE_INSERTED = "SingleInserted.xml";
    private static final String DATA_SET_FILE_UPDATED = "SingleUpdated.xml";
    private static final String DATA_SET_FILE_EMTY = "SingleEmpty.xml";

    protected void setUp() throws Exception {
        super.setUp();
        _jdo = (JDOManager) _context.getBean(getJDOManagerBeanName());
        _dbtester = new DefaultDatabaseTester(new DatabaseConnection(_jdo
                .getConnectionFactory().createConnection()));
        _dbtester.setDataSet(new XmlDataSet(new FileInputStream(getClass()
                .getResource(DATA_SET_FILE).getFile())));
        _dbtester.onSetup();
    }

    protected abstract String getJDOManagerBeanName();

    /**
     * Returns an {@link ClassPathXmlApplicationContext} for single entity
     * tests.
     * 
     * @return A {@link ClassPathXmlApplicationContext}.
     * @see BaseSingleTest#getApplicationContext()
     */
    protected ApplicationContext getApplicationContext() {
        return new ClassPathXmlApplicationContext(getClass().getResource(
                SPRING_CONFIG).toExternalForm());
    }

    /**
     * Tests if loading a book element from database works.
     * 
     * @throws Exception
     *             if something goes wrong
     */
    public void testLoadBook() throws Exception {
        // Assertion.assertEquals(new XmlDataSet(new
        // FileInputStream(getClass().getResource(DATA_SET_FILE).getFile())),getConnection().createDataSet());
        Database db = _jdo.getDatabase();
        assertNotNull(db);
        db.begin();

        Book lookup = (Book) db.load(Book.class, new Long(1234));
        assertNotNull(lookup);

        assertEquals(1234, lookup.getIsbn());
        assertEquals("Heart of Darkness", lookup.getTitle());

        db.commit();
        db.close();
    }

    public void testCreateBook() throws Exception {
        Database db = _jdo.getDatabase();
        assertNotNull(db);

        Book myBook = new Book();
        myBook.setIsbn(1235);
        myBook.setTitle("My Definition");

        db.begin();
        db.create(myBook);
        db.commit();

        db.begin();
        Book lookup = (Book) db.load(Book.class, new Long(1235));
        assertNotNull(lookup);
        assertEquals(1235, lookup.getIsbn());
        assertEquals("My Definition", lookup.getTitle());
        db.commit();
        db.close();
        assertDataset(DATA_SET_FILE_INSERTED);
    }

    /**
     * Tests remove.
     * 
     * @throws Exception
     *             if db setup fails.
     */
    public void testRemoveBook() throws Exception {
        Database db = _jdo.getDatabase();
        assertNotNull(db);
        db.begin();
        Book lookup = (Book) db.load(Book.class, new Long(1234));
        assertNotNull(lookup);
        assertEquals(1234, lookup.getIsbn());
        assertEquals("Heart of Darkness", lookup.getTitle());
        db.remove(lookup);
        Book lookup2 = null;
        try {
            lookup2 = (Book) db.load(Book.class, new Long(1234));
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
            lookup2 = (Book) db.load(Book.class, new Long(1234));
            fail("object not found excetion expected!");
        } catch (ObjectNotFoundException e) {
            assertNull(lookup2);
        } catch (Exception e) {
            fail();
        }
        db.commit();
        db.close();

    }

    /**
     * Tests a simple OQL query.
     * 
     * @throws Exception
     *             if db setup fails.
     */
    public void testSelectQuery() throws Exception {
        Database db = _jdo.getDatabase();
        assertNotNull(db);
        db.begin();
        OQLQuery query = db.getOQLQuery("SELECT b FROM " + Book.class.getName()
                + " b WHERE b.title = $1");
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

//    /**
//     * Tests update on a book.
//     * 
//     * @throws Exception
//     *             if db setup fails.
//     */
//    public void testUpdateLongTransactionBook() throws Exception {
//        Database db = _jdo.getDatabase();
//        assertNotNull(db);
//
//        db.begin();
//        Book lookup = (Book) db.load(Book.class, new Long(1234));
//        assertNotNull(lookup);
//        assertEquals(1234, lookup.getIsbn());
//        assertEquals("Heart of Darkness", lookup.getTitle());
//        db.commit();
//        assertDataset(DATA_SET_FILE);
//        db.begin();
//        lookup.setTitle("Heart of Light");
//        db.update(lookup);
//        db.commit();
//        db.begin();
//        assertDataset(DATA_SET_FILE_UPDATED);
//        lookup = null;
//        lookup = (Book) db.load(Book.class, new Long(1234));
//        assertEquals("Heart of Light", lookup.getTitle());
//        db.commit();
//        db.close();
//    }
    
    /**
     * Tests update on a book.
     * 
     * @throws Exception
     *             if db setup fails.
     */
    public void testUpdateBook() throws Exception {
        Database db = _jdo.getDatabase();
        assertNotNull(db);

        db.begin();
        Book lookup = (Book) db.load(Book.class, new Long(1234));
        assertNotNull(lookup);
        assertEquals(1234, lookup.getIsbn());
        assertEquals("Heart of Darkness", lookup.getTitle());
        lookup.setTitle("Heart of Light");
        db.commit();
        db.begin();
        assertDataset(DATA_SET_FILE_UPDATED);
        lookup = null;
        lookup = (Book) db.load(Book.class, new Long(1234));
        assertEquals("Heart of Light", lookup.getTitle());
        db.commit();
        db.close();
    }

    // /**
    // * Tests get identity.
    // * @throws Exception if db setup fails.
    // */
    // public void testGetIdentity() throws Exception {
    // Database db = _jdo.getDatabase();
    // assertNotNull(db);
    //
    // Book myBook = new Book();
    // myBook.setTitle("mybook");
    // db.begin();
    // assertEquals(0,myBook.getIsbn());
    // db.create(myBook);
    // assertEquals(1235,myBook.getIsbn());
    // db.commit();
    //
    // db.begin();
    //        
    // OQLQuery query = db.getOQLQuery("SELECT b FROM " + Book.class.getName()
    // +" b WHERE b.title = $1");
    // query.bind("mybook");
    // QueryResults result = query.execute();
    // assertEquals(1, result.size());
    // Book lookup = (Book) result.next();
    // assertNotNull(lookup);
    // assertEquals(1235,lookup.getIsbn());
    // assertEquals("mybook", lookup.getTitle());
    // assertEquals(1235, lookup.getIsbn());
    // db.commit();
    // db.close();
    //        
    // assertDataset(DATA_SET_FILE_INSERTED_KEYGEN);
    // }

    private void assertDataset(String datasetlocation) throws Exception {
        String tablename1 = "book";
        IDataSet actualDataSet = new DefaultDatabaseTester(
                new DatabaseConnection(_jdo.getConnectionFactory()
                        .createConnection())).getConnection().createDataSet(
                new String[] { tablename1 });
        IDataSet expectedDataSet = new XmlDataSet(new FileInputStream(
                getClass().getResource(datasetlocation).getFile()));
        Assertion.assertEquals(expectedDataSet, actualDataSet);

        ITable actualTable = actualDataSet.getTable(tablename1);
        ITable expectedTable = expectedDataSet.getTable(tablename1);
        Assertion.assertEquals(expectedTable, actualTable);

    }
}
