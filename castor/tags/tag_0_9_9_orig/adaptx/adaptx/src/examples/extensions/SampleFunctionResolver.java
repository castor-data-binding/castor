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

import org.exolab.adaptx.xpath.XPathContext;
import org.exolab.adaptx.xpath.XPathResult;
import org.exolab.adaptx.xpath.XPathException;
import org.exolab.adaptx.xpath.StringResult;
import org.exolab.adaptx.xslt.XSLTFunction;

/**
 * A sample FunctionResolver which shows how to create
 * a FunctionResolver, as well as how to create functions.
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
public class SampleFunctionResolver 
    implements org.exolab.adaptx.xslt.FunctionResolver
{
    /**
     * The namespace for this FunctionResolver
    **/
    private static final String NAMESPACE
        = "http://www.adaptx.org/functions/Samples";
        
    private static final String[] _namespaces = { NAMESPACE };
    
    /**
     * Creates a new SampleFunctionResolver
    **/
    public SampleFunctionResolver() {
        super();
    } //-- SampleFunctionResolver

    /**
     * Returns the namespaces for this FunctionResolver. These are the
     * namespaces which will be used by extension functions 
     * (within the XSLT document) that are to be resolved by this 
     * FunctionResolver,within the XSLT document.
     * <BR />
     * For Example:<BR />
     * <PRE>
     *  &lt;xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform/" 
     *                     xmlns:foo="http://my.example.com"&gt;
     *     ...
     *     &lt;xsl:template match="..."&gt;
     *         ...
     *         &lt;xsl:value-of select="foo:my-function()"&gt;
     *         ...
     *     &lt;/xsl:template&gt;
     *     ...
     *  &lt;/xsl:stylesheet&gt;
     * </PRE>
     * 
     * @return an array of namespaces supported by this FunctionResolver
     *
    **/
    public String[] getNamespaces() {
        return _namespaces;
    } //-- getNamespace
    
    /**
     * Returns true if this FunctionResolver has a function
     * with the given name.
     *
     * @param namespace of the function
     * @param name the name of the function
     *
     * @returns true if this resolver has a function
    **/
    public boolean hasFunction(String namespace, String name) {
        if (!NAMESPACE.equals(namespace)) return false;
        return ResultTypeFunctionCall.name.equals(name);
    } //-- hasFunction
    
    /**
     * Returns the FunctionCall for the Function with the
     * given name, as defined in FunctionResolver.
     * @return the FunctionCall for the Function with
     * the given name, or null if no Function exists with
     * the given name.
     * @see com.kvisco.xpath.FunctionResolver
    **/
    public XSLTFunction resolveFunction(String namespace, String name) {
        if (!NAMESPACE.equals(namespace)) return null;
        if (ResultTypeFunctionCall.name.equals(name)) {
            return new ResultTypeFunctionCall();
        }
        return null;
    } //-- resolveFunction
    
    
    class ResultTypeFunctionCall 
        extends org.exolab.adaptx.xslt.XSLTFunction
    {
        
        public static final String name = "result-type";
        
        /**
         * Creates a new ResultTypeFunctionCall
        **/
        public ResultTypeFunctionCall() {
            super(name);
        } //-- ResultTypeFunctionCall
        
        /**
         * Invokes the function and returns the XPath result.
         *
         * @param context The XPath context
         * @param params A list of zero or more arguments
         * @return An XPath result (not null)
         * @throws XPathException An error occured while invoking this function
        **/
        public XPathResult call(XPathContext context, XPathResult[] args)
            throws XPathException
        {
            if (args.length != 1)
                throw new XPathException(INVALID_NUMBER_PARAMS+this);
                
            String value = null;
            
            switch (args[0].getResultType()) {
                case XPathResult.BOOLEAN:
                    value = "boolean";
                    break;
                case XPathResult.NODE_SET:
                    value = "node-set";
                    break;
                case XPathResult.NUMBER:
                    value = "number";
                    break;
                case XPathResult.STRING:
                    value = "string";
                    break;
                //case XPathResult.TREE_FRAGMENT:
                    //value = "result-tree-fragment";
                    //break;
                default:
                    value = "undefined";
                    break;
            } 
            return new StringResult(value);
        }
            
    } //-- ResultTypeFunctionCall
    
    /**
     * Used for running the test via the command line
    **/
    public static void main(String[] args) {
        
        String xmlFilename = "extension-functions.xml";
        String outFilename = "result.html";
        org.exolab.adaptx.xslt.XSLTProcessor xslp =
            new org.exolab.adaptx.xslt.XSLTProcessor();
            
        java.io.FileWriter writer = null;
        
        //-- Add a new SampleFunctionResolver to the
        //-- XSLProcessor
        xslp.addFunctionResolver(new SampleFunctionResolver());
        
        try {
            writer = new java.io.FileWriter(outFilename);
            xslp.process(xmlFilename, writer);
            writer.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
    } //-- main
    
} //-- SampleFunctionResolver