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
 * Copyright 2001-2002 (C) Intalio, Inc. All Rights Reserved.
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
 * The XML Schema gYearMonth type.
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision $ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public class XSGYearMonth extends XSPatternBase {
    /** Jakarta's common-logging logger. */
    private static final Log LOG = LogFactory.getLog(XSGYearMonth.class);

    /** The JType represented by this XSType. */
    private static final JType JTYPE = new JClass("org.exolab.castor.types.GYearMonth");

    /** Maximum YearMonth (inclusive). */
    private String _maxInclusive;
    /** Maximum YearMonth (exclusive). */
    private String _maxExclusive;
    /** Minimum YearMonth (inclusive). */
    private String _minInclusive;
    /** Minimum YearMonth (exclusive). */
    private String _minExclusive;

    /**
     *  No-Arg constructor.
     */
    public XSGYearMonth() {
       super(XSType.GYEARMONTH_TYPE);
    }

    /**
     * Returns the JType that this XSType represents.
     * @return the JType that this XSType represents.
     */
    public JType getJType() {
        return XSGYearMonth.JTYPE;
    }

    /**
     * Returns the maximum exclusive value that this XSGYearMonth can hold.
     * @return the maximum exclusive value that this XSGYearMonth can hold. If
     * no maximum exclusive value has been set, Null will be returned
     * @see #getMaxInclusive
     */
    public String getMaxExclusive() {
        return _maxExclusive;
    } //-- getMaxExclusive

    /**
     * Returns the maximum inclusive value that this XSGYearMonth can hold.
     * @return the maximum inclusive value that this XSGYearMonth can hold. If
     * no maximum inclusive value has been set, Null will be returned
     * @see #getMaxExclusive
     */
    public String getMaxInclusive() {
        return _maxInclusive;
    } //-- getMaxInclusive

    /**
     * Returns the minimum exclusive value that this XSGYearMonth can hold.
     * @return the minimum exclusive value that this XSGYearMonth can hold. If
     * no minimum exclusive value has been set, Null will be returned
     * @see #getMinInclusive
     * @see #setMaxInclusive
     */
    public String getMinExclusive() {
        return _minExclusive;
    } //-- getMinExclusive

    /**
     * Returns the minimum inclusive value that this XSGYearMonth can hold.
     * @return the minimum inclusive value that this can XSGYearMonth hold. If
     * no minimum inclusive value has been set, Null will be returned
     * @see #getMinExclusive
     */
    public String getMinInclusive() {
        return _minInclusive;
    } //-- getMinInclusive

    /**
     * Sets the maximum exclusive value that this XSGYearMonth can hold.
     * @param max the maximum exclusive value this XSGYearMonth can be
     * @see #setMaxInclusive
    */
    public void setMaxExclusive(final String max) {
        _maxExclusive = max;
        _maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum inclusive value that this XSGYearMonth can hold.
     * @param max the maximum inclusive value this XSGYearMonth can be
     * @see #setMaxExclusive
    */
    public void setMaxInclusive(final String max) {
        _maxInclusive = max;
        _maxExclusive = null;
    } //-- setMaxInclusive

    /**
     * Sets the minimum exclusive value that this XSGYearMonth can hold.
     * @param min the minimum exclusive value this XSGYearMonth can be
     * @see #setMinInclusive
    */
    public void setMinExclusive(final String min) {
        _minExclusive = min;
        _minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum inclusive value that this XSGYearMonth can hold.
     * @param min the minimum inclusive value this XSGYearMonth can be
     * @see #setMinExclusive
    */
    public void setMinInclusive(final String min) {
        _minInclusive = min;
        _minExclusive = null;
    } //-- setMinInclusive

    /**
     * Returns true if a minimum (inclusive or exclusive) has been set.
     * @return true if a minimum (inclusive or exclusive) has been set.
     */
    public boolean hasMinimum() {
        return _minInclusive != null || _minExclusive != null;
    }

    /**
     * Returns true if a maximum (inclusive or exclusive) has been set.
     * @return true if a maximum (inclusive or exclusive) has been set.
     */
    public boolean hasMaximum() {
        return _maxInclusive != null || _maxExclusive != null;
    }

    /**
     * Transfer facets from the provided simpleType to <code>this</code>. The
     * GYearMonth SimpleType supports the following facets:
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
                this.setMaxExclusive(facet.getValue());
            } else if (Facet.MAX_INCLUSIVE.equals(name)) {
                this.setMaxInclusive(facet.getValue());
            } else if (Facet.MIN_EXCLUSIVE.equals(name)) {
                this.setMinExclusive(facet.getValue());
            } else if (Facet.MIN_INCLUSIVE.equals(name)) {
                this.setMinInclusive(facet.getValue());
            } else if (Facet.PATTERN.equals(name)) {
                addPattern(facet.getValue());
            } else if (Facet.WHITESPACE.equals(name)) {
                // If this facet is set correctly, we don't need to do anything
                if (!facet.getValue().equals(Facet.WHITESPACE_COLLAPSE)) {
                    LOG.warn("Warning: The facet 'whitespace' can only be set to '"
                             + Facet.WHITESPACE_COLLAPSE + "' for 'gYearMonth'.");
                }
            }
        } //while
    } //setFacets

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
        jsc.add("org.exolab.castor.xml.validators.DateTimeValidator typeValidator ="
                + " new org.exolab.castor.xml.validators.DateTimeValidator();");

        boolean addTryCatch = _minInclusive != null || _minExclusive != null
                || _maxInclusive != null || _maxExclusive != null || fixedValue != null;

        if (addTryCatch) {
            jsc.add("try {");
            jsc.indent();
        }

        // minInclusive / minExclusive facets (only one or the other, never both)
        if (_minInclusive != null) {
            jsc.add("org.exolab.castor.types.GYearMonth min = "
                    + "org.exolab.castor.types.GYearMonth.parseGYearMonth(\""
                    + _minInclusive + "\");");
            jsc.add("typeValidator.setMinInclusive(min);");
        } else if (_minExclusive != null) {
            jsc.add("org.exolab.castor.types.GYearMonth min = "
                    + "org.exolab.castor.types.GYearMonth.parseGYearMonth(\""
                    + _minExclusive + "\");");
            jsc.add("typeValidator.setMinExclusive(min);");
        }

        // maxInclusive / maxExclusive facets (only one or the other, never both)
        if (_maxInclusive != null) {
            jsc.add("org.exolab.castor.types.GYearMonth max = "
                    + "org.exolab.castor.types.GYearMonth.parseGYearMonth(\""
                    + _maxInclusive + "\");");
            jsc.add("typeValidator.setMaxInclusive(max);");
        } else if (_maxExclusive != null) {
            jsc.add("org.exolab.castor.types.GYearMonth max = "
                    + "org.exolab.castor.types.GYearMonth.parseGYearMonth(\""
                    + _maxExclusive + "\");");
            jsc.add("typeValidator.setMaxExclusive(max);");
        }

        // fixed values
        if (fixedValue != null) {
            jsc.add("typeValidator.setFixed(" + fixedValue + ");");
        }

        if (addTryCatch) {
            jsc.unindent();
            jsc.add("} catch (java.text.ParseException pe) {");
            jsc.indent();
            jsc.add("System.out.println(\"ParseException\" + pe);");
            jsc.unindent();
            jsc.add("}");
        }

        // pattern facet
        codePatternFacet(jsc, "typeValidator");

        jsc.add(fieldValidatorInstanceName + ".setValidator(typeValidator);");
    }

}
