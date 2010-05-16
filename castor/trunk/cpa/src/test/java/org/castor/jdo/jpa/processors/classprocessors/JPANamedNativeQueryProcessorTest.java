package org.castor.jdo.jpa.processors.classprocessors;

import java.util.Map;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.castor.jdo.jpa.info.ClassInfoBuilder;
import org.castor.jdo.jpa.info.ClassInfo;
import org.castor.jdo.jpa.natures.JPAClassNature;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.Entity;

import static org.junit.Assert.*;

public class JPANamedNativeQueryProcessorTest {

    final static String NAME = "name";
    final static String QUERY = "query";

    JPANamedNativeQueryProcessor processor;
    
    JPAClassNature classNature;
    
    @Mock
    NamedNativeQuery annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        processor = new JPANamedNativeQueryProcessor();
        ClassInfo classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        classNature = new JPAClassNature(classInfo);
    }

    @Test
    public void processorIsForNamedNativeQueryAnnotation() throws Exception {
        assertEquals(NamedNativeQuery.class, processor.forAnnotationClass());
    }

    @Test
    public void processorReturnsTrueForNamedNativeQueryAnnotatedClassCorrectly()
            throws Exception {
        boolean result = processor.processAnnotation(classNature, annotation,
                NamedNativeQueryAnnotatedClass.class);
        assertTrue(result);
    }

    @Test
    public void annotationValuesAreProcessedCorrectly() throws Exception {

        ClassInfo classInfo = ClassInfoBuilder
                .buildClassInfo(NamedNativeQueryAnnotatedClass.class);
        assertNotNull(classInfo);
        assertTrue(classInfo.hasNature(JPAClassNature.class.getName()));
        JPAClassNature jpaClassNature = new JPAClassNature(classInfo);

        final Map namedNativeQueryMap = jpaClassNature.getNamedNativeQuery();
        assertNotNull(namedNativeQueryMap);
        assertEquals(NAME, namedNativeQueryMap.keySet().iterator().next());
        assertEquals(QUERY, namedNativeQueryMap.values().iterator().next());
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

    // test classes

    @Entity()
    @NamedNativeQuery(name = NAME, query = QUERY)
    private class NamedNativeQueryAnnotatedClass {
    }

    private class NonAnnotatedClass {
    }

    @NamedQuery(name = "this is not a NamedNativeQuery annotation", query = "")
    private class OtherwiseAnnotatedClass {
    }
}