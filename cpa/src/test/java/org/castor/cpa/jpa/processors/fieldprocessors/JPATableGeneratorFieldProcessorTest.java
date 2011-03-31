package org.castor.cpa.jpa.processors.fieldprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

import org.castor.core.nature.PropertyHolder;
import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.FieldInfo;
import org.castor.cpa.jpa.info.JPAKeyGeneratorManager;
import org.castor.cpa.jpa.info.JPATableGeneratorDescriptor;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class JPATableGeneratorFieldProcessorTest {
    private JPATableGeneratorFieldProcessor _processor;
    private JPAFieldNature _nature;
    @Mock
    private TableGenerator _annotation;
    
    @SuppressWarnings("unused")
    private Long _id;
    
    @SuppressWarnings("unused")
    private void setter(final Long value) { }
    
    @TableGenerator(name = "generatorName")
    @Id
    @SuppressWarnings("unused")
    private Long tableGeneratorAnnotatedGetter() {
        return 0L;
    }
    
    @TableGenerator(name = "generatorName")
    @SuppressWarnings("unused")
    private Long nonIdAnnotatedGetter() {
        return 0L;
    }
    
    @SuppressWarnings("unused")
    private Long nonAnnotatedGetter() {
        return 0L;
    }
    
    @Id
    @SuppressWarnings("unused")
    private Long otherwiseAnnotatedGetter() {
        return 0L;
    }
    
    @TableGenerator(name = "generatorName")
    @SuppressWarnings("unused")
    private Long tableGeneratorAnnotatedGetterNotAnnotatedWithId() {
        return 0L;
    }
    
    @Before
    public void setUp() throws Exception {
        _processor = new JPATableGeneratorFieldProcessor();
        MockitoAnnotations.initMocks(this);
        initNature();
        when(_annotation.name()).thenReturn("name");
        when(_annotation.uniqueConstraints()).thenReturn(new UniqueConstraint[]{});
    }

    private void initNature() throws Exception {
        ClassInfo classInfo = new ClassInfo(this.getClass());

        Method getter = this.getClass().getDeclaredMethod("tableGeneratorAnnotatedGetter");
        Method setter = this.getClass().getDeclaredMethod("setter", Long.class);
        PropertyHolder holder = new FieldInfo(classInfo, Long.class, "id", getter, setter);
        holder.addNature(JPAFieldNature.class.getCanonicalName());
        _nature = new JPAFieldNature(holder);
    }
    
    @Test
    public void processorIsForTableGeneratorAnnotation() throws Exception {
        assertEquals(TableGenerator.class, _processor.forAnnotationClass());
    }
    
    private boolean processAnnotationOnMethod(final String methodName) throws Exception {
        Method method = this.getClass().getDeclaredMethod(methodName);
        boolean result = _processor.processAnnotation(_nature, _annotation, method);
        
        return result;
    }
    
    @Ignore
    @Test
    public void processorReturnsTrueForTableGeneratorAnnotatedGetter() throws Exception {
        boolean result = processAnnotationOnMethod("tableGeneratorAnnotatedGetter");
        assertTrue(result);
    }

    @Test
    public void processorReturnsFalseForTableGeneratorAnnotatedGetterNotAnnotatedWithId()
    throws Exception {
        boolean result = processAnnotationOnMethod(
                "tableGeneratorAnnotatedGetterNotAnnotatedWithId");
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
        
        _processor.processAnnotation(_nature, _annotation, method);
        
        JPAKeyGeneratorManager manager = JPAKeyGeneratorManager.getInstance();
        JPATableGeneratorDescriptor tableGenerator =
            (JPATableGeneratorDescriptor) manager.get("name");
        
        assertEquals("name", tableGenerator.getName());
    }
}
