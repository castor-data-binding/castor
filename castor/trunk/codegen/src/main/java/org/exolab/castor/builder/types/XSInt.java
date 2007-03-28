/*
 * Copyright 2007 Keith Visco, Ralf Joachim
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
 * The xsd:int XML Schema type.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class XSInt extends AbstractDigitsFacet {
    //--------------------------------------------------------------------------

    /** Name of this XSType. */
    public static final String NAME = "int";
    
    /** Type number of this XSType. */
    public static final short TYPE = XSType.INT_TYPE;
    
    /** A constant holding the minimum value an xsd:int can have, -2<sup>31</sup>. */
    public static final String MIN_VALUE = Integer.toString(Integer.MIN_VALUE);
    
    /** A constant holding the maximum value an xsd:int can have, 2<sup>31</sup>-1. */
    public static final String MAX_VALUE = Integer.toString(Integer.MAX_VALUE);

    //--------------------------------------------------------------------------

    /** True if this type is implemented using the wrapper class. */
    private final boolean _asWrapper;
    
    /** The JType represented by this XSType. */
    private final JType _jType;
    
    //--------------------------------------------------------------------------

    /**
     * No-arg constructor.
     */
    public XSInt() {
        this(false);
    }

    /**
     * Constructs a new XSInt.
     * 
     * @param asWrapper If true, use the java.lang wrapper class.
     */
    public XSInt(final boolean asWrapper) {
        super();
        
        _asWrapper = asWrapper;
        if (_asWrapper) {
            _jType = new JClass("java.lang.Integer");
        } else {
            _jType = JType.INT;
        }
        
        setMinInclusive(MIN_VALUE);
        setMaxInclusive(MAX_VALUE);
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
        return "new java.lang.Integer(0);";
    }
    
    /**
     * {@inheritDoc}
     */
    public String createToJavaObjectCode(final String variableName) {
        if (_asWrapper) { return variableName; }
        return "new java.lang.Integer(" + variableName + ")";
    }

    /**
     * {@inheritDoc}
     */
    public String createFromJavaObjectCode(final String variableName) {
        if (_asWrapper) { return "((java.lang.Integer) " + variableName + ")"; }
        return "((java.lang.Integer) " + variableName + ").intValue()";
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void validationCode(final JSourceCode jsc,
            final String fixedValue, final String validatorInstanceName) {
        jsc.add("org.exolab.castor.xml.validators.IntValidator typeValidator;\n"
              + "typeValidator = new org.exolab.castor.xml.validators.IntValidator();\n"
              + "{0}.setValidator(typeValidator);", validatorInstanceName);

        if (fixedValue != null) {
            jsc.add("typeValidator.setFixed(" + fixedValue + ");");
        }

        codePatternFacet(jsc, "typeValidator");
        codeWhiteSpaceFacet(jsc, "typeValidator");

        if (getMinExclusive() != null) {
            jsc.add("typeValidator.setMinExclusive(" + getMinExclusive() + ");");
        } else if (getMinInclusive() != null) {
            jsc.add("typeValidator.setMinInclusive(" + getMinInclusive() + ");");
        }

        if (getMaxExclusive() != null) {
            jsc.add("typeValidator.setMaxExclusive(" + getMaxExclusive() + ");");
        } else if (getMaxInclusive() != null) {
            jsc.add("typeValidator.setMaxInclusive(" + getMaxInclusive() + ");");
        }

        codeDigitsFacet(jsc, "typeValidator");
    }

    //--------------------------------------------------------------------------
}
