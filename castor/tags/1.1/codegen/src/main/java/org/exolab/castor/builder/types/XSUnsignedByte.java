/*
 * Copyright 2005-2007 Werner Guttmann, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.builder.types;

import org.exolab.javasource.JClass;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * The xsd:unsignedByte XML Schema type.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public final class XSUnsignedByte extends AbstractDigitsFacet {
    //--------------------------------------------------------------------------

    /** Name of this XSType. */
    public static final String NAME = "unsignedByte";
    
    /** Type number of this XSType. */
    public static final short TYPE = XSType.UNSIGNED_BYTE_TYPE;

    /** A constant holding the minimum value an xsd:unsignedByte can have, 0. */
    public static final String MIN_VALUE = "0";
    
    /** A constant holding the maximum value an xsd:unsignedByte can have, 255. */
    public static final String MAX_VALUE = "255";

    //--------------------------------------------------------------------------

    /** True if this type is implemented using the wrapper class. */
    private final boolean _asWrapper;

    /** The JType represented by this XSType. */
    private final JType _jType;

    //--------------------------------------------------------------------------

    /**
     * No-arg constructor.
     */
    public XSUnsignedByte() {
        this(false);
    }

    /**
     * Constructs a new XSUnsignedByte.
     * @param asWrapper if true, use the java.lang wrapper class.
     */
    public XSUnsignedByte(final boolean asWrapper) {
        super();
        
        _asWrapper = asWrapper;
        if (asWrapper) {
            _jType = new JClass("java.lang.Short");
        } else {
            _jType = JType.SHORT;
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
    public boolean isPrimitive() { return false; }
    
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
        return "new java.lang.Short((short) 0);";
    }
    
    /**
     * {@inheritDoc}
     */
    public String createToJavaObjectCode(final String variableName) {
        if (_asWrapper) { return variableName; }
        return "new java.lang.Short(" + variableName + ")";
    }

    /**
     * {@inheritDoc}
     */
    public String createFromJavaObjectCode(final String variableName) {
        if (_asWrapper) { return "((java.lang.Short) " + variableName + ")"; }
        return "((java.lang.Short) " + variableName + ").shortValue()";
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void validationCode(final JSourceCode jsc,
            final String fixedValue, final String validatorInstanceName) {
        jsc.add("org.exolab.castor.xml.validators.ShortValidator typeValidator;\n"
              + "typeValidator = new org.exolab.castor.xml.validators.ShortValidator();\n"
              + "{0}.setValidator(typeValidator);", validatorInstanceName);

        if (fixedValue != null) {
            jsc.add("typeValidator.setFixed((short) " + fixedValue + ");");
        }

        codePatternFacet(jsc, "typeValidator");
        codeWhiteSpaceFacet(jsc, "typeValidator");

        if (getMinExclusive() != null) {
            jsc.add("typeValidator.setMinExclusive((short) " + getMinExclusive() + ");");
        } else if (getMinInclusive() != null) {
            jsc.add("typeValidator.setMinInclusive((short) " + getMinInclusive() + ");");
        }

        if (getMaxExclusive() != null) {
            jsc.add("typeValidator.setMaxExclusive((short) " + getMaxExclusive() + ");");
        } else if (getMaxInclusive() != null) {
            jsc.add("typeValidator.setMaxInclusive((short) " + getMaxInclusive() + ");");
        }

        codeDigitsFacet(jsc, "typeValidator");
    }

    //--------------------------------------------------------------------------
}
