package xml.template;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.XMLContext;
import org.xml.sax.InputSource;

public final class TestTemplate extends TestCase {
    
    private static final String SAMPLE_FILE = "input.xml";
    private XMLContext context;
    
    public TestTemplate() {
        super();
    }
    
    public TestTemplate(final String name) {
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

        Unmarshaller unmarshaller = getXMLContext().createUnmarshaller();
        unmarshaller.setClass(Entity.class);
        
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

        StringWriter out = new StringWriter();
        Marshaller marshaller = getXMLContext().createMarshaller();
        marshaller.setWriter(out);
        
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
