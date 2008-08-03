import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.FileInputStream;
import java.net.URL;

import junit.framework.TestResult;
import harness.Harness;
import harness.TestHarness;
import harness.CastorTestCase;

import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.mapping.Mapping;

public class Main extends TestHarness {

    private final static String DEFAULT_FILE = "tests.xml";

    /**
     * Arguments
     */
    private String testBranchs;

    /**
     * Indicates we only display information of test case, but not running them.
     */
    private boolean printInfo;

    /**
     * The test file
     */
    private String testFile;

    /**
     * The test url
     */
    private String testUrl;

    /**
     * The test resource
     */
    private String testRes;

    public Main() throws Exception {
        this(false, false, false, null, null, null, "castor.mysql");
        String testCategory = System.getProperty("test.category", "castor.mysql");
        init (false, false, false, null, null, null, testCategory);
    }
    
    /**
     * Constructor
     */
    Main(final boolean verbose, 
            final boolean printInfo, 
            final boolean gui, 
            final String testRes,
            final String testFile, 
            final String testUrl, 
            final String tests)
            throws Exception {
        super(null, "Castor", "Root");
        init (false, false, false, null, null, null, tests);
    }

    private void init (final boolean verbose, 
            final boolean printInfo, 
            final boolean gui, 
            final String testRes,
            final String testFile, 
            final String testUrl, 
            final String tests) throws Exception {
        
        TestHarness.verbose = verbose;
        this.printInfo = printInfo;
        this.testBranchs = tests;
        this.testFile = testFile;
        this.testUrl = testUrl;
        this.testRes = testRes;
        if (verbose) {
            TestHarness.setVerboseStream(System.out);
            TestHarness.setVerbose(true);
            CastorTestCase.setVerboseStream(System.out);
            CastorTestCase.setVerbose(true);
        } else {
            TestHarness
                    .setVerboseStream(new PrintStream(new VoidOutputStream()));
            TestHarness.setVerbose(false);
            CastorTestCase.setVerboseStream(new PrintStream(
                    new VoidOutputStream()));
            CastorTestCase.setVerbose(false);
        }
        
        addTest(setupHarness());
    }
    
    private TestHarness setupHarness() throws Exception {
        Unmarshaller unm;
        Harness harness;
        TestHarness testApp;
        Mapping mapping;

        unm = new Unmarshaller(Harness.class);
        mapping = new Mapping();
        mapping.loadMapping(Main.class.getResource("harness/mapping.xml"));
        unm.setMapping(mapping);
        if (testRes != null) {
            harness = (Harness) unm.unmarshal(new InputStreamReader(
                    Main.class.getResourceAsStream(testRes)));
        } else if (testFile != null) {
            harness = (Harness) unm.unmarshal(new InputStreamReader(
                    new FileInputStream(testFile)));
        } else if (testUrl != null) {
            harness = (Harness) unm.unmarshal(new InputStreamReader(
                    (new URL(testUrl)).openStream()));
        } else {
            harness = (Harness) unm.unmarshal(new InputStreamReader(
                    Main.class.getResourceAsStream(DEFAULT_FILE)));
        }
        testApp = harness.createTestHarness(testBranchs);
        return testApp;
    }
    
    /**
     * Quick hack to get this suite going at all ...
     */
    public void testBlah() {
        // nothing
    }
    
    /**
     * Setup and run the test suites according to the xml configuration file.
     * Make the setup itself run like a test, such that setup errors is
     * reported.
     */
    public void run(TestResult result) {
        try {
            TestHarness testApp = setupHarness();
            if (printInfo)
                testApp.printInfo(System.out, testBranchs);
            else
                testApp.run(result);
        } catch (Exception e) {
            result.addError(this, e);
        }
    }

    /**
     * Helper class that sallows all the verbose output
     */
    private static class VoidOutputStream extends OutputStream {
        public void write(int b) {
        }
    }

}
