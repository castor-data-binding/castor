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


package org.exolab.castor.xml;


import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.FieldDescriptor;


/**
 * XML field descriptor. Wraps {@link FieldDescriptor} and adds
 * XML-related information, type conversion, etc.
 *
 * @author <a href="arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public interface XMLFieldDescriptor extends FieldDescriptor {
    
    
    /**
     * Return the "suggested" namespace prefix to use when marshalling
     * as XML.
     *
     * @return the "suggested" namespace prefix.
    **/
    public String getNameSpacePrefix();
    
    /**
     * Returns the namespace URI to be used when marshalling and 
     * unmarshalling as XML.
     * 
     * @return the namespace URI.
    **/
    public String getNameSpaceURI();
    
    /**
     * Returns the NodeType of the Field being described. The
     * NodeType represents the Type of Node that the Field will
     * be marshalled into XML as.
     *
     * @return the NodeType of the Field being described.
    **/
    public NodeType getNodeType();

    /**
     * Returns a specific validator for the field described by
     * this descriptor. A null value may be returned
     * if no specific validator exists. 
     *
     * @return the field validator for the described field
    **/
    public FieldValidator getValidator();
    
    /**
     * Returns the XML Name for the field being described.
     *
     * @return the XML name.
    **/
    public String getXMLName();


    /**
     * Returns the incremental flag which when true indicates that this
     * member may be safely added before the unmarshaller is finished
     * unmarshalling it.
     * @return true if the Object can safely be added before the unmarshaller
     * is finished unmarshalling the Object.
    **/
    public boolean isIncremental();
    
    /**
     * Returns true if the field described by this descriptor can
     * contain more than one value
     * @return true if the field described by this descriptor can
     * contain more than one value
    **/
    public boolean isMultivalued();
    
    /**
     * Returns true if the field described by this descriptor is
     * a reference (ie. IDREF) to another object in the 
     * "Object Model" (XML tree)
    **/
    public boolean isReference();
    
    /**
     * Returns true if this descriptor can be used to handle elements
     * or attributes with the given XML name. By default this method
     * simply compares the given XML name with the internal XML name.
     * This method can be overridden to provide more complex matching.
     * @param xmlName the XML name to compare
     * @return true if this descriptor can be used to handle elements
     * or attributes with the given XML name.
    **/
    public boolean matches(String xmlName);
    
    
} //-- XMLFieldDescriptor



