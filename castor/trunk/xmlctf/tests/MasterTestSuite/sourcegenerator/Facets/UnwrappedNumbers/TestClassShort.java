import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ekuns
 */
public class TestClassShort extends TestClassBase {
    private static final Log LOG = LogFactory.getLog(TestClassShort.class);

    // Restriction facets from the schema:
    // <xsd:pattern value=".*23.*"/>
    // <xsd:totalDigits value="4"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodValues = { "23", "230", "2300", "-2300" };
    private String[] badValues = { "100",        // doesn't match pattern
                                   "23232",      // too many digits
                                   "-23232", };  // too many digits

    // Restriction facets from the schema:
    // <xsd:minInclusive value="-12345"/>
    // <xsd:maxInclusive value="7654"/>
    // <xsd:pattern value=".*23.*"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodValuesMinMax = { "-12345",  "-23", "7623", "23" };
    private String[] badValuesMinMax = { "8823",     // too large
                                         "-22345",   // too small
                                         "100", };   // doesn't match pattern

    public void testAddGoodValues() throws Exception {
        NumberTests instance = new NumberTests();
        for (int i = 0; i < goodValues.length; i++) {
            instance.addMyshort(Short.parseShort(goodValues[i].trim()));
        }
        for (int i = 0; i < goodValuesMinMax.length; i++) {
            instance.addMyshortMinMax(Short.parseShort(goodValuesMinMax[i].trim()));
        }
        instance.validate();
    }

    public boolean testUnmarshalGoodValues() throws Exception {
        return testUnmarshalGoodInstances("myshort", goodValues)
               && testUnmarshalGoodInstances("myshortMinMax", goodValuesMinMax);
    }

    public boolean testAddBadValues() {
        NumberTests instance = new NumberTests();
        for (int i = 0; i < badValues.length; i++) {
            try {
                instance.addMyshort(Short.parseShort(badValues[i].trim()));
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
                instance.addMyshortMinMax(Short.parseShort(badValuesMinMax[i].trim()));
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
        return testUnmarshalBadInstances("myshort", badValues) &&
               testUnmarshalBadInstances("myshortMinMax", badValuesMinMax);
    }

}
