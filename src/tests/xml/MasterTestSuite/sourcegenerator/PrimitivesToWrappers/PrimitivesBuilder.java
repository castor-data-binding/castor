
import java.util.Vector;

import org.exolab.castor.tests.framework.ObjectModelBuilder;

public class PrimitivesBuilder implements ObjectModelBuilder
{

    /**
     * Build the object expected when unmarshalling 'input1.xml'
     */
    public Object buildInstance() {

        TestPrimitiveToWrapper test = new TestPrimitiveToWrapper();

        test.setBooleanTestAtt(new Boolean(false));

        test.setFloatTestAtt(new Float(3.141526f));

        test.setDoubleTestAtt(new Double(1.171077));

        test.setIntTestAtt(new Integer(23));
	  
        test.setIntegerTestAtt(new Long(12));
        
        test.setLongTestAtt(new Long(23121311L));
        
        test.setShortTestAtt(new Short((short)2));
       
        test.setBooleanTest(new Boolean(true));

        test.setFloatTest(new Float(1234567899876543210f));

        test.setDoubleTest(new Double(0.6385682166079459));
        
        test.setIntTest(new Integer(12));
	  
        test.addIntegerTest(new Long(325325));
        
        test.setLongTest(new Long(98998989898L));
        
        test.setShortTest(new Short((short)34));
        
        return test;

    }

}


