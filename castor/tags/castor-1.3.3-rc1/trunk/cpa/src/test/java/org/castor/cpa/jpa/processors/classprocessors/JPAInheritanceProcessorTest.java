package org.castor.cpa.jpa.processors.classprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedNativeQuery;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.exolab.castor.mapping.MappingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class JPAInheritanceProcessorTest {
    private JPAInheritanceProcessor _processor;
    private JPAClassNature _classNature;
    @Mock
    private Inheritance _annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        _processor = new JPAInheritanceProcessor();
        ClassInfo classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        _classNature = new JPAClassNature(classInfo);
    }

    @Test
    public void processorIsForNamedQueriesAnnotation() throws Exception {
        assertEquals(Inheritance.class, _processor.forAnnotationClass());
    }

    @Test(expected = MappingException.class)
    public void annotationDefaultValuesAreProcessedCorrectly() throws Exception {

        ClassInfo classInfo = ClassInfoBuilder.
                buildClassInfo(InheritanceAnnotatedClass.class);
        assertNotNull(classInfo);
        assertTrue(classInfo.hasNature(JPAClassNature.class.getName()));
        JPAClassNature jpaClassNature = new JPAClassNature(classInfo);
        assertEquals(InheritanceType.SINGLE_TABLE, 
                jpaClassNature.getInheritanceStrategy());
    }

     @Test
    public void annotationExplicitValuesAreProcessedCorrectly() throws Exception {

        ClassInfo classInfo = ClassInfoBuilder.
                buildClassInfo(JoinedAnnotatedClass.class);
        assertNotNull(classInfo);
        assertTrue(classInfo.hasNature(JPAClassNature.class.getName()));
        JPAClassNature jpaClassNature = new JPAClassNature(classInfo);
        assertEquals(InheritanceType.JOINED,
                jpaClassNature.getInheritanceStrategy());
    }

    @Test
    public void processorReturnsFalseForNonAnnotatedClass() throws Exception {
        boolean result = _processor.processAnnotation(_classNature, _annotation,
                NonAnnotatedClass.class);
        assertFalse(result);
    }

    @Test
    public void processorReturnsFalseForOtherwiseAnnotatedClass()
            throws Exception {
        boolean result = _processor.processAnnotation(_classNature, _annotation,
                OtherwiseAnnotatedClass.class);
        assertFalse(result);
    }

    @Entity()
    private class BaseClass { }

    @Entity
    @Inheritance(strategy = InheritanceType.JOINED)
    private class JoinedAnnotatedClass extends BaseClass { }

    @Entity()
    @Inheritance
    private class InheritanceAnnotatedClass extends BaseClass { }

    private class NonAnnotatedClass { }

    @Entity
    @NamedNativeQuery(name = "this is not a Inheritance annotation", query = "")
    private class OtherwiseAnnotatedClass extends BaseClass { }
}
