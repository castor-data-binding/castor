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
 * Copyright 1999-2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

//-- we should change this to SchemaValidationException
//-- and localize the package
import org.exolab.castor.xml.ValidationException;

/**
 * The base class for all XML Schema stuctures.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
**/
public abstract class Structure implements java.io.Serializable {

    public static final short ANYTYPE             =  0;
    public static final short ANNOTATION          =  1;
    public static final short APPINFO             =  2;
    public static final short ATTRIBUTE           =  3;
    public static final short ATTRIBUTE_GROUP     =  4;
    public static final short COMPLEX_CONTENT     =  5;
    public static final short COMPLEX_TYPE        =  6;
    public static final short DOCUMENTATION       =  7;
    public static final short ELEMENT             =  8;
    public static final short FACET               =  9;
    public static final short GROUP               = 10;
    public static final short IDENTITY_FIELD      = 11;
    public static final short IDENTITY_SELECTOR   = 12;
    public static final short KEY                 = 13;
    public static final short KEYREF              = 14;
    public static final short LIST                = 15;
    public static final short MODELGROUP          = 16;
    public static final short MODELGROUP_REF      = 17;
    public static final short REDEFINE            = 18;
    public static final short SCHEMA              = 19;
    public static final short SIMPLE_CONTENT      = 20;
    public static final short SIMPLE_TYPE         = 21;
    public static final short UNION               = 22;
    public static final short UNIQUE              = 23;
    public static final short WILDCARD            = 24;

    //-- should be removed eventually
    public static final short UNKNOWN         = -1;


    /**
     * Creates a new XML Schema Structure
    **/
    protected Structure() {
        super();
    } //-- Structure

    /**
     * Calls validate() to determine if this Schema Definition
     * is valid.
     *
     * @return true if this Schema definition is valid, otherwise false.
    **/
    public boolean isValid() {
        try {
            validate();
        }
        catch(ValidationException ex) {
            return false;
        }
        return true;
    } //-- isValid

    /**
     * Returns the type of this Schema Structure.
     *
     * @return the type of this Schema Structure.
    **/
    public abstract short getStructureType();

    /**
     * Checks the validity of this Schema defintion.
     *
     * @exception ValidationException when this Schema definition
     * is invalid.
    **/
    public abstract void validate()
        throws ValidationException;

} //-- Structure
