/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.clc-marketing.com/xslp/license.txt
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 *
 * $Id$
 */


package org.exolab.adaptx.xpath;


/**
 * Represents a string result. This is an immutable object.
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class StringResult 
    extends XPathResult 
{


    /**
     * String result representing an empty string.
     */
    public static final StringResult EMPTY = new StringResult( "" );



    /**
     * The value of the string result.
    **/
    private final String _value;
    
    /**
     * Creates a new string result with an empty string as 
     * it's value.
    **/
    public StringResult( ) {
        _value = "";
    } //-- StringResult
    
    /**
     * Creates a new string result with the given value.
     *
     * @param value The string value (not null)
    **/
    public StringResult( String value ) {
        if ( value == null )
            throw new IllegalArgumentException( "Argument value is null" );
        _value = value;
    } //-- StringResult
    

    /**
     * Returns the type of this result.
     *
     * @return {@link XPathResult#STRING}
    **/
    public int getResultType() {
        return XPathResult.STRING;
    } //-- getResultType


    /**
     * Returns the result as a boolean value. Returns true if not
     * an empty string.
     *
     * @return The result as a boolean value
     */
    public boolean booleanValue() {
        return _value.length() > 0 ;
    } //-- booleanValue

    /**
     * Returns the result as a number value. Returns {@link java.lang.Double#NaN}
     * if the value is not a valid number.
     *
     * @return The result as a number value
    **/
    public double numberValue() {
        try {
            Double dbl = Double.valueOf( _value.trim() );
            return dbl.doubleValue();
        } 
        catch ( NumberFormatException except ) { }
        return Double.NaN;
    } //-- doubleValue


    /**
     * Returns the result as a string value.
     *
     * @return The result as a string value
    **/
    public String stringValue() {
        return _value;
    } //-- stringValue


    /**
     * Returns the result as a Java object. Returns an object of
     * type {@link String} with the same string value.
     *
     * @return The result as a Java object
     */
    public Object javaObject()
    {
        return _value;
    } //-- javaObject


    /**
     * Returns true if the given result is a string result
     * and has the same string value.
     *
     * @param result An XPath result
     * @return True if a string result and has same value
    **/
    public boolean equals( XPathResult result ) {
        if ( result == this )
            return true;
        if ( result != null && result instanceof StringResult )
            return ( (StringResult) result )._value.equals( _value );
        return false;
    } //-- equals
    

    /**
     * Returns the String representation of this XPathResult
     *
     * @return the String representation of this XPathResult
    **/
    public String toString() {
        return _value;
    } //--toString

} //-- StringResult
