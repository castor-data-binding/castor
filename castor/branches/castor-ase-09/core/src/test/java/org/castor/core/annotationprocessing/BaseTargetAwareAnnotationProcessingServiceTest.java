/*
 * Copyright 2009 Werner Guttmann, Peter Eibner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.core.annotationprocessing;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.castor.core.nature.BaseNature;
import org.castor.core.nature.PropertyHolder;

public class BaseTargetAwareAnnotationProcessingServiceTest extends TestCase {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface SupportedAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface UnsupportedAnnotation {
    }

    /**
     * This is a {@link TargetAwareAnnotationProcessor} that actually does nothing, except
     * telling that it processed the given Annotation if it was of Type
     * {@link SupportedAnnotation}. It's just a dummy.
     */
    class SupportedClassAnnotationProcessor implements TargetAwareAnnotationProcessor {

        public Class<? extends Annotation> forAnnotationClass() {
            return SupportedAnnotation.class;
        }

        public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
                I info, A annotation) {
            if (annotation instanceof SupportedAnnotation) {
                // do
                return true;
            }
            return false;
        }

        public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
                I info, A annotation, AnnotatedElement target) {
            if ((annotation instanceof SupportedAnnotation)&&(target instanceof Class)) {
                // do
                return true;
            }
            return false;
        }
    }

    /**
     * This is a {@link TargetAwareAnnotationProcessor} that actually does nothing, except
     * telling that it processed the given Annotation if it was of Type
     * {@link SupportedAnnotation}. It's just a dummy.
     */
    class SupportedMethodAnnotationProcessor implements TargetAwareAnnotationProcessor {

        public Class<? extends Annotation> forAnnotationClass() {
            return SupportedAnnotation.class;
        }

        public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
                I info, A annotation) {
            if (annotation instanceof SupportedAnnotation) {
                // do
                return true;
            }
            return false;
        }

        public <I extends BaseNature, A extends Annotation> boolean processAnnotation(
                I info, A annotation, AnnotatedElement target) {
            if ((annotation instanceof SupportedAnnotation) && (target instanceof Method)) {
                // do
                return true;
            }
            return false;
        }
    }

    /**
     * A simple empty class, that just has 2 Annotations - for one (
     * {@link SupportedAnnotation}) we have a dummy processor, for the other one
     * ( {@link UnsupportedAnnotation}) we don't have a processor.
     * 
     */
    @SupportedAnnotation
    @UnsupportedAnnotation
    class AnnotationHolder {

        @SupportedAnnotation
        @UnsupportedAnnotation
        public void annotatedMethod(){
            int i = 0;
            i++;
        }
    }

    /**
     * A dummy property holder needed for use with {@link BaseNature}s. In real
     * usage within Castor, this would be a ClassInfo or FieldInfo.
     * 
     */
    class MyPropertyHolder implements PropertyHolder {

        public Object getProperty(String arg0) {
            return null;
        }

        public void setProperty(String arg0, Object arg1) {
        }

        public void addNature(String arg0) {
        }

        public boolean hasNature(String arg0) {
            return true;
        }

    }

    @Override
    protected void setUp() throws Exception {
        assertTrue(AnnotationHolder.class
                .isAnnotationPresent(SupportedAnnotation.class));
        assertTrue(AnnotationHolder.class
                .isAnnotationPresent(UnsupportedAnnotation.class));
        assertTrue(AnnotationHolder.class.getMethod("annotatedMethod",
                new Class<?>[0]).isAnnotationPresent(SupportedAnnotation.class));
        assertTrue(AnnotationHolder.class.getMethod("annotatedMethod",
                new Class<?>[0]).isAnnotationPresent(UnsupportedAnnotation.class));
    }

    public void testAddGetAnnotationProcessor() {
        BaseTargetAwareAnnotationProcessingService baps = new BaseTargetAwareAnnotationProcessingService();
        SupportedClassAnnotationProcessor annotationProcessor = new SupportedClassAnnotationProcessor();

        assertNotNull(baps.getAnnotationProcessors());
        assertFalse(baps.getAnnotationProcessors()
                .contains(annotationProcessor));

        baps.addAnnotationProcessor(annotationProcessor);

        assertNotNull(baps.getAnnotationProcessors());
        assertFalse(baps.getAnnotationProcessors().contains(annotationProcessor));
        assertTrue(baps.getAllAnnotationProcessors().contains(annotationProcessor));
        assertTrue(baps.getTargetAwareAnnotationProcessors().contains(annotationProcessor));
    }

    public void testProcessAnnotations() {
        BaseTargetAwareAnnotationProcessingService baps = new BaseTargetAwareAnnotationProcessingService();
        TargetAwareAnnotationProcessor annotationProcessor = new SupportedClassAnnotationProcessor();
        baps.addAnnotationProcessor(annotationProcessor);
        Annotation[] annotations = AnnotationHolder.class.getAnnotations();
        assertEquals(2, annotations.length);
        /*
         * now annotations[] contains @SupportedAnnotation and
         * @UnsupportedAnnotation
         */

        MyPropertyHolder holder = new MyPropertyHolder();
        holder.addNature(BaseNature.class.getName());

        BaseNature info = new BaseNature(holder) {
            public String getId() {
                return BaseNature.class.getName();
            }
        };

        /*
         * try to process all annotations
         */
        Annotation[] unprocessed = baps.processAnnotations(info, annotations);

        /*
         * we know, that we don't have processor for @UnsupportedAnnotation, so
         * 
         * @UnsupportedAnnotation is put into unprocessed[]
         */
        assertNotNull(unprocessed);
        assertEquals(1, unprocessed.length);
        assertEquals(UnsupportedAnnotation.class, unprocessed[0]
                .annotationType());
    }

    public void testProcessAnnotationsTargetAware() throws SecurityException, NoSuchMethodException, AnnotationTargetException {
        BaseTargetAwareAnnotationProcessingService baps = new BaseTargetAwareAnnotationProcessingService();
        TargetAwareAnnotationProcessor annotationProcessor = new SupportedMethodAnnotationProcessor();
        baps.addAnnotationProcessor(annotationProcessor);
        Annotation[] annotations = AnnotationHolder.class.getAnnotations();
        assertEquals(2, annotations.length);
        /*
         * now annotations[] contains @SupportedAnnotation and
         * @UnsupportedAnnotation
         */

        MyPropertyHolder holder = new MyPropertyHolder();
        holder.addNature(BaseNature.class.getName());

        BaseNature info = new BaseNature(holder) {
            public String getId() {
                return BaseNature.class.getName();
            }
        };

        /*
         * try to process all annotations (the Method-processor added can't process them)
         */
        Annotation[] unprocessed = baps.processAnnotations(info, annotations, AnnotationHolder.class);

        assertNotNull(unprocessed);
        assertEquals(2, unprocessed.length);
        
        ArrayList<Class<? extends Annotation>> unprocessedList = new ArrayList<Class<? extends Annotation>>();
        for (Annotation anno : unprocessed) {
            unprocessedList.add(anno.annotationType());
        }
        
        assertTrue(unprocessedList.contains(SupportedAnnotation.class));
        assertTrue(unprocessedList.contains(UnsupportedAnnotation.class));
        
        /*
         * instead we try to process the methods annotations - they should work with our setup.
         */
        Method method = AnnotationHolder.class.getMethod("annotatedMethod", new Class<?>[0]);
        Annotation[] methodAnnotations = method.getAnnotations();
        unprocessed = baps.processAnnotations(info, methodAnnotations, method);

        assertNotNull(unprocessed);
        assertEquals(1, unprocessed.length);
        assertEquals(UnsupportedAnnotation.class, unprocessed[0].annotationType());
        
        /*
         * add the processor for Class annotations and try again
         */
        
        annotationProcessor = new SupportedClassAnnotationProcessor();
        baps.addAnnotationProcessor(annotationProcessor);
        unprocessed = baps.processAnnotations(info, annotations, AnnotationHolder.class);
        
        /*
         * we know, that we don't have processor for @UnsupportedAnnotation, so
         * 
         * @UnsupportedAnnotation is put into unprocessed[]
         */
        assertNotNull(unprocessed);
        assertEquals(1, unprocessed.length);
        assertEquals(UnsupportedAnnotation.class, unprocessed[0]
                .annotationType());
    }

    
    public void testProcessAnnotation() {
        BaseAnnotationProcessingService baps = new BaseAnnotationProcessingService();
        baps.addAnnotationProcessor(new SupportedClassAnnotationProcessor());

        MyPropertyHolder holder = new MyPropertyHolder();
        holder.addNature(BaseNature.class.getName());
        BaseNature info = new BaseNature(holder) {
            public String getId() {
                return BaseNature.class.getName();
            }
        };

        /*
         * our AnnotationHolder has 2 Annotations: @UnsupportedAnnotation and
         * 
         * @SupportedAnnotation, so we get them
         */
        Annotation unsupportedAnnotation = AnnotationHolder.class
                .getAnnotation(UnsupportedAnnotation.class);
        Annotation supportedAnnotation = AnnotationHolder.class
                .getAnnotation(SupportedAnnotation.class);
        assertNotNull(unsupportedAnnotation);
        assertNotNull(supportedAnnotation);

        /*
         * we know, that we don't have processor for @UnsupportedAnnotation, so
         * processing it will return false.
         */
        assertFalse(baps.processAnnotation(info, unsupportedAnnotation));
        assertTrue(baps.processAnnotation(info, supportedAnnotation));

    }

}
