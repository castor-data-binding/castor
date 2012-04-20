package org.castor.cpa.jpa.processors.classprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import javax.persistence.Entity;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.JPAKeyGeneratorManager;
import org.castor.cpa.jpa.info.JPATableGeneratorDescriptor;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.castor.cpa.jpa.processors.BaseJPAAnnotationProcessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class JPATableGeneratorClassProcessorTest {
    private JPATableGeneratorClassProcessor _processor = new JPATableGeneratorClassProcessor();
    private JPAClassNature _nature;
    @Mock
    private TableGenerator _annotation;
    
    @Before
    public void setUp() throws Exception {
        _processor = new JPATableGeneratorClassProcessor();
        MockitoAnnotations.initMocks(this);
        initNature();
        when(_annotation.name()).thenReturn("name");
        when(_annotation.uniqueConstraints()).thenReturn(new UniqueConstraint[]{});
    }
    
    private void initNature() throws Exception {
        ClassInfo classInfo = new ClassInfo(this.getClass());
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        _nature = new JPAClassNature(classInfo);
    }
    
    @SuppressWarnings("cast")
    @Test
    public void processorIsBaseJPAAnnotationProcessor() throws Exception {
        assertTrue(_processor instanceof BaseJPAAnnotationProcessor);
    }
    
    @Test
    public void processorIsForTableGeneratorAnnotation() throws Exception {
        assertEquals(TableGenerator.class, _processor.forAnnotationClass());
    }
    
//  @Test
//  public void processorReturnsTrueForTableGeneratorAnnotatedClass() throws Exception {
//      boolean result = processor.processAnnotation(nature, annotation, AnnotatedClass.class);
//      assertTrue(result);
//  }
    
    @Test
    public void nonAnnotatedClassResultsInFalseProcessingResult() throws Exception {
        boolean result = _processor.processAnnotation(
                _nature, _annotation, NonAnnotatedClass.class);
        assertFalse(result);
    }

    @Test
    public void otherwiseAnnotatedClassResultsInFalseProcessingResult() throws Exception {
        boolean result = _processor.processAnnotation(
                _nature, _annotation, OtherwiseAnnotatedClass.class);
        assertFalse(result);
    }
    
    @Test
    public void nonEntityAnnotatedClassResultsInFalseProcessingResult() throws Exception {
        boolean result = _processor.processAnnotation(
                _nature, _annotation, NonEntityAnnotatedClass.class);
        assertFalse(result);
    }
    
    @Test
    public void processedTableGeneratorWillBeManaged() throws Exception {
        _processor.processAnnotation(_nature, _annotation, AnnotatedClass.class);
        
        JPAKeyGeneratorManager manager = JPAKeyGeneratorManager.getInstance();
        JPATableGeneratorDescriptor tableGenerator =
            (JPATableGeneratorDescriptor) manager.get("name");
        
        assertEquals("name", tableGenerator.getName());
    }
    
    @TableGenerator(name = "testGenerator")
    @Entity
    class AnnotatedClass {

    }
    
    @TableGenerator(name = "generatorName")
    class NonEntityAnnotatedClass { }
    
    class NonAnnotatedClass { }
    
    @Entity
    class OtherwiseAnnotatedClass { }
}
