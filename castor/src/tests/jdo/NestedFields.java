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
import java.util.Date;
import java.util.Enumeration;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.exolab.castor.jdo.DataObjects;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.TransactionAbortedException;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Assert;
import harness.TestHarness;
import harness.CastorTestCase;


/**
 * Test for nested-field support in Castor JDO. A nested-field
 * is java field that maps to multiple database fields.
 */
public class NestedFields extends CastorTestCase {


    private JDOCategory    _category;

    private Database       _db;

    public NestedFields( TestHarness category ) {
        super( category, "TC27", "Nested fields" );
        _category = (JDOCategory) category;
    }


    public void setUp()
            throws PersistenceException {
        _db = _category.getDatabase();
    }

    public void runTest() 
            throws PersistenceException {

        OQLQuery         oql;
        TestNestedFields t;
        QueryResults     res;

        // Open transaction in order to perform JDO operations
        _db.begin();
        oql = _db.getOQLQuery( "SELECT t FROM jdo.TestNestedFields t WHERE id = $1" );
        oql.bind( TestNestedFields.DefaultId );
        res = oql.execute();
        if ( res.hasMore() ) {
            t = (TestNestedFields) res.next();
            _db.remove( t );
            stream.println( "Deleting object: " + t );
        }
        _db.commit();
        _db.begin();
        t = new TestNestedFields();
        stream.println( "Creating new object: " + t );
        _db.create( t );
        _db.commit();
        _db.begin();
        oql.bind( TestNestedFields.DefaultId );
        res = oql.execute();
        if ( res.hasMore() ) {
            t = (TestNestedFields) res.next();
            if ( t.getNested1().getValue1().equals(TestNestedFields.DefaultValue1)
                    && t.getNested2().getNested3().getValue2().equals(TestNestedFields.DefaultValue2)) {
                stream.println( "OK: Created object: " + t );
            } else {
                stream.println( "Error: Created object: " + t );
                fail("created object failed");
            }
        }
        _db.commit();
        oql.close();

        stream.println( "Testing nested fields in OQLQuery..." );
        _db.begin();
        oql = _db.getOQLQuery( "SELECT t FROM jdo.TestNestedFields t WHERE nested2.nested3.value2 = $1" );
        oql.bind( TestNestedFields.DefaultValue2 );
        res = oql.execute();
        if ( res.hasMore() ) {
            stream.println( "OK" );
        } else {
            stream.println( "Error" );
            fail("Failed to persist nested fields");
        }
        oql.close();
        _db.commit();

    }

    public void tearDown()
            throws PersistenceException {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
    }

}
