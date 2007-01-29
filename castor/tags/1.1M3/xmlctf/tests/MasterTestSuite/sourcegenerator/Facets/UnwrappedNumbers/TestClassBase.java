import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * FIXME:  Whitespace:collapse is not properly implemented, at least for numeric
 * classes.  Thus, this facet is not tested with the test cases here.
 *
 * @author ekuns
 */
public class TestClassBase {
    private static final Log LOG = LogFactory.getLog(TestClassBase.class);
    protected static final String PREAMBLE = "<?xml version='1.0'?><NumberTests>";
    protected static final String POSTAMBLE = "</NumberTests>";

    public boolean testUnmarshalGoodInstances(String type, String[] items) throws Exception {
        String item = null;
        try {
            for (int i = 0; i < items.length; i++) {
                item = items[i];
                java.io.Reader reader = new java.io.StringReader(PREAMBLE + "<" + type + ">"
                        + items[i] + "</" + type + ">" + POSTAMBLE);
                NumberTests instance = NumberTests.unmarshal(reader);
                instance.validate();
            }
        } catch (Exception e) {
            LOG.error("Good value '" + item + "' failed the test",e);
            return false;
        }

        return true;
    }

    public boolean testUnmarshalBadInstances(String type, String[] items) throws Exception {
        for (int i = 0; i < items.length; i++) {
            try {
                java.io.Reader reader = new java.io.StringReader(PREAMBLE + "<" + type + ">"
                        + items[i] + "</" + type + ">" + POSTAMBLE);
                NumberTests instance = NumberTests.unmarshal(reader);
                instance.validate();
            } catch (org.exolab.castor.xml.MarshalException e) {
                // Good, we caught the exception
                continue;
            } catch (org.exolab.castor.xml.ValidationException e) {
                // Good, we caught the exception
                continue;
            }
            LOG.error("Bad value '" + items[i] + "' did not fail the test");
            return false;
        }

        return true;
    }

}
