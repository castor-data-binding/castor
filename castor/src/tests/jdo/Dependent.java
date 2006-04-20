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


import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;

import harness.TestHarness;
import harness.CastorTestCase;


/**
 * Test for dependent relationship between data objects.
 * A dependent object life cycle rely on its master object.
 * For example, if the master object is deleted, it will
 * be deleted by Castor as well. If the dependent object
 * is dereferenced, it will be removed from the database.
 */
public class Dependent extends CastorTestCase {

    private JDOCategory    _category;

    private Database       _db;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public Dependent( TestHarness category ) {
        super( category, "TC24", "Dependent objects tests" );
        _category = (JDOCategory) category;
    }

    /**
     * Get a JDO database
     */
    public void setUp()
            throws PersistenceException {

        _db = _category.getDatabase();
    }

    public void runTest()
            throws PersistenceException {

        OQLQuery      oql;
        OQLQuery      groupOql;
        TestMaster    master;
        TestGroup     group;
        TestDetail    detail;
        QueryResults  qres;
        TestMaster    master2;
        int           cnt;
        
        _db = _category.getDatabase();

        stream.println( "Delete everything" );
        _db.begin();
        oql = _db.getOQLQuery( "SELECT master FROM jdo.TestMaster master" );
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
        master = new TestMaster();
        master.addDetail( new TestDetail( 5 ) );
        detail = new TestDetail( 6 );
        detail.addDetail2( new TestDetail2() );
        detail.addDetail2( new TestDetail2() );

        master.addDetail( detail );
        detail = new TestDetail( 7 );
        detail.setDetail3( new TestDetail3( 101 ) );
        detail.addDetail2( new TestDetail2() );
        detail.addDetail2( new TestDetail2() );
        master.addDetail( detail );
        group = new TestGroup();
        _db.create( group );
        master.setGroup( group );
        _db.create( master );
        _db.commit();

        _db.begin();
        master = (TestMaster) _db.load( TestMaster.class, new Integer( TestMaster.DefaultId ) );
        if ( master != null ) {
            if ( master.getGroup() == null ) {
                stream.println( "Error: loaded master without group: " + master );
                fail("loaded master without group: " + master);
            } else if ( master.getGroup().getId() != TestGroup.DefaultId ) {
                stream.println( "Error: loaded master with wrong group: " + master );
                fail("loaded master with wrong group: " + master);
            }
            if ( master.getDetails() == null ||
                 ! master.getDetails().contains( new TestDetail( 5 ) ) ||
                 ! master.getDetails().contains( new TestDetail( 6 ) ) ||
                 ! master.getDetails().contains( new TestDetail( 7 ) ) ) {
                stream.println( "Error: loaded master without three details: " + master );
                fail("loaded master without three details: " + master);
            }
            detail = master.findDetail( 5 );
            if ( detail.getDetails2() != null && detail.getDetails2().size() != 0 ) {
                stream.println( "Error: loaded detail 5 with details2: " + detail );
                fail("loaded detail 5 with details2: " + detail);
            }
            detail = master.findDetail( 6 );
            if ( detail.getDetails2() == null || detail.getDetails2().size() != 2 ) {
                stream.println( "Error: loaded detail 6 without two details: " + detail );
                fail("loaded detail 6 without two details: " + detail);
            }
            detail = master.findDetail( 7 );
            if ( detail.getDetails2() == null || detail.getDetails2().size() != 2) {
                stream.println( "Error: loaded detail 7 without two details: " + detail );
                fail("loaded detail 7 without two details: " + detail);
            }
            if ( detail.getDetail3() == null || detail.getDetail3().getId() != 101 ) {
                stream.println( "Error: loaded detail 6 wrong detail3: " + detail );
                fail("loaded detail 7 wrong detail3: " + detail);
            }
        } else {
            stream.println( "Error: failed to create master with details and group" );
            fail("failed to create master with details and group");
        }
        stream.println( "Created master with details: " + master );
        _db.commit();


        stream.println( "Attempt to change details" );
        _db.begin();
        master = (TestMaster) _db.load( TestMaster.class, new Integer( TestMaster.DefaultId ) );
        if ( master == null ) {
            stream.println( "Error: failed to find master with details group" );
            fail("failed to find master with details group" );
        }
        // remove detail with id == 5
        master.getDetails().remove( master.getDetails().indexOf( master.findDetail( 5 ) ) );
        // remove detail with id == 6 explicitly
        detail = (TestDetail) master.findDetail( 6 );
        master.getDetails().remove( master.getDetails().indexOf( detail ) );
        detail = (TestDetail) master.findDetail( 7 );
        detail.setDetail3( new TestDetail3( 102 ) );
        // add new detail
        master.addDetail( new TestDetail( 8 ) );
        // add new detail and create it explicitely
        detail = new TestDetail( 9 );
        master.addDetail( detail );
        // delete, then create detail with id == 7 explicitly
        detail = (TestDetail) master.findDetail( 7 );
        master.getDetails().remove( master.getDetails().indexOf( detail ) );
        master.addDetail( detail );
        _db.commit();
        _db.begin();
        master = (TestMaster) _db.load( TestMaster.class, new Integer( TestMaster.DefaultId ) );
        if ( master != null ) {
            if ( master.getDetails().size() == 0 ||
                 master.getDetails().contains( new TestDetail( 5 ) ) ||
                 master.getDetails().contains( new TestDetail( 6 ) ) ||
                 ! master.getDetails().contains( new TestDetail( 7 ) ) ||
                 ! master.getDetails().contains( new TestDetail( 8 ) ) ||
                 ! master.getDetails().contains( new TestDetail( 9 ) ) ) {
                stream.println( "Error: loaded master has wrong set of details: " + master );
                fail("loaded master has wrong set of details: " + master);
            } else {
                stream.println( "Details changed correctly: " + master );
            }
            detail = (TestDetail) master.findDetail( 7 );
            if ( detail.getDetail3() == null || detail.getDetail3().getId() != 102 ) {
                stream.println( "Error: loaded detail y wrong detail3: " + detail );
                fail("loaded detail 7 wrong detail3: " + detail);
            }
        } else {
            stream.println( "Error: master not found" );
            fail("master not found");
        }
        _db.commit();

        stream.println( "Test OQL query" );
        _db.begin();
        oql = _db.getOQLQuery( "SELECT master FROM jdo.TestMaster master WHERE master.details.value1=$1" );
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
        oql = _db.getOQLQuery( "SELECT master FROM jdo.TestMaster master WHERE master.details.details2.value1=$1" );
        oql.bind(TestDetail2.DefaultValue);
        qres = oql.execute();
        if ( qres.hasMore() ) {
            stream.println( "OK: correct result of query 3 " );
        } else {
            stream.println( "Error: incorrect result of query 3 " );
            fail("incorrect result of query 3");
        }
        oql.bind(TestDetail2.DefaultValue + "*");
        qres = oql.execute();
        if ( qres.hasMore() ) {
            stream.println( "Error: incorrect result of query 4 " );
            fail("incorrect result of query 4");
        } else {
            stream.println( "OK: correct result of query 4 " );
        }
        oql.close();
        oql = _db.getOQLQuery( "SELECT master FROM jdo.TestMaster master WHERE master.group=$1" );
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
