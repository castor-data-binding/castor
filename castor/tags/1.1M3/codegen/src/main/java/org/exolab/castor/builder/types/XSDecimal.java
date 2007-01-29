/*
 * Copyright 2007 Andrew Fawcett, Ralf Joachim
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
 * The xsd:decimal XML Schema datatype.
 * 
 * @author <a href="mailto:andrew DOT fawcett AT coda DOT com">Andrew Fawcett</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public final class XSDecimal extends AbstractDigitsFacet {
    //--------------------------------------------------------------------------

    /** Name of this XSType. */
    public static final String NAME = "decimal";
    
    /** Type number of this XSType. */
    public static final short TYPE = XSType.DECIMAL_TYPE;

    /** The JType represented by this XSType. */
    private static final JType JTYPE = new JClass("java.math.BigDecimal");

    //--------------------------------------------------------------------------

    /**
     * No-arg constructor.
     */
    public XSDecimal() {
        super(false);
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
    public JType getJType() { return JTYPE; }

    /**
     * {@inheritDoc}
     */
    public String newInstanceCode() {
        return "new java.math.BigDecimal(0);";
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
        return "(java.math.BigDecimal) " + variableName;
    }
    
    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void validationCode(final JSourceCode jsc,
            final String fixedValue, final String validatorInstanceName) {
        jsc.add("org.exolab.castor.xml.validators.DecimalValidator typeValidator;\n"
              + "typeValidator = new org.exolab.castor.xml.validators.DecimalValidator();\n"
              + "{0}.setValidator(typeValidator);", validatorInstanceName);

        if (fixedValue != null) {
            jsc.add("typeValidator.setFixed(" + fixedValue + ");");
        }

        codePatternFacet(jsc, "typeValidator");
        codeWhiteSpaceFacet(jsc, "typeValidator");

        if (getMinExclusive() != null) {
            jsc.add("java.math.BigDecimal min = new java.math.BigDecimal(\"{0}\");\n"
                  + "typeValidator.setMinExclusive(min);", getMinExclusive());
        } else if (getMinInclusive() != null) {
            jsc.add("java.math.BigDecimal min = new java.math.BigDecimal(\"{0}\");\n"
                  + "typeValidator.setMinInclusive(min);", getMinInclusive());
        }

        if (getMaxExclusive() != null) {
            jsc.add("java.math.BigDecimal max = new java.math.BigDecimal(\"{0}\");\n"
                  + "typeValidator.setMaxExclusive(max);", getMaxExclusive());
        } else if (getMaxInclusive() != null) {
            jsc.add("java.math.BigDecimal max = new java.math.BigDecimal(\"{0}\");\n"
                  + "typeValidator.setMaxInclusive(max);", getMaxInclusive());
        }

        codeDigitsFacet(jsc, "typeValidator");
    }
    
    //--------------------------------------------------------------------------
}
