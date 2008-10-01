import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ekuns
 */
public class TestClassGDay extends TestClassBase {
    private static final Log LOG = LogFactory.getLog(TestClassGDay.class);

    // Restriction facets from the schema:
    // <xsd:minInclusive value="---04"/>
    // <xsd:maxInclusive value="---24"/>
    // <xsd:pattern value="---.4"/>
    // <xsd:whiteSpace value="collapse"/>
    private String[] goodGDays = { "---04",
                                   "---24", };
    private String[] badGDays = { "---01+04:00",      // too early
                                  "---05",            // Doesn't match pattern
                                  "---25+04:00",      // Too late
                                  "--24", };          // Illegal format

    public void testAddGoodGDays() throws Exception {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < goodGDays.length; i++) {
            instance.addGdayRanged(new org.exolab.castor.types.GDay(goodGDays[i].trim()));
        }
        instance.validate();
    }

    public boolean testUnmarshalGoodGDays() throws Exception {
        return testUnmarshalGoodInstances("gdayRanged", goodGDays);
    }

    public boolean testAddBadGDays() {
        DateTimeTests instance = new DateTimeTests();
        for (int i = 0; i < badGDays.length; i++) {
            try {
                instance.addGdayRanged(new org.exolab.castor.types.GDay(badGDays[i].trim()));
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
            LOG.error("Bad date " + badGDays[i] + " did not fail the test");
            return false;
        }

        // If every test threw an expected exception, we're good.
        return true;
    }

    public boolean testUnmarshalBadGDays() throws Exception {
        return testUnmarshalBadInstances("gdayRanged", badGDays);
    }

}
