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


import java.io.IOException;
import java.util.Enumeration;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import org.exolab.castor.jdo.DataObjects;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.ObjectModifiedException;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Assert;
import harness.TestHarness;
import harness.CastorTestCase;


/**
 * Test for dependent relationship between data objects.
 * A dependent object life cycle rely on its master object.
 * For example, if the master object is deleted, it will
 * be deleted by Castor as well. If the dependent object
 * is dereferenced, it will be removed from the database.
 */
public class DependentKeyGen extends CastorTestCase {

    private JDOCategory    _category;

    private Database       _db;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public DependentKeyGen( TestHarness category ) {
        super( category, "TC24a", "Dependent objects tests" );
        _category = (JDOCategory) category;
    }

    /**
     * Get a JDO database
     */
    public void setUp()
            throws PersistenceException {

        _db = _category.getDatabase( verbose );
    }

    public void runTest()
            throws PersistenceException {

        int                 detailId5, detailId6, detailId7, detailId8, detailId9;
        TestDetailKeyGen    detail5,   detail6,   detail7,   detail8,   detail9;
        OQLQuery            oql;
        OQLQuery            groupOql;
        int                 masterId;
        TestMasterKeyGen    master;
        TestGroup           group;
        int                 groupId;
        TestDetailKeyGen    detail;
        QueryResults        qres;
        TestMasterKeyGen    master2;
        int                 cnt;
        
        _db = _category.getDatabase( verbose );

        stream.println( "Delete everything" );
        _db.begin();
        oql = _db.getOQLQuery( "SELECT master FROM jdo.TestMasterKeyGen master" );
        qres = oql.execute();

        for ( cnt = 0; qres.hasMore(); cnt++ ) {
            _db.remove( qres.next() );
        }
        oql.close();
        stream.println( "Deleting " + cnt + " master objects" );
        

        oql = _db.getOQLQuery( "SELECT group FROM jdo.TestGroup group" );
        qres = oql.execute();
        for ( cnt = 0; qres.hasMore(); cnt++ ) {
            _db.remove( qres.nextElement() );
        }
        oql.close();
        stream.println( "Deleting " + cnt + " group objects" );
        _db.commit();

        stream.println( "Attempt to create master with details" );
        _db.begin();
        master = new TestMasterKeyGen();
        detail5 = new TestDetailKeyGen();
        master.addDetail( detail5 );
        detail6 = new TestDetailKeyGen();
        detail6.addDetail2( new TestDetailKeyGen2() );
        detail6.addDetail2( new TestDetailKeyGen2() );
        master.addDetail( detail6 );
        detail7 = new TestDetailKeyGen();
        detail7.addDetail2( new TestDetailKeyGen2() );
        detail7.addDetail2( new TestDetailKeyGen2() );
        detail7.setDetail3( new TestDetailKeyGen3() );
        master.addDetail( detail7 );
        group = new TestGroup();
        _db.create( group );

        master.setGroup( group );
        _db.create( master );
        _db.commit();

        detailId5 = detail5.getId();
        detailId6 = detail6.getId();
        detailId7 = detail7.getId();
        masterId  = master.getId();
        groupId   = group.getId();

        _db.begin();
        master = (TestMasterKeyGen) _db.load( TestMasterKeyGen.class, new Integer( masterId ) );
        if ( master != null ) {
            if ( master.getGroup() == null ) {
                stream.println( "Error: loaded master without group: " + master );
                fail("loaded master without group: " + master);
            } else if ( master.getGroup().getId() != groupId ) {
                stream.println( "Error: loaded master with wrong group: " + master );
                fail("loaded master with wrong group: " + master);
            }
            if ( master.getDetails() == null ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId5 ) ) ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId6 ) ) ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId7 ) ) ) {
                stream.println( "Error: loaded master without three details: " + master );
                fail("loaded master without three details: " + master);
            }
            detail = master.findDetail( detailId5 );
            if ( detail.getDetails2() != null && detail.getDetails2().size() != 0 ) {
                stream.println( "Error: loaded detail 5 with details2: " + detail );
                fail("loaded detail 5 with details2: " + detail );
            }
            if ( detail.getDetail3() != null ) {
                stream.println( "Error: loaded detail 5 with unexpected details3: " + detail );
                fail("loaded detail 5 with unexpected details3: " + detail );
            }
            detail = master.findDetail( detailId6 );
            if ( detail.getDetails2() == null || detail.getDetails2().size() != 2 ) {
                stream.println( "Error: loaded detail 6 without two details: " + detail );
                fail("loaded detail 6 without two details2: " + detail);
            }
            if ( detail.getDetail3() != null ) {
                stream.println( "Error: loaded detail 6 with unexpected details3: " + detail );
                fail("loaded detail 6 with unexpected details3: " + detail );
            }

            detail = master.findDetail( detailId7 );
            if ( detail.getDetails2() == null || detail.getDetails2().size() != 2) {
                stream.println( "Error: loaded detail 7 without two details: " + detail );
                fail("loaded detail 7 without two details2: " + detail);
            }
            if ( detail.getDetail3() == null ) {
                stream.println( "Error: loaded detail 7 without the expected details3: " + detail );
                fail("loaded detail 7 without the expected details3: " + detail );
            }

        } else {
            stream.println( "Error: failed to create master with details and group" );
            fail("failed to create master with details and group");
        }
        stream.println( "Created master with details: " + master );
        _db.commit();


        stream.println( "Attempt to change details" );
        _db.begin();
        master = (TestMasterKeyGen) _db.load( TestMasterKeyGen.class, new Integer( masterId ) );
        if ( master == null ) {
            stream.println( "Error: failed to find master with details group" );
            fail("failed to find master with details group" );
        }
        // remove detail with id == 5
        master.getDetails().remove( master.getDetails().indexOf( master.findDetail( detailId5 ) ) );
        // remove detail with id == 6 explicitly
        detail = (TestDetailKeyGen) master.findDetail( detailId6 );
        master.getDetails().remove( master.getDetails().indexOf( detail ) );
        // add new detail
        detail8 = new TestDetailKeyGen();
        master.addDetail( detail8 );
        // add new detail and create it explicitely
        detail9 = new TestDetailKeyGen();
        master.addDetail( detail9 );
        // delete, then create detail with id == 7 explicitly
        detail7 = (TestDetailKeyGen) master.findDetail( detailId7 );
        master.getDetails().remove( master.getDetails().indexOf( detail7 ) );
        master.addDetail( detail7 );
        _db.commit();
        detailId8 = detail8.getId();
        detailId9 = detail9.getId();

        _db.begin();
        master = (TestMasterKeyGen) _db.load( TestMasterKeyGen.class, new Integer( masterId ) );
        if ( master != null ) {
            if ( master.getDetails().size() == 0 ||
                 master.getDetails().contains( new TestDetailKeyGen( detailId5 ) ) ||
                 master.getDetails().contains( new TestDetailKeyGen( detailId6 ) ) ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId7 ) ) ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId8 ) ) ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId9 ) ) ) {
                stream.println( "Error: loaded master has wrong set of details: " + master );
                fail("loaded master has wrong set of details: " + master);
            } else {
                stream.println( "Details changed correctly: " + master );
            }
        } else {
            stream.println( "Error: master not found" );
            fail("master not found");
        }
        _db.commit();

        stream.println( "Test OQL query" );
        _db.begin();
        oql = _db.getOQLQuery( "SELECT master FROM jdo.TestMasterKeyGen master WHERE master.details.value1=$1" );
        oql.bind(TestDetail.DefaultValue);
        qres = oql.execute();
        if ( qres.hasMore() ) {
            stream.println( "OK: correct result of query 1 " );
        } else {
            stream.println( "Error: incorrect result of query 1 " );
            fail("incorrect result of query 1");
        }
        oql.bind(TestDetail.DefaultValue + "*");
        qres = oql.execute();
        if ( qres.hasMore() ) {
            stream.println( "Error: incorrect result of query 2 " );
            fail("incorrect result of query 2");
        } else {
            stream.println( "OK: correct result of query 2 " );
        }
        oql.close();
        oql = _db.getOQLQuery( "SELECT master FROM jdo.TestMasterKeyGen master WHERE master.details.details2.value1=$1" );
        oql.bind(TestDetailKeyGen2.DefaultValue);
        qres = oql.execute();
        if ( qres.hasMore() ) {
            stream.println( "OK: correct result of query 3 " );
        } else {
            stream.println( "Error: incorrect result of query 3 " );
            fail("incorrect result of query 3");
        }
        oql.bind(TestDetailKeyGen2.DefaultValue + "*");
        qres = oql.execute();
        if ( qres.hasMore() ) {
            stream.println( "Error: incorrect result of query 4 " );
            fail("incorrect result of query 4");
        } else {
            stream.println( "OK: correct result of query 4 " );
        }
        oql.close();
        oql = _db.getOQLQuery( "SELECT master FROM jdo.TestMasterKeyGen master WHERE master.group=$1" );
        oql.bind(group);
        qres = oql.execute();
        if ( qres.hasMore() ) {
            stream.println( "OK: correct result of query 5 " );
        } else {
            stream.println( "Error: incorrect result of query 5 " );
            fail("incorrect result of query 5");
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
