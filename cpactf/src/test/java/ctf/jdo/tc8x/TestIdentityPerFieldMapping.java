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
package ctf.jdo.tc8x;

import harness.CastorTestCase;
import harness.TestHarness;
import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.persist.spi.Identity;

/**
 * JUnit test to test drive specification of class identity through the use of the
 * identity attribute on the <field> mapping.
 */
public final class TestIdentityPerFieldMapping extends CastorTestCase {
    private JDOCategory _category;

    /**
     * Creates an instance of this CTF test.
     * @param category The test suite of these tests
     */
    public TestIdentityPerFieldMapping(final TestHarness category) {
        super(category, "TC83",
                "Identity definition through identity attribute in field mapping");
        _category = (JDOCategory) category;
    }

    /**
     * @inheritDoc
     * @see junit.framework.TestCase#runTest()
     */
    public void runTest() throws Exception {
        testQueryEntityOne();
        testLoadChild();
        testLoadEntityWithCompoundId();
        testLoadChildWithCompoundId();
    }

    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testQueryEntityOne() throws Exception {
        Database db = _category.getDatabase();
        db.begin();
        
        Parent entity = (Parent) db.load(Parent.class, new Integer(1));

        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());
        
        db.commit();
        db.close();
    }
    
    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testLoadChild() throws Exception {
        Database db = _category.getDatabase();
        db.begin();
        
        Child child = (Child) db.load(Child.class, new Integer(1));

        assertNotNull(child);
        assertEquals(new Integer(1), child.getId());
        
        db.commit();
        db.close();
    }

    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testLoadEntityWithCompoundId() throws Exception {
        Database db = _category.getDatabase();
        db.begin();
        
        ParentWithCompoundId child = (ParentWithCompoundId) 
            db.load(ParentWithCompoundId.class,
                    new Identity(new Integer(1), new Integer(1)));

        assertNotNull(child);
        assertEquals(new Integer(1), child.getId1());
        assertEquals(new Integer(1), child.getId2());
        
        db.commit();
        db.close();
    }

    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testLoadChildWithCompoundId() throws Exception {
        Database db = _category.getDatabase();
        db.begin();
        
        ChildWithCompoundId child = (ChildWithCompoundId) 
            db.load(ChildWithCompoundId.class,
                    new Identity(new Integer(1), new Integer(1)));

        assertNotNull(child);
        assertEquals(new Integer(1), child.getId1());
        assertEquals(new Integer(1), child.getId2());
        
        db.commit();
        db.close();
    }

}
