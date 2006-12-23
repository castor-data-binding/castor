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

import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * The XML Schema dateTime type.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public final class XSDateTime extends XSPatternBase {
    /** Jakarta's common-logging logger. */
    private static final Log LOG = LogFactory.getLog(XSDateTime.class);

    /** The JType represented by this XSType. */
    private static final JType JTYPE = new JClass("java.util.Date");

    /** Maximum Date (inclusive). */
    private String _maxInclusive;
    /** Maximum Date (exclusive). */
    private String _maxExclusive;
    /** Minimum Date (inclusive). */
    private String _minInclusive;
    /** Minimum Date (exclusive). */
    private String _minExclusive;

    /**
     *  No-Arg constructor.
     */
    public XSDateTime() {
        super(XSType.DATETIME_TYPE);
    } //-- XSDateTime

    /**
     * Returns the String necessary to convert an Object to an instance of this
     * XSType. This method is really only useful for primitive types
     *
     * @param variableName
     *            the name of the Object
     * @return the String necessary to convert an Object to an instance of this
     *         XSType
     */
    public String createFromJavaObjectCode(final String variableName) {
        return "(java.util.Date) " + variableName;
    } //-- fromJavaObject

    /**
     * Returns the JType that this XSType represents.
     * @return the JType that this XSType represents.
     */
    public JType getJType() {
        return XSDateTime.JTYPE;
    }

    /**
     * Returns the maximum exclusive value that this XSDateTime can hold.
     * @return the maximum exclusive value that this XSDateTime can hold. If
     * no maximum exclusive value has been set, Null will be returned
     * @see #getMaxInclusive
     */
    public String getMaxExclusive() {
        return _maxExclusive;
    } //-- getMaxExclusive

    /**
     * Returns the maximum inclusive value that this XSDateTime can hold.
     * @return the maximum inclusive value that this XSDateTime can hold. If
     * no maximum inclusive value has been set, Null will be returned
     * @see #getMaxExclusive
     */
    public String getMaxInclusive() {
        return _maxInclusive;
    } //-- getMaxInclusive

    /**
     * Returns the minimum exclusive value that this XSDateTime can hold.
     * @return the minimum exclusive value that this XSDateTime can hold. If
     * no minimum exclusive value has been set, Null will be returned
     * @see #getMinInclusive
     */
    public String getMinExclusive() {
        return _minExclusive;
    } //-- getMinExclusive

    /**
     * Returns the minimum inclusive value that this XSDateTime can hold.
     * @return the minimum inclusive value that this XSDateTime can be.
     * @see #getMinExclusive
     */
    public String getMinInclusive() {
        return _minInclusive;
    } //-- getMinInclusive

    /**
     * Sets the maximum exclusive value that this XSDateTime can hold.
     * @param max the maximum exclusive value this XSDateTime can hold.
     * @see #setMaxInclusive
     */
    public void setMaxExclusive(final String max) {
        _maxExclusive = max;
        _maxInclusive = null;
    }

    /**
     * Sets the maximum inclusive value that this XSDateTime can hold.
     * @param max the maximum inclusive value this XSDateTime can hold.
     * @see #setMaxExclusive
     */
    public void setMaxInclusive(final String max) {
        _maxExclusive = null;
        _maxInclusive = max;
    }

    /**
     * Sets the minimum exclusive value that this XSDateTime can hold.
     * @param min the minimum exclusive value this XSDateTime can hold.
     * @see #setMinInclusive
     */
    public void setMinExclusive(final String min) {
        _minExclusive = min;
        _minInclusive = null;
    }

    /**
     * Sets the minimum inclusive value that this XSDateTime can hold.
     * @param min the minimum inclusive value this XSDateTime can hold.
     * @see #setMinExclusive
     */
    public void setMinInclusive(final String min) {
        _minExclusive = null;
        _minInclusive = min;
    }

    /**
     * Returns true if a maximum (inclusive or exclusive) has been set.
     * @return true if a maximum (inclusive or exclusive) has been set.
     */
    public boolean hasMaximum() {
        return _maxInclusive != null || _maxExclusive != null;
    }

    /**
     * Returns true if a minimum (inclusive or exclusive) has been set.
     * @return true if a minimum (inclusive or exclusive) has been set.
     */
    public boolean hasMinimum() {
        return _minInclusive != null || _minExclusive != null;
    }

    /**
     * Transfer facets from the provided simpleType to <code>this</code>. The
     * DateTime SimpleType supports the following facets:
     * <ul>
     *   <li>pattern</li>
     *   <li>enumeration (handled elsewhere, so we ignore it here)</li>
     *   <li>whiteSpace</li>
     *   <li>maxInclusive</li>
     *   <li>maxExclusive</li>
     *   <li>minInclusive</li>
     *   <li>minExclusive</li>
     * </ul>
     *
     * @param simpleType
     *            The SimpleType containing our facets.
     * @see org.exolab.castor.builder.types.XSType#getFacets
     */
    public void setFacets(final SimpleType simpleType) {
        Enumeration enumeration = getFacets(simpleType);
        while (enumeration.hasMoreElements()) {
            Facet facet = (Facet) enumeration.nextElement();
            String name = facet.getName();

            if (Facet.MAX_EXCLUSIVE.equals(name)) {
                setMaxExclusive(facet.getValue());
            } else if (Facet.MAX_INCLUSIVE.equals(name)) {
                setMaxInclusive(facet.getValue());
            } else if (Facet.MIN_EXCLUSIVE.equals(name)) {
                setMinExclusive(facet.getValue());
            } else if (Facet.MIN_INCLUSIVE.equals(name)) {
                setMinInclusive(facet.getValue());
            } else if (Facet.PATTERN.equals(name)) {
                setPattern(facet.getValue());
            } else if (Facet.WHITESPACE.equals(name)) {
                // If this facet is set correctly, we don't need to do anything
                if (!facet.getValue().equals(Facet.WHITESPACE_COLLAPSE)) {
                    LOG.warn("Warning: The facet 'whitespace' can only be set to '"
                             + Facet.WHITESPACE_COLLAPSE + "' for DateTime.");
                }
            }
        }
    }

    /**
     * Creates the validation code for an instance of this XSType. The
     * validation code should if necessary create a newly configured
     * TypeValidator, that should then be added to a FieldValidator instance
     * whose name is provided.
     *
     * @param jsc
     *            the JSourceCode to fill in.
     * @param fixedValue
     *            a fixed value to use if any
     * @param fieldValidatorInstanceName
     *            the name of the FieldValidator that the configured
     *            TypeValidator should be added to.
     */
    public void validationCode(final JSourceCode jsc, final String fixedValue,
                               final String fieldValidatorInstanceName) {
        jsc.add("org.exolab.castor.xml.validators.DateTimeValidator typeValidator ="
                + " new org.exolab.castor.xml.validators.DateTimeValidator();");

        boolean addTryCatch = _minInclusive != null || _minExclusive != null
                || _maxInclusive != null || _maxExclusive != null /* || fixedValue != null */;

        if (addTryCatch) {
            jsc.add("try {");
            jsc.indent();
        }

        // minInclusive / minExclusive facets (only one or the other, never both)
        if (_minInclusive != null) {
            jsc.add("org.exolab.castor.types.DateTime min = "
                    + "new org.exolab.castor.types.DateTime(\"" + _minInclusive + "\");");
            jsc.add("typeValidator.setMinInclusive(min);");
        } else if (_minExclusive != null) {
            jsc.add("org.exolab.castor.types.DateTime min = "
                    + "new org.exolab.castor.types.DateTime(\"" + _minExclusive + "\");");
            jsc.add("typeValidator.setMinExclusive(min);");
        }

        // maxInclusive / maxExclusive facets (only one or the other, never both)
        if (_maxInclusive != null) {
            jsc.add("org.exolab.castor.types.DateTime max = "
                    + "new org.exolab.castor.types.DateTime(\"" + _maxInclusive + "\");");
            jsc.add("typeValidator.setMaxInclusive(max);");
        } else if (_maxExclusive != null) {
            jsc.add("org.exolab.castor.types.DateTime max = "
                    + "new org.exolab.castor.types.DateTime(\"" + _maxExclusive + "\");");
            jsc.add("typeValidator.setMaxExclusive(max);");
        }

// TODO: We can't validate on the fixed value as long as Castor treats DateTime as java.util.Date
// because in the process any time zone information is discarded and comparisons will fail.
//        // fixed values
//        if (fixedValue != null) {
//            jsc.add("typeValidator.setFixed(");
//            jsc.append(fixedValue.replaceFirst(".toDate\\(\\)", ""));
//            jsc.append(");");
//        }

        if (addTryCatch) {
            jsc.unindent();
            jsc.add("} catch (java.text.ParseException pe) {");
            jsc.indent();
            jsc.add("System.out.println(\"ParseException\" + pe);");
            jsc.unindent();
            jsc.add("}");
        }

        // pattern facet
        String pattern = getPattern();
        if (pattern != null) {
            jsc.add("typeValidator.setPattern(\"");
            jsc.append(escapePattern(pattern));
            jsc.append("\");");
        }

        jsc.add(fieldValidatorInstanceName + ".setValidator(typeValidator);");
    }

} //-- XSDateTime
