import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

public class TestMarshalIDREFS {
    
    private static final Log LOG = LogFactory.getLog(TestMarshalIDREFS.class);
    
    /**
     * test marshall idfrefs
     * 
     * @throws Exception
     */
    public void testMarshallIdrefs() throws Exception {
        Root test = new Root();

        Element1 el1 = new Element1();
        el1.setId1("CASTOR");
        Element2 el2 = new Element2();
        el2.setId2("POLLUX");
        Element3 el3 = new Element3();
        el3.setId3("PROMETHEE");

        ElementRef elRef = new ElementRef();
        elRef.addIdref(el1);
        elRef.addIdref(el2);
        test.setElement1(el1);
        test.setElement2(el2);
        test.setElement3(el3);
        test.setElementRef(elRef);
        StringWriter out = new StringWriter();
        Marshaller marshaller = new Marshaller(out);
        marshaller.setValidation(true);
        marshaller.marshal(test);
    }

    /**
     * test validate a null idref in an idrefs
     * 
     * @throws Exception
     */
    public void testMarshallIdrefsNullId() throws Exception {
        Root test = new Root();

        Element1 el1 = new Element1();
        el1.setId1("CASTOR");
        Element2 el2 = new Element2();
        el2.setId2("POLLUX");
        Element3 el3 = new Element3();
        el3.setId3("PROMETHEE");
        Element3 el4 = new Element3();
        el4.setId3(null);

        ElementRef elRef = new ElementRef();
        elRef.addIdref(el1);
        elRef.addIdref(el4);
        test.setElement1(el1);
        test.setElement2(el2);
        test.setElement3(el3);
        test.setElementRef(elRef);
        
        try {
            StringWriter out = new StringWriter();
            Marshaller marshaller = new Marshaller(out);
            marshaller.setValidation(true);
            marshaller.marshal(test);
        } catch (Exception e) {
            // assertTrue(e.getCause() instanceof ValidationException);
        }
    }
    
    /**
     * test validate a non existing idref in an idrefs
     * 
     * @throws Exception
     */
    public void testMarshallIdrefsWrongId() throws Exception {
        Root test = new Root();

        Element1 el1 = new Element1();
        el1.setId1("CASTOR");
        Element2 el2 = new Element2();
        el2.setId2("POLLUX");
        Element3 el3 = new Element3();
        el3.setId3("PROMETHEE");
        Element3 el4 = new Element3();
        el4.setId3("WRONG");

        ElementRef elRef = new ElementRef();
        elRef.addIdref(el1);
        elRef.addIdref(el4);
        test.setElement1(el1);
        test.setElement2(el2);
        test.setElement3(el3);
        test.setElementRef(elRef);
        
        try {
            StringWriter out = new StringWriter();
            Marshaller marshaller = new Marshaller(out);
            marshaller.setValidation(true);
            marshaller.marshal(test);
        } catch (Exception e) {
            // assertTrue(e.getCause() instanceof ValidationException);
        }
    }    
}
