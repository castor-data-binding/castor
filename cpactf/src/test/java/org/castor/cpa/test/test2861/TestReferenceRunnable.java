package org.castor.cpa.test.test2861;

import org.castor.cpa.test.framework.CPAThreadedTestRunnable;
import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class TestReferenceRunnable extends CPAThreadedTestRunnable {
    private final TestReferenceChange _test;
    
    public TestReferenceRunnable(final TestReferenceChange test) {
        _test = test;
    }
    
    @Override
    public void runTest() throws Throwable {
        _test.executeQuery();
    }
}
