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
package org.exolab.castor.xml.util;

/**
 * A light-weight object used to wrap any object. This allows Castor to
 * effectively "wrap" and "unwrap" elements during marshaling and unmarshaling.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 */
public class ContainerElement {

    /** The "parent" object, i.e., the object that the contained object actually belongs to. */
    private Object _parent = null;
    /** The object "contained" within this ContainerElement. */
    private Object _object = null;

    /**
     * Creates a new empty ContainerElement.
     */
    public ContainerElement() {
        super();
    } //-- ContainerElement

    /**
     * Creates a ContainerElement with the specified object.
     *
     * @param object the Object to set
     */
    public ContainerElement(Object object) {
        super();
        _object = object;
    } //-- ContainerElement

    /**
     * Returns the object contained within this ContainerElement.
     *
     * @return the object contained within this ContainerElement.
     */
    public Object getObject() {
        return _object;
    } //-- getObject

    /**
     * Returns the parent of the contained object.
     *
     * @return the parent of the contained object.
     */
    public Object getParent() {
        return _parent;
    } //-- getParent

    /**
     * Sets the object that is contained within this ContainerElement.
     *
     * @param object the Object to set
     */
    public void setObject(Object object) {
        _object = object;
    } //-- setObject

    /**
     * Sets the parent of the contained object.
     *
     * @param parent the parent of the contained object
     */
    public void setParent(Object parent) {
        _parent = parent;
    } //-- setParent

} //-- class: ContainerElement
