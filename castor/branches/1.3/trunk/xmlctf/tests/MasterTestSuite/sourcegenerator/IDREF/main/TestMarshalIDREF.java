import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

public class TestMarshalIDREF {
    
    private static final Log LOG = LogFactory.getLog(TestMarshalIDREF.class);
    
    /**
     * Set the href to another existing element
     * @throws Exception
     */
    public void testMarshallChangeHref() throws Exception {
        Unmarshaller unmarshaller = new Unmarshaller (PartialTermination.class);
        
        try {
            PartialTermination entity = (PartialTermination)  unmarshaller.unmarshal(new InputSource(this.getClass().getResource("input-ok.xml").toExternalForm()));
            
            AssignmentNotification[] assignmentNotification = entity.getAssignmentNotification();
            // assertEquals(assignmentNotification[0].getPartyReference().getHref(), entity.getPartyTwo());
            assignmentNotification[0].getPartyReference().setHref(entity.getPartyOne());
            
            StringWriter out = new StringWriter();
            Marshaller marshaller = new Marshaller (out);
            marshaller.setValidation(true);
            marshaller.marshal(entity);
            
            // assertTrue(out.toString().indexOf("<assignmentNotification><partyReference href=\"ID000000\"/>")>-1);
            
        }
        catch (Exception e) {
            // assertTrue(e instanceof MarshalException);
        }
    }   
    
    /**
     * Set the href to an object that HAS have an id, but an inexistent one 
     * @throws Exception
     */
    public void testMarshallInexistentHref() throws Exception {
        Unmarshaller unmarshaller = new Unmarshaller (PartialTermination.class);
        
        try {
            PartialTermination entity = (PartialTermination)  unmarshaller.unmarshal(new InputSource(getClass().getResource("input-ok.xml").toExternalForm()));
            
            AssignmentNotification[] assignmentNotification = entity.getAssignmentNotification();
            // assertEquals(assignmentNotification[0].getPartyReference().getHref(), entity.getPartyTwo());
            
            PartyTwo partyTwo = new PartyTwo();
            partyTwo.setId("WRONG");
            partyTwo.setPartyName("Other");
            partyTwo.setPartyId(entity.getPartyTwo().getPartyId());
            assignmentNotification[0].getPartyReference().setHref(partyTwo);
            
            StringWriter out = new StringWriter();
            Marshaller marshaller = new Marshaller (out);
            marshaller.setValidation(true);
            marshaller.marshal(entity);
            
            // fail ("Nested ValidationException expected.");
        }
        catch (Exception e) {
            // assertTrue(e.getCause() instanceof ValidationException);
        }
    }
    
    /**
     * Set the href manually null
     */
    public void testMarshallNullHref() throws Exception {
        Unmarshaller unmarshaller = new Unmarshaller (PartialTermination.class);
        
        try {
            PartialTermination entity = (PartialTermination)  unmarshaller.unmarshal(new InputSource(getClass().getResource("input-ok.xml").toExternalForm()));
            
            AssignmentNotification[] assignmentNotification = entity.getAssignmentNotification();
            assignmentNotification[0].getPartyReference().setHref(null);
            
            StringWriter out = new StringWriter();
            Marshaller marshaller = new Marshaller (out);
            marshaller.setValidation(true);
            marshaller.marshal(entity);
            
            // fail ("MarshalException expected, because partyReference/@href is use=\"required\"");
        }
        catch (Exception e) {
            // assertTrue(e instanceof ValidationException);
        }
    }
    
    /**
     * Set the href to an object that doesnt have an id 
     * @throws Exception
     */
    public void testMarshallWrongHref() throws Exception {
        Unmarshaller unmarshaller = new Unmarshaller (PartialTermination.class);
        
        try {
            PartialTermination entity = (PartialTermination)  unmarshaller.unmarshal(new InputSource(getClass().getResource("input-ok.xml").toExternalForm()));
            
            AssignmentNotification[] assignmentNotification = entity.getAssignmentNotification();
            assignmentNotification[0].getPartyReference().setHref("bla");
            
            StringWriter out = new StringWriter();
            Marshaller marshaller = new Marshaller (out);
            marshaller.setValidation(true);
            marshaller.marshal(entity);
            
            // fail ("Nested ValidationException expected.");
        }
        catch (Exception e) {
            // assertTrue(e.getCause() instanceof ValidationException);
        }

    }
}