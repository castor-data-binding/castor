package org.castor.cpa.jpa.processors.fieldprocessors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.persistence.Id;
import javax.persistence.Version;

import org.castor.core.nature.PropertyHolder;
import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.FieldInfo;
import org.castor.cpa.jpa.info.JPAVersionManager;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public final class JPAVersionProcessorTest {
    private JPAVersionProcessor _processor;
    private JPAFieldNature _nature;
    private Version _annotation;

    @Before
    public void setUp() throws Exception {
        _processor = new JPAVersionProcessor();
        MockitoAnnotations.initMocks(this);
        initNature();
        _annotation = new Version() {

//            @Override
            public Class<? extends Annotation> annotationType() {
                return Version.class;
            }
        };
    }

    private void initNature() throws Exception {
        ClassInfo classInfo = new ClassInfo(this.getClass());

        Method getter = this.getClass().getDeclaredMethod("getVersion");
        Method setter = this.getClass().getDeclaredMethod("setter", Long.class);
        PropertyHolder holder = new FieldInfo(classInfo, Long.class, "id",
                getter, setter);
        holder.addNature(JPAFieldNature.class.getCanonicalName());
        _nature = new JPAFieldNature(holder);
    }

    @Test
    public void processorIsForVersionGeneratorAnnotation() throws Exception {
        assertEquals(Version.class, _processor.forAnnotationClass());
    }

    private boolean processAnnotationOnMethod(final String methodName) throws Exception {
        Method method = this.getClass().getDeclaredMethod(methodName);
        boolean result = _processor.processAnnotation(_nature, _annotation, method);

        return result;
    }

    @Test
    public void processorReturnsTrueForGeneratedValueAnnotatedGetter()
            throws Exception {
        boolean result = processAnnotationOnMethod("getVersion");
        assertTrue(result);
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
    public void processorAddsClassWithVersionFieldName() throws Exception {
        processAnnotationOnMethod("getVersion");
        JPAVersionManager manager = JPAVersionManager.getInstance();
        assertEquals("version", manager.get(this.getClass()));
    }

    @SuppressWarnings("unused")
    private Long _id;

    @SuppressWarnings("unused")
    private void setter(final Long value) { }

    @Version
    @SuppressWarnings("unused")
    private Long getVersion() {
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
}
