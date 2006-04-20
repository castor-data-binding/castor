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


import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Query;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.jdo.TimeStampable;


/**
 * Test object mapping to test_persistent used for Persistent test.
 */
public class TestPersistent implements Persistent, TimeStampable, java.io.Serializable
{


    static final int       DefaultId = 7;


    static final String    DefaultValue = "persistent";


    private int            _id;


    private String         _value;


    private Date           _creationTime;


    private Date           _modificationTime;


    private Integer        _parentId;


    private TestPersistent _parent;


    private Vector         _children;


    private Vector         _origChildren;


    private TestGroup      _group;


    private TestPersistRelated _related;


    private TestPersistRelated _origRelated;


    private transient Database _db;


    private long           _timeStamp;


    public TestPersistent()
    {
        this( DefaultId );
    }


    public TestPersistent( int id )
    {
        _id = id;
        _value = DefaultValue;
        _children = new Vector();
    }


    public void setId( int id )
    {
        _id = id;
    }


    public int getId()
    {
        return _id;
    }


    public void setParentId( Integer parentId )
    {
        _parentId = parentId;
    }


    public Integer getParentId()
    {
        return _parentId;
    }


    public void setValue1( String value )
    {
        _value = value;
    }


    public String getValue1()
    {
        return _value;
    }


    public void setCreationTime( Date creationTime )
    {
        _creationTime = creationTime;
    }


    public Date getCreationTime()
    {
        return _creationTime;
    }


    public void setModificationTime( Date modificationTime )
    {
        _modificationTime = modificationTime;
    }


    public Date getModificationTime()
    {
        return _modificationTime;
    }


    public void setParent( TestPersistent parent )
    {
        _parent = parent;
        _parentId = ( _parent == null ? null : new Integer( _parent._id ) );
    }


    public TestPersistent getParent()
    {
        return _parent;
    }


    public void addChild( TestPersistent child )
    {
        _children.addElement( child );
        child.setParent( this );
        child.setGroup( _group );
    }


    public Vector getChildren()
    {
        return _children;
    }


    public TestPersistent findChild(int id)
    {
        Enumeration enumeration;
        TestPersistent child;

        enumeration = _children.elements();
        while ( enumeration.hasMoreElements() ) {
            child = (TestPersistent) enumeration.nextElement();
            if ( child.getId() == id ) {
                return child;
            }
        }
        return null;
    }


    public void setGroup( TestGroup group )
    {
        TestPersistent child;

        if (_group == group) {
            return;
        }
        _group = group;
        for ( Enumeration enumeration = _children.elements(); enumeration.hasMoreElements(); ) {
            child = (TestPersistent) enumeration.nextElement();
            child.setGroup( _group );
        }
    }


    public TestGroup getGroup()
    {
        return _group;
    }


    public void setRelated( TestPersistRelated related )
    {
        _related = related;
        if (related != null) {
            related.setPersistent(this);
        }
    }


    public TestPersistRelated getRelated()
    {
        return _related;
    }


    public void jdoPersistent( Database db )
    {
        _db = db;
    }


    public void jdoTransient()
    {
        _db = null;
    }


    public Class jdoLoad(short accessMode)
        throws Exception
    {
        Query        qry;
        QueryResults res;

        if ( _parentId != null )
            _parent = (TestPersistent) _db.load( TestPersistent.class, _parentId, accessMode );

        qry = _db.getOQLQuery( "SELECT p FROM jdo.TestPersistent p WHERE parentId=$1" );
        qry.bind( _id );
        res = qry.execute();
        while ( res.hasMore() )
            _children.addElement( res.next() );
        _origChildren = (Vector) _children.clone();
        _origRelated = _related;
        return null;
    }


    public void jdoStore( boolean modified )
        throws Exception
    {
        TestPersistent child;

        if ( modified )
            _modificationTime = new Date();

        for ( Enumeration enumeration = _children.elements(); enumeration.hasMoreElements(); ) {
            child = (TestPersistent) enumeration.nextElement();
            if ( ! vectorContainsChild( _origChildren, child ) )
                _db.create( child );
        }
        for ( Enumeration enumeration = _origChildren.elements(); enumeration.hasMoreElements(); ) {
            child = (TestPersistent) enumeration.nextElement();
            if ( ! vectorContainsChild( _children, child ) )
                _db.remove( child );
        }
        if (_origRelated == null && _related != null) {
            _db.create(_related);
        }
        if (_origRelated != null && _related == null) {
            _db.remove(_origRelated);
        }
        _origRelated = _related;
    }


    public void jdoUpdate()
        throws Exception
    {
        TestPersistent child;

        for ( Enumeration enumeration = _origChildren.elements(); enumeration.hasMoreElements(); )
            _db.update( enumeration.nextElement() );
        if (_origRelated != null) {
            _db.update(_origRelated);
        }
    }


    public static boolean vectorContainsChild(Vector v, TestPersistent child) {
        TestPersistent ch;

        for ( Enumeration enumeration = v.elements(); enumeration.hasMoreElements(); ) {
            ch = (TestPersistent) enumeration.nextElement();
            if ( ch.getId() == child.getId() )
                return true;
        }
        return false;
    }


    public void jdoBeforeCreate( Database db )
        throws Exception
    {
        Object grp = null;

        if ( _group == null ) {
            throw new Exception("Incorrect object state: group is not set in " + this);
        }
        try {
            grp = db.load( TestGroup.class, new Integer( _group.getId() ) );
        } catch ( Exception ex ) {
        }
        if ( grp == null ) {
            db.create( _group );
        }
        _creationTime = new Date();
    }


    public void jdoAfterCreate()
        throws Exception
    {
        for ( Enumeration enumeration = _children.elements(); enumeration.hasMoreElements(); )
            _db.create( enumeration.nextElement() );
        _origChildren = (Vector) _children.clone();
        if (_related != null) {
            _db.create(_related);
        }
        _origRelated = _related;
    }


    public void jdoBeforeRemove()
        throws Exception
    {
        for ( Enumeration enumeration = _children.elements(); enumeration.hasMoreElements(); )
            _db.remove( enumeration.nextElement() );
        if (_related != null) {
            _db.remove(_related);
        }
    }


    public void jdoAfterRemove()
        throws Exception
    {
    }


    public long jdoGetTimeStamp()
    {
        return _timeStamp;
    }


    public void jdoSetTimeStamp( long timeStamp )
    {
        _timeStamp = timeStamp;
    }


    public String toString()
    {
        String children = "";

        for ( int i = 0 ; i < _children.size() ; ++i ) {
            if ( i > 0 )
                children = children + ", ";
            children = children + _children.elementAt( i ).toString();
        }
        return _id + " / " + _value + " / " + _modificationTime + " (" + _parentId + 
                ":" + ( _group != null ? new Integer( _group.getId() ) : null ) + 
                ") { " + children + " }";
    }


}
