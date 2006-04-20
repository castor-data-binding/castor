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

package org.exolab.adaptx.xslt;

import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xpath.NodeSet;

/**
 * Represents a TreeFragment result
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class TreeFragmentResult extends XSLTFunctionResult {

    /**
     * The XPathNode representing the "tree-fragment"
    **/
    private NodeSet _nodes = null;
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * Creates a new TreeFragmentResult that represents
     * a "empty" fragment.
    **/
    public TreeFragmentResult() {
        super();        
    } //-- TreeFragmentResult
    
    /**
     * Creates a new TreeFragmentResult with the given XPathNode.
     *
     * @param node the XPathNode which is the "tree fragment".
    **/
    public TreeFragmentResult(XPathNode node) {
        _nodes = new NodeSet(node);
    } //-- TreeFragmentResult

    /**
     * Creates a new TreeFragmentResult with the given NodeSet.
     *
     * @param nodes the NodeSet which is the "tree fragment".
    **/
    public TreeFragmentResult(NodeSet nodes) {
        _nodes = nodes;
    } //-- TreeFragmentResult

      //------------------/
     //- Public Methods -/
    //------------------/

    /**
     * Returns the value of this TreeFragmentResult
     *
     * @return the value of this TreeFragmentResult
    **/
    public NodeSet getValue() {
        return _nodes;
    } //-- getValue

    /**
     * Sets the value of this TreeFragmentResult
     *
     * @param node the XPathNode to use as the result fragment
    **/
    public void setValue(XPathNode node) {
        _nodes = new NodeSet(node);
    } //-- setValue

    /**
     * Sets the value of this TreeFragmentResult
     *
     * @param nodes the NodeSet to use as the result fragment
    **/
    public void setValue(NodeSet nodes) {
        _nodes = nodes;
    } //-- setValue
    
      //-------------------------------------------------/
     //- Public Methods defined by XSLTFunctionResult -/
    //------------------------------------------------/
    
    /**
     * Returns the type of this result.
     *
     * @return The type of this result
     */
    public short getXSLTResultType() {
        return XSLTFunctionResult.TREE_FRAGMENT;
    } //-- getXSLTResultType
    

      //-----------------------------------------/
     //- Public Methods defined by XPathResult -/
    //-----------------------------------------/
    
    /**
     * Returns the value of this ExprResult as a boolean
     * @return the value of this ExprResult as a boolean
    **/
    public boolean booleanValue() {
        if (_nodes == null) return false;
        return _nodes.booleanValue();
    } //-- booleanValue
    
    /**
     * Returns true if the given expression is the same tyoe as
     * this result and has the same value as this result.
     *
     * @param result An XPath result
     * @return True if same type and same value as this result
     */
    public boolean equals(XPathResult result) {
        if ((result == null) || (_nodes == null)) return false;
        
        if (result instanceof TreeFragmentResult) {
            TreeFragmentResult tfr = (TreeFragmentResult)result;
            return tfr._nodes.equals(this._nodes);
        }
        return false;
    } //-- equals
    
    /**
     * Returns the value of this XPathResult as a double
     *
     * @return the value of this XPathResult as a double
    **/
    public double numberValue() {
        
        if (_nodes == null) return Double.NaN;
        
        return _nodes.numberValue();
        
    } //-- numberValue
        
    /**
     * Returns the Result as a Java Object. For tree-fragment
     * this is simply an XPathNode.
     *
     * @return the Result as a Java Object
    **/
    public Object javaObject() {
        return _nodes;
    } //-- javaObject
    
    /**
     * Returns the result as a string value. Returns "false" or
     * "true" for a boolean result, the value of a string result,
     * the string value of a number result, or the string value of
     * a node-set.
     *
     * @return The result as a string value
     */
    public String stringValue() {
        if (_nodes == null) return null;
        return _nodes.stringValue();
    } //--stringValue
    
    public String toString() {
        return stringValue();
    }
    
} //-- TreeFragmentResult
