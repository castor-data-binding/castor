package org.castor.cpa.jpa.info;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.castor.core.annotationprocessing.AnnotationProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAColumnProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAGeneratedValueProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAIdProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAJoinColumnProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAJoinTableProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAManyToManyProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAManyToOneProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAOneToManyProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPAOneToOneProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPASequenceGeneratorFieldProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPATableGeneratorFieldProcessor;
import org.castor.cpa.jpa.processors.fieldprocessors.JPATransientProcessor;
import org.junit.Before;
import org.junit.Test;

public final class JPAFieldAnnotationProcessingServiceTest {
    private JPAFieldAnnotationProcessingService _processingService;

    @Before
    public void setUp() throws Exception {
        _processingService = new JPAFieldAnnotationProcessingService();
    }

    @Test
    public void basicProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPASequenceGeneratorFieldProcessor.class);
    }
    
    @Test
    public void columnProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPAColumnProcessor.class);
    }
    
    @Test
    public void idProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPAIdProcessor.class);
    }
    
    @Test
    public void joinColumnProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPAJoinColumnProcessor.class);
    }
    
    @Test
    public void joinTableProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPAJoinTableProcessor.class);
    }
    
    @Test
    public void manyToManyProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPAManyToManyProcessor.class);
    }
    
    @Test
    public void oneToManyProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPAOneToManyProcessor.class);
    }
    
    @Test
    public void manyToOneProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPAManyToOneProcessor.class);
    }
    
    @Test
    public void oneToOneProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPAOneToOneProcessor.class);
    }
    
    @Test
    public void transientProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPATransientProcessor.class);
    }

    @Test
    public void sequenceGeneratorProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPASequenceGeneratorFieldProcessor.class);
    }
    
    @Test
    public void generatedValueProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPAGeneratedValueProcessor.class);
    }
    
    @Test
    public void tableGeneratorProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPATableGeneratorFieldProcessor.class);
    }
    
    public void assertThatProcessorTypeIsRegisteredWithService(
            final Class<? extends AnnotationProcessor> processorType) throws Exception {
        boolean containsProcessor = false;
        Set<AnnotationProcessor> processorSet = _processingService
                .getAllAnnotationProcessors();
        for (AnnotationProcessor processor : processorSet) {
            if (processor.getClass() == processorType) {
                containsProcessor = true;
            }
        }
        assertTrue(containsProcessor);
    }
}
