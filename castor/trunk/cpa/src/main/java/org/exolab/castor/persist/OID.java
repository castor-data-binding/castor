/*
 * Copyright 2011 Assaf Arkin, Ralf Joachim, Wensheng Dou
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
package org.exolab.castor.persist;

import java.io.Serializable;

import org.exolab.castor.persist.spi.Identity;

/**
 * Object identifier. An object identifier is unique within a cache engine or
 * other persistence mechanism and is used to locate object based on their
 * identity as well as assure no duplicate identities. The object type and it's
 * identity object define the OID's identity. In addition the OID is used to
 * hold the object's stamp and db-lock access fields which are used to optimize
 * dirty checking within a transaction.
 * 
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @author <a href="mailto:wsdou55 AT gmail DOT com ">Wensheng Dou</a>
 * @version $Revision$ $Date: 2006-04-22 11:05:30 -0600 (Sat, 22 Apr 2006) $
 */
public final class OID implements Serializable {
    //-----------------------------------------------------------------------------------    

    /** SerialVersionUID. */
    private static final long serialVersionUID = 419512942519592363L;

    //-----------------------------------------------------------------------------------    

    /** The full qualified class name of the top level entity of an extends hierarchy. */
    private final String _topTypeName;

    /** The entities identity if known, null if the object was created without an identity. */
    private final Identity _identity;

    /** The OID's hash code. */
    private final int _hashCode;

    /** The full qualified class name of the entity. */
    private String _typeName;

    /** The OID of depended object. */
    private OID _depended;

    /** True if the object is loaded with db-lock access. */
    private boolean _dbLock;

    //-----------------------------------------------------------------------------------    

    /**
     * Protected default constructor invoked through reflection for testing only.
     */
    protected OID() {
        _topTypeName = null;
        _identity = null;
        _hashCode = 0;
    }

    /**
     * Constructor.
     * 
     * @param molder ClassMolder of the entity.
     * @param identity Identity of the entity.
     */
    public OID(final ClassMolder molder, final Identity identity) {
        _topTypeName = molder.getTopMolder().getName();
        _identity = identity;
        
        if (identity == null) {
            _hashCode = _topTypeName.hashCode();
        } else {
            _hashCode = _topTypeName.hashCode() + _identity.hashCode();
        }
        
        _typeName = molder.getName();
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Get the full qualified class name of the top level entity of an extends hierarchy.
     * 
     * @return The full qualified class name of the top level entity of an extends hierarchy.
     */
    String getTopTypeName() {
        return _topTypeName;
    }
    
    /**
     * Return the object's identity, if known. An identity exists for every object that was
     * loaded within a transaction and for those objects that were created with an identity.
     * No two objects may have the same identity in persistent storage. If the object was
     * created without an identity this method will return null until the object is first
     * stored and it's identity is set.
     * 
     * @return The object's identity, or null.
     */
    public Identity getIdentity() {
        return _identity;
    }

    //-----------------------------------------------------------------------------------    
    
    /**
     * Set the full qualified class name of the entity.
     * 
     * @param typeName The full qualified class name of the entity.
     */
    void setTypeName(final String typeName) {
        _typeName = typeName;
    }
    
    /**
     * Set the full qualified class name of the entity.
     * 
     * @return The full qualified class name of the entity.
     */
    String getTypeName() {
        return _typeName;
    }

    /**
     * Set the depended object's oid.
     * 
     * @param depended The depended object's oid.
     */
    public void setDepended(final OID depended) {
        _depended = depended;
    }

    /**
     * Get the depended object's OID.
     * 
     * @return the depended object's OID.
     */
    public OID getDepended() {
        return _depended;
    }

    /**
     * Specifies whether the object represented by this OID has a database lock.
     * Database locks overrides the need to perform dirty checking on the
     * object. This status is set when the object is loaded with db-lock access,
     * created or deleted. It is reset when the object is unlocked.
     * 
     * @param dbLock True the object represented by this OID has a database lock.
     */
    void setDbLock(final boolean dbLock) {
        _dbLock = dbLock;
    }

    /**
     * Returns true if the object represented by this OID has a database lock.
     * Database locks overrides the need to perform dirty checking on the
     * object. This status is set when the object is loaded with db-lock access,
     * created or deleted. It is reset when the object is unlocked.
     * 
     * @return True the object represented by this OID is loaded with a datbase lock.
     */
    public boolean isDbLock() {
        return _dbLock;
    }

    //-----------------------------------------------------------------------------------    

    /**
     * Returns true if the two OID's are identical. Two OID's are identical only if they
     * represent the same top level entity and have the same identity (based on equality
     * test). If no identity was specified for either or both objects, the objects are not
     * identical.
     * 
     * {@inheritDoc}
     */
    public boolean equals(final Object obj) {
        if (this == obj) { return true; }

        // If identity of at least one OID is null they are not equal.
        if (_identity == null) { return false; }
        
        // If other object is not an OID they are not equal.
        if (!(obj instanceof OID)) { return false; }
        
        OID other = (OID) obj;
        return _topTypeName.equals(other._topTypeName)
            && _identity.equals(other._identity);
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return _hashCode;
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_topTypeName);
        if (_typeName != null) {
            sb.append('(').append(_typeName).append(')');
        }
        if (_identity == null) {
            sb.append("<new>");
        } else {
            sb.append(_identity);
        }
        return sb.toString();
    }

    //-----------------------------------------------------------------------------------    
}
