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
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.engine.OQLQueryImpl;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;


/**
 * Test for generic key generators (MAX and HIGH/LOW).
 */
public class KeyGenGeneric
    extends CWTestCase
{


    private JDOCategory    _category;


    public KeyGenGeneric( CWTestCategory category )
        throws CWClassConstructorException
    {
        this( "TC41", "Key generators: MAX, HIGH/LOW", category );
    }


    public KeyGenGeneric( String name, String description, CWTestCategory category )
        throws CWClassConstructorException
    {
        super( name, description );
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
            db = _category.getDatabase( stream.verbose() );
            result = testAllKeyGens( stream, db );
            db.close();
        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }

    protected boolean testAllKeyGens( CWVerboseStream stream, Database db)
            throws Exception
    {
        return testOneKeyGen( stream, db, TestMaxObject.class, TestMaxExtends.class )
                && testOneKeyGen( stream, db, TestHighLowObject.class, TestHighLowExtends.class );
    }


    /**
     * The main goal of the test is to verify key generators in the case
     * of "extends" relation between two classes.
     * For each key generator we have a pair of classes: TestXXXObject and
     * TestXXXExtends which use key generator XXX.
     */
    protected boolean testOneKeyGen( CWVerboseStream stream, Database db,
                                  Class objClass, Class extClass )
            throws Exception
    {
        OQLQuery            oql;
        TestKeyGenObject    object;
        TestKeyGenObject    ext;
        QueryResults        enum;
        boolean             result;

        result = true;

        // Open transaction in order to perform JDO operations
        db.begin();

        // Create first object
        object = (TestKeyGenObject) objClass.newInstance();
        stream.writeVerbose( "Creating first object: " + object );
        db.create( object );
        stream.writeVerbose( "Created first object: " + object );

        // Create second object
        ext = (TestKeyGenObject) extClass.newInstance();
        stream.writeVerbose( "Creating second object: " + ext );
        db.create( ext );
        stream.writeVerbose( "Created second object: " + ext );

        db.commit();

        db.begin();

        // Find the first object and remove it 
        //object = (TestKeyGenObject) db.load( objClass, object.getId() );
        oql = db.getOQLQuery();
        oql.create( "SELECT object FROM " + objClass.getName() +
                       " object WHERE id = $1" );
        oql.bind( object.getId() );
        enum = oql.execute();
        stream.writeVerbose( "Removing first object: " + object );
        if ( enum.hasMore() ) {
            object = (TestKeyGenObject) enum.next();
            db.remove( object );
            stream.writeVerbose( "OK: Removed" );
        } else {
            stream.writeVerbose( "Error: Not found" );
            result = false;
        }

        // Find the second object and remove it
        //ext = (TestKeyGenObject) db.load( extClass, ext.getId() );
        oql = db.getOQLQuery();
        oql.create( "SELECT ext FROM " + extClass.getName() +
                       " ext WHERE id = $1" );
        oql.bind( ext.getId() );
        enum = oql.execute();
        stream.writeVerbose( "Removing second object: " + ext );
        if ( enum.hasMore() ) {
            ext = (TestKeyGenObject) enum.next();
            db.remove( ext );
            stream.writeVerbose( "OK: Removed" );
        } else {
            stream.writeVerbose( "Error: Not found" );
            result = false;
        }

        db.commit();

        return result;
    }


}
