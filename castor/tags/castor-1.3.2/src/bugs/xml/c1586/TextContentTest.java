package xml.c1586;

import junit.framework.TestCase;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.XMLContext;

import org.xml.sax.InputSource;

import java.io.StringWriter;

/**
 * Unit Test
 */
public class TextContentTest extends TestCase {
    private static final String MAPPING_FILE = "mapping.xml";
    
    private XMLContext xmlContext;
    private Mapping mapping;
    private Marshaller marshaller;
    private StringWriter writer;
    private Unmarshaller unmarshaller;    

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.xmlContext = new XMLContext();
        // das Test Objekt vorbereiten
        this.writer = new StringWriter();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        this.writer = null;
        this.xmlContext = null;
    }

    /**
     * Will throw exception when setting mapping in unmarshaller 
     */         
    public void testTextContentNotWorking() throws Exception {
         // das Mapping laden
        mapping = new Mapping(getClass().getClassLoader());
        mapping.loadMapping(new InputSource(getClass().getResourceAsStream(MAPPING_FILE)));

        // den Marshaller erzeugen
        marshaller = xmlContext.createMarshaller();
        marshaller.setWriter(writer);
//        marshaller = new Marshaller(writer);
        marshaller.setMapping(mapping);
        marshaller.setEncoding("UTF-8");
        marshaller.setValidation(true);
        
        // den Unmarshaller erzeugen
        unmarshaller = xmlContext.createUnmarshaller();
//        unmarshaller = new Unmarshaller();
        unmarshaller.setClassLoader(getClass().getClassLoader());
        unmarshaller.setValidation(false);
        unmarshaller.setMapping(mapping);
        unmarshaller.setWhitespacePreserve(true);

        // den Marshaller erzeugen
        marshaller = xmlContext.createMarshaller();
        marshaller.setWriter(writer);
//        marshaller = new Marshaller(writer);
        marshaller.setMapping(mapping);
        marshaller.setEncoding("UTF-8");
        marshaller.setValidation(true);
    }

    /**
     * Everything works
     */         
    public void testTextContentWorking() throws Exception {
        // das Mapping laden
        mapping = new Mapping(getClass().getClassLoader());
        mapping.loadMapping(getClass().getResource(MAPPING_FILE));

        // den Marshaller erzeugen
        marshaller = xmlContext.createMarshaller();
        marshaller.setWriter(writer);
//        marshaller = new Marshaller(writer);
        marshaller.setMapping(mapping);
        marshaller.setEncoding("UTF-8");
        marshaller.setValidation(true);

        // den Unmarshaller erzeugen
        unmarshaller = xmlContext.createUnmarshaller();
//        unmarshaller = new Unmarshaller();
        unmarshaller.setClassLoader(getClass().getClassLoader());
        unmarshaller.setValidation(false);
        unmarshaller.setMapping(mapping);
        unmarshaller.setWhitespacePreserve(true);
    }
}
