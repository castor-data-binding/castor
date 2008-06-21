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
package org.exolab.castor.mapping;

import java.util.Iterator;
import java.util.List;

import org.castor.mapping.BindingType;

/**
 * Provides the mapping descriptor for Java classes. The engines use
 * resolvers to obtain the mapping descriptor for a particular Java
 * class, or to list all the known descriptors. Although the interface
 * is identical, each engine will use a resolver that returns class
 * descriptor suitable for that particular engine. Resolvers are
 * immutable and engines need not cache the returned descriptors.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public interface MappingLoader {
    BindingType getBindingType();
    
    String getSourceType();
    
    void clear();
    
    void setClassLoader(final ClassLoader loader);
    
    /**
     * Returns the class loader associated with this mapping loader if one was specified. This is
     * the class loader used to load all the classes mapped by this mapping loader. May be null if
     * no class loader was specified or in certain JVMs.
     */
    ClassLoader getClassLoader();

    /**
     * Returns the ClassDescriptor for the class with the given name. If no such ClassDescriptor
     * exists, within the set of mappings for this MappingLoader, null will be returned.
     *
     * @param classname The className for which to return the associated ClassDescriptor.
     * @return The ClassDescriptor or null if not found.
     */
    ClassDescriptor getDescriptor(String classname);
    
    /**
     * Returns an iterator over all the known descriptors in the original order they have been
     * added. Each element is of type {@link ClassDescriptor}.
     */
    Iterator descriptorIterator();
    
    /**
     * Returns a List of {@link ClassDescriptor}s of all known descriptors.
     * @return List of {@link ClassDescriptor}
     */
    List getDescriptors();
}
