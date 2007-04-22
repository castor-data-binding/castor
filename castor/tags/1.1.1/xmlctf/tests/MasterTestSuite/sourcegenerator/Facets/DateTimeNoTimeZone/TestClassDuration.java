import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author ekuns
 */
public class TestClassDuration extends TestClassBase {
    private static final Log LOG = LogFactory.getLog(TestClassDuration.class);

    // Restriction facets from the schema:
    // <xsd:minInclusive value="P5DT20S"/>
    // <xsd:maxInclusive value="P200DT20S"/>
    // <xsd:pattern value=".*20S.*"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodDurations = { "P5DT20S",
                                       "P50DT20S",
                                       "P1M4DT3H20S", };
    private String[] badDurations = { "PT20S",                  // too short
                                      "P2DT20S",                // too short
                                      "T20S",                   // invalid duration
                                      "P500DT20S", };           // too long

    public void testAddGoodDurations() throws Exception {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < goodDurations.length; i++) {
            instance.addDurationRanged(new org.exolab.castor.types.Duration(goodDurations[i].trim()));
        }
        instance.validate();
    }

    public boolean testUnmarshalGoodDurations() throws Exception {
        return testUnmarshalGoodInstances("durationRanged", goodDurations);
    }

    public Object testAddBadDurations() {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < badDurations.length; i++) {
            try {
                instance.addDurationRanged(new org.exolab.castor.types.Duration(badDurations[i].trim()));
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
                LOG.error("Bad duration " + badDurations[i] + " threw an unexpected Exception");
                return e;
            }
            LOG.error("Bad duration " + badDurations[i] + " did not fail the test");
            return Boolean.FALSE;
        }

        // If every test threw one of the expected Exceptions, we're good.
        return Boolean.TRUE;
    }

    public boolean testUnmarshalBadDurations() throws Exception {
        return testUnmarshalBadInstances("durationRanged", badDurations);
    }

}
