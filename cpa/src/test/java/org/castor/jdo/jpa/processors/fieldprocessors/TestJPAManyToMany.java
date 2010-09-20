package org.castor.jdo.jpa.processors.fieldprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Arrays;

import javax.persistence.*;

import org.castor.jdo.jpa.info.ClassInfo;
import org.castor.jdo.jpa.info.ClassInfoBuilder;
import org.castor.jdo.jpa.info.FieldInfo;
import org.castor.jdo.jpa.natures.JPAFieldNature;
import org.exolab.castor.mapping.MappingException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestJPAManyToMany {

    @Before
    public void setUp() {
    }

    @Test
    public void testUnidirectional() {
        try {
            ClassInfoBuilder.buildClassInfo(Unidir1.class);
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testBidirectional1() throws MappingException {
        /* build class */
        ClassInfo classInfo = ClassInfoBuilder.buildClassInfo(Bidir1a.class);
        assertEquals(1, classInfo.getFieldCount());
        assertEquals(1, classInfo.getKeyFieldCount());

        FieldInfo fieldInfo = classInfo.getFieldInfos().iterator().next();
        JPAFieldNature fieldNature = new JPAFieldNature(fieldInfo);

        assertTrue(fieldNature.isManyToMany());
        assertFalse(fieldNature.isManyToManyInverseCopy());
        assertTrue(fieldNature.isRelationLazyFetch());
        assertFalse(fieldNature.isRelationOptional());
        assertEquals(Collection.class, fieldNature.getRelationCollectionType());
        assertNull(fieldNature.getRelationMappedBy());
        assertEquals(Bidir1b.class, fieldNature.getRelationTargetEntity());

        classInfo = ClassInfoBuilder.buildClassInfo(Bidir1b.class);
        assertEquals(1, classInfo.getFieldCount());
        assertEquals(1, classInfo.getKeyFieldCount());

        fieldInfo = classInfo.getFieldInfos().iterator().next();
        fieldNature = new JPAFieldNature(fieldInfo);

        assertTrue(fieldNature.isManyToMany());
        assertTrue(fieldNature.isManyToManyInverseCopy());
        assertTrue(fieldNature.isRelationLazyFetch());
        assertFalse(fieldNature.isRelationOptional());
        assertEquals(Collection.class, fieldNature.getRelationCollectionType());
        assertNotNull(fieldNature.getRelationMappedBy());
        assertEquals("dependents", fieldNature.getRelationMappedBy());
        assertEquals(Bidir1a.class, fieldNature.getRelationTargetEntity());
        assertNotNull(fieldNature.getCascadeTypes());
        assertEquals(2, fieldNature.getCascadeTypes().length);
        assertTrue(Arrays.asList(fieldNature.getCascadeTypes())
                .contains(CascadeType.PERSIST));
        assertTrue(Arrays.asList(fieldNature.getCascadeTypes())
                .contains(CascadeType.REMOVE));
    }

    @Test
    public void testBidirectional2WrongMappedBy() {
        try {
            ClassInfoBuilder.buildClassInfo(Bidir2WrongMappedByA.class);
            fail();
        } catch (Exception e) {
        }

        try {
            ClassInfoBuilder.buildClassInfo(Bidir2WrongMappedByB.class);
            fail();
        } catch (Exception e) {
        }

        try {
            ClassInfoBuilder.buildClassInfo(Bidir2WrongMappedByC.class);
            fail();
        } catch (Exception e) {
        }

        try {
            ClassInfoBuilder.buildClassInfo(Bidir2WrongMappedByD.class);
            fail();
        } catch (Exception e) {
        }

        try {
            ClassInfoBuilder.buildClassInfo(Bidir2WrongMappedByE.class);
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testSelfrelated() throws MappingException {
        /* build class */
        ClassInfo classInfo = ClassInfoBuilder
                .buildClassInfo(SelfRelated.class);

        assertEquals(1, classInfo.getFieldCount());
        assertEquals(1, classInfo.getKeyFieldCount());

        FieldInfo fieldInfo = classInfo.getFieldInfos().iterator().next();
        JPAFieldNature fieldNature = new JPAFieldNature(fieldInfo);

        assertTrue(fieldNature.isManyToMany());
        assertFalse(fieldNature.isManyToManyInverseCopy());
        assertTrue(fieldNature.isRelationLazyFetch());
        assertFalse(fieldNature.isRelationOptional());
        assertEquals(Collection.class, fieldNature.getRelationCollectionType());
        assertNull(fieldNature.getRelationMappedBy());
        assertEquals(SelfRelated.class, fieldNature.getRelationTargetEntity());
    }

    @Entity
    @Ignore
    public class SelfRelated {
        private int _id;
        private Collection<SelfRelated> _friends;

        @Id
        public int getId() {
            return _id;
        }

        public void setId(final int id) {
            _id = id;
        }

        @ManyToMany
        @JoinTable()
        public Collection<SelfRelated> getFriends() {
            return _friends;
        }

        public void setFriends(final Collection<SelfRelated> others) {
            _friends = others;
        }
    }

    @Entity
    @Ignore
    public class Unidir1 {
        private int _id;
        private Collection<Unidir2> _dependents;

        @Id
        public int getId() {
            return _id;
        }

        public void setId(final int id) {
            _id = id;
        }

        @ManyToMany
        public Collection<Unidir2> getDependents() {
            return _dependents;
        }

        public void setDependents(final Collection<Unidir2> dependents) {
            _dependents = dependents;
        }
    }

    @Entity
    @Ignore
    public class Unidir2 {
        private int _id;

        @Id
        public int getId() {
            return _id;
        }

        public void setId(final int id) {
            _id = id;
        }
    }

    @Entity
    @Ignore
    public class Bidir1a {
        private int _id;
        private Collection<Bidir1b> _dependents;

        @Id
        public int getId() {
            return _id;
        }

        public void setId(final int id) {
            _id = id;
        }

        @ManyToMany
        @JoinTable()
        public Collection<Bidir1b> getDependents() {
            return _dependents;
        }

        public void setDependents(final Collection<Bidir1b> dependents) {
            _dependents = dependents;
        }
    }

    @Entity
    @Ignore
    public class Bidir1b {
        private int _id;
        private Collection<Bidir1a> _owner;

        @Id
        public int getId() {
            return _id;
        }

        public void setId(final int id) {
            _id = id;
        }

        @ManyToMany(
                mappedBy = "dependents",
                cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
        )
        public Collection<Bidir1a> getOwner() {
            return _owner;
        }

        public void setOwner(final Collection<Bidir1a> owner) {
            _owner = owner;
        }
    }

    @Entity
    @Ignore
    public class Bidir2WrongMappedByA {
        private int _id;
        private Collection<Bidir2WrongMappedByB> _dependents;

        @Id
        public int getId() {
            return _id;
        }

        public void setId(final int id) {
            _id = id;
        }

        @ManyToMany
        public Collection<Bidir2WrongMappedByB> getDependents() {
            return _dependents;
        }

        public void setDependents(
                final Collection<Bidir2WrongMappedByB> dependents) {
            _dependents = dependents;
        }
    }

    @Entity
    @Ignore
    public class Bidir2WrongMappedByB {
        private int _id;
        private Collection<Bidir2WrongMappedByA> _owner;

        @Id
        public int getId() {
            return _id;
        }

        public void setId(final int id) {
            _id = id;
        }

        @ManyToMany(mappedBy = "non existant")
        public Collection<Bidir2WrongMappedByA> getOwner() {
            return _owner;
        }

        public void setOwner(final Collection<Bidir2WrongMappedByA> owner) {
            _owner = owner;
        }
    }

    @Entity
    @Ignore
    public class Bidir2WrongMappedByC {
        private int _id;
        private Collection<Bidir2WrongMappedByA> _owner;

        @Id
        public int getId() {
            return _id;
        }

        public void setId(final int id) {
            _id = id;
        }

        @ManyToMany(mappedBy = "dependents")
        public Collection<Bidir2WrongMappedByA> getOwner() {
            return _owner;
        }

        public void setOwner(final Collection<Bidir2WrongMappedByA> owner) {
            _owner = owner;
        }
    }

    @Entity
    @Ignore
    public class Bidir2WrongMappedByD {
        private int _id;
        private Collection<Bidir2WrongMappedByE> _dependents;

        @Id
        public int getId() {
            return _id;
        }

        public void setId(final int id) {
            _id = id;
        }

        @ManyToMany(mappedBy = "others")
        public Collection<Bidir2WrongMappedByE> getDependents() {
            return _dependents;
        }

        public void setDependents(
                final Collection<Bidir2WrongMappedByE> dependents) {
            _dependents = dependents;
        }
    }

    @Entity
    @Ignore
    public class Bidir2WrongMappedByE {
        private int _id;
        private Collection<Bidir2WrongMappedByD> _others;

        @Id
        public int getId() {
            return _id;
        }

        public void setId(final int id) {
            _id = id;
        }

        @ManyToMany(mappedBy = "dependents")
        public Collection<Bidir2WrongMappedByD> getOthers() {
            return _others;
        }

        public void setOthers(final Collection<Bidir2WrongMappedByD> others) {
            _others = others;
        }
    }

}
