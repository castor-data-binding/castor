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
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;


/**
 */
public class Dependent
    extends CWTestCase
{


    private Connection     _conn;


    private JDOCategory    _category;


    public Dependent( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC15", "Dependent objects tests" );
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
            OQLQuery      oql;
            OQLQuery      groupOql;
            TestMaster    master;
            TestGroup     group;
            TestDetail    detail;
            QueryResults  qres;
            TestMaster    master2;
            int           cnt;
            
            db = _category.getDatabase( stream.verbose() );

            stream.writeVerbose( "Delete everything" );
            db.begin();
            oql = db.getOQLQuery( "SELECT master FROM jdo.TestMaster master" );
            qres = oql.execute();
            
            for ( cnt = 0; qres.hasMore(); cnt++ ) {
                db.remove( qres.next() );
            }
            stream.writeVerbose( "Deleting " + cnt + " master objects" );
            oql = db.getOQLQuery( "SELECT detail FROM jdo.TestDetail detail" );
            qres = oql.execute();
            for ( cnt = 0; qres.hasMore(); cnt++ ) {
                db.remove( qres.nextElement() );
            }
            stream.writeVerbose( "Deleting " + cnt + " detail objects" );
            oql = db.getOQLQuery( "SELECT detail2 FROM jdo.TestDetail2 detail2" );
            qres = oql.execute();
            for ( cnt = 0; qres.hasMore(); cnt++ ) {
                db.remove( qres.nextElement() );
            }
            stream.writeVerbose( "Deleting " + cnt + " detail2 objects" );
            oql = db.getOQLQuery( "SELECT group FROM jdo.TestGroup group" );
            qres = oql.execute();
            for ( cnt = 0; qres.hasMore(); cnt++ ) {
                db.remove( qres.nextElement() );
            }
            stream.writeVerbose( "Deleting " + cnt + " group objects" );
            db.commit();
            
            stream.writeVerbose( "Attempt to create master with details" );
            db.begin();
            master = new TestMaster();
            master.addDetail( new TestDetail( 5 ) );
            detail = new TestDetail( 6 );
            detail.addDetail2( new TestDetail2( 61 ) );
            detail.addDetail2( new TestDetail2( 62 ) );
            master.addDetail( detail );
            detail = new TestDetail( 7 );
            detail.addDetail2( new TestDetail2( 71 ) );
            detail.addDetail2( new TestDetail2( 72 ) );
            master.addDetail( detail );
            group = new TestGroup();
            db.create( group );
            master.setGroup( group );
            db.create( master );
            db.commit();
            db.begin();
            master = (TestMaster) db.load( TestMaster.class, new Integer( TestMaster.DefaultId ) );
            if ( master != null ) {
                if ( master.getGroup() == null ) {
                    stream.writeVerbose( "Error: loaded master without group: " + master );
                    result  = false;
                } else if ( master.getGroup().getId() != TestGroup.DefaultId ) {
                    stream.writeVerbose( "Error: loaded master with wrong group: " + master );
                    result  = false;
                }
                if ( master.getDetails() == null ||
                     master.findDetail( 5 ) == null ||
                     master.findDetail( 6 ) == null  ||
                     master.findDetail( 7 ) == null  ) {
                    stream.writeVerbose( "Error: loaded master without three details: " + master );
                    result  = false;
                }
                detail = master.findDetail( 5 );
                if ( detail.getDetails2() != null && detail.getDetails2().size() != 0 ) {
                    stream.writeVerbose( "Error: loaded detail 5 with details2: " + qres.next() );
                    result = false;
                }
                detail = master.findDetail( 6 );
                if ( detail.getDetails2() == null ||
                     detail.findDetail2( 61 ) == null ||
                     detail.findDetail2( 62 ) == null ) {
                    stream.writeVerbose( "Error: loaded detail 6 without two details: " + detail );
                    result  = false;
                }
                detail = master.findDetail( 7 );
                if ( detail.getDetails2() == null ||
                     detail.findDetail2( 71 ) == null ||
                     detail.findDetail2( 72 ) == null ) {
                    stream.writeVerbose( "Error: loaded detail 7 without two details: " + detail );
                    result  = false;
                }
            } else {
                stream.writeVerbose( "Error: failed to create master with details and group" );
                result = false;
            }
            if ( result )
                stream.writeVerbose( "Created master with details: " + master );
            db.commit();
            if ( ! result )
                return false;


            stream.writeVerbose( "Attempt to change details" );
            db.begin();
            master = (TestMaster) db.load( TestMaster.class, new Integer( TestMaster.DefaultId ) );
            if ( master == null ) {
                stream.writeVerbose( "Error: failed to find master with details group" );
                return false;
            }
            // remove detail with id == 5
            master.getDetails().removeElement( master.findDetail( 5 ) );
            // remove detail with id == 6 explicitly
            detail = (TestDetail) master.findDetail( 6 );
            master.getDetails().removeElement( detail );
            // add new detail
            master.addDetail( new TestDetail( 8 ) );
            // add new detail and create it explicitely
            detail = new TestDetail( 9 );
            master.addDetail( detail );
            // delete, then create detail with id == 7 explicitly
            detail = (TestDetail) master.findDetail( 7 );
            master.getDetails().removeElement( detail );
            master.addDetail( detail );
            db.commit();
            db.begin();
            master = (TestMaster) db.load( TestMaster.class, new Integer( TestMaster.DefaultId ) );
            if ( master != null ) {
                if ( master.getDetails().size() == 0 ||
                     master.findDetail( 5 ) != null ||
                     master.findDetail( 6 ) != null ||
                     master.findDetail( 7 ) == null ||
                     master.findDetail( 8 ) == null ||
                     master.findDetail( 9 ) == null ) {
                    stream.writeVerbose( "Error: loaded master has wrong set of details: " + master );
                    result  = false;
                } else {
                    stream.writeVerbose( "Details changed correctly: " + master );
                }
            } else {
                stream.writeVerbose( "Error: master not found" );
                result = false;
            }
            db.commit();
            if ( ! result )
                return false;


            stream.writeVerbose( "Test long transaction with dirty checking" );
            db.begin();
            master = (TestMaster) db.load( TestMaster.class, new Integer( TestMaster.DefaultId ) );
            if ( master == null ) {
                stream.writeVerbose( "Error: failed to find master with details group" );
                return false;
            }
            db.commit();
            db.begin();
            master2 = (TestMaster) db.load( TestMaster.class, new Integer( TestMaster.DefaultId ) );
            master2.setValue( master2.getValue() + "2" );
            db.commit();

            stream.writeVerbose( "Test 1" );
            try {
                db.begin();
                db.update( master );
                db.commit();
                stream.writeVerbose( "Error: Dirty checking doesn't work" );
                result  = false;
            } catch ( ObjectModifiedException exept ) {
                db.rollback();
                stream.writeVerbose( "OK: Dirty checking works" );
            }
 
            stream.writeVerbose( "Test 2" );
            master2.addDetail( new TestDetail( 5 ) );
            master2.addDetail( new TestDetail( 6 ) );
            master2.getDetails().removeElement( master2.findDetail( 8 ) );
            master2.getDetails().removeElement( master2.findDetail( 9 ) );
            try {
                db.begin();
                db.update( master2 );
                db.commit();
                stream.writeVerbose( "OK: Dirty checking works" );
            } catch ( ObjectModifiedException exept ) {
                db.rollback();
                stream.writeVerbose( "Error: Dirty checking doesn't work" );
                result  = false;
            }
            stream.writeVerbose( "Test 3" );
            _conn = _category.getJDBCConnection(); 
            _conn.setAutoCommit( false );
            _conn.createStatement().execute( "UPDATE test_master SET value='concurrent' WHERE id=" 
                    + master2.getId() );
            _conn.commit();
            _conn.close();
            master2.addDetail( new TestDetail( 10 ) );
            try {
                db.begin();
                db.update( master2 );
                db.commit();
                stream.writeVerbose( "Error: Dirty checking doesn't work" );
                result  = false;
            } catch ( ObjectModifiedException exept ) {
                if (db.isActive()) {
                    db.rollback();
                }
                stream.writeVerbose( "OK: Dirty checking works" );
            }
            db.begin();
            master = (TestMaster) db.load( TestMaster.class, new Integer( TestMaster.DefaultId ) );
            if ( master != null ) {
                if ( master.getDetails().size() == 0 ||
                     master.findDetail( 5 ) == null ||
                     master.findDetail( 6 ) == null  ||
                     master.findDetail( 7 ) == null  ||
                     master.findDetail( 8 ) != null  ||
                     master.findDetail( 9 ) != null  ) {
                    stream.writeVerbose( "Error: loaded master has wrong set of details: " + master );
                    result  = false;
                } else {
                    stream.writeVerbose( "Details changed correctly in the long transaction: " + master );
                }
            } else {
                stream.writeVerbose( "Error: master not found" );
                result = false;
            }
            db.remove( master );
            db.commit();
            if ( ! result )
                return false;


            db.close();
        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }


}
