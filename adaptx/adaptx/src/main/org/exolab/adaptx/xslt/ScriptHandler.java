/*
 * (C) Copyright Keith Visco 1998, 1999  All rights reserved.
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

import org.w3c.dom.*;

/**
 * An interface for scripting environments
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
public interface ScriptHandler {


    /**
     * Calls the method with the given name, and set of arguments
     * @param name the name of the method to call
     * @param args the methods arguments
     * @return the result of the method invocation
    **/
    public Object call(String name, Object[] args);

    /**
     * Calls the method with the given name, and set of arguments
     * @param name the name of the method to call
     * @param args the methods arguments
     * @param namespace the Namespace to use for evaluation
     * @return the result of the method invocation
    **/
    public Object call(String name, Object[] args, String namespace);

    /**
     * Creates a new namespace with the given name
    **/
    public boolean createNamespace(String name);

    /**
     * Evaluates the given XSLScript element using the default namespace
     * @param xslScript the XSLScript to evaluate
     * @param context the current DOM Node that is the context
     * of this evaluation.
     * @return the result of the XSLScript evaluation
    **/
    public Object eval(XSLScript xslScript, Node context);

    /**
     * Evaluates the given XSLScript element using the given namespace
     * @param xslScript the XSLScript to evaluate
     * @param context the current DOM Node that is the context
     * of this evaluation.
     * @param namespace the Namespace to use for evaluation
     * @return the result of the XSLScript evaluation
    **/
    public Object eval(XSLScript xslScript, Node context, String namespace);

    /**
     * Evaluates the given XSLScript element as a function using the
     * default namespace.
     * @param xslScript the XSLScript to evaluate
     * @param context the current DOM Node that is the context
     * of this evaluation.
     * @return the result of the XSLScript evaluation
    **/
    public Object evalAsFunction(XSLScript xslScript, Node context);

    /**
     * Evaluates the given XSLScript element as a function using the
     * given namespace.
     * @param xslScript the XSLScript to evaluate
     * @param context the current DOM Node that is the context
     * of this evaluation.
     * @param namespace the Namespace to use for evaluation
     * @return the result of the XSLScript evaluation
    **/
    public Object evalAsFunction
        (XSLScript xslScript, Node context, String namespace);

    /**
     * Returns the name of the language that this ScriptHandler handles
     * @return the name of the language that this ScriptHandler handles
    **/
    public String getLanguage();

    public boolean hasDefinedFunction(String name, String namespace);

    /**
     * Initializes the scripting environment
     * @param pc the ProcessorCallback for supporting
     * access to the RuleProcessor.
    **/
    public void initialize(ProcessorCallback pc);

} //-- ScriptHandler