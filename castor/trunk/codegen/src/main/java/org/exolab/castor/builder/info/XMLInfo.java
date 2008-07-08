/*
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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder.info;

import org.exolab.castor.builder.types.XSType;

/**
 * A class for storing XML related information.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-02-23 01:37:50 -0700 (Thu, 23 Feb 2006) $
 */
public interface XMLInfo {

    /** Represents the attribute node type. */
    short ATTRIBUTE_TYPE = 0;
    /** Represents the element node type. */
    short ELEMENT_TYPE   = 1;
    /** Represents the text node type. */
    short TEXT_TYPE      = 2;

    /**
     * Identifies the node name for a choice group.
     */
    String CHOICE_NODE_NAME_ERROR_INDICATION = "-error-if-this-is-used-";


    /**
     * Returns the XML name for the object described by this XMLInfo.
     *
     * @return the XML name for the object described by this XMLInfo, or null if
     *         no name has been set
     */
    String getNodeName();
    
    /**
     * Returns the namespace prefix of the object described by this XMLInfo.
     *
     * @return the namespace prefix of the object described by this XMLInfo
     */
    String getNamespacePrefix();

    /**
     * Returns the namespace URI of the object described by this XMLInfo.
     *
     * @return the namespace URI of the object described by this XMLInfo
     */
    String getNamespaceURI();

    /**
     * Returns true if XSD is global element or element with anonymous type.
     *
     * @return true if xsd is element
     */
    boolean isElementDefinition();

    /**
     * Returns the node type for the object described by this XMLInfo.
     *
     * @return the node type for the object described by this XMLInfo
     */
    short getNodeType();

    /**
     * Returns the string name of the nodeType, either "attribute", "element" or
     * "text".
     *
     * @return the name of the node-type of the object described by this
     *         XMLInfo.
     */
    String getNodeTypeName();

    /**
     * Returns the XML Schema type for the described object.
     *
     * @return the XML Schema type.
     */
    XSType getSchemaType();

    /**
     * Return whether or not the object described by this XMLInfo is
     * multi-valued (appears more than once in the XML document).
     *
     * @return true if this object can appear more than once.
     */
    boolean isMultivalued();

    /**
     * Return true if the XML object described by this XMLInfo must appear at
     * least once in the XML document (or object model).
     *
     * @return true if the XML object must appear at least once.
     */
    boolean isRequired();

}
