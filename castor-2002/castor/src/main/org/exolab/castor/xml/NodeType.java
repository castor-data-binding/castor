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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml;


/**
 * The possible node types for an XML field. A field can be represented
 * as an attribute, an element or text content. The default is
 * attribute.
 *
 * @author <a href="arkin@exoffice.com">Assaf Arkin</a>
 * @author <a href="kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class NodeType
{


    /**
     * The attribute type
    **/
    public static final short ATTRIBUTE  = 0;
    
    /**
     * The element type
    **/
    public static final short ELEMENT    = 1;
    
    /**
     * The text type
    **/
    public static final short TEXT       = 2;
    
    /**
     * Attribute node type (<tt>attribute</tt>). This field will appear
     * in the XML document as an element's attribute.
     */
    public static final NodeType Attribute 
        = new NodeType( NodeType.ATTRIBUTE, "attribute" );


    /**
     * Element node type (<tt>element</tt>). This field will appear
     * in the XML document as a contained element.
     */
    public static final NodeType Element 
        = new NodeType( NodeType.ELEMENT, "element" );


    /**
     * Content node type (<tt>text</tt>). This field will appear in
     * the XML document as the element text content.
     */
    public static final NodeType Text 
        = new NodeType( NodeType.TEXT, "text" );


    /**
     * Returns the node type from the name. If <tt>nodeType</tt>
     * is null, return the default node type ({@link #Attribute}).
     * Otherwise returns the named node type mode.
     *
     * @param nodeType The node type name
     * @return The node type
     */
    public static NodeType getNodeType( String nodeType )
    {
        if ( nodeType == null )
            return Attribute;
        if ( nodeType.equals( Attribute._name ) )
            return Attribute;
        if ( nodeType.equals( Element._name ) )
            return Element;
        if ( nodeType.equals( Text._name ) )
            return Text;
        throw new IllegalArgumentException( "Unrecognized node type" );
    }


    /**
     * The name of this node type as it would appear in a
     * mapping file.
     */
    private String _name;

    /**
     * The type of this NodeType
    **/
    private short  _type = ELEMENT;
    
    private NodeType( short type, String name )
    {
        _name = name;
        _type = type;
    }

    /**
     * Returns the type of this NodeType
     * @return the type of this NodeType
    **/
    public short getType() {
        return _type;
    }

    public String toString()
    {
        return _name;
    }


} //-- NodeType
