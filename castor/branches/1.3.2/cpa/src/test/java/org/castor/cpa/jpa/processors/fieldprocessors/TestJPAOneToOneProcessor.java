package org.castor.cpa.jpa.processors.fieldprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.info.FieldInfo;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.exolab.castor.mapping.MappingException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestJPAOneToOneProcessor {

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

        assertTrue(jpaFieldNature.isOneToOne());
        assertEquals(JPADefault.class, jpaFieldNature
                .getRelationTargetEntity());
        assertNull(jpaFieldNature.getRelationMappedBy());
        assertTrue(jpaFieldNature.isRelationLazyFetch());
        assertNull(jpaFieldNature.getRelationCollectionType());
        assertEquals(CascadeType.ALL, jpaFieldNature.getCascadeTypes()[0]);

        /* check field "blob" */
        fieldInfo = classInfo.getFieldInfoByName("blob");
        assertNotNull(fieldInfo);

        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));
        jpaFieldNature = new JPAFieldNature(fieldInfo);

        assertTrue(jpaFieldNature.isOneToOne());
        assertEquals(JPANull.class, jpaFieldNature
                .getRelationTargetEntity());
        assertNull(jpaFieldNature.getRelationMappedBy());
        assertFalse(jpaFieldNature.isRelationLazyFetch());
        assertNull(jpaFieldNature.getRelationCollectionType());
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

        assertTrue(jpaFieldNature.isOneToOne());
        assertEquals(String.class, jpaFieldNature.getRelationTargetEntity());
        assertNull(jpaFieldNature.getRelationMappedBy());
        assertFalse(jpaFieldNature.isRelationLazyFetch());
        assertNull(jpaFieldNature.getRelationCollectionType());
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

        assertFalse(jpaFieldNature.isOneToOne());
        assertNull(jpaFieldNature.getRelationTargetEntity());
        assertNull(jpaFieldNature.getRelationMappedBy());
        assertFalse(jpaFieldNature.isRelationLazyFetch());
        assertNull(jpaFieldNature.getRelationCollectionType());
    }

    @Entity
    @Ignore
    private class JPAFull {
        public String _bla;

        public String _blob;

        @OneToOne(
                cascade = CascadeType.ALL, 
                fetch = FetchType.LAZY, 
                optional = false, 
                targetEntity = JPADefault.class, 
                mappedBy = "name"
        )
        public String getBla() {
            return _bla;
        }

        public void setBla(final String bla) {
            _bla = bla;
        }

        @OneToOne(fetch = FetchType.EAGER, targetEntity = JPANull.class)
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

        @OneToOne
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
