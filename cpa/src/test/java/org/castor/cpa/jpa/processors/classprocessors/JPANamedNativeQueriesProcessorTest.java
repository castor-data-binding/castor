package org.castor.cpa.jpa.processors.classprocessors;

import javax.persistence.NamedQuery;
import javax.persistence.NamedNativeQueries;
import java.util.Map;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.castor.cpa.jpa.processors.classprocessors.JPANamedNativeQueriesProcessor;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.Entity;

import javax.persistence.NamedNativeQuery;
import static org.junit.Assert.*;

public class JPANamedNativeQueriesProcessorTest {

    final static String NAME = "name";
    final static String QUERY = "query";
    final static String NAME2 = "name2";
    final static String QUERY2 = "query2";

    JPANamedNativeQueriesProcessor processor;
    JPAClassNature classNature;
    @Mock
    NamedNativeQueries annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        processor = new JPANamedNativeQueriesProcessor();
        ClassInfo classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        classNature = new JPAClassNature(classInfo);
    }

    @Test
    public void processorIsForNamedNativeQueriesAnnotation() throws Exception {
        assertEquals(NamedNativeQueries.class, processor.forAnnotationClass());
    }

    @Test
    public void processorReturnsTrueForNamedNativeQueriesAnnotatedClassCorrectly()
            throws Exception {
        boolean result = processor.processAnnotation(classNature, annotation,
                NamedNativeQueriesAnnotatedClass.class);
        assertTrue(result);
    }

    @Test
    public void annotationValuesAreProcessedCorrectly() throws Exception {

        ClassInfo classInfo = ClassInfoBuilder
                .buildClassInfo(NamedNativeQueriesAnnotatedClass.class);
        assertNotNull(classInfo);
        assertTrue(classInfo.hasNature(JPAClassNature.class.getName()));
        JPAClassNature jpaClassNature = new JPAClassNature(classInfo);

        final Map queryMap = jpaClassNature.getNamedNativeQuery();
        assertNotNull(queryMap);
        assertTrue(queryMap.keySet().contains(NAME));
        assertTrue(queryMap.keySet().contains(NAME2));
        assertEquals(QUERY, queryMap.get(NAME));
        assertEquals(QUERY2, queryMap.get(NAME2));
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
    @Entity
    @NamedNativeQueries( { @NamedNativeQuery(name = NAME, query = QUERY),
            @NamedNativeQuery(name = NAME2, query = QUERY2) })
    private class NamedNativeQueriesAnnotatedClass {
    }

    private class NonAnnotatedClass {
    }

    @Entity
    @NamedQuery(name = "this is not a NamedNativeQuery annotation", query = "")
    private class OtherwiseAnnotatedClass {
    }
}
