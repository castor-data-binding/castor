package org.castor.cpa.jpa.processors.fieldprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.info.FieldInfo;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class JPAEnumeratedProcessorTest {
    private JPAEnumeratedProcessor _processor;
    private JPAFieldNature _fieldNature;
    @Mock
    private Enumerated _annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        _processor = new JPAEnumeratedProcessor();
        ClassInfo classInfo = new ClassInfo();
        classInfo.addNature(JPAFieldNature.class.getCanonicalName());
        _fieldNature = new JPAFieldNature(classInfo);
    }

    @Test
    public void processorIsForEnumeratedAnnotation() throws Exception {
        assertEquals(Enumerated.class, _processor.forAnnotationClass());
    }

    @Test
    public void processorReturnsTrueForEnumeratedAnnotatedClassCorrectly()
            throws Exception {
        boolean result = _processor.processAnnotation(_fieldNature, _annotation,
                EnumeratedAnnotatedClass.class.getDeclaredMethod("getEnumerated"));
        assertTrue(result);
    }

    @Test
    public void processorReturnsFalseForNonAnnotatedClass() throws Exception {
        boolean result = _processor.processAnnotation(_fieldNature, _annotation,
                NonAnnotatedClass.class.getDeclaredMethod("getEnumerated"));
        assertFalse(result);
    }

    @Test
    public void processorReturnsFalseForOtherwiseAnnotatedClass()
            throws Exception {
        boolean result = _processor.processAnnotation(_fieldNature, _annotation,
                OtherwiseAnnotatedClass.class.getDeclaredMethod("getEnumerated"));
        assertFalse(result);
    }

    @Test
    public void annotationValuesAreProcessedCorrectly() throws Exception {
        ClassInfo classInfo = ClassInfoBuilder
                .buildClassInfo(EnumeratedAnnotatedClass.class);
        assertNotNull(classInfo);
        FieldInfo fieldInfo = classInfo.getFieldInfoByName("enumerated");
        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));
        JPAFieldNature jpaFieldNature = new JPAFieldNature(fieldInfo);
        assertTrue(jpaFieldNature.isStringEnumType());
    }

    // Test classes

    @Ignore
    private enum EnumeratedType {
        FOO, BAR, BAZ
    }

    @Ignore
    @Entity
    private class EnumeratedAnnotatedClass {
        private EnumeratedType _enumerated;

        @Enumerated(EnumType.STRING)
        public EnumeratedType getEnumerated() {
            return _enumerated;
        }

        public void setEnumerated(final EnumeratedType enumerated) {
            _enumerated = enumerated;
        }
    }

    @Ignore
    private class NonAnnotatedClass {
        private EnumeratedType _enumerated;

        public EnumeratedType getEnumerated() {
            return _enumerated;
        }

        public void setEnumerated(final EnumeratedType enumerated) {
            _enumerated = enumerated;
        }
    }

    @Ignore
    private class OtherwiseAnnotatedClass {
        private EnumeratedType _enumerated;

        @Column
        public EnumeratedType getEnumerated() {
            return _enumerated;
        }

        public void setEnumerated(final EnumeratedType enumerated) {
            _enumerated = enumerated;
        }
    }

}
