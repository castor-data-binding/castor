package xml.template;

import junit.framework.TestCase;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

public final class TestTemplate extends TestCase {
    
    private static final String SAMPLE_FILE = "entity.xml";
    private static final String MAPPING_FILE = "mapping.xml";
    
    public TestTemplate() {
        super();
    }
    
    public TestTemplate(final String name) {
        super(name);
    }

    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testQueryEntityOne() throws Exception {
        Mapping mapping = new Mapping();
        mapping.loadMapping(getClass().getResource(MAPPING_FILE).toExternalForm());
        
        Unmarshaller unmarshaller = new Unmarshaller (Entity.class);
        unmarshaller.setMapping(mapping);
        
        Entity entity = (Entity) unmarshaller.unmarshal(new InputSource(getClass().getResource(SAMPLE_FILE).toExternalForm()));
        assertNotNull (entity);
        assertEquals (1, entity.getId().intValue());
        assertEquals("name", entity.getName());
    }
}
