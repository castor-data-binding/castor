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
 * Copyright 1999-2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package jdo;


import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.StringReader;
import java.util.Date;
import java.util.Enumeration;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.SQLException;
import org.exolab.castor.jdo.DataObjects;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.engine.ClobImpl;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Assert;
import harness.TestHarness;
import harness.CastorTestCase;


/**
 * Test on BLOB and CLOB as a field type of data objects.
 */
public class TypeLOB extends CastorTestCase {


    private JDOCategory    _category;

    private Database       db;

    private OQLQuery       oql;
    /**
     * Constructor
     *
     * @param category The test suite of these tests
     */
    public TypeLOB( TestHarness category ) {
        super( category, "TC20a", "Type test for LOBs" );
        _category = (JDOCategory) category;
    }


    public void setUp() 
            throws PersistenceException {

        TestLOB     types;
        Enumeration   enum;

        // Open transaction in order to perform JDO operations
        db = _category.getDatabase( verbose );

        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        db.begin();
        oql = db.getOQLQuery( "SELECT types FROM jdo.TestLOB types WHERE id = $(integer)1" );
        // This one tests that bind performs type conversion
        oql.bind( TestLOB.DefaultId );
        enum = oql.execute();
        if ( enum.hasMoreElements() ) {
            types = (TestLOB) enum.nextElement();
            stream.println( "Updating object: " + types );
        } else {
            types = new TestLOB();
            types.setId( TestLOB.DefaultId );
            stream.println( "Creating new object: " + types );
            db.create( types );
        }
        db.commit();
    }

    public void runTest()
            throws PersistenceException, IOException, SQLException {

        TestLOB     types;

        Enumeration   enum;

        int           len;

        final byte[] BLOB_VALUE = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        final String CLOB_VALUE = "0123456789";

        byte[] bbuf = new byte[BLOB_VALUE.length + 1];

        char[] cbuf = new char[CLOB_VALUE.length() + 1];


        stream.println( "Testing BLOB and CLOB fields" );
        db.begin();
        oql.bind( TestLOB.DefaultId );
        enum = oql.execute();
        if ( enum.hasMoreElements() ) {
            types = (TestLOB) enum.nextElement();
            types.setBlob( BLOB_VALUE );
            types.setClob( CLOB_VALUE );
            //types.setBlob2( new ByteArrayInputStream( BLOB_VALUE ) );
            types.setClob2( new ClobImpl( new StringReader( CLOB_VALUE ), CLOB_VALUE.length() ) );
        }
        db.commit();

        db.begin();
        oql.bind( TestLOB.DefaultId );
        enum = oql.execute();
        if ( enum.hasMoreElements() ) {
            types = (TestLOB) enum.nextElement();

            if ( types.getBlob() == null || ! (new String(types.getBlob())).equals(new String(BLOB_VALUE)) ) {
                stream.println( "Error: BLOB value was not set" );
                fail("BLOB value was not set");
            }
            if ( ! CLOB_VALUE.equals(types.getClob()) ) {
                stream.println( "Error: CLOB value was not set" );
                fail("CLOB value was not set");
            }
            /*
            if ( types.getBlob2() == null ) {
                stream.println( "Error: InputStream value was not set" );
                fail("InputStream value was not set");
            } else {
                len = types.getBlob2().read(bbuf);
                if ( len <= 0 ) {
                    stream.println( "Error: InputStream has zero length" );
                    fail("InputStream has zero length");
                }
                if ( ! (new String(bbuf, 0, len)).equals(new String(BLOB_VALUE)) ) {
                    stream.println( "Error: InputStream value is wrong" );
                    fail("InputStream value mismatched!");
                }
            }*/
            if ( types.getClob2() == null ) {
                stream.println( "Error: Clob value was not set" );
                fail("Clob value was not set");
            } else {
                long clobLen = types.getClob2().length();
                len = types.getClob2().getCharacterStream().read(cbuf);
                if ( clobLen != CLOB_VALUE.length() ||
                        ! (new String(cbuf, 0, len)).equals(CLOB_VALUE) ) {
                    stream.println( "Error: Clob value is wrong" );
                    fail("Clob value mismatched!");
                }
            }
        } else {
            stream.println( "Error: failed to load object" );
            fail("failed to load object");
        }
        db.commit();
        stream.println( "OK: BLOB and CLOB fields passed" );
    }
}

