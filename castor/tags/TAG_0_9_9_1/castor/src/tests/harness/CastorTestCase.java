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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $$
 */


package harness;


import java.io.PrintStream;

import jdo.JDOCategory;
import junit.framework.TestCase;
import junit.framework.TestResult;

import org.exolab.castor.jdo.engine.DatabaseImpl;

public class CastorTestCase extends TestCase {

    private String description;

    private TestHarness suite;

    private String name;

    public static PrintStream stream;

    public static boolean verbose;

    public CastorTestCase( TestHarness suite, String name, String description ) {
        super( name );
        setName( name );
        setDescription( description );
        setSuite( suite );
    }

    /**
	 * @param name
	 */
	public CastorTestCase(String name) {
        super (name);
	}

    private void clearCache() {
        try {
            if (suite instanceof JDOCategory) {
                DatabaseImpl db = (DatabaseImpl) ((JDOCategory) suite).getDatabase();
                db.getCacheManager().expireCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static void setVerboseStream( PrintStream verboseStream ) {
        stream = verboseStream;
    }
    public static PrintStream getVerboseStream() {
        return stream;
    }
    public static void setVerbose( boolean vb ) {
        verbose = vb;
    }
    public static boolean getVerbose() {
        return verbose;
    }
    public void setName( String name ) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription( String desc ) {
        this.description = desc;
    }
    public void setSuite( TestHarness superTest ) {
        this.suite = superTest;
    }
    public TestHarness getSuite() {
        return suite;
    }
    public void printInfo( PrintStream ps ) {
        printInfo( ps, null );
    }
    public void printInfo( PrintStream ps, String branch ) {
        //System.out.println( " branch: " + branch + " name: " + getName()  );
        if ( branch == null || branch.equals("") || branch.startsWith( getName() ) ) {
        	// XXX [SMH]: sub is never used, delete?
            //String sub = null;
            //if ( branch != null && branch.startsWith( getName() ) )
            //    sub = branch.substring( branch.indexOf(".")==-1?branch.length():branch.indexOf(".")+1 );
            StringBuffer sb = new StringBuffer();
            sb.append( getName() );
            TestHarness upper = suite;
            while ( upper != null ) {
                sb.insert( 0, "." );
                sb.insert( 0, upper.getName() );
                upper = upper.getSuite();
            }
            sb.insert( 0, "[" );
            sb.append( "]" );
            sb.append( ' ' );
            sb.append( description );
            sb.append( '\n' );
            ps.print( sb.toString() );
        }
    }
    public void run( TestResult result ) {
        System.out.println();
        System.out.print( "Test: "+getName()+" "+getDescription()+" " );
        super.run( result );
		clearCache(); // XXX [SMH]: call to clearCache() added
    }
    public void run( TestResult result, String branch ) {
        System.out.println( "Test: "+getName()+" Branch: "+branch );
        if ( branch == null || branch.equals("") )
            run( result );
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append( getName() );
        TestHarness upper = suite;
        while ( upper != null ) {
            sb.insert( 0, "." );
            sb.insert( 0, upper.getName() );
            upper = upper.getSuite();
        }
        sb.insert( 0, "[" );
        sb.append( "]" );
        sb.append( ' ' );
        sb.append( description );
        sb.append( '\n' );

        return sb.toString();
    }
}
