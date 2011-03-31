package org.castor.cpa.jpa.processors.classprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class JPAMappedSuperclassProcessorTest {
    private JPAMappedSuperclassProcessor _processor;
    private JPAClassNature _classNature;
    @Mock
    private MappedSuperclass _annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        _processor = new JPAMappedSuperclassProcessor();
        ClassInfo classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        _classNature = new JPAClassNature(classInfo);
    }

    @Test
    public void processorIsForMappedSuperclassAnnotation() throws Exception {
        assertEquals(MappedSuperclass.class, _processor.forAnnotationClass());
    }

    @Test
    public void processorReturnsTrueForMappedSuperclassAnnotatedClassCorrectly()
            throws Exception {
        boolean result = _processor.processAnnotation(_classNature, _annotation,
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
        boolean result = _processor.processAnnotation(_classNature, _annotation,
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
        boolean result = _processor.processAnnotation(_classNature, _annotation,
                OtherwiseAnnotatedClass.class);
        assertFalse(result);
    }

     @Test
    public void processorReturnsFalseForNonAbstractSubclass()
            throws Exception {
        boolean result = _processor.processAnnotation(_classNature, _annotation,
                NonAbstractSubclass.class);
        assertFalse(result);
    }

    //test classes
    @MappedSuperclass
    private class MappedSuperclassAnnotatedClass { }

    private class NonAnnotatedClass { }

    @Entity
    private class OtherwiseAnnotatedClass { }

    @Entity
    private class NonAbstractSubclass extends MappedSuperclassAnnotatedClass { }
}
