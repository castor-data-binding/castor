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
 *
 */
package org.exolab.castor.builder.info.nature;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.exolab.castor.builder.info.ClassInfo;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.javasource.JClass;

/**
 * Tests access to {@link ClassInfo} properties via {@link JDOClassInfoNature}. Remember that
 * behavior of properties not set before is specified in the {@link BaseNature}
 * class and tested in {@link BaseNatureTest}. Property implementation will not
 * be tested in here.
 * 
 * @author Tobias Hochwallner
 * @since 1.2.1
 */
public final class JDOClassNatureTest extends TestCase {

    /**
     * Constructor.
     * 
     * @param name
     *            of the test case.
     */
    public JDOClassNatureTest(final String name) {
        super(name);
    }

    /**
     * Shows the usage of JDOClassNature.
     */
    public void testUsage() {
        ClassInfo classInfo = new ClassInfo(new JClass("test"));
        if (!classInfo.hasNature(JDOClassInfoNature.class.getName())) {
            classInfo.addNature(JDOClassInfoNature.class.getName());
            JDOClassInfoNature jdo = new JDOClassInfoNature(classInfo);
            jdo.setTableName("BOOK");
            jdo.addPrimaryKey("ISBN");
            // TODO Tobias jdo.addPrimaryKey("ISBN","Generator");
            assertEquals("BOOK", jdo.getTableName());
            assertEquals(1, jdo.getPrimaryKeys().size());
            assertTrue(jdo.getPrimaryKeys().contains("ISBN"));
        }
    }

    /**
     * Tests set and get JDO table name.
     */
    public void testTableName() {
        ClassInfo classInfo = new ClassInfo(new JClass("test"));
        classInfo.addNature(JDOClassInfoNature.class.getName());
        JDOClassInfoNature jdo = new JDOClassInfoNature(classInfo);
        jdo.setTableName("BOOK");
        assertEquals("BOOK", jdo.getTableName());
    }

    /**
     * Tests set and get primary keys. Adding the author column to the primary
     * key in reality would net really make sense.
     */
    public void testPrimaryKeys() {
        ClassInfo classInfo = new ClassInfo(new JClass("test"));
        classInfo.addNature(JDOClassInfoNature.class.getName());
        JDOClassInfoNature jdo = new JDOClassInfoNature(classInfo);

        List columns = new LinkedList();
        columns.add("ISBN");
        columns.add("AUTHOR");

        jdo.addPrimaryKey(columns.get(0).toString());
        assertEquals(1, jdo.getPrimaryKeys().size());

        assertTrue(jdo.getPrimaryKeys().contains(columns.get(0)));

        jdo.addPrimaryKey(columns.get(1).toString());

        List primaryKey = jdo.getPrimaryKeys();
        assertEquals(2, jdo.getPrimaryKeys().size());

        assertTrue(primaryKey.containsAll(columns));
    }

    /**
     * Tests set and get {@link AccessMode} of the JDO entity.
     */
    public void testAccessMode() {
        ClassInfo classInfo = new ClassInfo(new JClass("test"));
        classInfo.addNature(JDOClassInfoNature.class.getName());
        JDOClassInfoNature jdo = new JDOClassInfoNature(classInfo);
        jdo.setAccessMode(AccessMode.Shared);
        assertNotNull(jdo.getAccessMode());
        assertEquals(AccessMode.Shared, jdo.getAccessMode());
    }

}
