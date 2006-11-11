/*
 * Copyright 2006 Holger West, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.tools.log4j;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;
import org.exolab.castor.jdo.JDOManager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test for <code>CastorAppender</code> to showcase its usage.
 *
 * @version $Id: TestExecutionContextFactory.java,v 1.3 2006/02/22 18:34:29 jens Exp $
 * @author  <a href="mailto:holger.west@syscon-informatics.de">holger</a>, 
 *          Syscon Ingenieurbüro für Mess- und Datentechnik GmbH.
 */
public final class TestCastorAppender extends TestCase {
    // -----------------------------------------------------------------------------------
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestCastorAppender.class);
    
    /** Example JDO configuration. */
    private static final String JDO_CONF = "/org/castor/tools/log4j/example-jdo-conf.xml";
    
    /** Example LOG4J configuration. */
    private static final String XML_CONF = "/org/castor/tools/log4j/example-log4j.xml";
    
    // -----------------------------------------------------------------------------------

    /**
     * Initialize LOG4J logging with example configuration and create JUNIT test suite.
     * 
     * @return JUNIT test suite.
     */
    public static Test suite() {
        // load specific resource for testing
        URL url = TestCastorAppender.class.getResource(XML_CONF);
        DOMConfigurator.configure(url);
        
        TestSuite suite = new TestSuite("CastorAppender tests");
        
        suite.addTest(new TestCastorAppender("testAppender"));
        
        return suite;
    }
    
    // -----------------------------------------------------------------------------------

    /**
     * Constructs a new TestCase with the given name.
     * 
     * @param name The name for the test.
     */
    public TestCastorAppender(final String name) { super(name); }
    
    // -----------------------------------------------------------------------------------
    
    /**
     * Test CastorAppender for LOG4J.
     * 
     * @throws Exception If anything went wrong in the test.
     */
    public void testAppender() throws Exception {
        JDOManager.loadConfiguration(
                TestCastorAppender.class.getResource(JDO_CONF).toString());

        try {
            Integer.parseInt("cc");
        } catch (Exception e) {
            LOG.error("exception", e);
        }
        LOG.error(null);
        LOG.warn("This is only a message");
        
        LogReferenceExtension ext = new LogReferenceExtension();
        ext.setMessage("this is an extension");
        ext.setType(this.getClass().toString());
        ext.setValue("123456789");
        LOG.error(ext);
        
        // flush the buffer before exit !!!!
        CastorAppender.flush();
    }

    // -----------------------------------------------------------------------------------
}
