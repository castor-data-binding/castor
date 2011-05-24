package org.castor.cpa.jpa.processors.classprocessors;

import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.castor.cpa.jpa.processors.classprocessors.JPAInheritanceProcessor;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.Entity;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedNativeQuery;
import org.exolab.castor.mapping.MappingException;
import static org.junit.Assert.*;

public class JPAInheritanceProcessorTest {

    JPAInheritanceProcessor processor;
    JPAClassNature classNature;
    @Mock
    Inheritance annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        processor = new JPAInheritanceProcessor();
        ClassInfo classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        classNature = new JPAClassNature(classInfo);
    }

    @Test
    public void processorIsForNamedQueriesAnnotation() throws Exception {
        assertEquals(Inheritance.class, processor.forAnnotationClass());
    }

    @Test(expected=MappingException.class)
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
        boolean result = processor.processAnnotation(classNature, annotation,
                NonAnnotatedClass.class);
        assertFalse(result);
    }

    @Test
    public void processorReturnsFalseForOtherwiseAnnotatedClass()
            throws Exception {
        boolean result = processor.processAnnotation(classNature, annotation,
                OtherwiseAnnotatedClass.class);
        assertFalse(result);
    }

    @Entity()
    private class BaseClass {
    }

    @Entity
    @Inheritance(strategy = InheritanceType.JOINED)
    private class JoinedAnnotatedClass extends BaseClass{
    }

    @Entity()
    @Inheritance
    private class InheritanceAnnotatedClass extends BaseClass{
    }

    private class NonAnnotatedClass {
    }

    @Entity
    @NamedNativeQuery(name = "this is not a Inheritance annotation", query = "")
    private class OtherwiseAnnotatedClass extends BaseClass{
    }
}
