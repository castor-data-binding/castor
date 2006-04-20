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
 * Represents a boolean result. This is an immutable object.
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public final class BooleanResult
    extends XPathResult
{


    /**
     * Boolean result representing false.
     */
    public static final BooleanResult FALSE = new BooleanResult( false );

        
    /**
     * Boolean result representing true.
     */
    public static final BooleanResult TRUE = new BooleanResult( true );

        
    /**
     * The value of the boolean result.
     */
    private final boolean _value;


    /**
     * Private constructor.
     */
    private BooleanResult( boolean value )
    {
        _value = value;
    }


    /**
     * Returns a boolean result from a boolean value. Use this method
     * in lieu of the constructor.
     *
     * @param boolean The boolean value
     * @return A boolean result
     */
    public static BooleanResult from( boolean value )
    {
        return value ? TRUE : FALSE;
    }

    
    /**
     * Returns a boolean result from an XPath result. Return true if the
     * XPath result is a numeric value that is not zero or {@link java.lang.Double#NaN},
     * a non-empty string, or a non-empty {@link NodeSet}.
     *
     * @param result An XPath result
     * @return A boolean result
     */
    public static BooleanResult from( XPathResult result )
    {
        return result.booleanValue() ? TRUE : FALSE;
    }


    /**
     * Returns the type of this result.
     *
     * @return {@link XPathResult#BOOLEAN}
     */
    public int getResultType()
    {
        return XPathResult.BOOLEAN;
    }

    
    /**
     * Returns the result as a boolean value.
     *
     * @return The result as a boolean value
     */
    public boolean booleanValue()
    {
        return _value;
    }

    
    /**
     * Returns the result as a number value. Returns 0 for
     * false, 1 for true.
     *
     * @return The result as a number value
     */
    public double numberValue()
    {
        return  _value ? 1.0 : 0.0;
    }

    
    /**
     * Returns the result as a string value. Returns "false" or
     * "true".
     *
     * @return The result as a string value
     */
    public String stringValue()
    {
        return _value ? "true" : "false";
    }


    /**
     * Returns the result as a Java object. Returns an object of
     * type {@link java.lang.Boolean} with the same boolean value.
     *
     * @return The result as a Java object
     */
    public Object javaObject()
    {
        return _value ? Boolean.TRUE : Boolean.FALSE;
    }


    /**
     * Returns true if the given result is a boolean result
     * and has the same boolean value.
     *
     * @param result An XPath result
     * @return True if a boolean result and has same value
     */
    public boolean equals( XPathResult result )
    {
        if ( result == this )
            return true;
        return false;
    }
    

    public String toString()
    {
        return _value ? "true" : "false";
    }
    

}
