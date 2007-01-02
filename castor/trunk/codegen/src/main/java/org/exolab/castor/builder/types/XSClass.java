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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder.types;

import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * The XML Schema user-defined archetype.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public final class XSClass extends XSType {

    /** The JClass represented by this type. */
    private final JClass  _jClass;
    /** Name of this type. */
    private final String  _name;

    /**
     * Creates a new XSClass with the given JClass reference.
     * @param jClass the JClass type of this XSClass
     */
    public XSClass(final JClass jClass) {
        this(jClass, null);
    } //-- XSClass

    /**
     * Creates a new XSClass with the given JClass reference.
     * @param jClass the JClass associated with this XSType
     * @param schemaTypeName The XML Schema type name
     */
    public XSClass(final JClass jClass, final String schemaTypeName) {
        super(XSType.CLASS);
        this._jClass = jClass;
        if (schemaTypeName != null) {
            this._name = schemaTypeName;
        } else {
            this._name = jClass.getName();
        }
    } //-- XSClass

    /**
     * Transfer facets from the provided simpleType to <code>this</code>.
     *
     * @param simpleType
     *            The SimpleType containing our facets.
     * @see org.exolab.castor.builder.types.XSType#getFacets
     */
    public void setFacets(final SimpleType simpleType) {
        // Not implemented?
    }

    /**
     * Returns the JType that this XSType represents.
     * @return the JType that this XSType represents.
     */
    public JType getJType() {
        return this._jClass;
    } //-- getJType

    /**
     * Returns the name of this class.
     * @return the name of this class.
     */
    public String getName() {
        return this._name;
    } //-- getName

    /**
     * Creates the validation code for an instance of this XSType. The
     * validation code should if necessary create a newly configured
     * TypeValidator, that should then be added to a FieldValidator instance
     * whose name is provided.
     *
     * @param fixedValue
     *            a fixed value to use if any
     * @param jsc
     *            the JSourceCode to fill in.
     * @param fieldValidatorInstanceName
     *            the name of the FieldValidator that the configured
     *            TypeValidator should be added to.
     */
    public void validationCode(final JSourceCode jsc, final String fixedValue,
                               final String fieldValidatorInstanceName) {
        //--TBD
    }

} //-- XSClass
