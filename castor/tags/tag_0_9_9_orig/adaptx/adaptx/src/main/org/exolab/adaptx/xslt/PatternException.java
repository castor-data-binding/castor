/*
 * (C) Copyright Keith Visco 2001  All rights reserved.
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

package org.exolab.adaptx.xslt;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * An exception thrown during parsing of XSLT patterns
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
public class PatternException extends Exception {
  
    private Exception _exception = null;
    
      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new PatternException
     *
     * @param message the error message for this exception
    **/
    public PatternException( String message ) { 
        super(message);
    } //-- PatternException
    
    /**
     * Creates a new PatternException with the given nested
     * exception.
     *
     * @param exception the nested exception
    **/
    public PatternException(Exception exception) {
        super("");
        if (exception != null) {
            this._exception = exception;
        }
    } //-- PatternException(Exception)

    /**
     * Creates a new PatternException with the given message
     * and nested exception.
     *
     * @param message the detail message for this exception
     * @param exception the nested exception
    **/
    public PatternException(String message, Exception exception) {
        super(message);
        this._exception = exception;
    } //-- PatternException(String, Exception)
  
      //------------------/
     //- Public Methods -/
    //------------------/

    /**
     * Returns the exception, which in turn caused this Exception to
     * be thrown, or null if nested exception exists.
     * @return the exception, which in turn caused this Exception to
     * be thrown, or null if nested exception exists.
    **/
    public Exception getException() {
        return _exception;
    } //-- getException

    /**
     * Returns the error message for this Exception
     * @return the error message
    **/
    public String getMessage() {
        String message;
        if (_exception != null)
            message = _exception.getMessage();
        else
            message = super.getMessage();
        return message;
    } //-- getMessage


    public void printStackTrace()
    {
        if ( _exception == null )
            super.printStackTrace();
        else
            _exception.printStackTrace();
    }

    public void printStackTrace( PrintWriter printer )
    {
        if ( _exception == null )
            super.printStackTrace( printer );
        else
            _exception.printStackTrace( printer);
    }

    public void printStackTrace( PrintStream printer )
    {
        if ( _exception == null )
            super.printStackTrace( printer );
        else
            _exception.printStackTrace( printer );
    }

    /**
     * Returns the String representation of this Exception
     * @return the String representation of this Exception
    **/
    public String toString() {
        return getMessage();
    } //-- toString
    
} //-- PatternException
