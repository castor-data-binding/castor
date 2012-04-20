package org.castor.cpa.jpa.processors.classprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.JPAKeyGeneratorManager;
import org.castor.cpa.jpa.info.JPASequenceGeneratorDescriptor;
import org.castor.cpa.jpa.natures.JPAClassNature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public final class JPASequenceGeneratorClassProcessorTest {
    private JPASequenceGeneratorClassProcessor _processor;
    private JPAClassNature _nature;
    private SequenceGenerator _annotation;
    private JPAKeyGeneratorManager _manager = JPAKeyGeneratorManager.getInstance();
    private String _generatorName = "testGenerator";
    private int _initialValue = 1;
    private int _allocationSize = 1;
    private String _sequenceName = "testSequence";

    @Before
    public void setUp() throws Exception {
        _processor = new JPASequenceGeneratorClassProcessor();
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
        classInfo.addNature(JPAClassNature.class.getCanonicalName());
        _nature = new JPAClassNature(classInfo);
    }

    @After
    public void tearDown() throws Exception {
        _manager.reset();
    }

    @Test
    public void processorIsForSequenceGeneratorAnnotation() throws Exception {
        assertEquals(SequenceGenerator.class, _processor.forAnnotationClass());
    }

    @Test
    public void processorReturnsTrueForSequenceGeneratorAnnotatedClass()
            throws Exception {
        boolean result = _processor.processAnnotation(_nature, _annotation,
                AnnotatedClass.class);
        assertTrue(result);
    }

    @Test
    public void nonAnnotatedClassResultsInFalseProcessingResult()
            throws Exception {
        boolean result = _processor.processAnnotation(_nature, _annotation,
                NonAnnotatedClass.class);
        assertFalse(result);
    }

    @Test
    public void otherwiseAnnotatedClassResultsInFalseProcessingResult()
            throws Exception {
        boolean result = _processor.processAnnotation(_nature, _annotation,
                OtherwiseAnnotatedClass.class);
        assertFalse(result);
    }

    @Test
    public void nonEntityAnnotatedClassResultsInFalseProcessingResult()
            throws Exception {
        boolean result = _processor.processAnnotation(_nature, _annotation,
                NonEntityAnnotatedClass.class);
        assertFalse(result);
    }

    @Test
    public void processedSequenceGeneratorWillBeManaged() throws Exception {

        _processor.processAnnotation(_nature, _annotation, AnnotatedClass.class);

        JPASequenceGeneratorDescriptor actualDescriptor = (JPASequenceGeneratorDescriptor) _manager
                .get(_generatorName);
        assertEquals("testGenerator", actualDescriptor.getName());
    }

    @SequenceGenerator(name = "testGenerator", allocationSize = 1,
            initialValue = 1, sequenceName = "sequenceName")
    @Entity
    class AnnotatedClass { }

    @SequenceGenerator(name = "generatorName")
    class NonEntityAnnotatedClass { }

    class NonAnnotatedClass { }

    @Deprecated
    class OtherwiseAnnotatedClass { }
}
