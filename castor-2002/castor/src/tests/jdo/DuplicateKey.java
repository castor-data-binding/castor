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


import java.io.IOException;
import java.util.Enumeration;
import org.exolab.castor.jdo.DataObjects;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.exceptions.CWClassConstructorException;


/**
 * Tests for duplicate key detection. Tests both duplicate key detection
 * in memory and in the database.
 */
public class DuplicateKey
    extends CWTestCase
{


    private Database       _db;


    public DuplicateKey()
        throws CWClassConstructorException
    {
        super( "TC03", "Test duplicate key" );
        try {
            _db = JDOTests.getDatabase();
        } catch ( Exception except ) {
            throw new CWClassConstructorException( except.toString() );
        }
    }


    public void preExecute()
    {
        super.preExecute();
    }


    public void postExecute()
    {
        super.postExecute();
    }


    public boolean run( CWVerboseStream stream )
    {
        boolean result = true;

        try {
            OQLQuery      oql;
            TestObject    object;
            Enumeration   enum;
            
            // Open transaction in order to perform JDO operations
            _db.begin();
            
            // Determine if test object exists, if not create it.
            // If it exists, set the name to some predefined value
            // that this test will later override.
            oql = _db.getOQLQuery( "SELECT object FROM jdo.TestObject object WHERE id = $1" );
            oql.bind( TestObject.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                object = (TestObject) enum.nextElement();
                stream.writeVerbose( "Updating object: " + object );
            } else {
                object = new TestObject();
                stream.writeVerbose( "Creating new object: " + object );
                _db.create( object );
            }
            _db.commit();
            

            // Attempt to create a new object with the same identity,
            // while one is in memory. Will report duplicate key from
            // the cache engine.
            _db.begin();
            oql.bind( new Integer( TestObject.DefaultId ) );
            enum = oql.execute();
            while ( enum.hasMoreElements() )
                enum.nextElement();
            
            object = new TestObject();
            stream.writeVerbose( "Creating new object: " + object );
            stream.writeVerbose( "Will report duplicate identity from cache engine" );
            try {
                _db.create( object );
                result = false;
                stream.writeVerbose( "Error: DuplicateIdentityException not thrown" );
            } catch ( DuplicateIdentityException except ) {
                stream.writeVerbose( "OK: DuplicateIdentityException thrown" );
            } catch ( Exception except ) {
                result = false;
                stream.writeVerbose( "Error: " + except );
            }
            _db.commit();

	    
            // Attempt to create a new object with the same identity,
            // in the database. Will report duplicate key from SQL engine.
            _db.begin();
            object = new TestObject();
            stream.writeVerbose( "Creating new object: " + object );
            stream.writeVerbose( "Will report duplicate identity from SQL engine" );
            try {
                _db.create( object );
                result = false;
                stream.writeVerbose( "Error: DuplicateIdentityException not thrown" );
            } catch ( DuplicateIdentityException except ) {
                stream.writeVerbose( "OK: DuplicateIdentityException thrown" );
            } catch ( Exception except ) {
                // result = false;
                stream.writeVerbose( "Error: " + except );
            }
            _db.commit();
            _db.close();
        } catch ( Exception except ) {
            try {
                stream.writeVerbose( "Error: " + except );
            } catch ( IOException except2 ) { }
            except.printStackTrace();
            result = false;
        }
        return result;
    }


}
