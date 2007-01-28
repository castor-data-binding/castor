import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

public class TestMarshalIDs {
    
    private static final Log LOG = LogFactory.getLog(TestMarshalIDs.class);
    
    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testMarshallingAfterManuallyNullingID() throws Exception {
        
        Unmarshaller unmarshaller = new Unmarshaller (PartialTerminationElement.class);
        
        PartialTerminationElement entity = (PartialTerminationElement) 
           unmarshaller.unmarshal(new InputSource(this.getClass().getResource("input-ok.xml").toExternalForm()));
        // assertNotNull (entity);
        
        entity.getPartyOneElement().setId("");
        
        StringWriter out = new StringWriter();
        Marshaller marshaller = new Marshaller (out);
        marshaller.setValidation(true);
        try {
            marshaller.marshal(entity);
            // fail ("ValidationException expected");
        }
        catch (ValidationException e) {
            // nothing to check
        }
        
    }

    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testMarshallingAfterManuallySettingDuplicateID() throws Exception {
        Unmarshaller unmarshaller = new Unmarshaller (PartialTerminationElement.class);
        
        PartialTerminationElement entity = (PartialTerminationElement) 
           unmarshaller.unmarshal(new InputSource(getClass().getResource("input-ok.xml").toExternalForm()));
        // assertNotNull (entity);
        
        entity.getPartyOneElement().setId("ID000001");
        
        StringWriter out = new StringWriter();
        Marshaller marshaller = new Marshaller (out);
        marshaller.setValidation(true);
        try {
            marshaller.marshal(entity);
            // fail ("ValidationException expected");
        }
        catch (ValidationException e) {
            // nothing to check
        }
        
    }
    
}