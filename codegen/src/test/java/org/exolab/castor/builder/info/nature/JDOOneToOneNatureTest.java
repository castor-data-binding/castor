/*
 * Copyright 2008 Lukas Lang, Filip Hianik
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
 *
 */
package org.exolab.castor.builder.info.nature;

import java.util.List;

import junit.framework.TestCase;

import org.exolab.castor.builder.factory.FieldInfoFactory;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.castor.builder.info.nature.relation.JDOOneToOneNature;
import org.exolab.castor.builder.types.XSClass;
import org.exolab.javasource.JClass;

/**
 * Test various methods of {@link JDOOneToOneNature} using a {@link FieldInfo}.
 * 
 * @author Lukas Lang
 */
public final class JDOOneToOneNatureTest extends TestCase {

    /**
     * Test get foreign key method.
     */
    public void testGetForeignKeyNothingAdded() {
        FieldInfoFactory factory = new FieldInfoFactory();
        FieldInfo address = factory.createFieldInfo(new XSClass(new JClass(
                "Employee")), "address");
        address.addNature(JDOOneToOneNature.class.getName());
        JDOOneToOneNature relation = new JDOOneToOneNature(address);
        assertNull(relation.getForeignKeys());
    }
    
    /**
     * Test add and get foreign key methods.
     */
    public void testAddGetForeignKey() {
        FieldInfoFactory factory = new FieldInfoFactory();
        FieldInfo address = factory.createFieldInfo(new XSClass(new JClass(
                "Employee")), "address");
        address.addNature(JDOOneToOneNature.class.getName());
        JDOOneToOneNature relation = new JDOOneToOneNature(address);
        relation.addForeignKey("fk_address");
        assertEquals("fk_address", (String) relation.getForeignKeys().get(0));
    }

    /**
     * Test add and get foreign key methods with multiple foreign keys.
     */
    public void testAddGetForeignKeys() {
        FieldInfoFactory factory = new FieldInfoFactory();
        FieldInfo address = factory.createFieldInfo(new XSClass(new JClass(
                "Employee")), "token");
        address.addNature(JDOOneToOneNature.class.getName());
        JDOOneToOneNature relation = new JDOOneToOneNature(address);
        relation.addForeignKey("fk_sin");
        relation.addForeignKey("fk_dateofbirth");
        
        List keys = relation.getForeignKeys();
        
        if (keys.get(0).equals("fk_sin")) {
            // Check the second entry
            assertEquals("fk_dateofbirth", keys.get(1));
        } else if (keys.get(0).equals("fk_dateofbirth")) {
            // Check the second entry
            assertEquals("fk_sin", keys.get(1));
        } else {
            fail("No keys found!");
        }
    }
    
    /**
     * Tests get and set of read only.
     */
    public void testReadOnly() {
        FieldInfoFactory factory = new FieldInfoFactory();
        FieldInfo address = factory.createFieldInfo(
                new XSClass(new JClass("Employee")), "address");
        address.addNature(JDOOneToOneNature.class.getName());
        JDOOneToOneNature relation = new JDOOneToOneNature(address);
        relation.setReadOnly(true);
        assertEquals(true, relation.isReadOnly());
    }

    /**
     * Tests get and set of dirty.
     */
    public void testDirty() {
        FieldInfoFactory factory = new FieldInfoFactory();
        FieldInfo address = factory.createFieldInfo(
                new XSClass(new JClass("Employee")), "address");
        address.addNature(JDOOneToOneNature.class.getName());
        JDOOneToOneNature jdo = new JDOOneToOneNature(address);
        jdo.setDirty(true);
        assertEquals(true, jdo.isDirty());
    }

}
