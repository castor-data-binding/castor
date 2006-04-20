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
        super( "TC24", "Dependent objects tests" );
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
            oql.close();
            stream.writeVerbose( "Deleting " + cnt + " master objects" );
			/*
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
			*/
            oql = db.getOQLQuery( "SELECT group FROM jdo.TestGroup group" );
            qres = oql.execute();
            for ( cnt = 0; qres.hasMore(); cnt++ ) {
                db.remove( qres.nextElement() );
            }
            oql.close();
            stream.writeVerbose( "Deleting " + cnt + " group objects" );
            db.commit();

            stream.writeVerbose( "Attempt to create master with details" );
            db.begin();
            master = new TestMaster();
            master.addDetail( new TestDetail( 5 ) );
            detail = new TestDetail( 6 );
            detail.addDetail2( new TestDetail2() );
            detail.addDetail2( new TestDetail2() );
            master.addDetail( detail );
            detail = new TestDetail( 7 );
            detail.addDetail2( new TestDetail2() );
            detail.addDetail2( new TestDetail2() );
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
                     ! master.getDetails().contains( new TestDetail( 5 ) ) ||
                     ! master.getDetails().contains( new TestDetail( 6 ) ) ||
                     ! master.getDetails().contains( new TestDetail( 7 ) ) ) {
                    stream.writeVerbose( "Error: loaded master without three details: " + master );
                    result  = false;
                }
                detail = master.findDetail( 5 );
                if ( detail.getDetails2() != null && detail.getDetails2().size() != 0 ) {
                    stream.writeVerbose( "Error: loaded detail 5 with details2: " + qres.next() );
                    result = false;
                }
                detail = master.findDetail( 6 );
                if ( detail.getDetails2() == null || detail.getDetails2().size() != 2) {
                    stream.writeVerbose( "Error: loaded detail 6 without two details: " + detail );
                    result  = false;
                }
                detail = master.findDetail( 7 );
                if ( detail.getDetails2() == null || detail.getDetails2().size() != 2) {
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
            master.getDetails().remove( master.getDetails().indexOf( master.findDetail( 5 ) ) );
            // remove detail with id == 6 explicitly
            detail = (TestDetail) master.findDetail( 6 );
            master.getDetails().remove( master.getDetails().indexOf( detail ) );
            //db.remove( detail );
            // add new detail
            master.addDetail( new TestDetail( 8 ) );
            // add new detail and create it explicitely
            detail = new TestDetail( 9 );
            master.addDetail( detail );
            //db.create( detail );
            // delete, then create detail with id == 7 explicitly
            detail = (TestDetail) master.findDetail( 7 );
            master.getDetails().remove( master.getDetails().indexOf( detail ) );
            //db.remove( detail );
            master.addDetail( detail );
            //db.create( detail );
            db.commit();
            db.begin();
            master = (TestMaster) db.load( TestMaster.class, new Integer( TestMaster.DefaultId ) );
            if ( master != null ) {
                if ( master.getDetails().size() == 0 ||
                     master.getDetails().contains( new TestDetail( 5 ) ) ||
                     master.getDetails().contains( new TestDetail( 6 ) ) ||
                     ! master.getDetails().contains( new TestDetail( 7 ) ) ||
                     ! master.getDetails().contains( new TestDetail( 8 ) ) ||
                     ! master.getDetails().contains( new TestDetail( 9 ) ) ) {
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

            stream.writeVerbose( "Test OQL query" );
            db.begin();
            oql = db.getOQLQuery( "SELECT master FROM jdo.TestMaster master WHERE master.details.value1=$1" );
            oql.bind(TestDetail.DefaultValue);
            qres = oql.execute();
            if ( qres.hasMore() ) {
                stream.writeVerbose( "OK: correct result of query 1 " );
            } else {
                stream.writeVerbose( "Error: incorrect result of query 1 " );
                result = false;
            }
            oql.bind(TestDetail.DefaultValue + "*");
            qres = oql.execute();
            if ( qres.hasMore() ) {
                stream.writeVerbose( "Error: incorrect result of query 2 " );
                result = false;
            } else {
                stream.writeVerbose( "OK: correct result of query 2 " );
            }
            oql.close();
            oql = db.getOQLQuery( "SELECT master FROM jdo.TestMaster master WHERE master.details.details2.value1=$1" );
            oql.bind(TestDetail2.DefaultValue);
            qres = oql.execute();
            if ( qres.hasMore() ) {
                stream.writeVerbose( "OK: correct result of query 3 " );
            } else {
                stream.writeVerbose( "Error: incorrect result of query 3 " );
                result = false;
            }
            oql.bind(TestDetail2.DefaultValue + "*");
            qres = oql.execute();
            if ( qres.hasMore() ) {
                stream.writeVerbose( "Error: incorrect result of query 4 " );
                result = false;
            } else {
                stream.writeVerbose( "OK: correct result of query 4 " );
            }
            oql.close();
            oql = db.getOQLQuery( "SELECT master FROM jdo.TestMaster master WHERE master.group=$1" );
            oql.bind(group);
            qres = oql.execute();
            if ( qres.hasMore() ) {
                stream.writeVerbose( "OK: correct result of query 5 " );
            } else {
                stream.writeVerbose( "Error: incorrect result of query 5 " );
                result = false;
            }
            oql.close();
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
