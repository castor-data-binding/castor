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


import harness.CastorTestCase;
import harness.TestHarness;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.Utils;


/**
 */
public class OqlExtends extends CastorTestCase {

    private JDOCategory    _category;

    private Database       _db;

    public OqlExtends( TestHarness category ) {
        super( category, "TC30", "OQL queries for extends" );
        _category = (JDOCategory) category;
    }

    public void setUp()
    	throws PersistenceException 
	{
        _db = _category.getDatabase();
        
        // XXX [SMH]: We need to clear some tables because "TC23 ManyToMany" is doing something unwise, see bug 1445. 
        Connection conn = null;
        try {
            conn = _category.getJDBCConnection();
            conn.setAutoCommit(false);
            conn.createStatement().executeUpdate("DELETE FROM test_oqlext2");
			conn.createStatement().executeUpdate("DELETE FROM test_persistent");
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	Utils.closeConnection (conn);
        }
    }

    public void runTest()
            throws PersistenceException, Exception {

        Date date;
        OQLQuery         oqlAll;
        OQLQuery         oql;
        TestPersistent   p;
        TestOqlExtends   t;
        TestOqlExtends2  t2;
        QueryResults     res;
        int              cnt;
        TestGroup        group1 = new TestGroup();
        TestGroup        group2 = new TestGroup();

        group2.setId(TestGroup.DefaultId + 1);

        // remove old test objects
        _db.begin();
        oqlAll = _db.getOQLQuery( "SELECT t FROM jdo.TestOqlExtends t" );
        res = oqlAll.execute();
        while ( res.hasMore() ) {
            _db.remove( res.next() );
        }
        oql = _db.getOQLQuery( "SELECT t FROM jdo.TestGroup t" );
        res = oql.execute();
        while ( res.hasMore() ) {
            _db.remove( res.next() );
        }
        oql.close();
        oql = _db.getOQLQuery( "SELECT t FROM jdo.TestOqlExtends2 t" );
        res = oql.execute();
        while ( res.hasMore() ) {
            _db.remove( res.next() );
        }
        oql = _db.getOQLQuery( "SELECT t FROM jdo.TestPersistent t" );
        res = oql.execute();
        while ( res.hasMore() ) {
            _db.remove( res.next() );
        }
        oql.close();
        oql.close();

        // create data objects for test
        _db.commit();
        _db.begin();
        stream.println( "Creating group: " + group1 );
        _db.create( group1 );
        stream.println( "Creating group: " + group2 );
        _db.create( group2 );
        t2 = new TestOqlExtends2();
        t2.setId(TestOqlExtends2.DefaultId + 20);
        t2.setGroup(group2);
        stream.println( "Creating new object: " + t2 );
        _db.create( t2 );
        date = new Date();
        Thread.sleep(2000);
        t = new TestOqlExtends();
        t.setId(TestOqlExtends.DefaultId + 10);
        t.setGroup(group1);
        t.getList().add(t2);
        stream.println( "Creating new object: " + t );
        _db.create( t );
        t2 = new TestOqlExtends2();
        t2.setId(TestOqlExtends2.DefaultId + 21);
        t2.setGroup(group2);
        stream.println( "Creating new object: " + t2 );
        _db.create( t2 );
        t = new TestOqlExtends();
        t.setId(TestOqlExtends.DefaultId + 11);
        t.setGroup(group2);
        t.getList().add(t2);
        stream.println( "Creating new object: " + t );
        _db.create( t );
        oql.close();
        _db.commit();

        // query on extends object
        _db.begin();
        oql = _db.getOQLQuery( "SELECT t FROM jdo.TestOqlExtends t WHERE t.group.id=$1" );
        oql.bind( group1.getId() );
        res = oql.execute();
        for ( cnt = 0; res.hasMore(); cnt++ ) {
            t = (TestOqlExtends) res.next();
            stream.println( "Retrieved object: " + t );
            if (t.getExt() != 0) {
                stream.println( "Error: ext field = " + t.getExt());
                fail("ext field retrieval failed");
            }
        }
        oql.close();
        if (cnt == 1) {
            stream.println( "OK" );
        } else {
            stream.println( "Error: retrieved " + cnt + " objects");
            fail("result size mismatch in the query on extends object");
        }
        oql = _db.getOQLQuery( "SELECT t FROM jdo.TestPersistent t WHERE t.creationTime<=$1" );
        oql.bind( date );
        res = oql.execute();
        for ( cnt = 0; res.hasMore(); cnt++ ) {
            p = (TestPersistent) res.next();
            stream.println( "Retrieved object: " + p );
        }
        oql.close();
        if (cnt == 1) {
            stream.println( "OK" );
        } else {
            stream.println( "Error: retrieved " + cnt + " objects");
            fail("result size mismatch in the query on base object");
        }

        _db.commit();

        // query on many-to-many relation
        _db.begin();
        oql = _db.getOQLQuery( "SELECT t FROM jdo.TestOqlExtends t WHERE t.list.id=$1" );
        oql.bind( t2.getId() );
        res = oql.execute();
        for ( cnt = 0; res.hasMore(); cnt++ ) {
            t = (TestOqlExtends) res.next();
            stream.println( "Retrieved object: " + t );
        }
        oql.close();
        if (cnt == 1) {
            stream.println( "OK" );
        } else {
            stream.println( "Error: retrieved " + cnt + " objects");
            fail("result size mismatch in the query on many-to-many");
        }

        res = oqlAll.execute();
        while ( res.hasMore() ) {
            _db.remove( res.next() );
        }
        _db.commit();
        oqlAll.close();
    }

    public void tearDown()
            throws PersistenceException {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
    }
}
