
import java.util.Vector;

import org.exolab.castor.tests.framework.ObjectModelInstanceBuilder;

public class Restriction_Builder implements ObjectModelInstanceBuilder
{

    /**
     * Build the object expected when unmarshalling 'input1.xml'
     */
    public Object buildInstance() {

        MyDerivedElement test = new MyDerivedElement();

        test.addOtherElement(true);
        
        test.addOtherElement(false);

        return test;

    }

}


