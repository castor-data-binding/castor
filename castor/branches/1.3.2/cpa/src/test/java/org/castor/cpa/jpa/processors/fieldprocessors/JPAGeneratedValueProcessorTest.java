package org.castor.cpa.jpa.processors.fieldprocessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.castor.core.nature.PropertyHolder;
import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.FieldInfo;
import org.castor.cpa.jpa.info.JPAKeyGeneratorManager;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAGeneratedValueProcessor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class JPAGeneratedValueProcessorTest {

	JPAGeneratedValueProcessor processor;
	JPAFieldNature nature;
	GeneratedValue annotation;
	@Mock
	AnnotatedElement target;
	JPAKeyGeneratorManager manager = JPAKeyGeneratorManager.getInstance();
	
	@Before
	public void setUp() throws Exception {
		processor = new JPAGeneratedValueProcessor();
		MockitoAnnotations.initMocks(this);
		initNature();
		annotation = new GeneratedValue() {
			
			public Class<? extends Annotation> annotationType() {
				return this.getClass();
			}
			
			public GenerationType strategy() {
				return GenerationType.AUTO;
			}
			
			public String generator() {
				return "generator";
			}
		};	
	}

	private void initNature() throws Exception {
		ClassInfo classInfo = new ClassInfo(this.getClass());

		Method getter = this.getClass().getDeclaredMethod("generatedValueAnnotatedGetter");
		Method setter = this.getClass().getDeclaredMethod("setter", Long.class);
		PropertyHolder holder = new FieldInfo(classInfo, Long.class, "id", getter, setter);
		holder.addNature(JPAFieldNature.class.getCanonicalName());
		nature = new JPAFieldNature(holder);
	}
	
	@Test
	public void processorIsForSequenceGeneratorAnnotation() throws Exception {
		assertEquals(GeneratedValue.class, processor.forAnnotationClass());
	}
	
	private boolean processAnnotationOnMethod(String methodName) throws Exception {
		Method method = this.getClass().getDeclaredMethod(methodName);
		boolean result = processor.processAnnotation(nature, annotation, method);
		
		return result;
	}
	
	@Test
	public void processorReturnsTrueForGeneratedValueAnnotatedGetter() throws Exception {
		boolean result = processAnnotationOnMethod("generatedValueAnnotatedGetter");
		assertTrue(result);
	}

	@Test
	public void processorReturnsFalseForGeneratedValueAnnotatedGetterNotAnnotatedWithId() throws Exception {
		boolean result = processAnnotationOnMethod("generatedValueAnnotatedGetterNotAnnotatedWithId");
		assertFalse(result);
	}

	@Test
	public void nonAnnotatedGetterResultsInFalse() throws Exception {
		boolean result = processAnnotationOnMethod("nonAnnotatedGetter");
		assertFalse(result);
	}

	@Test
	public void otherwiseAnnotatedGetterResultsInFalse() throws Exception {
		boolean result = processAnnotationOnMethod("otherwiseAnnotatedGetter");
		assertFalse(result);
	}
	
	@Test
	public void nonIdAnnotatedGetterResultsInFalse() throws Exception {
		boolean result = processAnnotationOnMethod("nonIdAnnotatedGetter");
		assertFalse(result);
	}
	
	@Test
	public void generationStrategyWillBeSetInNature() throws Exception {	
		Method method = this.getClass().getDeclaredMethod("generatedValueAnnotatedGetter");		
		processor.processAnnotation(nature, annotation, method);	
		assertEquals(GenerationType.AUTO, nature.getGeneratedValueStrategy());
	}

	@Test
	public void generatorNameWillBeSetInNature() throws Exception {	
		Method method = this.getClass().getDeclaredMethod("generatedValueAnnotatedGetter");		
		processor.processAnnotation(nature, annotation, method);	
		assertEquals("generator", nature.getGeneratedValueGenerator());
	}
	
	@SuppressWarnings("unused")
	private Long id;
	
	@SuppressWarnings("unused")
	private void setter(Long value) {
		
	}
	
	@GeneratedValue()
	@Id
	@SuppressWarnings("unused")
	private Long generatedValueAnnotatedGetter() {
		return 0l;
	}
	
	@GeneratedValue()
	@SuppressWarnings("unused")
	private Long nonIdAnnotatedGetter() {
		return 0l;
	}
	
	@SuppressWarnings("unused")
	private Long nonAnnotatedGetter() {
		return 0l;
	}
	
	@Id
	@SuppressWarnings("unused")
	private Long otherwiseAnnotatedGetter() {
		return 0l;
	}
	
	@GeneratedValue()
	@SuppressWarnings("unused")
	private Long generatedValueAnnotatedGetterNotAnnotatedWithId() {
		return 0l;
	}

}
