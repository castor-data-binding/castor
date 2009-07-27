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

import javax.persistence.Column;
import javax.persistence.Id;

import junit.framework.TestCase;

import org.castor.jdo.jpa.natures.JPAFieldNature;
import org.exolab.castor.mapping.MappingException;

public class TestJPAAccessType extends TestCase {

    private ClassInfoBuilder _classBuilder;

    public void setUp() {
        _classBuilder = new ClassInfoBuilder();
    }

    public void testFieldAccessed() throws MappingException {

        ClassInfo classInfo = buildClassInfo(FieldAccessed.class);

        assertNotNull(classInfo);
        assertTrue(classInfo.getDescribedClass().getName().endsWith("FieldAccessed"));
        
        assertEquals(3, classInfo.getFieldCount());
        assertEquals(0, classInfo.getKeyFieldCount());

        FieldInfo fieldInfo;

        /* check field "primaryKey" */
        fieldInfo = classInfo.getKeyFieldInfoByName("primaryKey");
        assertNull(fieldInfo);
        fieldInfo = classInfo.getFieldInfoByName("primaryKey");
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

    public void testPropertyAccessed() throws MappingException {

        ClassInfo classInfo = buildClassInfo(PropertyAccessed.class);
        
        assertNotNull(classInfo);
        assertTrue(classInfo.getDescribedClass().getName().endsWith("PropertyAccessed"));

        assertEquals(3, classInfo.getFieldCount());
        assertEquals(0, classInfo.getKeyFieldCount());

        FieldInfo fieldInfo;

        /* check field "primaryKey" */
        fieldInfo = classInfo.getKeyFieldInfoByName("primaryKey");
        assertNull(fieldInfo);
        fieldInfo = classInfo.getFieldInfoByName("primaryKey");
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

    public void testMixedAccessed() throws MappingException {

        ClassInfo classInfo = buildClassInfo(MixedAccessed.class);

        assertNotNull(classInfo);
        assertTrue(classInfo.getDescribedClass().getName().endsWith("MixedAccessed"));
        
        assertEquals(3, classInfo.getFieldCount());
        assertEquals(0, classInfo.getKeyFieldCount());

        FieldInfo fieldInfo;

        /* check field "primaryKey" */
        fieldInfo = classInfo.getKeyFieldInfoByName("primaryKey");
        assertNull(fieldInfo);
        fieldInfo = classInfo.getFieldInfoByName("primaryKey");
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

    public void testWrongAccessed() {

        try {
            buildClassInfo(WrongAccessed.class);
            fail();
        } catch (MappingException e) {
            // exception expected
        }
    }
    
    private ClassInfo buildClassInfo(Class<?> aClass) throws MappingException {
        // build class
        ClassInfo classInfo = _classBuilder.buildClassInfo(aClass);
        return classInfo;
    }

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
        public int primaryKey;

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
        public String bla;

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
        public String blob;
    }

    private class PropertyAccessed {
        
        private int primaryKey;

        private String bla;

        private String blob;

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
            return primaryKey;
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
            return bla;
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
            return blob;
        }

        public void setPrimaryKey(int primaryKey) {
            this.primaryKey = primaryKey;
        }

        public void setBla(String bla) {
            this.bla = bla;
        }

        public void setBlob(String blob) {
            this.blob = blob;
        }
    }

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
        public int primaryKey;

        private String bla;

        private String blob;

        public int getPrimaryKey() {
            return primaryKey;
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
            return bla;
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
            return blob;
        }

        public void setBla(String bla) {
            this.bla = bla;
        }

        public void setBlob(String blob) {
            this.blob = blob;
        }
    }

    private class WrongAccessed {
        
        @Id
        private int primaryKey;

        private String bla;

        private String blob;

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
            return primaryKey;
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
            return bla;
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
            return blob;
        }
    }

}
