
import java.util.Vector;

import org.exolab.castor.tests.framework.ObjectModelInstanceBuilder;

public class NMTOKENS_Builder implements ObjectModelInstanceBuilder
{

    /**
     * Build the object expected when unmarshalling 'input1.xml'
     */
    public Object buildInstance() {

        Test_NMTOKENS test = new Test_NMTOKENS();

        test.addBar("nm1");
        
        test.addBar("nm2");
        
        test.addBar("nm3");
        
        test.addFoo("NMTOKEN1");
        
        test.addFoo("NMTOKEN2");

        return test;

    }

}


