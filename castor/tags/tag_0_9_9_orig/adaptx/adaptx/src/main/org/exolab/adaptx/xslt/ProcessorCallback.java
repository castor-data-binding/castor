/*
 * (C) Copyright Keith Visco 1998  All rights reserved.
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 */

package org.exolab.adaptx.xslt;

import org.w3c.dom.*;

import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.util.ErrorObserver;
import org.exolab.adaptx.util.ErrorObserverAdapter;


/**
 * This class is used for calling back into the RuleProcessor
 * to perform special tasks, such as adding nodes to the
 * result tree
 * @author Keith Visco (kvisco@ziplink.net)
**/
public class ProcessorCallback extends ErrorObserverAdapter {
    
    private RuleProcessor  rp         = null;
    private ProcessorState ps         = null;
    
    /**
     * Creates a new ProcessorCallback for the given 
     * RuleProcessor
     * @param ruleProcessor the RuleProcessor to create the 
     *  ProcessorCallback for
     * @param env the current ProcessorState
    **/
    protected ProcessorCallback(RuleProcessor ruleProcessor, ProcessorState ps) 
    {
        rp = ruleProcessor;
        addErrorObserver(rp);
        this.ps = ps;
    } //-- ProcessorCallback
      //------------------/
     //- Public Methods -/
    //------------------/
    /**
     * Adds the given node as a child to the current element in the result
     * tree
     * @param node the node to add to the result tree
    **
    public void addToResultTree(Node node) {
        if (ps == null) return;
        //ps.addToResultTree(node);
    } //-- addToResultTree
    
    
    /**
     * Creates an Attr
     * @param name the name of the Attr
     * @param value the value of the Attr
     * @retutn the new Attr
    **
    public Attr createAttribute(String name, String value) {
        Attr attr = ps.getResultDocument().createAttribute(name);
        attr.setValue(value);
        return attr;
    } //-- createAttribute
    
    /**
     * Creates a CDATASection with the given data
     * @param data the contents of the CDATASection
     * @retutn the new CDATASection
    **
    public CDATASection createCDATASection(String data) {
        return ps.getResultDocument().createCDATASection(data);
    } //-- createCDATASection
    
    /**
     * Creates a Comment with the given data
     * @param data the contents of the Comment
     * @retutn the new Comment
    **
    public Comment createComment(String data) {
        return ps.getResultDocument().createComment(data);
    } //-- createComment
    
    /**
     * Creates an Element with the given tag name (gi)
     * @param tagName the tagName of the Element
     * @retutn the new Element
    **
    public Element createElement(String tagName) {
        return ps.getResultDocument().createElement(tagName);
    } //-- createElement
    
    /**
     * Creates a ProcessingInstruction with the given name and data
     * @param name the target of the pi
     * @param data the contents of the pi
     * @retutn the new ProcessingInstruction
    **
    public ProcessingInstruction createProcessingInstruction
        (String name, String data) 
    {
        return ps.getResultDocument().createProcessingInstruction(name,data);
    } //-- createPI
    
    /**
     * Creates a Text node with the given data
     * @param data the contents of the Text node
     * @retutn the new Text
    **
    public Text createText(String data) {
        return ps.getResultDocument().createTextNode(data);
    } //-- createText
    */

    /**
     * Returns the parameter value associated with the given name.
     * @param name the name of the parameter to retrieve the value of
     * @return the parameter value associated with the given name.
    **/
    public String getParameter(String name) {
        return rp.getProperty(name);
    } //-- getParameter
    /**
     * Returns the XSL property value associated with the given name
     * @param name the name of the property to retrieve the value of
     * @return the XSL property value associated with the given name
    **/
    String getProperty(String name) {
        return rp.getProperty(name);
    } //-- getProperty
    
    /**
     *
    **/
    public XPathResult processVariable(Variable var, ProcessorState ps) 
    {
        return rp.processVariable(var, ps);
    }
} //-- ProcessorCallback
