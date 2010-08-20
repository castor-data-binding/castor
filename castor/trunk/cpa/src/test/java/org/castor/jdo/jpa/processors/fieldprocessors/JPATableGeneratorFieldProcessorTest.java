package org.castor.jdo.jpa.processors.fieldprocessors;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

import org.castor.core.nature.PropertyHolder;
import org.castor.jdo.jpa.info.ClassInfo;
import org.castor.jdo.jpa.info.FieldInfo;
import org.castor.jdo.jpa.info.JPAKeyGeneratorManager;
import org.castor.jdo.jpa.info.JPATableGeneratorDescriptor;
import org.castor.jdo.jpa.natures.JPAFieldNature;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;


public class JPATableGeneratorFieldProcessorTest {

	JPATableGeneratorFieldProcessor processor;
	JPAFieldNature nature;
	String tableGeneratorName = "generatorName";
	@Mock
	TableGenerator annotation;
	@Mock
	AnnotatedElement target;
	JPAKeyGeneratorManager manager = JPAKeyGeneratorManager.getInstance();
	
	@SuppressWarnings("unused")
	private Long id;
	
	@SuppressWarnings("unused")
	private void setter(Long value) {
		
	}
	
	@TableGenerator(name="generatorName")
	@Id
	@SuppressWarnings("unused")
	private Long tableGeneratorAnnotatedGetter() {
		return 0l;
	}
	
	@TableGenerator(name="generatorName")
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
	
	@TableGenerator(name="generatorName")
	@SuppressWarnings("unused")
	private Long tableGeneratorAnnotatedGetterNotAnnotatedWithId() {
		return 0l;
	}
	
	@Before
	public void setUp() throws Exception {
		processor = new JPATableGeneratorFieldProcessor();
		MockitoAnnotations.initMocks(this);
		initNature();
		when(annotation.name()).thenReturn("name");
		when(annotation.uniqueConstraints()).thenReturn(new UniqueConstraint[]{});
	}

	private void initNature() throws Exception {
		ClassInfo classInfo = new ClassInfo(this.getClass());

		Method getter = this.getClass().getDeclaredMethod("tableGeneratorAnnotatedGetter");
		Method setter = this.getClass().getDeclaredMethod("setter", Long.class);
		PropertyHolder holder = new FieldInfo(classInfo, Long.class, "id", getter, setter);
		holder.addNature(JPAFieldNature.class.getCanonicalName());
		nature = new JPAFieldNature(holder);
	}
	
	@Test
	public void processorIsForTableGeneratorAnnotation() throws Exception {
		assertEquals(TableGenerator.class, processor.forAnnotationClass());
	}
	
	private boolean processAnnotationOnMethod(String methodName) throws Exception {
		Method method = this.getClass().getDeclaredMethod(methodName);
		boolean result = processor.processAnnotation(nature, annotation, method);
		
		return result;
	}
	
	@Ignore
	@Test
	public void processorReturnsTrueForTableGeneratorAnnotatedGetter() throws Exception {
		boolean result = processAnnotationOnMethod("tableGeneratorAnnotatedGetter");
		assertTrue(result);
	}

	@Test
	public void processorReturnsFalseForTableGeneratorAnnotatedGetterNotAnnotatedWithId() throws Exception {
		boolean result = processAnnotationOnMethod("tableGeneratorAnnotatedGetterNotAnnotatedWithId");
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
	public void processedTableGeneratorWillBeManaged() throws Exception {
		
		Method method = this.getClass().getDeclaredMethod("tableGeneratorAnnotatedGetter");
		
		processor.processAnnotation(nature, annotation, method);
		
		JPAKeyGeneratorManager manager = JPAKeyGeneratorManager.getInstance();
		JPATableGeneratorDescriptor tableGenerator = (JPATableGeneratorDescriptor)manager.get("name");
		
		assertEquals("name", tableGenerator.getName());
	}
}
