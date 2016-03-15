
import org.castor.xmlctf.ObjectModelBuilder;

public class PrimitivesBuilder implements ObjectModelBuilder {

    /**
     * Build the object expected when unmarshalling 'input1.xml'.
     */
    public Object buildInstance() {
        TestPrimitiveToWrapper test = new TestPrimitiveToWrapper();
        test.setBooleanTestAtt(Boolean.FALSE);
        test.setFloatTestAtt(Float.valueOf(3.141526f));
        test.setDoubleTestAtt(Double.valueOf(1.171077));
        test.setIntTestAtt(Integer.valueOf(23));
        test.setIntegerTestAtt(Long.valueOf(12));
        test.setLongTestAtt(Long.valueOf(23121311L));
        test.setShortTestAtt(Short.valueOf((short)2));
        test.setBooleanTest(Boolean.TRUE);
        test.setFloatTest(Float.valueOf(123456.78f));
        test.setDoubleTest(Double.valueOf(0.6385682166079459));
        test.setIntTest(Integer.valueOf(12));
        test.addIntegerTest(Long.valueOf(325325));
        test.setLongTest(Long.valueOf(98998989898L));
        test.setShortTest(Short.valueOf((short)34));

        return test;
    }

}
