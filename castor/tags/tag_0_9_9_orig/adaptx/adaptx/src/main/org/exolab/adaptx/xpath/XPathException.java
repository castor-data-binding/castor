/*
 * (C) Copyright Keith Visco 1998, All rights reserved.
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


import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * Indicates an error occured while evaluating an XPath expression.
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class XPathException
    extends Exception
{
    

    /**
     * The nested exception which caused the error
    **/
    private Exception   _exception;

    /**
     * Creates a new XPathException with the given message
     *
     * @param message the error message for this XPathException
    **/
    public XPathException( String message ) {
        super( message );
        _exception = null;
    } //-- XPathException

    /**
     * Creates a new XPathException with the given message
     * and exception.
     *
     * @param message the error message for this XPathException
     * @param exception the Exception which caused the error.
    **/
    public XPathException( String message, Exception exception ) {
        super( message );
        
        if (( exception instanceof XPathException ) && 
             ( (XPathException) exception )._exception != null )
            _exception = ( (XPathException) exception )._exception;
        else
            _exception = exception;
            
    } //-- XPathException

    /**
     * Creates a new XPathException with the given exception.
     *
     * @param exception the Exception which caused the error.
    **/
    public XPathException( Exception exception ) {
        this( exception.getMessage(), exception );
    } //-- XPathException


    /**
     * Returns the nested exception for this XPathException.
     *
     * @return the nested exception, or null if no nested exception 
     * exists.
    **/     
    public Exception getException() {
        return _exception;
    } //-- getException


    /**
     * Prints the stack trace for this exception
    **/
    public void printStackTrace() {
        if ( _exception == null )
            super.printStackTrace();
        else
            _exception.printStackTrace();
    } //-- printStackTrace


    /**
     * Prints the stack trace for this exception
     *
     * @param stream the PrintStream to print the stack trace to.
    **/
    public void printStackTrace( PrintStream stream ) {
        if ( _exception == null )
            super.printStackTrace( stream );
        else
            _exception.printStackTrace( stream );
    } //-- printStackTrace

    /**
     * Prints the stack trace for this exception
     *
     * @param writer the PrintWriter to print the stack trace to.
    **/
    public void printStackTrace( PrintWriter writer ) {
        if ( _exception == null )
            super.printStackTrace( writer );
        else
            _exception.printStackTrace( writer );
    } //-- printStackTrace
       
} //-- XPathException
