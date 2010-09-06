package org.castor.jdo.jpa.processors.classprocessors;

import java.lang.reflect.AnnotatedElement;

import javax.persistence.Entity;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

import org.castor.jdo.jpa.info.ClassInfo;
import org.castor.jdo.jpa.info.JPAKeyGeneratorManager;
import org.castor.jdo.jpa.info.JPATableGeneratorDescriptor;
import org.castor.jdo.jpa.natures.JPAClassNature;
import org.castor.jdo.jpa.processors.BaseJPAAnnotationProcessor;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class JPATableGeneratorClassProcessorTest {

	JPATableGeneratorClassProcessor processor = new JPATableGeneratorClassProcessor();
	JPAClassNature nature;
	@Mock
	TableGenerator annotation;
	@Mock
	AnnotatedElement target;
	
	@Before
	public void setUp() throws Exception {
		processor = new JPATableGeneratorClassProcessor();
		MockitoAnnotations.initMocks(this);
		initNature();
		when(annotation.name()).thenReturn("name");
		when(annotation.uniqueConstraints()).thenReturn(new UniqueConstraint[]{});
	}
	
	private void initNature() throws Exception {
		ClassInfo classInfo = new ClassInfo(this.getClass());
		classInfo.addNature(JPAClassNature.class.getCanonicalName());
		nature = new JPAClassNature(classInfo);
	}
	
	@SuppressWarnings("cast")
	@Test
	public void processorIsBaseJPAAnnotationProcessor() throws Exception {
		assertTrue(processor instanceof BaseJPAAnnotationProcessor);
	}
	
	@Test
	public void processorIsForTableGeneratorAnnotation() throws Exception {
		assertEquals(TableGenerator.class, processor.forAnnotationClass());
	}
	
//	@Test
//	public void processorReturnsTrueForTableGeneratorAnnotatedClass() throws Exception {
//		boolean result = processor.processAnnotation(nature, annotation, AnnotatedClass.class);
//		assertTrue(result);
//	}
	
	@Test
	public void nonAnnotatedClassResultsInFalseProcessingResult() throws Exception {
		boolean result = processor.processAnnotation(nature, annotation, NonAnnotatedClass.class);
		assertFalse(result);
	}

	@Test
	public void otherwiseAnnotatedClassResultsInFalseProcessingResult() throws Exception {
		boolean result = processor.processAnnotation(nature, annotation, OtherwiseAnnotatedClass.class);
		assertFalse(result);
	}
	
	@Test
	public void nonEntityAnnotatedClassResultsInFalseProcessingResult() throws Exception {
		boolean result = processor.processAnnotation(nature, annotation, NonEntityAnnotatedClass.class);
		assertFalse(result);
	}
	
	@Test
	public void processedTableGeneratorWillBeManaged() throws Exception {
		processor.processAnnotation(nature, annotation, AnnotatedClass.class);
		
		JPAKeyGeneratorManager manager = JPAKeyGeneratorManager.getInstance();
		JPATableGeneratorDescriptor tableGenerator = (JPATableGeneratorDescriptor)manager.get("name");
		
		assertEquals("name", tableGenerator.getName());
	}
	
	@TableGenerator(name="testGenerator")
	@Entity
	class AnnotatedClass {

	}
	
	@TableGenerator(name="generatorName")
	class NonEntityAnnotatedClass {

	}
	
	class NonAnnotatedClass {
	}
	
	@Entity
	class OtherwiseAnnotatedClass {
	}
}
