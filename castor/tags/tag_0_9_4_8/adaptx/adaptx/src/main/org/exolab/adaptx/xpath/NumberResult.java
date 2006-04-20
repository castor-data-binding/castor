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
 * Represents a number result. This is an immutable object.
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public final class NumberResult
    extends XPathResult
{


    /**
     * Number result representing {@link java.lang.Double#NaN} (not a number).
     */
    public static final NumberResult NaN = new NumberResult( Double.NaN );


    /**
     * The value of the number result.
     */
    private final double _value;


    /**
     * Creates a new number result with the given value.
     *
     * @param value The number value
     */
    public NumberResult( double value )
    {
        _value = value;
    }
    

    
    /**
     * Returns the type of this result.
     *
     * @return {@link XPathResult#NUMBER}
     */
    public int getResultType()
    {
        return XPathResult.NUMBER;
    }


    /**
     * Returns the result as a boolean value. Returns true if the
     * number value is not zero.
     *
     * @return The result as a boolean value
     */
    public boolean booleanValue()
    {
        return _value != 0.0;
    }


    /**
     * Returns the result as a number value.
     *
     * @return The result as a number value
     */
    public double numberValue()
    {
        return  _value;
    }


    /**
     * Returns the result as a string value.
     *
     * @return The result as a string value
     */
    public String stringValue()
    {
        //-- NaN
        if ( Double.isNaN( _value ) ) return "NaN";
        
        //-- Negative Infinity
        if (_value == Double.NEGATIVE_INFINITY) return "-Infinity";
        
        //-- Positive Infinity
        if (_value == Double.POSITIVE_INFINITY) return "Infinity";
        
        int integer = (int) _value;
        if ( _value == (double) integer )
            return String.valueOf( integer );
        else
            return String.valueOf( _value );
    }


    /**
     * Returns the result as a Java object. Returns an object of
     * type {@link java.lang.Double} with the same number value.
     *
     * @return The result as a Java object
     */
    public Object javaObject()
    {
        return new Double( _value );
    }


     /**
     * Returns true if the given result is a number result
     * and has the same number value.
     *
     * @param result An XPath result
     * @return True if a number result and has same value
     */
    public boolean equals( XPathResult result )
    {
        if ( result == this )
            return true;
        if ( result != null && result instanceof NumberResult )
            return ( (NumberResult) result )._value == _value;
        return false;
    }


    public String toString()
    {
        return stringValue();
    }


}
