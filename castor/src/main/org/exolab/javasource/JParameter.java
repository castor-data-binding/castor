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


import java.util.Vector;

/**
 * Represents a parameter to a JMethod. 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class JParameter extends JAnnotatedElementHelper {
    
    /**
     * The type associated with this JParameter
    **/
    private JType type = null;
    
    /**
     * The name of this JParameter
    **/
    private String name = null;

    /**
     * Creates a new JParameter with the given type, and name
     * @param type the type to associate with this JParameter
     * @param the name of the JParameter
    **/
    public JParameter(JType type, String name) 
        throws IllegalArgumentException
    {
        super();
        setType(type);
        setName(name);
    } //-- JParameter
    
    /**
     * Returns the name of the parameter
     * @return the name of the parameter
    **/
    public String getName() {
        return this.name;
    } //-- getName
    
    /**
     * Returns the parameter type
     * @return the parameter type
    **/
    public JType getType() {
        return this.type;
    } //-- getType
    
    /**
     * Sets the name of this parameter
     * @param name the new name of the parameter
    **/
    public void setName(String name) {
        this.name = name;
    } //-- setName
    
    /**
     * Sets the type of this parameter
     * @param type the new type of this parameter
    **/
    public void setType(JType type) 
        throws IllegalArgumentException
    {
        if (type == null) {
            String err = "A Parameter cannot have a null type.";
            throw new IllegalArgumentException(err);
        }
        this.type = type;
    } //-- setType

    /**
     * Returns the String representation of this JParameter. The
     * String returns will consist of the String representation
     * of the parameter type, followed by the name of the parameter
     * @return the String representation of this JParameter
    **/
    public String toString() {
        StringBuffer sb = new StringBuffer(this.type.toString());
        sb.append(' ');
        sb.append(this.name);
        return sb.toString();
    } //-- toString
    
} //-- JParamater