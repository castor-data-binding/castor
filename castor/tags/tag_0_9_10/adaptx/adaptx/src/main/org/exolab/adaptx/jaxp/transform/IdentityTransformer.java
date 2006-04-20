/*
 * (C) Copyright Keith Visco 2003  All rights reserved.
 *
 * The contents of this file are released under an Open Source 
 * Definition (OSD) compliant license; you may not use this file 
 * execpt in compliance with the license. Please see license.txt, 
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

package org.exolab.adaptx.jaxp.transform;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;

//-- Adaptx imports
import org.exolab.adaptx.net.impl.URIUtils;
import org.exolab.adaptx.xml.parser.DOMParser;
import org.exolab.adaptx.xml.DOM2SAX;
import org.exolab.adaptx.xslt.ResultHandler;
import org.exolab.adaptx.xslt.XSLException;
import org.exolab.adaptx.xslt.dom.Root;
import org.exolab.adaptx.xslt.handlers.DefaultHandler;
import org.exolab.adaptx.xslt.handlers.DOMBuilder;
import org.exolab.adaptx.xslt.handlers.ResultHandlerAdapter2;
import org.exolab.adaptx.xslt.util.Configuration;
import org.exolab.adaptx.xslt.util.SAXInput;
import org.exolab.adaptx.xslt.util.SAX2ResultHandler;

//-- JAXP 1.1
import javax.xml.transform.URIResolver;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.OutputKeys;

//-- DOM
import org.w3c.dom.Document;
import org.w3c.dom.Node;

//-- SAX
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;

/**
 * The implementation of javax.xml.transform.Transformer to
 * allow Adaptx to be compatible with the JAXP 1.1 
 * specification.
 *
 * For information on what this class is supposed to do, 
 * please read the JAXP 1.1 specification.
 * 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class IdentityTransformer 
    extends javax.xml.transform.Transformer
{

    /**
     * The URIResolver to use
     */
    private URIResolver _uriResolver = null;

    /** 
     * The error event handler in effect for the transformation.
     */
    private ErrorListener _errorListener = null;

    /**
     * The output properties to use with this transformation,
     * in Properties format.
     */
    private Properties _outputProperties = null;
     
    /**
     * User defined parameters
     */
    private Hashtable _params = null;
    
    /**
     * Creates a new IdentityTransformer
     */
    public IdentityTransformer() {
        super();
    }  //-- IdentityTransformer

    /**
     * Clear all parameters set with setParameter.
     */
    public void clearParameters() {
        if (_params != null)
            _params.clear();
    } //-- clearParameters

    /**
     * Get the error event handler in effect for the transformation.
     *
     * @return The current error handler, which should never be null.
     */
    public ErrorListener getErrorListener () {
        return _errorListener;
    } //-- getErrorListener
    
    /**
     * Returns the parameter with the given name, that was previously set 
     * with the setParameter or setParameters methods.
     * 
     * <p>Javadoc included from JAXP 1.1 Transfomer:</p>
     *
     * <p>This method does not return a default parameter value, which
     * cannot be determined until the node context is evaluated during
     * the transformation process.
     * 
     * @return A parameter that has been set with setParameter 
     * or setParameters.
     */
    public Object getParameter(String name) {        
        if ((_params == null) || (name == null)) 
            return null;
            
        return _params.get(name);
    } //-- getParameter
  
    /**
     * Returns the URIResolver that will used during processing for
     * resolving URIs. This will be the URIResolver that was previously 
     * set with a call to the setURIResolver method, or null if no 
     * such call was made.
     * 
     * @return the previously set URIResolver, or null if no 
     * URIResolver has been set.
     */
    public URIResolver getURIResolver() {
        return _uriResolver;
    } //-- getURIResolver
    
    /**
     * Returns an Adaptx ResultHandler for the given JAXP Result
     *
     * @return the Adaptx ResultHandler for the given JAXP Result
     * @throws TransformerException if the given Result is not supported, or
     * if any exceptions occur while attempting to create a ResultHandler.
     */
    public static final ResultHandler getResultHandler(Result result) 
        throws TransformerException
    {
        ResultHandler handler = null;
        
        //-- StreamResult
        if (result instanceof StreamResult) {
            StreamResult streamResult = (StreamResult)result;
            Writer writer = null;
            if (streamResult.getOutputStream() != null)
                writer = new OutputStreamWriter(streamResult.getOutputStream());
            else if (streamResult.getWriter() != null) {
                writer = streamResult.getWriter();
            }
            else if (streamResult.getSystemId() != null) {
                try {
                    writer = URIUtils.getWriter(streamResult.getSystemId(), null);
                }
                catch(java.io.IOException iox) {
                    throw new TransformerException(iox);
                }
            }
            else {
                String error = "Invalid StreamResult, missing Writer, or OutputStream.";
                throw new TransformerException(error);
            }
            handler = new DefaultHandler(writer);
        }
        //-- DOMResult
        else if (result instanceof DOMResult) {
            DOMResult domResult = (DOMResult)result;
            Node node = domResult.getNode();
            if (node == null) {
                DOMParser domParser = Configuration.getDOMParser();
                Document document = domParser.createDocument();
                domResult.setNode(document);
                handler = new DOMBuilder(document);
            }
            else {
                handler = new DOMBuilder(node);
            }
        }
        //-- SAXResult
        else if (result instanceof SAXResult) {
            handler = new ResultHandlerAdapter2(((SAXResult)result).getHandler());
        }
        //-- Custom Result type
        else {
            String error = "The Result of type '" + result.getClass().getName();
            error += "' is not supported by this Transformer.";
            throw new TransformerException(error);
        }
        
        return handler;        
    } //-- getResultHandler 
     
    
    /**
     * Set the error event listener in effect for the transformation.
     *
     * @param listener The new error listener.
     * @throws IllegalArgumentException if listener is null.
     */
    public void setErrorListener (ErrorListener listener)
        throws IllegalArgumentException 
    {
        _errorListener = listener;
    } //-- setErrorListener
    
    /**
     * Adds the given parameter (name/value binding) for the 
     * transformation.
     * 
     * <p>Javadoc included from JAXP 1.1 Transfomer:</p>
     *
     * <p>Pass a qualified name as a two-part string, the namespace URI
     * enclosed in curly braces ({}), followed by the local name. If the
     * name has a null URL, the String only contain the local name. An
     * application can safely check for a non-null URI by testing to see if the first
     * character of the name is a '{' character.</p> 
     * <p>For example, if a URI and local name were obtained from an element
     * defined with &lt;xyz:foo xmlns:xyz="http://xyz.foo.com/yada/baz.html"/&gt;,
     * then the TrAX name would be "{http://xyz.foo.com/yada/baz.html}foo". Note that
     * no prefix is used.</p>
     *
     * @param name The name of the parameter, which may begin with a namespace URI
     * in curly braces ({}).
     * @param value The value object.  This can be any valid Java object. It is
     * up to the processor to provide the proper object coersion or to simply
     * pass the object on for use in an extension.
     */
    public void setParameter(String name, Object value) {
        if (name == null) return;        
        if (_params == null) {
            if (value == null) return;
            _params = new Hashtable();
        }
        if (value == null)
            _params.remove(name);
        else
            _params.put(name, value);
    } //-- setParameter
    
    /**
     * Set an object that will be used to resolve URIs encountered
     * during processing.
     * 
     * @param resolver An object that implements the URIResolver interface,
     * or null.
     */
    public void setURIResolver(URIResolver resolver) {
        _uriResolver = resolver;
    } //-- setURIResolver
    
    /**
     * Process the given source tree to the output result.
     * @param xmlSource  The input for the source tree.
     * @param outputTarget The output target.
     *
     * @throws TransformerException If an unrecoverable error occurs 
     * during the course of the transformation.
     */
    public void transform(Source xmlSource, Result outputTarget)
        throws TransformerException 
    {
        
        ResultHandler handler = getResultHandler(outputTarget);

        
        //-- DOMSource
        if (xmlSource instanceof DOMSource) {
            DOMSource domSource = (DOMSource)xmlSource;
            Node node = domSource.getNode();
            boolean setNode = false;
            if (node == null) {
                DOMParser domParser = Configuration.getDOMParser();
                Document document = domParser.createDocument();
                node = document;
            }
            switch (node.getNodeType()) {
                case Node.DOCUMENT_NODE:
                case Node.ELEMENT_NODE:
                    try {
                        DOM2SAX dom2sax = new DOM2SAX();
                        dom2sax.process(node, handler);
                    }
                    catch(SAXException sx) {
                        throw new TransformerException(sx);
                    }
                    break;
                default:
                    String error = "Invalid node type. Only Document " +
                        "and Element nodes are supported as input " +
                        "to a trasformation.";
                    throw new TransformerException(error);
            }
            if (setNode) domSource.setNode(node);
        }
        else {
            InputSource is = null;
            XMLReader reader = null;
            if (xmlSource instanceof StreamSource) {
                StreamSource streamSource = (StreamSource) xmlSource;
                String href = streamSource.getSystemId();
                if (href == null) href = streamSource.getPublicId();
                if (streamSource.getInputStream() != null) 
                    is = new InputSource(streamSource.getInputStream());
                else if (streamSource.getReader() != null) 
                    is = new InputSource(streamSource.getReader());
                else {
                    is = new InputSource(href);
                }
            }
            //-- SAXSource
            else if (xmlSource instanceof SAXSource) {
                SAXSource saxSource = (SAXSource)xmlSource;
                is = saxSource.getInputSource();
                reader = saxSource.getXMLReader();
                if (is == null) {
                    //-- nothing to do!
                    return;
                }
            }
            //-- Custom Source type
            else {
                String error = "The Source of type '" + xmlSource.getClass().getName();
                error += "' is not supported by this Transformer."; 
                throw new TransformerException(error);
            }
            
            if (reader == null) {
                reader = Configuration.getXMLReader();
            }
            try {
                if (handler instanceof ContentHandler) {
                    reader.setContentHandler((ContentHandler)handler);
                }
                else {
                    reader.setContentHandler(new SAX2ResultHandler(handler));
                }
                reader.parse(is);
            }
            catch(SAXException sx) {
                throw new TransformerException(sx);
            }
            catch(IOException iox) {
                throw new TransformerException(iox);
            }
        }
    } //-- transform


    

    /**
     * Set the output properties for the transformation.  These
     * properties will override properties set in the 
     * stylesheet / Templates with xsl:output.
     *
     * <p>Javadoc included from JAXP 1.1 Transfomer:</p>
     *
     * <p>If argument to this function is null, any properties
     * previously set are removed.</p>
     * 
     * <p>Pass a qualified property key name as a two-part string, the namespace URI
     * enclosed in curly braces ({}), followed by the local name. If the
     * name has a null URL, the String only contain the local name. An
     * application can safely check for a non-null URI by testing to see if the first
     * character of the name is a '{' character.</p> 
     * <p>For example, if a URI and local name were obtained from an element
     * defined with &lt;xyz:foo xmlns:xyz="http://xyz.foo.com/yada/baz.html"/&gt;,
     * then the TrAX name would be "{http://xyz.foo.com/yada/baz.html}foo". Note that
     * no prefix is used.</p>
     * 
     * <p>If a given property is not supported, it will be silently ignored.</p>
     *
     * @param properties A set of output properties that will be
     * used to override any of the same properties in effect
     * for the transformation.   
     * 
     * @see javax.xml.transform.OutputKeys
     * @see java.util.Properties
     * 
     * @throws IllegalArgumentException if any of the argument keys are not 
     * recognized and are not namespace qualified.
     */
    public void setOutputProperties(Properties properties)
        throws IllegalArgumentException 
    {
        if (properties == null) {
            _outputProperties = null;
        }
        else {
            _outputProperties = new Properties();
            Enumeration enum = properties.keys();
            while (enum.hasMoreElements()) {
                String name = (String)enum.nextElement();
                String value = properties.getProperty(name);
                _outputProperties.setProperty(name, value);
            }
        }
    } //-- setOutputProperties

    /**
     * Get a copy of the output properties for the transformation.
     * 
     * <p>Javadoc included from JAXP 1.1 Transfomer:</p>
     *
     * <p>The properties should contain a set of layered properties.  The 
     * first "layer" will contain the properties that were set with 
     * setOutputProperties and setOutputProperty.  Subsequent layers 
     * contain the properties set in the stylesheet and the 
     * default properties for the transformation type.
     * There is no guarantee on how the layers are ordered after the 
     * first layer.  Thus, getOutputProperties().getProperty(String key) will obtain any 
     * property in effect for the stylesheet, while 
     * getOutputProperties().get(String key) will only retrieve properties 
     * that were explicitly set with setOutputProperties and setOutputProperty.</p>
     * 
     * <p>Note that mutation of the Properties object returned will not 
     * effect the properties that the transformation contains.</p>
     *
     * @returns A copy of the set of output properties in effect
     * for the next transformation.
     * 
     * @see javax.xml.transform.OutputKeys
     * @see java.util.Properties
     */
    public Properties getOutputProperties() {
        if (_outputProperties == null) 
            return new Properties();
        return new Properties(_outputProperties);
    } //-- getOutputProperties

    /**
     * <p>Javadoc included from JAXP 1.1 Transfomer:</p>
     *
     * Set an output property that will be in effect for the 
     * transformation.
     * 
     * <p>Pass a qualified property name as a two-part string, the namespace URI
     * enclosed in curly braces ({}), followed by the local name. If the
     * name has a null URL, the String only contain the local name. An
     * application can safely check for a non-null URI by testing to see if the first
     * character of the name is a '{' character.</p> 
     * <p>For example, if a URI and local name were obtained from an element
     * defined with &lt;xyz:foo xmlns:xyz="http://xyz.foo.com/yada/baz.html"/&gt;,
     * then the TrAX name would be "{http://xyz.foo.com/yada/baz.html}foo". Note that
     * no prefix is used.</p>
     * 
     * @param name A non-null String that specifies an output 
     * property name, which may be namespace qualified.
     * @param value The non-null string value of the output property.
     *
     * @throws IllegalArgumentException If the property is not supported, and is 
     * not qualified with a namespace.
     * 
     * @see javax.xml.transform.OutputKeys
     */
    public void setOutputProperty(String name, String value)
        throws IllegalArgumentException 
    {
        if (_outputProperties == null) {
            _outputProperties = new Properties();
        }
        _outputProperties.setProperty(name, value);
        
    } //-- setOutputProperty

    /**
     *
     * <p>Javadoc included from JAXP 1.1 Transfomer:</p>
     *    
     * Get an output property that is in effect for the 
     * transformation.  The property specified may be a property 
     * that was set with setOutputProperty, or it may be a 
     * property specified in the stylesheet.
     *
     * @param name A non-null String that specifies an output 
     * property name, which may be namespace qualified.
     *
     * @return The string value of the output property, or null 
     * if no property was found.
     *
     * @throws IllegalArgumentException If the property is not supported.
     * 
     * @see javax.xml.transform.OutputKeys
     */
    public String getOutputProperty(String name)
        throws IllegalArgumentException 
    {
        if (_outputProperties == null) return null;
        return _outputProperties.getProperty(name);
    } //-- getOutputProperty
  
} //-- IdentityTransformer
