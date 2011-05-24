package org.castor.cpa.jpa.processors.fieldprocessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import javax.persistence.Id;
import javax.persistence.Version;

import org.castor.core.nature.PropertyHolder;
import org.castor.cpa.jpa.info.ClassInfo;
import org.castor.cpa.jpa.info.FieldInfo;
import org.castor.cpa.jpa.info.JPAKeyGeneratorManager;
import org.castor.cpa.jpa.info.JPAVersionManager;
import org.castor.cpa.jpa.natures.JPAFieldNature;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAVersionProcessor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class JPAVersionProcessorTest {

    JPAVersionProcessor processor;
    JPAFieldNature nature;
    Version annotation;
    @Mock
    AnnotatedElement target;
    JPAKeyGeneratorManager manager = JPAKeyGeneratorManager.getInstance();

    @Before
    public void setUp() throws Exception {
        processor = new JPAVersionProcessor();
        MockitoAnnotations.initMocks(this);
        initNature();
        annotation = new Version() {

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
        nature = new JPAFieldNature(holder);
    }

    @Test
    public void processorIsForVersionGeneratorAnnotation() throws Exception {
        assertEquals(Version.class, processor.forAnnotationClass());
    }

    private boolean processAnnotationOnMethod(String methodName)
            throws Exception {
        Method method = this.getClass().getDeclaredMethod(methodName);
        boolean result = processor
                .processAnnotation(nature, annotation, method);

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
    private Long id;

    @SuppressWarnings("unused")
    private void setter(Long value) {

    }

    @Version
    @SuppressWarnings("unused")
    private Long getVersion() {
        return 0l;
    }

    @SuppressWarnings("unused")
    private Long nonAnnotatedGetter() {
        return 0l;
    }

    @Id
    @SuppressWarnings("unused")
    private Long otherwiseAnnotatedGetter() {
        return 0l;
    }
}
