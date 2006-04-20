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
import org.exolab.castor.jdo.DataObjects;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.TransactionAbortedException;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Assert;
import harness.TestHarness;
import harness.CastorTestCase;

/**
 * Test for the behaviors the persistent interface. A data object
 * that implements the persistence interaface is notified by 
 * Castor for loading, creating and storing events.
 */
public class Persistent extends CastorTestCase {


    private JDOCategory    _category;

    private Database       _db;

    public Persistent( TestHarness category ) {
        super( category, "TC47", "Persistence interface tests" );
        _category = (JDOCategory) category;
    }

    public void setUp() 
            throws PersistenceException {
        _db = _category.getDatabase();
    }

    public void runTest() 
            throws PersistenceException {

        OQLQuery      oql;
        TestPersistent parent;
        TestPersistent child;
        QueryResults  qres;
        long          beforeModTime;
        long          afterModTime;
        long          modTime;
        TestPersistRelated related;

        stream.println( "Delete everything" );
        _db.begin();
        oql = _db.getOQLQuery( "SELECT p FROM jdo.TestPersistent p WHERE id=$1" );
        oql.bind( TestPersistent.DefaultId );
        qres = oql.execute();
        while ( qres.hasMore() ) {
            _db.remove( qres.next() );
        }
        oql.close();
        oql = _db.getOQLQuery( "SELECT g FROM jdo.TestGroup g" );
        qres = oql.execute();
        while ( qres.hasMore() ) {
            _db.remove( qres.next() );
        }
        oql.close();
        oql = _db.getOQLQuery( "SELECT r FROM jdo.TestPersistRelated r" );
        qres = oql.execute();
        while ( qres.hasMore() ) {
            _db.remove( qres.next() );
        }
        oql.close();
        _db.commit();

        stream.println( "Attempt to create parent with children" );
        _db.begin();
        parent = new TestPersistent();
        parent.setGroup( new TestGroup() );
        parent.addChild( new TestPersistent( 71 ) );
        parent.addChild( new TestPersistent( 72 ) );
        child = new TestPersistent( 73 );
        child.addChild( new TestPersistent( 731 ) );
        child.addChild( new TestPersistent( 732 ) );
        parent.addChild( child );
        related = new TestPersistRelated();
        parent.setRelated( related );
        _db.create( parent );
        _db.commit();
        _db.begin();
        parent = (TestPersistent) _db.load( TestPersistent.class, new Integer( TestPersistent.DefaultId ) );
        if ( parent != null ) {
            if ( parent.getGroup() == null ||
                    parent.getGroup().getId() != TestGroup.DefaultId ) {
                stream.println( "Error: loaded parent without group: " + parent );
                fail("group not found");
            }
            if ( parent.getRelated() == null ) {
                stream.println( "Error: loaded parent without related: " + parent );
                fail("related not found");
            }
            if ( parent.getChildren() == null || parent.getChildren().size() != 3 ||
                 parent.findChild( 71 ) == null ||
                 parent.findChild( 72 ) == null ||
                 parent.findChild( 73 ) == null ) {
                stream.println( "Error: loaded parent without three children: " + parent );
                fail("children size mismatched");
            }
            child = parent.findChild( 73 );
            if ( child == null || child.getChildren() == null ||
                 child.getChildren().size() != 2 ||
                 child.findChild( 731 ) == null ||
                 child.findChild( 732 ) == null ) {
                stream.println( "Error: loaded child without two grandchildren: " + child );
                fail("garndchildren not found");
            }
            child.setValue1("new value");
        } else {
            stream.println( "Error: failed to create parent with children" );
            fail("failed to create parent with children");
        }


        stream.println( "Created parent with children: " + parent );
        beforeModTime = System.currentTimeMillis() / 1000;
        _db.commit();
        afterModTime = System.currentTimeMillis() / 1000;
        _db.begin();
        parent = (TestPersistent) _db.load( TestPersistent.class, new Integer( 7 ) );
        child = parent.findChild( 73 );
        if ( child == null ) {
            stream.println( "Error: child not loaded" );
            fail("child load failed");
        } else if ( child.getModificationTime() == null ) {
            stream.println( "Error: wrong modification time: " + child );
            fail("modification time incorrect");
        } else {
            modTime = child.getModificationTime().getTime() / 1000;
            if ( modTime < beforeModTime || modTime  > afterModTime ) {
                stream.println( "Error: wrong modification time: " + child );
                fail("modificationo time incorrect");
            }
        }
        _db.commit();

        stream.println( "Long transaction test" );
        parent.setValue1( "long transaction parent" );
        parent.getChildren().removeElement( parent.findChild( 71 ) );
        child = new TestPersistent( 74 );
        child.setValue1( "long transaction child" );
        child.addChild( new TestPersistent( 741 ) );
        parent.addChild( child );
        parent.findChild( 73 ).getChildren().removeElement(
                parent.findChild( 73 ).findChild( 731 ) );
        parent.findChild( 73 ).addChild( new TestPersistent( 733 ) );
        _db.begin();
        _db.update( parent );
        _db.commit();
        _db.begin();
        parent = (TestPersistent) _db.load( TestPersistent.class, new Integer( TestPersistent.DefaultId ) );
        if ( parent != null ) {
            if ( parent.getChildren() == null || parent.getChildren().size() != 3 ||
                 ! "long transaction parent".equals( parent.getValue1() ) ||
                 parent.findChild( 71 ) != null ||
                 parent.findChild( 72 ) == null ||
                 parent.findChild( 73 ) == null ||
                 parent.findChild( 74 ) == null ) {
                stream.println( "Error: loaded parent without three children: " + parent );
                fail("children size mismatched");
            }
            child = parent.findChild( 73 );
            if ( child == null || child.getChildren() == null || 
                 child.getChildren().size() != 2 ||
                 child.findChild( 731 ) != null ||
                 child.findChild( 732 ) == null ||
                 child.findChild( 733 ) == null ) {
                stream.println( "Error: loaded child without two grandchildren: " + child );
                fail("grandchildren size mismatched");
            }
            child = parent.findChild( 74 );
            if ( child == null || child.getChildren() == null || 
                 child.getChildren().size() != 1 ||
                 ! "long transaction child".equals( child.getValue1() ) ||
                 child.findChild( 741 ) == null ) {
                stream.println( "Error: loaded child without one grandchildren: " + child );
                fail("grandchildren size mismatched");
            }
        } else {
            stream.println( "Error: failed to create parent with children" );
            fail("failed to create parent with children");
        }

        stream.println( "Created parent with children: " + parent );
        _db.commit();

    }

    public void tearDown()
            throws PersistenceException {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
    }

}
