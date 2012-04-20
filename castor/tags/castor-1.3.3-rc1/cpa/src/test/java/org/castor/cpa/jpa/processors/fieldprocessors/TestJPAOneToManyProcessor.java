package org.castor.cpa.jpa.processors.fieldprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.info.FieldInfo;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.exolab.castor.mapping.MappingException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public final class TestJPAOneToManyProcessor {

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

        assertTrue(jpaFieldNature.isOneToMany());
        assertEquals(JPADefault.class, jpaFieldNature
                .getRelationTargetEntity());
        assertEquals("name", jpaFieldNature.getRelationMappedBy());
        assertTrue(jpaFieldNature.isRelationLazyFetch());
        assertEquals(Collection.class, jpaFieldNature
                .getRelationCollectionType());
        assertEquals(CascadeType.ALL, jpaFieldNature.getCascadeTypes()[0]);

        /* check field "blob" */
        fieldInfo = classInfo.getFieldInfoByName("blob");
        assertNotNull(fieldInfo);

        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));
        jpaFieldNature = new JPAFieldNature(fieldInfo);

        assertTrue(jpaFieldNature.isOneToMany());
        assertEquals(JPADefault.class, jpaFieldNature
                .getRelationTargetEntity());
        assertNull(jpaFieldNature.getRelationMappedBy());
        assertFalse(jpaFieldNature.isRelationLazyFetch());
        assertEquals(List.class, jpaFieldNature.getRelationCollectionType());
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

        assertTrue(jpaFieldNature.isOneToMany());
        assertEquals(JPANull.class, jpaFieldNature
                .getRelationTargetEntity());
        assertNull(jpaFieldNature.getRelationMappedBy());
        assertTrue(jpaFieldNature.isRelationLazyFetch());
        assertEquals(Set.class, jpaFieldNature.getRelationCollectionType());
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

        assertFalse(jpaFieldNature.isOneToMany());
        assertNull(jpaFieldNature.getRelationTargetEntity());
        assertNull(jpaFieldNature.getRelationMappedBy());
        assertFalse(jpaFieldNature.isRelationLazyFetch());
        assertNull(jpaFieldNature.getRelationCollectionType());
    }

    @Test
    public void testJPAWrongTarget() {
        /* build class */
        try {
            ClassInfoBuilder.buildClassInfo(JPAWrongTarget1.class);
            fail();
        } catch (Exception e) {

        }

        /* build class */
        try {
            ClassInfoBuilder.buildClassInfo(JPAWrongTarget2.class);
            fail();
        } catch (Exception e) {

        }
    }

    @Entity
    @Ignore
    private class JPAFull {
        private Collection<JPADefault> _bla;

        private List<JPANull> _blob;

        @SuppressWarnings("unchecked")
        @OneToMany(
                cascade = CascadeType.ALL, 
                fetch = FetchType.LAZY, 
                targetEntity = JPADefault.class, 
                mappedBy = "name"
        )
        public Collection getBla() {
            return _bla;
        }

        public void setBla(final Collection<JPADefault> bla) {
            _bla = bla;
        }

        @OneToMany(fetch = FetchType.EAGER, targetEntity = JPADefault.class)
        public List<JPANull> getBlob() {
            return _blob;
        }

        public void setBlob(final List<JPANull> blob) {
            _blob = blob;
        }

    }

    @Entity
    @Ignore
    private class JPADefault {
        private Set<JPANull> _name;

        @OneToMany
        public Set<JPANull> getName() {
            return _name;
        }

        public void setName(final Set<JPANull> name) {
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

    @Entity
    @Ignore
    private class JPAWrongTarget1 {
        private Vector<String> _name;

        @OneToMany
        public Vector<String> getName() {
            return _name;
        }

        public void setName(final Vector<String> name) {
            _name = name;
        }
    }

    @SuppressWarnings("unchecked")
    @Entity
    @Ignore
    private class JPAWrongTarget2 {
        private Collection _name;

        @OneToMany
        public Collection getName() {
            return _name;
        }

        public void setName(final Collection name) {
            _name = name;
        }
    }

}
