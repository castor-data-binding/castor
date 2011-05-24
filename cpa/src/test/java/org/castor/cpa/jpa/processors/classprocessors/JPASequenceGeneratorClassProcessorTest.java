package org.castor.cpa.jpa.processors.classprocessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.JPAKeyGeneratorManager;
import org.castor.cpa.jpa.info.JPASequenceGeneratorDescriptor;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.castor.cpa.jpa.processors.classprocessors.JPASequenceGeneratorClassProcessor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class JPASequenceGeneratorClassProcessorTest {

	JPASequenceGeneratorClassProcessor processor;
	JPAClassNature nature;
	SequenceGenerator annotation;
	@Mock
	AnnotatedElement target;
	JPAKeyGeneratorManager manager = JPAKeyGeneratorManager.getInstance();

	String generatorName = "testGenerator";
	int initialValue = 1;
	int allocationSize = 1;
	String sequenceName = "testSequence";

	@Before
	public void setUp() throws Exception {
		processor = new JPASequenceGeneratorClassProcessor();
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
		classInfo.addNature(JPAClassNature.class.getCanonicalName());
		nature = new JPAClassNature(classInfo);
	}

	@After
	public void tearDown() throws Exception {
		manager.reset();
	}

	@Test
	public void processorIsForSequenceGeneratorAnnotation() throws Exception {
		assertEquals(SequenceGenerator.class, processor.forAnnotationClass());
	}

	@Test
	public void processorReturnsTrueForSequenceGeneratorAnnotatedClass()
			throws Exception {
		boolean result = processor.processAnnotation(nature, annotation,
				AnnotatedClass.class);
		assertTrue(result);
	}

	@Test
	public void nonAnnotatedClassResultsInFalseProcessingResult()
			throws Exception {
		boolean result = processor.processAnnotation(nature, annotation,
				NonAnnotatedClass.class);
		assertFalse(result);
	}

	@Test
	public void otherwiseAnnotatedClassResultsInFalseProcessingResult()
			throws Exception {
		boolean result = processor.processAnnotation(nature, annotation,
				OtherwiseAnnotatedClass.class);
		assertFalse(result);
	}

	@Test
	public void nonEntityAnnotatedClassResultsInFalseProcessingResult()
			throws Exception {
		boolean result = processor.processAnnotation(nature, annotation,
				NonEntityAnnotatedClass.class);
		assertFalse(result);
	}

	@Test
	public void processedSequenceGeneratorWillBeManaged() throws Exception {

		processor.processAnnotation(nature, annotation, AnnotatedClass.class);

		JPASequenceGeneratorDescriptor actualDescriptor = (JPASequenceGeneratorDescriptor) manager
				.get(generatorName);
		assertEquals("testGenerator", actualDescriptor.getName());
	}

	@SequenceGenerator(name = "testGenerator", allocationSize = 1, initialValue = 1, sequenceName = "sequenceName")
	@Entity
	class AnnotatedClass {

	}

	@SequenceGenerator(name = "generatorName")
	class NonEntityAnnotatedClass {

	}

	class NonAnnotatedClass {
	}

	@Deprecated
	class OtherwiseAnnotatedClass {
	}

}
