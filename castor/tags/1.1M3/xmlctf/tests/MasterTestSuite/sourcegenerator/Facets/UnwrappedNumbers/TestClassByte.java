import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ekuns
 */
public class TestClassByte extends TestClassBase {
    private static final Log LOG = LogFactory.getLog(TestClassByte.class);

    // Restriction facets from the schema:
    // <xsd:pattern value=".*9.*"/>
    // <xsd:totalDigits value="2"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodValues = { "9", "99", "-99", "-9" };
    private String[] badValues = { "1",       // doesn't match pattern
                                   "-109",    // too many digits
                                   "109", };  // too many digits

    // Restriction facets from the schema:
    // <xsd:minInclusive value="-10"/>
    // <xsd:maxInclusive value="80"/>
    // <xsd:pattern value=".*9.*"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodValuesMinMax = { "9", "-9" };
    private String[] badValuesMinMax = { "109",    // too large
                                         "-19",    // too small
                                         "10", };  // doesn't match pattern

    public void testAddGoodValues() throws Exception {
        NumberTests instance = new NumberTests();
        for (int i = 0; i < goodValues.length; i++) {
            instance.setMybyte(Byte.parseByte(goodValues[i].trim()));
            instance.validate();
        }
        for (int i = 0; i < goodValuesMinMax.length; i++) {
            instance.setMybyteMinMax(Byte.parseByte(goodValuesMinMax[i].trim()));
            instance.validate();
        }
    }

    public boolean testUnmarshalGoodValues() throws Exception {
        return testUnmarshalGoodInstances("mybyte", goodValues)
               && testUnmarshalGoodInstances("mybyteMinMax", goodValuesMinMax);
    }

    public boolean testAddBadValues() {
        NumberTests instance = new NumberTests();
        for (int i = 0; i < badValues.length; i++) {
            try {
                instance.setMybyte(Byte.parseByte(badValues[i].trim()));
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
                instance.setMybyteMinMax(Byte.parseByte(badValuesMinMax[i].trim()));
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
        return testUnmarshalBadInstances("mybyte", badValues) &&
               testUnmarshalBadInstances("mybyteMinMax", badValuesMinMax);
    }

}
