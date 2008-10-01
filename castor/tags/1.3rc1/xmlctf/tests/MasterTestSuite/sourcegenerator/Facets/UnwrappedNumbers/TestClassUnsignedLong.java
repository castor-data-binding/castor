import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ekuns
 */
public class TestClassUnsignedLong extends TestClassBase {
    private static final Log LOG = LogFactory.getLog(TestClassUnsignedLong.class);

    // Restriction facets from the schema:
    // <xsd:pattern value=".*23.*"/>
    // <xsd:totalDigits value="8"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodValues = { "23", "230", "2300", "23000", "23000000",
                                    "0000000023"}; // leading zeros are not counted
    private String[] badValues = { "100",        // doesn't match pattern
                                   "232323232", };  // too many digits

    // Restriction facets from the schema:
    // <xsd:minInclusive value="12345678"/>
    // <xsd:maxInclusive value="87654231"/>
    // <xsd:pattern value=".*23.*"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodValuesMinMax = { "12345678", "23232323", "87654231" };
    private String[] badValuesMinMax = { "823888888",   // too large
                                         "100",         // too small
                                         "1334567", };  // doesn't match pattern

    public void testAddGoodValues() throws Exception {
        NumberTests instance = new NumberTests();
        for (int i = 0; i < goodValues.length; i++) {
            instance.addMyunsignedlong(new BigInteger(goodValues[i].trim()));
        }
        for (int i = 0; i < goodValuesMinMax.length; i++) {
            instance.addMyunsignedlongMinMax(new BigInteger(goodValuesMinMax[i].trim()));
        }
        instance.validate();
    }

    public boolean testUnmarshalGoodValues() throws Exception {
        return testUnmarshalGoodInstances("myunsignedlong", goodValues)
               && testUnmarshalGoodInstances("myunsignedlongMinMax", goodValuesMinMax);
    }

    public boolean testAddBadValues() {
        NumberTests instance = new NumberTests();
        for (int i = 0; i < badValues.length; i++) {
            try {
                instance.addMyunsignedlong(new BigInteger(badValues[i].trim()));
                instance.validate();
            } catch (org.exolab.castor.xml.ValidationException e) {
                // Good, we caught the exception
                continue;
            } catch (java.lang.IllegalArgumentException e) {
                // Good, we caught the exception
                continue;
            }
            LOG.error("Bad value " + badValues[i] + " did not fail the test");
            return false;
        }

        for (int i = 0; i < badValuesMinMax.length; i++) {
            try {
                instance.addMyunsignedlongMinMax(new BigInteger(badValuesMinMax[i].trim()));
                instance.validate();
            } catch (org.exolab.castor.xml.ValidationException e) {
                // Good, we caught the exception
                continue;
            } catch (java.lang.IllegalArgumentException e) {
                // Good, we caught the exception
                continue;
            }
            LOG.error("Bad value " + badValuesMinMax[i] + " did not fail the test");
            return false;
        }

        // If every test threw an expected exception, we're good.
        return true;
    }

    public boolean testUnmarshalBadValues() throws Exception {
        return testUnmarshalBadInstances("myunsignedlong", badValues) &&
               testUnmarshalBadInstances("myunsignedlongMinMax", badValuesMinMax);
    }

}
