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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package jdo;


import java.util.Vector;
import java.util.Enumeration;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.util.Logger;
import org.exolab.jtf.CWTestCategory;
import org.exolab.jtf.CWTestCase;
import org.exolab.exceptions.CWClassConstructorException;



public class JDOTests
    extends CWTestCategory
{


    public static final String DatabaseFile = "database.xml";


    public JDOTests()
        throws CWClassConstructorException
    {
        super( "jdo", "JDO Tests");
        
        CWTestCase tc;
        
        tc = new Concurrent();
        add( tc.name(), tc, true );
        tc = new Deadlock();
        add( tc.name(), tc, true );
        tc = new DuplicateKey();
        add( tc.name(), tc, true );
        tc = new ReadOnly();
        add( tc.name(), tc, true );
    }


    public static Database getDatabase()
        throws PersistenceException
    {
        JDO jdo;

        jdo = new JDO();
        jdo.setConfiguration( JDOTests.class.getResource( DatabaseFile ).toString() );
        jdo.setDatabaseName( "test" );
        jdo.setLogWriter( Logger.getSystemLogger() );
        return jdo.getDatabase();
    }


    public static Connection getJDBCConnection()
        throws SQLException
    {
        String driverClass;

        driverClass = "postgresql.Driver";
        try {
            Class.forName( driverClass );
        } catch ( ClassNotFoundException except ) {
            throw new RuntimeException( except.toString() );
        }

        String jdbcUri;

	jdbcUri = "jdbc:postgresql:test?user=test&password=test";
        return DriverManager.getConnection( jdbcUri );
    }



}
