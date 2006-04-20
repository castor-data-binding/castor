/*
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
 * The Original Code is XSL:P XSLT processor.
 * 
 * The Initial Developer of the Original Code is Keith Visco.
 * Portions created by Keith Visco (C) 1998-2001 Keith Visco.
 * All Rights Reserved..
 *
 * Contributor(s): 
 * Keith Visco, kvisco@ziplink.net
 *    -- original author. 
 *
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.adaptx.util;

/**
 * An adapter which allows adding and removing error observers
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public abstract class ErrorObserverAdapter implements ErrorObserver {

    private List _errorObservers   = null;

    /**
     * Adds the given ErrorObserver to this DefaultObserver
     * @param observer the ErrorObserver to add
    **/
    public void addErrorObserver(ErrorObserver observer) {

        if (observer == null) return;
        
        if (_errorObservers == null) {
            _errorObservers = new List(3);
            _errorObservers.add(observer);
        }
        else if (!_errorObservers.contains(observer)) {
            _errorObservers.add(observer);
        }
    } //-- addErrorObserver

    
    /**
     * Signals an error with normal level
     *
     * @param exception the Exception that caused the error
    **/
    public void receiveError(Exception exception) {
        receiveError(exception, ErrorObserver.NORMAL);
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
        //-- notify registered observers
        if (_errorObservers != null) {
            for (int i = 0; i < _errorObservers.size(); i++) {
                ErrorObserver observer 
                    = (ErrorObserver) _errorObservers.get(i);
                observer.receiveError(exception, level);
            }
        }
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
        //-- notify registered observers
        if (_errorObservers != null) {
            for (int i = 0; i < _errorObservers.size(); i++) {
                ErrorObserver observer 
                    = (ErrorObserver) _errorObservers.get(i);
                observer.receiveError(exception, message, level);
            }
        }
    } //-- receiveError
    
    /**
     * Signals an error message with normal level
     *
     * @param message the error message
    **/
    public void receiveError(String message) {
        receiveError(message, ErrorObserver.NORMAL);
    } //-- receiveError
    
    /**
     * Signals an error with the given error level
     *
     * @param message the error message
     * @param level the error level
    **/
    public void receiveError(String message, int level) {
        //-- notify registered observers
        if (_errorObservers != null) {
            for (int i = 0; i < _errorObservers.size(); i++) {
                ErrorObserver observer 
                    = (ErrorObserver) _errorObservers.get(i);
                observer.receiveError(message, level);
            }
        }
    } //-- recieveError


    /**
     * Removes all the ErrorObservers from the cascading
     * ErrorObserver list
    **/
    public void removeAllErrorObservers() {
        if (_errorObservers != null)
            _errorObservers.clear();
    } //-- removeAllErrorObservers
    
    /**
     * Removes the given ErrorObserver from the cascading
     * ErrorObserver list
     * @param observer the ErrorObserver to remove
    **/
    public void removeErrorObserver(ErrorObserver observer) {
        if (observer == null) return;
        if (_errorObservers != null) {
            _errorObservers.remove(observer);
        }
    } //-- removeErrorObserver

} //-- ErrorObserverAdapter
