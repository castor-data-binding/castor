/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
 
package org.exolab.castor.xml.util;

import org.exolab.castor.xml.AttributeSet;
import org.exolab.castor.util.List;

/**
 * The default implementation of AttributeSet used by
 * the Marshalling Framework.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
**/
public class AttributeSetImpl 
    implements AttributeSet 
{
 
    /**
     * The XML namespace declaration prefix.
     * It is an error for an attribute name to be equal to this value.
    **/
    public static final String XMLNS = "xmlns";
     
    private static final String EMPTY_STRING = "";
    
    /**
     * The list of attributes in this AttributeSet
    **/
    private List _attributes = null;
    
    
    /**
     * Creates a new AttributeSetImpl
    **/
    public AttributeSetImpl() {
        _attributes = new List(3);
    } //-- AttributeSetImpl

    /**
     * Creates a new AttributeSetImpl
     *
     * @param size the default size for this AttributeSetImpl
    **/
    public AttributeSetImpl(int size) {
        if (size < 0) {
            String err = "size cannot be less than zero";
            throw new IllegalArgumentException(err);
        }
        _attributes = new List(size);
    } //-- AttributeSetImpl
    
    /**
     * Removes all Attributes in this AttributeSetImpl
    **/
    public void clear() {
        _attributes.clear();
    } //-- clear
    
    
    /**
     * Returns the index of the attribute associated with the given name
     * and namespace.
     * 
     *
     * @param name the name of the attribute whose value should be returned.
     * @param namespace the namespace of the attribute
     * @return the index of the attribute, or -1 if not found.
    **/
    public int getIndex(String name, String namespace) {
        if (namespace == null) namespace = EMPTY_STRING;
        for (int i = 0; i < _attributes.size(); i++) {
            Attribute attr = (Attribute)_attributes.get(i);
            if (namespace.equals(attr.namespace)) {
                if (attr.name.equals(name)) return i;
            }
        }
        return -1;
    } //-- getIndex
    
    /**
     * Returns the name of the attribute located at the given index.
     *
     * @param index the index of the attribute whose name should be returned.
     * @return the name of the attribute located at the given index.
    **/
    public String getName(int index) {
        Attribute attr = (Attribute)_attributes.get(index);
        return attr.name;
    } //-- getName
    
    /**
     * Returns the namespace of the attribute located at the given index.
     *
     * @return the namespace of the attribute located at the given index.
    **/
    public String getNamespace(int index) {
        Attribute attr = (Attribute)_attributes.get(index);
        return attr.namespace;
    } //-- getNamespace
    
    /**
     * Returns the number of Attributes within this AttributeSet.
     *
     * @return the number of Attributes within this AttributeSet.
    **/
    public int getSize() {
        return _attributes.size();
    } //-- getSize
    
    /**
     * Returns the value of the attribute located at the given index
     * within this AttributeSet.
     *
     * @param index the index of the attribute whose value should be returned.
    **/
    public String getValue(int index) {
        Attribute attr = (Attribute)_attributes.get(index);
        return attr.value;
    } //-- getValue
    
    /**
     * Returns the value of the attribute associated with the given name.
     * This method is equivalent to call #getValue(name, null);
     *
     * @param name the name of the attribute whose value should be returned.
    **/
    public String getValue(String name) {
        if (name == null) return null;
        Attribute attr = getAttribute(name, "");
        if (attr != null) 
            return attr.value;
        return null;
    } //-- getValue
    
    /**
     * Returns the value of the attribute associated with the given name.
     * This method is equivalent to call #getValue(name, null);
     *
     * @param name the name of the attribute whose value should be returned.
     * @param namespace the namespace of the attribute
    **/
    public String getValue(String name, String namespace) {
        if (name == null) return null;
        Attribute attr = getAttribute(name, namespace);
        if (attr != null) 
            return attr.value;
        return null;
    } //-- getValue

    /**
     * Adds or replaces the attribute with the given name.
     * No namespace is associated with the attribute.
     *
     * @param name the name of the attribute
     * @param value the attribute value.
    **/
    public void setAttribute(String name, String value) {
        setAttribute(name, value, EMPTY_STRING);
    } //-- setAttribute
    
    /**
     * Adds or replaces the attribute with the given name.
     * No namespace is associated with the attribute.
     *
     * @param name the name of the attribute
     * @param value the attribute value.
    **/
    public void setAttribute(String name, String value, String namespace) 
    {
        if ((name == null) || (name.length() == 0))
            throw new IllegalArgumentException("name must not be null");
            
        if (XMLNS.equals(name)) {
            String err = "'xmlns' is a reserved word for use with XML " 
                + "namespace declarations. It may not be used as an "
                + "attribute name.";
            throw new IllegalArgumentException(err);
        }

        if (namespace == null) namespace = EMPTY_STRING;
        
        Attribute attr = getAttribute(name, namespace);
        if (attr == null) {
            _attributes.add(new Attribute(name, value, namespace));
        }
        else attr.value = value;
        
    } //-- setAttribute
    
    private Attribute getAttribute(String name, String namespace) {
        if (namespace == null) namespace = EMPTY_STRING;
        for (int i = 0; i < _attributes.size(); i++) {
            Attribute attr = (Attribute)_attributes.get(i);
            if (namespace.equals(attr.namespace)) {
                if (attr.name.equals(name)) return attr;
            }
        }
        return null;
    } //-- getAttribute
    
    /**
     * A representation of an Attribute
    **/
    class Attribute {
        
        String name      = null;
        String value     = null;
        String namespace = null;
        
        public Attribute() {
            super();
        } //-- Attribute
        
        public Attribute(String name, String value, String namespace) {
            this.name = name;
            this.value = value;
            this.namespace = namespace;
        } //-- Attribute
        
    } //-- class Attribute
    
} //-- class AttributeSetImpl
 
 
 
 