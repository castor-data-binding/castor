package org.castor.cpa.jpa.processors.fieldprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.info.FieldInfo;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.exolab.castor.mapping.MappingException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestJPAJoinColumnProcessor {

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

        assertEquals("JPAjoincolumnTESTbla", jpaFieldNature.getJoinColumnName());
        assertEquals(Boolean.TRUE, jpaFieldNature.getJoinColumnUnique());
        assertEquals(Boolean.FALSE, jpaFieldNature.getJoinColumnNullable());
        assertEquals(Boolean.TRUE, jpaFieldNature.getJoinColumnInsertable());
        assertEquals(Boolean.TRUE, jpaFieldNature.getJoinColumnUpdatable());
        assertEquals("TESTDefinitionBla", jpaFieldNature
                .getJoinColumnColumnDefinition());
        assertEquals("JPAtableTEST", jpaFieldNature.getJoinColumnTable());
        assertEquals("reference_bla", jpaFieldNature
                .getJoinColumnReferencedColumnName());

        /* check field "blob" */
        fieldInfo = classInfo.getFieldInfoByName("blob");
        assertNotNull(fieldInfo);

        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));
        jpaFieldNature = new JPAFieldNature(fieldInfo);

        assertEquals("JPAjoincolumnTESTblob", jpaFieldNature
                .getJoinColumnName());
        assertEquals(Boolean.FALSE, jpaFieldNature.getJoinColumnUnique());
        assertEquals(Boolean.TRUE, jpaFieldNature.getJoinColumnNullable());
        assertEquals(Boolean.FALSE, jpaFieldNature.getJoinColumnInsertable());
        assertEquals(Boolean.FALSE, jpaFieldNature.getJoinColumnUpdatable());
        assertEquals("TESTDefinitionBlob", jpaFieldNature
                .getJoinColumnColumnDefinition());
        assertEquals("JPAtableTEST", jpaFieldNature.getJoinColumnTable());
        assertEquals("reference_blob", jpaFieldNature
                .getJoinColumnReferencedColumnName());
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

        assertEquals("", jpaFieldNature.getJoinColumnName());
        assertEquals(Boolean.FALSE, jpaFieldNature.getJoinColumnUnique());
        assertEquals(Boolean.TRUE, jpaFieldNature.getJoinColumnNullable());
        assertEquals(Boolean.TRUE, jpaFieldNature.getJoinColumnInsertable());
        assertEquals(Boolean.TRUE, jpaFieldNature.getJoinColumnUpdatable());
        assertEquals("", jpaFieldNature.getJoinColumnColumnDefinition());
        assertEquals("", jpaFieldNature.getJoinColumnTable());
        assertEquals("", jpaFieldNature.getJoinColumnReferencedColumnName());
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

        assertNull(jpaFieldNature.getJoinColumnName());
        assertNull(jpaFieldNature.getJoinColumnUnique());
        assertNull(jpaFieldNature.getJoinColumnNullable());
        assertNull(jpaFieldNature.getJoinColumnInsertable());
        assertNull(jpaFieldNature.getJoinColumnUpdatable());
        assertNull(jpaFieldNature.getJoinColumnColumnDefinition());
        assertNull(jpaFieldNature.getJoinColumnTable());
        assertNull(jpaFieldNature.getJoinColumnReferencedColumnName());
    }

    @Test
    public void testJPAWrongTarget() throws MappingException {
        /* build class */
        ClassInfo classInfo = ClassInfoBuilder
                .buildClassInfo(JPAWrongTarget.class);

        assertEquals(1, classInfo.getFieldCount());
        assertEquals(0, classInfo.getKeyFieldCount());

        FieldInfo fieldInfo;
        JPAFieldNature jpaFieldNature;

        /* check field "name" */
        fieldInfo = classInfo.getFieldInfoByName("name");
        assertNotNull(fieldInfo);

        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));
        jpaFieldNature = new JPAFieldNature(fieldInfo);

        assertNull(jpaFieldNature.getJoinColumnName());
        assertNull(jpaFieldNature.getJoinColumnUnique());
        assertNull(jpaFieldNature.getJoinColumnNullable());
        assertNull(jpaFieldNature.getJoinColumnInsertable());
        assertNull(jpaFieldNature.getJoinColumnUpdatable());
        assertNull(jpaFieldNature.getJoinColumnColumnDefinition());
        assertNull(jpaFieldNature.getJoinColumnTable());
        assertNull(jpaFieldNature.getJoinColumnReferencedColumnName());
    }

    @Entity
    @Ignore
    private class JPAFull {
        public String _bla;

        public String _blob;

        @OneToOne
        @JoinColumn(
                name = "JPAjoincolumnTESTbla", 
                unique = true, 
                nullable = false, 
                insertable = true, 
                updatable = true, 
                columnDefinition = "TESTDefinitionBla", 
                table = "JPAtableTEST", 
                referencedColumnName = "reference_bla"
        )
        public String getBla() {
            return _bla;
        }

        public void setBla(final String bla) {
            _bla = bla;
        }

        @OneToOne
        @JoinColumn(
                name = "JPAjoincolumnTESTblob", 
                unique = false, 
                nullable = true, 
                insertable = false, 
                updatable = false, 
                columnDefinition = "TESTDefinitionBlob", 
                table = "JPAtableTEST", 
                referencedColumnName = "reference_blob"
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

        @OneToOne
        @JoinColumn
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

    @Entity
    @Ignore
    private class JPAWrongTarget {
        public String _name;

        @JoinColumn
        public String getName() {
            return _name;
        }

        public void setName(final String name) {
            _name = name;
        }
    }

}
