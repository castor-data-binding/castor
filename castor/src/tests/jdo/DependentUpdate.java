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


import java.sql.Connection;
import java.sql.SQLException;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ObjectModifiedException;

import harness.TestHarness;
import harness.CastorTestCase;


/**
 * Test for dependent relationship between data objects for
 * long transaction. A dependent object life cycle rely on 
 * its master object. For example, if the master object is 
 * deleted, it will be deleted by Castor as well. If the 
 * dependent object is dereferenced, it will be removed from 
 * the database.
 */
public class DependentUpdate extends CastorTestCase {


    private Connection     _conn;


    private JDOCategory    _category;


    private Database       _db;


    public DependentUpdate( TestHarness category )
            throws PersistenceException {

        super( category, "TC25", "Dependent update objects tests" );
        _category = (JDOCategory) category;
    }

    public void setUp()
            throws PersistenceException, SQLException {
        _db = _category.getDatabase();
        _conn = _category.getJDBCConnection();
        _conn.setAutoCommit( false );
    }

    public void runTest()
            throws PersistenceException, SQLException {

        OQLQuery      oql;
        OQLQuery      groupOql;
        TestMaster    master;
        TestGroup     group;
        TestDetail    detail;
        TestDetail2   detail2;
        QueryResults  qres;
        TestMaster    master2;
        int           cnt;
        int           detailId = 0;

        stream.println( "Delete everything" );
        _db.begin();
        oql = _db.getOQLQuery( "SELECT master FROM jdo.TestMaster master" );
        qres = oql.execute();

        for ( cnt = 0; qres.hasMore(); cnt++ ) {
            _db.remove( qres.next() );
        }
        stream.println( "Deleting " + cnt + " master objects" );

        oql = _db.getOQLQuery( "SELECT group FROM jdo.TestGroup group" );
        qres = oql.execute();
        for ( cnt = 0; qres.hasMore(); cnt++ ) {
            _db.remove( qres.nextElement() );
        }
        stream.println( "Deleting " + cnt + " group objects" );
        _db.commit();

        stream.println( "Attempt to create master with details" );
        _db.begin();
        master = new TestMaster();
        master.addDetail( new TestDetail( 5 ) );
        detail = new TestDetail( 6 );
        detail.addDetail2( new TestDetail2() );
        detail.addDetail2( new TestDetail2() );
        detail.setDetail3( new TestDetail3( 101 ) );
        master.addDetail( detail );
        detail = new TestDetail( 7 );
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
                fail("expecting group");
            } else if ( master.getGroup().getId() != TestGroup.DefaultId ) {
                stream.println( "Error: loaded master with wrong group: " + master );
                fail("incorrect group");
            }
            if ( master.getDetails() == null ||
                 ! master.getDetails().contains( new TestDetail( 5 ) ) ||
                 ! master.getDetails().contains( new TestDetail( 6 ) ) ||
                 ! master.getDetails().contains( new TestDetail( 7 ) ) ) {
                stream.println( "Error: loaded master without three details: " + master );
                fail("incorrect detail(s)");
            }
            detail = master.findDetail( 5 );
            if ( detail.getDetails2() != null && detail.getDetails2().size() != 0 ) {
                stream.println( "Error: loaded detail 5 with details2: " + qres.next() );
                fail("unexpected element found");
            }
            detail = master.findDetail( 6 );
            if ( detail.getDetails2() == null || detail.getDetails2().size() != 2) {
                stream.println( "Error: loaded detail 6 without two details: " + detail );
                fail("details' size mismatch");
            }
            if ( detail.getDetail3() == null || detail.getDetail3().getId() != 101 ) {
                stream.println( "Error: loaded detail 6 with wrong detail3: " + detail );
                fail("loaded detail 6 with wrong detail3: " + detail);
            }
            detail = master.findDetail( 7 );
            if ( detail.getDetails2() == null || detail.getDetails2().size() != 2) {
                stream.println( "Error: loaded detail 7 without two details: " + detail );
                fail("details' size mismatch");
            }
        } else {
            stream.println( "Error: failed to create master with details and group" );
            fail("failed to create master with details and group");
        }
        _db.commit();
        stream.println( "Created master with details: " + master );

        stream.println( "Attempt to change details" );
        _db.begin();
        master = (TestMaster) _db.load( TestMaster.class, new Integer( TestMaster.DefaultId ) );
        if ( master == null ) {
            stream.println( "Error: failed to find master with details group" );
            fail("master not found");
        }
        // remove detail with id == 5
        master.getDetails().remove( master.getDetails().indexOf( master.findDetail( 5 ) ) );
        // add new detail
        master.addDetail( new TestDetail( 8 ) );
        // add new detail and create it explicitely
        detail = new TestDetail( 9 );
        master.addDetail( detail );
        detail = (TestDetail) master.findDetail( 6 );
        // change 1:1 dependent relationship
        detail.setDetail3( new TestDetail3( 102 ) );
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
                 ! master.getDetails().contains( new TestDetail( 6 ) ) ||
                 master.findDetail( 6 ).getDetails2() == null ||
                 master.findDetail( 6 ).getDetails2().size() != 2 ||
                 master.findDetail( 6 ).getDetail3() == null ||
                 master.findDetail( 6 ).getDetail3().getId() != 102 ||
                 ! master.getDetails().contains( new TestDetail( 7 ) ) ||
                 ! master.getDetails().contains( new TestDetail( 8 ) ) ||
                 ! master.getDetails().contains( new TestDetail( 9 ) ) ) {
                stream.println( "Error: loaded master has wrong set of details: " + master );
                fail("loaded master has wrong set of details");
            } else {
                stream.println( "Details changed correctly: " + master );
            }
        } else {
            stream.println( "Error: master not found" );
            fail("master not found");
        }
        _db.commit();


        stream.println( "Test long transaction with dirty checking" );
        _db.begin();
        master = (TestMaster) _db.load( TestMaster.class, new Integer( TestMaster.DefaultId ) );
        if ( master == null ) {
            stream.println( "Error: failed to find master with details group" );
            fail("master not found");
        }
        _db.commit();
        _db.begin();
        master2 = (TestMaster) _db.load( TestMaster.class, new Integer( TestMaster.DefaultId ) );
        master2.setValue1( master2.getValue1() + "2" );
        _db.commit();

        stream.println( "Test 1" );
        try {
            _db.begin();
            _db.update( master );
            _db.commit();
            stream.println( "Error: Dirty checking doesn't work" );
            fail("dirty check failed");
        } catch ( ObjectModifiedException exept ) {
            _db.rollback();
            stream.println( "OK: Dirty checking works" );
        }

        stream.println( "Test 2" );
        detail = new TestDetail( 5 );
        detail2 = new TestDetail2();
        detail.addDetail2( detail2 );
        master2.addDetail( detail );
        master2.getDetails().remove( new TestDetail( 8 ) );
        master2.getDetails().remove( new TestDetail( 9 ) );
        try {
            _db.begin();
            _db.update( master2 );
            _db.commit();
            detailId = detail2.getId();
            stream.println( "OK: Dirty checking works" );
        } catch ( ObjectModifiedException exept ) {
            _db.rollback();
            stream.println( "Error: Dirty checking doesn't work" );
            fail("dirty check failed");
        }

        stream.println( "Test 3" );
        _conn.createStatement().execute( "UPDATE test_master SET value1='concurrent' WHERE id="
                + master2.getId() );
        _conn.commit();
        master2.setValue1( "long transaction new value" );
        try {
            _db.begin();
            _db.update( master2 );
            _db.commit();
            stream.println( "Error: Dirty checking doesn't work" );
            fail("dirty check failed");
        } catch ( ObjectModifiedException except ) {
            if (_db.isActive()) {
                _db.rollback();
            }

            stream.println( "OK: Dirty checking works" );
        }
        _db.begin();
        master = (TestMaster) _db.load( TestMaster.class, new Integer( TestMaster.DefaultId ) );
        if ( master != null ) {
            if ( master.getDetails().size() == 0 ||
                 ! master.getDetails().contains( new TestDetail( 5 ) ) ||
                 master.findDetail( 5 ).findDetail2( detailId ) == null  ||
                 ! master.getDetails().contains( new TestDetail( 6 ) ) ||
                 master.findDetail( 6 ).getDetails2() == null ||
                 master.findDetail( 6 ).getDetails2().size() != 2 ||
                 ! master.getDetails().contains( new TestDetail( 7 ) ) ||
                 master.getDetails().contains( new TestDetail( 8 ) ) ||
                 master.getDetails().contains( new TestDetail( 9 ) ) ) {
                stream.println( "Error: loaded master has wrong set of details: " + master );
                fail("unexpect set of details");
            } else {
                stream.println( "Details changed correctly in the long transaction: " + master );
            }
        } else {
            stream.println( "Error: master not found" );
            fail("master not found");
        }
        _db.commit();

        // modify an dependent object and see if it got updated
        stream.println( "Test 3" );
        detail = master.findDetail( 5 );
        detail.setValue1("new updated value");
        detail.findDetail2( detailId ).setValue1("new detail 2 value");
        _db.begin();
        _db.update( master );
        _db.commit();

        _db.begin();
        master = (TestMaster) _db.load( TestMaster.class, new Integer( TestMaster.DefaultId ) );
        if ( master != null ) {
            if ( master.getDetails() == null ||
                 master.getDetails().size() == 0 ||
                 ! master.getDetails().contains( new TestDetail( 5 ) ) ||
                 ! master.getDetails().contains( new TestDetail( 6 ) ) ||
                 ! master.getDetails().contains( new TestDetail( 7 ) ) ||
                 master.getDetails().contains( new TestDetail( 8 ) ) ||
                 master.getDetails().contains( new TestDetail( 9 ) ) ||
                 ! "new updated value".equals( master.findDetail( 5 ).getValue1()) ||
                 master.findDetail( 5 ).findDetail2( detailId ) == null ||
                 ! "new detail 2 value".equals( master.findDetail( 5 ).findDetail2( detailId ).getValue1() ) ) {

                stream.println( "Error: loaded master has wrong set of details: " + master );
                fail("unexpected set of details");
            } else {
                stream.println( "Details changed correctly in the long transaction: " + master );
            }
        } else {
            stream.println( "Error: master not found" );
            fail("master not found");
        }
        _db.commit();
    }

    public void tearDown()
            throws PersistenceException, SQLException {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
        _conn.close();
    }
}
