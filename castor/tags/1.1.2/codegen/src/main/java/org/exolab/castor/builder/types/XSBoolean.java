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
 * The xsd:boolean XML Schema datatype.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class XSBoolean extends AbstractWhiteSpaceFacet {
    //--------------------------------------------------------------------------

    /** Name of this XSType. */
    public static final String NAME = "boolean";
    
    /** Type number of this XSType. */
    public static final short TYPE = XSType.BOOLEAN_TYPE;

    //--------------------------------------------------------------------------

    /** The JType represented by this XSType. */
    private final JType _jType;
    
    /** True if this type is implemented using the wrapper class. */
    private final boolean _asWrapper;

    //--------------------------------------------------------------------------
    
    /**
     * No-arg constructor.
     */
    public XSBoolean() {
        this(false);
    }

    /**
     * Constructs a new XSBoolean.
     * 
     * @param asWrapper If true, use the java.lang wrapper class.
     */
    public XSBoolean(final boolean asWrapper) {
        super();
        
        _asWrapper = asWrapper;
        if (_asWrapper) {
            _jType = new JClass("java.lang.Boolean");
        } else {
            _jType = JType.BOOLEAN;
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
        return "new java.lang.Boolean(false);";
    }
    
    /**
     * {@inheritDoc}
     */
    public String createToJavaObjectCode(final String variableName) {
        if (_asWrapper) { return variableName; }
        return "(" + variableName + " ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE)";
    }

    /**
     * {@inheritDoc}
     */
    public String createFromJavaObjectCode(final String variableName) {
        if (_asWrapper) { return "((java.lang.Boolean) " + variableName + ")"; }
        return "((java.lang.Boolean) " + variableName + ").booleanValue()";
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void validationCode(final JSourceCode jsc,
            final String fixedValue, final String validatorInstanceName) {
        jsc.add("org.exolab.castor.xml.validators.BooleanValidator typeValidator;\n"
              + "typeValidator = new org.exolab.castor.xml.validators.BooleanValidator();\n"
              + "{0}.setValidator(typeValidator);", validatorInstanceName);

        if (fixedValue != null) {
            jsc.add("typeValidator.setFixed(" + fixedValue + ");");
        }

        codePatternFacet(jsc, "typeValidator");
        codeWhiteSpaceFacet(jsc, "typeValidator");
    }

    //--------------------------------------------------------------------------
}
