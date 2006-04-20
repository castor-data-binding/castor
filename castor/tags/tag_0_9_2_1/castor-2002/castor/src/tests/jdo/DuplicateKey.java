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
import java.util.Enumeration;
import org.exolab.castor.jdo.DataObjects;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;
import org.exolab.castor.persist.OID;

/**
 * Tests for duplicate key detection. Tests both duplicate key detection
 * in memory and in the database.
 */
public class DuplicateKey
    extends CWTestCase
{


    private JDOCategory    _category;



    public DuplicateKey( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC01", "Duplicate key detection" );
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


    public boolean run( CWVerboseStream stream )
    {
        boolean result = true;
        Database db;

        try {
            OQLQuery      oql;
            TestObject    object;
            QueryResults   enum;
            
            // Open transaction in order to perform JDO operations
            db = _category.getDatabase( stream.verbose() );
            db.begin();
            
            // Determine if test object exists, if not create it.
            // If it exists, set the name to some predefined value
            // that this test will later override.
            oql = db.getOQLQuery( "SELECT object FROM jdo.TestObject object WHERE id = $1" );
            oql.bind( TestObject.DefaultId );
            enum = oql.execute();
            if ( enum.hasMore() ) {
                object = (TestObject) enum.next();
                stream.writeVerbose( "Updating object: " + object );
            } else {
                object = new TestObject();
                stream.writeVerbose( "Creating new object: " + object );
                db.create( object );
            }
            db.commit();
            

            // Attempt to create a new object with the same identity,
            // while one is in memory. Will report duplicate key from
            // the cache engine.
            db.begin();
            oql.bind( new Integer( TestObject.DefaultId ) );
            enum = oql.execute();
            while ( enum.hasMore() )
                enum.next();
            
            object = new TestObject();
            stream.writeVerbose( "Creating new object: " + object );
            stream.writeVerbose( "Will report duplicate identity from cache engine" );
            try {
                db.create( object );
                result = false;
                stream.writeVerbose( "Error: DuplicateIdentityException not thrown" );
            } catch ( DuplicateIdentityException except ) {
                stream.writeVerbose( "OK: DuplicateIdentityException thrown" );
            } catch ( Exception except ) {
                result = false;
                stream.writeVerbose( "Error: " + except );
            }
            db.commit();

            //System.out.println("Second commit done!");
	    
            // Attempt to create a new object with the same identity,
            // in the database. Will report duplicate key from SQL engine.
            db.begin();
            object = new TestObject();
            stream.writeVerbose( "Creating new object: " + object );
            stream.writeVerbose( "Will report duplicate identity from SQL engine" );
            try {
                db.create( object );
                result = false;
                stream.writeVerbose( "Error: DuplicateIdentityException not thrown" );
            } catch ( DuplicateIdentityException except ) {
                stream.writeVerbose( "OK: DuplicateIdentityException thrown" );
            } catch ( Exception except ) {
                // result = false;
                stream.writeVerbose( "Error: " + except );
            }
            db.commit();
            db.close();
        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }


}
