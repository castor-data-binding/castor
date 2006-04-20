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
    
    
    /**
     * The name of the binary type
    **/
    public static final String BINARY_NAME         = "binary";

    /**
     * The name of the boolean type
    **/
    public static final String BOOLEAN_NAME        = "boolean";
    
    /**
     * The name of the decimal type
    **/
    public static final String DECIMAL_NAME        = "decimal";
    
    /**
     * The name of the double type
    **/
    public static final String DOUBLE_NAME         = "double";

    /**
     * The name of the ID type
    **/
    public static final String ID_NAME             = "ID";

    /**
     * The name of the IDREF type
    **/
    public static final String IDREF_NAME          = "IDREF";
    
    /**
     * The name of the integer type
    **/
    public static final String INTEGER_NAME        = "integer";
    
    
    /**
     * The name of the long type
    **/
    public static final String LONG_NAME           = "long";
    
    /**
     * The name of the NCName type
    **/
    public static final String NCNAME_NAME         = "NCName";
    
    /**
     * The name of the negative-interger type
    **/
    public static final String NEGATIVE_INTEGER_NAME  = "negative-integer";
    
    
    /**
     * The name of the NMTOKEN type
    **/
    public static final String NMTOKEN_NAME        = "NMTOKEN";
    
    /**
     * The name of the posative-interger type
    **/
    public static final String POSITIVE_INTEGER_NAME  = "positive-integer";
    
    /**
     * The name of the string type
    **/
    public static final String STRING_NAME         = "string";
    
    /**
     * The name of the timeInstant type
    **/
    public static final String TIME_INSTANT_NAME   = "timeInstant";
    
    
    
    public static final short NULL               = -1;
    
    //-- this type should change to user-defined or
    //-- something like that
    public static final short CLASS              =  0;
    //--
    public static final short BINARY             =  1;
    public static final short BOOLEAN            =  2;
    public static final short DECIMAL            =  3;
    public static final short DOUBLE             =  4;
    public static final short ID                 =  5;
    public static final short IDREF              =  6;
    public static final short INTEGER            =  7;
    public static final short LIST               =  8;
    public static final short LONG               =  9;
    public static final short NCNAME             = 10;
    public static final short NEGATIVE_INTEGER   = 11;
    public static final short NMTOKEN            = 12;
    public static final short POSITIVE_INTEGER   = 13;
    public static final short STRING             = 14;
    public static final short TIME_INSTANT       = 15;
    
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
            case BOOLEAN:
            case DOUBLE:
            case INTEGER:
            case LONG:
            case NEGATIVE_INTEGER:
            case POSITIVE_INTEGER:
                return true;
            default:
                return false;
        }
    }
    
    /** 
     * Returns the name of this XSType
     * @return the name of this XSType
    **/
    public String getName() {
        switch (type) {
            case BINARY:
                return BINARY_NAME;
            case BOOLEAN:
                return BOOLEAN_NAME;
            case DECIMAL:
                return DECIMAL_NAME;
            case DOUBLE:
                return DOUBLE_NAME;
            case ID:
                return ID_NAME;
            case IDREF:
                return IDREF_NAME;
            case INTEGER:
                return INTEGER_NAME;
            case LONG:
                return LONG_NAME;
            case NCNAME:
                return NCNAME_NAME;
            case NEGATIVE_INTEGER:
                return NEGATIVE_INTEGER_NAME;
            case NMTOKEN:
                return NMTOKEN_NAME;
            case POSITIVE_INTEGER:
                return POSITIVE_INTEGER_NAME;
            case STRING:
                return STRING_NAME;
            case TIME_INSTANT:
                return TIME_INSTANT_NAME;
            default:
                return null;
        }
    } //-- getName
    
} //-- XSType
