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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml.handlers;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;
import org.exolab.castor.xml.util.ContainerElement;

/**
 * The FieldHandler for ContainerElement.
 *
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 * @see FieldDescriptor org.exolab.castor.mapping.FieldDescriptor
 * @see FieldHandler .org.exolab.castor.mapping.FieldHandler
 */
public final class ContainerFieldHandler implements FieldHandler {

    /** Automatically choose the mode to use. */
    public static final int MODE_AUTO        = 0;
    /** When the field is not a ContainerElement, this mode is used. */
    public static final int MODE_PARENT_LINK = 1;
    /** The mode for a ContainerElement. getValue and setValue operate on the parent. */
    public static final int MODE_CHILD_LINK  = 2;

    /** The actual FieldHandler to delegate to. */
    private final FieldHandler _handler;
    /** Mode to use for this ContainerFieldHandler. */
    private final int _mode = MODE_AUTO;

    /**
     * Creates a new ContainerFieldHandler with the given FieldHandler.
     * @param handler The field handler to delegate to.
     */
    public ContainerFieldHandler(final FieldHandler handler) {
        super();
        _handler = handler;
    } //-- ContainerFieldHandler

    //-----------------------------/
    //- Methods from FieldHandler -/
    //-----------------------------/

    /**
     * Returns the value of the field from the object. If mode is
     * MODE_CHILD_LINK or mode is MODE_AUTO and the object is a
     * ContainerElement, then the value of the parent is returned. Otherwise, a
     * new ContainerElement is returned. The value of this new ContainerElement
     * is the value of the provided object and the parent of the new
     * ContainerElement is the provided object.
     *
     * @param object The object to get the value of
     * @return The value of the field
     * @throws IllegalStateException The Java object has changed and is no
     *         longer supported by this handler, or the handler is not
     *         compatible with the Java object
     */
    public Object getValue(final Object object) throws IllegalStateException {
        int mode = _mode;
        if (mode == MODE_AUTO) {
            if (object instanceof ContainerElement) {
                mode = MODE_CHILD_LINK;
            } else {
                mode = MODE_PARENT_LINK;
            }
        }

        if (mode == MODE_CHILD_LINK) {
            return _handler.getValue(((ContainerElement) object).getParent());
        }

        // MODE_PARENT_LINK
        ContainerElement container = new ContainerElement(_handler.getValue(object));
        container.setParent(object);
        return container;
    } //-- getValue

    /**
     * Creates a new instance of the object described by this field. Of the
     * object provided is a ContainerElement, then a new isntance of the parent
     * object is returned. Otherwise a new ContainerElement is created and
     * returned, with the parent set to the provided object.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple type and cannot be
     *         instantiated
     */
    public Object newInstance(final Object parent) throws IllegalStateException {
        //-- MODE_CHILD_LINK and MODE_AUTO
        if (parent instanceof ContainerElement) {
            return _handler.newInstance(((ContainerElement) parent).getParent());
        }

        // MODE_PARENT_LINK
        ContainerElement container = new ContainerElement();
        container.setParent(parent);
        return container;
    } //-- newInstance

    /**
     * Sets the value of the field to a default value.
     * <p>
     * Reference fields are set to null, primitive fields are set to their
     * default value, collection fields are emptied of all elements.
     *
     * @param object The object
     * @throws IllegalStateException The Java object has changed and is no
     *         longer supported by this handler, or the handler is not
     *         compatiable with the Java object
     */
    public void resetValue(final Object object) throws IllegalStateException {
        _handler.resetValue(object);
    }

    /**
     * Sets the value of the field on the object. That is, sets the value of the
     * container parent to the provided value.
     *
     * @param object The object whose value to set.
     * @param value The new value
     * @throws IllegalStateException The Java object has changed and is no
     *         longer supported by this handler, or the handler is not
     *         compatiable with the Java object
     * @throws IllegalArgumentException The value passed is not of a supported
     *         type
     */
    public void setValue(final Object object, final Object value)
                        throws IllegalStateException, IllegalArgumentException {
        if (_mode == MODE_PARENT_LINK) {
            // Do nothing for MODE_PARENT_LINK; the container is not part of the object model
            return;
        }

        // For MODE_AUTO and MODE_CHILD_LINK:
        if (object instanceof ContainerElement) {
            _handler.setValue(((ContainerElement) object).getParent(), value);
        }
    } //-- setValue

    /**
     * Checks the field validity. Returns successfully if the field can be
     * stored, is valid, etc, throws an exception otherwise.
     *
     * @param object The object
     * @throws ValidityException The field is invalid, is required and null, or
     *         any other validity violation
     * @throws IllegalStateException The Java object has changed and is no
     *         longer supported by this handler, or the handler is not
     *         compatiable with the Java object
     */
    public void checkValidity(final Object object) throws ValidityException, IllegalStateException {
        //-- deprecated...do nothing
    } //-- checkValidity

} //-- ContainerFieldHandler
