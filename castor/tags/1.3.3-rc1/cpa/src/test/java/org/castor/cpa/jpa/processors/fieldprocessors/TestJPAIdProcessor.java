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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.info.FieldInfo;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.exolab.castor.mapping.MappingException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public final class TestJPAIdProcessor {
    @Before
    public void setUp() {
    }

    @Test
    public void testJPADouble() {
        try {
            ClassInfoBuilder.buildClassInfo(JPADouble.class);
            fail();
        } catch (Exception e) {
            // exception expected!
        }

    }

    @Test
    public void testJPAFull() throws MappingException {
        /* build class */
        ClassInfo classInfo = ClassInfoBuilder.buildClassInfo(JPAFull.class);

        assertEquals(1, classInfo.getFieldCount());
        assertEquals(1, classInfo.getKeyFieldCount());

        FieldInfo fieldInfo;
        JPAFieldNature jpaFieldNature;

        /* check field "primaryKey" */
        fieldInfo = classInfo.getFieldInfoByName("primaryKey");
        assertNull(fieldInfo);
        fieldInfo = classInfo.getKeyFieldInfoByName("primaryKey");
        assertNotNull(fieldInfo);

        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));

        jpaFieldNature = new JPAFieldNature(fieldInfo);

        assertEquals(true, jpaFieldNature.isId());

        /* check field "blob" */
        fieldInfo = classInfo.getFieldInfoByName("blob");
        assertNotNull(fieldInfo);

        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));
        jpaFieldNature = new JPAFieldNature(fieldInfo);

        assertEquals(false, jpaFieldNature.isId());
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

        assertFalse(jpaFieldNature.isId());
    }

    @Entity
    @Ignore
    private class JPADouble {
        private String _primaryKey;

        private String _primaryKey2;

        private String _blob;

        @Id
        public String getPrimaryKey() {
            return _primaryKey;
        }

        public void setPrimaryKey(final String primaryKey) {
            _primaryKey = primaryKey;
        }

        @Id
        public String getPrimaryKey2() {
            return _primaryKey2;
        }

        public void setPrimaryKey2(final String primaryKey2) {
            _primaryKey2 = primaryKey2;
        }

        public String getBlob() {
            return _blob;
        }

        public void setBlob(final String blob) {
            _blob = blob;
        }

    }

    @Entity
    @Ignore
    private class JPAFull {
        private String _primaryKey;

        private String _blob;

        @Id
        public String getPrimaryKey() {
            return _primaryKey;
        }

        public void setPrimaryKey(final String primaryKey) {
            _primaryKey = primaryKey;
        }

        public String getBlob() {
            return _blob;
        }

        public void setBlob(final String blob) {
            _blob = blob;
        }

    }

    @Entity
    @Ignore
    private class JPANull {
        private String _name;

        public String getName() {
            return _name;
        }

        public void setName(final String name) {
            _name = name;
        }
    }
}
