import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ekuns
 */
public class TestClassGMonthDay extends TestClassBase {
    private static final Log LOG = LogFactory.getLog(TestClassGMonthDay.class);

    // Restriction facets from the schema:
    // <xsd:minInclusive value="--04-01"/>
    // <xsd:maxInclusive value="--12-31"/>
    // <xsd:pattern value="--.*9.*"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodGMonthDays = { "--04-09",
                                        "--09-30",
                                        "--12-29", };
    private String[] badGMonthDays = { "--01-09",       // too early
                                       "--05-05",       // Doesn't match pattern
                                       "--12-32", };    // Illegal format

    public void testAddGoodGMonthDays() throws Exception {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < goodGMonthDays.length; i++) {
            instance.addGmonthdayRanged(new org.exolab.castor.types.GMonthDay(goodGMonthDays[i].trim()));
        }
        instance.validate();
    }

    public boolean testUnmarshalGoodGMonthDays() throws Exception {
        return testUnmarshalGoodInstances("gmonthdayRanged", goodGMonthDays);
    }

    public boolean testAddBadGMonthDays() {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < badGMonthDays.length; i++) {
            try {
                instance.addGmonthdayRanged(new org.exolab.castor.types.GMonthDay(badGMonthDays[i].trim()));
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
            LOG.error("Bad date " + badGMonthDays[i] + " did not fail the test");
            return false;
        }

        // If every test threw an expected exception, we're good.
        return true;
    }

    public boolean testUnmarshalBadGMonthDays() throws Exception {
        return testUnmarshalBadInstances("gmonthdayRanged", badGMonthDays);
    }

}
