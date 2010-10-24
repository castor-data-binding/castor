package org.castor.cpa.jpa.processors.classprocessors;

import java.util.Map;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.castor.cpa.jpa.processors.classprocessors.JPANamedQueriesProcessor;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.Entity;

import javax.persistence.NamedNativeQuery;
import static org.junit.Assert.*;

public class JPANamedQueriesProcessorTest {

    final static String NAME = "name";
    final static String QUERY = "query";
    final static String NAME2 = "name2";
    final static String QUERY2 = "query2";
    JPANamedQueriesProcessor processor;
    JPAClassNature classNature;
    @Mock
    NamedQueries annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        processor = new JPANamedQueriesProcessor();
        ClassInfo classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        classNature = new JPAClassNature(classInfo);
    }

    @Test
    public void processorIsForNamedQueriesAnnotation() throws Exception {
        assertEquals(NamedQueries.class, processor.forAnnotationClass());
    }

    @Test
    public void processorReturnsTrueForNamedQueriesAnnotatedClassCorrectly()
            throws Exception {
        boolean result = processor.processAnnotation(classNature, annotation,
                NamedQueriesAnnotatedClass.class);
        assertTrue(result);
    }

    @Test
    public void annotationValuesAreProcessedCorrectly() throws Exception {

        ClassInfo classInfo = ClassInfoBuilder.
                buildClassInfo(NamedQueriesAnnotatedClass.class);
        assertNotNull(classInfo);
        assertTrue(classInfo.hasNature(JPAClassNature.class.getName()));
        JPAClassNature jpaClassNature = new JPAClassNature(classInfo);

        final Map namedQueryMap = jpaClassNature.getNamedQuery();
        assertNotNull(namedQueryMap);
        assertTrue(namedQueryMap.keySet().contains(NAME));
        assertTrue(namedQueryMap.keySet().contains(NAME2));
        assertEquals(QUERY, namedQueryMap.get(NAME));
        assertEquals(QUERY2, namedQueryMap.get(NAME2));
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

    //test classes
    @Entity
    @NamedQueries({
        @NamedQuery(name = NAME, query = QUERY),
        @NamedQuery(name = NAME2, query = QUERY2)
    })
    private class NamedQueriesAnnotatedClass {
    }

    private class NonAnnotatedClass {
    }

    @Entity
    @NamedNativeQuery(name = "this is not a NamedQuery annotation", query = "")
    private class OtherwiseAnnotatedClass {
    }
}
