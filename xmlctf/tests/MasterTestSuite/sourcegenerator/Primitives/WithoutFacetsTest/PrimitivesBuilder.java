
import java.util.Vector;

import org.exolab.castor.tests.framework.ObjectModelBuilder;

public class PrimitivesBuilder implements ObjectModelBuilder {

    /**
     * Build the object expected when unmarshalling 'input1.xml'
     */
    public Object buildInstance() {

        TestPrimitives test = new TestPrimitives();

        test.setStringTestAtt("StringAttribute");
        test.setBooleanTestAtt(false);
        test.setFloatTestAtt(3.141526f);
        test.setDoubleTestAtt(1.171077);
        test.setDecimalTestAtt(new java.math.BigDecimal("123456789.987654321"));

        try {
            test.setDurationTestAtt(org.exolab.castor.types.Duration.parseDuration("P23Y3MT5H"));
            test.setTimeTestAtt(org.exolab.castor.types.Time.parseTime("04:14:00-08:00"));
            test.setDateTestAtt(org.exolab.castor.types.Date.parseDate("1976-02-02"));
            test.setGYearMonthTestAtt(org.exolab.castor.types.GYearMonth.parseGYearMonth("2001-07"));
            test.setGYearTestAtt(org.exolab.castor.types.GYear.parseGYear("1977"));
            test.setGMonthDayTestAtt(org.exolab.castor.types.GMonthDay.parseGMonthDay("--07-02"));
            test.setGDayTestAtt(org.exolab.castor.types.GDay.parseGDay("---17"));
            test.setGMonthTestAtt(org.exolab.castor.types.GMonth.parseGMonth("--02--"));
        } catch (Exception e) {
            //Can't happen
        }

        test.setAnyURITestAtt("http://www.castor.org");
        test.setStringTest("CAS-TOR");
        test.setBooleanTest(true);
        test.setFloatTest(123456.78f);
        test.setDoubleTest(0.6385682166079459);
        test.setDecimalTest(new java.math.BigDecimal("0.2693678757526658529286578414030373096466064453125"));

        try {
            test.setDurationTest(org.exolab.castor.types.Duration.parseDuration("P1D"));
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            test.setDateTimeTest(df.parse("2117-07-02T19:06:07.654"));
            test.setTimeTest(org.exolab.castor.types.Time.parseTime("17:01:32.3"));
            test.setDateTest(org.exolab.castor.types.Date.parseDate("2117-07-02"));
            test.setGYearMonthTest(org.exolab.castor.types.GYearMonth.parseGYearMonth("1977-10"));
            test.setGYearTest(org.exolab.castor.types.GYear.parseGYear("2134"));
            test.setGMonthDayTest(org.exolab.castor.types.GMonthDay.parseGMonthDay("--12-12"));
            test.setGDayTest(org.exolab.castor.types.GDay.parseGDay("---23"));
            test.setGMonthTest(org.exolab.castor.types.GMonth.parseGMonth("--05--+08:00"));

        } catch(Exception e) {
            // can't happen
        }

        test.setAnyURITest("http://www.castor.org");

        return test;
    }
}
