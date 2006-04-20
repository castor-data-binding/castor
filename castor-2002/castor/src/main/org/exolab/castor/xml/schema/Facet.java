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

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.ValidationException;

/**
 * Represents the base type for XML Schema Facets
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Facet extends Annotated {

    public static final String DURATION         = "duration";
    public static final String ENCODING         = "encoding";
    public static final String ENUMERATION      = "enumeration";
    public static final String LENGTH           = "length";
    public static final String PATTERN          = "pattern";
    public static final String PRECISION        = "precision";
    public static final String MAX_EXCLUSIVE    = "maxExclusive";
    public static final String MAX_INCLUSIVE    = "maxInclusive";
    public static final String MIN_EXCLUSIVE    = "minExclusive";
    public static final String MIN_INCLUSIVE    = "minInclusive";
    public static final String MAX_LENGTH       = "maxLength";
    public static final String MIN_LENGTH       = "minLength";
    public static final String PERIOD           = "period";
    public static final String SCALE            = "scale";


    /**
     * The name of this Facet
    **/
    private String _name    = null;

    /**
     * The character value of this Facet
    **/
    private String  _value  = null;


    private static final String CLASSNAME =
        Facet.class.getName();

    private static final String NULL_ARGUMENT =
        "A null argument was passed to " + CLASSNAME + "#";

    private static final String ZERO_LENGTH_STRING =
        "A zero-length String was passed to " + CLASSNAME + "#";


    /**
     * Creates a new Facet with the given name
     * @param name the name of the Facet
     * @param value the value of the Facet
    **/
    public Facet(String name, String value) {
        if (name == null) {
            String err = NULL_ARGUMENT;
            err += "Facet: 'name' and 'value' must not be null.";
            throw new IllegalArgumentException(err);
        }
        if (name.length() == 0) {
            String err = ZERO_LENGTH_STRING;
            err += "Facet: 'name' and 'value' must not be zero-length.";
            throw new IllegalArgumentException(err);
        }
        this._name = name;
        this._value= value;
    } //-- Facet

    /**
     * Returns the name of this Facet
     * @return the name of this Facet
    **/
    public String getName() {
        return _name;
    } //-- getName


    /**
     * Returns the character (String) representation of this facet
     * @return the value of this facet
    **/
    public String getValue() {
        return this._value;
    } //-- getValue


   /**
     * Sets the value of this facet
     * @param value the new character value of this facet
    **/
//    public void setValue(String value) {
//       this._value = value;
//    } //-- setValue


    /**
     * Returns true if this Facet can occur more than once, such
     * as the "enumeration" facet.
     * @return true if this Facet can occur more than once.
    **/
    public boolean isMultivalued() {
        return _name.equals(Facet.ENUMERATION);
    }

    /**
     * Returns an int representation of the value of this facet
     * @return an int representation of the value of this facet
    **/
    public int toInt() throws NumberFormatException {
        return Integer.parseInt(_value);
    } //-- toInt


    /**
     * Returns a long representation of the value of this facet
     * @return a long representation of the value of this facet
    **/
    public long toLong() throws NumberFormatException {
        return Long.parseLong(_value);
    } //-- toInt


    /**
     * Returns an short representation of the value of this facet
     * @return an short representation of the value of this facet
    **/
    public short toShort() throws NumberFormatException {
        return Short.parseShort(_value);
    } //-- toShort


    /**
     * Returns a double representation of the value of this facet
     * @return a double representation of the value of this facet
    **/
    public double toDouble() throws NumberFormatException {
        return Double.valueOf(_value).doubleValue();
    } //-- toInt

    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.FACET;
    } //-- getStructureType

    /**
     * Checks the validity of this Schema defintion.
     * @exception ValidationException when this Schema definition
     * is invalid.
    **/
    public void validate()
        throws ValidationException
    {
         //-- do nothing for now
    } //-- validate

} //-- Facet

