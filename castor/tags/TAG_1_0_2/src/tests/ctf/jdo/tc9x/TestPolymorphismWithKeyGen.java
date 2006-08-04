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
package ctf.jdo.tc9x;

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.ObjectNotFoundException;

/**
 * Tests that modification to read only objects are not persist in the 
 * database.
 */
public final class TestPolymorphismWithKeyGen extends CastorTestCase {
    
    private JDOCategory _category;

    /**
     * Constructor
     * @param category the test suite that this test case belongs
     */
    public TestPolymorphismWithKeyGen(final TestHarness category) {
        super(category, "TC95", "Polymorphism tests with key generator");
        _category = (JDOCategory) category;
    }

    /**
     * Tests that modification to read only objects are not persist in the 
     * database.
     */
    public void runTest() throws Exception {
        testCreateAndLoadLaptop();
    }

    public void testCreateAndLoadLaptop() throws Exception {
        Database database = null;
        
        database = _category.getDatabase();
        
        database.begin();
        ProductDetail detail = new ProductDetail();
        detail.setId(10);
        detail.setCategory("category 10");
        detail.setLocation("location 10");
        database.create(detail);
        database.commit();
        
        database.begin();
        LaptopKeyGen laptop = new LaptopKeyGen();
        laptop.setName("laptop 10");
        laptop.setCpu("centrino");
        laptop.setResolution("1600");
        laptop.setWeight(2750);
        laptop.setDetail((ProductDetail) 
                database.load(ProductDetail.class, new Integer (10)));
        database.create(laptop);
        database.commit();
        
        int laptopId = laptop.getId();
        
        database.begin();
        laptop = (LaptopKeyGen)
                database.load(LaptopKeyGen.class, new Integer(laptopId));
        database.commit();
        
        assertNotNull(laptop);
        assertEquals("ctf.jdo.tc9x.LaptopKeyGen", laptop.getClass().getName());
        assertEquals(laptopId, laptop.getId());
        assertEquals("laptop 10", laptop.getName());
        
        database.begin();
        database.remove(database.load(LaptopKeyGen.class, new Integer(laptopId)));
        database.commit();
        
        database.begin();
        try {
            laptop = (LaptopKeyGen)
                    database.load(LaptopKeyGen.class, new Integer(laptopId));
            fail("Laptop with id " + laptopId + " still exists.");
        } catch (ObjectNotFoundException e) {
            assertTrue("The object of type ctf.jdo.tc9x.Laptop with identity " + laptopId 
                     + " was not found in persistent storage", true);
        }
        
        database.commit();
        database.close();
    }
}
