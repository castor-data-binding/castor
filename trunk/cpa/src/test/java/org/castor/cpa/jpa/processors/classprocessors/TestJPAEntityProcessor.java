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

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.exolab.castor.mapping.MappingException;
import org.junit.Ignore;
import org.junit.Test;

public final class TestJPAEntityProcessor {
    @Test
    public void testJPAFull() throws MappingException {

        ClassInfo classInfo = ClassInfoBuilder.buildClassInfo(JPAFull.class);

        assertNotNull(classInfo);
        assertTrue(classInfo.hasNature(JPAClassNature.class.getName()));
        JPAClassNature jpaClassNature = new JPAClassNature(classInfo);

        assertNotNull(jpaClassNature.getEntityName());
        assertEquals("JPAentityTEST", jpaClassNature.getEntityName());

    }

    @Test
    public void testJPADefault() throws MappingException {

        ClassInfo classInfo = ClassInfoBuilder.buildClassInfo(JPADefault.class);

        assertNotNull(classInfo);
        assertTrue(classInfo.hasNature(JPAClassNature.class.getName()));
        JPAClassNature jpaClassNature = new JPAClassNature(classInfo);

        assertNotNull(jpaClassNature.getEntityName());
        assertEquals("JPADefault", jpaClassNature.getEntityName());

    }

    @Test
    public void testJPANull() throws MappingException {

        ClassInfo classInfo = ClassInfoBuilder.buildClassInfo(JPANull.class);

        assertNull(classInfo);
    }

    @Entity(name = "JPAentityTEST")
    @Ignore
    private class JPAFull {
    }

    @Entity()
    @Ignore
    private class JPADefault {
    }

    @Ignore
    private class JPANull {
    }

}
