/*
 * (C) Copyright Keith Visco 1999  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
 * distributed with this file. You may also obtain a copy of the
 * license at http://www.clc-marketing.com/xslp/license.txt
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


package org.exolab.adaptx.xpath;

import java.util.Hashtable;

/**
 * Base implementation of an XPath context. An XPath context provides
 * a way to manage a stack of node-sets, resolve variable and
 * function names, and return the size and position of the context.
 * This implementation is not thread-safe, care must be taken not to
 * use the same context when evaluating expressions concurrently.
 * <p>
 * Impelementations may wish to extend this class and provide additional
 * facilities for locating the document order of a node, or providing
 * a function library.
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @version $Revision$
 */
public class XPathContext
{


    /**
     * The set of default namespace bindings
    **/
    private Hashtable _namespaces = null;
    
    /**
     * The parent context of this XPathContext
    **/
    private XPathContext _parent = null;


    /**
     * The position of the context node.
     */
    private int _position;

    /**
     * Variable bindings.
     */
    private final VariableSet   _variables;


    /**
     * The context NodeSet
    **/
    private NodeSet _nodeSet = null;

    /**
     * Constructs a new XPath context using the given node
     * as the context node. The size is set to 1 and
     * position is set to 0 and no 
     * variable bindings are provided.
     * @param node the context XPathNode.
    **/
    public XPathContext(XPathNode node)
    {
        _position = 0;
        _variables = null;
        _nodeSet = new NodeSet(node);
        
    } //-- XPathContext

    
    /**
     * Constructs a new XPath context using the given node as
     * as the context node. The size is set to 1 and
     * position is set to 0. Variables bindings may be
     * optionally provided.
     *
     * @param node the context XPathNode.
     * @param variable Variable bindings, or null
     */
    public XPathContext
        (XPathNode node, VariableSet variables)
    {
        _nodeSet = new NodeSet(node);
        _position = 0;
        _variables = variables;
    } //-- XPathContext


    /**
     * Constructs a new XPath context. Variables bindings may be
     * optionally provided, size and position are required.
     *
     * @param variable Variable bindings, or null
     * @param nodeSet the context node-set for this XPathContext
     * @param position The position of the context node (zero base)
     */
    public XPathContext
        (VariableSet variables, NodeSet nodeSet, int position)
    {
        _nodeSet = nodeSet;
        setPosition(position);
        _variables = variables;
    } //-- XPathContext

    /**
     * Creates a binding within this XPathContext between a given prefix 
     * and a namespace URI. This namespace binding will be override
     * any binding for the given prefix within a parent context, and will
     * be available to all sub-contexts unless they also override the
     * namespace binding with one of their own.
     *
     * @param prefix the prefix to associate with the namespace
     * @param namespace the namespace URI.
    **/
    public void addNamespaceBinding(String prefix, String namespace) {
        
        //-- create namespaces if null. We do that here so that
        //-- we do not create unecessary hashtables for each context.
        if (_namespaces == null) {
            _namespaces = new Hashtable();
        }
        
        //-- adjust prefix. Hashtable cannot have null value.
        if (prefix == null) prefix = "";
        
        if (namespace == null)
            _namespaces.remove(prefix);
        else 
            _namespaces.put(prefix, namespace);
    } //-- addNamespaceBinding
    
    /**
     * Returns the context node of this XPathContext
     *
     * @return the context node
    **/
    public XPathNode getNode() {
        if (_nodeSet == null) return null;
        return _nodeSet.item(_position);
    } //-- getNode

    /**
     * Returns the position of the context node. The position is
     * a value between zero and the context size minus one.
     * One must be added in order to obtain a value XPath position.
     *
     * @return The position of the context node
     * @see #getSize
     */
    public int getPosition()
    {
        return _position;
    }


    /**
     * Returns the size of the context.
     *
     * @return The size of the context
     */
    public int getSize()
    {
        if (_nodeSet == null) return 0;
        return _nodeSet.size();
    } //-- getSize

    /**
     * Creates a new XPathContext with this XPathContext as it's
     * parent.
     *
     * @return the new XPathContext
    **/
    public XPathContext newContext(XPathNode node) {
        XPathContext context = new XPathContext(node);
        context._parent = this;
        return context;
    } //-- newContext

    /**
     * Creates a new XPathContext with this XPathContext as it's
     * parent.
     *
     * @return the new XPathContext
    **/
    public XPathContext newContext(NodeSet nodeSet, int position) {
        XPathContext context = new XPathContext(null, nodeSet, position);
        context._parent = this;
        return context;
    } //-- newContext

    /**
     * Constructs and returns a new node-set with the specified size.
     *
     * @param size The size of the node-set
     * @return A new node-set with that size
     */
    public NodeSet newNodeSet( int size )
    {
        return new NodeSet( size );
    }

    
    /**
     * Constructs and returns a new node-set with one node.
     *
     * @param node The node to include in the node-set
     * @return A new node-set
    **/
    public NodeSet newNodeSet( XPathNode node )
    {
        return new NodeSet( node );
    }


    /**
     * Constructs and returns a new empty node-set.
     *
     * @return A new empty node-set
    **/
    public NodeSet newNodeSet()
    {
        return new NodeSet();
    }


    /**
     * Returns the current context node-set.
     *
     * @return The current context node-set
    **/
    public NodeSet getNodeSet() {
        
        return _nodeSet;
        
    } //-- getNodeSet


    /**
     * Sets the given node-set as the context node-set for
     * this XPathContext
     *
     * @param nodeSet the node-set to use as the context node-set.
     * @param position the position of the context node
    **/
    public void setNodeSet( NodeSet nodeSet, int position )
    {
        if ( nodeSet == null )
            throw new IllegalArgumentException( "Argument nodeSet is null" );
            
        _nodeSet = nodeSet;        
        setPosition(position);
        
    } //-- setNodeSet
    

    /**
     * Returns the document order of the given node.
     *
     * @return The document order of the given node
    **/
    public int[] getDocumentOrder( XPathNode node )
    {
        if (_parent != null) {
            return _parent.getDocumentOrder(node);
        }
        return new int[ 0 ];
    }

    
    /**
     * Returns the element associated with the given identifier.
     * Locates the element underneath the specified root node.
     *
     * @param root The root node
     * @param id The element's identifier
     * @return The first element in document order with the given
     * identifier, or null if no such element was found
    **/
    public XPathNode getElementById( XPathNode root, String id )
    {
        if (_parent != null) {
            return getElementById(root, id);
        }
        return null;
    }

    
    /**
     * Returns the XPath result bound to the given variable name.
     * Returns null if the variable was not set.
     *
     * @param name The variable name
     * @return The variable's value
    **/
    public XPathResult getVariable( String name )
    {
        XPathResult  value;

        if ( _variables == null ) {
            if (_parent == null)
                return null;
            else
                return _parent.getVariable(name);
        }
        else {
            XPathResult result = _variables.getVariable(name);
            if (result == null) {
                if (_parent != null)
                    result = _parent.getVariable(name);
            }
            return result;
        }
    } //-- getVariable
    
    /**
     * Returns the XPath function by the given name, or null if no
     * such function is defined.
     *
     * @param uri The function's namespace URI
     * @param name The function's name within that URI
     * @return The XPath function by the given name, or null if no
     * such function is defined
    **/
    public XPathFunction getFunction( String uri, String name )
    {
        if (_parent != null) {
            return _parent.getFunction(uri, name);
        }
        return null;
        
    } //-- getFunction

    /** 
     * Returns the namespace associated with the given prefix as
     * defined in this context. Null is returned if no namespace
     * has been defined.
     *
     * @param prefix the namespace prefix
     * @return the namespace uri or null.
    **/
    public String getNamespaceURI( String prefix ) {
        
        if (_namespaces != null) {
            if (prefix == null) prefix = "";
            String ns = (String) _namespaces.get(prefix);
            if (ns != null) return ns;
        }
        
        //-- check parent
        if (_parent != null) {
            return _parent.getNamespaceURI(prefix);
        }
        return null;
        
    } //-- getNamespaceURI
    
    /**
     * Sets the position of the context node within the context
     * node-set
     *
     * @param the position of the context node within the
     * context node-set
     * @exception IndexOutOfBoundsException when the position
     * is not within the bounds of the context node-set.
    **/
    public void setPosition(int position) {
        
        if (_nodeSet == null) 
            throw new IllegalStateException("Error no context "+
                "node-set exists.");
        
        if ((position < 0) || (position >= _nodeSet.size()))
            throw new IndexOutOfBoundsException(position + 
                " is not within the bounds of the context node-set.");
            
        _position = position;
        
    } //-- setPosition
    
} //-- XPathContext
