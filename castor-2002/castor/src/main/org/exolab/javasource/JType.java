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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.javasource;

import java.util.Vector;

/**
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
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
    
    private boolean isArray = false;
    
    
    /**
     * Creates a new JType with the given name
     * @param the name of the type
    **/
    protected JType(String name) 
    {
        super();
        this.name = name;
    } //-- JType
    
    protected JType(String name, boolean isArray) {
        this.isArray = isArray;
        this.name = name;
    } //-- JType
    
    /**
     * Creates a JType Object representing an array of the current
     * JType.
     * @return the new JType which is represents an array.
    **/
    public JType createArray() {
        return new JType(getName(), true);
    }
    
    public String getName() {
        return this.name;
    } //-- getName
    
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
    
    /**
     * Checks to see if this JType represents an array.
     * @return true if this JType represents an array, otherwise false
    **/
    public boolean isArray() {
        return isArray;
    }
    
    /**
     * Checks to see if this JType represents a primitive
     * @return true if this JType represents a primitive, otherwise false
    **/
    public boolean isPrimitive() {
        return ((this == this.Boolean) ||
                (this == this.Byte)    ||
                (this == this.Char)    ||
                (this == this.Double)  ||
                (this == this.Float)   ||
                (this == this.Int)     ||
                (this == this.Long)    ||
                (this == this.Short));
    } //-- isPrimitive
    
    public String toString() {
        return this.name;
    } //-- toString
    
} //-- JType
