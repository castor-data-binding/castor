
import java.util.Vector;

import org.castor.xmlctf.ObjectModelBuilder;

public class Restriction_Builder implements ObjectModelBuilder {

    /**
     * Build the object expected when unmarshalling 'input1.xml'.
     */
    public Object buildInstance() {
        MyDerivedElement test = new MyDerivedElement();
        test.addOtherElement(true);
        test.addOtherElement(false);
        return test;
    }

}
