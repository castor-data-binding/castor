package org.castor.cpa.jpa.processors.classprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class JPANamedNativeQueriesProcessorTest {
    static final String NAME = "name";
    static final String QUERY = "query";
    static final String NAME2 = "name2";
    static final String QUERY2 = "query2";

    private JPANamedNativeQueriesProcessor _processor;
    private JPAClassNature _classNature;
    @Mock
    private NamedNativeQueries _annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        _processor = new JPANamedNativeQueriesProcessor();
        ClassInfo classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        _classNature = new JPAClassNature(classInfo);
    }

    @Test
    public void processorIsForNamedNativeQueriesAnnotation() throws Exception {
        assertEquals(NamedNativeQueries.class, _processor.forAnnotationClass());
    }

    @Test
    public void processorReturnsTrueForNamedNativeQueriesAnnotatedClassCorrectly()
            throws Exception {
        boolean result = _processor.processAnnotation(_classNature, _annotation,
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

        final Map<String, String> queryMap = jpaClassNature.getNamedNativeQuery();
        assertNotNull(queryMap);
        assertTrue(queryMap.keySet().contains(NAME));
        assertTrue(queryMap.keySet().contains(NAME2));
        assertEquals(QUERY, queryMap.get(NAME));
        assertEquals(QUERY2, queryMap.get(NAME2));
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

    // test classes
    @Entity
    @NamedNativeQueries({ @NamedNativeQuery(name = NAME, query = QUERY),
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
