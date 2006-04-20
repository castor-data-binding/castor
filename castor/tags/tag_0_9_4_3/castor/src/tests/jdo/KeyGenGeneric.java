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

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Assert;
import harness.TestHarness;
import harness.CastorTestCase;


/**
 * Test for generic key generators (MAX and HIGH-LOW).
 */
public class KeyGenGeneric extends CastorTestCase {

    private JDOCategory    _category;

    private Database       _db;

    public KeyGenGeneric( TestHarness category ) {
        this( category, "TC41", "Key generators: MAX, HIGH-LOW" );
    }

    public KeyGenGeneric( TestHarness category, String name, String description ) {
        super( category, name, description );
        _category = (JDOCategory) category;
    }

    public void setUp()
            throws PersistenceException {
        _db = _category.getDatabase( verbose );
    }

    public void runTest()
            throws PersistenceException, Exception {

        testOneKeyGen( TestMaxObject.class, TestMaxExtends.class );

        testOneKeyGen( TestHighLowObject.class, TestHighLowExtends.class );

    }

    public void tearDown()
            throws PersistenceException {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
    }

    /**
     * The main goal of the test is to verify key generators in the case
     * of "extends" relation between two classes.
     * For each key generator we have a pair of classes: TestXXXObject and
     * TestXXXExtends which use key generator XXX.
     */
    protected boolean testOneKeyGen( Class objClass, Class extClass )
            throws PersistenceException, Exception {

        OQLQuery            oql;
        TestKeyGenObject    object;
        TestKeyGenObject    ext;
        QueryResults        enum;
        boolean             result;

        result = true;

        // Open transaction in order to perform JDO operations
        _db.begin();

        // Create first object
        object = (TestKeyGenObject) objClass.newInstance();
        stream.println( "Creating first object: " + object );
        _db.create( object );
        stream.println( "Created first object: " + object );

        // Create second object
        ext = (TestKeyGenObject) extClass.newInstance();
        stream.println( "Creating second object: " + ext );
        _db.create( ext );
        stream.println( "Created second object: " + ext );

        _db.commit();

        _db.begin();

        // Find the first object and remove it 
        //object = (TestKeyGenObject) _db.load( objClass, object.getId() );
        oql = _db.getOQLQuery();
        oql.create( "SELECT object FROM " + objClass.getName() +
                       " object WHERE id = $1" );
        oql.bind( object.getId() );
        enum = oql.execute();
        stream.println( "Removing first object: " + object );
        if ( enum.hasMore() ) {
            object = (TestKeyGenObject) enum.next();
            _db.remove( object );
            stream.println( "OK: Removed" );
        } else {
            stream.println( "Error: Not found" );
            result = false;
        }

        // Find the second object and remove it
        //ext = (TestKeyGenObject) _db.load( extClass, ext.getId() );
        oql = _db.getOQLQuery();
        oql.create( "SELECT ext FROM " + extClass.getName() +
                       " ext WHERE id = $1" );
        oql.bind( ext.getId() );
        enum = oql.execute();
        stream.println( "Removing second object: " + ext );
        if ( enum.hasMore() ) {
            ext = (TestKeyGenObject) enum.next();
            _db.remove( ext );
            stream.println( "OK: Removed" );
        } else {
            stream.println( "Error: Not found" );
            result = false;
        }

        _db.commit();

        return result;
    }

}
