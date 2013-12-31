package org.castor.cpa.jpa.processors.classprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class JPANamedNativeQueryProcessorTest {
    static final String NAME = "name";
    static final String QUERY = "query";

    private JPANamedNativeQueryProcessor _processor;
    private JPAClassNature _classNature;
    @Mock
    private NamedNativeQuery _annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        _processor = new JPANamedNativeQueryProcessor();
        ClassInfo classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        _classNature = new JPAClassNature(classInfo);
    }

    @Test
    public void processorIsForNamedNativeQueryAnnotation() throws Exception {
        assertEquals(NamedNativeQuery.class, _processor.forAnnotationClass());
    }

    @Test
    public void processorReturnsTrueForNamedNativeQueryAnnotatedClassCorrectly()
            throws Exception {
        boolean result = _processor.processAnnotation(_classNature, _annotation,
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

        final Map<String, String> namedNativeQueryMap = jpaClassNature.getNamedNativeQuery();
        assertNotNull(namedNativeQueryMap);
        assertEquals(NAME, namedNativeQueryMap.keySet().iterator().next());
        assertEquals(QUERY, namedNativeQueryMap.values().iterator().next());
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