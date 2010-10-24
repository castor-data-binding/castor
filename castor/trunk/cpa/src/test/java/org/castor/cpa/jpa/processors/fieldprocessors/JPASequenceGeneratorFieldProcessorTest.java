package org.castor.cpa.jpa.processors.fieldprocessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.castor.core.nature.PropertyHolder;
import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.FieldInfo;
import org.castor.cpa.jpa.info.JPAKeyGeneratorManager;
import org.castor.cpa.jpa.info.JPASequenceGeneratorDescriptor;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.castor.cpa.jpa.processors.fieldprocessors.JPASequenceGeneratorFieldProcessor;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class JPASequenceGeneratorFieldProcessorTest {

	JPASequenceGeneratorFieldProcessor processor;
	JPAFieldNature nature;
	String sequenceGeneratorName = "generatorName";
	SequenceGenerator annotation;
	@Mock
	AnnotatedElement target;
	JPAKeyGeneratorManager manager = JPAKeyGeneratorManager.getInstance();
	
	
	String generatorName = "testGenerator";
	int initialValue = 1;
	int allocationSize = 1;
	String sequenceName = "testSequence";
	
	@SuppressWarnings("unused")
	private Long id;
	
	@SuppressWarnings("unused")
	private void setter(Long value) {
		
	}
	
	@SequenceGenerator(name="generatorName")
	@Id
	@SuppressWarnings("unused")
	private Long sequenceGeneratorAnnotatedGetter() {
		return 0l;
	}
	
	@SequenceGenerator(name="generatorName")
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
	
	@SequenceGenerator(name="generatorName")
	@SuppressWarnings("unused")
	private Long sequenceGeneratorAnnotatedGetterNotAnnotatedWithId() {
		return 0l;
	}
	
	@Before
	public void setUp() throws Exception {
		processor = new JPASequenceGeneratorFieldProcessor();
		MockitoAnnotations.initMocks(this);
		initNature();
		annotation = new SequenceGenerator() {
			
			public Class<? extends Annotation> annotationType() {
				return SequenceGenerator.class;
			}
			
			public String sequenceName() {
				return sequenceName;
			}
			
			public String name() {
				return generatorName;
			}
			
			public int initialValue() {
				return initialValue;
			}
			
			public int allocationSize() {
				return allocationSize;
			}
		};		
	}

	private void initNature() throws Exception {
		ClassInfo classInfo = new ClassInfo(this.getClass());

		Method getter = this.getClass().getDeclaredMethod("sequenceGeneratorAnnotatedGetter");
		Method setter = this.getClass().getDeclaredMethod("setter", Long.class);
		PropertyHolder holder = new FieldInfo(classInfo, Long.class, "id", getter, setter);
		holder.addNature(JPAFieldNature.class.getCanonicalName());
		nature = new JPAFieldNature(holder);
	}
	
	@Test
	public void processorIsForSequenceGeneratorAnnotation() throws Exception {
		assertEquals(SequenceGenerator.class, processor.forAnnotationClass());
	}
	
	private boolean processAnnotationOnMethod(String methodName) throws Exception {
		Method method = this.getClass().getDeclaredMethod(methodName);
		boolean result = processor.processAnnotation(nature, annotation, method);
		
		return result;
	}
	
	@Ignore
	@Test
	public void processorReturnsTrueForSequenceGeneratorAnnotatedGetter() throws Exception {
		boolean result = processAnnotationOnMethod("sequenceGeneratorAnnotatedGetter");
		assertTrue(result);
	}

	@Test
	public void processorReturnsFalseForSequenceGeneratorAnnotatedGetterNotAnnotatedWithId() throws Exception {
		boolean result = processAnnotationOnMethod("sequenceGeneratorAnnotatedGetterNotAnnotatedWithId");
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
	public void processedSequenceGeneratorWillBeManaged() throws Exception {
		
		Method method = this.getClass().getDeclaredMethod("sequenceGeneratorAnnotatedGetter");
		
		processor.processAnnotation(nature, annotation, method);

		JPASequenceGeneratorDescriptor actualDescriptor = (JPASequenceGeneratorDescriptor) manager
				.get(generatorName);
		assertEquals(generatorName, actualDescriptor.getName());
	}
	
}
