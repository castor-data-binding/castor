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


/**
 * Represents a primitive or class type
 * 
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class JType {

    public static final JType Boolean  = new JType("boolean");
    public static final JType Byte     = new JType("byte");
    public static final JType Char     = new JType("char");
    public static final JType Double   = new JType("double");
    public static final JType Float    = new JType("float");
    public static final JType Int      = new JType("int");
    public static final JType Long     = new JType("long");
    public static final JType Short    = new JType("short");

    private String name = null;
    
    private boolean _isArray = false;
    
    /**
     * used for array types
    **/
    private JType _componentType = null;
    
    /**
     * Creates a new JType with the given name
     * @param name the name of the type
    **/
    protected JType(String name) 
    {
        super();
        this.name = name;
    } //-- JType
    
    /**
     * Creates a JType Object representing an array of the current
     * JType.
     * @return the new JType which is represents an array.
    **/
    public final JType createArray() {
        JType jType = new JType(getName());
        jType._isArray = true;
        jType._componentType = this;
        return jType;
    } //-- createArray
    
    /**
     * If this JType is an array this method will returns the component type 
     * of the array, otherwise null will be returned.
     * @return the component JType if this JType is an array, otherwise null.
    **/
    public JType getComponentType() {
        return _componentType;
    } //-- getComponentType
    
    public String getLocalName() {

        //-- use getName method in case it's been overloaded
        String name = getName();

        if (name == null) return null;
        int idx = name.lastIndexOf('.');
        if (idx >= 0) {
            name = name.substring(idx+1);
        }
        return name;
    } //-- getLocalName
    
    public String getName() {
        return this.name;
    } //-- getName
    
    /**
     * Checks to see if this JType represents an array.
     * @return true if this JType represents an array, otherwise false
    **/
    public final boolean isArray() {
        return _isArray;
    }
    
    /**
     * Checks to see if this JType represents a primitive
     * @return true if this JType represents a primitive, otherwise false
    **/
    public boolean isPrimitive() {
        return ((this == JType.Boolean) ||
                (this == JType.Byte)    ||
                (this == JType.Char)    ||
                (this == JType.Double)  ||
                (this == JType.Float)   ||
                (this == JType.Int)     ||
                (this == JType.Long)    ||
                (this == JType.Short));
    } //-- isPrimitive
    
    /**
     * Returns the String representation of this JType, which is
     * simply the name of this type. 
     * @return the String representation of this JType
    **/
    public String toString() {
        
        if (_isArray) return _componentType.toString()+"[]";
        return this.name;
        
    } //-- toString
    
    //---------------------/
    //- Protected methods -/
    //---------------------/
    
    /**
     * Allows subtypes, such as JClass to alter the package to which
     * this JType belongs
     * @param newPackage the new package to which this JType belongs
     * <BR>
     * <B>Note:</B> The package name cannot be changed on a primitive type.
    **/
    protected void changePackage(String newPackage) {
        
        if (this.name == null) return;
        if (this.isPrimitive()) return;
        
        String localName = null;
        int idx = name.lastIndexOf('.');
        if (idx >= 0)
            localName = this.name.substring(idx+1);
        else 
            localName = this.name;
            
        if ((newPackage == null) || (newPackage.length() == 0))
            this.name = localName;
        else
            this.name = newPackage + "." + localName;
        
    } //-- changePackage
    
} //-- JType
