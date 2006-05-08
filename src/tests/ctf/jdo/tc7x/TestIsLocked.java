/*
 * Copyright 2005 Werner Guttmann
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
package ctf.jdo.tc7x;

import harness.CastorTestCase;
import harness.TestHarness;

import java.sql.SQLException;

import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;


/**
 * Test for different collection types supported by Castor JDO.
 * This test creates data objects that each has a collection as
 * a field type.
 */
public class TestIsLocked extends CastorTestCase {

    private Database       _db;

    private JDOCategory    _category;

    public TestIsLocked ( TestHarness category ) {
        super( category, "TC79", "Test the use of Database.isLocked()" );
        _category = (JDOCategory) category;
    }

    public void setUp()
            throws PersistenceException, SQLException {
        _db = _category.getDatabase();
    }

    public void runTest() throws PersistenceException, SQLException, Exception
    {
        testIsLockedEntity();
        testIsNotLockedEntity();
    }
    
    public void testIsLockedEntity() throws Exception 
    {
        
        _db = _category.getDatabase();
        _db.begin();
        TestLimit item = new TestLimit();
        item.setId(111);
        item.setValue1("value1 111");
        item.setValue2("value2 111");
        _db.create(item);
        _db.commit();
        
        _db.begin();
        item = (TestLimit) _db.load(TestLimit.class, new Integer(111));
        
        assertNotNull(item);
        assertEquals(111, item.getId());
        
        assertTrue(_db.isLocked(TestLimit.class, new Integer (111)));
        _db.commit();

        _db = _category.getDatabase();
        _db.begin();
        item = (TestLimit) _db.load(TestLimit.class, new Integer(111));
        _db.remove(item);
        _db.commit();

    }

    public void testIsNotLockedEntity() throws Exception 
    {
        
        _db = _category.getDatabase();
        _db.begin();
        TestLimit item = new TestLimit();
        item.setId(111);
        item.setValue1("value1 111");
        item.setValue2("value2 111");
        _db.create(item);
        _db.commit();
        
        _db.begin();
        item = (TestLimit) _db.load(TestLimit.class, new Integer(111));
        
        assertNotNull(item);
        assertEquals(111, item.getId());
        
        assertFalse(_db.isLocked(TestLimit.class, new Integer (112)));
        _db.commit();

        _db = _category.getDatabase();
        _db.begin();
        item = (TestLimit) _db.load(TestLimit.class, new Integer(111));
        _db.remove(item);
        _db.commit();

    }

}
