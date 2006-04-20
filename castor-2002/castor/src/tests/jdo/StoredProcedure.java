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
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;


/**
 */
public class StoredProcedure
    extends CWTestCase
{

    private final String USER1 = "user1"; 
    private final String USER2 = "user2";
    private final String GROUP1 = "group1";
    private final String GROUP2 = "group2";

    private JDOCategory    _category;


    public StoredProcedure( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC48", "Stored procedure query" );
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
            Enumeration   enum;
            int resCnt;
            
            // Open transaction in order to perform JDO operations
            db = _category.getDatabase( stream.verbose() );
            db.begin();
            
            // Remove all objects.
            // Then create three objects with the given field values.
            oql = db.getOQLQuery( "SELECT object FROM jdo.TestObject object" );
            enum = oql.execute();
            while ( enum.hasMoreElements() ) {
                object = (TestObject) enum.nextElement();
                db.remove( object );
            }
            db.commit();

            db.begin();
            object = new TestObject();
            object.setId( 1 );
            object.setValue1( USER1 );
            stream.writeVerbose( "Creating the first new object: " + object );
            db.create( object );

            object = new TestObject();
            object.setId( 2 );
            object.setValue1( USER1 );
            object.setValue2( GROUP2 );
            stream.writeVerbose( "Creating the second new object: " + object );
            db.create( object );

            object = new TestObject();
            object.setId( 3 );
            object.setValue1( USER2 );
            object.setValue2( GROUP1 );
            stream.writeVerbose( "Creating the second new object: " + object );
            db.create( object );

            db.commit();

            // Now get the created objects using the stored procedure
            // The stored procedure sp_check_permission stands for some
            // application-specific algorithm of checking permissions and 
            // returns the set of all objects to which access is granted.
            // In this test we assume that value1 holds the name of the user
            // who created the object, and value2 holds the name of the group
            // who has read access to the object. We pass the user name "user1" 
            // and the name of his group "group1" and fetch sequencially two
            // result sets: one by the rule "creator can access object", second
            // by the rule "read access group".
            // We expect to fetch objects with identities 1,2,3.
            db.begin();
            oql = db.getOQLQuery( "CALL proc_check_permissions($,$) AS jdo.TestObject" );
            oql.bind( USER1 );
            oql.bind( GROUP1 );
            enum = oql.execute();

            resCnt = 0;
            for (int i = 1; enum.hasMoreElements(); i++ ) {
                object = (TestObject) enum.nextElement();
                stream.writeVerbose( "Fetched object: " + object );
                resCnt++;
            }
            if ( resCnt != 3 ) {
                stream.writeVerbose( "Error: Wrong number of objects in the result" );
                result = false;
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
