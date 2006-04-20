/*
 * (C) Copyright Keith Visco 1998-2002  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * except in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.kvisco.com/xslp/license.txt
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 *
 * $Id$
 */
 
package org.exolab.adaptx.xslt;

import java.util.Enumeration;
import java.util.Hashtable;

import org.w3c.dom.*;
import org.xml.sax.AttributeList;

import org.exolab.adaptx.xml.AttributeListImpl;
import org.exolab.adaptx.xslt.util.ActionTemplateImpl;
import org.exolab.adaptx.util.List;

import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.XPathParser;
import org.exolab.adaptx.xpath.XPathExpression;



/**
 * This class represents an XSLObject in the style tree. It is the most
 * basic of all XSLObjects and contains common functionality
 * across different XSLObjects. I originally had this implemented
 * as an extended W3C DOM XML Element, but due to problems with 
 * extending different implementations, and to make it more
 * cross-DOM accessible I've chosen an this approach. Since this once
 * was a DOM Element, you will notice many of the methods are
 * very DOM-like.
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class XSLObject {
    
    
    private static final String DEFAULT_NAME = "xsl:element";
    private static final String DEFAULT_NS_DECL = "xmlns";
    private static final String PREFIX_NS_DECL  = "xmlns:";
    
    
    public static final short    APPLY_IMPORTS         =  0;
    public static final short    APPLY_TEMPLATES       =  1;
    public static final short    ARG                   =  2;
    public static final short    ATTRIBUTE             =  3;
    public static final short    ATTRIBUTE_SET         =  4;
    public static final short    CALL_TEMPLATE         =  5;
    public static final short    CDATA                 =  6;
    public static final short    CHOOSE                =  7;
    public static final short    COMMENT               =  8;
    public static final short    CONTENTS              =  9;
    public static final short    COPY                  = 10;
    public static final short    COPY_OF               = 11;
    public static final short    ELEMENT               = 12;
    public static final short    FOR_EACH              = 13;
    public static final short    FUNCTIONS             = 14;
    public static final short    ID                    = 15;
    public static final short    IF                    = 16;
    public static final short    IMPORT                = 17;
    public static final short    INCLUDE               = 18;
    public static final short    KEY                   = 19;
    public static final short    LITERAL               = 20;
    public static final short    LOCALE                = 21;
    public static final short    MESSAGE               = 22;
    public static final short    NUMBER                = 23;
    public static final short    OTHERWISE             = 24;
    public static final short    OUTPUT                = 25;
    public static final short    PARAM                 = 26;
    public static final short    PI                    = 27;
    public static final short    PRESERVE_SPACE        = 28;
    public static final short    SORT                  = 29;
    public static final short    STRIP_SPACE           = 30;
    public static final short    STYLESHEET            = 31;
    public static final short    TEMPLATE              = 32;    
    public static final short    TEXT                  = 33;
    public static final short    VALUE_OF              = 34;
    public static final short    VARIABLE              = 35;
    public static final short    WHEN                  = 36;
    public static final short    WITH_PARAM            = 37;
    
    // Proprietary XSL elements
    public static final short    ENTITY_REF            = 38;
    public static final short    SCRIPT                = 39;
    
    //-- total number of xsl element types
    private static final short   MAX_TYPE              = 40;
    
    /**
     * The type of this XSLObject
    **/
    private short type = LITERAL; // default
    
    /**
     * This XSLObjects list of actions.
    **/
    private ActionTemplateImpl actions = null;
    
    private AttributeListImpl attributes = null;
    
    private List readOnlyAttrs = null;
    
    private boolean allowActions = true;
    
    private XSLTStylesheet parentStylesheet = null;
    
    private XSLObject parent = null;
    
    private String typeName = DEFAULT_NAME;
   
    private NamespaceDecl _namespaces = null;
    
    //-- static variables
    private static final Hashtable typeNames = buildNameHash();
    
    private static final XPathParser _parser = new XPathParser();
    
    /**
     * This XSLObject's namespace, really only useful for
     * literal elements
     */
    private String _namespace = null;
    
    //-- configure XPathParser
    static {
        _parser.setUseErrorExpr(true);
    }
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    
    /**
     * Creates an XSLObject using the specified type
     * @param parentStylesheet the owner XSLStylesheet 
     * of the new Element
     * @param type the type of XSLObject that the new instance
     * represents
    **/
    public XSLObject(short type) {
        super();
        this.type      = type;
        this.typeName  = getNameFromType(type);
        attributes     = new AttributeListImpl(5);
        actions        = new ActionTemplateImpl();
        readOnlyAttrs  = new List(0);
    } //-- XSLObject
    
      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Adds the Given namespace declaration to this XSLObject's set of namespace
     * declarations
     */
    public void addNamespaceDecl(String prefix, String namespace) {
        if (prefix == null) prefix = "";
        
        NamespaceDecl nsDecl = null;
        //-- look for existing prefix declaration and overwrite
        //-- if necessary
        if (_namespaces != null) {
            nsDecl = _namespaces;
            while (nsDecl != null) {
                if (nsDecl.prefix.equals(prefix)) {
                    nsDecl.uri = namespace;
                    return;
                }
                nsDecl = nsDecl.next;
            }
        }
        
        //-- not found, create new declaration        
        nsDecl = new NamespaceDecl();
        nsDecl.prefix = prefix;
        nsDecl.uri = namespace;
        if (_namespaces != null) {
            nsDecl.next = _namespaces;
        }
        _namespaces = nsDecl;
    } //-- addNamespaceDecl
    
    
    /**
     * Appends the given XSLObject to this XSLObject's list of
     * actions. 
     * @param xslObject the XSLObject to add to this XSLObject's
     * list of actions
     * @return true if the given XSLObject has been added to this
     * XSLObject otherwise false 
    **/
    public boolean appendAction(XSLObject xslObject) {
        if (!allowActions) return false;
        if (handleAction(xslObject)) return true;
        if (!actions.addAction(xslObject)) return false;
        xslObject.setParent(this);
        return true;
    } //-- appendAction
    
    /**
     * Appends the text to this XSLObject. This is slightly more efficient
     * than using appendAction(new XSLText(text)) if the last child
     * is already an XSLText object. Otherwise there is no difference.
     * @param text the text to append
    **/
    public void appendText(String text) {
        if (!allowActions) return;
        
        //-- if last child is a text node, just append
        XSLObject action = actions.lastAction();
        if (action != null) {
            if (action.getType() == XSLObject.TEXT) {
                XSLText xslText = (XSLText)action;
                if (!xslText.disableOutputEscaping()) {
                    xslText.appendText(text);
                    return;
                }
            }
        }
        XSLText xslText = new XSLText(text);
        actions.addAction(xslText);
        xslText.setParent(this);
    } //-- appendText
    
    /**
     * Appends the text to this XSLObject. This is slightly more efficient
     * than using appendAction(new XSLText(text)) if the last child
     * is already an XSLText object. Otherwise there is no difference.
     * @param text the text to append
    **/
    public void appendText(char[] chars, int start, int length) {
        if (!allowActions) return;
        
        //-- if last child is a text node, just append
        XSLObject action = actions.lastAction();
        if (action != null) {
            if (action.getType() == XSLObject.TEXT) {
                XSLText xslText = (XSLText)action;
                if (!xslText.disableOutputEscaping()) {
                    xslText.appendText(chars, start, length);
                    return;
                }
            }
        }
        //-- create a new text object and add child
        XSLText xslText = new XSLText();
        xslText.appendText(chars, start, length);
        actions.addAction(xslText);
        xslText.setParent(this);
    } //-- appendText
    
    /**
     * Returns the list of actions for this XSLObject
     * @return the list of actions for this XSLObject
    **/
    public ActionTemplate getActions() {
        return actions;
    } //-- getActions
    
    /**
     * Returns the value of the attribute whose name is equal to the given name.
     * @return the value of the attribute whose name is equal to the given name 
     * or null if no attribute exist's with such a name.
    **/
    public String getAttribute(String name) {
        if (name != null) return (String)attributes.getValue(name);
        return null;
    } //-- getAttribute
    
    /**
	 * Returns the value of the specified attribute as an AttributeValueTemplate
	 * @return the value of the specified attribute as an AttributeValueTemplate
	 * @exception XSLException when the Attribute is not a valid 
	 * AttrubueValueTemplate
	**
    public AttributeValueTemplate getAttributeAsAVT(String name) 
        throws XSLException
    {
        if (name == null) return null;
        
        String attValue = getAttribute(name);
        
        AttributeValueTemplate avt = null;
        
        if ((attValue != null) && (attValue.length() > 0)) {
            // look in cache first
            avt = (AttributeValueTemplate) avtCache.get(attValue);
            if (avt == null) {
                try {
                avt = new AttributeValueTemplate(attValue);
                // add to cache for performace
                // Do we need to clean cache? Yes if we are reusing
                // XSLObjects. No if not. I am currently not 
                // reusing XSLObjects so I am not doing any house 
                // cleaning. This could lead to memory problems if 
                // XSLObjects are reused heavily.
                avtCache.put(attValue, avt);
                }
                catch(InvalidExprException iee) {
                    throw new XSLException
                        (XSLException.INVALID_ATTRIBUTE_VALUE_TEMPLATE,
                            iee.getMessage());
                }
            }
        }
       return avt;
    } //-- getAttributeAsAVT
    
    /**
     * Returns the AttributeList for this XSLObject
     * @return an Enumeration of the names of the attributes of this
     * XSLObject
    **/
    public AttributeList getAttributes() {
        return attributes;
    } //-- getAttributeNames
    
    /**
     * Returns this XSLObject's namespace URI.
     *
     * @return the namespace URI or null if none exists.
     */
    public String getNamespace() {
        if (type == LITERAL) {
            if (_namespace != null)
                return _namespace;
                
            if (typeName != null) {
                String prefix = "";
                int idx = typeName.indexOf(':');
                if (idx >= 0) {
                    prefix = typeName.substring(0, idx);
                }
                _namespace = resolveNamespace(prefix);
                return _namespace;
            }
            return null;
        }
        return XSLTStylesheet.XSLT_NAMESPACE;
    } //-- getNamespace
    
    /**
     * Returns the nearest ancestor of this XSLObject that is of the given
     * type.
     * @param type the type of ancestor to search for
     * @return the nearest ancestor of this XSLObject that is of the given
     * type.
    **/
    public XSLObject getNearestAncestor(short type) {
        if (parent == null) return null;
        if (parent.getType() == type) return parent;
        else return parent.getNearestAncestor(type);
    } //-- getNearestAncestor
    
	/**
	 * Returns the String value of a DOM Node.
	 * @return the String value of a DOM Node.
	 * @see org.w3c.dom.Node
	**/
    public static String getNodeValue(Node node) {
        if (node == null) return "";
        switch (node.getNodeType()) {
            
            case Node.DOCUMENT_NODE:
                return getNodeValue(((Document)node).getDocumentElement());
            case Node.DOCUMENT_FRAGMENT_NODE:
                StringBuffer sb = new StringBuffer();
                NodeList nl = node.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    sb.append(getNodeValue(nl.item(i)));
                }
                return sb.toString();
            // elements
            case Node.ELEMENT_NODE:
                return XSLObject.getText((Element)node);
            // attributes
            case Node.ATTRIBUTE_NODE:
                return ((Attr)node).getValue();
            // Text and Character Data
            case Node.CDATA_SECTION_NODE:
            case Node.TEXT_NODE:
                return ((Text)node).getData();
            // Comments
            case Node.COMMENT_NODE:
                return ((Comment)node).getData();
            // Processing Instructions
            case Node.PROCESSING_INSTRUCTION_NODE:
                return ((ProcessingInstruction)node).getData();
            default:
                break;
        }
        return "";
    } //-- getNodeValue
    
    
    protected XSLObject getParent() {
        return parent;
    } //-- getParent
    
    /**
     * Returns the XSLStylesheet which contains this XSLObject
     * @return the XSLStylesheet which contains this XSLObject
    **/
    public XSLTStylesheet getStylesheet() {
        
        if (this.type == XSLObject.STYLESHEET) 
            return (XSLTStylesheet) this;
        
        if (parent == null) return null;
        return parent.getStylesheet();
        
    } //-- getStylesheet
    
    /**
     * Retrieves the text of an Element
     * @return the text of the given Element
     * @see org.w3c.dom.Element
    **/
    public static String getText(Element element) {
        if (element == null) return null;
        
        NodeList list = element.getChildNodes();
        Node node;
        int size = list.getLength();
        
        StringBuffer sb = new StringBuffer();
        
        for (int i = 0; i < size; i++) {
            node = list.item(i);
            switch(node.getNodeType()) {
                case Node.TEXT_NODE:
                case Node.CDATA_SECTION_NODE:
                    if (size == 1) return ((Text)node).getData();
                    else sb.append(((Text)node).getData());
                    break;
                case Node.ELEMENT_NODE:
                    if (size == 1) return XSLObject.getText((Element)node);
                    else sb.append(XSLObject.getText((Element)node));
                    break;
                default:
                    break;
            }
        }
        return sb.toString();
    } //-- getText
    
    /**
     * Returns the type of XSLObject this Object represents
     * @return the type of XSLObject that this Object represents
    **/
    public final short getType() {
        return type;
    } //-- getType
    
    /**
     * Returns the type of XSL Object that has the given name
     * @param name the name the XSLObject
     * @return the type of XSL Object that has the given name
    **/
    public static short getTypeFromName(String name) {
        Short sh = (Short)typeNames.get(name);
        if (sh == null) return LITERAL;
        else return sh.shortValue();
    } //-- getTypeFromName
    
    /**
     * Returns the name of this XSLObject
     * @return the name of this XSLObject
    **/
    public String getTypeName() {
        return typeName;
    } //-- getTypeName
    
    public String resolveNamespace(String prefix) {
        //-- we need to change the following line
        //-- to return the default namespace...eventually        
        if (prefix == null) prefix = "";
        
        if (_namespaces != null) {
            NamespaceDecl nsDecl = _namespaces;
            while (nsDecl != null) {
                if (nsDecl.prefix.equals(prefix)) {
                    return nsDecl.uri;
                }
                nsDecl = nsDecl.next;
            }
        }
        
        if (parent != null) {
            return parent.resolveNamespace(prefix);
        }        
        return null;
    } //-- resolveNamespace
    
    public void setAllowActions(boolean allow) {
        this.allowActions = allow;
    } //-- allowChildActions
    
    public void setTypeName(String name) {
        this.typeName = name;
    } //-- setTypeName
    
    /**
     * Sets the attribute with the given name to the given value.
     * @param name the name of the attribute to set
     * @param value the value to set the attribute to
     * @throws XSLException if this XSLObject does not allow attributes
     * with the given name, or if the attribute is read only
    **/
    public void setAttribute(String name, String value) 
        throws XSLException
    {
        if ((name != null) && (value != null)) {
            
            if (name.equals(DEFAULT_NS_DECL)) {
                addNamespaceDecl("", value);
                attributes.addAttribute(name, value);
            }
            else if (name.startsWith(PREFIX_NS_DECL)) {
                String prefix = name.substring(PREFIX_NS_DECL.length());
                addNamespaceDecl(prefix, value);
                attributes.addAttribute(name, value);
            }
            else if (readOnlyAttrs.contains(name)) {
                StringBuffer err = new StringBuffer("The attribute '");
                err.append(name);
                err.append("' has been set to read only for this ");
                err.append(getTypeName());
                err.append(" and cannot be changed.");
                throw new XSLException(err.toString());
            }
            else {
                attributes.addAttribute(name,value);
            }
        }
    } //-- setAttribute
    
    /** 
     * Sets this XSLObject's namespace URI
     *
     * @param uri the namespace URI 
     */
    public void setNamespace(String uri) {
        _namespace = uri;
    } //-- setNamespace
    
      //---------------------/
     //- Protected Methods -/
    //---------------------/
    
    /**
     * Allows subclasses to handle the append operation of the 
     * given XSLObject. Called from #appendAction
     * this method should return true, if there is nothing left to do,
     * otherwise false, if the super class should handle the append
    **/
    protected boolean handleAction(XSLObject xslObject) {
        return false;
    } //--  handleAction
    
    /**
     * Copies the attributes from the given XSLObject to
     * this XSLObject.
    **/
    protected void copyAttributes(XSLObject xslObj) {
        xslObj.copyAttributesInto(this.attributes);
    } //-- copyAttributes
    
    /**
     * Copies the actions of the XSLObject argument and
     * adds them to this XSLObject
     * @param xslObject the XSLObject to copy actions from
    **/
    protected void copyActions(XSLObject xslObject) {
        ActionTemplate template = xslObject.getActions();
        if (template.size() > 0) {
            ActionIterator iter = template.actions();
            while (iter.hasNext()) appendAction(iter.next());
        }
        
    } //-- copyActions
    
    protected void makeAttrReadOnly(String name) {
        readOnlyAttrs.add(name);
    } //-- makeAttrReadOnly
    
    protected void setParent(XSLObject parent) {
        this.parent = parent;
    } //-- setParent
        
    protected void copyAttributesInto(AttributeListImpl dest) {
        if (dest == null) return;
        int size = attributes.getLength();
        for (int i = 0; i < size; i++) {
            dest.addAttribute(attributes.getName(i),
                attributes.getValue(i));
        }
        
    } //-- copyAttributesInto
    
    /**
     * Creates an XPath select expression from the given String value.
     * The expression created with always evaluate to a NodeSet.
     * 
    **/
    protected final XPathExpression createSelectExpression(String expr) 
        throws XPathException
    {
        return _parser.createSelectExpression(expr);
    } //-- createSelectExpression

    /**
     * Creates an XPath expression from the given String value
    **/
    protected final XPathExpression createExpression(String expr) 
        throws XPathException
    {
        return _parser.createExpression(expr);
    } //-- createSelectExpression
    
    
    
      //-------------------/
     //- private methods -/
    //-------------------/
    
    /**
     * Builds a hashtable of all XSL Element names
    **/
    private static Hashtable buildNameHash() {
        Hashtable ht = new Hashtable();
        for (short i = 0; i < MAX_TYPE; i++) {
            ht.put(getNameFromType(i), new Short(i));
        }
        //-- the stylesheet element can have two different names,
        //-- "stylesheet" or "transform", stupid in my opinion
        ht.put(Names.TRANSFORM, new Short(STYLESHEET));
        return ht;
    } //-- buildNameHash
    
    /**
     * Returns the Name of XSL Objects that have the given Type
     * @param type the XSL Object type
    **/
    private static String getNameFromType(short type) {
        
        switch (type) {
            case APPLY_IMPORTS:
                return Names.APPLY_IMPORTS;
            case APPLY_TEMPLATES:
                return Names.APPLY_TEMPLATES;
            case ATTRIBUTE:
                return Names.ATTRIBUTE;
            case ATTRIBUTE_SET:
                return Names.ATTRIBUTE_SET;
            case CALL_TEMPLATE:
                return Names.CALL_TEMPLATE;
            case CDATA:
                return Names.CDATA;
            case CHOOSE:
                return Names.CHOOSE;
            case COMMENT:
                return Names.COMMENT;
            case COPY:
                return Names.COPY;
            case COPY_OF:
                return Names.COPY_OF;
            case ELEMENT:
                return Names.ELEMENT;
            case ENTITY_REF:
                return Names.ENTITY_REF;
            case FOR_EACH:
                return Names.FOR_EACH;
            case FUNCTIONS:
                return Names.FUNCTIONS;
            case ID:
                return Names.ID;
            case IF:
                return Names.IF;
            case IMPORT:
                return Names.IMPORT;
            case INCLUDE:
                return Names.INCLUDE;
            case KEY:
                return Names.KEY;
            case LOCALE:
                return Names.LOCALE;
            case MESSAGE:
                return Names.MESSAGE;
            case NUMBER:
                return Names.NUMBER;
            case OTHERWISE:
                return Names.OTHERWISE;
            case OUTPUT:
                return Names.OUTPUT;
            case PARAM:
                return Names.PARAM;
            case PI:
                return Names.PI;
            case PRESERVE_SPACE:
                return Names.PRESERVE_SPACE;
            case SORT:
                return Names.SORT;
            case STRIP_SPACE:
                return Names.STRIP_SPACE;
            case STYLESHEET:
                return Names.STYLESHEET;
            case TEMPLATE:    
                return Names.TEMPLATE;    
            case TEXT:
                return Names.TEXT;
            case VALUE_OF:
                return Names.VALUE_OF;
            case VARIABLE:
                return Names.VARIABLE;
            case WHEN:
                return Names.WHEN;
            case WITH_PARAM:
                return Names.WITH_PARAM;
            case SCRIPT:
                return Names.SCRIPT;
            default:
                return DEFAULT_NAME;
        }
    } //-- getNameFromType
    
    class NamespaceDecl {
        String uri = null;
        String prefix = null;
        NamespaceDecl next = null;
    }
    
    /* */
} //-- XSLObject
