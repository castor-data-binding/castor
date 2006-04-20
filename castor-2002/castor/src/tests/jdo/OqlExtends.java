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
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;


/**
 */
public class OqlExtends
    extends CWTestCase
{


    private JDOCategory    _category;


    public OqlExtends( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC30", "OQL queries for extends" );
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
        Date date;

        try {
            OQLQuery         oqlAll;
            OQLQuery         oql;
            TestOqlExtends   t;
            TestGroup        group1 = new TestGroup();
            TestGroup        group2 = new TestGroup();
            QueryResults     res;
            int              cnt;

            group2.setId(TestGroup.DefaultId + 1);

            db = _category.getDatabase( stream.verbose() );
            db.begin();
            oqlAll = db.getOQLQuery( "SELECT t FROM jdo.TestOqlExtends t" );
            res = oqlAll.execute();
            while ( res.hasMore() ) {
                db.remove( res.next() );
            }
            oql = db.getOQLQuery( "SELECT t FROM jdo.TestPersistent t" );
            res = oql.execute();
            while ( res.hasMore() ) {
                db.remove( res.next() );
            }
            oql.close();
            oql = db.getOQLQuery( "SELECT t FROM jdo.TestGroup t" );
            res = oql.execute();
            while ( res.hasMore() ) {
                db.remove( res.next() );
            }
            oql.close();
            db.commit();
            db.begin();
            stream.writeVerbose( "Creating group: " + group1 );
            db.create( group1 );
            stream.writeVerbose( "Creating group: " + group2 );
            db.create( group2 );
            t = new TestOqlExtends();
            t.setId(TestOqlExtends.DefaultId + 10);
            t.setGroup(group1);
            stream.writeVerbose( "Creating new object: " + t );
            db.create( t );
            date = new Date();
            t = new TestOqlExtends();
            t.setId(TestOqlExtends.DefaultId + 11);
            t.setGroup(group2);
            stream.writeVerbose( "Creating new object: " + t );
            db.create( t );
            db.commit();
            db.begin();
            oql.close();
            oql = db.getOQLQuery( "SELECT t FROM jdo.TestOqlExtends t WHERE t.group.id=$1" );
            oql.bind( group1.getId() );
            res = oql.execute();
            for ( cnt = 0; res.hasMore(); cnt++ ) {
                t = (TestOqlExtends) res.next();
                stream.writeVerbose( "Retrieved object: " + t );
                if (t.getExt() != 0) {
                    stream.writeVerbose( "Error: ext field = " + t.getExt());
                    result = false;
                }
            }
            oql.close();
            if (cnt == 1) {
                stream.writeVerbose( "OK" );
            } else {
                stream.writeVerbose( "Error: retrieved " + cnt + " objects");
                result = false;
            }

            oql = db.getOQLQuery( "SELECT t FROM jdo.TestPersistent t WHERE t.creationTime<$1" );
            oql.bind( date );
            res = oql.execute();
            for ( cnt = 0; res.hasMore(); cnt++ ) {
                t = (TestOqlExtends) res.next();
                stream.writeVerbose( "Retrieved object: " + t );
            }
            oql.close();
            if (cnt == 1) {
                stream.writeVerbose( "OK" );
            } else {
                stream.writeVerbose( "Error: retrieved " + cnt + " objects");
                result = false;
            }

            res = oqlAll.execute();
            while ( res.hasMore() ) {
                db.remove( res.next() );
            }

            db.commit();
            oqlAll.close();

            db.close();
        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }


}
