import junit.framework.Test;
import junit.framework.TestSuite;

public final class AllTests {
    public static Test suite() {
        TestSuite suite = new TestSuite("Test suite for all bug reports");

        suite.addTestSuite(jdo.template.TestTemplate.class);
//        suite.addTestSuite(jdo.bug1900.TestFieldWithSpace.class);

        return suite;
    }
    
    private AllTests() { }
}
