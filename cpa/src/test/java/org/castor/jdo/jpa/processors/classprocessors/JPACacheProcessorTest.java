package org.castor.jdo.jpa.processors.classprocessors;

import java.util.Properties;

import javax.persistence.Entity;

import org.castor.core.nature.PropertyHolder;
import org.castor.jdo.jpa.annotations.Cache;
import org.castor.jdo.jpa.annotations.CacheProperty;
import org.castor.jdo.jpa.info.ClassInfo;
import org.castor.jdo.jpa.natures.JPAClassNature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class JPACacheProcessorTest {
	JPACacheProcessor processor;
	JPAClassNature classNature;
	@Mock
	Cache annotation;
	@Mock
	CacheProperty cacheProperty;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		processor = new JPACacheProcessor();
		ClassInfo classInfo = new ClassInfo();
		classInfo.addNature(JPAClassNature.class.getCanonicalName());
		classNature = new JPAClassNature(classInfo);
	}
	
	@Test
	public void processorIsForCacheAnnotation() throws Exception {
		assertEquals(Cache.class, processor.forAnnotationClass());
	}
	
	@Test
	public void processorReturnsTrueForCacheAnnotatedClass() throws Exception {
		
		CacheProperty[] cacheProperties = new CacheProperty[1];
		when(cacheProperty.key()).thenReturn("type");
		when(cacheProperty.value()).thenReturn("none");
		cacheProperties[0] = cacheProperty;
		when(annotation.value()).thenReturn(cacheProperties);
		boolean result = processor.processAnnotation(classNature, annotation, CacheAnnotatedClass.class);
		assertTrue(result);
	}
	
	@Test
	public void processorReturnsNatureEnrichedWithCacheProperties() throws Exception {
		CacheProperty[] cacheProperties = new CacheProperty[1];
		when(cacheProperty.key()).thenReturn("type");
		when(cacheProperty.value()).thenReturn("none");
		cacheProperties[0] = cacheProperty;
		when(annotation.value()).thenReturn(cacheProperties);
		processor.processAnnotation(classNature, annotation, CacheAnnotatedClass.class);
		Properties actualProperties = classNature.getCacheProperties();
		assertEquals("none", actualProperties.getProperty("type"));
	}
	
	@Test
	public void processorReturnsFalseForNonAnnotatedClass() throws Exception {
		boolean result = processor.processAnnotation(classNature, annotation, NonAnnotatedClass.class);
		assertFalse(result);
	}
	
	@Test
	public void processorReturnsFalseForOtherwiseAnnotatedClass() throws Exception {
		boolean result = processor.processAnnotation(classNature, annotation, OtherwiseAnnotatedClass.class);
		assertFalse(result);
	}
	
	@Cache({
		@CacheProperty(key="", value="")
	})
	private class CacheAnnotatedClass {
		
	}
	
	private class NonAnnotatedClass {
		
	}
	
	@Entity
	private class OtherwiseAnnotatedClass {
		
	}
}
