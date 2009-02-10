import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ekuns
 */
public class TestClassGMonth extends TestClassBase {
    private static final Log LOG = LogFactory.getLog(TestClassGMonth.class);

    // Restriction facets from the schema:
    // <xsd:minInclusive value="--04--"/>
    // <xsd:maxInclusive value="--12--"/>
    // <xsd:pattern value="--.*1.*"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodGMonths = { "--05--+01:00",
                                     "--12--", };
    private String[] badGMonths = { "--01--",       // too early
                                    "--02--",       // Doesn't match pattern
                                    "--00--",       // Illegal format
                                    "00--",         // Illegal format
                                    "--13--", };    // Illegal format

    public void testAddGoodGMonths() throws Exception {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < goodGMonths.length; i++) {
            instance.addGmonthRanged(new org.exolab.castor.types.GMonth(goodGMonths[i].trim()));
        }
        instance.validate();
    }

    public boolean testUnmarshalGoodGMonths() throws Exception {
        return testUnmarshalGoodInstances("gmonthRanged", goodGMonths);
    }

    public boolean testAddBadGMonths() {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < badGMonths.length; i++) {
            try {
                instance.addGmonthRanged(new org.exolab.castor.types.GMonth(badGMonths[i].trim()));
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
            LOG.error("Bad date " + badGMonths[i] + " did not fail the test");
            return false;
        }

        // If every test threw an expected exception, we're good.
        return true;
    }

    public boolean testUnmarshalBadGMonths() throws Exception {
        return testUnmarshalBadInstances("gmonthRanged", badGMonths);
    }

}
