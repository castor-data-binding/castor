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

package org.exolab.castor.builder.types;

import org.exolab.javasource.*;

/**
 * The base XML Schema Type class
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public abstract class XSType {
    
    public static final short NULL               = -1;
    
    //-- this type should change to Archetype or
    //-- something like that
    public static final short CLASS              =  0;
    
    public static final short ID                 =  1;
    public static final short IDREF              =  2;
    public static final short INTEGER            =  3;
    public static final short LIST               =  4;
    public static final short NCNAME             =  5;
    public static final short NEGATIVE_INTEGER   =  6;
    public static final short NMTOKEN            =  7;
    public static final short POSITIVE_INTEGER   =  8;
    public static final short REAL               =  9;
    public static final short STRING             = 10;
    public static final short TIME_INSTANT       = 11;
    
    private short type = NULL;
    
    protected XSType(short type) {
        this.type = type;
    }
    
    /**
     * Returns the JType that this XSType represents
     * @return the JType that this XSType represents
    **/
    public abstract JType getJType();
    
    /**
     * Returns the type of this XSType
     * @return the type of this XSType
    **/
    public short getType() {
        return this.type;
    } //-- getType
    
    /**
     * Returns the String necessary to convert an instance of this XSType
     * to an Object. This method is really only useful for primitive types
     * @param variableName the name of the instance variable
     * @return the String necessary to convert an instance of this XSType
     * to an Object
    **/
    public String createToJavaObjectCode(String variableName) {
        return variableName;
    } //-- toJavaObject

    /**
     * Returns the String necessary to convert an Object to
     * an instance of this XSType. This method is really only useful 
     * for primitive types
     * @param variableName the name of the Object
     * @return the String necessary to convert an Object to an 
     * instance of this XSType
    **/
    public String createFromJavaObjectCode(String variableName) {
        StringBuffer sb = new StringBuffer();
        
        JType jType = getJType();
        if (jType != null) {
            sb.append('(');
            sb.append(jType.getName());
            sb.append(") ");
        }
        sb.append(variableName);
        return sb.toString();
    } //-- fromJavaObject
    
    public boolean isPrimitive() {
        switch (type) {
            case INTEGER:
            case NEGATIVE_INTEGER:
            case POSITIVE_INTEGER:
            case REAL:
                return true;
            default:
                return false;
        }
    }
    
} //-- XSType
