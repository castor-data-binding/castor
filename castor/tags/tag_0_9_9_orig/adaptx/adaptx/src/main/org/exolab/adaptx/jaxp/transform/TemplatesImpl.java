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

//-- Java imports
import java.util.Properties;

//-- Adaptx imports
import org.exolab.adaptx.xslt.XSLOutput;
import org.exolab.adaptx.xslt.XSLTStylesheet;

//-- JAXP 1.1
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.Transformer;

//-- SAX
import org.xml.sax.AttributeList;

/**
 * An implementation of javax.xml.transform.Templates for
 * use with Adaptx. For more information on the Templates
 * class please see the JAXP 1.1 documenation.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class TemplatesImpl 
    implements javax.xml.transform.Templates
{
    /**
     * The XSLTStylesheet wrapped by this TemplatesImpl
     */
    private XSLTStylesheet _stylesheet = null;

    /**
     * The default output properties 
     */
    private static final Properties _defaultProps = getDefaultProperties();
    
    /**
     * Creates a new TemplatesImpl for the given stylesheet
     *
     * @param stylesheet the XSLTStylesheet to create the
     * TemplatesImpl for.
     */
    public TemplatesImpl(XSLTStylesheet stylesheet) {
        if (stylesheet == null) {
            String error = "the argument 'stylesheet' must not be null.";
            throw new IllegalArgumentException(error);
        }
        _stylesheet  = stylesheet;
    }  //-- TemplatesImpl

    /**
     * Create a new transformation context for this Templates object.
     *
     * @return A valid non-null instance of a Transformer.
     * @throws TransformerConfigurationException if a Transformer can not be created.
     */
    public Transformer newTransformer() 
        throws TransformerConfigurationException 
    {
        return new TransformerImpl(_stylesheet);
    } //-- newTransformer

    /**
     * Returns the output Properties as specified by the
     * JAXP 1.1 specification, please see the Javadoc for Templates
     * for more information.
     *
     * @return a copy of the output Properties, never null.
     */
    public Properties getOutputProperties() {
        
        Properties props = new Properties((Properties)_defaultProps.clone());
        XSLOutput output = _stylesheet.getOutput();
        AttributeList attributes = output.getAttributes();
        int size = attributes.getLength();
        for (int i = 0; i < size; i++) {
            props.setProperty(attributes.getName(i), attributes.getValue(i));
        }
        return props;
        
    } //-- getOutputProperties

    
    /**
     * Return the XSLTStylesheet being wrapped by this TemplatesImpl
     * 
     * @return the XSLTStylesheet
     */
    public XSLTStylesheet getStylesheet() {
        return _stylesheet;
    } //-- getStylesheet

    /**
     * Returns the default Properties
     *
     * @return the Properties object containing the default properties
     */
    private static final Properties getDefaultProperties() {
        Properties props = new Properties();
        props.setProperty(OutputKeys.INDENT, "no");
        props.setProperty(OutputKeys.VERSION, "1.0");
        props.setProperty(OutputKeys.ENCODING, "UTF-8");
        props.setProperty(OutputKeys.METHOD, "xml");
        props.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        props.setProperty(OutputKeys.STANDALONE, "yes");
        return props;
    } //-- getDefaultProperties
        
} //-- TemplatesImpl
