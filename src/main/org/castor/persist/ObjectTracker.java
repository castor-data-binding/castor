/*
 * Copyright 2005 Ralf Joachim, Gregory Bock, Werner Guttmann
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
package org.castor.persist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.castor.persist.proxy.LazyCGLIB;
import org.castor.util.IdentityMap;
import org.castor.util.IdentitySet;
import org.castor.util.Messages;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.persist.ClassMolder;
import org.exolab.castor.persist.LockEngine;
import org.exolab.castor.persist.OID;

/**
 * A transaction records all objects accessed during the lifetime
 * of the transaction in this record (queries and created).
 * 
 * This information, stored on a per-object basis within the
 * ObjectTracker, covers the database engine used
 * to persist the object, the object's OID, the object itself, and
 * whether the object has been deleted in this transaction,
 * created in this transaction. 
 * 
 * Sidenote: Objects identified as read only are not updated 
 * when the transaction commits.
 * 
 * @author <a href="mailto: ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:gblock AT ctoforaday DOT COM">Gregory Block</a>
 * @version $Revision$ $Date$
 * @since 0.9.9
 */
public final class ObjectTracker {
    //--------------------------------------------------------------------------

    /** Map of Object->ClassMolder. */
    private final Map _objectToMolder = new IdentityMap();
    
    /** Map of Engine -> OID -> Object. */
    private final Map _engineToOIDToObject = new HashMap();
    
    /** Map of Object->OID. */
    private final Map _objectToOID = new IdentityMap();
    
    /** Set of all objects marked 'deleted'. */
    private final Map _deletedMap = new IdentityMap();
    
    /** Set of all objects marked 'creating'. */
    private final Map _creatingMap = new IdentityMap();
    
    /** Set of all objects marked 'created'. */
    private final Set _createdSet = new IdentitySet();
    
    /** Set of all objects for which we need to update persistence. */
    private final Set _updatePersistNeededSet = new IdentitySet();
    
    /** Set of all objects for which we need to update cache. */
    private final Set _updateCacheNeededSet = new IdentitySet();
    
    /** Set of all objects marked read only in this transaction. */
    private final Set _readOnlySet = new IdentitySet();
    
    /** Set of all objects marked read-write in this transaction. */
    private final Set _readWriteSet = new IdentitySet();
    
    /** Operation counter of ObjectTracker. */
    private long _operation = Long.MIN_VALUE;
    
    //--------------------------------------------------------------------------

    /**
     * Retrieve the object for a given OID.
     * 
     * @param allowReadOnly Allow (or ignore, if false) read-only objects to be returned.
     * @return The object associated with this oid.
     */
    public Object getObjectForOID(final LockEngine engine, final OID oid, 
                                  final boolean allowReadOnly) {
        Map oidToObject = (Map) _engineToOIDToObject.get(engine);
        if (oidToObject != null) {
            Object found = oidToObject.get(oid);
            if (!allowReadOnly) {
                if (_readOnlySet.contains(found)) {
                    return null;
                }
            }
            return found;
        }
        
        // We don't know about this engine in this transaction.
        // Hence, to us, there is no mapping.  Yet.
        return null;
    }
    
    public boolean isReadWrite(final Object object) {
        Object aObject = supportCGLibObject(object);
        return (_readWriteSet.contains(aObject));
    }
    
    public void unmarkAllDeleted() {
        _operation++;
        _deletedMap.clear();
    }
    
    public void clear() {
        _operation++;
        _createdSet.clear();
        _creatingMap.clear();
        _deletedMap.clear();
        _engineToOIDToObject.clear();
        _objectToMolder.clear();
        _objectToOID.clear();
        _readOnlySet.clear();
        _readWriteSet.clear();
        _updateCacheNeededSet.clear();
        _updatePersistNeededSet.clear();
    }
    
    public boolean isUpdateCacheNeeded(final Object object) {
        Object aObject = supportCGLibObject(object);
        return _updateCacheNeededSet.contains(aObject);
    }
    
    public boolean isUpdatePersistNeeded(final Object object) {
        Object aObject = supportCGLibObject(object);
        return _updatePersistNeededSet.contains(aObject);
    }
    
    public void markUpdateCacheNeeded(final Object object) {
        _operation++;
        Object aObject = supportCGLibObject(object);
        if (!isTracking(aObject)) {
            return;
        }
        
        _updateCacheNeededSet.add(aObject);
    }
    
    public void unmarkUpdateCacheNeeded(final Object object) {
        _operation++;
        Object aObject = supportCGLibObject(object);
        _updateCacheNeededSet.remove(aObject);
    }
    
    public Collection getObjectsWithUpdateCacheNeededState() {
        return new ArrayList(_updateCacheNeededSet);
    }
    
    public void markUpdatePersistNeeded(final Object object) {
        _operation++;
        Object aObject = supportCGLibObject(object);
        if (!isTracking(aObject)) {
            return;
        }
        
        _updatePersistNeededSet.add(aObject);
        // updatePersistNeeded implies updateCacheNeeded (from comment in persist())
        _updateCacheNeededSet.add(aObject);
    }
    
    public void unmarkUpdatePersistNeeded(final Object object) {
        _operation++;
        Object aObject = supportCGLibObject(object);
        _updatePersistNeededSet.remove(aObject);
    }
    
    public void markCreating(final Object object) throws PersistenceException {
        _operation++;
        Object aObject = supportCGLibObject(object);
        if (!isTracking(aObject)) {
            return;
        }
        
        if (_createdSet.contains(aObject)) {
            throw new PersistenceException("Invalid state change; can't mark something " 
                    + " creating which is already marked created.");
        }
        _creatingMap.put(aObject, new Long(_operation));
    }
    
    public void markCreated(final Object object) {
        _operation++;
        Object aObject = supportCGLibObject(object);
        if (!isTracking(aObject)) {
            return;
        }
        
        _createdSet.add(aObject);
        _creatingMap.remove(aObject);
    }
    
    public void markDeleted(final Object object) {
        _operation++;
        Object aObject = supportCGLibObject(object);
        if (!isTracking(aObject)) {
            return;
        }
        
        _deletedMap.put(aObject, new Long(_operation));
    }
    
    public void unmarkDeleted(final Object object) {
        _operation++;
        Object aObject = supportCGLibObject(object);
        _deletedMap.remove(aObject);
    }
    
    /**
     * Determine whether an object is being tracked within this tracking manager.
     * @param object
     * @return
     */
    public boolean isTracking(final Object object) {
        Object aObject = supportCGLibObject(object);
        return _objectToOID.containsKey(aObject);
    }
    
    /**
     * Record changes to an OID by re-tracking the OID information.  When an object's 
     * OID can change, ensure that the object is retracked.
     * 
     * @param obj The object to record a tracking change for.
     * @param engine The engine which is responsible for the old and new OID
     * @param oldoid The old oid.
     * @param newoid The new oid.
     */
    public void trackOIDChange(final Object obj, 
            final LockEngine engine, 
            final OID oldoid, 
            final OID newoid) {
        _operation++;
        Object object = supportCGLibObject(obj);
        
        removeOIDForObject(engine, oldoid);
        setOIDForObject(object, engine, newoid);
    }
    
    
    /**
     * For a given lockengine and OID, set the object in the maps.  Note that an OID
     * can only be accessed via the LockManager which manages it.
     * @param obj The object to track
     * @param engine The engine to which the OID belongs
     * @param oid The OID of the object to track
     */
    public void setOIDForObject(final Object obj, 
            final LockEngine engine, 
            final OID oid) {
        _operation++;
        Object object = supportCGLibObject(obj);
        
        // Remove any current mapping.
        removeOIDForObject(engine, oid);
        
        // Now add it in.
        Map oidToObject = (Map) _engineToOIDToObject.get(engine);
        if (oidToObject == null) {
            oidToObject = new HashMap();
            _engineToOIDToObject.put(engine, oidToObject);
        }
        oidToObject.put(oid, obj);
        
        // Add the reversal.
        _objectToOID.put(object, oid);
    }
    
    /**
     * For a given lockengine and OID, remove references to an object in the maps.
     * This eliminates both the engine->oid->object and the object->oid.
     * @param obj The object to stop tracking on
     * @param engine The engine to stop tracking the OID for
     * @param oid The oid of the object to stop tracking on.
     */
    public void removeOIDForObject(final LockEngine engine, final OID oid) {
        _operation++;
        Object found = null;
        Map oidToObject = (Map) _engineToOIDToObject.get(engine);
        if (oidToObject != null) {
            found = oidToObject.get(oid);
            oidToObject.remove(oid);
        }
        if (found != null) {
            _objectToOID.remove(found);
        }
    }
    
    public boolean isCreating(final Object o) {
        Object object = supportCGLibObject(o);
        return _creatingMap.containsKey(object);
    }
    
    public boolean isCreated(final Object o) {
        Object object = supportCGLibObject(o);
        return _createdSet.contains(object);
    }
    
    public boolean isDeleted(final Object o) {
        Object object = supportCGLibObject(o);
        return _deletedMap.containsKey(object);
    }
    
    /** Retrieve the ClassMolder associated with a specific object. */
    public ClassMolder getMolderForObject(final Object o) {
        Object object = supportCGLibObject(o);
        return (ClassMolder) _objectToMolder.get(object);
    }
    
    private void setMolderForObject(final Object obj, final ClassMolder molder) {
        _operation++;
        Object object = supportCGLibObject(obj);
        
        // remove if exists
        removeMolderForObject(object);
        // Update objectToMolder (handles both new and pre-existing mappings)
        _objectToMolder.put(object, molder);
    }
    
    private void removeMolderForObject(final Object obj) {
        _operation++;
        Object object = supportCGLibObject(obj);
        _objectToMolder.remove(object);
    }
    
    /** Retrieve the list of all read-write objects being tracked. */
    public Collection getReadWriteObjects() {
        ArrayList returnedList = new ArrayList(_readWriteSet);
        return returnedList;
    }
    
    /** Retrieve the list of all read-only objects being tracked. */
    public Collection getReadOnlyObjects() {
        ArrayList returnedList = new ArrayList(_readOnlySet);
        return returnedList;
    }
    
    /** Retrieve the list of creating objects, sorted in the order they should be 
     * created. */
    public Collection getObjectsWithCreatingStateSortedByLowestMolderPriority() {
        ArrayList entryList = new ArrayList(_creatingMap.entrySet());
        Collections.sort(entryList, new ObjectMolderPriorityComparator(this, false));
        ArrayList returnedList = new ArrayList(entryList.size());
        for (int i = 0; i < entryList.size(); i++) {
            returnedList.add(((Map.Entry) entryList.get(i)).getKey());
        }
        return returnedList;
    }
    
    /** Retrieve the list of deleted objects, sorted in the order they should be 
     * deleted. */
    public Collection getObjectsWithDeletedStateSortedByHighestMolderPriority() {
        ArrayList entryList = new ArrayList(_deletedMap.entrySet());
        Collections.sort(entryList, new ObjectMolderPriorityComparator(this, true));
        ArrayList returnedList = new ArrayList(entryList.size());
        for (int i = 0; i < entryList.size(); i++) {
            returnedList.add(((Map.Entry) entryList.get(i)).getKey());
        }
        return returnedList;
    }
    
    public void trackObject(
            final ClassMolder molder, 
            final OID oid, 
            final Object object) {
        _operation++;
        Object aObject = supportCGLibObject(object);
        
        setMolderForObject(aObject, molder);
        setOIDForObject(aObject, molder.getLockEngine(), oid);
        _readWriteSet.add(aObject);
    }
    
    
    public void untrackObject(final Object object) {
        _operation++;
        Object aObject = supportCGLibObject(object);
        
        // Grab any lockengine/OID information for removal of the nested 
        // engine-oid-object maps.
        LockEngine engine = getMolderForObject(aObject).getLockEngine();
        OID oid = getOIDForObject(aObject);
        
        removeMolderForObject(aObject);
        removeOIDForObject(engine, oid);
        
        _deletedMap.remove(aObject);
        _creatingMap.remove(aObject);
        _createdSet.remove(aObject);
        _updatePersistNeededSet.remove(aObject);
        _updateCacheNeededSet.remove(aObject);
        _readOnlySet.remove(aObject);
        _readWriteSet.remove(aObject);
    }
    
    public OID getOIDForObject(final Object o) {
        Object object = supportCGLibObject(o);
        return (OID) _objectToOID.get(object);
    }
    
    public boolean isReadOnly(final Object o) {
        Object object = supportCGLibObject(o);
        return (_readOnlySet.contains(object));
    }
    
    public void markReadOnly(final Object o) {
        _operation++;
        Object object = supportCGLibObject(o);
        
        if (!isTracking(object)) {
            throw new IllegalStateException(Messages.format("persist.internal",
            "Attempt to make read-only object that is not in transaction"));
        }
        
        // Add it to our list of read only objects.
        _readOnlySet.add(object);
        _readWriteSet.remove(object);
    }
    
    public void unmarkReadOnly(final Object o) {
        _operation++;
        Object object = supportCGLibObject(o);
        
        if (!isTracking(object)) {
            throw new IllegalStateException(Messages.format("persist.internal",
            "Attempt to make read-write object that is not in transaction"));
        }
        
        // Add it to our list of read only objects.
        _readWriteSet.add(object);
        _readOnlySet.remove(object);
    }
    
    public int readOnlySize() {
        return _readOnlySet.size();
    }
    
    public int readWriteSize() {
        return _readWriteSet.size();
    }
    
    /**
     * "Transform" an object provided as a LazyCGLib into a 'natural' object within 
     * the transaction, undecorated. This allows us to find the true object associated 
     * with a decorated object in the transaction.
     * 
     * We do this by asking the decorated object for enough information to re-generate 
     * its proper OID; we then go looking for that OID in the system to find the object 
     * in the transaction.
     * 
     * @param object
     * @return
     */
    private Object supportCGLibObject(final Object object) {
        if (object instanceof LazyCGLIB) {
            LazyCGLIB cgObject = (LazyCGLIB) object;
            
            // TODO [WG] We still might have an option for some serious optimization
            // here if the instance has not been materialized yet.
            Object identity = cgObject.interceptedIdentity();
            ClassMolder molder = cgObject.interceptedClassMolder();
            LockEngine engine = molder.getLockEngine();
            
            // Get the OID we're looking for.
            OID oid = new OID(molder, identity);
            
            // Retrieve the object for this OID; returns null if there ain't such object 
            return getObjectForOID(engine, oid, true);
        }

        return object;
    }
    
    public String allObjectStates() {
        StringBuffer sb = new StringBuffer();
        Iterator it = _objectToOID.keySet().iterator();
        while (it.hasNext()) {
            sb.append(objectStateToString(it.next()));
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public String objectStateToString(final Object obj) {
        StringBuffer sb = new StringBuffer();
        sb.append(getOIDForObject(obj));
        sb.append('\t'); sb.append("deleted: ");  sb.append(_deletedMap.containsKey(obj));
        sb.append('\t'); sb.append("creating: "); sb.append(_creatingMap.containsKey(obj));
        sb.append('\t'); sb.append("created: ");  sb.append(_createdSet.contains(obj));
        return sb.toString();
    }
    
    //--------------------------------------------------------------------------

    private static final class ObjectMolderPriorityComparator implements Comparator {
        private ObjectTracker _tracker;
        private boolean _reverseOrder;
        
        public ObjectMolderPriorityComparator(
                final ObjectTracker tracker, final boolean reverseOrder) {
            _tracker = tracker;
            _reverseOrder = reverseOrder;
        }
        
        public int compare(final Object object1, final Object object2) {
            Map.Entry entry1 = (Map.Entry) object1;
            Map.Entry entry2 = (Map.Entry) object2;
            
            long oper1 = ((Long) entry1.getValue()).longValue();
            long oper2 = ((Long) entry2.getValue()).longValue();
            
            ClassMolder molder1 = _tracker.getMolderForObject(entry1.getKey());
            ClassMolder molder2 = _tracker.getMolderForObject(entry2.getKey());
            
            if (molder1 == null || molder2 == null) {
                if (oper1 > oper2) {
                    return 1;
                } else if (oper1 < oper2) {
                    return -1;
                } else {
                    return 0;
                }
            }
            
            int pri1 = molder1.getPriority();
            int pri2 = molder2.getPriority();
            
            if (pri1 == pri2) {
                if (oper1 > oper2) {
                    return 1;
                } 
                if (oper1 < oper2) {
                    return -1;
                }    
                return 0;
            }
            
            if (_reverseOrder) {
                return (pri1 < pri2) ? 1 : -1;
            }
            return (pri1 < pri2) ? -1 : 1;
        }
    }

    //--------------------------------------------------------------------------
}
