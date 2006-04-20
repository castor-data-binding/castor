package xml.xml2java;



import java.io.*;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import junit.framework.*;
import junit.extensions.*;

public class TimeInstantTest extends TestCase {
   
    Unmarshaller unmarshaller;
    TimeInstantTestxsd    test_obj;

   
        
   public TimeInstantTest(String name) {
        super(name);
    }
    
    
    public void test() {

        try {
           
            unmarshaller = new Unmarshaller(TimeInstantTestxsd.class);
            
            test_obj = (TimeInstantTestxsd) unmarshaller.unmarshal( new FileReader( "src/tests/xml/xml2java/timeInstant.xml" ) );
            
         
            assert("both timeInstant are not equals: "
                   + test_obj.getTimeInstant() + " and "
                   + "Mon May 31 13:20:00 PDT 1999", (((test_obj
                       .getTimeInstant()).toString())
                           .equals("Mon May 31 13:20:00 PDT 1999")));
            
            
            
            
                        
        } catch (Exception excep) {
            fail("error: " + excep);
        }
    }
    
}