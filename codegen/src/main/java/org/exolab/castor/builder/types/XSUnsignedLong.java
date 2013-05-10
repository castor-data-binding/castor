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
 * The xsd:unsignedLong XML Schema type.
 * 
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 6317 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class XSUnsignedLong extends AbstractDigitsFacet {
    //--------------------------------------------------------------------------

    /** Name of this XSType. */
    public static final String NAME = "unsignedLong";
    
    /** Type number of this XSType. */
    public static final short TYPE = XSType.UNSIGNED_LONG_TYPE;
    
    /** The JType represented by this XSType. */
    private static final JType JTYPE = new JClass("java.math.BigInteger");

    /** A constant holding the minimum value an xsd:long can have, 0. */
    public static final String MIN_VALUE = "0";
    
    /** A constant holding the maximum value an xsd:long can have, 2<sup>64</sup>-1. */
    public static final String MAX_VALUE = "18446744073709551615";

    //--------------------------------------------------------------------------
    
    /**
     * No-arg constructor.
     */
    public XSUnsignedLong() {
        super();
        
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
    public JType getJType() { return JTYPE; }
    
    /**
     * {@inheritDoc}
     */
    public String newInstanceCode() {
        return "new java.math.BigInteger(\"0\");";
    }

    /**
     * {@inheritDoc}
     */
    public String createToJavaObjectCode(final String variableName) {
        return variableName;
    }
    
    /**
     * {@inheritDoc}
     */
    public String createFromJavaObjectCode(final String variableName) {
        return "((java.math.BigInteger) " + variableName + ")";
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void validationCode(final JSourceCode jsc,
            final String fixedValue, final String validatorInstanceName) {
        jsc.add("org.exolab.castor.xml.validators.BigIntegerValidator typeValidator;\n"
              + "typeValidator = new org.exolab.castor.xml.validators.BigIntegerValidator();\n"
              + "{0}.setValidator(typeValidator);", validatorInstanceName);

        if (fixedValue != null) {
            jsc.add("typeValidator.setFixed(new BigInteger(\"" + fixedValue + "\");");
        }

        codePatternFacet(jsc, "typeValidator");
        codeWhiteSpaceFacet(jsc, "typeValidator");

        if (getMinExclusive() != null) {
            jsc.add("java.math.BigInteger min = new java.math.BigInteger(\"{0}\");\n"
                  + "typeValidator.setMinExclusive(min);", getMinExclusive());
        } else if (getMinInclusive() != null) {
            jsc.add("java.math.BigInteger min = new java.math.BigInteger(\"{0}\");\n"
                  + "typeValidator.setMinInclusive(min);", getMinInclusive());
        }

        if (getMaxExclusive() != null) {
            jsc.add("java.math.BigInteger max = new java.math.BigInteger(\"{0}\");\n"
                  + "typeValidator.setMaxExclusive(max);", getMaxExclusive());
        } else if (getMaxInclusive() != null) {
            jsc.add("java.math.BigInteger max = new java.math.BigInteger(\"{0}\");\n"
                  + "typeValidator.setMaxInclusive(max);", getMaxInclusive());
        }

        codeDigitsFacet(jsc, "typeValidator");
    }

    //--------------------------------------------------------------------------
}
