
import java.util.Vector;

import org.exolab.castor.tests.framework.ObjectModelBuilder;

public class IDREFSBuilder implements ObjectModelBuilder
{

    /**
     * Build the object expected when unmarshalling 'input1.xml'
     */
    public Object buildInstance() {

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
        return test;

    }

}


