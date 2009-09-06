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

import org.exolab.castor.xml.schema.Facet;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * The XML Schema user-defined archetype.
 * 
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public final class XSClass extends XSType {
    //--------------------------------------------------------------------------

    /** Type number of this XSType. */
    public static final short TYPE = XSType.CLASS;

    //--------------------------------------------------------------------------

    /** Name of this type. */
    private final String _name;

    /** The JClass represented by this type. */
    private final JClass _jClass;

    //--------------------------------------------------------------------------

    /**
     * Creates a new XSClass with the given JClass reference.
     * 
     * @param jClass The JClass type of this XSClass.
     */
    public XSClass(final JClass jClass) {
        this(jClass, null);
    }

    /**
     * Creates a new XSClass with the given JClass reference.
     * 
     * @param jClass The JClass associated with this XSType.
     * @param schemaTypeName The XML Schema type name.
     */
    public XSClass(final JClass jClass, final String schemaTypeName) {
        super();
        
        _jClass = jClass;
        if (schemaTypeName != null) {
            _name = schemaTypeName;
        } else {
            _name = jClass.getName();
        }
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String getName() { return _name; }

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
    public JType getJType() { return _jClass; }

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
    protected void setFacet(final Facet facet) {
        // Not implemented
    }

    /**
     * {@inheritDoc}
     */
    public void validationCode(final JSourceCode jsc,
            final String fixedValue, final String validatorInstanceName) {
        // Not implemented
    }
    
    //--------------------------------------------------------------------------
}
