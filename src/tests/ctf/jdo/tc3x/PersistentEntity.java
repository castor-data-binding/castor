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

package ctf.jdo.tc3x;

import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Persistent;
import org.exolab.castor.jdo.Query;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.TimeStampable;
import org.exolab.castor.mapping.AccessMode;

public class PersistentEntity implements Persistent, TimeStampable, Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = 861059599755591225L;

    public static final int     DEFAULT_ID = 7;
    public static final String  DEFAULT_VALUE = "persistent";

    private int                 _id;
    private String              _value;
    private Date                _creationTime;
    private Date                _modificationTime;
    private Integer             _parentId;
    private PersistentEntity    _parent;
    private Vector              _children;
    private Vector              _origChildren;
    private GroupEntity         _group;
    private RelatedEntity       _related;
    private RelatedEntity       _origRelated;

    private transient Database  _db;
    private long                _timeStamp;

    public PersistentEntity() {
        this(DEFAULT_ID);
    }

    public PersistentEntity(final int id) {
        _id = id;
        _value = DEFAULT_VALUE;
        _children = new Vector();
    }

    public final void setId(final int id) { _id = id; }
    public final int getId() { return _id; }

    public final void setParentId(final Integer parentId) { _parentId = parentId; }
    public final Integer getParentId() { return _parentId; }

    public final void setValue1(final String value) { _value = value; }
    public final String getValue1() { return _value; }

    public final void setCreationTime(final Date creationTime) {
        _creationTime = creationTime;
    }
    public final Date getCreationTime() { return _creationTime; }

    public final void setModificationTime(final Date modificationTime) {
        _modificationTime = modificationTime;
    }
    public final Date getModificationTime() { return _modificationTime; }

    public final void setParent(final PersistentEntity parent) {
        _parent = parent;
        _parentId = (_parent == null) ? null : new Integer(_parent._id);
    }
    public final PersistentEntity getParent() { return _parent; }

    public final void addChild(final PersistentEntity child) {
        _children.addElement(child);
        child.setParent(this);
        child.setGroup(_group);
    }
    public final Vector getChildren() { return _children; }
    public final PersistentEntity findChild(final int id) {
        for (Enumeration en = _children.elements(); en.hasMoreElements();) {
            PersistentEntity child = (PersistentEntity) en.nextElement();
            if (child.getId() == id) { return child; }
        }
        return null;
    }

    public final void setGroup(final GroupEntity group) {
        if (_group == group) { return; }
        _group = group;
        for (Enumeration en = _children.elements(); en.hasMoreElements();) {
            PersistentEntity child = (PersistentEntity) en.nextElement();
            child.setGroup(_group);
        }
    }
    public final GroupEntity getGroup() { return _group; }

    public final void setRelated(final RelatedEntity related) {
        _related = related;
        if (related != null) { related.setPersistent(this); }
    }
    public final RelatedEntity getRelated() { return _related; }

    public final void jdoPersistent(final Database db) { _db = db; }
    public final void jdoTransient() { _db = null; }
    public final Class jdoLoad(final AccessMode accessMode) throws Exception {
        if (_parentId != null) {
            _parent = (PersistentEntity) _db.load(
                    PersistentEntity.class, _parentId, accessMode);
        }

        Query qry = _db.getOQLQuery("SELECT p FROM " + PersistentEntity.class.getName()
                + " p WHERE parentId=$1");
        qry.bind(_id);
        QueryResults res = qry.execute();
        while (res.hasMore()) { _children.addElement(res.next()); }
        _origChildren = (Vector) _children.clone();
        _origRelated = _related;
        return null;
    }
    public final void jdoStore(final boolean modified) throws Exception {
        if (modified) { _modificationTime = new Date(); }

        PersistentEntity child;
        for (Enumeration en = _children.elements(); en.hasMoreElements();) {
            child = (PersistentEntity) en.nextElement();
            if (!vectorContainsChild(_origChildren, child)) {
                _db.create(child);
            }
        }
        for (Enumeration en = _origChildren.elements(); en.hasMoreElements();) {
            child = (PersistentEntity) en.nextElement();
            if (!vectorContainsChild(_children, child)) {
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
    public final void jdoUpdate() throws Exception {
        for (Enumeration en = _origChildren.elements(); en.hasMoreElements();) {
            _db.update(en.nextElement());
        }
        if (_origRelated != null) {
            _db.update(_origRelated);
        }
    }
    public final void jdoBeforeCreate(final Database db) throws Exception {
        if (_group == null) {
            throw new Exception("Incorrect object state: group is not set in " + this);
        }
        Object grp;
        try {
            grp = db.load(GroupEntity.class, new Integer(_group.getId()));
        } catch (Exception ex) {
            grp = null;
        }
        if (grp == null) { db.create(_group); }
        _creationTime = new Date();
    }
    public final void jdoAfterCreate() throws Exception {
        for (Enumeration en = _children.elements(); en.hasMoreElements();) {
            _db.create(en.nextElement());
        }
        _origChildren = (Vector) _children.clone();
        if (_related != null) { _db.create(_related); }
        _origRelated = _related;
    }
    public final void jdoBeforeRemove() throws Exception {
        for (Enumeration en = _children.elements(); en.hasMoreElements();) {
            _db.remove(en.nextElement());
        }
        if (_related != null) { _db.remove(_related); }
    }
    public final void jdoAfterRemove() throws Exception { }
    public final long jdoGetTimeStamp() { return _timeStamp; }

    public final void jdoSetTimeStamp(final long timeStamp) { _timeStamp = timeStamp; }

    private boolean vectorContainsChild(final Vector v, final PersistentEntity child) {
        for (Enumeration en = v.elements(); en.hasMoreElements();) {
            PersistentEntity ch = (PersistentEntity) en.nextElement();
            if (ch.getId() == child.getId()) { return true; }
        }
        return false;
    }

    public String toString() {
        String children = "";

        for (int i = 0; i < _children.size(); ++i) {
            if (i > 0) { children = children + ", "; }
            children = children + _children.elementAt(i).toString();
        }
        return _id + " / " + _value + " / " + _modificationTime + " ("
            + _parentId + ":" + ((_group != null) ? new Integer(_group.getId()) : null)
            + ") { " + children + " }";
    }
}
