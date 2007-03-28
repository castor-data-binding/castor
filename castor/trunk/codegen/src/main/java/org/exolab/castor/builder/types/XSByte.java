/*
 * Copyright 2007 Arnaud Blandin, Ralf Joachim
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.exolab.castor.builder.types;

import org.exolab.javasource.JClass;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * The xsd:byte XML Schema type.
 * 
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class XSByte extends AbstractDigitsFacet {
    //--------------------------------------------------------------------------

    /** Name of this XSType. */
    public static final String NAME = "byte";
    
    /** Type number of this XSType. */
    public static final short TYPE = XSType.BYTE_TYPE;

    /** A constant holding the minimum value an xsd:byte can have, -2<sup>7</sup>. */
    public static final String MIN_VALUE = Byte.toString(Byte.MIN_VALUE);
    
    /** A constant holding the maximum value an xsd:byte can have, 2<sup>7</sup>-1. */
    public static final String MAX_VALUE = Byte.toString(Byte.MAX_VALUE);

    //--------------------------------------------------------------------------

    /** True if this type is implemented using the wrapper class. */
    private final boolean _asWrapper;

    /** The JType represented by this XSType. */
    private final JType _jType;

    //--------------------------------------------------------------------------

    /**
     * No-arg constructor.
     */
    public XSByte() {
         this(false);
    }

    /**
     * Constructs a new XSByte.
     * 
     * @param asWrapper If true, use the java.lang wrapper class.
     */
    public XSByte(final boolean asWrapper) {
        super();
        
        _asWrapper = asWrapper;
        if (_asWrapper) {
            _jType = new JClass("java.lang.Byte");
        } else {
            _jType = JType.BYTE;
        }
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String getName() { return NAME; }

    /**
     * {@inheritDoc}
     */
    public short getType() { return TYPE; }

    /**
     * {@inheritDoc}
     */
    public boolean isPrimitive() { return true; }
    
    /**
     * {@inheritDoc}
     */
    public boolean isDateTime() { return false; }
    
    /**
     * {@inheritDoc}
     */
    public JType getJType() { return _jType; }

    /**
     * {@inheritDoc}
     */
    public String newInstanceCode() {
        return "new java.lang.Byte((byte) 0);";
    }
    
    /**
     * {@inheritDoc}
     */
    public String createToJavaObjectCode(final String variableName) {
        if (_asWrapper) { return variableName; }
        return "new java.lang.Byte(" + variableName + ")";
    }

    /**
     * {@inheritDoc}
     */
    public String createFromJavaObjectCode(final String variableName) {
        if (_asWrapper) { return "((java.lang.Byte) " + variableName + ")"; }
        return "((java.lang.Byte) " + variableName + ").byteValue()";
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void validationCode(final JSourceCode jsc,
            final String fixedValue, final String validatorInstanceName) {
        jsc.add("org.exolab.castor.xml.validators.ByteValidator typeValidator;\n"
              + "typeValidator = new org.exolab.castor.xml.validators.ByteValidator();\n"
              + "{0}.setValidator(typeValidator);", validatorInstanceName);

        if (fixedValue != null) {
            jsc.add("typeValidator.setFixed((byte) " + fixedValue + ");");
        }

        codePatternFacet(jsc, "typeValidator");
        codeWhiteSpaceFacet(jsc, "typeValidator");

        if (getMinExclusive() != null) {
            jsc.add("typeValidator.setMinExclusive((byte) " + getMinExclusive() + ");");
        } else if (getMinInclusive() != null) {
            jsc.add("typeValidator.setMinInclusive((byte) " + getMinInclusive() + ");");
        }

        if (getMaxExclusive() != null) {
            jsc.add("typeValidator.setMaxExclusive((byte) " + getMaxExclusive() + ");");
        } else if (getMaxInclusive() != null) {
            jsc.add("typeValidator.setMaxInclusive((byte) " + getMaxInclusive() + ");");
        }

        codeDigitsFacet(jsc, "typeValidator");
    }

    //--------------------------------------------------------------------------
}
