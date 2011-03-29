import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ekuns
 */
public class TestClassDate extends TestClassBase {
    private static final Log LOG = LogFactory.getLog(TestClassDate.class);

    // Restriction facets from the schema:
    //    <xsd:minInclusive value="2006-03-01"/>
    //    <xsd:maxInclusive value="2006-06-30"/>
    //    <xsd:whiteSpace value="collapse"/>
    //    <xsd:pattern value=".*04.*"/>
    private String[] goodDates = { "2006-03-04",
//                                   "2006-03-01-14:04",
//                                   "2006-03-01+13:04",
                                   "2006-03-02-04:00",
                                   "2006-04-01",
                                   "2006-04-04",
                                   "2006-04-30",
                                   "2006-05-04",
                                   "2006-06-01-04:00",
                                   "2006-06-04",
                                   "2006-06-30-14:04", };
    private String[] badDates = { "2004-03-04",       // too early
                                  "2006-03-01",       // Doesn't match pattern
                   //             "2006-03-01-04:00", // because of time zone, ambiguous
                                  "2006-03-04-25:00", // Invalid time zone
                                  "2006-04-31",       // Invalid date
//                                  "2006-06-30+14:04", // FIXME: because of time zone, ambiguous
                                  "2006-09-04",       // Too late
                                  "0000-09-04",       // Illegal format
                                  "2006-04-01-24:00", // invalid time zone
                                  "2006-04-01-01:60", // invalid time zone
                                  "2006-04-01-24-00", // invalid time zone
                                  "2006-04-01-24", }; // invalid time zone

    public void testAddGoodDates() throws Exception {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < goodDates.length; i++) {
            instance.addDateRanged(new org.exolab.castor.types.Date(goodDates[i].trim()));
        }
        instance.validate();
    }

    public boolean testUnmarshalGoodDates() throws Exception {
        return testUnmarshalGoodInstances("dateRanged", goodDates);
    }

    public boolean testAddBadDates() {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < badDates.length; i++) {
            try {
                instance.addDateRanged(new org.exolab.castor.types.Date(badDates[i].trim()));
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
            LOG.error("Bad date " + badDates[i] + " did not fail the test");
            return false;
        }

        // If every test threw an expected exception, we're good.
        return true;
    }

    public boolean testUnmarshalBadDates() throws Exception {
        return testUnmarshalBadInstances("dateRanged", badDates);
    }

}
