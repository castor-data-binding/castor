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

package org.exolab.javasource;

import org.exolab.castor.builder.BuilderConfiguration;

/**
 * Represents a primitive or class type
 * 
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class JType {

    public static final JType BOOLEAN  = new JType("boolean", "Boolean");
    public static final JType BYTE     = new JType("byte",    "Byte");
    public static final JType CHAR     = new JType("char",    "Character");
    public static final JType DOUBLE   = new JType("double",  "Double");
    public static final JType FLOAT    = new JType("float",   "Float");
    public static final JType INT      = new JType("int",     "Integer");
    public static final JType LONG     = new JType("long",    "Long");
    public static final JType SHORT    = new JType("short",   "Short");

    private String _name = null;
    
    /**
     * Indicates whether this JType represents an array
     */
    private boolean _isArray = false;
    
    /**
     * Indicates whether this JType represents a collection
     */
    private boolean _isCollection = false;
    
    /**
     * Used for Array and Collection types to indicate the data type contained
     * in the Array or Collection
     */
    private JType _componentType = null;
    
    /**
     * Only populated for primitive types and indicates the wrapper Object class
     * name for this primitive type
     */
    private String _wrapperName = null;

    /**
     * Creates a new JType with the given name
     * 
     * @param name the name of the type
     */
    protected JType(final String name) {
        super();
        this._name = name;
    } //-- JType

    /**
     * Creates a new JType for a primitive with the given name and wrapper name.
     * This constructor is private so it can only be used by the primitives
     * defined here.
     * 
     * @param name the name of the type
     * @param wrapperName the name of the wrapper Object type for this primirive
     *            type
     */
    private JType(final String name, final String wrapperName) {
        this(name);
        this._wrapperName = wrapperName;
    } //-- JType

    /**
     * Creates a JType Object representing an array of the current JType
     * instance.
     * 
     * @return the new JType which is represents an array.
     */
    public final JType createArray() {
        JType jType = new JType(getName());
        jType._isArray = true;
        jType._componentType = this;
        return jType;
    } //-- createArray

    /**
     * Creates a JType Object representing a collection of the given type or an
     * iterator over a given type
     * <p>
     * FIXME: This method should be replaced with two methods, one for
     * collections and one for iterators.  The iterator version type probably
     * should not set _isCollection to true.
     * 
     * @param name the class name of the collection or iterator
     * @param componentType object type contains in the collection or iterated
     *            over in the iterator
     * @return the new JType which is a collection or iterator over the provided
     *         component type
     */
    public static final JType createCollection(final String name,
            final JType componentType) {
        JType jType = new JType(name);
        jType._isCollection = true;
        jType._componentType = componentType;
        return jType;
    } //-- createCollection

    /**
     * If this JType is an array/collection, this method will return the
     * component type of the array/collection, otherwise it will return null
     * 
     * @return the component JType if this JType is an array/collection,
     *         otherwise null.
     */
    public final JType getComponentType() {
        return _componentType;
    } //-- getComponentType
    
    public final String getLocalName() {

        //-- use getName method in case it's been overloaded
        String name = getName();

        if (name == null) { return null; }
        int idx = name.lastIndexOf('.');
        if (idx >= 0) {
            name = name.substring(idx + 1);
        }
        return name;
    } //-- getLocalName
    
    public final String getName() {
        return this._name;
    } //-- getName

    /**
     * Return the name of the wrapper object for a primitive type, null for
     * non-primitive types
     * 
     * @return the name of the wrapper object for a primitive type, null for
     *         non-primitive types
     */
    public final String getWrapperName() {
      return this._wrapperName;
    } //-- getWrapperName

    /**
     * Checks to see if this JType represents an array.
     * 
     * @return true if this JType represents an array, otherwise false
     */
    public final boolean isArray() {
        return _isArray;
    }

    /**
     * Checks to see if this JType represents a collection.
     * 
     * @return true if this JType represents a collection, otherwise false
     */
    public final boolean isCollection() {
        return _isCollection;
    }
    
    /**
     * Checks to see if this JType represents a primitive type
     * 
     * @return true if this JType represents a primitive type, otherwise false
     */
    public final boolean isPrimitive() {
        return ((this == JType.BOOLEAN)
             || (this == JType.BYTE)
             || (this == JType.CHAR)
             || (this == JType.DOUBLE)
             || (this == JType.FLOAT)
             || (this == JType.INT)
             || (this == JType.LONG)
             || (this == JType.SHORT));
    } //-- isPrimitive
    
    /**
     * Returns the String representation of this JType, which is simply the name
     * of this type.
     * 
     * @return the String representation of this JType
     */
    public final String toString() {
        if (_isArray) {
            return _componentType.toString() + "[]";
        }
        
        if (isCollection() && BuilderConfiguration.createInstance().useJava50()) {
          if (_componentType.isPrimitive()) {
            return _name + "<" + _componentType.getWrapperName() + ">";
          } 
          return _name + "<" + _componentType.toString() + ">";
        }
        
        return this._name;
    } //-- toString
    
    //---------------------/
    //- Protected methods -/
    //---------------------/
    
    /**
     * Change the package this JType belongs to. This method is protected to
     * allow subtypes, such as JClass to alter the package to which this JType
     * belongs
     * 
     * @param newPackage the new package to which this JType belongs <BR>
     *            <B>Note:</B> The package name cannot be changed on a
     *            primitive type.
     */
    protected final void changePackage(final String newPackage) {
        if (this._name == null) { return; }
        if (this.isPrimitive()) { return; }
        
        String localName = null;
        int idx = _name.lastIndexOf('.');
        if (idx >= 0) {
            localName = this._name.substring(idx + 1);
        } else {
            localName = this._name;
        }
            
        if ((newPackage == null) || (newPackage.length() == 0)) {
            this._name = localName;
        } else {
            this._name = newPackage + "." + localName;
        }
    } //-- changePackage
    
} //-- JType
