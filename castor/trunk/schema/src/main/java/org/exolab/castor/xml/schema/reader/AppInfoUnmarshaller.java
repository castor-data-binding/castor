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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

import java.util.Enumeration;
import java.util.Stack;

import org.exolab.castor.types.AnyNode;
import org.exolab.castor.xml.AttributeSet;
import org.exolab.castor.xml.Namespaces;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.XMLContext;
import org.exolab.castor.xml.XMLException;
import org.exolab.castor.xml.schema.AppInfo;
import org.exolab.castor.xml.schema.SchemaContext;
import org.exolab.castor.xml.schema.SchemaNames;

/**
 * A class for unmarshalling XML Schema {@literal <appinfo>} elements
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-10-01 07:25:46 -0600 (Fri, 01 Oct 2004) $
**/
public class AppInfoUnmarshaller extends ComponentReader {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * The Attribute reference for the Attribute we are constructing.
     */
    private AppInfo _appInfo = null;

    /**
     * Stack of AnyNodes being unmarshalled.
     */
    private Stack _nodes = null;

    /**
     * Name of the table annotation element.
     */
    private static final String TABLE_NAME = "table";
    
    /**
     * Name of the column annotation element.
     */
    private static final String COLUMN_NAME = "column";

    /**
     * Package where to find JDO classes to unmarshall annotations.
     */
    private static final String JDO_PACKAGE = 
        "org.exolab.castor.xml.schema.annotations.jdo";
    
    /**
     * JDO Namespace.
     */
    private static final String JDO_NAMESPACE = 
        "http://www.castor.org/binding/persistence";
    
      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new AppInfoUnmarshaller.
     * @param schemaContext the schema context to get some configuration settings from
     * @param atts the AttributeList
     * @throws XMLException if instantiation failed for any reason.
    **/
    public AppInfoUnmarshaller(
            final SchemaContext schemaContext, 
            final AttributeSet atts)
        throws XMLException {
        super(schemaContext);

        _appInfo = new AppInfo();
        _appInfo.setSource(atts.getValue(SchemaNames.SOURCE_ATTR));
        
        _nodes = new Stack();

    } //-- AppInfoUnmarshaller

      //-----------/
     //- Methods -/
    //-----------/

    /**
     * @return appinfo.
    **/
    public AppInfo getAppInfo() {
        return _appInfo;
    } //-- getArchetype

    /**
     * Returns the name of the element that this ComponentReader
     * handles.
     * @return the name of the element that this ComponentReader
     * handles
    **/
    public String elementName() {
        return SchemaNames.APPINFO;
    } //-- elementName

    /**
     * Called to signal an end of unmarshalling. This method should
     * be overridden to perform any necessary clean up by an unmarshaller
    **/
    public void finish() {
        //-- do nothing
    } //-- finish

    /**
     * Returns the Object created by this ComponentReader.
     * @return the Object created by this ComponentReader
    **/
    public Object getObject() {
        return getAppInfo();
    } //-- getObject

    /**
     * Signals the start of an element with the given name.
     *
     * @param name the NCName of the element. It is an error
     * if the name is a QName (ie. contains a prefix).
     * @param namespace the namespace of the element. This may be null.
     * Note: A null namespace is not the same as the default namespace unless
     * the default namespace is also null.
     * @param atts the AttributeSet containing the attributes associated
     * with the element.
     * @param nsDecls the namespace declarations being declared for this 
     * element. This may be null.
     * @throws XMLException if any error occurs
    **/
    public void startElement(String name, String namespace, AttributeSet atts,
        Namespaces nsDecls)
        throws XMLException {
        
        String prefix = null;
        if (nsDecls != null) {
            //-- find prefix (elements use default namespace if null)
            if (namespace == null) {
                namespace = "";
            }
            prefix = nsDecls.getNamespacePrefix(namespace);
        }
        
        AnyNode node = new AnyNode(AnyNode.ELEMENT, name, prefix, namespace, null);
        _nodes.push(node);
        
        //-- process namespace nodes
        if (nsDecls != null) {
            Enumeration enumeration = nsDecls.getLocalNamespaces();
            while (enumeration.hasMoreElements()) {
                namespace = (String) enumeration.nextElement();
                prefix = nsDecls.getNamespacePrefix(namespace);
                node.addNamespace (new AnyNode(AnyNode.NAMESPACE, 
                                                null,  //-- no local name for a ns decl.
                                                prefix, 
                                                namespace,
                                                null)); //-- no value
            }
        }
        //-- process attributes
        if (atts != null) {
            for (int i = 0; i < atts.getSize(); i++) {
                namespace = atts.getNamespace(i);
                if ((nsDecls != null) && (namespace != null)) {
                    prefix = nsDecls.getNamespacePrefix(namespace);
                } else {
                    prefix = null;
                }
                node.addAttribute(new AnyNode(AnyNode.ATTRIBUTE, 
                                           atts.getName(i), 
                                           prefix, namespace, 
                                           atts.getValue(i)));
            }
        }

    } //-- startElement

    /**
     * Signals to end of the element with the given name.
     *
     * @param name the NCName of the element. It is an error
     * if the name is a QName (ie. contains a prefix).
     * @param namespace the namespace of the element.
     * @throws XMLException if unmarshalling fails.
     * 
    **/
    public void endElement(final String name, final String namespace)
        throws XMLException {
        AnyNode node = (AnyNode) _nodes.pop();
        if (_nodes.isEmpty()) {
            //-- unmarshall jdo appinfo content
            if (node.getNamespaceURI().equals(JDO_NAMESPACE) 
                    && (node.getLocalName().equals(TABLE_NAME) 
                            || node.getLocalName().equals(COLUMN_NAME))) {
                XMLContext context = new XMLContext();
                context.addPackage(JDO_PACKAGE);
                Unmarshaller unmarshaller = context.createUnmarshaller();               
                unmarshaller.setClassLoader(getClass().getClassLoader());
                _appInfo.getJdoContent().add(unmarshaller.unmarshal(node));
            }            
            //-- add to appInfo
            _appInfo.add(node);
        } else {
            //-- add to parent AnyNode
            ((AnyNode) _nodes.peek()).addChild(node);
        }
    } //-- endElement

    public void characters(char[] ch, int start, int length)
        throws XMLException {
        //-- Do delagation if necessary
        AnyNode text = new AnyNode(AnyNode.TEXT, 
                                   null,  //-- no local name for text nodes 
                                   null,  //-- no prefix
                                   null,  //-- no namespace
                                   new String(ch, start, length));
                                   
        if (!_nodes.isEmpty()) {
            AnyNode parent = (AnyNode) _nodes.peek();
            parent.addChild(text);
        } else {
            _appInfo.add(text);
        }
        
    } //-- characters

} //-- AppInfoUnmarshaller
