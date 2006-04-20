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


import org.exolab.castor.persist.sql.KeyGeneratorDescriptor;


/**
 * EntityInfo specify a Entity
 *
 */
public final class EntityInfo {

    // -- Implementation Notes:
    //
    // Developer! Let's agree that all array used in this class should 
    // never be null. We uses zero length array instead
    //

    /**
     * Object saver final static variable
     */
    private final static EntityFieldInfo[] emptyEntityFieldInfo = new EntityFieldInfo[0];

    /**
     * Object saver final static variable
     */
    private final static EntityInfo[]      emptyEntityInfo      = new EntityInfo[0];

    /**
     * The entity class which this object represent.
     */
    public final String entityClass;

    /**
     * The information of all logical fields
     */
    public final EntityFieldInfo[] fieldInfo;

    /**
     * The information of all identity fields
     */
    public final EntityFieldInfo[] idInfo;

    /**
     * All entities which directly extends this entities
     */
    public final EntityInfo[] subEntities;

    /**
     * The super entity of this entity
     */
    public EntityInfo superEntity;

    /**
     * The discriminator the Bridge Layer should be used to determines
     * the actual entity type
     */
    public final Object discriminator;

    /**
     * The key generator descriptor used to generate the identity for
     * the entity
     */
    public final KeyGeneratorDescriptor keyGen;

    /**
     * Constructor
     */
    public EntityInfo( String entityClass, EntityFieldInfo[] idInfo,
                       EntityFieldInfo[] fieldInfo, EntityInfo[] subEntities, 
                       Object discriminator, KeyGeneratorDescriptor keyGen ) {
        this.entityClass   = entityClass;
        this.superEntity    = superEntity;
        this.discriminator  = discriminator;
        this.keyGen         = keyGen;

        // uses empty arrays to avoid null pointer
        if ( fieldInfo == null || fieldInfo.length <= 0 )
            this.fieldInfo  = emptyEntityFieldInfo;
        else
            this.fieldInfo     = fieldInfo;

        if ( idInfo == null || idInfo.length <= 0 )
            this.idInfo     = emptyEntityFieldInfo;
        else
            this.idInfo         = idInfo;

        if ( subEntities == null || subEntities.length <= 0 ) 
            this.subEntities    = emptyEntityInfo;
        else
            this.subEntities    = subEntities;

    } // -- EntityInfo

    /**
     * Returns the offset of the first field in the entity
     * (aka, the sum of the number of field for all super entities)
     */
    public int getFieldOffset() {

        EntityInfo cur = this;
        int offset = 0;
        while ( cur.superEntity != null ) {
            offset += cur.fieldInfo.length;
            cur = cur.superEntity;
        }
        return offset;
    } // -- getFieldOffset

    /**
     * Determines if the entity represented by this EntityInfo 
     * object is either the same as, or is a superentity of, 
     * the entity represented by the specified EntityInfo parameter. 
     * It returns true if so; otherwise it returns false. 
     */ 
    public boolean isSuper( EntityInfo info ) {
        if ( this.equals(info) )
            return true;

        while ( !this.equals( info ) ) {
            if ( info == null )
                return false;

            info = info.superEntity;
        }
        return true;
    } // -- isSuper

    /**
     * Return the base entityInfo of this entity
     */
    public EntityInfo getBase() {
        
        EntityInfo cur = this;
        while ( cur.superEntity != null ) {
            cur = cur.superEntity;
        }
        return cur;
    } // -- getBase

    /**
     * Get the deep of this sub entities to the base
     */
    public int getDeep() {
        int deep = 0;
        EntityInfo cur = this;
        while ( cur != null ) {
            ++deep;
            cur = cur.superEntity;
        }
        return deep;
    } // -- getDeep

    /**
     * Determines the position of this sub entitiy related to the super 
     * entities.
     */
    public int getEntityPos() {
        if ( superEntity == null ) 
            return 0;

        for ( int i = 0; i < superEntity.subEntities.length; i++ ) {
            if ( superEntity.subEntities[i] == this )
                return i;
        }
        throw new IllegalStateException("EntityInfo, "+this+", is corrupted. It doesn't not contained in an super entity it belongs");
    } // -- getEntityPos

    /**
     * Return the maximium number of field possible
     * (aka, the sum of the number of fields of this and all sub entities)
     */
    public int getMaxLength() {
        int fieldLen = fieldInfo==null?0:fieldInfo.length;
        if ( subEntities == null || subEntities.length == 0 )
            return fieldLen;

        int max = 0;
        for ( int i = 0; i < subEntities.length; i++ ) {
            if ( subEntities[i].getMaxLength() > max )
                max = subEntities[i].getMaxLength();
        }
        return max+fieldLen;
    } // -- getMaxLength

    /**
     * Determines the specified EntityInfo is equals to this object.
     */
    public boolean equals( Object object ) {
        if ( object == null || !( object instanceof EntityInfo ) )
            return false;

        EntityInfo info = (EntityInfo) object;

        return entityClass.equals(info.entityClass);
    } // -- equals

    /**
     * Returns the hashCode for this EntityInfo
     */
    public int hashCode() {
        return entityClass == null ? 0 : entityClass.hashCode();
    } // -- hashCode

    /**
     * Returns a string representing this EntityInfo
     */
    public String toString() {
        return entityClass;
    } // -- toString
}