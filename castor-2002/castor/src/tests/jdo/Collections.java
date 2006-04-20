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
 * Only for JDK 1.2
 */
public class Collections
    extends CWTestCase
{


    private Connection     _conn;


    private JDOCategory    _category;


    public Collections( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC19", "Collections tests" );
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
            TestSetMaster    master;
            TestGroup     group;
            TestSetDetail    detail;
            QueryResults  qres;
            TestSetMaster    master2;
            int           cnt;
            
            db = _category.getDatabase( stream.verbose() );

            stream.writeVerbose( "Delete everything" );
            db.begin();
            oql = db.getOQLQuery( "SELECT master FROM jdo.TestSetMaster master" );
            qres = oql.execute();
            
            for ( cnt = 0; qres.hasMore(); cnt++ ) {
                db.remove( qres.next() );
            }
            stream.writeVerbose( "Deleting " + cnt + " master objects" );
            oql = db.getOQLQuery( "SELECT detail FROM jdo.TestSetDetail detail" );
            qres = oql.execute();
            for ( cnt = 0; qres.hasMore(); cnt++ ) {
                db.remove( qres.nextElement() );
            }
            stream.writeVerbose( "Deleting " + cnt + " detail objects" );
            db.commit();
            
            stream.writeVerbose( "Attempt to create master with details" );
            db.begin();
            master = new TestSetMaster();
            master.addDetail( new TestSetDetail( 5 ) );
            master.addDetail( new TestSetDetail( 6 ) );
            master.addDetail( new TestSetDetail( 7 ) );
            db.create( master );
            db.commit();
            db.begin();
            master = (TestSetMaster) db.load( TestSetMaster.class, new BigDecimal( TestSetMaster.DefaultId ) );
            if ( master != null ) {
                if ( master.getDetails() == null ||
                     ! master.getDetails().contains( new TestSetDetail( 5 ) ) ||
                     ! master.getDetails().contains( new TestSetDetail( 6 ) ) ||
                     ! master.getDetails().contains( new TestSetDetail( 7 ) ) ) {
                    stream.writeVerbose( "Error: loaded master without three details: " + master );
                    result  = false;
                }
            } else {
                stream.writeVerbose( "Error: failed to create master with details and group" );
                result = false;
            }
            if ( result )
                stream.writeVerbose( "Created master with details: " + master );
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
