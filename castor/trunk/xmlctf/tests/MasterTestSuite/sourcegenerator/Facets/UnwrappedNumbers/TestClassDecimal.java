import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ekuns
 */
public class TestClassDecimal extends TestClassBase {
    private static final Log LOG = LogFactory.getLog(TestClassDecimal.class);

    // Restriction facets from the schema:
    // <xsd:pattern value=".*23.*"/>
    // <xsd:totalDigits value="5"/>
    // <xsd:fractionDigits value="3"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodValues = { "23232", "232.22", "23.232",
                                    "-23232", "-232.22", "-23.232",
                                    "0000000023",       // leading zeros are not counted
                                    "0000023.23300", }; // trailing zeros are not counted
    private String[] badValues = { "100.005",    // doesn't match pattern
                                   "230000",     // too many digits (make sure zeros here are counted)
                                   "223232",     // too many digits
                                   "22323.2",    // too many digits
                                   "2232.32",    // too many digits
                                   "223.232",    // too many digits
                                   "0.2322",     // too many fraction digits
                                   "2.3232", };  // too many fraction digits

    // Restriction facets from the schema:
    // <xsd:minInclusive value="-12345.5"/>
    // <xsd:maxInclusive value="54321.25"/>
    // <xsd:pattern value=".*23.*"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodValuesMinMax = { "-12345.5", "54321.23", "0.23" };
    private String[] badValuesMinMax = { "55555",       // too large
                                         "54321.2523",  // too large
                                         "-22222",      // too small
                                         "-12345.723",  // too small
                                         "33", };       // doesn't match pattern

    public void testAddGoodValues() throws Exception {
        NumberTests instance = new NumberTests();
        for (int i = 0; i < goodValues.length; i++) {
            instance.addMydecimal(new BigDecimal(goodValues[i].trim()));
        }
        for (int i = 0; i < goodValuesMinMax.length; i++) {
            instance.addMydecimalMinMax(new BigDecimal(goodValuesMinMax[i].trim()));
        }
        instance.validate();
    }

    public boolean testUnmarshalGoodValues() throws Exception {
        return testUnmarshalGoodInstances("mydecimal", goodValues)
               && testUnmarshalGoodInstances("mydecimalMinMax", goodValuesMinMax);
    }

    public boolean testAddBadValues() {
        NumberTests instance = new NumberTests();
        for (int i = 0; i < badValues.length; i++) {
            try {
                instance.addMydecimal(new BigDecimal(badValues[i].trim()));
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
                instance.addMydecimalMinMax(new BigDecimal(badValuesMinMax[i].trim()));
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
        return testUnmarshalBadInstances("mydecimal", badValues) &&
               testUnmarshalBadInstances("mydecimalMinMax", badValuesMinMax);
    }

}
