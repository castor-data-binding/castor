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
 * $Id$
 */


package jdo;

import java.math.BigDecimal;
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
 * Test on different type conversions.
 */
public class TypeConversion extends CastorTestCase {


    private JDOCategory     _category;

    private Database        _db;

    private OQLQuery        _oql;

    /**
     * Constructor
     *
     * @param category The test suite of these tests
     */
    public TypeConversion( TestHarness category ) {

        super( category, "TC20b", "Type Conversion" );
        _category = (JDOCategory) category;
    }


    public void setUp() 
            throws PersistenceException {

        TestConversion      types;
        Enumeration         enum;

        // Open transaction in order to perform JDO operations
        _db = _category.getDatabase( verbose );

        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        _db.begin();
        _oql = _db.getOQLQuery( "SELECT types FROM jdo.TestConversion types WHERE id = $1" );
        // This one tests that bind performs type conversion
        _oql.bind( new Integer(TestConversion.DefaultId) );
        enum = _oql.execute();
        if ( enum.hasMoreElements() ) {
            types = (TestConversion) enum.nextElement();
            // reset all value to default
            types.setBoolByte(false);
            types.setBoolShort(false);
            types.setBoolInt(false);
            types.setBoolBigdec(false);

            types.setByteInt((byte)0);
            types.setShortInt((short)0);
            types.setLongInt((long)0);
            types.setDoubleInt(0.0);
            types.setFloatInt(0.0f);

            types.setByteBigdec((byte)0);
            types.setShortBigdec((short)0);
            types.setIntBigdec(0);
            types.setFloatBigdec(0.0f);
            types.setDoubleBigdec(0.0);

            types.setByteString((byte)0);
            types.setShortString((short)0);
            types.setIntString(0);
            types.setLongString((long)0);
            types.setBigdecString(new BigDecimal(0) );
            types.setFloatString(0.0f);
            types.setDoubleString(0.0d);

            stream.println( "Updating object: " + types );
        } else {
            types = new TestConversion();
            stream.println( "Creating new object: " + types );
            types.setId( TestConversion.DefaultId );
            _db.create( types );
        }
        _db.commit();
    }

    public void runTest()
            throws PersistenceException, ParseException {

        testConversion();
    }

    private void testConversion()
            throws PersistenceException {

        TestConversion      types;

        Enumeration         enum;

        stream.println( "Testing date/time conversion" );
        _db.begin();
        // This one tests that bind performs type conversion
        types = (TestConversion) _db.load( TestConversion.class, new Integer(TestConversion.DefaultId) );
        types.setBoolByte(true);
        types.setBoolShort(true);
        types.setBoolInt(true);
        types.setBoolBigdec(true);

        types.setByteInt((byte)123);
        types.setShortInt((short)123);
        types.setLongInt((long)123);
        types.setDoubleInt(123.0);
        types.setFloatInt(123.0f);

        types.setByteBigdec((byte)123);
        types.setShortBigdec((short)123);
        types.setIntBigdec(123);
        types.setFloatBigdec(123.0f);
        types.setDoubleBigdec(123.0);

        types.setByteString((byte)123);
        types.setShortString((short)123);
        types.setIntString(123);
        types.setLongString((long)123);
        types.setBigdecString(new BigDecimal(123));
        types.setFloatString(123.45f);
        types.setDoubleString(123.45);        
        _db.commit();

        _db.begin();
        types = (TestConversion) _db.load( TestConversion.class, new Integer(TestConversion.DefaultId) );

        if (    types.getBoolByte()     != true &&
                types.getBoolShort()    != true &&
                types.getBoolInt()      != true &&
                types.getBoolBigdec()   != true &&
                types.getByteInt()      != (byte)123 &&
                types.getShortInt()     != (short)123 &&
                types.getLongInt()      != (long)123 &&
                types.getDoubleInt()    != 123.0d &&
                types.getFloatInt()     != 123.0 &&
                types.getByteBigdec()   != (byte)123 &&
                types.getShortBigdec()  != (short)123 &&
                types.getIntBigdec()    != 123 &&
                types.getFloatBigdec()  != 123.0 &&
                types.getDoubleBigdec() != 123.0d &&
                types.getByteString()   != (byte)123 &&
                types.getShortString()  != (short)123 &&
                types.getIntString()    != 123 &&
                types.getLongString()   != (long)123 &&
                types.getBigdecString() != new BigDecimal(123) &&
                types.getFloatString()  != 123.0 &&
                types.getDoubleString() != 123.0d ) {

            stream.println( "Error: conversion failed! some value does not match what is expected" );
            fail("conversion failed! some value does not match what is expected");
        }
        _db.commit();
        stream.println( "OK: Handled date/time types" );

    }

    public void tearDown() 
            throws PersistenceException {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
    }
}

