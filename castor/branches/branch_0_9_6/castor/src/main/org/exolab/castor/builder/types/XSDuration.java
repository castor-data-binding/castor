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
 * Copyright 2001-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 * Date         Author              Changes
 * 05/22/2001   Arnaud Blandin      Created
 */

package org.exolab.castor.builder.types;

import org.exolab.castor.types.Duration;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;

import org.exolab.javasource.JType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JSourceCode;

import java.text.ParseException;
import java.util.Enumeration;

public final class XSDuration extends XSType {

	private static final JType JTYPE =
		new JClass("org.exolab.castor.types.Duration");

	private Duration _maxInclusive;
	private Duration _maxExclusive;
	private Duration _minInclusive;
	private Duration _minExclusive;

	public XSDuration() {
		super(XSType.DURATION_TYPE);
	}

	public JType getJType() {
		return this.JTYPE;
	}
	/**
	* Returns the maximum exclusive value that this XSDuration can hold.
	* @return the maximum exclusive value that this XSDuration can hold. If
	* no maximum exclusive value has been set, Null will be returned
	* @see #getMaxInclusive
	**/
	public Duration getMaxExclusive() {
		return _maxExclusive;
	} //-- getMaxExclusive

	/**
	 * Returns the maximum inclusive value that this XSDuration can hold.
	 * @return the maximum inclusive value that this XSDuration can hold. If
	 * no maximum inclusive value has been set, Null will be returned
	 * @see #getMaxExclusive
	**/
	public Duration getMaxInclusive() {
		return _maxInclusive;
	} //-- getMaxInclusive

	/**
	 * Returns the minimum exclusive value that this XSDuration can hold.
	 * @return the minimum exclusive value that this XSDuration can hold. If
	 * no minimum exclusive value has been set, Null will be returned
	 * @see #getMinInclusive
	 * @see #setMaxInclusive
	**/
	public Duration getMinExclusive() {
		return _minExclusive;
	} //-- getMinExclusive

	/**
	 * Returns the minimum inclusive value that this XSDuration can hold.
	 * @return the minimum inclusive value that this can XSDuration hold. If
	 * no minimum inclusive value has been set, Null will be returned
	 * @see #getMinExclusive
	**/
	public Duration getMinInclusive() {
		return _minInclusive;
	} //-- getMinInclusive

	/**
	 * Sets the maximum exclusive value that this XSDuration can hold.
	 * @param max the maximum exclusive value this XSDuration can be
	 * @see #setMaxInclusive
	**/
	public void setMaxExclusive(Duration max) {
		_maxExclusive = max;
		_maxInclusive = null;
	} //-- setMaxExclusive

	/**
	 * Sets the maximum inclusive value that this XSDuration can hold.
	 * @param max the maximum inclusive value this XSDuration can be
	 * @see #setMaxExclusive
	**/
	public void setMaxInclusive(Duration max) {
		_maxInclusive = max;
		_maxExclusive = null;
	} //-- setMaxInclusive

	/**
	 * Sets the minimum exclusive value that this XSDuration can hold.
	 * @param max the minimum exclusive value this XSDuration can be
	 * @see #setMinInclusive
	**/
	public void setMinExclusive(Duration min) {
		_minExclusive = min;
		_minInclusive = null;
	} //-- setMinExclusive

	/**
	 * Sets the minimum inclusive value that this XSInt can hold.
	 * @param max the minimum inclusive value this XSInt can be
	 * @see #setMinExclusive
	**/
	public void setMinInclusive(Duration min) {
		_minInclusive = min;
		_minExclusive = null;
	} //-- setMinInclusive

	public boolean hasMinimum() {
		return ((_minInclusive != null) || (_minExclusive != null));
	}

	public boolean hasMaximum() {
		return ((_maxInclusive != null) || (_maxExclusive != null));
	}

	/**
	 * Reads and sets the facets for XSDuration
	 * override the readFacet method of XSType
	 * @param simpletype the Simpletype containing the facets
	 * @param xsType the XSType to set the facets of
	 * @see org.exolab.castor.builder.types.XSType#getFacets
	 */
	public void setFacets(SimpleType simpleType) {
		//-- copy valid facets
		Enumeration enumeration = getFacets(simpleType);
		while (enumeration.hasMoreElements()) {

			Facet facet = (Facet) enumeration.nextElement();
			String name = facet.getName();

			try {
				//-- maxExclusive
				if (Facet.MAX_EXCLUSIVE.equals(name))
					this.setMaxExclusive(
						Duration.parseDuration(facet.getValue()));
				//-- maxInclusive
				else if (Facet.MAX_INCLUSIVE.equals(name))
					this.setMaxInclusive(
						Duration.parseDuration(facet.getValue()));
				//-- minExclusive
				else if (Facet.MIN_EXCLUSIVE.equals(name))
					this.setMinExclusive(
						Duration.parseDuration(facet.getValue()));
				//-- minInclusive
				else if (Facet.MIN_INCLUSIVE.equals(name))
					this.setMinInclusive(
						Duration.parseDuration(facet.getValue()));
				//-- pattern
				else if (Facet.PATTERN.equals(name)) {
					//do nothing for the moment
					System.out.println(
						"Warning: The facet 'pattern' is not currently supported for Duration.");
				}
			} catch (ParseException e) {
				//not possible to set the facet properly
				//This can't happen since a ParseException would have been set
				//during the unmarshalling of the facets
				e.printStackTrace();
				return;
			}
		} //while

	} //setFacets

	/**
	 * Creates the validation code for an instance of this XSType. The validation
     * code should if necessary create a newly configured TypeValidator, that
     * should then be added to a FieldValidator instance whose name is provided.
	 * 
	 * @param fixedValue a fixed value to use if any
	 * @param jsc the JSourceCode to fill in.
     * @param fieldValidatorInstanceName the name of the FieldValidator
     * that the configured TypeValidator should be added to.
	 */
	public void validationCode (JSourceCode jsc, String fixedValue, String fieldValidatorInstanceName) {

		if (jsc == null)
			jsc = new JSourceCode();
		
		jsc.add("DurationValidator typeValidator = new DurationValidator();");
		if (hasMinimum()) {
			Duration min = getMinExclusive();
			if (min != null)
				jsc.add("typeValidator.setMinExclusive(");
			else {
				min = getMinInclusive();
				jsc.add("typeValidator.setMinInclusive(");
			}
			/* it is better for a good understanding to use
			the parse method with 'min.toSring()' but in that case
			we have to deal with the ParseException*/
			jsc.append(
				"new org.exolab.castor.types.Duration(" + min.toLong() + "L)");
			jsc.append(");");
		}
		if (hasMaximum()) {
			Duration max = getMaxExclusive();
			if (max != null)
				jsc.add("typeValidator.setMaxExclusive(");
			else {
				max = getMaxInclusive();
				jsc.add("typeValidator.setMaxInclusive(");
			}
			/* it is better for a good understanding to use
			the parse method with 'min.toSring()' but in that case
			we have to deal with the ParseException*/
			jsc.append(
				"new org.exolab.castor.types.Duration(" + max.toLong() + "L)");
			jsc.append(");");
		}
		//-- pattern facet
		
		jsc.add(fieldValidatorInstanceName+".setValidator(typeValidator);");
        
	}

} //--XSDuration
