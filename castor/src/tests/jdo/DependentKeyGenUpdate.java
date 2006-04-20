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
public class DependentKeyGenUpdate extends CastorTestCase {


    private Connection     _conn;


    private JDOCategory    _category;


    private Database       _db;


    public DependentKeyGenUpdate( TestHarness category )
            throws PersistenceException {

        super( category, "TC25a", "Dependent update objects tests" );
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

        OQLQuery            oql;
        OQLQuery            groupOql;
        QueryResults        qres;
        int                 cnt;
        TestMasterKeyGen    master, master2;
        int                 masterId, masterId2;
        TestDetailKeyGen    detail5, detail6, detail7, detail8, detail9;
        int                 detailId5, detailId6, detailId7, detailId8, detailId9;
        TestDetailKeyGen    detailA, detailB, detailC, detailD, detailE;
        int                 detailIdA, detailIdB, detailIdC, detailIdD, detailIdE;
        TestGroup           group;
        TestDetailKeyGen    detail;
        TestDetailKeyGen2   detail2;
        int                 detail2Id;
        TestDetailKeyGen3   detail3;
        int                 detail3Id;

        stream.println( "Delete everything" );
        _db.begin();
        oql = _db.getOQLQuery( "SELECT master FROM jdo.TestMasterKeyGen master" );
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
        master = new TestMasterKeyGen();
        detail5 = new TestDetailKeyGen();
        master.addDetail( detail5 );
        detail6 = new TestDetailKeyGen();
        detail6.addDetail2( new TestDetailKeyGen2() );
        detail6.addDetail2( new TestDetailKeyGen2() );
        detail3 = new TestDetailKeyGen3( 101 );
        detail6.setDetail3( detail3 );
        master.addDetail( detail6 );
        detail7 = new TestDetailKeyGen();
        detail7.addDetail2( new TestDetailKeyGen2() );
        detail7.addDetail2( new TestDetailKeyGen2() );
        master.addDetail( detail7 );
        group = new TestGroup();
        _db.create( group );
        master.setGroup( group );
        _db.create( master );
        _db.commit();

        masterId  = master.getId();
        detailId5 = detail5.getId();
        detailId6 = detail6.getId();
        detailId7 = detail7.getId();
        detail3Id = detail3.getId();

        _db.begin();
        master = (TestMasterKeyGen) _db.load( TestMasterKeyGen.class, new Integer( masterId ) );
        if ( master != null ) {
            if ( master.getGroup() == null ) {
                stream.println( "Error: loaded master without group: " + master );
                fail("expecting group");
            } else if ( master.getGroup().getId() != TestGroup.DefaultId ) {
                stream.println( "Error: loaded master with wrong group: " + master );
                fail("incorrect group" + master);
            }
            if ( master.getDetails() == null ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId5 ) ) ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId6 ) ) ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId7 ) ) ) {
                stream.println( "Error: loaded master without three details: " + master );
                fail("incorrect detail(s)"+master+" expecting: "+detailId5+","+detailId6+","+detailId7);
            }
            detail5 = master.findDetail( detailId5 );
            if ( detail5.getDetails2() != null && detail5.getDetails2().size() != 0 ) {
                stream.println( "Error: loaded detail 5 with details2: " + detail5 );
                fail("unexpected element found");
            }
            detail6 = master.findDetail( detailId6 );
            if ( detail6.getDetails2() == null || detail6.getDetails2().size() != 2) {
                stream.println( "Error: loaded detail 6 without two details: " + detail6 );
                fail("details' size mismatch");
            }
            if ( detail6.getDetail3() == null || detail6.getDetail3().getId() != detail3Id ) {
                stream.println( "Error: loaded detail 6 with wrong detail3: " + detail6 );
                fail("loaded detail 6 with wrong detail3: " + detail6);
            }
            detail7 = master.findDetail( detailId7 );
            if ( detail7.getDetails2() == null || detail7.getDetails2().size() != 2) {
                stream.println( "Error: loaded detail 7 without two details: " + detail7 );
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
        master = (TestMasterKeyGen) _db.load( TestMasterKeyGen.class, new Integer( masterId ) );
        if ( master == null ) {
            stream.println( "Error: failed to find master with details group" );
            fail("master not found");
        }
        // remove detail with id == 5
        master.getDetails().remove( master.getDetails().indexOf( master.findDetail( detailId5 ) ) );
        // add new detail
        detail8 = new TestDetailKeyGen();
        master.addDetail( detail8 );
        // add new detail and create it explicitely
        detail9 = new TestDetailKeyGen();
        master.addDetail( detail9 );
        detail6 = (TestDetailKeyGen) master.findDetail( detailId6 );
        // change 1:1 dependent relationship
        detail3 = new TestDetailKeyGen3();
        detail6.setDetail3( detail3 );

        _db.commit();
        detailId8 = detail8.getId();
        detailId9 = detail9.getId();
        detail3Id = detail3.getId();

        _db.begin();
        master = (TestMasterKeyGen) _db.load( TestMasterKeyGen.class, new Integer( masterId ) );
        if ( master != null ) {
            if ( master.getDetails().size() == 0 ||
                 master.getDetails().contains( new TestDetailKeyGen( detailId5 ) ) ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId6 ) ) ||
                 master.findDetail( detailId6 ).getDetails2() == null ||
                 master.findDetail( detailId6 ).getDetails2().size() != 2 ||
                 master.findDetail( detailId6 ).getDetail3() == null ||
                 master.findDetail( detailId6 ).getDetail3().getId() != detail3Id ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId7 ) ) ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId8 ) ) ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId9 ) ) ) {
                stream.println( "Error: loaded master has wrong set of details: " + master );
                fail("loaded master has wrong set of details: "+master.findDetail( detailId6 ));
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
        master = (TestMasterKeyGen) _db.load( TestMasterKeyGen.class, new Integer( masterId ) );
        if ( master == null ) {
            stream.println( "Error: failed to find master with details group" );
            fail("master not found");
        }
        _db.commit();
        _db.begin();
        master2 = (TestMasterKeyGen) _db.load( TestMasterKeyGen.class, new Integer( masterId ) );
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
        detailA = new TestDetailKeyGen();
        detail2 = new TestDetailKeyGen2();
        detailA.addDetail2( detail2 );
        master2.addDetail( detailA );
        master2.getDetails().remove( new TestDetailKeyGen( detailId8 ) );
        master2.getDetails().remove( new TestDetailKeyGen( detailId9 ) );
        try {
            _db.begin();
            _db.update( master2 );
            _db.commit();
            stream.println( "OK: Dirty checking works" );
        } catch ( ObjectModifiedException exept ) {
            _db.rollback();
            stream.println( "Error: Dirty checking doesn't work" );
            fail("dirty check failed");
        }
        detailIdA = detailA.getId();
        detail2Id = detail2.getId();

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
        master = (TestMasterKeyGen) _db.load( TestMasterKeyGen.class, new Integer( masterId ) );
        if ( master != null ) {
            if ( master.getDetails().size() == 0 ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailIdA ) ) ||
                 master.findDetail( detailIdA ).findDetail2( detail2Id ) == null  ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId6 ) ) ||
                 master.findDetail( detailId6 ).getDetails2() == null ||
                 master.findDetail( detailId6 ).getDetails2().size() != 2 ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId7 ) ) ||
                 master.getDetails().contains( new TestDetailKeyGen( detailId8 ) ) ||
                 master.getDetails().contains( new TestDetailKeyGen( detailId9 ) ) ) {
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
        detailA = master.findDetail( detailIdA );
        detailA.setValue1("new updated value");
        detailA.findDetail2( detail2Id ).setValue1("new detail 2 value");
        detail3 = new TestDetailKeyGen3();
        detailA.setDetail3( detail3 );
        detail6 = master.findDetail( detailId6 );
        detail6.getDetails2().clear();

        _db.begin();
        _db.update( master );
        _db.commit();
        detail3Id = detail3.getId();

        _db.begin();
        master = (TestMasterKeyGen) _db.load( TestMasterKeyGen.class, new Integer( masterId ) );
        if ( master != null ) {
            if ( master.getDetails() == null ||
                 master.getDetails().size() == 0 ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailIdA ) ) ||
                 master.findDetail( detailIdA ).getDetail3() == null ||
                 master.findDetail( detailIdA ).getDetail3().getId() != detail3Id ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId6 ) ) ||
                 master.findDetail( detailId6 ).getDetails2().size() != 0 ||
                 master.findDetail( detailId6 ).getDetail3() == null ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId7 ) ) ||
                 master.getDetails().contains( new TestDetailKeyGen( detailId8 ) ) ||
                 master.getDetails().contains( new TestDetailKeyGen( detailId9 ) ) ||
                 ! "new updated value".equals( master.findDetail( detailIdA ).getValue1()) ||
                 master.findDetail( detailIdA ).findDetail2( detail2Id ) == null ||
                 ! "new detail 2 value".equals( master.findDetail( detailIdA ).findDetail2( detail2Id ).getValue1() ) ) {

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

        // test unsetting one-one relationship
        detail6 = master.findDetail( detailId6 );
        detail6.setDetail3( null );
        _db.begin();
        _db.update( master );
        _db.commit();

        _db.begin();
        master = (TestMasterKeyGen) _db.load( TestMasterKeyGen.class, new Integer( masterId ) );
        if ( master != null ) {
            if ( master.getDetails() == null ||
                 master.getDetails().size() == 0 ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailIdA ) ) ||
                 master.findDetail( detailIdA ).getDetail3() == null ||
                 master.findDetail( detailIdA ).getDetail3().getId() != detail3Id ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId6 ) ) ||
                 master.findDetail( detailId6 ).getDetails2().size() != 0 ||
                 master.findDetail( detailId6 ).getDetail3() != null ||
                 ! master.getDetails().contains( new TestDetailKeyGen( detailId7 ) ) ||
                 master.getDetails().contains( new TestDetailKeyGen( detailId8 ) ) ||
                 master.getDetails().contains( new TestDetailKeyGen( detailId9 ) ) ||
                 ! "new updated value".equals( master.findDetail( detailIdA ).getValue1()) ||
                 master.findDetail( detailIdA ).findDetail2( detail2Id ) == null ||
                 ! "new detail 2 value".equals( master.findDetail( detailIdA ).findDetail2( detail2Id ).getValue1() ) ) {

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
