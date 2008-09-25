/*
 * Copyright 2008 Lukas Lang
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
package org.castor.cpa.functional.onetoone;

import java.io.FileInputStream;

import org.castor.cpa.functional.BaseSpringTestCase;
import org.castor.cpa.functional.single.BaseSingleTest;
import org.dbunit.Assertion;
import org.dbunit.DefaultDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.XmlDataSet;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * 
 * @author Lukas Lang
 * 
 */
public abstract class BaseOneToOneTest extends BaseSpringTestCase {

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
    private static final String DATA_SET_FILE = "OneToOne.xml";

    private static final String DATA_SET_FILE_INSERTED = "OneToOneInserted.xml";

    private static final String DATA_SET_FILE_EMP_REMOVED = "OneToOneEmpRemoved.xml";

    private static final String DATA_SET_FILE_EMP_UPDATED = "OneToOneEmpUpdated.xml";

    private static final String DATA_SET_FILE_ADDR_UPDATED = "OneToOneAddrUpdated.xml";

    protected void setUp() throws Exception {
        super.setUp();
        _jdo = (JDOManager) _context.getBean(getJDOManagerBeanName());
        _dbtester = new DefaultDatabaseTester(new DatabaseConnection(_jdo.getConnectionFactory().createConnection()));
        _dbtester.setDataSet(new XmlDataSet(new FileInputStream(
                getClass().getResource(DATA_SET_FILE).getFile())));
        _dbtester.onSetup();
    }

    protected abstract String getJDOManagerBeanName();

    /**
     * Returns an {@link ClassPathXmlApplicationContext} for onetoone tests.
     * 
     * @return A {@link ClassPathXmlApplicationContext}.
     * @see BaseSingleTest#getApplicationContext()
     */
    protected ApplicationContext getApplicationContext() {
        return new ClassPathXmlApplicationContext(getClass().getResource(
                SPRING_CONFIG).toExternalForm());
    }
    
    /**
     * Asserts that the database matches the given data-set
     * @param datasetlocation the location of the data-set to match
     * @throws Exception
     */
    private void assertDataset(String datasetlocation) throws Exception{
        String tablename1 = "employee";
        String tablename2 = "address";
        
        IDataSet actualDataSet = new DefaultDatabaseTester(
                new DatabaseConnection(_jdo.getConnectionFactory()
                        .createConnection())).getConnection().createDataSet(
                new String[] { tablename1, tablename2 });
        IDataSet expectedDataSet = new XmlDataSet(new FileInputStream(
                getClass().getResource(datasetlocation).getFile()));
        
        Assertion.assertEquals(expectedDataSet, actualDataSet);
    
        ITable actualTable = actualDataSet.getTable(tablename1);
        ITable expectedTable = expectedDataSet.getTable(tablename1);
        Assertion.assertEquals(expectedTable, actualTable);
    
        actualTable = actualDataSet.getTable(tablename2);
        expectedTable = expectedDataSet.getTable(tablename2);
        Assertion.assertEquals(expectedTable, actualTable);
    }

    /**
     * Tests if loading an address element from database works.
     * 
     * @throws Exception
     *             if something goes wrong
     */
    public void testLoadAddress() throws Exception {
        Database db = _jdo.getDatabase();
        assertNotNull(db);
        db.begin();

        Address address = (Address) db.load(Address.class, new Long(2));

        assertNotNull(address);
        assertEquals(2, address.getId());

        db.commit();
        db.close();
    }

    /**
     * Tests if loading an employee element from database including the
     * corresponding address works.
     * 
     * @throws Exception
     *             if something goes wrong
     */
    public void testLoadEmployeeAddress() throws Exception {
        Database db = _jdo.getDatabase();
        assertNotNull(db);
        db.begin();

        Employee employee = (Employee) db.load(Employee.class, new Long(1));
        assertNotNull(employee);

        assertEquals(1, employee.getId());
        assertNotNull(employee.getAddress());
        
        Address address = employee.getAddress();
        assertEquals(2, address.getId());

        db.commit();
        db.close();
    }
    
    /**
     * Test create employee.
     * @throws Exception if db setup fails.
     */
    public void testCreateEmployee() throws Exception {
        Database db = _jdo.getDatabase();
        assertNotNull(db);
        db.begin();
        Employee emp = new Employee();
        emp.setId(666);
        Address addr = new Address();
        addr.setId(999);
        emp.setAddress(addr);
        assertEquals(999,emp.getAddress().getId());
        //TODO does the address have to exist in the db for creating an emp?
        db.create(addr);
        db.create(emp);
        assertNotNull(db.load(Employee.class, new Long(666)));
        
        db.commit();
//        db.close();
        
        db.begin();
        assertNotNull(db.load(Employee.class, new Long(666)));
        db.commit();
        db.close();
        
        assertDataset(DATA_SET_FILE_INSERTED);
    }

    /**
     * Tests remove.
     * @throws Exception if db setup fails.
     */
    public void testRemoveEmployee() throws Exception {
        Database db = _jdo.getDatabase();
        assertNotNull(db);
        db.begin();
        Employee emp = (Employee) db.load(Employee.class, new Long(1));
        db.remove(emp);
        db.commit();
        db.close();
        assertDataset(DATA_SET_FILE_EMP_REMOVED);
    }
    
//    /**
//     * Tests a simple OQL query.
//     * @throws Exception if db setup fails.
//     */
//    public void testSelectQuery() throws Exception {
//        Database db = _jdo.getDatabase();
//        assertNotNull(db);
//        db.begin();
//        OQLQuery query = db.getOQLQuery("SELECT e FROM " + Employee.class.getName() + " e WHERE e.address.id = $1");
//        query.bind("2");
//        QueryResults result = query.execute();
//        assertEquals(1, result.size());
//        Employee h = (Employee) result.next();
//        assertEquals(1, h.getId());
//        assertEquals(2, h.getAddress().getId());
//        db.commit();
//        db.close();
//    }
    
    /**
     * Tests update on a employee.
     * @throws Exception if db setup fails.
     */
    public void testUpdateEmployee() throws Exception {
        Database db = _jdo.getDatabase();
        assertNotNull(db);
        db.begin();
        Employee emp = (Employee) db.load(Employee.class, new Long(1));
        Address addr = new Address();
        addr.setId(666);
        db.create(addr);
        emp.setAddress(addr);
        db.commit();
        db.close();
        assertDataset(DATA_SET_FILE_EMP_UPDATED);
    }
 
}
