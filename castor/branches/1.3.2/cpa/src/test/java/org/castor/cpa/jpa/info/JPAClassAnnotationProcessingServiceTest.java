package org.castor.cpa.jpa.info;

import java.util.Set;

import org.castor.core.annotationprocessing.AnnotationProcessor;
import org.castor.cpa.jpa.info.JPAClassAnnotationProcessingService;
import org.castor.cpa.jpa.processors.classprocessors.JPACacheProcessor;
import org.castor.cpa.jpa.processors.classprocessors.JPAEntityProcessor;
import org.castor.cpa.jpa.processors.classprocessors.JPAInheritanceProcessor;
import org.castor.cpa.jpa.processors.classprocessors.JPANamedNativeQueriesProcessor;
import org.castor.cpa.jpa.processors.classprocessors.JPANamedNativeQueryProcessor;
import org.castor.cpa.jpa.processors.classprocessors.JPANamedQueryProcessor;
import org.castor.cpa.jpa.processors.classprocessors.JPASequenceGeneratorClassProcessor;
import org.castor.cpa.jpa.processors.classprocessors.JPATableGeneratorClassProcessor;
import org.castor.cpa.jpa.processors.classprocessors.JPATableProcessor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JPAClassAnnotationProcessingServiceTest {

    JPAClassAnnotationProcessingService processingService;

    @Before
    public void setUp() throws Exception {
        processingService = new JPAClassAnnotationProcessingService();
    }

    @Test
    public void entityProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPAEntityProcessor.class);
    }

    @Test
    public void tableProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPATableProcessor.class);
    }

    @Test
    public void namedQueryProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPANamedQueryProcessor.class);
    }

    @Test
    public void namedNativeQueryProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPANamedNativeQueryProcessor.class);
    }

    @Test
    public void namedNativeQueriesProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPANamedNativeQueriesProcessor.class);
    }

    @Test
    public void inheritanceProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPAInheritanceProcessor.class);
    }

    @Test
    public void cacheProcessorIsRegistered() throws Exception {
        assertThatProcessorTypeIsRegisteredWithService(JPACacheProcessor.class);
    }

	@Test
	public void sequenceGeneratorProcessorIsRegistered() throws Exception {
		assertThatProcessorTypeIsRegisteredWithService(JPASequenceGeneratorClassProcessor.class);
	}
	
	@Test
	public void tableGeneratorProcessorIsRegistered() throws Exception {
		assertThatProcessorTypeIsRegisteredWithService(JPATableGeneratorClassProcessor.class);
	}
    
    public void assertThatProcessorTypeIsRegisteredWithService(
            Class<? extends AnnotationProcessor> processorType)
            throws Exception {
        boolean containsProcessor = false;
        Set<AnnotationProcessor> processorSet = processingService
                .getAllAnnotationProcessors();
        for (AnnotationProcessor processor : processorSet) {
            if (processor.getClass() == processorType) {
                containsProcessor = true;
            }
        }
        assertTrue(containsProcessor);
    }
}
