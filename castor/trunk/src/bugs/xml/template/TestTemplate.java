package xml.template;

import java.io.FileWriter;
import java.io.PrintWriter;

import junit.framework.TestCase;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

public final class TestTemplate extends TestCase {
    
    private static final String OUTPUT_FILE = "out.xml";
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
    public void testUnmarshalEntity() throws Exception {
        Mapping mapping = new Mapping();
        mapping.loadMapping(getClass().getResource(MAPPING_FILE).toExternalForm());
        
        Unmarshaller unmarshaller = new Unmarshaller (Entity.class);
        unmarshaller.setMapping(mapping);
        
        Entity entity = (Entity) unmarshaller.unmarshal(new InputSource(getClass().getResource(SAMPLE_FILE).toExternalForm()));
        assertNotNull (entity);
        assertEquals (1, entity.getId().intValue());
        assertEquals("name", entity.getName());
    }
    
    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testMarshalEntity() throws Exception {
        Mapping mapping = new Mapping();
        mapping.loadMapping(getClass().getResource(MAPPING_FILE).toExternalForm());
        
        PrintWriter out = new PrintWriter(new FileWriter(OUTPUT_FILE));
        Marshaller marshaller = new Marshaller (out);
        marshaller.setMapping(mapping);
        
        Entity entity = new Entity();
        entity.setId(new Integer(100));
        entity.setName("entity 100");
        marshaller.marshal(entity);
    }
    
}
