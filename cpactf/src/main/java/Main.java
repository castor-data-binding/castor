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

public final class Main extends TestHarness {

    private static final String DEFAULT_FILE = "tests.xml";

    /** Arguments. */
    private String _testBranchs;

    /** Indicates we only display information of test case, but not running them. */
    private boolean _printInfo;

    /** The test file. */
    private String _testFile;

    /** The test url. */
    private String _testUrl;

    /** The test resource. */
    private String _testRes;

    public Main() throws Exception {
        this(false, false, false, null, null, null, "castor.mysql");
        String testCategory = System.getProperty("test.category", "castor.mysql");
        init (false, false, false, null, null, null, testCategory);
    }
    
    /**
     * Constructor.
     */
    public Main(final boolean verbose, final boolean printInfo, final boolean gui,
            final String testRes, final String testFile, final String testUrl, final String tests)
    throws Exception {
        super(null, "Castor", "Root");
        init (false, false, false, null, null, null, tests);
    }

    private void init(final boolean verbose, final boolean printInfo, final boolean gui,
            final String testRes, final String testFile, final String testUrl, final String tests)
    throws Exception {
        
        TestHarness._verbose = verbose;
        _printInfo = printInfo;
        _testBranchs = tests;
        _testFile = testFile;
        _testUrl = testUrl;
        _testRes = testRes;
        if (verbose) {
            TestHarness.setVerboseStream(System.out);
            TestHarness.setVerbose(true);
            CastorTestCase.setVerboseStream(System.out);
            CastorTestCase.setVerbose(true);
        } else {
            TestHarness.setVerboseStream(new PrintStream(new VoidOutputStream()));
            TestHarness.setVerbose(false);
            CastorTestCase.setVerboseStream(new PrintStream(new VoidOutputStream()));
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
        if (_testRes != null) {
            harness = (Harness) unm.unmarshal(new InputStreamReader(
                    Main.class.getResourceAsStream(_testRes)));
        } else if (_testFile != null) {
            harness = (Harness) unm.unmarshal(new InputStreamReader(
                    new FileInputStream(_testFile)));
        } else if (_testUrl != null) {
            harness = (Harness) unm.unmarshal(new InputStreamReader(
                    (new URL(_testUrl)).openStream()));
        } else {
            harness = (Harness) unm.unmarshal(new InputStreamReader(
                    Main.class.getResourceAsStream(DEFAULT_FILE)));
        }
        testApp = harness.createTestHarness(_testBranchs);
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
    public void run(final TestResult result) {
        try {
            TestHarness testApp = setupHarness();
            if (_printInfo) {
                testApp.printInfo(System.out, _testBranchs);
            } else {
                testApp.run(result);
            }
        } catch (Exception e) {
            result.addError(this, e);
        }
    }

    /**
     * Helper class that sallows all the verbose output.
     */
    private static class VoidOutputStream extends OutputStream {
        public void write(final int b) { }
    }
}
