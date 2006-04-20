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

package org.exolab.castor.xml.schema.types;

import org.exolab.castor.xml.schema.*;

/**
 * The base class for built-in Schema types
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$ 
**/
public abstract class BuiltInType extends Simpletype {

    /**
     * The name of the binary type
    **/
    public static final String BINARY_NAME         = "binary";
    
    /**
     * The type associated with binary
    **/
    public static final short  BINARY_TYPE         = 0;

    /**
     * The name of the boolean type
    **/
    public static final String BOOLEAN_NAME        = "boolean";
    
    /**
     * The type associated with boolean
    **/
    public static final short  BOOLEAN_TYPE        = 1;

    /**
     * The name of the decimal type
    **/
    public static final String DECIMAL_NAME        = "decimal";
    
    /**
     * The type associated with decimal
    **/
    public static final short  DECIMAL_TYPE        = 2;
    
    /**
     * The name of the double type
    **/
    public static final String DOUBLE_NAME         = "double";
    
    /**
     * The type associated with double
    **/
    public static final short  DOUBLE_TYPE         = 3;
    

    /**
     * The name of the ID type
    **/
    public static final String ID_NAME             = "ID";
    
    /**
     * The type associated with ID
    **/
    public static final short  ID_TYPE             = 4;

    /**
     * The name of the IDREF type
    **/
    public static final String IDREF_NAME          = "IDREF";
    
    /**
     * The type associated with IDREF
    **/
    public static final short  IDREF_TYPE          = 5;
    
    /**
     * The name of the integer type
    **/
    public static final String INTEGER_NAME        = "integer";
    
    /**
     * The type associated with integer
    **/
    public static final short  INTEGER_TYPE        = 6;
    
    
    /**
     * The name of the long type
    **/
    public static final String LONG_NAME           = "long";
    
    /**
     * The type associated with long
    **/
    public static final short  LONG_TYPE           = 7;
    
    /**
     * The name of the NCName type
    **/
    public static final String NCNAME_NAME         = "NCName";
    
    /**
     * The type associated with NCName
    **/
    public static final short  NCNAME_TYPE         = 8;
    
    
    /**
     * the name of the negative integer type
    **/
    public static final String NEGATIVE_INTEGER_NAME    = "negative-integer";
    
    /**
     * the type associated with negative-integer
    **/
    public static final short  NEGATIVE_INTEGER_TYPE    = 9;
    
    /**
     * The name of the NMTOKEN type
    **/
    public static final String NMTOKEN_NAME             = "NMTOKEN";
    
    /**
     * The type associated with NMTOKEN
    **/
    public static final short  NMTOKEN_TYPE             = 10;
    
    /**
     * the name of the positive integer type
    **/
    public static final String POSITIVE_INTEGER_NAME    = "positive-integer";
    
    /**
     * the type associated with positive-integer
    **/
    public static final short  POSITIVE_INTEGER_TYPE    = 11;
    
    
    /**
     * The name of the string type
    **/
    public static final String STRING_NAME              = "string";
    
    /**
     * The type associated with string
    **/
    public static final short  STRING_TYPE              = 12;
            

    /**
     * The name of the timeInstant type
    **/
    public static final String TIME_INSTANT_NAME        = "timeInstant";
    
    /**
     * The type associated with timeInstant
    **/
    public static final short  TIME_INSTANT_TYPE        = 13;
    
    
    //----------------/
    //- Constructors -/
    //----------------/
    
    
    /**
     * Creates a new BuiltInType with the given name and
     * Schema reference
     * @param name the name of the primitive type
     * @param schema the schema reference
    **/
    protected BuiltInType(Schema schema, String name) {
        super(schema, name);
    } //-- BuiltInType
    
    /**
     * Adds the given Facet to this Simpletype.
     * @param facet the Facet to add to this Simpletype
    **/
    public void addFacet(Facet facet) {
        String err = "Facets cannot be added to built-in types.";
        throw new IllegalArgumentException(err);
    } //-- addFacet
    
    /**
     * Returns the type of this BuiltInType
     * @return the type of this BuiltInType
    **/
    public abstract short getType();
    
    /**
     * Sets the source type for this simpletype
     * @param source the source type which this simpletype inherits from
    **/
    public void setSourceRef(String source) {
        String err = "Primitives types cannot extend other types.";
        throw new IllegalArgumentException(err);
    } //-- setBaseTypeRef
    
} //-- BuiltInType
