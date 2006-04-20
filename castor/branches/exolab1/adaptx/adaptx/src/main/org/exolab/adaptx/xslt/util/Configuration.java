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
package org.exolab.adaptx.xslt.util;


import java.io.*;
import org.exolab.adaptx.xml.parser.DOMParser;
import org.xml.sax.Parser;
import org.xml.sax.helpers.ParserFactory;
import java.util.Properties;
import org.exolab.adaptx.xslt.XSLTProcessor;

/**
 * A simple configuration class for the XSLT processor
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Configuration {



    //-- DEFAULT VALUES
    
    /**
     * The default DOM parser
    **/
    public static final String DEFAULT_PARSER 
        = "org.exolab.adaptx.xml.parser.XercesParser";
        
        
    public static final String DEFAULT_PARSER_NAME
        = "DefaultParser";
        
    /**
     * The DOMParser property name
    **/
    public static final String DOM_PARSER = "parser.dom";
    
    /**
     * The SAX parser property name
    **/
    public static final String SAX_PARSER = "parser.sax";
    
    
    /**
     * The Name of the properties file
    **/
    public  static final String PROPERTIES_FILE   = "adaptx.properties";
    private static final String PROPERTIES_PATH   = "org/exolab/adaptx";
    
    
    private static boolean loaded = false;
    private static Properties props = null;
    
    /**
     * Used to create instances of the DOMParser
    **/
    private static DOMParser _DOMParser = null;
    
    /**
     * Create a new Configuration
    **/
    private Configuration() {
        super();
    } //-- Configuration
    
    /**
     * Returns the DOMParser specified in the properties file,
     * or the one set via a call to #setDOMParser.
     * @return the DOMParser specified in the properties file
    **/
    public static DOMParser getDOMParser() {
        
        if (_DOMParser != null)
            return _DOMParser.copyInstance();
            
        if (!loaded) loadProperties();        
        
	    String parser = getProperty(DOM_PARSER);
	    
	    if (parser == null)
	        props.put(DOM_PARSER, DEFAULT_PARSER);
	        
	    try {
	        Class _DOMParserClass = Class.forName(parser);	            
	        _DOMParser = (DOMParser) _DOMParserClass.newInstance();
	    }
	    catch(Exception ex) {
	        String err = "unable to load DOM parser: " + parser;
	        err += " (" +parser+ ")\n -- ";
	        err += ex.getMessage();
	        err += "\n -- please reconfigure the properties file '";
	        err += PROPERTIES_FILE + "' and try again.";
	        throw new RuntimeException(err);
	    }
	    
        return _DOMParser;
    } //-- getDOMParser
    
    
    /**
     * Returns the property value associated with the given name
     * @return the property with the given name, or null if no
     * property was found.
    **/
    public static String getProperty(String name) {
        if (name == null) return null;
        if (!loaded) loadProperties();
        
        String value = null;
        String resolved = props.getProperty(name);
        while (resolved != null) {
            value = resolved;
            resolved = props.getProperty(resolved);
        }
        return value;
    } //-- getProperty
    
    
    /**
     * Returns the SAX Parser specified in the properties file.
     * If no SAX parser was specified, the default one will be returned.
     * @return the SAX Parser specified in the properties file
    **/
    public static Parser getSAXParser() {
        
        
        Parser parser = null;
        
	    // [arkin] Temporary hack, very bad of me, but the parser factory
	    // breaks on Linux 1.2.2 RC 2.
	    parser = new org.apache.xerces.parsers.SAXParser();
        if (parser != null) return parser;
        // END Temporary hack
        
        String parserName = getProperty(SAX_PARSER);
        
        try {
            if ((parserName != null) && (parserName.length() > 0)) {
                Class parserClass = Class.forName(parserName);
                parser = (Parser)parserClass.newInstance();
            }
        }
        catch(ClassNotFoundException cnfe) {
            //-- do nothing for now
        }
        catch(IllegalAccessException iae) {
            //-- do nothing for now
        }
        catch(InstantiationException ie) {
            //-- do nothing for now
        }
        
        return parser;
    } //-- getSAXParser
    
    /**
     * Sets the DOMParser to return when a call to getDOMParser
     * is made.
     * <br />
     * <b>Note:</b>This is static, so it will be shared with all
     * instances of XSL:P within the same VM. A new instance of
     * the parser will be created for thread safety, but only
     * one type of DOMParser may be used.
     * @param domParser the DOMParser to return when a call
     * to #getDOMParser is made.
    **/
    public static void setDOMParser(DOMParser domParser) {
        _DOMParser = domParser;
    } //-- setDOMParser

    /**
     * Sets the property value associated with the given String.
     * @property the property to set
     * @value the value of the property
     * <BR>See xslp.properties for for a list of properties
    **/
    public static void setProperty(String property, String value) {
        props.put(property, value);
        //-- if neccessary clear DOMParserClass, for next call 
        //-- to getDOMParser;
        if (DOM_PARSER.equals(property))
            _DOMParser = null; 
    } //-- setProperty
    
    //-------------------/
    //- Private Methods -/
    //-------------------/
    
    /** 
     * Loads the given resource
     * @return the InputStream for the given resouce
    **/
    private static InputStream getResourceAsStream(String path) {
        return Configuration.class.getResourceAsStream(path);
    } //-- getResourceAsStream
    
    /**
     * Loads the properties file
    **/
    private static void loadProperties() {
	    props = new Properties();
	    try {
	        InputStream is = null;
	        // look in local working directory
	        File propsFile = new File(PROPERTIES_FILE);
	        if (propsFile.exists())
	            is = new FileInputStream(propsFile);
	        // look in class directory
	        else {
	            
	            propsFile = new File(PROPERTIES_PATH, PROPERTIES_FILE);
	            if (propsFile.exists())
    	            is = new FileInputStream(propsFile);
	            else 
	                is = getResourceAsStream("/"+PROPERTIES_PATH + 
	                    PROPERTIES_FILE);
	        }
	        
	        if (is != null) props.load(is);
	        else createDefaultProperties();

	    }
	    catch(IOException iox) {
	        System.out.println(iox.toString());
	        createDefaultProperties();
	    }
	    loaded = true;
    } //-- loadProperties
    
    /**
     * Creates the default properties...in case the properties
     * File is missing...perhaps we should throw an Exception 
     * instead?
    **/ 
    private static void createDefaultProperties() {
        props = new Properties();        
        //-- add parser information
        props.put(DOM_PARSER, DEFAULT_PARSER);
    } //-- createDefaultProperties
        
} //-- Configuration

