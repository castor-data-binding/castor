/*
 * Copyright 2007 Ralf Joachim
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

import org.exolab.javasource.JArrayType;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * The xsd:hexBinary XML Schema datatype.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 6623 $ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 * @since 1.1
 */
public final class XSHexBinary extends AbstractLengthFacet {
    //--------------------------------------------------------------------------

    /** Name of this XSType. */
    public static final String NAME = "hexBinary";
    
    /** Type number of this XSType. */
    public static final short TYPE = XSType.HEXBINARY_TYPE;

    //--------------------------------------------------------------------------

    /** The JType represented by this XSType. */
    private final JType _jType;

    //--------------------------------------------------------------------------

    /**
     * Create a new XSHexBinary object.
     *
     * @param useJava50 If true, Java 5 code artifacts will be generated.
     */
    public XSHexBinary(final boolean useJava50) {
        super();
        
        _jType = new JArrayType(JType.BYTE, useJava50);
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
        return "new byte[] {};";
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
        return "(" + getJType().toString() + ") " + variableName;
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void validationCode(final JSourceCode jsc,
            final String fixedValue, final String validatorInstanceName) {
        // Not implemented
    }

    //--------------------------------------------------------------------------
}
