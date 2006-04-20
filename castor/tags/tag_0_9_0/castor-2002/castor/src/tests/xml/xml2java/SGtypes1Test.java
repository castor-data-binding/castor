package xml.xml2java;



import java.io.*;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import junit.framework.*;
import junit.extensions.*;

/**
 */
public class SGtypes1Test 

{


    
    public static Test suite() {
         TestSuite suite= new TestSuite();
         suite.addTest(new TestSuite(SGtypes1TestCases.class));
         TestSetup wrapper= new TestSetup(suite) { 
            public void setUp() { 
                SGtypes1TestCases.oneTimeSetUp();   // this setUp method will be used only once during the test process
            } 
         }; 
       return wrapper; 
    }
    

}