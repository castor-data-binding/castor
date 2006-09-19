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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 * Date         Author              Changes
 * 04/24/2001   Arnaud Blandin      Rewrited from scratch
 * 04/22/2001   Arnaud Blandin      Clean-up and support of comments
 * 04/04/2001   Arnaud Blandin      Created
 */

package org.exolab.castor.types;

import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.Stack;
import org.exolab.castor.xml.Serializer;
import org.exolab.castor.xml.util.AnyNode2SAX;

import java.io.StringWriter;

/**
 * A class used to represent an XML node.
 * This is an alternative to DOM which is too heavy for
 * our purpose (mainly handle XML Fragment when {@literal <any>} is used in
 * an XML schema).
 * The model is based on XPath Node.
 * An AnyNode can be a:
 * <ul>
 *      <li>ELEMENT</li>
 *      <li>ATTRIBUTE</li>
 *      <li>NAMESPACE</li>
 *      <li>COMMENT</li>
 *      <li>TEXT</li>
 *      <li>PROCESSING INSTRUCTION</li>
 * </ul>
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class AnyNode implements java.io.Serializable {
    //TODO Processing Instructions
    //TODO Full handling of namespaces

    /** SerialVersionUID */
    private static final long serialVersionUID = -4104117996051705975L;

    /**
     * The prefix for XML namespace
     */
    private final static String XMLNS_PREFIX  = "xmlns";

    /**
     * Representation for an element node.
     */
    public static final short ELEMENT = 1;

    /**
     * Representation for an attribute node.
     */
    public static final short ATTRIBUTE = 2;

    /**
     * Representation for a Namespace node.
     */
    public static final short NAMESPACE = 3;

    /**
     * Representation for a processing instruction node.
     */
    public static final short PI = 4;

    /**
     * Representation for a comment node.
     */
    public static final short COMMENT = 5;

    /**
     * Representation for a text node.
     */
    public static final short TEXT  = 6;

    /**
     * The type of the current node.
     * ELEMENT is the default value.
     */
     private short _nodeType = ELEMENT;

    /**
     * The next sibling of this AnyNode
     */
    private AnyNode _nextSiblingNode = null;

    /**
     * The first child of this AnyNode
     */
    private AnyNode _firstChildNode = null;


    /**
     * the local name of the current node.
     */
    private String _localName;

   /**
    * the Namespace URI of the current node
    */
    private String _uri;

   /**
    * The prefix of the Namespace
    */
    private String _prefix;

    /**
    * A stack used for avoiding endless loop
    * in toString()
    */
    private static Stack _elements;

    /**
     * The namespace context used in the toString()
     */

     /**
      * The value of this node defined as follow:
      * <ul>
      *     <li>for an element the value is its TEXT NODE value (if any)</li>
      *     <li>for an attribute the value is the value of the attribute</li>
      *     <li>for a text node it is the character data</li>
      *     <li>for a namespace it is the namespace URI that is being bound
      *     to the namespace prefix</li>
      *     <li>for a comment it is the content of the comment not including the
      *     opening &lt;!-- and the closing --&gt;. </li>
      * </ul>
      */
      private String _value;

    /**
     * Default constructor: creates an empty element node
     */
    public AnyNode() {
       this(ELEMENT, null, null, null, null);
    }

    /**
     * Creates a node given all the necessary information:
     * type, localName, prefix, uri and value.
     * This constructor is not user-friendly and launched
     * RunTime exception is you try to instantiate non-valid node.
     * @param type the node type.
     * @param localName the name of the node.
     * @param prefix the prefix if any of the namespace.
     * @param uri the namespace uri of this node.
     * @param value the value of this node.
     */
    public AnyNode(short type, String localName,
                   String prefix,String uri,
                   String value)
    {
        if ( (type > 6) && (type<1) )
           throw new IllegalArgumentException("Illegal node type");
         _nodeType = type;

         //comment and text nodes don't have name
         if ( (type>PI) && (localName != null) ) {
            String err = "This node can not have a local name";
            throw new IllegalArgumentException(err);
         }
         _localName = localName;

         //for comment, pi or text we should have no namespaces
         if ((type>NAMESPACE) && ( (uri != null) || (prefix != null)) ) {
            String err = "This node can not handle namespace";
            throw new IllegalArgumentException(err);
         }
        _uri = uri;
        _prefix = prefix;

         //attributes can not be namespaces
         if (type == AnyNode.ATTRIBUTE)
            if (localName.startsWith(XMLNS_PREFIX)) {
               String err = "Namespaces can't be used as attributes.";
               throw new IllegalArgumentException(err);
            }

         //you can't set value for element
         if ( (type == ELEMENT) && (value != null) ) {
            String err = "You can't set a value for this node type";
            throw new IllegalArgumentException(err);
         }
         _value = value;
    }


    /**
     * Adds an AnyNode to the current node
     * @param node the node to append
     */
    public void addAnyNode(AnyNode node) {
        if (node == null)
           throw new IllegalArgumentException("null argument in addAnyNode");
        switch(node.getNodeType()) {
            case ATTRIBUTE:
                 addAttribute(node);
                 break;
            case NAMESPACE:
                 addNamespace(node);
                 break;
            default:
                 addChild(node);

        }
    }

    /**
     * <p>Adds a child AnyNode to this node.
     * A 'child' can be either an ELEMENT node, a COMMENT node,
     * a TEXT node or a PROCESSING INSTRUCTION.
     * If the current node already has a child then
     * the node to add will be append as a sibling.
     * <p>Note: you cannot add a child to a TEXT node.
     * @param node the node to add.
     */
    public void addChild(AnyNode node) {
        if (node == null)
           throw new IllegalArgumentException("null argument in appendChild");

        if ( (node.getNodeType() == ATTRIBUTE) ||
             (node.getNodeType() == NAMESPACE) )
            throw new IllegalArgumentException("An Attribute or an Namespace can't be added as a child");

       if (this.getNodeType() == TEXT)
           throw new IllegalArgumentException("a TEXT node can't have children.");

       if (_firstChildNode == null)
            _firstChildNode = node;

       else if ( (_firstChildNode.getNodeType() == ATTRIBUTE) ||
                 (_firstChildNode.getNodeType() == NAMESPACE) )
            _firstChildNode.addChild(node);

       else _firstChildNode.appendSibling(node);
    }

    /**
     * Adds an attribute to the current node.
     * @param node the attribute to add.
     */
    public void addAttribute(AnyNode node) {

        if (node == null)
           throw new IllegalArgumentException("null argument in addAttribute");
        if (node.getNodeType() != ATTRIBUTE)
            throw new IllegalArgumentException("Only attribute can be added as an attribute");
        if (_firstChildNode == null)
            _firstChildNode = node;
        else {
            //if we reach an attribute then we add the node as its sibling
            if (_firstChildNode.getNodeType() == ATTRIBUTE)
               _firstChildNode.appendSibling(node);
            //if we reach an namespace the attributre should be added to
            //the first child of the namespace
            else if (_firstChildNode.getNodeType() == NAMESPACE)
                 _firstChildNode.addAttribute(node);
            //unplug the current firstNode to add a new one
            else {
                 node.addChild(_firstChildNode);
                 _firstChildNode = node;
            }
        }
    }//addAttribute

    /**
     * Appends an namespace to the current node.
     * @param node the attribute to add.
     */
     public void addNamespace(AnyNode node){
        if (node == null)
           throw new IllegalArgumentException("null argument in addNamespace");
        if (node.getNodeType() != NAMESPACE)
            throw new IllegalArgumentException("Only namespace can be added as an namespace");
        if (_firstChildNode == null)
            _firstChildNode = node;
        else {
             //if we reach an namepace then we add the node as its sibling
            if (_firstChildNode.getNodeType() == NAMESPACE)
               _firstChildNode.appendSibling(node);
             //if we reach an attribute the attributre should be added to
            //the first child of the attribute
            else if (_firstChildNode.getNodeType() == ATTRIBUTE)
               _firstChildNode.addNamespace(node);
            //unplug the current firstNode to add a new one
            else {
                 node.addChild(_firstChildNode);
                 _firstChildNode = node;
            }
        }
    }//addNamespace

    /**
     * Returns the first attribute of the current ELEMENT node
     * or null.
     * The next attribute,if any,is the sibling of the returned node.
     */
     public AnyNode getFirstAttribute() {

         if (this.getNodeType() != ELEMENT) {
            String err = "This node type can not contain attributes";
            throw new OperationNotSupportedException(err);
         }
         boolean found = false;
         AnyNode tempNode = this.getFirstChildNode();
         while ( (tempNode != null) && !(found)) {
               short type = tempNode.getNodeType();
               //if the child is not an attribute or a namespace
               //this element does not have any attribute
               if (type == ELEMENT || type == COMMENT || type == TEXT || type == PI)
                  tempNode = null;
               else if (type == NAMESPACE)
                   tempNode = tempNode.getFirstChildNode();
               else found = true;
         }
         return tempNode;
     }

    /**
     * Returns the first namespace of the current ELEMENT node
     * or null.
     * The next attribute if any is the sibling of the returned node.
     */
     public AnyNode getFirstNamespace() {

         if (this.getNodeType() != ELEMENT) {
            String err = "This node type can not contain namespaces";
            throw new OperationNotSupportedException(err);
         }

         AnyNode tempNode = this.getFirstChildNode();
         boolean found = false;
         while ( (tempNode != null) && !(found) ) {
               short type = tempNode.getNodeType();
               //if the child is not an attribute or a namespace
               //this element does not have any namespace
               if (type == ELEMENT || type == COMMENT || type == TEXT || type == PI)
                  tempNode = null;
               else if (type == ATTRIBUTE)
                    tempNode = tempNode.getFirstChildNode();
                else found = true;
         }
         return tempNode;
     }

    /**
     * Returns the first Child node of this node.
     * A 'child' can be either an ELEMENT node, a COMMENT node,
     * a TEXT node or a PROCESSING INSTRUCTION.
     * @return the first child of this node
     */
     public AnyNode getFirstChild() {
         //an ATTRIBUTE or a NAMESPACE can not
         //have children
         if ((this.getNodeType() == ATTRIBUTE) ||
             (this.getNodeType() == NAMESPACE))
             return null;

         //loop througth the first two (in the worst case) nodes
         //and then return the firstChild if any
         AnyNode tempNode = this.getFirstChildNode();
         boolean found = false;
         while ( (tempNode != null) && !(found) ) {
               short type = tempNode.getNodeType();
               if (type == ELEMENT || type == COMMENT || type == TEXT || type == PI)
                  found = true;
               else if ((type == ATTRIBUTE) || (type == NAMESPACE) )
                    tempNode = tempNode.getFirstChildNode();
         }
         return tempNode;
     }

    /**
     * Returns the next sibling of the current node.
     * When the AnyNode is an ATTRIBUTE, it will return the next ATTRIBUTE node.
     * When the AnyNode is a NAMESPACE, it will return the next NAMESPACE node.
     * @return the next sibling of the current node
     */
    public AnyNode getNextSibling() {
        return _nextSiblingNode;
    }

    /**
     * Returns the type of this node.
     * @return The type of this node
     */
    public short getNodeType() {
         return _nodeType;
    }


    /**
     * Returns the local name of the node.
     * Returns the local name of an element or attribute, the prefix of a namespace
     * node, the target of a processing instruction, or null for
     * all other node types.
     *
     * @return The local name of the node, or null if the node has
     * no name
     */
    public  String getLocalName(){
        return _localName;
    }


    /**
     * Returns the namespace URI of the node. Returns the namespace URI
     * of an element, attribute or namespace node,  or null for
     * all other node types.
     *
     * @return The namespace URI of the node, or null if the node has
     * no namespace URI
     */
    public String getNamespaceURI() {
        return _uri;
    }

    /**
     * Returns the string value of the node. The string value of a text
     * node or an attribute node is its text value. The string value of
     * an element or a root node is the concatenation of the string value
     * of all its child nodes. The string value of a namespace node is its
     * namespace URI. The string value of a processing instruction is the
     * instruction, and the string value of a comment is the comment text.
     *
     * @return The string value of the node
     */
    public String getStringValue() {
           switch(_nodeType) {
                case ATTRIBUTE:
                case TEXT:
                    return _value;
                case NAMESPACE:
                    return _uri;
                //not yet supported
                case PI:
                    return "";
                case COMMENT:
                    return _value;
                case ELEMENT:
                    StringBuffer result = new StringBuffer(4096);
                    AnyNode tempNode = this.getNextSibling();
                    while (tempNode != null &&
                           tempNode.getNodeType() == TEXT)
                    {
                        result.append(tempNode.getStringValue());
                        tempNode = tempNode.getNextSibling();
                    }
                   tempNode = this.getFirstChild();
                    while (tempNode != null) {
                        result.append(tempNode.getStringValue());
                        tempNode = tempNode.getNextSibling();
                    }
                    tempNode = null;
                    return result.toString();
                default:
                    return null;

           }
    }


    /**
     * Returns the namespace prefix associated with the namespace URI of
     * this node.
     * Returns null if no prefix.
     * is defined for this namespace URI. Returns an empty string if the
     * default prefix is associated with this namespace URI.
     *
     * @return The namespace prefix, or null
     */
    public String getNamespacePrefix() {
        return _prefix;
    }


     /**
      * Returns the String representation of this AnyNode
      * The String representation is a xml well-formed fragment corresponding
      * to the representation of this node.
      */
     public String toString() {

        Serializer serializer = LocalConfiguration.getInstance().getSerializer();
        if (serializer == null)
            throw new RuntimeException("Unable to obtain serializer");

        StringWriter writer = new StringWriter();

        serializer.setOutputCharStream( writer );

        try {
            AnyNode2SAX.fireEvents(this,serializer.asDocumentHandler());
        } catch (java.io.IOException ioe) {
            return privateToString();
        } catch (org.xml.sax.SAXException saxe) {
            throw new RuntimeException(saxe.getMessage());
        }

        return writer.toString();
     }

     private String privateToString() {

         StringBuffer sb = new StringBuffer(4096);

         if (_elements == null)
            _elements = new Stack();

          //check the Stack too see if we have
          //already proceed the node
          if (_elements.search(this) == -1) {
              _elements.push(this);

             if (this.getNodeType() == ELEMENT) {
                  //open the tag
                  sb.append("<");
                  String prefix = getNamespacePrefix();
                  if (prefix != null)
                      sb.append(prefix+":");
                  prefix = null;
                  sb.append(getLocalName());

                  //append the attributes
                  AnyNode tempNode = this.getFirstAttribute();
                  while (tempNode != null) {
                     sb.append(" ");
                     sb.append(tempNode.getLocalName());
                     sb.append("='"+tempNode.getStringValue()+"'");
                     tempNode = tempNode.getNextSibling();
                  }

                  //append the namespaces
                  tempNode = this.getFirstNamespace();
                  while (tempNode != null) {
                     sb.append(" ");
                     sb.append(XMLNS_PREFIX);
                     prefix = tempNode.getNamespacePrefix();
                     if ( (prefix != null) && (prefix.length() != 0) )
                        sb.append(":"+prefix);
                     sb.append("='"+tempNode.getNamespaceURI()+"'");
                     tempNode = tempNode.getNextSibling();
                  }//namespaceNode


                  tempNode = this.getFirstChild();
                  if (tempNode != null) {
                      sb.append(">");
                      while (tempNode != null) {
                          sb.append(tempNode.privateToString());
                          tempNode = tempNode.getNextSibling();
                      }
                      //close the tag
                      sb.append("</"+getLocalName()+">");
                  } else sb.append("/>");

             }//ELEMENT
             else {
                sb.append(this.getStringValue());
             }
            return sb.toString();
          }
          return sb.toString();
       }//toString()

    /**
     * Appends a sibling AnyNode to the current node.
     * The node to append will be added at the end of the
     * sibling branch.
     * @param node the node to add
     */
    protected void appendSibling(AnyNode node) {
        if (node == null)
           throw new IllegalArgumentException();

        if ( ((node.getNodeType() == ATTRIBUTE) || (node.getNodeType()== NAMESPACE))&&
              (this.getNodeType() != node.getNodeType())) {
            String err = "a NAMESPACE or an ATTRIBUTE can only be add as a sibling to a node of the same type";
            throw new OperationNotSupportedException(err);
        }

        if (_nextSiblingNode == null) {
            //if we already have a TEXT node -> merge
            if ((node.getNodeType() == TEXT) && (this.getNodeType() == TEXT))
                mergeTextNode(this,node);
            else _nextSiblingNode = node;
        }

        else _nextSiblingNode.appendSibling(node);
    }


    /**
     * Returns the first child node in the tree.
     * @return the first child node in the tree.
     */
     protected AnyNode getFirstChildNode() {
         return _firstChildNode;
     }

     /**
      * Adds the text value of a TEXT node to another
      * TEXT node.
      * @param node1 the AnyNode that receives the text value
      * @param node2 the AnyNode that needs to be merges with node1.
      */
     private void mergeTextNode(AnyNode node1, AnyNode node2) {
         if (node1.getNodeType() != node2.getNodeType())
            return;
        if (node1.getNodeType() != AnyNode.TEXT)
            return;
        StringBuffer temp = new StringBuffer(node1.getStringValue());
        temp.append(node2.getStringValue());
        node1._value = temp.toString();
        node2 = null;
     }

}