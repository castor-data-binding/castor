package org.castor.cpa.jpa.processors.classprocessors;

import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.castor.cpa.jpa.processors.classprocessors.JPAMappedSuperclassProcessor;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.Entity;

import javax.persistence.MappedSuperclass;
import static org.junit.Assert.*;

public class JPAMappedSuperclassProcessorTest {

    JPAMappedSuperclassProcessor processor;
    JPAClassNature classNature;
    @Mock
    MappedSuperclass annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        processor = new JPAMappedSuperclassProcessor();
        ClassInfo classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        classNature = new JPAClassNature(classInfo);
    }

    @Test
    public void processorIsForMappedSuperclassAnnotation() throws Exception {
        assertEquals(MappedSuperclass.class, processor.forAnnotationClass());
    }

    @Test
    public void processorReturnsTrueForMappedSuperclassAnnotatedClassCorrectly()
            throws Exception {
        boolean result = processor.processAnnotation(classNature, annotation,
                MappedSuperclassAnnotatedClass.class);
        assertTrue(result);
    }

    @Test
    public void annotationValuesAreProcessedCorrectly() throws Exception {

        ClassInfo classInfo = ClassInfoBuilder.
                buildClassInfo(MappedSuperclassAnnotatedClass.class);
        assertNotNull(classInfo);
        assertTrue(classInfo.hasNature(JPAClassNature.class.getName()));
        JPAClassNature jpaClassNature = new JPAClassNature(classInfo);

        assertTrue(jpaClassNature.hasMappedSuperclass());
    }

    @Test
    public void processorReturnsFalseForNonAnnotatedClass() throws Exception {
        boolean result = processor.processAnnotation(classNature, annotation,
                NonAnnotatedClass.class);
        assertFalse(result);
    }

    @Test
    public void annotationDefaultValuesAreProcessedCorrectly() throws Exception {

        ClassInfo classInfo = ClassInfoBuilder.
                buildClassInfo(OtherwiseAnnotatedClass.class);
        assertNotNull(classInfo);
        assertTrue(classInfo.hasNature(JPAClassNature.class.getName()));
        JPAClassNature jpaClassNature = new JPAClassNature(classInfo);

        assertTrue(!jpaClassNature.hasMappedSuperclass());
    }

    @Test
    public void processorReturnsFalseForOtherwiseAnnotatedClass()
            throws Exception {
        boolean result = processor.processAnnotation(classNature, annotation,
                OtherwiseAnnotatedClass.class);
        assertFalse(result);
    }

     @Test
    public void processorReturnsFalseForNonAbstractSubclass()
            throws Exception {
        boolean result = processor.processAnnotation(classNature, annotation,
                NonAbstractSubclass.class);
        assertFalse(result);
    }

    //test classes
    @MappedSuperclass
    private class MappedSuperclassAnnotatedClass {
    }

    private class NonAnnotatedClass {
    }

    @Entity
    private class OtherwiseAnnotatedClass {
    }

    @Entity
    private class NonAbstractSubclass extends MappedSuperclassAnnotatedClass{
    }
}
