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

/**
 * An Entity represents a tuple of values in a data store.
 * <p>
 * Each entity belongs to some entity classes. Entity class is a type structure
 * that base on a single inheritance model. Each entity class may have zero or 
 * more sub entity class(es), and each sub entity class may have zero or one
 * super entity classes. If an entitiy belongs to a sub entity class, it also 
 * belong to the super entity class and all its super entity classes, but not
 * the other way around. The ultimate super entity class is called a base 
 * entity class.
 * <p>
 * The EntityInfo contains the type information for a base class and all its
 * direct or indirect sub classes. Each sub class EntityInfo describes the 
 * fields in additional to the super EntityInfo and are not repeated and 
 * are not overrided, except if it is a identity. In most case, the actual 
 * entity doesn't span all the classes described in the EntityInfo.
 * <p>
 * An entity can be distinguished from any other entity with the same base by 
 * an identity. An identity can be one of the value, or composed by multiple 
 * values in an entity. Multiple values identity is wrapped in 
 * {@link org.exolab.castor.persist.types.Complex}.
 * <p>
 * An entity may has a stamp. A stamp is a value in an entity that can be used to 
 * identify the state of an entity. The state of entity is change in every update, 
 * and a stamp is a value in the entity that always change when the state of the 
 * entity change.
 * <p>
 * For example, the time of the last modification on an entity can be used as a 
 * stamp. A sequence counter that increment itself after each modification to the 
 * entity can also be used as a stamp.
 */
public final class Entity implements Cloneable {

    /**
     * The base type of the entity.
     */
    public EntityInfo   base;

    /**
     * The identity of this entity.
     */
    public Object       identity;

    /**
     * The timestamp or counter of the entity in long.
     */
    public long         longStamp;

    /**
     * The timestamp or counter of the entity in object type.
     */
    public Object       objectStamp;

    /**
     * Indicate this entity is locked in the data store.
     */
    public boolean      locked;

    /** 
     * Indicate which entity classes is this entity actually belongs to
     * The super-most entity of "actual" must equal to "base".
     */ 
    public EntityInfo   actual;

    /**
     * Represent the actual values in the data store. The values is
     * ordered in "super-most first" order. In the other words, the value 
     * representing the base.fieldInfo[0] is stored in position zero of 
     * values. Assuming actual is an immediate sub entity of base, the
     * first values of actual is stored in value[base.fieldInfo.length].
     */
    public Object[]     values;

    /**
     * Constructor
     */
    public Entity() {
        super();
    } // -- Entity

    /**
     * Constructor
     */ 
    public Entity( EntityInfo base, Object identity ) {
        super();
        this.base = base;
        this.identity = identity;
    } // -- Entity

    /**
     * Constructor
     */
    public Entity( EntityInfo base, EntityInfo actual, Object identity ) {
        super();
        this.base = base;
        this.actual = actual;
        this.identity = identity;
    } // -- Entity

    /**
     * Test if this object belongs to the same entity type and has the 
     * same identity. (warning! this behavior subject to change)
     *
     * @specify by java.lang.Object.equals(Object)
     */
    public boolean equals(Object obj) {
        Entity ent;

        if (obj == null || !(obj instanceof Entity)) {
            return false;
        }
        ent = (Entity) obj;
        return (base.equals(ent.base) && identity.equals(ent.identity));
    } // -- equals

    /**
     * Determines this entity and the specified entitiy is strickly equals.
     * In other words, they has the same base entity, same actual entity,
     * same identity, same set of values.
     */
    public boolean strictEquals( Entity entity ) {
        if ( entity == null )
            return false;

        // yip: three comparsions only. let me know if you can beat this
        if ( !( base==entity.base || (base!=null && base.equals(entity.base) ) ) )
            return false;

        if ( !(actual==entity.actual || (actual!=null && actual.equals(entity.actual)) ) )
            return false;

        if ( !(identity==entity.identity || (identity!=null && identity.equals(entity.identity)) ) )
            return false;

        // make sure both is not null && both has enough length
        if ( values == null && entity.values == null )
            return true;
        if ( values == null || entity.values == null )
            return false;

        int minLen = actual.getFieldOffset() + actual.fieldInfo.length;
        if ( values.length < minLen || entity.values.length < minLen )
            return false;

        for ( int i = 0; i < minLen; i++ ) {
            if ( !(values[i]==entity.values[i] 
                    || (values[i]!=null && values[i].equals(entity.values[i])) ) )
                return false;
        }
        return true;
    } // -- strictEquals

    /**
     * Return the hashCode of this entity
     *
     * @specify by java.lang.Object.hashCode()
     */
    public int hashCode() {
        return base.hashCode() + (identity == null ? 0 : identity.hashCode());
    } // -- hashCode

    /**
     * Get a string representing this entity
     * 
     * @return a string representing this entity
     */
    public String toString() {
        return base + "[" + identity + "]";
    } // -- toString

    /**
     * Get a clone of this object
     *
     * @return an new instance of the same object that has the same values
     */
    public Object clone() throws CloneNotSupportedException {

        Entity target = (Entity) super.clone();
        target.actual = actual;
        if ( values != null ) {
            target.values = new Object[values.length];
            System.arraycopy( values, 0, target.values, 0, values.length );
        } else {
            target.values = null;
        }
        return target;
    } // -- clone

    /**
     * Copy all the fields of this entity into the target entity
     *
     * @param target The entity that the fields to be copied into
     */
    public void copyInto( Entity target ) {

        target.base        = base;
        target.identity    = identity;
        target.longStamp   = longStamp;
        target.objectStamp = objectStamp;
        target.locked      = locked;
        target.actual      = actual;
        if ( values != null ) {
            target.values = new Object[values.length];
            System.arraycopy( values, 0, target.values, 0, values.length );
        } else {
            target.values = null;
        }
    } // -- copyInto

    /**
     * Obtain the value of a field represented by fieldInfo, if this entity
     * is initalized. FieldInfo must belongs to one of the entity that is lay 
     * in the path between base and actual.
     * <p>
     * If this entity is not initalized, it method always return false
     * <p>
     */
    public Object getFieldValue( EntityFieldInfo fieldInfo ) 
            throws IllegalArgumentException {

        if ( base == null || actual == null || values == null )
            return null;

        // make sure fieldInfo is in the path between actual and base
        if ( !fieldInfo.entityClass.isSuper( actual ) )
            throw new IllegalArgumentException("Entity " + fieldInfo.entityClass 
                      + " for the field " + fieldInfo + " not found in " + this);

        // get the field
        int pos  = fieldInfo.entityClass.getFieldOffset();
            pos += fieldInfo.getFieldPos();
        return values[pos];
    } // -- getFieldValue(...)

    /**
     * Set the value of the field represents by the specified fieldInfo
     * to be the specified value.
     * <p>
     * If this entity is uninitialized, this method will initalized it.
     * <p>
     * This method throws IllegalArgumentException if the fieldInfo 
     * is not compatible with the existing state. For example, fieldInfo
     * does not belong to the path between {@link #actual} and {@link #base}
     */
    public void setFieldValue( EntityFieldInfo fieldInfo, Object value ) 
            throws IllegalArgumentException {

        // check the state of base
        if ( base == null )
            // set base if it is not already been set
            base = fieldInfo.entityClass.getBase();
        else if ( !fieldInfo.entityClass.getBase().equals( base ) ) 
            throw new IllegalArgumentException("EntityFieldInfo does not contains the same base");

        // check the state of actual
        if ( actual == null ) {
            // set actual if not already been set
            actual = fieldInfo.entityClass;
        } else if ( !fieldInfo.entityClass.isSuper( actual ) ) {
            // make sure the new field is compatible with the path between
            // base and actual
            if ( actual.isSuper( fieldInfo.entityClass ) )
                // upgrade the entity to a sub entity
                actual = fieldInfo.entityClass;
            else
                throw new IllegalStateException("Field represented by "+fieldInfo+" is not compatible");
        }

        // check the state of value
        if ( values==null ) {
            values = new Object[base.getMaxLength()];
        } else if ( values.length < (actual.getFieldOffset()+actual.fieldInfo.length) ) {
            Object[] old = values;
            values = new Object[base.getMaxLength()];
            System.arraycopy( old, 0, values, 0, old.length );
        }

        int pos  = fieldInfo.entityClass.getFieldOffset();
            pos += fieldInfo.getFieldPos();
        values[pos] = value;
    } // -- setFieldValue

    /**
     * Get a new instance of Create Helper
     */
    public CreateHelper helper() {
        return new CreateHelper( this );
    } // -- helper

    /**
     * Get a new instance of Iterator
     *
     * @throws IllegalStateException if the entity is raw. Specifically,
     *         "base" and "real" is not set
     */
    public EntityIterator iterator() 
            throws IllegalStateException {
        return new EntityIterator( this );
    } // -- iterator

    /**
     * Entity Iterator
     */
    public static class EntityIterator {
        /**
         * The entity to iterate on
         */
        private Entity          entity;

        /**
         * The EntityInfo in the current position
         */
        private EntityInfo      cur;

        /**
         * The offset position of sub entity field value
         */
        private int             valuesOffset;

        /**
         * The path to the entity.real from entity.base
         */
        private long            level;

        /**
         * The level of the cur entity in the path
         */

        /**
         * Constrcutor
         */
        private EntityIterator( Entity entity )
                throws NullPointerException, IllegalStateException {

            if ( entity.base == null || entity.actual == null )
                throw new IllegalStateException( "Entity is not initalized!" );

            this.entity = entity;
        } // -- EntityIterator

        //===================
        //  Global
        //===================
        /**
         * Get the entity info represents the base of the entity that
         * this iterator iterating on.
         */
        public EntityInfo getBaseEntityInfo() {
            return entity.base;
        } // -- EntityInfo

        //===================
        //  Sub Entity level
        //===================
        /**
         * Iterate to the next avaliable entity
         */
        public boolean next() {
            int deep = entity.actual.getDeep();
            if ( ++level > deep ) {
                cur = null;
                return false;
            }

            if ( level == 1 ) {
                cur = entity.base;
                valuesOffset = cur.getFieldOffset();
                return true;
            }

            cur = entity.actual;
            for ( int i = 0; i < (deep-level); i++ ) {
                cur = cur.superEntity;
            }
            valuesOffset = cur.getFieldOffset();
            return true;
        } // -- next

        /**
         * Returns the EntityInfo of the current position
         *
         * @throws IllegalStateException if next() has not been called
         */
        public EntityInfo getEntityInfo() 
                throws IllegalStateException {

            if ( cur == null )
                throw new IllegalStateException("next() should be called first!");

            return cur;
        } // -- getEntityInfo

        //=====================
        //  Entity Field level
        //=====================
        /**
         * Get the number of field of the sub entity in the current position
         *
         * @throws IllegalStateException if entity base is not set
         */
        public int getEntityFieldSize() 
                throws IllegalStateException {

            if ( cur == null )
                throw new IllegalStateException("next() should be called first!");

            return cur.fieldInfo.length;
        } // -- getEntityFieldSize

        /**
         * Get the specified field info of the current entity
         *
         * @throws IllegalStateException if next() has not been called
         * @throws ArrayIndexOutOfBoundsException if there is no field
         *         matches to the specified fieldNumber
         */
        public EntityFieldInfo getEntityFieldInfo( int fieldNumber ) 
                throws IllegalStateException, ArrayIndexOutOfBoundsException {

            if ( cur == null )
                throw new IllegalStateException("next() should be called first!");

            return cur.fieldInfo[fieldNumber];
        } // -- getEntityFieldInfo

        /**
         * Get the specified field value of the current entity
         *
         * @throws IllegalStateException if next() has not been called
         * @throws ArrayIndexOutOfBoundsException if there is no field
         *         matches to the specified fieldNumber
         */
        public Object getFieldValue( int fieldNumber ) 
                throws IllegalStateException, ArrayIndexOutOfBoundsException {

            if ( cur == null )
                throw new IllegalStateException("next() should be called first!");

            // let java to check ArrayIndex for me
            Object junk = cur.fieldInfo[fieldNumber];

            if ( entity.values == null 
                    || entity.values.length < valuesOffset+cur.fieldInfo.length )
                return null;

            return entity.values[valuesOffset+fieldNumber];
        } // -- getFieldValue

        /**
         * Get the specified field value of the current entity
         *
         * @throws IllegalStateException if next has not been called
         * @throws ArrayIndexOutOfBoundsException if there is no field
         *         matches to the specified fieldNumber
         */
        public void setFieldValue( int fieldNumber, Object value )
                throws IllegalStateException, ArrayIndexOutOfBoundsException {

            if ( cur == null )
                throw new IllegalStateException("next() should be called first!");

            // let java to check ArrayIndex for me
            Object junk = cur.fieldInfo[fieldNumber];

            if ( entity.values == null ) {
                entity.values = new Object[entity.base.getMaxLength()];
            } else if ( entity.values.length < valuesOffset+cur.fieldInfo.length ) {
                Object[] temp = entity.values;
                entity.values = new Object[entity.base.getMaxLength()];
                System.arraycopy( temp, 0, entity.values, 0, temp.length );
            }

            entity.values[valuesOffset+fieldNumber] = value;
        } // -- setFieldValue

    } // == EntityIterator

    /**
     * Create Iterator
     */
    public static class CreateHelper {
        /**
         * The entity to iterate on
         */
        private Entity          entity;

        /**
         * The EntityInfo in the current position
         */
        private EntityInfo      cur;

        /**
         * The offset position of sub entity field value
         */
        private int             valuesOffset;

        /**
         * Constructor
         *
         * @throws NullPointerException if entity is null
         */
        private CreateHelper( Entity entity ) 
                throws NullPointerException {

            if ( entity == null )
                throw new NullPointerException();

            this.entity = entity;

        } // -- CreateHelper

        //=============
        //  Global
        //=============
        /**
         * Get the entity info represents the base of the entity that
         * this iterator iterating on.
         */
        public EntityInfo getBaseEntityInfo() {
            return entity.base;
        } // -- EntityInfo

        /**
         * Set the entityInfo as the base of the entity this iterator
         * iterator on.
         *
         * @throws IllegalStateException if the original base is not null,
         *         or different from the specified base to be set
         */
        public void setBaseEntityInfo( EntityInfo base )
                throws IllegalStateException {

            if ( entity.base == base )
                return;

            if ( entity.base == null ) {
                entity.base = base;
                return;
            }
            throw new IllegalStateException("Change of entity base is not allowed");
        } // -- EntityInfo

        //===================
        //  Sub Entity level
        //===================
        /**
         * Returns the EntityInfo of the current position
         */
        public EntityInfo getEntityInfo() {
            if ( entity.base == null )
                return null;

            if ( cur == null ) {
                cur = entity.base;
                valuesOffset = 0;
            }
            
            return cur;
        } // -- getEntityInfo

        /**
         * Returns the number of sub entity the current entity contains
         *
         * @throws IllegalStateException if entity base is not set
         */
        public int getSubEntitySize() {
            if ( entity.base == null )
                throw new IllegalStateException("entity base is not defined");

            if ( cur == null ) {
                cur = entity.base;
                valuesOffset = 0;
            }
            
            return cur.subEntities.length;
        } // -- getSubEntitySize

        /**
         * Returns the specified sub entity of the current entity
         *
         * @throws IllegalStateException if entity base is not set
         * @throws ArrayIndexOutOfBoundsException if there is no sub entity
         *         matches to the specified fieldNumber
         */
        public EntityInfo getSubEntityInfo( int subEntityNumber ) 
                throws IllegalStateException, ArrayIndexOutOfBoundsException {

            if ( entity.base == null )
                throw new IllegalStateException("entity base is not defined");

            if ( cur == null ) {
                cur = entity.base;
                valuesOffset = 0;
            }

    
            return cur.subEntities[subEntityNumber];
        } // -- getSubEntityInfo

        /**
         * Iterates to the specified sub entity from the current entity
         *
         * @throws IllegalStateException if entity base is not set
         * @throws ArrayIndexOutOfBoundsException if there is no sub entity
         *         matches to the specified fieldNumber
         */
        public void goSubEntity( int subEntityNumber ) 
                throws IllegalStateException, ArrayIndexOutOfBoundsException {
            if ( entity.base == null )
                throw new IllegalStateException("entity base is not defined");

            if ( cur == null ) {
                cur = entity.base;
                valuesOffset = 0;
            }

            cur = cur.subEntities[subEntityNumber];
        } // -- goSubEntity

        //=====================
        //  Entity Field level
        //=====================
        /**
         * Get the number of field of the sub entity in the current position
         *
         * @throws IllegalStateException if entity base is not set
         */
        public int getEntityFieldSize() 
                throws IllegalStateException {

            if ( entity.base == null )
                throw new IllegalStateException("entity base is not defined");

            if ( cur == null ) {
                cur= entity.base;
                valuesOffset = 0;
            }

            return cur.fieldInfo.length;
        } // -- getEntityFieldSize

        /**
         * Get the specified field info of the current entity
         *
         * @throws IllegalStateException if entity base is not set
         * @throws ArrayIndexOutOfBoundsException if there is no field
         *         matches to the specified fieldNumber
         */
        public EntityFieldInfo getEntityFieldInfo( int fieldNumber ) 
                throws IllegalStateException, ArrayIndexOutOfBoundsException {

            if ( entity.base == null )
                throw new IllegalStateException("entity base is not defined");

            if ( cur == null ) {
                cur= entity.base;
                valuesOffset = 0;
            }

            return cur.fieldInfo[fieldNumber];
        } // -- getEntityFieldInfo

        /**
         * Get the specified field value of the current entity
         *
         * @throws IllegalStateException if entity base is not set
         * @throws ArrayIndexOutOfBoundsException if there is no field
         *         matches to the specified fieldNumber
         */
        public Object getFieldValue( int fieldNumber ) 
                throws IllegalStateException, ArrayIndexOutOfBoundsException {

            if ( entity.base == null )
                throw new IllegalStateException("entity base is not defined");

            if ( cur == null ) {
                cur = entity.base;
                valuesOffset = 0;
            }

            // let java to check ArrayIndex for me
            Object junk = cur.fieldInfo[fieldNumber];

            if ( entity.values == null 
                    || entity.values.length < valuesOffset+cur.fieldInfo.length )
                return null;

            return entity.values[valuesOffset+fieldNumber];
        } // -- getFieldValue

        /**
         * Get the specified field value of the current entity
         *
         * @throws IllegalStateException if entity base is not set
         * @throws ArrayIndexOutOfBoundsException if there is no field
         *         matches to the specified fieldNumber
         */
        public void setFieldValue( int fieldNumber, Object value )
                throws IllegalStateException, ArrayIndexOutOfBoundsException {

            if ( entity.base == null )
                throw new IllegalStateException("entity base is not defined");

            if ( cur == null ) {
                cur = entity.base;
                valuesOffset = 0;
            }

            // let java to check ArrayIndex for me
            Object junk = cur.fieldInfo[fieldNumber];

            if ( entity.values == null ) {
                entity.values = new Object[entity.base.getMaxLength()];
            } else if ( entity.values.length < valuesOffset+cur.fieldInfo.length ) {
                Object[] temp = entity.values;
                entity.values = new Object[entity.base.getMaxLength()];
                System.arraycopy( temp, 0, entity.values, 0, temp.length );
            }

            entity.values[valuesOffset+fieldNumber] = value;
        } // -- setFieldValue

    } // == CreateHelper

}
