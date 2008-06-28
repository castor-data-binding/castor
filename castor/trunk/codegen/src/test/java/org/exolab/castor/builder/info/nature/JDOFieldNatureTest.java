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
 *
 */
package org.exolab.castor.builder.info.nature;

import junit.framework.TestCase;

import org.exolab.castor.builder.factory.FieldInfoFactory;
import org.exolab.castor.builder.info.FieldInfo;
import org.exolab.castor.builder.types.XSClass;
import org.exolab.javasource.JClass;

/**
 * Tests access to {@link FieldInfo} properties via {@link JDOFieldNature}. Remember that
 * behavior of properties not set before is specified in the {@link BaseNature}
 * class and tested in {@link BaseNatureTest}. Property implementation will not
 * be tested in here.
 * 
 * @author Lukas Lang
 * @since 1.2.1
 * 
 */
public final class JDOFieldNatureTest extends TestCase {

    /**
     * Tests get and set of column name.
     */
    public void testColumnName() {
        FieldInfoFactory factory = new FieldInfoFactory();
        FieldInfo isbn = factory.createFieldInfo(
                new XSClass(new JClass("Book")), "isbn");
        isbn.addNature(JDOFieldNature.class.getName());
        JDOFieldNature jdo = new JDOFieldNature(isbn);

        jdo.setColumnName("ISBN");
        assertEquals("ISBN", jdo.getColumnName());

    }

    /**
     * Tests get and set of column type.
     */
    public void testColumnType() {
        FieldInfoFactory factory = new FieldInfoFactory();
        FieldInfo isbn = factory.createFieldInfo(
                new XSClass(new JClass("Book")), "isbn");
        isbn.addNature(JDOFieldNature.class.getName());
        JDOFieldNature jdo = new JDOFieldNature(isbn);

        jdo.setColumnType("varchar");
        assertEquals("varchar", jdo.getColumnType());
    }

    /**
     * Tests get and set of read only.
     */
    public void testReadOnly() {
        FieldInfoFactory factory = new FieldInfoFactory();
        FieldInfo isbn = factory.createFieldInfo(
                new XSClass(new JClass("Book")), "isbn");
        isbn.addNature(JDOFieldNature.class.getName());
        JDOFieldNature jdo = new JDOFieldNature(isbn);
        jdo.setReadOnly(true);
        assertEquals(true, jdo.isReadOnly());
    }

    /**
     * Tests get and set of dirty.
     */
    public void testDirty() {
        FieldInfoFactory factory = new FieldInfoFactory();
        FieldInfo isbn = factory.createFieldInfo(
                new XSClass(new JClass("Book")), "isbn");
        isbn.addNature(JDOFieldNature.class.getName());
        JDOFieldNature jdo = new JDOFieldNature(isbn);
        jdo.setDirty(true);
        assertEquals(true, jdo.isDirty());
    }

}
