/*
 * (C) Copyright Keith Visco 2003  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.kvisco.com/xslp/license.txt
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
 
package org.exolab.adaptx.jaxp.transform;

import org.exolab.adaptx.util.ErrorObserver;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

/**
 * A simple ErrorObserver implementation which wraps
 * a JAXP 1.1 ErrorListener for use with Adaptx.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class ErrorListenerWrapper 
    implements ErrorObserver 
{

    /**
     * The JAXP ErrorListener in which error messages
     * need to be sent to
     */
    private ErrorListener _listener = null;

    /**
     * Creates a new ErrorListerWrapper for the given
     * ErrorListener
     *
     * @param listener the ErrorListener to create the
     * wrapper for
     */
    public ErrorListenerWrapper(ErrorListener listener) {
        if (listener == null) {
            String err = "The argument 'listener' must not be null.";
            throw new IllegalArgumentException(err);
        }
        _listener = listener;
    } //-- ErrorListenerWrapper

    /**
     * Returns the ErrorListener being wrapped by this 
     * ErrorListenerWrapper.
     *
     * @return the underlying ErrorListener 
     */
    public ErrorListener getListener() {
        return _listener;
    } //-- getListener

    
    /**
     * Signals an error message with normal level
     *
     * @param message the error message
     */
    public void receiveError(String message) {
        try {
            _listener.error(new TransformerException(message));
        } 
        catch (TransformerException tx) {
            //-- need to stop processing
            throw new IllegalStateException(tx.getMessage());
        }
    } //-- receiveError
    

    /**
     * Signals an error message with the given error level
     *
     * @param message the error message
     * @param level the error level
     */
    public void receiveError(String message, int level) {
        try {
            switch (level) {
                case ErrorObserver.FATAL:   
                    _listener.fatalError(new TransformerException(message)); 
                    break;
                case ErrorObserver.WARNING: 
                    _listener.warning(new TransformerException(message));
                    break;
                default:
                    _listener.error(new TransformerException(message)); 
                    break;
            }
        }
        catch (TransformerException tx) {
            //-- need to stop processing
            throw new IllegalStateException(tx.getMessage());
        }
    } //-- receiveError


    /**
     * Signals an error with normal level
     *
     * @param exception the Exception that caused the error
     */
    public void receiveError(Exception exception) {
        try {
            _listener.error(new TransformerException(exception));
        }
        catch (TransformerException tx) {
            //-- need to stop processing
            throw new IllegalStateException(tx.getMessage());
        }
    } //-- receiveError

    /**
     * Signals an error with normal level
     *
     * @param exception the Exception that caused the error
     * @param message an option message, used when additional information
     * can be provided.
     */
    public void receiveError(Exception exception, String message) {
        try {
            _listener.error(new TransformerException(message, exception));
        } 
        catch (TransformerException tx) {
            //-- need to stop processing
            throw new IllegalStateException(tx.getMessage());
        }
    } //-- receiveError

    /**
     * Signals an error with the given error level
     *
     * @param exception the Exception that caused the error
     * @param level the error level
     */
    public void receiveError(Exception exception, int level) {
        try {
            switch (level) {
                case ErrorObserver.FATAL:   
                    _listener.fatalError(new TransformerException(exception)); 
                    break;
                case ErrorObserver.WARNING: 
                    _listener.warning(new TransformerException(exception));
                    break;
                default:
                    _listener.error(new TransformerException(exception));
                    break;
            }
        } catch (TransformerException tx) {
            //-- need to stop processing
            throw new IllegalStateException(tx.getMessage());
        }
    } //-- receiveError

    /**
     * Signals an error with the given error level
     *
     * @param exception the Exception that caused the error
     * @param message an option message, used when additional information
     * can be provided.
     * @param level the error level
     */
    public void receiveError(Exception exception, String message, int level) 
    {
        try {
            switch (level) {
                case ErrorObserver.FATAL:   
                    _listener.fatalError(new TransformerException(message, exception)); 
                    break;
                case ErrorObserver.WARNING: 
                    _listener.warning(new TransformerException(message, exception));    
                    break;
                default:
                    _listener.error(new TransformerException(message, exception));      
                    break;
            }
        } 
        catch (TransformerException tx) {
            //-- need to stop processing
            throw new IllegalStateException(tx.getMessage());
        }
    } //-- receiveError

} //-- ErrorListenerWrapper
