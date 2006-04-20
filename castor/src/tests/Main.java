/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.FileInputStream;
import java.net.URL;

import junit.framework.TestResult;
// harness is the root of the test configuration
import harness.Harness;
import harness.TestHarness;
import harness.CastorTestCase;

// for using castor to read tests.xml into data object
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.mapping.Mapping;


public class Main extends TestHarness {

    private final static String DEFAULT_FILE = "tests.xml";

    /**
     * Arguments
     */
    private String testBranchs;

    /**
     * Indicates all the test cases run in verbose mode
     */
    private boolean verbose;

    /**
     * Indicates we only display information of test case, 
     * but not running them.
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

    /**
     * Constructor
     */
    private Main( boolean verbose, boolean printInfo, boolean gui, String testRes, String testFile, String testUrl, String tests ) {
        super(null,"Castor","Root");
        this.verbose     = verbose;
        this.printInfo   = printInfo;
        this.testBranchs = tests;
        this.testFile    = testFile;
        this.testUrl     = testUrl;
        this.testRes     = testRes;
        if ( verbose ) {
            TestHarness.setVerboseStream( System.out );
            TestHarness.setVerbose( true );
            CastorTestCase.setVerboseStream( System.out );
            CastorTestCase.setVerbose( true );
        } else {
            TestHarness.setVerboseStream( new PrintStream( new VoidOutputStream() ) );
            TestHarness.setVerbose( false );
            CastorTestCase.setVerboseStream( new PrintStream( new VoidOutputStream() ) );
            CastorTestCase.setVerbose( false );
        }
    }

    /**
     * Setup and run the test suites according to the xml configuration file.
     * Make the setup itself run like a test, such that setup errors is 
     * reported.
     */
    public void run( TestResult result ) {
        try {
            Unmarshaller      unm;
            Harness           harness;
            TestHarness       testApp;
            Mapping           mapping;
            
            unm = new Unmarshaller( Harness.class );
            mapping = new Mapping();
            mapping.loadMapping( Main.class.getResource( "harness/mapping.xml" ) );
            unm.setMapping( mapping );
            if ( testRes != null ) {
                harness = (Harness) unm.unmarshal( new InputStreamReader( Main.class.getResourceAsStream( testRes ) ) );
            } else if ( testFile != null ) {
                harness = (Harness) unm.unmarshal( new InputStreamReader( new FileInputStream( testFile ) ) );
            } else if ( testUrl != null ) {
                harness = (Harness) unm.unmarshal( new InputStreamReader( (new URL(testUrl)).openStream() ) );
            } else {
                harness = (Harness) unm.unmarshal( new InputStreamReader( Main.class.getResourceAsStream( DEFAULT_FILE ) ) );
            }
            testApp = harness.createTestHarness( testBranchs );
            if ( printInfo )
                testApp.printInfo( System.out, testBranchs );
            else
                testApp.run( result );
        } catch ( Exception e ) {
            result.addError( this, e );
        }
    }

    /**
     * The main method. 
     * Usage: test [-verbose] [-info] [-gui] [-res test.xml | -file test.xml | -url urlOfTest] testcases
     */
    public static void main( String args[] ) {
        int     cur       = 0;
        boolean infoOnly  = false;
        boolean verbose   = false;
        boolean gui       = false;
        String  file      = null;
        String  url       = null;
        String  res       = null;

        if ( args.length == 0 ) {
            usage();
            return;
        }

        while ( cur < args.length && args[cur].startsWith( "-" ) ) {
            if ( args[cur].equals("-verbose") ) {
                verbose  = true;
            } else if ( args[cur].equals("-info") ) {
                infoOnly = true;
            } else if ( args[cur].equals("-gui") ) {
                gui = true;
            } else if ( args[cur].equals("-file") ) {
                cur++;
                if ( cur < args.length && args[cur] != null && !args[cur].trim().equals("") )
                    file = args[cur].trim();
                else {
                    usage();
                    return;
                }
            } else if ( args[cur].equals("-url") ) {
                cur++;
                if ( cur < args.length && args[cur] != null && !args[cur].trim().equals("") )
                    url = args[cur].trim();
                else {
                    usage();
                    return;
                }
            } else if ( args[cur].equals("-res") ) {
                cur++;
                if ( cur < args.length && args[cur] != null && !args[cur].trim().equals("") )
                    res = args[cur].trim();
                else {
                    usage();
                    return;
                }
            }
            cur++;
        }
        if ( cur >= args.length ) {
            if ( !infoOnly ) {
                usage();
                return;
            } else {
                junit.textui.TestRunner.run( new Main( verbose, infoOnly, gui, res, file, url, null ) );
                return;

            }
        }
        if ( cur < (args.length-1) ) {
            System.out.println( "argument(s) ignored:" );
            for ( int i = (cur+1); i < args.length; i++ ) {
                System.out.println( args[i] + "\t" );
            }
        }
        junit.textui.TestRunner.run( new Main( verbose, infoOnly, gui, res, file, url, args[cur] ) );
    }

    /**
     * Print the usage
     */
    private static void usage() {
        System.out.println( "Usage: test [-verbose] [-info] [-gui] [-res test.xml | -file test.xml | -url urlOfTest] testcases" );
    }

    /**
     * Helper class that sallows all the verbose output
     */
    private static class VoidOutputStream extends OutputStream {
        public void write( int b ) {
        }
    }

}
