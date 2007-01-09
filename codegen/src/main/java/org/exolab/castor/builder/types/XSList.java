/*
 * Copyright 2007 Assaf Arkin, Keith Visco, Ralf Joachim
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

import org.exolab.castor.builder.SourceGeneratorConstants;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JCollectionType;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * A list type.
 * 
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public final class XSList extends XSListType {
    //--------------------------------------------------------------------------

    /** Name of the IDREFS type. */
    public static final String IDREFS_NAME = "IDREFS";
    
    /** name of the NMTOKENS type. */
    public static final String NMTOKENS_NAME  = "NMTOKENS";

    /** Type number of this XSType. */
    public static final short TYPE = XSType.COLLECTION;

    //--------------------------------------------------------------------------

    /** The JType represented by this XSType. */
    private final JType _jType;

    //--------------------------------------------------------------------------

    /**
     * Create a XSList.
     *
     * @param colType Type of collection to use.
     * @param contentType Type of the collection members.
     * @param useJava50 If true, the collection will be generated using Java 5
     *        features such as generics.
     */
    public XSList(final String colType, final XSType contentType, final boolean useJava50) {
        super(contentType);
        
        if (colType.equalsIgnoreCase(SourceGeneratorConstants.FIELD_INFO_ARRAY_LIST)) {
            _jType = new JCollectionType("java.util.List", "java.util.ArrayList",
                    contentType.getJType(), useJava50);
        } else if (colType.equalsIgnoreCase(SourceGeneratorConstants.FIELD_INFO_COLLECTION)) {
            _jType = new JCollectionType("java.util.Collection", "java.util.LinkedList",
                    contentType.getJType(), useJava50);
        } else if (colType.equalsIgnoreCase(SourceGeneratorConstants.FIELD_INFO_SET)) {
            _jType = new JCollectionType("java.util.Set", "java.util.HashSet",
                    contentType.getJType(), useJava50);
        } else if (colType.equalsIgnoreCase(SourceGeneratorConstants.FIELD_INFO_SORTED_SET)) {
            _jType = new JCollectionType("java.util.SortedSet", "java.util.TreeSet",
                    contentType.getJType(), useJava50);
        } else if (colType.equalsIgnoreCase(SourceGeneratorConstants.FIELD_INFO_VECTOR)) {
            _jType = new JCollectionType("java.util.Vector", contentType.getJType(), useJava50);
        } else if (colType.equalsIgnoreCase(SourceGeneratorConstants.FIELD_INFO_ODMG)) {
            _jType = new JClass("org.odmg.DArray");
        } else {
            _jType = null;
        }
    }

    //--------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String getName() {
        short type = ((XSListType) this).getContentType().getType();
        if (type == NMTOKEN_TYPE) {
            return NMTOKENS_NAME;
        } else if (type == IDREF_TYPE) {
            return IDREFS_NAME;
        } else {
            return _jType.getName();
        }
    }
    
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
        return "null;";
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
