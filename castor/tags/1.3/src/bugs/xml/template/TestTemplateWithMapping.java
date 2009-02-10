package xml.template;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.XMLContext;
import org.xml.sax.InputSource;

public final class TestTemplateWithMapping extends TestCase {
    
    private static final String SAMPLE_FILE = "input.xml";
    private static final String MAPPING_FILE = "mapping.xml";

    private XMLContext context;

    public TestTemplateWithMapping() {
        super();
    }
    
    public TestTemplateWithMapping(final String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        this.context = new XMLContext();
        context.setProperty("org.exolab.castor.indent", "true");
    }

    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testUnmarshalEntity() throws Exception {
        Mapping mapping = new Mapping();
        mapping.loadMapping(getClass().getResource(MAPPING_FILE).toExternalForm());
        
        Unmarshaller unmarshaller = getXMLContext().createUnmarshaller();
        unmarshaller.setClass(Entity.class);
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
        
        StringWriter out = new StringWriter();
        Marshaller marshaller = getXMLContext().createMarshaller();
        marshaller.setWriter(out);
        marshaller.setMapping(mapping);
        
        Entity entity = new Entity();
        entity.setId(new Integer(100));
        entity.setName("entity 100");
        
        marshaller.marshal(entity);
        
        System.out.println(out.toString());
    }
    
    private XMLContext getXMLContext() {
        return this.context;
    }
    
}
