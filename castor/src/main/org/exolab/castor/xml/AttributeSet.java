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
 
package org.exolab.castor.xml;

/**
 * A simple interface for handling Attributes in the Marshalling
 * Framework.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public interface AttributeSet {
    
    
    /**
     * Returns the index of the attribute associated with the given name
     * and namespace.
     * 
     *
     * @param name the name of the attribute whose value should be returned.
     * @param namespace the namespace of the attribute
     * @return the index of the attribute, or -1 if not found.
    **/
    public int getIndex(String name, String namespace);
    
    /**
     * Returns the name of the attribute located at the given index.
     *
     * @param index the index of the attribute whose name should be returned.
     * @return the name of the attribute located at the given index.
    **/
    public String getName(int index);
    
    /**
     * Returns the namespace of the attribute located at the given index.
     *
     * @return the namespace of the attribute located at the given index.
    **/
    public String getNamespace(int index);
    
    /**
     * Returns the number of Attributes within this AttributeSet.
     *
     * @return the number of Attributes within this AttributeSet.
    **/
    public int getSize();
    
    /**
     * Returns the value of the attribute located at the given index
     * within this AttributeSet.
     *
     * @param index the index of the attribute whose value should be returned.
    **/
    public String getValue(int index);
    
    /**
     * Returns the value of the attribute associated with the given name.
     * This method is equivalent to call #getValue(name, null);
     *
     * @param name the name of the attribute whose value should be returned.
    **/
    public String getValue(String name);
    
    /**
     * Returns the value of the attribute associated with the given name.
     * This method is equivalent to call #getValue(name, null);
     *
     * @param name the name of the attribute whose value should be returned.
     * @param namespace the namespace of the attribute
    **/
    public String getValue(String name, String namespace);
    
} //-- AttributeSet
 
 
 
 