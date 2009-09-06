import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ekuns
 */
public class TestClassInt extends TestClassBase {
    private static final Log LOG = LogFactory.getLog(TestClassInt.class);

    // Restriction facets from the schema:
    // <xsd:pattern value=".*23.*"/>
    // <xsd:totalDigits value="5"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodValues = { "23", "230", "2300", "23000" };
    private String[] badValues = { "100",        // doesn't match pattern
                                   "-232323",    // too many digits
                                   "232323", };  // too many digits

    // Restriction facets from the schema:
    // <xsd:minInclusive value="-1234567"/>
    // <xsd:maxInclusive value="7654321"/>
    // <xsd:pattern value=".*23.*"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodValuesMinMax = { "1234567",  "-23", "-1234567", "7654231" };
    private String[] badValuesMinMax = { "8238888",     // too large
                                         "-2222322",    // too small
                                         "1334567", };  // doesn't match pattern

    public void testAddGoodValues() throws Exception {
        NumberTests instance = new NumberTests();
        for (int i = 0; i < goodValues.length; i++) {
            instance.addMyint(Integer.parseInt(goodValues[i].trim()));
        }
        for (int i = 0; i < goodValuesMinMax.length; i++) {
            instance.addMyintMinMax(Integer.parseInt(goodValuesMinMax[i].trim()));
        }
        instance.validate();
    }

    public boolean testUnmarshalGoodValues() throws Exception {
        return testUnmarshalGoodInstances("myint", goodValues)
               && testUnmarshalGoodInstances("myintMinMax", goodValuesMinMax);
    }

    public boolean testAddBadValues() {
        NumberTests instance = new NumberTests();
        for (int i = 0; i < badValues.length; i++) {
            try {
                instance.addMyint(Integer.parseInt(badValues[i].trim()));
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
                instance.addMyintMinMax(Integer.parseInt(badValuesMinMax[i].trim()));
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
        return testUnmarshalBadInstances("myint", badValues) &&
               testUnmarshalBadInstances("myintMinMax", badValuesMinMax);
    }

}
