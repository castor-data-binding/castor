package org.castor.cpa.jpa.processors.fieldprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.ClassInfoBuilder;
import org.castor.cpa.jpa.info.FieldInfo;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class JPALobProcessorTest {
    private JPALobProcessor _processor;
    private JPAFieldNature _fieldNature;
    @Mock
    private Lob _annotation;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        _processor = new JPALobProcessor();
        ClassInfo classInfo = new ClassInfo();
        classInfo.addNature(JPAFieldNature.class.getCanonicalName());
        _fieldNature = new JPAFieldNature(classInfo);
    }

    @Test
    public void processorIsForLobAnnotation() throws Exception {
        assertEquals(Lob.class, _processor.forAnnotationClass());
    }

    @Test
    public void processorReturnsTrueForLobAnnotatedClassCorrectly()
            throws Exception {
        boolean result = _processor.processAnnotation(_fieldNature, _annotation,
                LobAnnotatedClass.class.getDeclaredMethod("getLob"));
        assertTrue(result);
    }

    @Test
    public void processorReturnsFalseForNonAnnotatedClass() throws Exception {
        boolean result = _processor.processAnnotation(_fieldNature, _annotation,
                NonAnnotatedClass.class.getDeclaredMethod("getLob"));
        assertFalse(result);
    }

    @Test
    public void processorReturnsFalseForOtherwiseAnnotatedClass()
            throws Exception {
        boolean result = _processor.processAnnotation(_fieldNature, _annotation,
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
        private String _lob;

        @Lob
        public String getLob() {
            return _lob;
        }

        public void setLob(final String lob) {
            _lob = lob;
        }
    }

    @Ignore
    private class NonAnnotatedClass {
        private String _lob;

        public String getLob() {
            return _lob;
        }

        public void setLob(final String lob) {
            _lob = lob;
        }
    }

    @Ignore
    private class OtherwiseAnnotatedClass {
        private String _lob;

        @Column
        public String getLob() {
            return _lob;
        }

        public void setLob(final String lob) {
            _lob = lob;
        }
    }
}
