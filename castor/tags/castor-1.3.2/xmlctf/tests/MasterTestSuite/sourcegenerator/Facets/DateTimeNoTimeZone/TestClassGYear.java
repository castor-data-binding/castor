import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ekuns
 */
public class TestClassGYear extends TestClassBase {
    private static final Log LOG = LogFactory.getLog(TestClassGYear.class);

    // Restriction facets from the schema:
    // <xsd:minInclusive value="1965"/>
    // <xsd:maxInclusive value="2065"/>
    // <xsd:pattern value=".*9.*"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodGYears = { "1965",
                                    "1999",
                                    "2009",
                                    "2059", };
    private String[] badGYears = { "1964",       // too early
                                   "1800",       // Doesn't match pattern
                                   "0000", };    // Illegal format

    public void testAddGoodGYears() throws Exception {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < goodGYears.length; i++) {
            instance.addGyearRanged(new org.exolab.castor.types.GYear(goodGYears[i].trim()));
        }
        instance.validate();
    }

    public boolean testUnmarshalGoodGYears() throws Exception {
        return testUnmarshalGoodInstances("gyearRanged", goodGYears);
    }

    public boolean testAddBadGYears() {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < badGYears.length; i++) {
            try {
                instance.addGyearRanged(new org.exolab.castor.types.GYear(badGYears[i].trim()));
                instance.validate();
            } catch (java.text.ParseException e) {
                // Good, we caught the exception
                continue;
            } catch (org.exolab.castor.xml.ValidationException e) {
                // Good, we caught the exception
                continue;
            } catch (java.lang.IllegalArgumentException e) {
                // Good, we caught the exception
                continue;
            }
            LOG.error("Bad date " + badGYears[i] + " did not fail the test");
            return false;
        }

        // If every test threw an expected exception, we're good.
        return true;
    }

    public boolean testUnmarshalBadGYears() throws Exception {
        return testUnmarshalBadInstances("gyearRanged", badGYears);
    }

}
