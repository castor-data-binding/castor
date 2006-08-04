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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

/**
 * An exception that can be used to signal XML validation errors
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
**/
public class ValidationException extends XMLException {
    /** SerialVersionUID */
    private static final long serialVersionUID = 2220902174700444631L;

    /**
     * The location for this Exception
     */
    private Location  _location   = null;
    
    /**
     * The next exception in the list, allows for reporting
     * of several validation exceptions
     */
    private ValidationException _next = null;
    
    /**
     * Creates a new ValidationException with no message,
     * or nested Exception
    **/
    public ValidationException() {
        super();
    } //-- ValidationException
    
    /**
     * Creates a new ValidationException with the given message.
     * @param message the message for this Exception
    **/
    public ValidationException(String message) {
        super(message);
    } //-- ValidationException(String)

    /**
     * Creates a new ValidationException with the given message.
     * @param message the message for this Exception
     * @param errorCode the errorCode for this Exception
    **/
    public ValidationException(String message, int errorCode) {
        super(message, errorCode);
    } //-- ValidationException(String)
    
    /**
     * Creates a new ValidationException with the given nested
     * exception.
     * @param exception the nested exception
    **/
    public ValidationException(Throwable exception) {
        super(exception);
    } //-- ValidationException(Exception)

    /**
     * Creates a new ValidationException with the given message
     * and nested exception.
     * @param message the detail message for this exception
     * @param exception the nested exception
    **/
    public ValidationException(String message, Throwable exception) {
        super(message, exception);
    } //-- ValidationException(String, Exception)

    /**
     * Creates a new ValidationException with the given message,
     * nested exception, and errorCode.
     * @param message the detail message for this exception
     * @param exception the nested exception
     * @param errorCode the errorCode for this Exception
    **/
    public ValidationException
        (String message, Exception exception, int errorCode) 
    {
        super(message, exception, errorCode);
    } //-- ValidationException(String, Exception, int)
        
    /**
     * Returns the location of the exception
     *
     * @return the location of the exception
     */
    public Location getLocation() {
        return _location;
    } //-- getLocation
    
    /**
     * Returns the next ValidationException in the list, or null
     * if no additional validation exceptions exist.
     *
     * @return the next ValidationException in the list, or null if
     * there are no additional exceptions.
     */
    public ValidationException getNext() {
        return _next;
    } //-- getNext
    
    /**
     * Sets the location information for this ValidationException
     * @param location, the location information for this validation
     * exception
    **/
    public void setLocation(Location location) {
        this._location = location;
    } //-- setLocation
    
    /**
     * Removes the given ValidationException from the current
     * list of ValidationException.
     *
     * @param exception the ValidationException to remove
     * @return true if the given ValidationException was
     * successfully removed.
     */
    protected boolean remove(ValidationException exception) {
        
        if (exception == null) return false;
        
        ValidationException current = _next;
        ValidationException previous = this;
        while (current != null) {
            if (current == exception) {
                previous._next = current._next; 
                current._next = null;
                return true;
            }
            current = current._next;
        }
        return false;
    } //-- remove
    
    /**
     * Adds the given ValidationException as the last exception
     * in the list. This is equivalent to calling #setNext if no
     * additional ValidationException(s) exist.
     *
     * @param exception the ValidationException to set as the last
     * exception in the list.
     */
    protected void setLast(ValidationException exception) {
        if (exception == null) return;
        
        ValidationException current = this;
        while (current._next != null) {
            current = current._next;
        }
        current._next = exception;
    } //-- setLast
    
    /**
     * Sets the given ValidationException as the next exception
     * in the list. This method will overwrite any existing
     * ValidationException that may already exist as the next
     * exception.
     *
     * @param exception the ValidationException to set as the next
     * exception in the list.
     */
    protected void setNext(ValidationException exception) {
        _next = exception;
    } //-- setNext
    

    /**
     * Returns the String representation of this Exception
     * @return the String representation of this Exception
    **/
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (getNext() != null) {
            int count = 1;
            ValidationException vx = this;
            while (vx != null) {
                if (count > 1) {
                    sb.append('\n');
                    sb.append('\n');
                }
                sb.append(count);
                sb.append(". ValidationException: ");
                String message = vx.getMessage();
                if (message != null) sb.append(message);
                Location location = getLocation();
                if (location != null) {
                    sb.append(";\n   - location of error: ");
                    sb.append(location.toString());
                }
                vx = vx.getNext();
                ++count;
            }
        }
        else {
            sb.append("ValidationException: ");
            String message = getMessage();
            if (message != null) sb.append(message);
            if (_location != null) {
                sb.append(";\n   - location of error: ");
                sb.append(_location.toString());
            }
            Throwable t = getCause();
            if (t!=null) {
                sb.append("\n");
                sb.append(t.getMessage());
            }
        }
        return sb.toString();
    } //-- toString

} //-- ValidationException
