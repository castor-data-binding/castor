package org.castor.jdo.jpa.processors.fieldprocessors;

import org.castor.jdo.jpa.info.ClassInfo;
import org.castor.jdo.jpa.info.ClassInfoBuilder;
import org.castor.jdo.jpa.info.FieldInfo;
import org.castor.jdo.jpa.natures.JPAFieldNature;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class JPAEnumeratedProcessorTest {

    JPAEnumeratedProcessor processor;
    JPAFieldNature fieldNature;
    @Mock
    Enumerated annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        processor = new JPAEnumeratedProcessor();
        ClassInfo classInfo = new ClassInfo();
        classInfo.addNature(JPAFieldNature.class.getCanonicalName());
        fieldNature = new JPAFieldNature(classInfo);
    }

    @Test
    public void processorIsForEnumeratedAnnotation() throws Exception {
        assertEquals(Enumerated.class, processor.forAnnotationClass());
    }

    @Test
    public void processorReturnsTrueForEnumeratedAnnotatedClassCorrectly()
            throws Exception {
        boolean result = processor.processAnnotation(fieldNature, annotation,
                EnumeratedAnnotatedClass.class.getDeclaredMethod("getEnumerated"));
        assertTrue(result);
    }

    @Test
    public void processorReturnsFalseForNonAnnotatedClass() throws Exception {
        boolean result = processor.processAnnotation(fieldNature, annotation,
                NonAnnotatedClass.class.getDeclaredMethod("getEnumerated"));
        assertFalse(result);
    }

    @Test
    public void processorReturnsFalseForOtherwiseAnnotatedClass()
            throws Exception {
        boolean result = processor.processAnnotation(fieldNature, annotation,
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
        private EnumeratedType enumerated;

        @Enumerated(EnumType.STRING)
        public EnumeratedType getEnumerated() {
            return enumerated;
        }

        public void setEnumerated(final EnumeratedType enumerated) {
            this.enumerated = enumerated;
        }
    }

    @Ignore
    private class NonAnnotatedClass {
        private EnumeratedType enumerated;

        public EnumeratedType getEnumerated() {
            return enumerated;
        }

        public void setEnumerated(final EnumeratedType enumerated) {
            this.enumerated = enumerated;
        }
    }

    @Ignore
    private class OtherwiseAnnotatedClass {
        private EnumeratedType enumerated;

        @Column
        public EnumeratedType getEnumerated() {
            return enumerated;
        }

        public void setEnumerated(final EnumeratedType enumerated) {
            this.enumerated = enumerated;
        }
    }

}
