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

import org.exolab.castor.xml.schema.Facet;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * The xsd:normalizedString XML Schema type. It is simply a XSString with some
 * specific validation.
 * 
 * @author <a href="mailto:blandin AT intalio DOT com">Arnaud Blandin</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-03-16 10:34:22 -0700 (Thu, 16 Mar 2006) $
 */
public final class XSNormalizedString extends AbstractLengthFacet {
    //--------------------------------------------------------------------------

    /** Name of this XSType. */
    public static final String NAME = "normalizedString";
    
    /** Type number of this XSType. */
    public static final short TYPE = XSType.NORMALIZEDSTRING_TYPE;

    /** The JType represented by this XSType. */
    private static final JType JTYPE = new JClass("java.lang.String");

    //--------------------------------------------------------------------------

    /**
     * No-arg constructor.
     */
    public XSNormalizedString() {
        super(false);
        
        setWhiteSpace(Facet.WHITESPACE_PRESERVE);
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
        return "new java.lang.String();";
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
        return "(java.lang.String) " + variableName;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void validationCode(final JSourceCode jsc,
            final String fixedValue, final String validatorInstanceName) {
        jsc.add("org.exolab.castor.xml.validators.NameValidator typeValidator;\n"
              + "typeValidator = new org.exolab.castor.xml.validators.NameValidator(\n"
              + " org.exolab.castor.xml.XMLConstants.NAME_TYPE_CDATA);\n"
              + "{0}.setValidator(typeValidator);", validatorInstanceName);

        if (fixedValue != null) {
            jsc.add("typeValidator.setFixed(" + fixedValue + ");");
        }

        codePatternFacet(jsc, "typeValidator");
        codeWhiteSpaceFacet(jsc, "typeValidator");
        codeLengthFacet(jsc, "typeValidator");
    }

    //--------------------------------------------------------------------------
}
