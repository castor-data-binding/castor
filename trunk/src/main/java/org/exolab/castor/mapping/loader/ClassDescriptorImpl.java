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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.mapping.loader;

import java.util.Collection;
import java.util.LinkedList;

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.xml.ClassMapping;

/**
 * A basic class descriptor implementation. Engines will extend this class to provide
 * additional functionality.
 *
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-01-07 15:48:31 -0700 (Sat, 07 Jan 2006) $
 */
public class ClassDescriptorImpl implements ClassDescriptor {
    //-----------------------------------------------------------------------------------

    private ClassMapping _mapping;
    
    /** The Java class for this descriptor. */
    private Class _javaClass;

    /** The descriptor of the class which this class extends,
     *  or null if this is a top-level class. */
    private ClassDescriptor _extends;
    
    /** A collection of class descriptors that extend this class, or 
     *  an empty collection if this is a leaf class. */
    private final Collection _extended = new LinkedList();

    private ClassDescriptor _depends;

    /** The fields described for this class. */
    private FieldDescriptor[] _fields;

    /** The field of the identity for this class. */
    private FieldDescriptor[] _identities;

    //-----------------------------------------------------------------------------------

    public void setMapping(final ClassMapping mapping) {
        _mapping = mapping;
    }
    
    public ClassMapping getMapping() {
        return _mapping;
    }
    
    public void setJavaClass(final Class javaClass) {
        _javaClass = javaClass;
    }

    public Class getJavaClass() {
        return _javaClass;
    }
    
    public void setExtends(final ClassDescriptor extend) {
        _extends = extend;
    }
    
    public ClassDescriptor getExtends() {
        return _extends;
    }
    
    public void addExtended(final ClassDescriptor classDesc) {
        _extended.add(classDesc);
    }
    
    /**
     * Returns a collection of class descriptors that extend this class descriptor.
     *
     * @return A collection of class descriptors.
     */
    public Collection getExtended() {
    	return _extended;
    }
    
    public void setDepends(final ClassDescriptor depends) {
        _depends = depends;
    }

    public ClassDescriptor getDepends() {
        return _depends;
    }

    public void setFields(final FieldDescriptor[] fields) {
        _fields = fields;
    }

    public FieldDescriptor[] getFields() {
        return _fields;
    }
    
    public void setIdentities(final FieldDescriptor[] identities) {
        _identities = identities;
    }

    public FieldDescriptor[] getIdentities() {
        return _identities;
    }

    public FieldDescriptor getIdentity() {
        return (_identities == null) ? null : _identities[0];
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return _javaClass.getName();
    }

    //-----------------------------------------------------------------------------------
}


