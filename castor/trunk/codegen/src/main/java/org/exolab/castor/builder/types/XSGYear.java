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
 * The xsd:gYear XML Schema type.
 * 
 * @author <a href="mailto:blandin AT intalio DOT com">Arnaud Blandin</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision $ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public final class XSGYear extends AbstractRangeFacet {
    //--------------------------------------------------------------------------

    /** Name of this XSType. */
    public static final String NAME = "gYear";
    
    /** Type number of this XSType. */
    public static final short TYPE = XSType.GYEAR_TYPE;

    /** The JType represented by this XSType. */
    private static final JType JTYPE = new JClass ("org.exolab.castor.types.GYear");

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
    public boolean isDateTime() { return true; }
    
    /**
     * {@inheritDoc}
     */
    public JType getJType() { return JTYPE; }

    /**
     * {@inheritDoc}
     */
    public String newInstanceCode() {
        return "new " + getJType().getName() + "();";
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
        return "(" + getJType().getName() + ") " + variableName;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void validationCode(final JSourceCode jsc,
            final String fixedValue, final String validatorInstanceName) {
        jsc.add("org.exolab.castor.xml.validators.DateTimeValidator typeValidator;\n"
              + "typeValidator = new org.exolab.castor.xml.validators.DateTimeValidator();\n"
              + "{0}.setValidator(typeValidator);", validatorInstanceName);

        if (fixedValue != null) {
            jsc.add("try {\n"
                  + " typeValidator.setFixed({0});\n"
                  + "} catch (java.text.ParseException pe) {\n"
                  + " System.out.println(\"ParseException\" + pe);\n"
                  + "}", fixedValue);
        }

        codePatternFacet(jsc, "typeValidator");
        codeWhiteSpaceFacet(jsc, "typeValidator");

        if (hasMinimum() || hasMaximum()) {
            jsc.add("try {");

            // minInclusive / minExclusive facets (only one or the other, never both)
            if (getMinInclusive() != null) {
                jsc.add(" org.exolab.castor.types.GYear min;\n"
                      + " min = org.exolab.castor.types.GYear.parseGYear(\"{0}\");\n"
                      + " typeValidator.setMinInclusive(min);", getMinInclusive());
            } else if (getMinExclusive() != null) {
                jsc.add(" org.exolab.castor.types.GYear min;\n"
                      + " min = org.exolab.castor.types.GYear.parseGYear(\"{0}\");\n"
                      + " typeValidator.setMinExclusive(min);", getMinExclusive());
            }

            // maxInclusive / maxExclusive facets (only one or the other, never both)
            if (getMaxInclusive() != null) {
                jsc.add(" org.exolab.castor.types.GYear max;\n"
                      + " max = org.exolab.castor.types.GYear.parseGYear(\"{0}\");\n"
                      + " typeValidator.setMaxInclusive(max);", getMaxInclusive());
            } else if (getMaxExclusive() != null) {
                jsc.add(" org.exolab.castor.types.GYear max;\n"
                      + " max = org.exolab.castor.types.GYear.parseGYear(\"{0}\");\n"
                      + " typeValidator.setMaxExclusive(max);", getMaxExclusive());
            }

            jsc.add("} catch (java.text.ParseException pe) {\n"
                  + " System.out.println(\"ParseException\" + pe);\n"
                  + "}", "");
        }
    }

    //--------------------------------------------------------------------------
}
