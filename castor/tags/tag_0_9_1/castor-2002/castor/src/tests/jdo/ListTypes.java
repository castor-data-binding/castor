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


package jdo;


import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Collection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.lang.Math;
import java.util.Vector;
import java.util.Random;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;
import java.util.ArrayList;


/**
 * Concurrent access test. Tests a JDO modification and concurrent
 * JDBC modification to determine if JDO can detect the modification
 * with dirty checking.
 */
public class ListTypes extends CWTestCase {

    private Database       _db;


    private Connection     _conn;


    private JDOCategory    _category;


    public ListTypes( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC60", "List SQL Types" );
        _category = (JDOCategory) category;
    }

    public void preExecute()
    {
        super.preExecute();
    }

    public void postExecute()
    {
        super.postExecute();
    }


    public boolean run( CWVerboseStream stream ) {
        boolean result = true;
        try {
            _db = _category.getDatabase( stream.verbose() );
            _conn = _category.getJDBCConnection(); 

			System.out.println("ARRAY"+" : "+java.sql.Types.ARRAY);
			System.out.println("BIGINT"+" : "+java.sql.Types.BIGINT);
			System.out.println("BINARY"+" : "+java.sql.Types.BINARY);
			System.out.println("BIT"+" : "+java.sql.Types.BIT);
			System.out.println("BLOB"+" : "+java.sql.Types.BLOB);
			System.out.println("CHAR"+" : "+java.sql.Types.CHAR);
			System.out.println("CLOB"+" : "+java.sql.Types.CLOB);
			System.out.println("DATE"+" : "+java.sql.Types.DATE);
			System.out.println("DECIMAL"+" : "+java.sql.Types.DECIMAL);
			System.out.println("DISTINCT"+" : "+java.sql.Types.DISTINCT);
			System.out.println("DOUBLE"+" : "+java.sql.Types.DOUBLE);
			System.out.println("FLOAT"+" : "+java.sql.Types.FLOAT);
			System.out.println("INTEGER"+" : "+java.sql.Types.INTEGER);
			System.out.println("JAVA"+" : "+java.sql.Types.JAVA_OBJECT);
			System.out.println("LONGVARBINARY"+" : "+java.sql.Types.LONGVARBINARY);
			System.out.println("LONGVARCHAR"+" : "+java.sql.Types.LONGVARCHAR);
			System.out.println("NULL"+" : "+java.sql.Types.NULL);
			System.out.println("NUMERIC"+" : "+java.sql.Types.NUMERIC);
			System.out.println("OTHER"+" : "+java.sql.Types.OTHER);
			System.out.println("REAL"+" : "+java.sql.Types.REAL);
			System.out.println("REF"+" : "+java.sql.Types.REF);
			System.out.println("SMALLINT"+" : "+java.sql.Types.SMALLINT);
			System.out.println("STRUCT"+" : "+java.sql.Types.STRUCT);
			System.out.println("TIME"+" : "+java.sql.Types.TIME);
			System.out.println("TIMESTAMP"+" : "+java.sql.Types.TIMESTAMP);
			System.out.println("TINYINT"+" : "+java.sql.Types.TINYINT);
			System.out.println("VARBINARY"+" : "+java.sql.Types.VARBINARY);
			System.out.println("VARCHAR"+" : "+java.sql.Types.VARCHAR);

			String query = "SELECT * from list_types";
			
			// Create our table
            Statement statement = _conn.createStatement();

            ResultSet rs = statement.executeQuery (query);
            // Get the ResultSetMetaData.  This will be used for
            // the column headings
            ResultSetMetaData rsmd = rs.getMetaData ();

            // Get the number of columns in the result set

            int numCols = rsmd.getColumnCount ();

            // Display column headings

            for (int i=1; i<=numCols; i++) 
            {
                if (i > 1) 
                System.out.print(rsmd.getColumnLabel(i));
				System.out.print("     :");
                System.out.print(rsmd.getColumnType(i));
				System.out.print(":");
				System.out.print(rsmd.getColumnTypeName(i));
				System.out.println();
            }
            System.out.println();

            rs.close();

			statement.executeUpdate("DELETE list_types");

			PreparedStatement pstat = _conn.prepareStatement("INSERT INTO list_types ( o_varchar, o_long, o_clob ) VALUES (?,?,?)");
			//char[] something = {0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x47, 0x46, 0x45, 0x44, 0x43, 0x42, 0x41};
			String something = "Hello world";
			pstat.setString( 1, something );
			pstat.setString( 2, something );
			pstat.setString( 3, something );
			pstat.execute();

			rs = statement.executeQuery("SELECT o_varchar, o_long, o_clob FROM list_types");
			/*
			while ( rs.next() ) {
				System.out.println("start");
				InputStream is = rs.getBinaryStream(1);
				System.out.println("get binaray stream");
				rs.getBytes(1);
				byte[] out = rs.getBytes(1);
				for ( int i=0; i < out.length; i++ ) {
					System.out.print( out[i]+" " );
				}				
				System.out.println("get bytes 1");
				rs.getBinaryStream(2);
				System.out.println("get binary stream 2");
				out = rs.getBytes(2);
				for ( int i=0; i < out.length; i++ ) {
					System.out.print( out[i]+" " );
				}
				System.out.println("get bytes");

				rs.getBinaryStream(3);
				System.out.println("get binaray stream 3");
				out = rs.getBytes(3);
				for ( int i=0; i < out.length; i++ ) {
					System.out.print( out[i]+" " );
				}
				System.out.println("get bytes 3");
			} */
			while ( rs.next() ) {
			    System.out.println( "1: "+rs.getString(1) + "<" );
				System.out.println( "2: "+rs.getString(2) + "<" );
				System.out.println( "3: "+rs.getString(3) + "<" );
				System.out.println("3:"+rs.getClob(3)+"<");
				Reader r = rs.getClob(3).getCharacterStream();
				int c = r.read();
				while ( c != -1 ) {
				    System.out.print((char)c+" ");
					c = r.read();
				}
			}


			_conn.close();
        } catch ( Exception e ) {
            result = false;
            e.printStackTrace();
        }
        return true;
    }
}


