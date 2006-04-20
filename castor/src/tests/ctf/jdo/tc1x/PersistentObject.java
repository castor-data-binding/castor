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

package ctf.jdo.tc1x;

import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Query;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.jdo.TimeStampable;
import org.exolab.castor.mapping.AccessMode;

public final class PersistentObject implements Persistent, TimeStampable, Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = -2190482779395735049L;
    
    public static final int       DEFAULT_ID = 7;
    public static final String    DEFAULT_VALUE = "persistent";

    private int                 _id;
    private String              _value;
    private Date                _creationTime;
    private Date                _modificationTime;
    private Integer             _parentId;
    private PersistentObject    _parent;
    private Vector              _children;
    private Vector              _origChildren;
    private PersistentGroup     _group;
    private PersistentRelated   _related;
    private PersistentRelated   _origRelated;

    private long                _timeStamp;

    private transient Database  _db;

    public PersistentObject() {
        this(DEFAULT_ID);
    }

    public PersistentObject(final int id) {
        _id = id;
        _value = DEFAULT_VALUE;
        _children = new Vector();
    }

    public void setId(final int id) { _id = id; }
    public int getId() { return _id; }

    public void setCreationTime(final Date creationTime) {
        _creationTime = creationTime;
    }
    
    public Date getCreationTime() { return _creationTime; }

    public void setModificationTime(final Date modificationTime) {
        _modificationTime = modificationTime;
    }
    
    public Date getModificationTime() { return _modificationTime; }

    public void setValue1(final String value) { _value = value; }
    public String getValue1() { return _value; }

    public void setParentId(final Integer parentId) { _parentId = parentId; }
    public Integer getParentId() { return _parentId; }

    public void setParent(final PersistentObject parent) {
        _parent = parent;
        _parentId = (_parent == null ? null : new Integer(_parent._id));
    }
    
    public PersistentObject getParent() { return _parent; }

    public void addChild(final PersistentObject child) {
        _children.addElement(child);
        child.setParent(this);
        child.setGroup(_group);
    }
    
    public Vector getChildren() { return _children; }

    public PersistentObject findChild(final int id) {
        Enumeration enumeration = _children.elements();
        PersistentObject child;
        while (enumeration.hasMoreElements()) {
            child = (PersistentObject) enumeration.nextElement();
            if (child.getId() == id) {
                return child;
            }
        }
        return null;
    }

    public void setGroup(final PersistentGroup group) {
        if (_group != group) {
            _group = group;
            
            Enumeration enumeration = _children.elements();
            PersistentObject child;
            while (enumeration.hasMoreElements()) {
                child = (PersistentObject) enumeration.nextElement();
                child.setGroup(_group);
            }
        }
    }

    public PersistentGroup getGroup() { return _group; }

    public void setRelated(final PersistentRelated related) {
        _related = related;
        if (related != null) {
            related.setPersistent(this);
        }
    }

    public PersistentRelated getRelated() { return _related; }

    public void jdoPersistent(final Database db) { _db = db; }

    public void jdoTransient() { _db = null; }

    public Class jdoLoad(final AccessMode accessMode) throws Exception {
        if (_parentId != null) {
            _parent = (PersistentObject) _db.load(PersistentObject.class,
                                                _parentId, accessMode);
        }

        Query qry = _db.getOQLQuery("SELECT p FROM "
                + PersistentObject.class.getName() + " p WHERE parentId=$1");
        qry.bind(_id);
        QueryResults res = qry.execute();
        while (res.hasMore()) {
            _children.addElement(res.next());
        }
        
        _origChildren = (Vector) _children.clone();
        _origRelated = _related;
        return null;
    }

    public void jdoStore(final boolean modified) throws Exception {
        Enumeration enumeration;
        PersistentObject child;

        if (modified) {
            _modificationTime = new Date();
        }

        enumeration = _children.elements();
        while (enumeration.hasMoreElements()) {
            child = (PersistentObject) enumeration.nextElement();
            if (!containsChild(_origChildren, child)) {
                _db.create(child);
            }
        }
        enumeration = _origChildren.elements();
        while (enumeration.hasMoreElements()) {
            child = (PersistentObject) enumeration.nextElement();
            if (!containsChild(_children, child)) {
                _db.remove(child);
            }
        }
        
        if ((_origRelated == null) && (_related != null)) {
            _db.create(_related);
        }
        if ((_origRelated != null) && (_related == null)) {
            _db.remove(_origRelated);
        }
        _origRelated = _related;
    }

    public void jdoUpdate() throws Exception {
        Enumeration enumeration = _origChildren.elements();
        while (enumeration.hasMoreElements()) {
            _db.update(enumeration.nextElement());
        }

        if (_origRelated != null) {
            _db.update(_origRelated);
        }
    }

    public static boolean containsChild(final Vector vector,
                                        final PersistentObject child) {
        
        Enumeration enumeration = vector.elements();
        PersistentObject ch;
        while (enumeration.hasMoreElements()) {
            ch = (PersistentObject) enumeration.nextElement();
            if (ch.getId() == child.getId()) {
                return true;
            }
        }
        return false;
    }

    public void jdoBeforeCreate(final Database db) throws Exception {
        if (_group == null) {
            throw new Exception(
                    "Incorrect object state: group is not set in " + this);
        }
        
        Object grp = null;
        try {
            grp = db.load(PersistentGroup.class, new Integer(_group.getId()));
        } catch (Exception ex) {
        }
        if (grp == null) {
            db.create(_group);
        }
        _creationTime = new Date();
    }

    public void jdoAfterCreate() throws Exception {
        Enumeration enumeration = _children.elements();
        while (enumeration.hasMoreElements()) {
            _db.create(enumeration.nextElement());
        }
        _origChildren = (Vector) _children.clone();
        
        if (_related != null) {
            _db.create(_related);
        }
        _origRelated = _related;
    }

    public void jdoBeforeRemove() throws Exception {
        Enumeration enumeration = _children.elements();
        while (enumeration.hasMoreElements()) {
            _db.remove(enumeration.nextElement());
        }
        
        if (_related != null) {
            _db.remove(_related);
        }
    }

    public void jdoAfterRemove() throws Exception { }

    public long jdoGetTimeStamp() {
        return _timeStamp;
    }

    public void jdoSetTimeStamp(final long timeStamp) {
        _timeStamp = timeStamp;
    }

    public String toString() {
        String children = "";
        for (int i = 0; i < _children.size(); i++) {
            if (i > 0) {
                children += ", ";
            }
            children += _children.elementAt(i).toString();
        }

        Integer group = (_group != null ? new Integer(_group.getId()) : null);
        return _id + " / " + _value + " / " + _modificationTime
            + " (" + _parentId + ":" + group + ") { " + children + " }";
    }
}
