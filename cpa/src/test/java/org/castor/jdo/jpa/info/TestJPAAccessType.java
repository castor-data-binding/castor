/*
 * Copyright 2008 Werner Guttmann
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
package org.castor.jdo.jpa.info;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.castor.jdo.jpa.natures.JPAFieldNature;
import org.exolab.castor.mapping.MappingException;
import org.junit.Ignore;
import org.junit.Test;

public class TestJPAAccessType {

    @Test
    public void testFieldAccessed() {

        // is not supported by castor
        try {
            ClassInfoBuilder.buildClassInfo(FieldAccessed.class);
            fail();
        } catch (MappingException e) {
            // exception expected
        }
    }

    @Test
    public void testPropertyAccessed() throws MappingException {

        ClassInfo classInfo = ClassInfoBuilder.buildClassInfo(PropertyAccessed.class);
        
        assertNotNull(classInfo);
        assertTrue(classInfo.getDescribedClass().getName().endsWith("PropertyAccessed"));

        assertEquals(2, classInfo.getFieldCount());
        assertEquals(1, classInfo.getKeyFieldCount());

        FieldInfo fieldInfo;

        /* check field "primaryKey" */
        fieldInfo = classInfo.getFieldInfoByName("primaryKey");
        assertNull(fieldInfo);
        fieldInfo = classInfo.getKeyFieldInfoByName("primaryKey");
        assertNotNull(fieldInfo);
        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));
        assertEquals("primaryKey", fieldInfo.getFieldName());
        assertEquals(int.class, fieldInfo.getFieldType());

        /* check field "bla" */
        fieldInfo = classInfo.getFieldInfoByName("bla");
        assertNotNull(fieldInfo);
        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));
        assertEquals("bla", fieldInfo.getFieldName());
        assertEquals(String.class, fieldInfo.getFieldType());

        /* check field "blob" */
        fieldInfo = classInfo.getFieldInfoByName("blob");
        assertNotNull(fieldInfo);
        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));
        assertEquals("blob", fieldInfo.getFieldName());
        assertEquals(String.class, fieldInfo.getFieldType());
    }

    @Test
    public void testMixedAccessed() {
        // is not supported by castor

        try {
            ClassInfoBuilder.buildClassInfo(MixedAccessed.class);
            fail();
        } catch (MappingException e) {
            // exception expected
        }
    }

    @Test
    public void testWrongAccessed() {

        try {
            ClassInfoBuilder.buildClassInfo(WrongAccessed.class);
            fail();
        } catch (MappingException e) {
            // exception expected
        }
    }
    
    @Ignore
    @Entity
    private class FieldAccessed {
        @Id
        @Column(name = "primary_key", 
                unique = true, 
                nullable = false, 
                insertable = true, 
                updatable = true, 
                columnDefinition = "TESTDefinitionPrimaryKey", 
                table = "JPAtableTEST", 
                length = 10, 
                precision = 100, 
                scale = 1000)
        public int _primaryKey;

        @Column(name = "JPAcolumnTESTbla", 
                unique = true, 
                nullable = false, 
                insertable = true, 
                updatable = true, 
                columnDefinition = "TESTDefinitionBla", 
                table = "JPAtableTEST", 
                length = 10, 
                precision = 100, 
                scale = 1000)
        public String _bla;

        @Column(name = "JPAcolumnTESTblob", 
                unique = false, 
                nullable = true, 
                insertable = false, 
                updatable = false, 
                columnDefinition = "TESTDefinitionBlob", 
                table = "JPAtableTEST", 
                length = 2000, 
                precision = 200, 
                scale = 20)
        public String _blob;
    }

    @Ignore
    @Entity
    private class PropertyAccessed {
        private int _primaryKey;
        private String _bla;
        private String _blob;

        @Id
        @Column(name = "primary_key", 
                unique = true, 
                nullable = false, 
                insertable = true, 
                updatable = true, 
                columnDefinition = "TESTDefinitionPrimaryKey", 
                table = "JPAtableTEST", 
                length = 10, 
                precision = 100, 
                scale = 1000)
        public int getPrimaryKey() {
            return _primaryKey;
        }

        @Column(name = "JPAcolumnTESTbla", 
                unique = true, 
                nullable = false, 
                insertable = true, 
                updatable = true, 
                columnDefinition = "TESTDefinitionBla", 
                table = "JPAtableTEST", 
                length = 10, 
                precision = 100, 
                scale = 1000)
        public String getBla() {
            return _bla;
        }

        @Column(name = "JPAcolumnTESTblob", 
                unique = false, 
                nullable = true, 
                insertable = false, 
                updatable = false, 
                columnDefinition = "TESTDefinitionBlob", 
                table = "JPAtableTEST", 
                length = 2000, 
                precision = 200, 
                scale = 20)
        public String getBlob() {
            return _blob;
        }

        public void setPrimaryKey(final int primaryKey) {
            this._primaryKey = primaryKey;
        }

        public void setBla(final String bla) {
            this._bla = bla;
        }

        public void setBlob(final String blob) {
            this._blob = blob;
        }
    }

    @Ignore
    @Entity
    private class MixedAccessed {
        @Id
        @Column(name = "primary_key", 
                unique = true, 
                nullable = false, 
                insertable = true, 
                updatable = true, 
                columnDefinition = "TESTDefinitionPrimaryKey", 
                table = "JPAtableTEST", 
                length = 10, 
                precision = 100, 
                scale = 1000)
        public int _primaryKey;

        private String _bla;

        private String _blob;

        public int getPrimaryKey() {
            return _primaryKey;
        }

        @Column(name = "JPAcolumnTESTbla", 
                unique = true, 
                nullable = false, 
                insertable = true, 
                updatable = true, 
                columnDefinition = "TESTDefinitionBla", 
                table = "JPAtableTEST", 
                length = 10, 
                precision = 100, 
                scale = 1000)
        public String getBla() {
            return _bla;
        }

        @Column(name = "JPAcolumnTESTblob", 
                unique = false, 
                nullable = true, 
                insertable = false, 
                updatable = false, 
                columnDefinition = "TESTDefinitionBlob", 
                table = "JPAtableTEST", 
                length = 2000, 
                precision = 200, 
                scale = 20)
        public String getBlob() {
            return _blob;
        }

        public void setBla(final String bla) {
            this._bla = bla;
        }

        public void setBlob(final String blob) {
            this._blob = blob;
        }
    }

    @Ignore
    @Entity
    private class WrongAccessed {
        
        @Id
        private int _primaryKey;
        private String _bla;
        private String _blob;

        @Column(name = "primary_key", 
                unique = true, 
                nullable = false, 
                insertable = true, 
                updatable = true, 
                columnDefinition = "TESTDefinitionPrimaryKey", 
                table = "JPAtableTEST", 
                length = 10, 
                precision = 100, 
                scale = 1000)
        public int getPrimaryKey() {
            return _primaryKey;
        }

        @Column(name = "JPAcolumnTESTbla", 
                unique = true, 
                nullable = false, 
                insertable = true, 
                updatable = true, 
                columnDefinition = "TESTDefinitionBla", 
                table = "JPAtableTEST", 
                length = 10, 
                precision = 100, 
                scale = 1000)
        public String getBla() {
            return _bla;
        }

        @Column(name = "JPAcolumnTESTblob", 
                unique = false, 
                nullable = true, 
                insertable = false, 
                updatable = false, 
                columnDefinition = "TESTDefinitionBlob", 
                table = "JPAtableTEST", 
                length = 2000, 
                precision = 200, 
                scale = 20)
        public String getBlob() {
            return _blob;
        }
    }
}
