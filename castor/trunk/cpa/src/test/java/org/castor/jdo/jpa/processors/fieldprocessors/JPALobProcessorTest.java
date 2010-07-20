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
import javax.persistence.Lob;

public class JPALobProcessorTest {

    JPALobProcessor processor;
    JPAFieldNature fieldNature;
    @Mock
    Lob annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        processor = new JPALobProcessor();
        ClassInfo classInfo = new ClassInfo();
        classInfo.addNature(JPAFieldNature.class.getCanonicalName());
        fieldNature = new JPAFieldNature(classInfo);
    }

    @Test
    public void processorIsForLobAnnotation() throws Exception {
        assertEquals(Lob.class, processor.forAnnotationClass());
    }

    @Test
    public void processorReturnsTrueForLobAnnotatedClassCorrectly()
            throws Exception {
        boolean result = processor.processAnnotation(fieldNature, annotation,
                LobAnnotatedClass.class.getDeclaredMethod("getLob"));
        assertTrue(result);
    }

    @Test
    public void processorReturnsFalseForNonAnnotatedClass() throws Exception {
        boolean result = processor.processAnnotation(fieldNature, annotation,
                NonAnnotatedClass.class.getDeclaredMethod("getLob"));
        assertFalse(result);
    }

    @Test
    public void processorReturnsFalseForOtherwiseAnnotatedClass()
            throws Exception {
        boolean result = processor.processAnnotation(fieldNature, annotation,
                OtherwiseAnnotatedClass.class.getDeclaredMethod("getLob"));
        assertFalse(result);
    }

    @Test
    public void annotationValuesAreProcessedCorrectly() throws Exception {
        ClassInfo classInfo = ClassInfoBuilder
                .buildClassInfo(LobAnnotatedClass.class);
        assertNotNull(classInfo);
        FieldInfo fieldInfo = classInfo.getFieldInfoByName("lob");
        assertTrue(fieldInfo.hasNature(JPAFieldNature.class.getName()));
        JPAFieldNature jpaFieldNature = new JPAFieldNature(fieldInfo);
        assertTrue(jpaFieldNature.isLob());
    }

    // Test classes

    @Ignore
    @Entity
    private class LobAnnotatedClass {
        private String lob;

        @Lob
        public String getLob() {
            return lob;
        }

        public void setLob(final String lob) {
            this.lob = lob;
        }
    }

    @Ignore
    private class NonAnnotatedClass {
        private String lob;

        public String getLob() {
            return lob;
        }

        public void setLob(final String lob) {
            this.lob = lob;
        }
    }

    @Ignore
    private class OtherwiseAnnotatedClass {
        private String lob;

        @Column
        public String getLob() {
            return lob;
        }

        public void setLob(final String lob) {
            this.lob = lob;
        }
    }

}
