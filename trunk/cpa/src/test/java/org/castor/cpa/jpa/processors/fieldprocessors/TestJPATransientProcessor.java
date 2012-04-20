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
import static org.junit.Assert.assertTrue;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.info.FieldInfo;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.exolab.castor.mapping.MappingException;
import org.junit.Ignore;
import org.junit.Test;

public final class TestJPATransientProcessor {
    @Test
    public void testJPAFull() throws MappingException {

        ClassInfo classInfo = null;
        classInfo = ClassInfoBuilder.buildClassInfo(JPAFull.class);

        assertEquals(1, classInfo.getFieldCount());
        assertEquals(0, classInfo.getKeyFieldCount());

        FieldInfo fieldInfo;
        JPAFieldNature jpaFieldNature;

        fieldInfo = classInfo.getFieldInfoByName("name");
        assertNotNull(fieldInfo);

        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));

        jpaFieldNature = new JPAFieldNature(fieldInfo);

        assertTrue(jpaFieldNature.isTransient());
    }

    @Test
    public void testJPANull() throws MappingException {
        ClassInfo classInfo = null;
        classInfo = ClassInfoBuilder.buildClassInfo(JPANull.class);

        assertEquals(1, classInfo.getFieldCount());
        assertEquals(0, classInfo.getKeyFieldCount());

        FieldInfo fieldInfo;
        JPAFieldNature jpaFieldNature;

        fieldInfo = classInfo.getFieldInfoByName("name");
        assertNotNull(fieldInfo);

        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));

        jpaFieldNature = new JPAFieldNature(fieldInfo);

        assertFalse(jpaFieldNature.isTransient());
    }

    @Entity
    @Ignore
    private class JPAFull {
        private String _name;

        @Transient
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
        private String _name;

        public String getName() {
            return _name;
        }

        public void setName(final String name) {
            _name = name;
        }
    }
}
