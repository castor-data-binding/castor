import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ekuns
 */
public class TestClassGYearMonth extends TestClassBase {
    private static final Log LOG = LogFactory.getLog(TestClassGYearMonth.class);

    // Restriction facets from the schema:
    // <xsd:minInclusive value="1965-01"/>
    // <xsd:maxInclusive value="2065-12"/>
    // <xsd:pattern value=".*9.*"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodGYearMonths = { "1965-01",
                                         "2065-09",
                                         "2059-01", };
    private String[] badGYearMonths = { "1964-01",       // too early
                                        "2000-01",       // Doesn't match pattern
                                        "2099-01",       // too late
                                        "0000-01", };    // Illegal format

    public void testAddGoodGYearMonths() throws Exception {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < goodGYearMonths.length; i++) {
            instance.addGyearmonthRanged(new org.exolab.castor.types.GYearMonth(goodGYearMonths[i].trim()));
        }
        instance.validate();
    }

    public boolean testUnmarshalGoodGYearMonths() throws Exception {
        return testUnmarshalGoodInstances("gyearmonthRanged", goodGYearMonths);
    }

    public boolean testAddBadGYearMonths() {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < badGYearMonths.length; i++) {
            try {
                instance.addGyearmonthRanged(new org.exolab.castor.types.GYearMonth(badGYearMonths[i].trim()));
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
            LOG.error("Bad date " + badGYearMonths[i] + " did not fail the test");
            return false;
        }

        // If every test threw an expected exception, we're good.
        return true;
    }

    public boolean testUnmarshalBadGYearMonths() throws Exception {
        return testUnmarshalBadInstances("gyearmonthRanged", badGYearMonths);
    }

}
