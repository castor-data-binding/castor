package org.castor.cpa.jpa.processors.fieldprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.castor.core.nature.PropertyHolder;
import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.FieldInfo;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public final class JPAGeneratedValueProcessorTest {
    private JPAGeneratedValueProcessor _processor;
    private JPAFieldNature _nature;
    private GeneratedValue _annotation;
    
    @Before
    public void setUp() throws Exception {
        _processor = new JPAGeneratedValueProcessor();
        MockitoAnnotations.initMocks(this);
        initNature();
        _annotation = new GeneratedValue() {
            
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
        _nature = new JPAFieldNature(holder);
    }
    
    @Test
    public void processorIsForSequenceGeneratorAnnotation() throws Exception {
        assertEquals(GeneratedValue.class, _processor.forAnnotationClass());
    }
    
    private boolean processAnnotationOnMethod(final String methodName) throws Exception {
        Method method = this.getClass().getDeclaredMethod(methodName);
        boolean result = _processor.processAnnotation(_nature, _annotation, method);
        
        return result;
    }
    
    @Test
    public void processorReturnsTrueForGeneratedValueAnnotatedGetter() throws Exception {
        boolean result = processAnnotationOnMethod("generatedValueAnnotatedGetter");
        assertTrue(result);
    }

    @Test
    public void processorReturnsFalseForGeneratedValueAnnotatedGetterNotAnnotatedWithId()
    throws Exception {
        boolean result = processAnnotationOnMethod(
                "generatedValueAnnotatedGetterNotAnnotatedWithId");
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
        _processor.processAnnotation(_nature, _annotation, method); 
        assertEquals(GenerationType.AUTO, _nature.getGeneratedValueStrategy());
    }

    @Test
    public void generatorNameWillBeSetInNature() throws Exception { 
        Method method = this.getClass().getDeclaredMethod("generatedValueAnnotatedGetter");     
        _processor.processAnnotation(_nature, _annotation, method); 
        assertEquals("generator", _nature.getGeneratedValueGenerator());
    }
    
    @SuppressWarnings("unused")
    private Long _id;
    
    @SuppressWarnings("unused")
    private void setter(final Long value) {
        
    }
    
    @GeneratedValue()
    @Id
    @SuppressWarnings("unused")
    private Long generatedValueAnnotatedGetter() {
        return 0L;
    }
    
    @GeneratedValue()
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
    
    @GeneratedValue()
    @SuppressWarnings("unused")
    private Long generatedValueAnnotatedGetterNotAnnotatedWithId() {
        return 0L;
    }
}
