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

package org.exolab.adaptx.xslt.util;

import org.exolab.adaptx.util.ErrorObserver;
import org.exolab.adaptx.util.NestedRuntimeException;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Vector;

/**
 * A simple implementation of MessageObserver and ErrorObserver.
 * This observer is cascading, which means you can add observers
 * to it, and it will cascade notifications.
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class DefaultObserver 
    implements MessageObserver, ErrorObserver
{
    
    private static final String ERROR_PREFIX   = "error: ";
    private static final String FATAL_PREFIX   = "fatal error: ";
    private static final String WARNING_PREFIX = "warning: ";
    
    private Vector _errorObservers   = null;
    private Vector _messageObservers = null;
    
    /**
     * The PrintWriter to write all messages to
    **/
    private PrintWriter _out = null;

    /**
     * The flag that treats all errors as fatal
    **/
    private boolean _allErrorsFatal = false;
    
    /**
     * Creates a new SimpleMessageObserver which will
     * print messages to the console. A fatal error
     * will cause a RuntimeException.
    **/
    public DefaultObserver() {
        _out = new PrintWriter(System.out, true);
    } //-- DefaultObserver

    /**
     * Creates a new SimpleMessageObserver which will
     * print messages to the console. A fatal error
     * will cause a RuntimeException.
     * @param allErrorsFatal a boolean, when true will
     * treat all errors as fatal errors (excludes warnings).
    **/
    public DefaultObserver(boolean allErrorsFatal) {
        _out = new PrintWriter(System.out, true);
        _allErrorsFatal = allErrorsFatal;
    } //-- DefaultObserver

    /**
     * Creates a new SimpleMessageObserver which will
     * print messages to the console. A fatal error
     * will cause a RuntimeException.
     * @param writer the Writer to print messages to. This
     * writer may be null, to create a null sink.
     *
    **/
    public DefaultObserver(Writer writer) {
        setWriter(writer);
    } //-- DefaultObserver
    
    /**
     * Creates a new SimpleMessageObserver which will
     * print messages to the console. A fatal error
     * will cause a RuntimeException.
     * @param writer the Writer to print messages to. This
     * writer may be null, to create a null sink.
     * @param allErrorsFatal a boolean, when true will
     * treat all errors as fatal errors (excludes warnings).
    **/
    public DefaultObserver(Writer writer, boolean allErrorsFatal) {
        setWriter(writer);
        _allErrorsFatal = allErrorsFatal;
    } //-- DefaultObserver
    
    /**
     * Adds the given ErrorObserver to this DefaultObserver
     * @param observer the ErrorObserver to add
    **/
    public void addErrorObserver(ErrorObserver observer) {
        if (observer == null) return;
        if (_errorObservers == null) {
            _errorObservers = new Vector(3);
            _errorObservers.addElement(observer);
        }
        else if (!_errorObservers.contains(observer)) {
            _errorObservers.addElement(observer);
        }
    } //-- addErrorObserver

    /**
     * Adds the given MessageObserver to this DefaultObserver
     * @param observer the MessageObserver to add
    **/
    public void addMessageObserver(MessageObserver observer) {
        if (observer == null) return;
        if (_messageObservers == null) {
            _messageObservers = new Vector(3);
            _messageObservers.addElement(observer);
        }
        else if (!_messageObservers.contains(observer)) {
            _messageObservers.addElement(observer);
        }
    } //-- addMessageObserver
    
    
    /**
     * Signals an error with normal level
     *
     * @param exception the Exception that caused the error
    **/
    public void receiveError(Exception exception) {
        receiveError(exception, null, ErrorObserver.NORMAL);
    } //-- receieveError

    /**
     * Signals an error with normal level
     *
     * @param exception the Exception that caused the error
     * @param message an option message, used when additional information
     * can be provided.
    **/
    public void receiveError(Exception exception, String message) {
        receiveError(exception, message, ErrorObserver.NORMAL);
    } //-- receiveError
    
    /**
     * Signals an error with the given error level
     *
     * @param exception the Exception that caused the error
     * @param level the error level
    **/
    public void receiveError(Exception exception, int level) {
        receiveError(exception, null, level);
    } //-- receiveError
    
    /**
     * Signals an error with the given error level
     *
     * @param exception the Exception that caused the error
     * @param message an option message, used when additional information
     * can be provided.
     * @param level the error level
    **/
    public void receiveError(Exception exception, String message, int level) {
        String prefix = null;
        
        boolean fatal = false;
        
        switch(level) {
            case ErrorObserver.FATAL:
                prefix = FATAL_PREFIX;
                fatal = true;
                break;
            case ErrorObserver.WARNING:
                prefix = WARNING_PREFIX;
                break;
            default:
                prefix = ERROR_PREFIX;
                fatal = _allErrorsFatal;
                break;
        }
        
        if ((_out != null) && (!fatal)) {
            _out.print(prefix);
            if (exception != null) {
                _out.print(exception.toString());
                if (message != null) {
                    _out.print("; ");
                    _out.println(message);               
                }
                else _out.println();
                exception.printStackTrace(_out);
            }
            else if (message != null) {
                _out.println(message);
            }
            else {
                _out.println("no exception or error message given.");
            }
            _out.flush();
        }
        
        //-- cascade error message
        if (_errorObservers != null) {
            for (int i = 0; i < _errorObservers.size(); i++) {
                ErrorObserver observer 
                    = (ErrorObserver) _errorObservers.elementAt(i);
                observer.receiveError(exception, message, level);
            }
        }
        
        if (fatal) {
            String err = null;
            if ((message == null) && (exception == null))
                err = "no exception or error message given.";
            else if (message == null)
                err = exception.toString();
            else if (exception == null)
                err = message;
            else {
                err = exception.toString() + "; " + message;
            }
            throw new NestedRuntimeException(prefix+err,exception);
        }
    } //-- receiveError
    
    /**
     * Signals an error message with normal level
     * @param message the error message
    **/
    public void receiveError(String message) {
        receiveError(null, message, ErrorObserver.NORMAL);
    } //-- receiveError
    
    /**
     * Signals an error message with the given error level
     * @param message the error message
     * @param level the error level
    **/
    public void receiveError(String message, int level) {
        receiveError(null, message, level);
    } //-- recieveError
    
    /**
     * Notifies this observer of a new message
     * @param message the message to observe
    **/
    public void receiveMessage(String message) {
        if (_out == null) return;
        
        _out.print("xsl:message - ");
        _out.println(message);
        _out.flush();
        
        //-- cascade message
        if (_messageObservers != null) {
            for (int i = 0; i < _messageObservers.size(); i++) {
                MessageObserver observer 
                    = (MessageObserver) _messageObservers.elementAt(i);
                observer.receiveMessage(message);
            }
        }
    } //-- recieve
    
    /**
     * Removes all the ErrorObservers from the cascading
     * ErrorObserver list
    **/
    public void removeAllErrorObservers() {
        if (_errorObservers != null)
            _errorObservers.removeAllElements();
    } //-- removeAllErrorObservers
    
    /**
     * Removes all the MessageObservers from the cascading
     * ErrorObserver list
    **/
    public void removeAllMessageObservers() {
        if (_messageObservers != null)
            _messageObservers.removeAllElements();
    } //-- removeAllMessageObservers
    
    /**
     * Removes the given ErrorObserver from the cascading
     * ErrorObserver list
     * @param observer the ErrorObserver to remove
    **/
    public void removeErrorObserver(ErrorObserver observer) {
        if (observer == null) return;
        if (_errorObservers != null) {
            _errorObservers.removeElement(observer);
        }
    } //-- removeErrorObserver

    /**
     * Removes the given MessageObserver from the cascading
     * MessageObserver list
     * @param observer the MessageObserver to remove
    **/
    public void removeMessageObserver(MessageObserver observer) {
        if (observer == null) return;
        if (_messageObservers != null) {
            _messageObservers.removeElement(observer);
        }
    } //-- removeMessageObserver
    
    /**
     * Sets the Writer of this MessageObserver
     * @param writer the Writer to print messages to
    **/
    public void setWriter(Writer writer) {
        if (writer instanceof PrintWriter)
            _out = (PrintWriter) writer;
        else 
            _out = new PrintWriter(writer, true);
    } //-- setWriter
    
} //-- MessageObserver
