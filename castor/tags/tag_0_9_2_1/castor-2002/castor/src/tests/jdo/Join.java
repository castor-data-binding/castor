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
import org.exolab.castor.jdo.DataObjects;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;


/**
 */
public class Join
    extends CWTestCase
{


    private JDOCategory    _category;


    public Join( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC21", "Outer join tests" );
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
            QueryResults   enum;
            TestMaster    master2;
            
            db = _category.getDatabase( stream.verbose() );
            db.begin();
            oql = db.getOQLQuery( "SELECT master FROM jdo.TestMaster master WHERE id = $1" );
            oql.bind( TestMaster.DefaultId );
            enum = oql.execute();
            while ( enum.hasMore() ) {
                master = (TestMaster) enum.next();
                group = master.getGroup();
                if ( group != null ) {
                    master.setGroup( null );
                    db.remove( group );
                }
                db.remove( master );
                stream.writeVerbose( "Deleting old master" );
            }
            groupOql = db.getOQLQuery( "SELECT group FROM jdo.TestGroup group WHERE id = $1" );
            groupOql.bind( TestGroup.DefaultId );
            enum = groupOql.execute();
            if ( enum.hasMore() ) {
                group = (TestGroup) enum.next();
                stream.writeVerbose( "Using existing group: " + group );
            } else {
                group = new TestGroup();
                db.create( group );
                stream.writeVerbose( "Creating new group: " + group );
            }
            db.commit();


            stream.writeVerbose( "Attempt to create master with no group" );
            db.begin();
            master = new TestMaster();
            db.create( master );
            db.commit();
            db.begin();
            oql.bind( TestMaster.DefaultId );
            enum = oql.execute();
            if ( enum.hasMore() ) {
                master = (TestMaster) enum.next();
                db.remove( master );
                stream.writeVerbose( "Created master with no group: " + master );
            } else {
                stream.writeVerbose( "Error: failed to create master with no group" );
                result = false;
            }
            db.commit();
            if ( ! result )
                return false;


            stream.writeVerbose( "Attempt to create master with a group" );
            db.begin();
            groupOql.bind( TestGroup.DefaultId );
            group = (TestGroup) groupOql.execute().nextElement();
            master = new TestMaster();
            master.setGroup( group );
            db.create( master );
            db.commit();
            db.begin();
            oql.bind( TestMaster.DefaultId );
            enum = oql.execute();
            if ( enum.hasMore() ) {
                master = (TestMaster) enum.next();
                if ( master.getGroup() == null || master.getGroup().getId() != TestGroup.DefaultId ) {
                    stream.writeVerbose( "Error: loaded master without any group: " + master );
                    result  = false;
                } else {
                    db.remove( master.getGroup() );
                    master.setGroup( null );
                    db.remove( master );
                    stream.writeVerbose( "Created master with group: " + master );
                }
            } else {
                stream.writeVerbose( "Error: failed to create master with group" );
                result = false;
            }
            db.commit();
            if ( ! result )
                return false;
            

            stream.writeVerbose( "Attempt to create master with details" );
            db.begin();
            master = new TestMaster();
            master.addDetail( new TestDetail( TestDetail.DefaultId ) );
            master.addDetail( new TestDetail( TestDetail.DefaultId + 1 ) );
            master.addDetail( new TestDetail( TestDetail.DefaultId + 2 ) );
            db.create( master );
            db.commit();
            db.begin();
            oql.bind( TestMaster.DefaultId );
            enum = oql.execute();
            if ( enum.hasMore() ) {
                master = (TestMaster) enum.next();
                if ( master.getDetails().size() == 0 ||
                     ! master.getDetails().contains( new TestDetail( TestDetail.DefaultId ) ) ||
                     ! master.getDetails().contains( new TestDetail( TestDetail.DefaultId + 1 ) ) ||
                     ! master.getDetails().contains( new TestDetail( TestDetail.DefaultId + 2 ) ) ) {
                    stream.writeVerbose( "Error: loaded master without three details: " + master );
                    result  = false;
                } else {
                    db.remove( master );
                    stream.writeVerbose( "Created master with details: " + master );
                }
            } else {
                stream.writeVerbose( "Error: failed to create master with details group" );
                result = false;
            }
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
