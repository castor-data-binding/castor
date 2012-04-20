package org.castor.cpa.jpa.processors.classprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Properties;

import javax.persistence.Entity;

import org.castor.cpa.jpa.annotations.Cache;
import org.castor.cpa.jpa.annotations.CacheProperty;
import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class JPACacheProcessorTest {
    private JPACacheProcessor _processor;
    private JPAClassNature _classNature;
    @Mock
    private Cache _annotation;
    @Mock
    private CacheProperty _cacheProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        _processor = new JPACacheProcessor();
        ClassInfo classInfo = new ClassInfo();
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        _classNature = new JPAClassNature(classInfo);
    }

    @Test
    public void processorIsForCacheAnnotation() throws Exception {
        assertEquals(Cache.class, _processor.forAnnotationClass());
    }
    
    @Test
    public void processorReturnsTrueForCacheAnnotatedClass() throws Exception {
        
        CacheProperty[] cacheProperties = new CacheProperty[1];
        when(_cacheProperty.key()).thenReturn("type");
        when(_cacheProperty.value()).thenReturn("none");
        cacheProperties[0] = _cacheProperty;
        when(_annotation.value()).thenReturn(cacheProperties);
        boolean result = _processor.processAnnotation(
                _classNature, _annotation, CacheAnnotatedClass.class);
        assertTrue(result);
    }
    
    @Test
    public void processorReturnsNatureEnrichedWithCacheProperties() throws Exception {
        CacheProperty[] cacheProperties = new CacheProperty[1];
        when(_cacheProperty.key()).thenReturn("type");
        when(_cacheProperty.value()).thenReturn("none");
        cacheProperties[0] = _cacheProperty;
        when(_annotation.value()).thenReturn(cacheProperties);
        _processor.processAnnotation(_classNature, _annotation, CacheAnnotatedClass.class);
        Properties actualProperties = _classNature.getCacheProperties();
        assertEquals("none", actualProperties.getProperty("type"));
    }
    
    @Test
    public void processorReturnsFalseForNonAnnotatedClass() throws Exception {
        boolean result = _processor.processAnnotation(
                _classNature, _annotation, NonAnnotatedClass.class);
        assertFalse(result);
    }
    
    @Test
    public void processorReturnsFalseForOtherwiseAnnotatedClass() throws Exception {
        boolean result = _processor.processAnnotation(
                _classNature, _annotation, OtherwiseAnnotatedClass.class);
        assertFalse(result);
    }
    
    @Cache({
        @CacheProperty(key = "", value = "")
    })
    private class CacheAnnotatedClass {
        
    }
    
    private class NonAnnotatedClass {
        
    }
    
    @Entity
    private class OtherwiseAnnotatedClass {
        
    }
}
