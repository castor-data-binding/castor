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

//-- Java
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;

import java.net.URL;
import java.util.Properties;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ArrayList;

//-- Adaptx 
import org.exolab.adaptx.jaxp.transform.sax.TemplatesHandlerImpl;
import org.exolab.adaptx.jaxp.transform.sax.TransformerHandlerImpl;
import org.exolab.adaptx.xslt.XSLTProcessor;
import org.exolab.adaptx.xslt.XSLTReader;
import org.exolab.adaptx.xslt.XSLTStylesheet;
import org.exolab.adaptx.xslt.XSLException;
import org.exolab.adaptx.xslt.util.StylesheetHandler;
import org.exolab.adaptx.xslt.FunctionResolver;


//-- JAXP 1.1
import javax.xml.transform.URIResolver;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;


//-- DOM
import org.w3c.dom.Document;
import org.w3c.dom.Node;

//-- SAX
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.XMLReader;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * An implementation of JAXP 1.1 TransfomerFactory. Please
 * see the JAXP 1.1 documentation for more information on how
 * this class should be used.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class TransformerFactoryImpl 
    extends javax.xml.transform.sax.SAXTransformerFactory
{

    /**
     * Error listener for the TransformerFactory, used when
     * creating a Templates instance from a Source instance.
     * Note: this ErrorListener is not to be passed along 
     * to the Transformer.
     */
    private ErrorListener _errorListener = null;

    /**
     * The URIResolver to be used by the transformer.
     */
    private URIResolverWrapper _uriResolver = null;


    /**
     * List of the attributes on the underlying implementation. 
     */
    private Hashtable _attributes = null;


    /**
     * The list of supported features
     */
    public static final String[] SUPPORTED_FEATURES = {
        DOMResult.FEATURE,
        DOMSource.FEATURE,
        StreamResult.FEATURE,
        StreamSource.FEATURE,        
        SAXResult.FEATURE,
        SAXSource.FEATURE,
        SAXTransformerFactory.FEATURE
    };
    

    /**
     * Creates a new TransformerFactoryImpl
     */
    public TransformerFactoryImpl() {
        super();
    } //-- TransformerFactoryImpl
    
    

    /**
     * Returns the associated stylesheet with the given source document.
     *
     * <p>Refer to the JAXP 1.1 TransformerFactory documentation for
     * more information</p>
     *
     * <p>Note: This method is not yet supported.</p>
     *
     * @throws TransformerConfigurationException.
     */
    public Source getAssociatedStylesheet(Source source, String media, String title, String charset)
        throws TransformerConfigurationException 
    {
        String error = "TransfomerFactory#getAssociatedStylesheet is not supported.";
        throw new TransformerConfigurationException(error);
    } //-- getAssociatedStylesheet


    /**
     * Allows the user to retrieve specific attributes on the underlying
     * implementation.
     *
     * @param name The name of the attribute.
     * @return value The value of the attribute.
     * @throws IllegalArgumentException thrown if the underlying
     * implementation doesn't recognize the attribute.
     */
    public Object getAttribute(String name)
        throws IllegalArgumentException 
    {
        if (_attributes == null) return null;
        return (String)_attributes.get(name);
    } //-- getAttribute

    /**
     * Get the error event handler for the TransformerFactory.
     *
     * @return The current error handler, which should never be null.
     */
    public ErrorListener getErrorListener () {
        return _errorListener;
    } //-- getErrorListener

    /**
     * Look up the value of a feature.
     *
     * <p>The feature name is any absolute URI.</p>
     *
     * @param name The feature name, which is an absolute URI.
     * @return The current state of the feature (true or false).
     */
    public boolean getFeature(String name) {
        for (int i = 0; i < SUPPORTED_FEATURES.length; i++) {
            if (SUPPORTED_FEATURES[i].equals(name)) return true;
        }
        return false;
    } //-- getFeature

    /**
     * Get the object that is used by default during the transformation
     * to resolve URIs used in document(), xsl:import, or xsl:include.
     *
     * @return The URIResolver that was set with setURIResolver.
     */
    public URIResolver getURIResolver() {
        if (_uriResolver != null)
            return _uriResolver.getResolver();
        return null;
    } //-- getURIResolver
    
    
    /**
     * Process the Source into a Templates object, which is a
     * a compiled representation of the source. This Templates object
     * may then be used concurrently across multiple threads.  Creating
     * a Templates object allows the TransformerFactory to do detailed
     * performance optimization of transformation instructions, without
     * penalizing runtime transformation.
     *
     * @param source An object that holds a URL, input stream, etc.
     *
     * @return A Templates object capable of being used for transformation purposes,
     * never null.
     *
     * @exception TransformerConfigurationException May throw this during the parse when it
     *            is constructing the Templates object and fails.
     */
    public Templates newTemplates(Source source)
        throws TransformerConfigurationException 
    {
        
        XSLTReader reader = null;
        if (_uriResolver != null)
            reader = new XSLTReader(_uriResolver);
        else
            reader = new XSLTReader();
            
        XSLTStylesheet stylesheet = null;
        
        if (source instanceof SAXSource) {
            SAXSource saxSource = (SAXSource)source;
            InputSource is = ((SAXSource) source).getInputSource();
            XMLReader xmlReader = saxSource.getXMLReader();
            if (xmlReader != null) {
                StylesheetHandler handler = new StylesheetHandler();
                xmlReader.setContentHandler(handler);
                try {
                    xmlReader.parse(is);
                } 
                catch(IOException iox) {
                    throw new TransformerConfigurationException(iox);
                }
                catch (SAXException sx) {
                    throw new TransformerConfigurationException(sx);
                }
                stylesheet = handler.getStylesheet();
            } 
            else {
                try {
                    stylesheet = reader.read(is);
                }
                catch(IOException iox) {
                    throw new TransformerConfigurationException(iox);
                }
                catch(XSLException xslx) {
                    throw new TransformerConfigurationException(xslx);
                }
            }
        } 
        //-- DOM Document or Node
        else if (source instanceof DOMSource) {
            Node node = ((DOMSource)source).getNode();
            try {
                stylesheet = reader.read(node, source.getSystemId());
            }
            catch(XSLException xsle) {
                throw new TransformerConfigurationException(xsle);
            }
        } 
        //-- InputStream/Reader
        else if (source instanceof StreamSource) {
            StreamSource streamSource = (StreamSource) source;
            try {
                InputSource is = new InputSource(streamSource.getSystemId());
                is.setPublicId(streamSource.getPublicId());
                
                if (streamSource.getInputStream() != null) 
                    is.setByteStream(streamSource.getInputStream());
                else if (streamSource.getReader() != null) 
                    is.setCharacterStream(streamSource.getReader());
                    
                stylesheet = reader.read(is);
            } 
            catch (XSLException xslx) {
                throw new TransformerConfigurationException(xslx);
            } 
            catch (IOException iox) {
                throw new TransformerConfigurationException(iox);
            } 
        }
        else {
            String error = "Source instances of type: " + source.getClass().getName() + 
                " are not supported.";
            throw new TransformerConfigurationException(error);
        }
        
        return new TemplatesImpl(stylesheet);
        
    } //-- newTemplates
    

    /**
     * Process the Source into a Transformer object.  Care must
     * be given not to use this object in multiple threads running concurrently.
     * Different TransformerFactories can be used concurrently by different
     * threads.
     *
     * @param source An object that holds a URI, input stream, etc.
     *
     * @return A Transformer object that may be used to perform a transformation
     * in a single thread, never null.
     *
     * @exception TransformerConfigurationException May throw this during the parse
     *            when it is constructing the Templates object and fails.
     */
    public Transformer newTransformer(Source source)
        throws TransformerConfigurationException 
    {
        if (source == null) return newTransformer();
        return newTemplates(source).newTransformer();
    } //-- newTransformer


    /**
     * Create a new Transformer object that performs a copy
     * of the source to the result.
     *
     * @param source An object that holds a URI, input stream, etc.
     *
     * @return A Transformer object that may be used to perform a transformation
     * in a single thread, never null.
     *
     * @exception TransformerConfigurationException May throw this during
     *            the parse when it is constructing the
     *            Templates object and fails.
     */
    public Transformer newTransformer()
        throws TransformerConfigurationException 
    {
        return new IdentityTransformer();

    } //-- newTransformer



    /**
     * Allows the user to set specific attributes on the underlying
     * implementation.  An attribute in this context is defined to
     * be an option that the implementation provides.
     *
     * @param name The name of the attribute.
     * @param value The value of the attribute.
     * @throws IllegalArgumentException thrown if the underlying
     * implementation doesn't recognize the attribute.
     */
    public void setAttribute(String name, Object value)
        throws IllegalArgumentException 
    {
        if (_attributes == null) {
            _attributes = new Hashtable();
        }
        _attributes.put(name, value);
    }

    /**
     * Set the error event listener for the TransformerFactory, which
     * is used for the processing of transformation instructions,
     * and not for the transformation itself.
     *
     * @param listener The new error listener.
     * @throws IllegalArgumentException if listener is null.
     */
    public void setErrorListener (ErrorListener listener)
        throws IllegalArgumentException 
    {
        if (listener == null)
            throw new IllegalArgumentException("ErrorListener is null");

        _errorListener = listener;
    }
    
    /**
     * Set an object that is used by default during the transformation
     * to resolve URIs used in xsl:import, or xsl:include.
     *
     * @param resolver An object that implements the URIResolver interface,
     * or null.
     */
    public void setURIResolver(URIResolver resolver) {
        if (resolver == null)
            _uriResolver = null;
            
        _uriResolver = new URIResolverWrapper(resolver);
    }

    //-------------------------------------------/
    //- Implementation of SAXTransformerFactory -/
    //-------------------------------------------/

    /**
     * Get a TransformerHandler object that can process SAX
     * ContentHandler events into a Result, based on the transformation
     * instructions specified by the argument.
     *
     * @param src The Source of the transformation instructions.
     *
     * @return TransformerHandler ready to transform SAX events.
     *
     * @throws TransformerConfigurationException If for some reason the
     * TransformerHandler can not be created.
     */
    public TransformerHandler newTransformerHandler(Source source)
        throws TransformerConfigurationException 
    {
        return newTransformerHandler(newTemplates(source));
    } //-- newTransformerHandler


    /**
     * Get a TransformerHandler object that can process SAX
     * ContentHandler events into a Result, based on the Templates argument.
     *
     * @param templates The compiled transformation instructions.
     *
     * @return TransformerHandler ready to transform SAX events.
     *
     * @throws TransformerConfigurationException If for some reason the
     * TransformerHandler can not be created.
     */
    public TransformerHandler newTransformerHandler(Templates templates)
        throws TransformerConfigurationException 
    {
        return new TransformerHandlerImpl(((TemplatesImpl)templates).getStylesheet());
    } //-- newTransformerHandler


    /**
     * Get a TransformerHandler object that can process SAX
     * ContentHandler events into a Result. The transformation
     * is defined as an identity (or copy) transformation, for example
     * to copy a series of SAX parse events into a DOM tree.
     *
     * @return A non-null reference to a TransformerHandler, that may
     * be used as a ContentHandler for SAX parse events.
     *
     * @throws TransformerConfigurationException If for some reason the
     * TransformerHandler cannot be created.
     */
    public TransformerHandler newTransformerHandler()
        throws TransformerConfigurationException 
    {
        return new TransformerHandlerImpl();
    } //-- newTransformerHandler


    /**
     * Get a TemplatesHandler object that can process SAX
     * ContentHandler events into a Templates object.
     *
     * @return A non-null reference to a TransformerHandler, that may
     * be used as a ContentHandler for SAX parse events.
     *
     * @throws TransformerConfigurationException If for some reason the
     * TemplatesHandler cannot be created.
     */
    public TemplatesHandler newTemplatesHandler()
        throws TransformerConfigurationException 
    {
        return new TemplatesHandlerImpl();
    } //-- new TemplatesHandler

    /**
     * Create an XMLFilter that uses the given Source as the
     * transformation instructions.
     *
     * @param src The Source of the transformation instructions.
     *
     * @return An XMLFilter object, or null if this feature is not supported.
     *
     * @throws TransformerConfigurationException If for some reason the
     * TemplatesHandler cannot be created.
     */
    public XMLFilter newXMLFilter(Source source)
        throws TransformerConfigurationException 
    {
        return newXMLFilter(newTemplates(source));
    }

    /**
     * Create an XMLFilter, based on the Templates argument..
     *
     * @param templates The compiled transformation instructions.
     *
     * @return An XMLFilter object, or null if this feature is not supported.
     *
     * @throws TransformerConfigurationException If for some reason the
     * TemplatesHandler cannot be created.
     */
    public XMLFilter newXMLFilter(Templates templates)
        throws TransformerConfigurationException 
    {
        TransformerHandler handler = newTransformerHandler(templates);
        XMLFilterImpl xmlFilter = new XMLFilterImpl();
        xmlFilter.setContentHandler(handler);
        return xmlFilter;
    } //-- newXMLFilter


    
    
} //-- TransformerFactoryImpl
