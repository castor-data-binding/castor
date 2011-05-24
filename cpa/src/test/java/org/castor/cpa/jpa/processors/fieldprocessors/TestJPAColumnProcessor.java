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
package org.castor.cpa.jpa.processors.fieldprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.info.FieldInfo;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.exolab.castor.mapping.MappingException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestJPAColumnProcessor {

    @Before
    public void setUp() {
    }

    @Test
    public void testJPAFull() throws MappingException {
        /* build class */
        ClassInfo classInfo = ClassInfoBuilder.buildClassInfo(JPAFull.class);

        assertEquals(2, classInfo.getFieldCount());
        assertEquals(0, classInfo.getKeyFieldCount());

        FieldInfo fieldInfo;
        JPAFieldNature jpaFieldNature;

        /* check field "bla" */
        fieldInfo = classInfo.getFieldInfoByName("bla");
        assertNotNull(fieldInfo);

        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));
        jpaFieldNature = new JPAFieldNature(fieldInfo);

        assertEquals("JPAcolumnTESTbla", jpaFieldNature.getColumnName());
        assertEquals(Boolean.TRUE, jpaFieldNature.getColumnUnique());
        assertEquals(Boolean.FALSE, jpaFieldNature.getColumnNullable());
        assertEquals(Boolean.TRUE, jpaFieldNature.getColumnInsertable());
        assertEquals(Boolean.TRUE, jpaFieldNature.getColumnUpdatable());
        assertEquals("TESTDefinitionBla", jpaFieldNature.getColumnDefinition());
        assertEquals("JPAtableTEST", jpaFieldNature.getColumnTable());
        assertEquals(new Integer(10), jpaFieldNature.getColumnLength());
        assertEquals(new Integer(100), jpaFieldNature.getColumnPrecision());
        assertEquals(new Integer(1000), jpaFieldNature.getColumnScale());

        /* check field "blob" */
        fieldInfo = classInfo.getFieldInfoByName("blob");
        assertNotNull(fieldInfo);

        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));
        jpaFieldNature = new JPAFieldNature(fieldInfo);

        assertEquals("JPAcolumnTESTblob", jpaFieldNature.getColumnName());
        assertEquals(Boolean.FALSE, jpaFieldNature.getColumnUnique());
        assertEquals(Boolean.TRUE, jpaFieldNature.getColumnNullable());
        assertEquals(Boolean.FALSE, jpaFieldNature.getColumnInsertable());
        assertEquals(Boolean.FALSE, jpaFieldNature.getColumnUpdatable());
        assertEquals("TESTDefinitionBlob", jpaFieldNature.getColumnDefinition());
        assertEquals("JPAtableTEST", jpaFieldNature.getColumnTable());
        assertEquals(new Integer(2000), jpaFieldNature.getColumnLength());
        assertEquals(new Integer(200), jpaFieldNature.getColumnPrecision());
        assertEquals(new Integer(20), jpaFieldNature.getColumnScale());
    }

    @Test
    public void testJPADefault() throws MappingException {
        /* build class */
        ClassInfo classInfo = ClassInfoBuilder
                .buildClassInfo(JPADefault.class);

        assertEquals(1, classInfo.getFieldCount());
        assertEquals(0, classInfo.getKeyFieldCount());

        FieldInfo fieldInfo;
        JPAFieldNature jpaFieldNature;

        /* check field "name" */
        fieldInfo = classInfo.getFieldInfoByName("name");
        assertNotNull(fieldInfo);

        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));
        jpaFieldNature = new JPAFieldNature(fieldInfo);

        assertEquals("", jpaFieldNature.getColumnName());
        assertEquals(Boolean.FALSE, jpaFieldNature.getColumnUnique());
        assertEquals(Boolean.TRUE, jpaFieldNature.getColumnNullable());
        assertEquals(Boolean.TRUE, jpaFieldNature.getColumnInsertable());
        assertEquals(Boolean.TRUE, jpaFieldNature.getColumnUpdatable());
        assertEquals("", jpaFieldNature.getColumnDefinition());
        assertEquals("", jpaFieldNature.getColumnTable());
        assertEquals(new Integer(255), jpaFieldNature.getColumnLength());
        assertEquals(new Integer(0), jpaFieldNature.getColumnPrecision());
        assertEquals(new Integer(0), jpaFieldNature.getColumnScale());
    }

    @Test
    public void testJPANull() throws MappingException {
        /* build class */
        ClassInfo classInfo = ClassInfoBuilder
                .buildClassInfo(JPANull.class);

        assertEquals(1, classInfo.getFieldCount());
        assertEquals(0, classInfo.getKeyFieldCount());

        FieldInfo fieldInfo;
        JPAFieldNature jpaFieldNature;

        /* check field "name" */
        fieldInfo = classInfo.getFieldInfoByName("name");
        assertNotNull(fieldInfo);

        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));
        jpaFieldNature = new JPAFieldNature(fieldInfo);

        assertNull(jpaFieldNature.getColumnName());
        assertNull(jpaFieldNature.getColumnUnique());
        assertNull(jpaFieldNature.getColumnNullable());
        assertNull(jpaFieldNature.getColumnInsertable());
        assertNull(jpaFieldNature.getColumnUpdatable());
        assertNull(jpaFieldNature.getColumnDefinition());
        assertNull(jpaFieldNature.getColumnTable());
        assertNull(jpaFieldNature.getColumnLength());
        assertNull(jpaFieldNature.getColumnPrecision());
        assertNull(jpaFieldNature.getColumnScale());
    }

    @Entity
    @Ignore
    private class JPAFull {
        public String _bla;

        public String _blob;

        @Column(
                name = "JPAcolumnTESTbla", 
                unique = true, 
                nullable = false, 
                insertable = true, 
                updatable = true, 
                columnDefinition = "TESTDefinitionBla", 
                table = "JPAtableTEST", 
                length = 10, 
                precision = 100, 
                scale = 1000
        )
        public String getBla() {
            return _bla;
        }

        public void setBla(final String bla) {
            _bla = bla;
        }

        @Column(
                name = "JPAcolumnTESTblob", 
                unique = false, 
                nullable = true, 
                insertable = false, 
                updatable = false, 
                columnDefinition = "TESTDefinitionBlob", 
                table = "JPAtableTEST", 
                length = 2000, 
                precision = 200, 
                scale = 20
        )
        public String getBlob() {
            return _blob;
        }

        public void setBlob(final String blob) {
            _blob = blob;
        }

    }

    @Entity
    @Ignore
    private class JPADefault {
        public String _name;

        @Column
        public String getName() {
            return _name;
        }

        public void setName(final String name) {
            _name = name;
        }

    }

    @Entity
    @Ignore
    private class JPANull {
        public String _name;

        public String getName() {
            return _name;
        }

        public void setName(final String name) {
            _name = name;
        }
    }

}
