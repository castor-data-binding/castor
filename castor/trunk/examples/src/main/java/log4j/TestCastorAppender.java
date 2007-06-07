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
package log4j;

import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test for <code>CastorAppender</code> to showcase its usage.
 *
 * @author  <a href="mailto:holger.west@syscon-informatics.de">Holger West</a>
 */
public final class TestCastorAppender extends TestCase {
    // -----------------------------------------------------------------------------------
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging </a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestCastorAppender.class);
    
    /** Example JDO configuration. */
    private static final String JDO_CONF = "/log4j/example-jdo-conf.xml";
    
    /** Example LOG4J configuration. */
    private static final String XML_CONF = "/log4j/example-log4j.xml";
    
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
    public TestCastorAppender(final String name) { 
        super(name);
    }
    
    // -----------------------------------------------------------------------------------
    
    public void setUp () throws Exception {
        JDOManager.loadConfiguration(
                TestCastorAppender.class.getResource(JDO_CONF).toString());
        JDOManager jdo = JDOManager.createInstance("LOGGING");
        Database db = jdo.getDatabase();
        db.begin();
        Connection connection = db.getJdbcConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE LOG_EXTENSION");
        statement.executeUpdate("DROP TABLE LOG_EXCEPTION");
        statement.executeUpdate("DROP TABLE LOG");
        
        statement.executeUpdate("CREATE TABLE LOG (LOG_ID INT NOT NULL, " + 
                " LOG_TIMESTAMP   TIMESTAMP        NOT NULL, " + 
                " LOG_CLASS       VARCHAR  ( 100)  NOT NULL, " +  
                " LOG_LEVEL       VARCHAR  (  10)  NOT NULL, " +   
                " LOG_THREAD      VARCHAR  ( 100)  NOT NULL, " +   
                " LOG_MESSAGE     VARCHAR  (1000)  DEFAULT NULL, " + 
                " LOG_COUNT       INT              NOT NULL)");
        statement.executeUpdate("ALTER TABLE LOG ADD PRIMARY KEY (LOG_ID)");
        statement.executeUpdate("CREATE TABLE LOG_EXCEPTION ( " + 
            " LOGE_ID         INT              NOT NULL, " + 
            " LOGE_LOG_ID     INT              NOT NULL, " + 
            " LOGE_STACKTRACE BLOB             NOT NULL)");
        statement.executeUpdate("ALTER TABLE LOG_EXCEPTION ADD PRIMARY KEY (LOGE_ID)");
        statement.executeUpdate("ALTER TABLE LOG_EXCEPTION ADD CONSTRAINT FK_LOGE_LOG_ID" + 
                " FOREIGN KEY (LOGE_LOG_ID) REFERENCES LOG (LOG_ID)");
        statement.executeUpdate("CREATE TABLE LOG_EXTENSION (LOGX_LOG_ID INT NOT NULL, LOGX_TYPE VARCHAR(100) NOT NULL, LOGX_VALUE VARCHAR(100) NOT NULL)");
        statement.executeUpdate("ALTER TABLE LOG_EXTENSION ADD PRIMARY KEY (LOGX_LOG_ID)");
        statement.executeUpdate("ALTER TABLE LOG_EXTENSION ADD CONSTRAINT FK_LOGX_LOG_ID " +
                " FOREIGN KEY (LOGX_LOG_ID) REFERENCES LOG (LOG_ID)");
        
        statement.executeQuery("select count(*) from LOG");
        db.commit();
        db.close();
    }
    
    /**
     * Test CastorAppender for LOG4J.
     * 
     * @throws Exception If anything went wrong in the test.
     */
    public void testAppender() throws Exception {
//        JDOManager.loadConfiguration(
//                TestCastorAppender.class.getResource(JDO_CONF).toString());

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
