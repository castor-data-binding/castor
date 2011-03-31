package org.castor.cpa.jpa.processors.fieldprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.castor.core.nature.PropertyHolder;
import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.FieldInfo;
import org.castor.cpa.jpa.info.JPAKeyGeneratorManager;
import org.castor.cpa.jpa.info.JPASequenceGeneratorDescriptor;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public final class JPASequenceGeneratorFieldProcessorTest {
    private JPASequenceGeneratorFieldProcessor _processor;
    private JPAFieldNature _nature;
    private SequenceGenerator _annotation;
    private JPAKeyGeneratorManager _manager = JPAKeyGeneratorManager.getInstance();
    private String _generatorName = "testGenerator";
    private int _initialValue = 1;
    private int _allocationSize = 1;
    private String _sequenceName = "testSequence";
    
    @SuppressWarnings("unused")
    private Long _id;
    
    @SuppressWarnings("unused")
    private void setter(final Long value) { }
    
    @SequenceGenerator(name = "generatorName")
    @Id
    @SuppressWarnings("unused")
    private Long sequenceGeneratorAnnotatedGetter() {
        return 0L;
    }
    
    @SequenceGenerator(name = "generatorName")
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
    
    @SequenceGenerator(name = "generatorName")
    @SuppressWarnings("unused")
    private Long sequenceGeneratorAnnotatedGetterNotAnnotatedWithId() {
        return 0L;
    }
    
    @Before
    public void setUp() throws Exception {
        _processor = new JPASequenceGeneratorFieldProcessor();
        MockitoAnnotations.initMocks(this);
        initNature();
        _annotation = new SequenceGenerator() {
            
            public Class<? extends Annotation> annotationType() {
                return SequenceGenerator.class;
            }
            
            public String sequenceName() {
                return _sequenceName;
            }
            
            public String name() {
                return _generatorName;
            }
            
            public int initialValue() {
                return _initialValue;
            }
            
            public int allocationSize() {
                return _allocationSize;
            }
        };      
    }

    private void initNature() throws Exception {
        ClassInfo classInfo = new ClassInfo(this.getClass());

        Method getter = this.getClass().getDeclaredMethod("sequenceGeneratorAnnotatedGetter");
        Method setter = this.getClass().getDeclaredMethod("setter", Long.class);
        PropertyHolder holder = new FieldInfo(classInfo, Long.class, "id", getter, setter);
        holder.addNature(JPAFieldNature.class.getCanonicalName());
        _nature = new JPAFieldNature(holder);
    }
    
    @Test
    public void processorIsForSequenceGeneratorAnnotation() throws Exception {
        assertEquals(SequenceGenerator.class, _processor.forAnnotationClass());
    }
    
    private boolean processAnnotationOnMethod(final String methodName) throws Exception {
        Method method = this.getClass().getDeclaredMethod(methodName);
        boolean result = _processor.processAnnotation(_nature, _annotation, method);
        
        return result;
    }
    
    @Ignore
    @Test
    public void processorReturnsTrueForSequenceGeneratorAnnotatedGetter() throws Exception {
        boolean result = processAnnotationOnMethod("sequenceGeneratorAnnotatedGetter");
        assertTrue(result);
    }

    @Test
    public void processorReturnsFalseForSequenceGeneratorAnnotatedGetterNotAnnotatedWithId()
    throws Exception {
        boolean result = processAnnotationOnMethod(
                "sequenceGeneratorAnnotatedGetterNotAnnotatedWithId");
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
        
        _processor.processAnnotation(_nature, _annotation, method);

        JPASequenceGeneratorDescriptor actualDescriptor = (JPASequenceGeneratorDescriptor) _manager
                .get(_generatorName);
        assertEquals(_generatorName, actualDescriptor.getName());
    }
}
