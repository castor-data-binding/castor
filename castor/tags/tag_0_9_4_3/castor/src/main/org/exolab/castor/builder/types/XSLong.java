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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.builder.types;

import org.exolab.castor.xml.FieldValidator;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.builder.SourceGenerator;
import org.exolab.javasource.*;

import java.util.Enumeration;
/**
 * The XML Schema long type
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class XSLong extends XSPatternBase {

	//- Constraints for long type
	Long maxInclusive = null;
	Long maxExclusive = null;
	Long minInclusive = null;
	Long minExclusive = null;

	/**
	 * The JType represented by this XSType
	**/
	private static JType jType = JType.Long;
	private boolean _asWrapper = false;

	public XSLong() {
		this(SourceGenerator.usePrimitiveWrapper());
	}

	public XSLong(boolean asWrapper) {
		super(XSType.LONG_TYPE);
		_asWrapper = asWrapper;
		if (_asWrapper)
			this.jType = new JClass("java.lang.Long");
		else
			this.jType = JType.Long;
	} //-- XSLong

	/**
	 * Returns the JType that this XSType represents
	 * @return the JType that this XSType represents
	**/
	public JType getJType() {
		return this.jType;
	}

	/**
	 * Returns the maximum exclusive value that this XSLong can hold.
	 * @return the maximum exclusive value that this XSLong can hold. If
	 * no maximum exclusive value has been set, Null will be returned
	 * @see #getMaxInclusive
	**/
	public Long getMaxExclusive() {
		return maxExclusive;
	} //-- getMaxExclusive

	/**
	 * Returns the maximum inclusive value that this XSLong can hold.
	 * @return the maximum inclusive value that this XSLong can hold. If
	 * no maximum inclusive value has been set, Null will be returned
	 * @see #getMaxExclusive
	**/
	public Long getMaxInclusive() {
		return maxInclusive;
	} //-- getMaxInclusive

	/**
	 * Returns the minimum exclusive value that this XSLong can hold.
	 * @return the minimum exclusive value that this XSLong can hold. If
	 * no minimum exclusive value has been set, Null will be returned
	 * @see #getMinInclusive
	 * @see #setMaxInclusive
	**/
	public Long getMinExclusive() {
		return minExclusive;
	} //-- getMinExclusive

	/**
	 * Returns the minimum inclusive value that this XSLong can hold.
	 * @return the minimum inclusive value that this XSLong can hold. If
	 * no minimum inclusive value has been set, Null will be returned
	 * @see #getMinExclusive
	**/
	public Long getMinInclusive() {
		return minInclusive;
	} //-- getMinInclusive

	public boolean hasMaximum() {
		return ((maxInclusive != null) || (maxExclusive != null));
	} //-- hasMaximum

	public boolean hasMinimum() {
		return ((minInclusive != null) || (minExclusive != null));
	} //-- hasMinimum

	//public String toString() {
	//    return value.toString();
	//}

	/**
	 * Sets the maximum exclusive value that this XSLong can hold.
	 * @param max the maximum exclusive value this XSLong can be
	 * @see #setMaxInclusive
	**/
	public void setMaxExclusive(long max) {
		maxExclusive = new Long(max);
		maxInclusive = null;
	} //-- setMaxExclusive

	/**
	 * Sets the maximum exclusive value that this XSLong can hold.
	 * @param max the maximum exclusive value this XSLong can be
	 * @see #setMaxInclusive
	**/
	public void setMaxExclusive(Long max) {
		maxExclusive = max;
		maxInclusive = null;
	} //-- setMaxExclusive

	/**
	 * Sets the maximum inclusive value that this XSLong can hold.
	 * @param max the maximum inclusive value this XSLong can be
	 * @see #setMaxExclusive
	**/
	public void setMaxInclusive(long max) {
		maxInclusive = new Long(max);
		maxExclusive = null;
	} //-- setMaxInclusive

	/**
	 * Sets the maximum inclusive value that this XSLong can hold.
	 * @param max the maximum inclusive value this XSLong can be
	 * @see #setMaxExclusive
	**/
	public void setMaxInclusive(Long max) {
		maxInclusive = max;
		maxExclusive = null;
	} //-- setMaxInclusive

	/**
	 * Sets the minimum exclusive value that this XSLong can hold.
	 * @param max the minimum exclusive value this XSLong can be
	 * @see #setMinInclusive
	**/
	public void setMinExclusive(long min) {
		minExclusive = new Long(min);
		minInclusive = null;
	} //-- setMinExclusive

	/**
	 * Sets the minimum exclusive value that this XSLong can hold.
	 * @param max the minimum exclusive value this XSLong can be
	 * @see #setMinInclusive
	**/
	public void setMinExclusive(Long min) {
		minExclusive = min;
		minInclusive = null;
	} //-- setMinExclusive

	/**
	 * Sets the minimum inclusive value that this XSLong can hold.
	 * @param max the minimum inclusive value this XSLong can be
	 * @see #setMinExclusive
	**/
	public void setMinInclusive(long min) {
		minInclusive = new Long(min);
		minExclusive = null;
	} //-- setMinInclusive

	/**
	 * Sets the minimum inclusive value that this XSLong can hold.
	 * @param max the minimum inclusive value this XSLong can be
	 * @see #setMinExclusive
	**/
	public void setMinInclusive(Long min) {
		minInclusive = min;
		minExclusive = null;
	} //-- setMinInclusive

	/**
	 * Reads and sets the facets for XSTimeDuration
	 * override the readFacet method of XSType
	 * @param simpletype the Simpletype containing the facets
	 * @param xsType the XSType to set the facets of
	 * @see org.exolab.castor.builder.types.XSType#getFacets
	 */
	public void setFacets(SimpleType simpleType) {

		//-- copy valid facets
		Enumeration enum = getFacets(simpleType);
		while (enum.hasMoreElements()) {

			Facet facet = (Facet) enum.nextElement();
			String name = facet.getName();

			//-- maxExclusive
			if (Facet.MAX_EXCLUSIVE.equals(name))
				setMaxExclusive(facet.toLong());
			//-- maxInclusive
			else if (Facet.MAX_INCLUSIVE.equals(name))
				setMaxInclusive(facet.toLong());
			//-- minExclusive
			else if (Facet.MIN_EXCLUSIVE.equals(name))
				setMinExclusive(facet.toLong());
			//-- minInclusive
			else if (Facet.MIN_INCLUSIVE.equals(name))
				setMinInclusive(facet.toLong());
			//-- pattern
			else if (Facet.PATTERN.equals(name))
				setPattern(facet.getValue());
		} //setFacets

	} //-- readLongFacets
	/**
	 * Returns the String necessary to convert an instance of this XSType
	 * to an Object. This method is really only useful for primitive types
	 * @param variableName the name of the instance variable
	 * @return the String necessary to convert an instance of this XSType
	 * to an Object
	**/
	public String createToJavaObjectCode(String variableName) {
		if (SourceGenerator.usePrimitiveWrapper())
			return super.createToJavaObjectCode(variableName);
		else {
			StringBuffer sb = new StringBuffer("new Long(");
			sb.append(variableName);
			sb.append(")");
			return sb.toString();
		}
	} //-- toJavaObject

	/**
	 * Returns the String necessary to convert an Object to
	 * an instance of this XSType. This method is really only useful
	 * for primitive types
	 * @param variableName the name of the Object
	 * @return the String necessary to convert an Object to an
	 * instance of this XSType
	**/
	public String createFromJavaObjectCode(String variableName) {
		StringBuffer sb = new StringBuffer("((Long)");
		sb.append(variableName);
		sb.append(")");
		if (!_asWrapper) {
			sb.append(".longValue()");
		}
		return sb.toString();
	} //-- fromJavaObject

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
		
		jsc.add("LongValidator typeValidator = new LongValidator();");
		if (hasMinimum()) {
			Long min = getMinExclusive();
			if (min != null)
				jsc.add("typeValidator .setMinExclusive(");
			else {
				min = getMinInclusive();
				jsc.add("typeValidator .setMinInclusive(");
			}
			jsc.append(min.toString());
			jsc.append("L);");
		}
		if (hasMaximum()) {
			Long max = getMaxExclusive();
			if (max != null)
				jsc.add("typeValidator .setMaxExclusive(");
			else {
				max = getMaxInclusive();
				jsc.add("typeValidator .setMaxInclusive(");
			}
			jsc.append(max.toString());
			jsc.append("L);");
		}

		//-- fixed values
		if (fixedValue != null) {
			//-- make sure we have a valid value...
			Long.parseLong(fixedValue);

			jsc.add("typeValidator .setFixed(");
			jsc.append(fixedValue);
			jsc.append(");");
		}
		//-- pattern facet
		String pattern = getPattern();
		if (pattern != null) {
			jsc.add("typeValidator .setPattern(\"");
			jsc.append(escapePattern(pattern));
			jsc.append("\");");
		}
		jsc.add(fieldValidatorInstanceName+".setValidator(typeValidator);");
		
	}

} //-- XSLong
