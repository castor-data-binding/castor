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

import org.exolab.castor.builder.SourceGeneratorConstants;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JCollectionType;
import org.exolab.javasource.JType;

/**
 * A base class for all list types.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision: 6678 $ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 */
public abstract class XSListType extends XSType {
    //--------------------------------------------------------------------------

    /** Content type of the collection. */
    private final XSType _contentType;
    
    /** The JType represented by this XSType. */
    private final JType _jType;
    
    /** Maximum size of this list. If set to -1 the maximum size is undefined. */
    private int _maxSize = -1;
    
    /** Minimum size of this list. */
    private int _minSize = 0;

    //--------------------------------------------------------------------------

    /**
     * Creates an instance of this (abstract base) collection type.
     * @param colType Type of collection to use.
     * @param contentType Type of the collection members.
     * @param useJava50 If true, the collection will be generated using Java 5
       */
    public XSListType(final String colType, final XSType contentType, final boolean useJava50) {
        super();
        
        _contentType = contentType;
        
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
     * Returns the type contained in the list.
     * 
     * @return The type contained in the list.
     */
    public final XSType getContentType() {
        return _contentType;
    }

    /**
     * {@inheritDoc}
     */
    public JType getJType() { return _jType; }

    /**
     * Returns the maximum allowed size for this list.
     * 
     * @return The maximum allowed size for this list.
     */
    public final int getMaximumSize() {
        return _maxSize;
    }

    /**
     * Sets the maximum allowed size for this list.
     * 
     * @param size New maximum size for this list
     */
    public final void setMaximumSize(final int size) {
        _maxSize = size;
    }

    /**
     * Returns the minimum allowed size for this list.
     * 
     * @return The minimum allowed size for this list.
     */
    public final int getMinimumSize() {
        return _minSize;
    }

    /**
     * Sets the minimum allowed size for this list.
     * 
     * @param size New minimum size for this list
     */
    public final void setMinimumSize(final int size) {
        _minSize = size;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCollection() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return _jType.getName();
    }

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

    
}
