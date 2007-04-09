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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml;

import org.exolab.castor.xml.location.Location;

/**
 * An Exception that can be used to signal XML validation errors.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public class ValidationException extends XMLException {
    /** SerialVersionUID */
    private static final long serialVersionUID = 2220902174700444631L;

    /** The location for this Exception. */
    private Location            _location   = null;
    /** The next Exception in the list, allowing the reporting of several
     * validation Exceptions. */
    private ValidationException _next       = null;

    /**
     * Creates a new ValidationException with no message or nested Exception.
     */
    public ValidationException() {
        super();
    }

    /**
     * Creates a new ValidationException with the given message.
     * @param message the message for this Exception
     */
    public ValidationException(final String message) {
        super(message);
    }

    /**
     * Creates a new ValidationException with the given message.
     * @param message the message for this Exception
     * @param errorCode the errorCode for this Exception
     */
    public ValidationException(final String message, final int errorCode) {
        super(message, errorCode);
    }

    /**
     * Creates a new ValidationException with the given nested Exception.
     * @param exception the nested Exception
     */
    public ValidationException(final Throwable exception) {
        super(exception);
    }

    /**
     * Creates a new ValidationException with the given message and nested
     * Exception.
     *
     * @param message the detail message for this Exception
     * @param exception the nested Exception
     */
    public ValidationException(final String message, final Throwable exception) {
        super(message, exception);
    }

    /**
     * Creates a new ValidationException with the given message, nested
     * Exception, and errorCode.
     *
     * @param message the detail message for this Exception
     * @param exception the nested Exception
     * @param errorCode the errorCode for this Exception
     */
    public ValidationException(final String message, final Exception exception,
                               final int errorCode) {
        super(message, exception, errorCode);
    }

    /**
     * Returns the location of the Exception.
     *
     * @return the location of the Exception.
     */
    public Location getLocation() {
        return _location;
    }

    /**
     * Returns the next ValidationException in the list, or null if no
     * additional validation exceptions exist.
     *
     * @return the next ValidationException in the list, or null if there are no
     *         additional Exceptions.
     */
    public ValidationException getNext() {
        return _next;
    }

    /**
     * Sets the location information for this ValidationException.
     *
     * @param location The location information for this validation Exception.
     */
    public void setLocation(final Location location) {
        _location = location;
    }

    /**
     * Removes the given ValidationException from the current list of
     * ValidationException.
     *
     * @param exception the ValidationException to remove
     * @return true if the given ValidationException was successfully removed.
     */
    protected boolean remove(final ValidationException exception) {
        if (exception == null) {
            return false;
        }

        ValidationException previous = this;
        for (ValidationException current = _next; current != null;
                                                  previous = current, current = current._next) {
            if (current == exception) {
                previous._next = current._next;
                current._next = null;
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the given ValidationException as the last exception in the list.
     * This is equivalent to calling {@link #setNext} if no additional
     * ValidationException(s) exist.
     *
     * @param exception the ValidationException to set as the last exception in
     *        the list.
     */
    protected void setLast(final ValidationException exception) {
        if (exception == null) {
            return;
        }

        ValidationException current = this;
        while (current._next != null) {
            current = current._next;
        }
        current._next = exception;
    }

    /**
     * Sets the given ValidationException as the next Exception in the list.
     * This method will overwrite any existing ValidationException that may
     * already exist as the next Exception.
     *
     * @param exception the ValidationException to set as the next Exception in
     *        the list.
     */
    public void setNext(final ValidationException exception) {
        _next = exception;
    }

    /**
     * Returns the String representation of this ValidationException.
     * @return the String representation of this ValidationException.
     */
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        if (getNext() != null) {
            int count = 1;
            for (ValidationException vx = this; vx != null; vx = vx.getNext()) {
                if (count > 1) {
                    sb.append("\n\n");
                }
                sb.append(count);
                sb.append(". ");
                dumpOneException(sb, vx);
                ++count;
            }
        } else {
            dumpOneException(sb, this);
        }
        return sb.toString();
    }

    /**
     * Dump information for a single ValidationException.
     * @param sb The StringBuffer to which we print information
     * @param vx The ValidationException for which we print information.
     */
    private void dumpOneException(final StringBuffer sb, final ValidationException vx) {
        sb.append("ValidationException: ");
        String message = vx.getMessage();
        if (message != null) {
            sb.append(message);
        }
        Location location = vx.getLocation();
        if (location != null) {
            sb.append(";\n   - location of error: ");
            sb.append(location.toString());
        }
        Throwable t = vx.getCause();
        if (t != null) {
            sb.append("\n");
            sb.append(t.getMessage());
        }
    }

}
