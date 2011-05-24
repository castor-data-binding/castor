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
package org.castor.cpa.jpa.processors.classprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.exolab.castor.mapping.MappingException;
import org.junit.Ignore;
import org.junit.Test;

public class TestJPATableProcessor {

    @Test
    public void testJPAFull() throws MappingException {

        ClassInfo classInfo = ClassInfoBuilder.buildClassInfo(JPAFull.class);

        assertTrue(classInfo.hasNature(JPAClassNature.class.getName()));
        JPAClassNature jpaClassNature = new JPAClassNature(classInfo);

        assertNotNull(jpaClassNature.getTableName());
        assertEquals("JPAtableTEST", jpaClassNature.getTableName());
        assertNotNull(jpaClassNature.getTableCatalog());
        assertEquals("TESTcatalog", jpaClassNature.getTableCatalog());
        assertNotNull(jpaClassNature.getTableSchema());
        assertEquals("TESTschema", jpaClassNature.getTableSchema());

    }

    @Test
    public void testJPADefault() throws MappingException {

        ClassInfo classInfo = ClassInfoBuilder.buildClassInfo(JPADefault.class);

        assertTrue(classInfo.hasNature(JPAClassNature.class.getName()));
        JPAClassNature jpaClassNature = new JPAClassNature(classInfo);

        assertNotNull(jpaClassNature.getTableName());
        assertEquals("", jpaClassNature.getTableName());
        assertNotNull(jpaClassNature.getTableCatalog());
        assertEquals("", jpaClassNature.getTableCatalog());
        assertNotNull(jpaClassNature.getTableSchema());
        assertEquals("", jpaClassNature.getTableSchema());

    }

    @Test
    public void testJPANull() throws MappingException {

        ClassInfo classInfo = ClassInfoBuilder.buildClassInfo(JPANull.class);

        assertTrue(classInfo.hasNature(JPAClassNature.class.getName()));
        JPAClassNature jpaClassNature = new JPAClassNature(classInfo);

        assertNull(jpaClassNature.getTableName());
        assertNull(jpaClassNature.getTableCatalog());
        assertNull(jpaClassNature.getTableSchema());

    }

    @Entity
    @Table(name = "JPAtableTEST", schema = "TESTschema", catalog = "TESTcatalog")
    @Ignore
    private class JPAFull {
    }

    @Entity
    @Table()
    @Ignore
    private class JPADefault {
    }

    @Entity
    @Ignore
    private class JPANull {
    }

}
