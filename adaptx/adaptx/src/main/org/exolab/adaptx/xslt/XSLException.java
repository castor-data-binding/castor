/*
 * (C) Copyright Keith Visco 1998  All rights reserved.
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by 
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or 
 * lost profits even if the Copyright owner has been advised of the 
 * possibility of their occurrence.  
 */

package org.exolab.adaptx.xslt;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * The main exception thrown during XSLT processing
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
public class XSLException extends Exception {
  
    public static final int INVALID_CHILD_NODE               = 0;
    public static final int INVALID_RULE                     = 1;
    public static final int INVALID_MATCH_PATTERN            = 2;
    public static final int INVALID_SELECT_PATTERN           = 3;
    public static final int INVALID_XSL_ELEMENT              = 4;
    public static final int INVALID_ATTRIBUTE_VALUE_TEMPLATE = 5;
    public static final int MISSING_REQUIRED_ATTR            = 6;
      
    public static String[] errorMessages = {
        "Invalid child node - ",
        "Invalid Rule - ",
        "Invalid Match Pattern - ",
        "Invalid Select Pattern - ",
        "Invalid XSL Element - ",
        "Invalid attribute value template - ",
        "Missing required attribute - "
    };
  
    private int error = -1;
    
    private static String prefix = "XSLException: ";
    
    private Exception _exception = null;
    
      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new XSLException
     * @param message the error message for this exception
    **/
    public XSLException( String message ) { 
        super(message);
    } //-- XSLException
    
    /**
     * Creates a new XSLException
     * @param error the error code of this exception
     * @param message the error message for this exception
    **/
    public XSLException( int error, String message ) { 
        super(message);
        this.error = error;
    } //--XSLException
  
    /**
     * Creates a new XSLException with the given nested
     * exception.
     * @param exception the nested exception
    **/
    public XSLException(Exception exception) {
        super("");
        if (exception != null) {
            this._exception = exception;
        }
    } //-- XSLException(Exception)

    /**
     * Creates a new XSLException with the given message
     * and nested exception.
     * @param message the detail message for this exception
     * @param exception the nested exception
    **/
    public XSLException(String message, Exception exception) {
        super(message);
        this._exception = exception;
    } //-- XSLException(String, Exception)
  
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
            
        if ((error >= 0) && (error < errorMessages.length)) {
            return errorMessages[error] + message;
        }
        
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
    
} //-- XSLException
