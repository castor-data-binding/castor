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
 * $Id $
 */
package jdo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Enumeration;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import org.xml.sax.InputSource;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.jdo.conf.Database;
import org.exolab.castor.jdo.conf.JdoConf;
import org.exolab.castor.jdo.conf.Param;
import org.exolab.castor.jdo.engine.DatabaseRegistry;
import org.exolab.castor.util.DTDResolver;

/**
 * BatchUpdateSQL is a command line utility that helps a user to execute a 
 * batch of SQL update statements. It takes a connection configuration file 
 * which is same as the one used in Castor JDO and a file contains SQL update
 * statements.
 *
 * @author <a href="yip@intalio.com">Thomas Yip</a>
 */
public class BatchUpdateSQL {
    private static boolean verbose;
    private static int cur;
    private static boolean skipgrants;
    private static boolean fromIS;

    public static void printUsage() {
        System.out.println( "Usage: BatchUpdateSQL [-verbose] [-skipgrants] [-pipe] connection [createscript]" );
        System.out.println( "    connection     -- the url of the connection configuration in xml format" );
        System.out.println( "    createscript   -- the url of SQL DDL for database tables" );
        System.out.println( "    verbose        -- enable verbose output" );
        System.out.println( "    skipgrants     -- skip grants statements in the files" );
        System.out.println( "    pipe           -- create script is piped" );
    }
    public static void main( String args[] ) throws Exception {
        while ( cur < args.length && args[cur].startsWith( "-" ) ) {
            if ( args[cur].equals("-verbose") ) {
                verbose  = true;
            } else if ( args[cur].equals("-skipgrants") ) {
                skipgrants = true;
            } else if ( args[cur].equals("-pipe") ) {
                fromIS = true;
            }
            cur++;
        }
        if ( (fromIS && cur >= args.length) || (!fromIS && cur >= (args.length-1)) ) {
            printUsage();
            return;
        }
        if ( (fromIS && cur < (args.length-1)) || (!fromIS && cur < (args.length-2)) ) {
            System.out.print( "argument(s) ignored:\t" );
            for ( int i = (cur+(fromIS?1:2)); i < args.length; i++ ) {
                System.out.print( args[i] + "\t" );
            }
            System.out.println();
        }

        Connection c = getConnection( args[cur] );
        Statement stat = c.createStatement();
        BufferedReader r;
        if ( !fromIS ) {
            r = new BufferedReader( 
                   new InputStreamReader( (new URL(args[cur+1])).openStream() ) );
        } else {
            r = new BufferedReader( new InputStreamReader( System.in ) );
        }
        StringBuffer sb = new StringBuffer();
        String line = null;
        ArrayList errorsSummary = new ArrayList();
        while ( (line = r.readLine()) != null ) {
            line = line.trim();
            
            // ignore comment
            if ( line.startsWith("--") )
                continue;

            // Sybase style of ending token
            if ( line.toLowerCase().startsWith("go") && sb.length() > 0 ) {
                String s = sb.toString();
                if ( skipgrants && (s.toLowerCase().startsWith("grant")) ) {
                    sb.setLength(0);
                    continue;
                }
                // execute the statement
                if ( verbose )
                    System.out.println( "Executing: "+s+" " );
                try {
                    int i = stat.executeUpdate( s );
                    if ( verbose )
                        System.out.println( "    Result: "+i );
                } catch ( SQLException e ) {
                    if ( verbose )
                        System.out.println( "    Exception: "+e.toString() );
                    errorsSummary.add( "Executing: "+s+" " );
                    errorsSummary.add( "    Exception: "+e.toString() );
                    errorsSummary.add( "" );
                }
                if ( verbose )
                    System.out.println();
                sb.setLength( 0 );
                continue;
            }

            // multiple line store procedure for oracle
            if ( line.startsWith("/") ) {
                if ( sb.length()>0 && sb.charAt(0) == '/' ) {
                    String s = sb.substring( 1, sb.length() ).trim();
                    // execute the statement
                    if ( verbose )
                        System.out.println( "Executing: "+s+" " );
                    try {
                        int i = stat.executeUpdate( s );
                        if ( verbose )
                            System.out.println( "    Result: "+i );
                    } catch ( SQLException e ) {
                        if ( verbose )
                            System.out.println( "    Exception: "+e.toString() );
                        errorsSummary.add( "Executing: "+s+" " );
                        errorsSummary.add( "    Exception: "+e.toString() );
                        errorsSummary.add( "" );
                    }
                    if ( verbose )
                        System.out.println();
                    sb.setLength( 0 );
                    continue;
                }
                sb.append( line );
                continue;
            }

            // ending tokens for most DDL syntax
            if ( line.endsWith(";") ) {
                if ( sb.length() > 0 )
                    sb.append(" ");
                sb.append( line );
                sb.deleteCharAt( sb.length() - 1 );

                // creating oracle call procedure
                String s = sb.toString();
                if ( s.startsWith("CREATE OR REPLACE") ) {
                    if ( s.indexOf("END") == -1 ) {
                        sb.append("; ");
                        continue;
                    }
                } 
                if ( s.startsWith("/") ) {
                    sb.append("; ");
                    continue;
                }

                // skip grant statements
                if ( skipgrants && (s.toLowerCase().startsWith("grant")) ) {
                    sb.setLength(0);
                    continue;
                }

                // execute the statement
                if ( verbose )
                    System.out.println( "Executing: "+s+" " );
                try {
                    int i = stat.executeUpdate( s );
                    if ( verbose )
                        System.out.println( "    Result: "+i );
                } catch ( SQLException e ) {
                    if ( verbose )
                        System.out.println( "    Exception: "+e.toString() );
                        errorsSummary.add( "Executing: "+s+" " );
                        errorsSummary.add( "    Exception: "+e.toString() );
                        errorsSummary.add( "" );
                }
                if ( verbose )
                System.out.println();
                sb.setLength( 0 );
                continue;
            }
            if ( sb.length() > 0 )
                sb.append(" ");
            sb.append( line );
        }
        if ( errorsSummary.size() != 0 || sb.length() != 0 ) {
            System.out.println();
            System.out.println();
            System.out.println( "Tasks completed with error(s)" );
            System.out.println( "Errors Summary:" );
            System.out.println( "=============================================" );
            for ( int i = 0; i < errorsSummary.size(); i++ ) {
                System.out.println( errorsSummary.get(i) );
            }
            if ( sb.length() != 0 ) {
                System.out.println("Unexpected EOF reached. Expecting: end of statement token(ie. \";\" or \"go\").");
                System.out.println("Statement is not executed: "+sb.toString());
            }
        } else {
            System.out.println();
            System.out.println();
            System.out.println( "All tasks completed successfully" );
        }

    }

    // copy from DatabaseRegistry and modified
    public static Connection getConnection( String url )
            throws Exception {

        Unmarshaller       unm;
        JdoConf            jdoConf;

        // non-used field
        unm = new Unmarshaller( JdoConf.class );
        // Load the JDO database configuration file from the specified
        // input source. If the database was already configured, ignore
        // this file (allowing multiple loadings).
        InputSource source = new InputSource( (new URL(url)).openStream() );
        unm.setEntityResolver( new DTDResolver() );

        jdoConf = (JdoConf) unm.unmarshal( source );

        if ( jdoConf.getDatabase()[0].getDatabaseChoice().getDriver() != null ) {
            // JDO configuration file specifies a driver, use the driver
            // properties to create a new registry object.
            Properties  props;
            Enumeration params;
            Param       param;

            if ( jdoConf.getDatabase()[0].getDatabaseChoice().getDriver().getClassName() != null ) {
                Class.forName( jdoConf.getDatabase()[0].getDatabaseChoice().getDriver().getClassName() ).newInstance();
            }
            if ( DriverManager.getDriver( jdoConf.getDatabase()[0].getDatabaseChoice().getDriver().getUrl() ) == null )
                throw new RuntimeException( "jdo.missingDriver" );

            props = new Properties();
            params = jdoConf.getDatabase()[0].getDatabaseChoice().getDriver().enumerateParam();
            while ( params.hasMoreElements() ) {
                param = (Param) params.nextElement();
                props.put( param.getName(), param.getValue() );
            }

            return DriverManager.getConnection( jdoConf.getDatabase()[0].getDatabaseChoice().getDriver().getUrl(), props );

        } else if ( jdoConf.getDatabase()[0].getDatabaseChoice().getDataSource() != null ) {
            // JDO configuration file specifies a DataSource object, use the
            // DataSource which was configured from the JDO configuration file
            // to create a new registry object.
            DataSource ds;

            ds = DatabaseRegistry.loadDataSource(jdoConf.getDatabase()[0], BatchUpdateSQL.class.getClassLoader());
            // ds = (DataSource) jdoConf.getDatabase()[0].getDatabaseChoice().getDataSource().getParams();
            if ( ds == null )
                throw new RuntimeException( "jdo.missingDataSource" );
            return ds.getConnection();
        } else if ( jdoConf.getDatabase()[0].getDatabaseChoice().getJndi() != null ) {
            // JDO configuration file specifies a DataSource lookup through JNDI,
            // locate the DataSource object frome the JNDI namespace and use it.
            Object    ds;

            ds = new InitialContext().lookup( jdoConf.getDatabase()[0].getDatabaseChoice().getJndi().getName() );
            if ( ! ( ds instanceof DataSource ) )
                throw new RuntimeException( "jdo.jndiNameNotFound" );

            return ((DataSource)ds).getConnection();
        } else {
            throw new RuntimeException( "jdo.missingDataSource" );
        }
    }
}