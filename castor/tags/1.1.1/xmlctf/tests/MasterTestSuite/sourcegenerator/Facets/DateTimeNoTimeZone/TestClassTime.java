import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author ekuns
 */
public class TestClassTime extends TestClassBase {
    private static final Log LOG = LogFactory.getLog(TestClassTime.class);

    // Restriction facets from the schema:
    // <xsd:minInclusive value="07:01:01.999"/>
    // <xsd:maxInclusive value="17:59:59.999"/>
    // <xsd:pattern value=".*9.*"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodTimes = { "07:01:01.999",
                                   "17:59:59.999",
                                   "17:59:59",
                                   "17:59:59.9",
                                   "17:59:59.999999", };
    private String[] badTimes = { "07:00:00",                  // too early
                                  "25:00:00",                  // invalid time
                                  "09:0:00",                   // invalid time
                                  "09:0:00-24:00",             // invalid time zone
                                  "09:0:00-24-00",             // invalid time zone
                                  "09:0:00-24",                // invalid time zone
                                  "18:0:00", };                // too late

    public void testAddGoodTimes() throws Exception {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < goodTimes.length; i++) {
            instance.addTimeRanged(new org.exolab.castor.types.Time(goodTimes[i].trim()));
        }
        instance.validate();
    }

    public boolean testUnmarshalGoodTimes() throws Exception {
        return testUnmarshalGoodInstances("timeRanged", goodTimes);
    }

    public Object testAddBadTimes() {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < badTimes.length; i++) {
            try {
                instance.addTimeRanged(new org.exolab.castor.types.Time(badTimes[i].trim()));
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
            } catch (java.lang.Exception e) {
                LOG.error("Bad time " + badTimes[i] + " threw an unexpected Exception");
                return e;
            }
            LOG.error("Bad time " + badTimes[i] + " did not fail the test");
            return Boolean.FALSE;
        }

        // If every test threw one of the expected Exceptions, we're good.
        return Boolean.TRUE;
    }

    public boolean testUnmarshalBadTimes() throws Exception {
        return testUnmarshalBadInstances("dateTimeRanged", badTimes);
    }

}
