import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author ekuns
 */
public class TestClassDateTime extends TestClassBase {
    private static final Log LOG = LogFactory.getLog(TestClassDateTime.class);

    // Restriction facets from the schema:
    //    <xsd:minInclusive value="2006-03-01T00:00:00"/>
    //    <xsd:maxInclusive value="2006-06-30T23:59:59"/>
    //    <xsd:pattern value=".*23.*"/>
    //    <xsd:whiteSpace value="collapse"/>
    private String[] goodDateTimes = { //"2006-03-01T00:00:23", FIXME: Enable this after we use Calendar not Date
                                       "2006-03-23T00:00:00",
                                       "2006-03-01T23:00:00",
                                       "2006-04-23T00:00:00",
                                       "2006-05-23T00:00:00",
                                       "2006-05-20T00:00:23",
                                       "2006-06-30T23:59:59", };
    private String[] badDateTimes = { "2004-03-01T00:00:23",       // too early
                                      "2006-06-30T23:59:60",       // invalid date
                                      "2006-06-30T23:60:59",       // invalid date
                                      "2006-06-30T24:59:59",       // invalid date
                                      "2006-06-30T12:5:59",        // invalid date
                                      "2006-06-30T12:05:0",        // invalid date
                                      "0000-06-30T23:59:59",       // invalid date
                                      "2006-06-30T23:59:59-24:00", // invalid time zone
                                      "2006-06-30T23:59:59-24-00", // invalid time zone
                                      "2006-06-30T23:59:59-24",    // invalid time zone
                                      "2008-06-30T23:59:59", };    // too late

    public void testAddGoodDateTimes() throws Exception {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < goodDateTimes.length; i++) {
            instance.addDateTimeRanged(new org.exolab.castor.types.DateTime(goodDateTimes[i].trim()).toDate());
        }
        instance.validate();
    }

    public boolean testUnmarshalGoodDateTimes() throws Exception {
        return testUnmarshalGoodInstances("dateTimeRanged", goodDateTimes);
    }

    public Object testAddBadDateTimes() {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < badDateTimes.length; i++) {
            try {
                instance.addDateTimeRanged(new org.exolab.castor.types.DateTime(badDateTimes[i].trim()).toDate());
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
                LOG.error("Bad dateTime " + badDateTimes[i] + " threw an unexpected Exception");
                return e;
            }
            LOG.error("Bad dateTime " + badDateTimes[i] + " did not fail the test");
            return Boolean.FALSE;
        }

        // If every test threw one of the expected Exceptions, we're good.
        return Boolean.TRUE;
    }

    public boolean testUnmarshalBadDateTimes() throws Exception {
        return testUnmarshalBadInstances("dateTimeRanged", badDateTimes);
    }

}
