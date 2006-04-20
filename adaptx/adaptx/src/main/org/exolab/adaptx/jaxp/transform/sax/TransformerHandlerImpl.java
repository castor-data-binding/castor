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

package org.exolab.adaptx.jaxp.transform.sax;

//-- Adaptx
import org.exolab.adaptx.jaxp.transform.TransformerImpl;
import org.exolab.adaptx.net.impl.URILocationImpl;
import org.exolab.adaptx.xslt.XSLTProcessor;
import org.exolab.adaptx.xslt.XSLTStylesheet;
import org.exolab.adaptx.xslt.util.SAXInput;

//-- JAXP
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.TransformerHandler;

//-- SAX
import org.xml.sax.SAXException;
import org.xml.sax.DTDHandler;

/**
 * An implemenation of the JAXP TransformerHandler interface
 *
 * <p>see javax.xml.transform.sax.TransformerHandler 
 * for more information</p>
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class TransformerHandlerImpl extends SAXInput
    implements TransformerHandler 
{  
    
    /**
     * The System Id
     */
    private String _systemId = null;

    /**
     * The Transformer instance
     */
    private TransformerImpl _transformer = null;


    /**
     * Result of the transformation.
     */
    private Result _result = null;


    /**
     * Creates a new TransformerHandlerImpl
     */
    public TransformerHandlerImpl() {
        super();
        _transformer = new TransformerImpl();
        //-- initialize the SAXInput which this class extends
        setProcessor(_transformer.getProcessor());
    } //-- TransformerHandlerImpl

    /**
     * Creates a new TransformerHandlerImpl with the given 
     * XSLTStylesheet to use during the transformation.
     *
     * @param stylesheet the XSLTStylesheet to use during the transformation
     */
    public TransformerHandlerImpl(XSLTStylesheet stylesheet) {
        super();
        _transformer = new TransformerImpl(stylesheet);
        //-- initialize the SAXInput which this class extends
        setProcessor(_transformer.getProcessor());
        setStylesheet(stylesheet);
    } //-- TransformerHandlerImpl

    /**
     * Enables the user of the TransformerHandler to set the
     * to set the Result for the transformation.
     *
     * A result must be set before the events are fired!
     *
     * @param result A Result instance, should not be null.
     * 
     * @throws IllegalArgumentException if result is invalid for some reason.
     */
    public void setResult(Result result)
        throws IllegalArgumentException 
    {
        try {
            setOutputHandler(TransformerImpl.getResultHandler(result));
        }
        catch(TransformerException tx) {
            throw new IllegalArgumentException(tx.getMessage());
        }
    } //-- setResult
    
    /**
     * Set the base ID (URI or system ID) from where relative 
     * URLs will be resolved.
     *
     * @param systemID Base URI for the source tree.
     */
    public void setSystemId(String systemID) {
        _systemId = systemID;
    } //-- setSystemId
    
    /**
     * Get the base ID (URI or system ID) from where relative 
     * URLs will be resolved.
     * @return The systemID that was set with {@link #setSystemId}.
     */
    public String getSystemId() {
        return _systemId;
    } //-- getSystemId
    
    /**
     * Get the Transformer associated with this handler, which 
     * is needed in order to set parameters and output properties.
     */
    public Transformer getTransformer() {
        return _transformer;
    } //-- getTransformer
    
    //---------------------------------/
    //-- Implementation of DTDHandler -/
    //---------------------------------/
    
    public void notationDecl(String name, String publicId, String systemId)
        throws SAXException 
    {
        //-- do nothing, unsupported
    } //-- notationDecl

    public void unparsedEntityDecl
        (String name, String publicId, String systemId, String notationName)
        throws SAXException 
    {
        //-- do nothing, unsupported
    } //-- unparsedEntityDecl


} //-- TransformerHandlerImpl
