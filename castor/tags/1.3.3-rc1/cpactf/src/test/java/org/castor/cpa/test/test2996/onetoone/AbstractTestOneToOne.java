/*
 * Copyright 2008 Lukas Lang, Ralf Joachim
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
package org.castor.cpa.test.test2996.onetoone;

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
import org.exolab.castor.mapping.MappingException;

/**
 * Abstract base class for testing XML and class mapping with classes
 * having a one to one relation. 
 */
public abstract class AbstractTestOneToOne extends CPATestCase {
    private static final String DATA_SET_FILE_SETUP = "dbunit-setup.xml";
    private static final String DATA_SET_FILE_INSERTED = "dbunit-inserted.xml";
    private static final String DATA_SET_FILE_REMOVED = "dbunit-removed.xml";
    private static final String DATA_SET_FILE_UPDATED = "dbunit-updated.xml";

    private JDOManager _jdo;

    public AbstractTestOneToOne(final String name) {
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

    public final void testLoadAddress() throws Exception {
        Database db = _jdo.getDatabase();

        db.begin();
        Address address = db.load(Address.class, new Long(2));
        assertNotNull(address);
        assertEquals(2, address.getId());
        db.commit();

        db.close();
    }

    public final void testLoadEmployeeAddress() throws Exception {
        Database db = _jdo.getDatabase();

        db.begin();
        Employee employee = db.load(Employee.class, new Long(1));
        assertNotNull(employee);
        assertEquals(1, employee.getId());
        assertNotNull(employee.getAddress());
        Address address = employee.getAddress();
        assertEquals(2, address.getId());
        db.commit();

        db.close();
    }
    
    public final void testCreateEmployee() throws Exception {
        Database db = _jdo.getDatabase();

        db.begin();
        Employee emp = new Employee();
        emp.setId(666);
        Address addr = new Address();
        addr.setId(999);
        emp.setAddress(addr);
        assertEquals(999, emp.getAddress().getId());
        db.create(addr);
        db.create(emp);
        assertNotNull(db.load(Employee.class, new Long(666)));
        db.commit();
        
        db.begin();
        assertNotNull(db.load(Employee.class, new Long(666)));
        db.commit();

        db.close();
        
        assertDataset(DATA_SET_FILE_INSERTED);
    }

    public final void testRemoveEmployee() throws Exception {
        Database db = _jdo.getDatabase();

        db.begin();
        Employee emp = db.load(Employee.class, new Long(1));
        db.remove(emp);
        db.commit();
        
        db.close();
        
        assertDataset(DATA_SET_FILE_REMOVED);
    }
    
    public final void testUpdateEmployee() throws Exception {
        Database db = _jdo.getDatabase();

        db.begin();
        Employee emp = db.load(Employee.class, new Long(1));
        Address addr = new Address();
        addr.setId(666);
        db.create(addr);
        emp.setAddress(addr);
        db.commit();
        
        db.close();
        
        assertDataset(DATA_SET_FILE_UPDATED);
    }
 
    private void assertDataset(final String datasetlocation) throws Exception {
        String tablename1 = "test2996_onetoone_employee";
        String tablename2 = "test2996_onetoone_address";
        String[] tables = new String[] {tablename1, tablename2};
        
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
