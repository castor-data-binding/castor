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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id $
 */
package org.exolab.castor.persist;


import java.util.Arrays;

/**
 * This class specify a logic field of an entity.
 * <p>
 * In general, one logical field maps to an actual field of the entity.
 * Exception happens when a logical field is a foreign identity, and
 * the foreign identity spans more than one actually field. In such
 * case, the logical field is said to be complex.
 */
public final class EntityFieldInfo {

    public final static int CARDINALITY_UNDEFINED = 0;

    public final static int CARDINALITY_ONE_TO_ONE = 1;

    public final static int CARDINALITY_ONE_TO_MANY = 2;

    public final static int CARDINALITY_MANY_TO_ONE = 3;


    /**
     * Indicate this field is an identity field.
     */
    public boolean id;

    /**
     * Indicate this field is a foreign identitiy.
     */
    public boolean foreign;

    /**
     * Indicate this field
     */
    public boolean complex;

    /**
     * Indicate this field is virtual field and is obtained by a join of this
     * entity and another entitiy.
     */
    public boolean join;

    /**
     * Indicate this field is a large binary field.
     */
    public boolean blob;

    /**
     * Indicate this field is a foreign identity which corresponds to a foregin
     * entity, which is stored in a different data store than this entity.
     */
    public boolean sameDataStore;

    /**
     * Specify the cardinality number of this logical field
     */
    public int cardinality;

    /**
     * Indicate the bridge should perform dirty check on this field when
     * it the entity is stored.
     */
    public boolean dirtyCheck;

    /**
     * The expected types of this data store.
     */
    public Class[] expectedType;

    /**
     * The native type of this field in the data store.
     */
    public Class[] declaredType;

    /**
     * The type conversion parameter of the expected type and declared type.
     */
    public String[] typeParam;

    /**
     * The Entity in which this field belongs to.
     */
    public EntityInfo entityClass;

    /**
     * The native field name(s) of this entity.
     */
    public String[] fieldNames;

    /**
     * The foreign entity that this field corresponds to.
     */
    public EntityInfo relatedEntityClass;

    /**
     * The foreign key which this field correspond to (for one-to-many and one-to-one relations).
     */
    public EntityFieldInfo relatedForeignKey;

    /**
     * Constructor
     */
    public EntityFieldInfo() {
        // implements it....
    } // -- EntityFieldInfo

    public int getFieldPos() {
        for ( int i = 0; i < entityClass.fieldInfo.length; i++ ) {
            if ( entityClass.fieldInfo[i] == this )
                return i;
        }
        throw new IllegalStateException("FieldInfo, "+this+", is corrupted. It doesn't not contained in an entity it belongs");
    } // -- getFieldPos

    /**
     * Determines the specified EntityFieldInfo representing the same
     * Entity field as this EntityFieldInfo
     */
    public boolean equals( Object object ) {
        EntityFieldInfo info;

        if ( object == null || !( object instanceof EntityFieldInfo ) )
            return false;

        if (object == this)
            return true;

        info = (EntityFieldInfo) object;
        return  (entityClass.equals(info.entityClass) &&
                ((fieldNames == null && info.fieldNames == null) ||
                    Arrays.equals(fieldNames, info.fieldNames)) &&
                ((relatedEntityClass == null && info.relatedEntityClass == null) ||
                    relatedEntityClass.equals(info.relatedEntityClass)) &&
                ((relatedForeignKey == null && info.relatedForeignKey == null) ||
                    relatedForeignKey.equals(info.relatedForeignKey)));
    } // -- equals

    /**
     * Returns the hashCode for this EntityFieldInfo
     */
    public int hashCode() {
        return (entityClass == null ? 0: entityClass.hashCode()) +
                (fieldNames == null ? 0 : fieldNames[0].hashCode()) +
                (relatedEntityClass == null ? 0 : relatedEntityClass.hashCode());
    } // -- hashCode

    /**
     * Returns a string representing this EntityFieldInfo
     */
    public String toString() {
        return entityClass + "." + (fieldNames == null ? relatedEntityClass.toString()
                                                       : fieldNames[0].toString());
    } // -- toString
}