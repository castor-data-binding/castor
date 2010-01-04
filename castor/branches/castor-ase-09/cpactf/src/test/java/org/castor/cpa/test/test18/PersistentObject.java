/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.cpa.test.test18;

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
import org.junit.Ignore;

@Ignore
public final class PersistentObject implements Persistent, TimeStampable, Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = -2190482779395735049L;
    
    public static final int       DEFAULT_ID = 7;
    public static final String    DEFAULT_VALUE = "persistent";

    private int _id;
    private String _value;
    private Date _creationTime;
    private Date _modificationTime;
    private Integer _parentId;
    private PersistentObject _parent;
    private Vector<PersistentObject> _children;
    private Vector<PersistentObject> _origChildren;
    private PersistentGroup _group;
    private PersistentRelated _related;
    private PersistentRelated _origRelated;

    private long _timeStamp;

    private transient Database _db;

    public PersistentObject() {
        this(DEFAULT_ID);
    }

    public PersistentObject(final int id) {
        _id = id;
        _value = DEFAULT_VALUE;
        _children = new Vector<PersistentObject>();
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
    
    public Vector<PersistentObject> getChildren() { return _children; }

    public PersistentObject findChild(final int id) {
        Enumeration<PersistentObject> enumeration = _children.elements();
        PersistentObject child;
        while (enumeration.hasMoreElements()) {
            child = enumeration.nextElement();
            if (child.getId() == id) {
                return child;
            }
        }
        return null;
    }

    public void setGroup(final PersistentGroup group) {
        if (_group != group) {
            _group = group;
            
            Enumeration<PersistentObject> enumeration = _children.elements();
            PersistentObject child;
            while (enumeration.hasMoreElements()) {
                child = enumeration.nextElement();
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

    public Class<?> jdoLoad(final AccessMode accessMode) throws Exception {
        if (_parentId != null) {
            _parent = _db.load(PersistentObject.class, _parentId, accessMode);
        }

        Query qry = _db.getOQLQuery("SELECT p FROM "
                + PersistentObject.class.getName() + " p WHERE parentId=$1");
        qry.bind(_id);
        QueryResults res = qry.execute();
        while (res.hasMore()) {
            _children.addElement((PersistentObject) res.next());
        }
        
        _origChildren = new Vector<PersistentObject>(_children);
        _origRelated = _related;
        return null;
    }

    public void jdoStore(final boolean modified) throws Exception {
        Enumeration<PersistentObject> enumeration;
        PersistentObject child;

        if (modified) {
            _modificationTime = new Date();
        }

        enumeration = _children.elements();
        while (enumeration.hasMoreElements()) {
            child = enumeration.nextElement();
            if (!containsChild(_origChildren, child)) {
                _db.create(child);
            }
        }
        enumeration = _origChildren.elements();
        while (enumeration.hasMoreElements()) {
            child = enumeration.nextElement();
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
        Enumeration<PersistentObject> enumeration = _origChildren.elements();
        while (enumeration.hasMoreElements()) {
            _db.update(enumeration.nextElement());
        }

        if (_origRelated != null) {
            _db.update(_origRelated);
        }
    }

    public static boolean containsChild(final Vector<PersistentObject> vector,
                                        final PersistentObject child) {
        
        Enumeration<PersistentObject> enumeration = vector.elements();
        PersistentObject ch;
        while (enumeration.hasMoreElements()) {
            ch = enumeration.nextElement();
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
        Enumeration<PersistentObject> enumeration = _children.elements();
        while (enumeration.hasMoreElements()) {
            _db.create(enumeration.nextElement());
        }
        _origChildren = new Vector<PersistentObject>(_children);
        
        if (_related != null) {
            _db.create(_related);
        }
        _origRelated = _related;
    }

    public void jdoBeforeRemove() throws Exception {
        Enumeration<PersistentObject> enumeration = _children.elements();
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
