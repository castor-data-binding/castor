package org.castor.cpa.test.test2861;

import org.castor.cpa.test.framework.CPAThreadedTestRunnable;
import org.junit.Ignore;

/**
 * @author cwichoski
 */
@Ignore
public class TestReferenceRunnable extends CPAThreadedTestRunnable {
    private final TestReferenceChange _test;
	private boolean _readOnly;
    
    public TestReferenceRunnable(final TestReferenceChange test, boolean readOnly) {
        _test = test;
        _readOnly = readOnly;
    }
    
    @Override
    public void runTest() throws Throwable {
    	if (_readOnly) {
    		_test.executeQueryReadOnly();
    	} else {
    		_test.executeQuery();	
    	}
    }
}
