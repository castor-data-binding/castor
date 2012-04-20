package org.castor.cpa.jpa.processors.classprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class JPANamedQueriesProcessorTest {
    static final String NAME = "name";
    static final String QUERY = "query";
    static final String NAME2 = "name2";
    static final String QUERY2 = "query2";
    
    private JPANamedQueriesProcessor _processor;
    private JPAClassNature _classNature;
    @Mock
    private NamedQueries _annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        _processor = new JPANamedQueriesProcessor();
        ClassInfo classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        _classNature = new JPAClassNature(classInfo);
    }

    @Test
    public void processorIsForNamedQueriesAnnotation() throws Exception {
        assertEquals(NamedQueries.class, _processor.forAnnotationClass());
    }

    @Test
    public void processorReturnsTrueForNamedQueriesAnnotatedClassCorrectly()
            throws Exception {
        boolean result = _processor.processAnnotation(_classNature, _annotation,
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

        final Map<String, String> namedQueryMap = jpaClassNature.getNamedQuery();
        assertNotNull(namedQueryMap);
        assertTrue(namedQueryMap.keySet().contains(NAME));
        assertTrue(namedQueryMap.keySet().contains(NAME2));
        assertEquals(QUERY, namedQueryMap.get(NAME));
        assertEquals(QUERY2, namedQueryMap.get(NAME2));
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
