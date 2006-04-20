/*
 * (C) Copyright Keith Visco 1998-2003  All rights reserved.
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
package org.exolab.adaptx.xslt;

import org.exolab.adaptx.net.URIException;
import org.exolab.adaptx.net.URIResolver;
import org.exolab.adaptx.net.URILocation;
import org.exolab.adaptx.net.impl.URILocationImpl;
import org.exolab.adaptx.net.impl.URIResolverImpl;

import org.exolab.adaptx.util.ConsoleDialog;
import org.exolab.adaptx.util.ErrorObserver;
import org.exolab.adaptx.util.ErrorObserverAdapter;
import org.exolab.adaptx.xslt.handlers.DefaultHandler;
import org.exolab.adaptx.xslt.handlers.DOMBuilder;
import org.exolab.adaptx.xslt.util.Configuration;
import org.exolab.adaptx.xslt.util.MessageObserver;
import org.exolab.adaptx.xslt.util.DefaultObserver;
import org.exolab.adaptx.util.CommandLineOptions;
import org.exolab.adaptx.util.List;

//-- XML related imports
import org.exolab.adaptx.xpath.XPathNode;
import org.exolab.adaptx.xml.dom2xpn.DocumentWrapperXPathNode;
import org.exolab.adaptx.xml.parser.DOMParser;
import org.exolab.adaptx.xslt.dom.XPNBuilder;
import org.w3c.dom.*;
import org.xml.sax.Parser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

//-- Java IO/Utilties
import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;




/**
 * This class represents an XSLT Processor that implements
 * the W3C XSLT 1.0 Recommendation.
 *
 * @author <a href="mailto:keith@kvisco.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class XSLTProcessor 
    extends ErrorObserverAdapter
    implements MessageObserver
{
    
    
    /* <command-line-flags> */
    
    /**
     * The flag directive for the help screen
    **/
    public static final String HELP_FLAG        = "h";
    
    /**
     * The flag directive for the xml input file
    **/
    public static final String INPUT_FLAG       = "i";
    
    /**
     * The flag directive for the result tree output file
    **/
    public static final String OUTPUT_FLAG      = "o";
    /**
     * The flag directive for the stylesheet to use
    **/
    public static final String STYLESHEET_FLAG  = "s";
    
    /**
     * The flag directive for the turning on validation
    **/
    public static final String VALIDATE_FLAG    = "val";
    /**
     * The flag directive for displaying the version
    **/
    public static final String VERSION_FLAG     = "v";
    
    /**
     * The flag directive for the error log file
     * -- added by Mohan Embar
    **/
    public static final String ERR_OUTPUT_FLAG  = "e";  
    /* </command-line-flags> */
    
    private final String ERR_PROPERTIES_NOT_FOUND =
        "unable to load properties file";
       
    /* <properties> */
    
    /**
     * Indent Size property name
    **/
    public static final String INDENT_SIZE = "indent-size";
    
    /** 
     * The property prefix to define formatters, e.g.
	 * formatter.text=com.acme.MyFormatter
	 * (added by Franck Mangin)
	 */
	private static final String FORMATTER_PREFIX = "formatter.";
	
	
	
	//------------------/
	//- Error Messages -/
	//------------------/
	
	
	private static final String NULL_HANDLER_ERR =
	    "The ResultHandler passed to #process() must not be null.";
	    
	private static final String NULL_DOCUMENT_ERR = 
	    "The XML Source Document passed to #process() must not be null.";
	    
	private static final String NULL_XML_FILENAME_ERR =
	    "The XML filename, passed to #process(), must not be null.";

	private static final String NULL_XML_LOCATION_ERR =
	    "The XML URILocation, passed to #process(), must not be null.";
	    
	private static final String NULL_XML_NODE_ERR = 
	    "The XML Source Node passed to #process() must not be null.";
	    
    /* </properties> */
    
    
    private final String HTML_RESULT_NS   = "http://www.w3.org/TR/REC-html";
    private final String HTML             = "html";
    private final String DEFAULT_NS       = "";

    /**
     * Name of this app 
    **/
    private static final String appName = "Adaptx";
    
    /**
     * Version of this app 
    **/
    private static final String appVersion = "0.9 (20010716)";
    
    
    /**
     * document base for relative url's
    **/
	private String documentBase = null;
	
	/**
	 * The list of function resolvers for resolving
	 * extension functions
	**/
	private List fnResolvers = null;
	
    /**
     * The list of MessageObservers for this processor
    **/
    private List msgObservers = null;
    
    private Properties params = null;
    
    /**
     * The default MessageObserver
    **/
    private static final DefaultObserver _defaultObserver =
        new DefaultObserver();
    
    /**
     * Flag indicating whether or not to validate when reading an
     * XML document
    **/
    private boolean validate = false;
    
    /**
     * The URIResolver to use for resolving URIs
    **/
    private URIResolver _uriResolver = null;
    
    /**
     * The output properties for the transformation 
     * This will over-ride the XSLOutput from the
     * Stylesheet.
     */
    private XSLOutput _output = null;
    
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * Creates a new XSLTProcessor
    **/
	public XSLTProcessor() {
	    super();
	    params = new Properties();
        //-- initialize message observers
        msgObservers = new List(3);
        msgObservers.add(_defaultObserver);
        addErrorObserver(_defaultObserver);
        fnResolvers = new List(3);
        _uriResolver = new URIResolverImpl();
        
    } //-- XSL:Processor
    
    
      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Adds the given ErrorObserver to the list of ErrorObservers
     * for this processor
     * @param observer the ErrorObserver to add
    **/
    public void addErrorObserver(ErrorObserver observer) {
        if (observer == null) return;
        //-- remove the default observer if necessary
        removeErrorObserver(_defaultObserver);
        super.addErrorObserver(observer);
    } //-- addErrorObserver
    
    /**
     * Adds the given FunctionResolver used for resovling
     * extension functions.
     * @param fnResolver the FunctionResolver to add
     * @see org.exolab.adaptx.xpath.FunctionResolver
    **/
    public void addFunctionResolver(FunctionResolver fnResolver) {
        if (fnResolver != null) {
            fnResolvers.add(fnResolver);
        }
    } //-- addFunctionResolver
    
    /**
     * Adds the given MessageObserver to this processors list
     * of MessageObservers
     * @param msgObserver the MessageObserver to add to this processors
     * list of MessageObservers
    **/
    public void addMessageObserver(MessageObserver msgObserver) {
        msgObservers.add(msgObserver);
    } //-- addMessageObserver
    
    
    /**
	 * Retrieves the name and version of this application
	 * @returns a String with the name and version of this application
	**/
    public static String getAppInfo() {
        return appName + " " + appVersion;
    }

    /**
     * Returns the XSLOutput object containing the
     * output properties. This is used for over-riding
     * any output properties which may appear in the
     * XSLT stylesheet.
     *
     * @return the XSLOutput object, or null if not set.
     */
    public XSLOutput getOutputProperties() {
        return _output;
    } //-- getOutputProperties
    
    /**
     * Returns the value of the top-level parameter 
     * associated with the given name.
     *
     * @param name the name of the top-level parameter whose value
     * should be returned.
     * @return the parameter value
     * @see getParameterNames
     * @see removeParameter
     * @see removeAllParameters
     * @see setParameter
    **/
    public String getParameter(String name) {
        if (name == null) return null;
        return params.getProperty(name);
    } //-- getParameter

    /**
     * Returns an enumeration of all top-level parameter names.
     *
     * @return an enumeration of all top-level parameter names.
     * @see getParameter
     * @see removeParameter
     * @see removeAllParameters
     * @see setParameter
    **/
    public Enumeration getParameterNames() {
        return params.keys();
    } //-- getParameterNames
    
    /**
     * Returns the property value associated with the given String
     * @return the property value associated with the given String
     * <BR>See xslp.properties for for a list of properties
    **/
    public String getProperty(String property) {
        return Configuration.getProperty(property);
    } //-- getProperty
    
    /**
     * Creates a URILocation for the given Reader and Filename.
     * Filename must not be null.
     *
     * @param reader the Reader to create the URILocation for
     * @param filename, the absolute filename for the URILocation
     * @return the new URILocation
    **/
    public static URILocation createURILocation(Reader reader, String filename) {
        return new URILocationImpl(reader, filename);
    } //-- createURILocation
    
	/**
     * Runs this XSLProcessor based on the given arguments. 
     * This method can be called from another Class however,
     * one of the process methods should be more appropriate
     * @param args a list of arguments to this XSLProcessor
     * <BR>
     * Though I do not recommend the following, if you need a static
     * call to XSL:P use the following:<BR>
     * args = {"-i", "xmlfile.xml", "-s", "style.xsl","-o","result.html"}
     * <BR>-- OR --<BR>
     * args = {"-ixmlfile.xml", "-sstyle.xsl","-oresult.html"}
     * <BR>If the Stylesheet is referenced by the <?xml:stylesheet ?>
     * processing instruction use the following<BR>
     * args = {"-ixmlfile.xml","-oresult.html"}
    **/
	public static void main(String args[]) {
	    
        //-- Performance Testing
        /* *
        Runtime rt = Runtime.getRuntime();
        rt.gc();
        System.out.println("Free Memory: " + rt.freeMemory());
        long stime = System.currentTimeMillis();
        /* */
	    
	    
        String xmlFile     = null;
        String outFile     = null;
        String xslFile     = null;
        String fclass      = null;
        String errFile     = null;
        Writer errWriter   = null;
	    
	    List flags = new List(8);
	    flags.add(HELP_FLAG);       //-- help screen
	    flags.add(STYLESHEET_FLAG); //-- stylesheet filename
	    flags.add(OUTPUT_FLAG);     //-- output filename
	    flags.add(INPUT_FLAG);      //-- XML input filename
	    flags.add(VALIDATE_FLAG);   //-- validation
	    flags.add(VERSION_FLAG);    //-- version info
	    flags.add(ERR_OUTPUT_FLAG); //-- error output filename (Mohan)
	    flags.add("d");             //-- Show DOM Package info
	    flags.add("f");             //-- Force processing
	    
	    boolean showDOMPackage = false;
	    
	    Hashtable options = getOptions(args,flags);
	    
	    // -- display usage
	    if ((options == null) || (options.size() == 0)) {
	        printUsage(System.out);
	        return;	        
	    }
	    //-- display help
	    else if (options.containsKey(HELP_FLAG)) {
	        printHelp(System.out);
	        return;
        }
	    //-- display version
	    else if (options.containsKey(VERSION_FLAG)) {
	        System.out.println(appName + " " +appVersion);
	        return;
	    }
	    else {
	        xslFile        = (String)options.get(STYLESHEET_FLAG);
	        xmlFile        = (String)options.get(INPUT_FLAG);
	        outFile        = (String)options.get(OUTPUT_FLAG);
	        showDOMPackage = options.containsKey("d");
	        
	        //-- set error output file (Mohan Embar)
            if ((errFile = (String)options.get(ERR_OUTPUT_FLAG)) != null)
            {
	            try {
	                errWriter = new FileWriter(errFile);
	            }
	            catch (java.io.IOException iox) {
	                printError("Cannot open error file: " + errFile, false); 
	            }
            }
            
            //-- set XML file
	        if (xmlFile == null) {
    	        printError("XML filename missing.", true);            
	        }	        
	        /* I removed this due to a suggestion from
	         * Warren Hedley (Auckland University)
	         * it now dumps to stdout if no file is given
	         */
	        /*
	        else if (outFile == null) {
    	        printError("Output filename missing.", true);	            
	        }
	        */
	    }
	    

	    Writer out = null;
	    if (outFile == null)
	        out = new PrintWriter(System.out); // dump to stdout (WH)
	    else {
	        try {
	            File file = new File(outFile);
	            if (file.exists() && (!options.containsKey("f"))) {
	                ConsoleDialog dialog = new ConsoleDialog();
	                String confirmMsg = "File '" + outFile;
	                confirmMsg += "' already exists! Overwrite?";
	                if (!dialog.confirm(confirmMsg)) {
	                    System.out.println("processing halted!");
	                    return;
	                }
	            }
	            out = new FileWriter(file);
	        }
	        catch (java.io.IOException ex) {
	            System.out.println("XSLProcessor error: " + ex.getMessage());
	        }
	    }
	    
    	XSLTProcessor xslp = new XSLTProcessor();
    	xslp.addErrorObserver(new DefaultObserver(true));
    	xslp.setValidation(options.containsKey(VALIDATE_FLAG));
    	
    	if (showDOMPackage) {
    	    System.out.println(appName + " invoked using: ");
    	    //-- DOM information
    	    System.out.print("parser.dom: ");
    	    DOMParser domParser = Configuration.getDOMParser();
    	    if (domParser == null) System.out.println("<none>");
    	    else System.out.println(domParser.getClass().getName());
    	    //-- SAX information
    	    System.out.print("parser.sax: ");
    	    Parser saxParser = Configuration.getSAXParser();
    	    if (saxParser == null) System.out.println("<none>");
    	    else System.out.println(saxParser.getClass().getName());
    	}
    	
    	//-- add error observer if necessary
    	if (errWriter != null) {
    	    xslp.addErrorObserver(new DefaultObserver(errWriter));
    	}
    	
    	xslp.process(xmlFile, xslFile, out);
    	
    	try {
	        if (errWriter != null) errWriter.close();
	        if (out != null) {
	            out.flush();
	            out.close();
	        }
	    }
	    catch(java.io.IOException ioe) {};
	   
	    /* <performance-testing> *
	    System.out.println();
	    System.out.print("Total XSL:P/ Time: ");
	    System.out.print(System.currentTimeMillis()-stime);
	    System.out.println(" (ms)\n");
	    /* </performance-testing> */
	}  //-- main

    /**
     * Processes the specified xml file, using the stylesheet specified 
     * by the xml stylesheet PI, and the default ResultHandler. 
     * All results are sent to the Writer.
     *
     * @param xmlFilename the path to the XML file to process
     * @param out the Writer to print all processing results to.
    **/
    public void process(String xmlFilename, Writer out) {
        String xslFilename = null;
        process(xmlFilename, xslFilename, out);
    } //-- process(String, Writer)

    /**
     * Processes the specified xml file, using the stylesheet specified 
     * by the xml stylesheet PI, and returns the resulting document.
     *
     * @param xmlFilename the path to the XML file to process
     * @returns the resulting Document
    **/
    public Document process(String xmlFilename) {
        String xslFile = null;
        return process(xmlFilename, xslFile);
    } //-- process(String)
    
    /**
     * Processes the specified xml file, using the specified xsl file, and
     * the default ResultHandler. 
     * All results are sent to the given Writer.
     *
     * @param xmlFilename the href to the XML file to process
     * @param xslFilename the href to the XSL file to use for processing.
     * This stylesheet will supercede any embedded stylesheets in the
     * xsl document. Set to null, to allow xml:stylesheet PI to be processed.
     * @param out the Writer to print all processing results to.
    **/
    public void process
        (String xmlFilename, String xslFilename, Writer out) 
    {
        process(xmlFilename, xslFilename, new DefaultHandler(out));
    } //-- process(String, String, Writer)
            
    
    /**
     * Processes the specified xml file, using the specified xsl file, and
     * the default ResultHandler. 
     * All results are sent to the given Writer.
     *
     * @param xmlFilename the href to the XML file to process
     * @param stylesheet the XSLStylesheet to use for processing
     * This stylesheet will supercede any embedded stylesheets in the
     * xsl document. Set to null, to allow xml:stylesheet PI to be processed.
     * @param out the Writer to print all processing results to.
    **/
    public void process
        (String xmlFilename, XSLTStylesheet stylesheet, Writer out) 
    {
        process(xmlFilename, stylesheet, new DefaultHandler(out));
    } //-- process(String, XSLStylesheet, Writer)
    
    /**
     * Processes the specified xml file, using the specified xsl file, and
     * the desired ResultHandler. 
     * All results are sent to the PrintWriter.
     *
     * @param xmlFilename the path to the XML file to process
     * @param xslFilename the path to the XSL file to use for processing.
     * This stylesheet will supercede any embedded stylesheets in the
     * xsl document.
     * @param handler the ResultHandler which handles the result tree.
    **/    
    public void process
        (String xmlFilename, String xslFilename, ResultHandler handler)
    {
        
        if (handler == null) {
            throw new IllegalArgumentException(NULL_HANDLER_ERR); 
        }
        
        URILocation xmlLocation = null;
        try {
            xmlLocation = _uriResolver.resolve(xmlFilename, documentBase);
        }
        catch (URIException exception) {
            String err = "Error reading stylesheet: " + 
                exception.getMessage();
            err += "\n -- processing with default rules";
            receiveError(err);
        }
        URILocation xslLocation = null;
        
        // Create XSL InputStream
        if ((xslFilename != null) && (xslFilename.length() > 0)) {            
            try {
                xslLocation = _uriResolver.resolve(xslFilename, documentBase);
            }
            catch(URIException ex) {
                StringBuffer err = new StringBuffer();
                err.append("Error reading stylesheet: ");
                err.append(ex.getMessage());
                err.append("\n -- processing with default rules");
                receiveError(err.toString());
            }
        }
        
		XSLPIHandler piHandler = new XSLPIHandler();		
		piHandler.setDocumentBase(xmlLocation.getBaseURI());
		
		XPathNode source = readXMLDocument(xmlLocation);
		if (source == null) {
		    String err = "unable to read XML document, processing halted.";
		    receiveError(err, ErrorObserver.FATAL);
		    return;
		}
		parsePIs(source, piHandler);
	    XSLTStylesheet xsl = readXSLStylesheet(xslLocation,piHandler);
	    process(source, xsl, handler);
	    
    } //--process
         
    /**
     * Processes the specified xml file, using the specified xsl file, and
     * the desired ResultHandler. 
     * All results are sent to the PrintWriter.
     *
     * @param xmlFilename the path to the XML file to process
     * @param stylesheet the XSLStylesheet to use for processing.
     * This stylesheet will supercede any embedded stylesheets in the
     * xsl document.
     * @param handler the ResultHandler which handles the result tree.
    **/    
    public void process
        (String xmlFilename, XSLTStylesheet stylesheet, ResultHandler handler) 
    {
        
        if (handler == null) {
            throw new IllegalArgumentException(NULL_HANDLER_ERR); 
        }
        
        URILocation location = null;
        try {
            location = _uriResolver.resolve(xmlFilename, documentBase);
        }
        catch(Exception e) {
            String err = "Unable to read XML document: " + xmlFilename;
            err += "; " + e.getMessage();
            receiveError(err, ErrorObserver.FATAL);
            return;           
        }        
		XPathNode source = readXMLDocument(location);
	    process(source, stylesheet, handler);
	    
    } //--process

    /**
     * Processes the specified xml file, using the specified xsl file. 
     * @param xmlFilename the path to the XML file to process
     * @param xslFilename the path to the XSL file to use for processing.
     * This stylesheet will supercede any embedded stylesheets in the
     * xsl document.
     * @return the resulting Document
    **/    
    public Document process(String xmlFilename, String xslFilename) {
        
        if (xmlFilename == null) 
            throw new IllegalArgumentException(NULL_XML_FILENAME_ERR);
            
        URILocation xmlLocation = null;
        URILocation xslLocation = null;
        try {
            xmlLocation = 
                _uriResolver.resolve(xmlFilename, documentBase);
            xslLocation = 
                _uriResolver.resolve(xslFilename, xmlLocation.getBaseURI());
        }
        catch(Exception e) {
            receiveError(e, "processing aborted", ErrorObserver.FATAL);
            return null;           
        }        
        
		DOMParser domParser = Configuration.getDOMParser();
		Document result = domParser.createDocument();
		DOMBuilder domBuilder = new DOMBuilder(result);
        process(xmlLocation, xslLocation, domBuilder);
	    return result;
	    
    } //-- process

    /**
     * Processes the specified xml URILocation, using the specified xslt 
     * stylesheet URILocation, and the desired ResultHandler.
     *
     * @param xmlLocation the URILocation for the input XML source
     * @param xslLocation the URILocation for the stylesheet to use. 
     * If present this stylesheet will supercede any embedded stylesheets 
     * in the xml document. Set to null, to allow the xml:stylesheet PI to 
     * be processed.
     *
     * @return the resulting DOM Document
    **/    
	public Document process(URILocation xmlLocation, URILocation xslLocation) {
	    
	    if (xmlLocation == null)
	        throw new IllegalArgumentException(NULL_XML_LOCATION_ERR);
	    
		XPathNode source = readXMLDocument(xmlLocation);
		XSLPIHandler piHandler = new XSLPIHandler();
		piHandler.setDocumentBase(xmlLocation.getBaseURI());
		parsePIs(source,piHandler);
	    XSLTStylesheet xsl = readXSLStylesheet(xslLocation,piHandler);
	    
		DOMParser domParser = Configuration.getDOMParser();
		Document result = domParser.createDocument();
		DOMBuilder domBuilder = new DOMBuilder(result);
        process(source, xsl, domBuilder);
        
        return result;
        
	} //--process
	

    /**
     * Processes the specified xml URILocation, using the specified xslt 
     * stylesheet URILocation, and the desired ResultHandler.
     *
     * @param xmlLocation the URILocation for the input XML source
     * @param xslLocation the URILocation for the stylesheet to use. 
     * If present this stylesheet will supercede any embedded stylesheets 
     * in the xml document. Set to null, to allow the xml:stylesheet PI to 
     * be processed.
     * @param handler the ResultHandler to use for processing the stylesheet
     */    
	public void process
	    (URILocation xmlLocation, URILocation xslLocation, ResultHandler handler) 
	{
		                    
        if (handler == null)
            throw new IllegalArgumentException(NULL_HANDLER_ERR);
            
	    if (xmlLocation == null)
	        throw new IllegalArgumentException(NULL_XML_LOCATION_ERR);
	        
		XPathNode source = readXMLDocument(xmlLocation);
		XSLPIHandler piHandler = new XSLPIHandler();
		piHandler.setDocumentBase(xmlLocation.getBaseURI());
		parsePIs(source,piHandler);
	    XSLTStylesheet xsl = readXSLStylesheet(xslLocation,piHandler);	    
		process(source, xsl, handler);
    } //-- process

    /**
     * Processes the specified xml URILocation, using the specified xslt 
     * stylesheet, and the desired ResultHandler.
     *
     * @param xmlLocation the URILocation for the input XML source
     * @param stylesheet the XSLTStylesheet to use (may be null). 
     * If present this stylesheet will supercede any embedded stylesheets 
     * in the xml document. Set to null, to allow the xml:stylesheet PI to 
     * be processed.
     * @param handler the ResultHandler to use for processing the stylesheet
     */    
	public void process
	    (URILocation xmlLocation, XSLTStylesheet stylesheet, ResultHandler handler) 
	{
		                    
        if (handler == null)
            throw new IllegalArgumentException(NULL_HANDLER_ERR);
            
	    if (xmlLocation == null)
	        throw new IllegalArgumentException(NULL_XML_LOCATION_ERR);
	        
		XPathNode source = readXMLDocument(xmlLocation);
		
        if (stylesheet == null) {
            // look for stylesheet PI
		    XSLPIHandler piHandler = new XSLPIHandler();
		    piHandler.setDocumentBase(xmlLocation.getBaseURI());
		    parsePIs(source,piHandler);
		    stylesheet = readXSLStylesheet(null, piHandler);
        }
		process(source, stylesheet, handler);
    } //-- process
    
    
    /**
     * Processes the specified xml (DOM) Document, using the specified 
     * (DOM) xsl stylesheet.
     * @param xmlDocument the XML Document to process
     * @param xslDocument the XSL Document to use for processing.
     * @return the resulting Document
    **/    
    public Document process(Document xmlDocument, Document xslDocument) {
        
        
        if (xmlDocument == null) 
            throw new IllegalArgumentException(NULL_DOCUMENT_ERR);
            
        XSLTStylesheet xslStylesheet = null;
        if (xslDocument == null) {
		    XSLPIHandler piHandler = new XSLPIHandler();
		    parsePIs(xmlDocument,piHandler);
		    xslStylesheet = readXSLStylesheet(null, piHandler);
        }
        else xslStylesheet = readXSLStylesheet(xslDocument,"XSL Document");
        return process(xmlDocument, xslStylesheet);
    } //--process

    /**
     * Processes the specified xml (DOM) Document, using the specified 
     * xsl (DOM) Document and the default ResultHandler. 
     * All results are sent to the specified PrintWriter.
     *
     * @param xmlDocument the XML Document to process
     * @param xslDocument the XSL Document to use for processing.
     * @param out the Writer to print all processing results to.
    **/    
    public void process(Document xmlDocument, 
                        Document xslDocument,
                        Writer out) 
    {
        process(xmlDocument, xslDocument, new DefaultHandler(out));
    } //-- process
    
    /**
     * Processes the specified xml (DOM) Document, using the specified 
     * xsl (DOM) Document and the desired ResultHandler. 
     *
     * All results are sent to the specified PrintWriter.
     * @param xmlDocument the XML Document to process
     * @param xslDocument the XSL Document to use for processing.
     * @param out the PrintWriter to print all processing results to.
     * @param handler the desired ResultHandler to use during processing
    **/        
    public void process
        (Document xmlDocument, Document xslDocument, ResultHandler handler) 
    {
        
        if (handler == null)
            throw new IllegalArgumentException(NULL_HANDLER_ERR);
            
        if (xmlDocument == null) 
            throw new IllegalArgumentException(NULL_DOCUMENT_ERR);
            
        XSLTStylesheet xslStylesheet = null;
        if (xslDocument == null) {
            // look for stylesheet PI
		    XSLPIHandler piHandler = new XSLPIHandler();
		    parsePIs(xmlDocument,piHandler);
		    xslStylesheet = readXSLStylesheet(null, piHandler);
        }
        else xslStylesheet = readXSLStylesheet(xslDocument,"XSL Document");
        process(xmlDocument, xslStylesheet, handler);
    } //-- process
    
    
    /**
     * Processes the specified xml (DOM) Document, using the specified xsl 
     * stylesheet.
     *
     * @param xmlDocument the XML Document to process
     * @param stylesheet the XSLStylesheet to use for processing.
     * @return the resulting Document
    **/    
    public XPathNode process(XPathNode source, XSLTStylesheet stylesheet) {
        
        
        if (source == null) 
            throw new IllegalArgumentException(NULL_XML_NODE_ERR);
            
        if (stylesheet == null) {
            // look for stylesheet PI
		    XSLPIHandler piHandler = new XSLPIHandler();
		    parsePIs(source,piHandler);
		    stylesheet = readXSLStylesheet(null, piHandler);
        }
        
        RuleProcessor rp = new RuleProcessor(stylesheet);
        rp.setURIResolver(_uriResolver);
        copyParams(rp);
        rp.addMessageObserver(this);
		rp.addErrorObserver(this);		
		//-- copy FunctionResolvers
		for (int i = 0; i < fnResolvers.size(); i++)
		    rp.addFunctionResolver((FunctionResolver)fnResolvers.get(i));
		
		XPNBuilder xpnBuilder = new XPNBuilder();
		rp.process(source, xpnBuilder);
		return xpnBuilder.getRoot();
		
    } //--process
    
    
    /**
     * Processes the specified xml (DOM) Document, using the specified xsl 
     * stylesheet.
     *
     * @param xmlDocument the XML Document to process
     * @param stylesheet the XSLStylesheet to use for processing.
     * @return the resulting Document
    **/    
    public Document process(Document xmlDocument, XSLTStylesheet stylesheet) {
        
        if (xmlDocument == null) 
            throw new IllegalArgumentException(NULL_DOCUMENT_ERR);
            
        if (stylesheet == null) {
            // look for stylesheet PI
		    XSLPIHandler piHandler = new XSLPIHandler();
		    parsePIs(xmlDocument,piHandler);
		    stylesheet = readXSLStylesheet(null, piHandler);
        }
        
        RuleProcessor rp = new RuleProcessor(stylesheet);
        rp.setURIResolver(_uriResolver);
        copyParams(rp);
        rp.addMessageObserver(this);
		rp.addErrorObserver(this);		
		//-- copy FunctionResolvers
		for (int i = 0; i < fnResolvers.size(); i++)
		    rp.addFunctionResolver((FunctionResolver)fnResolvers.get(i));
		
		DOMParser domParser = Configuration.getDOMParser();
		Document result = domParser.createDocument();
		DOMBuilder domBuilder = new DOMBuilder(result);
		rp.process(new DocumentWrapperXPathNode(xmlDocument), domBuilder);
		return result;
    } //--process


    /**
     * Processes the specified xml (DOM) Document, using the specified xsl 
     * stylesheet, and the default ResultHandler. 
     * All results are sent to the specified Writer.
     *
     * @param source the XML source node (XPathNode) to process
     * @param stylesheet the XSLStylesheet to use for processing.
     * @param out the Writer to print all processing results to.
    **/    
    public void process
        (XPathNode source, XSLTStylesheet stylesheet, Writer out) 
    {
        ResultHandler handler = null;
        
        if (stylesheet != null)
            handler = new DefaultHandler(out, getOutputProperties(stylesheet));
        else
            handler = new DefaultHandler(out);
            
        process(source, stylesheet, handler);
    } //-- process
    
    /**
     * Processes the specified xml (DOM) Document, using the specified xsl 
     * stylesheet, and the default ResultHandler. 
     *
     * All results are sent to the specified PrintWriter.
     * @param xmlDocument the XML Document to process
     * @param stylesheet the XSLStylesheet to use for processing.
     * @param out the PrintWriter to print all processing results to.
    **/    
    public void process
        (Document xmlDocument, XSLTStylesheet stylesheet, Writer out) 
    {
        ResultHandler handler = null;
        
        if (stylesheet != null)
            handler = new DefaultHandler(out, getOutputProperties(stylesheet));
        else
            handler = new DefaultHandler(out);
            
        XPathNode source = new DocumentWrapperXPathNode(xmlDocument);
        process(source, stylesheet, handler);
    } //-- process
    
    /**
     * Processes the specified xml source node (XPathNode), 
     * using the specified xsl stylesheet, and the desired ResultHandler. 
     * All results are sent to the specified ResultHandler.
     *
     * @param source the XML source node to process
     * @param stylesheet the XSLStylesheet to use for processing.
     * @param handler the ResultHandler to handle the result.
    **/        
    public void process
        (XPathNode source, XSLTStylesheet stylesheet, ResultHandler handler) 
    {
        
        if (handler == null)
            throw new IllegalArgumentException(NULL_HANDLER_ERR);
           
        if (source == null) 
            throw new IllegalArgumentException(NULL_XML_NODE_ERR);
            
        if (stylesheet == null) {
            // look for stylesheet PI
		    XSLPIHandler piHandler = new XSLPIHandler();
		    parsePIs(source,piHandler);
		    stylesheet = readXSLStylesheet(null, piHandler);
        }
           
        handler.setOutputFormat(getOutputProperties(stylesheet));
            
        //-- Set indentation from Properties file
        //-- (suggested by Aaron Metzger)
        String indentSize = getProperty(INDENT_SIZE);
        if (indentSize != null) {
            try {
                handler.setIndentSize((short)Integer.parseInt(indentSize));
            }
            catch(NumberFormatException nfe) {};
        }
        RuleProcessor rp = new RuleProcessor(stylesheet);
        rp.setURIResolver(_uriResolver);
        copyParams(rp);
        rp.addMessageObserver(this);
        rp.addErrorObserver(this);        
		//-- copy FunctionResolvers
		for (int i = 0; i < fnResolvers.size(); i++)
		    rp.addFunctionResolver((FunctionResolver)fnResolvers.get(i));
		rp.process(source, handler);
    } //-- process
    
    
    /**
     * Processes the specified xml (DOM) Document, using the specified xsl 
     * stylesheet, and the desired ResultHandler. 
     * All results are sent to the specified ResultHandler.
     *
     * @param xmlDocument the XML Document to process
     * @param stylesheet the XSLStylesheet to use for processing.
     * @param handler the desired ResultHandler to use during processing
    **/        
    public void process
        (Document xmlDocument, XSLTStylesheet stylesheet, ResultHandler handler) 
    {
        XPathNode source = new DocumentWrapperXPathNode(xmlDocument);
        process(source, stylesheet, handler);
    } //-- process
    
    
    
    /**
     * Recieves a message
     * @param message the message to recieve
     * @see org.exolab.adaptx.xslt.util.MessageObserver
    **/
    public void receiveMessage(String message) {
        for (int i = 0; i < msgObservers.size(); i++) {
            ((MessageObserver)msgObservers.get(i)).receiveMessage(message);
        }
    } //-- recieveMessage
    
    /**
     * Removes the given FunctionResolver from the list of
     * extension function resolvers.
     * @param fnResolver the FunctionResolver to remove
     * @see org.exolab.adaptx.xpath.FunctionResolver
    **/
    public void removeFunctionResolver(FunctionResolver fnResolver) {
        fnResolvers.remove(fnResolver);
    } //-- removeFunctionResolver
    
    /**
     * Removes the given MessageObserver from this processors list
     * of MessageObservers
     * @param msgObserver the MessageObserver to remove from this processors
     * list of MessageObservers
     * @return the given MessageObserver if it was removed from the list,
     * otherwise return null
    **/
    public MessageObserver removeMessageObserver
        (MessageObserver msgObserver) 
    {
        if (msgObservers.remove(msgObserver)) return msgObserver;
        else return null;
    } //-- addMessageObserver

    /**
     * Removes the top-level parameter binding with the given name
     *
     * @param name the name of the parameter binding to remove
     * @see removeAllParameters
     * @see setParameter
    **/
    public void removeParameter(String name) {
        if (name == null) return;
        params.remove(name);
    } //-- removeParameter
    
    /**
     * Removes all the top-level parameter bindings
     *
     * @see removeParameter
     * @see setParameter
    **/
    public void removeAllParameters() {
        params.clear();
    } //-- removeAllParameters
    
    /**
     * Sets the document base for resolving relative URLs
     * @param documentBase the document base to use while processing.
    **/
    public void setDocumentBase(String documentBase) {
        this.documentBase = documentBase;
    }
    
    /**
     * Sets the DOMParser that will be used to read in XML Documents
     * @param domParser the DOMParser that is to be used to read in
     * XML Documents
     * <br />
     * <b>Note:</b>This will make a call to Configuration#setDOMParser
     * which is static, so this DOMParser will be shared by all
     * instances of XSL:P running in the same VM.
    **/
    public void setDOMParser(DOMParser domParser) {
        Configuration.setDOMParser(domParser);
    } //-- setDOMParser
    
    /**
     * Sets the XSLOutput object containing the
     * output properties. This is used for over-riding
     * any output properties which may appear in the
     * XSLT stylesheet.
     *
     * @param output the XSLOutput object to set.
     */
    public void setOutputProperties(XSLOutput output) {
        _output = output;
    } //-- setOutputProperties
    
    /**
     * Sets the property value associated with the given String.
     * @property the property to set
     * @value the value of the property
     * <BR>See xslp.properties for for a list of properties
    **/
    public void setProperty(String property, String value) {
        Configuration.setProperty(property, value);
    } //-- setProperty
    
    /**
     * Sets a parameter which may be accessed using a top-level
     * parameter in the XSLT Stylesheet.
     * @param name the name of the parameter
     * @param value the value of the parameter
    **/
    public void setParameter(String name, String value) {
        if ((name == null) || (value == null)) return;
        if (name.length() > 0) {
            params.put(name, value);
        }
    } //-- setParameter
    
    /**
     * Sets the URIResolver for resolving all URIs. If null,
     * the default URIResolver will be used.
     *
     * @param resolver the URIResolver to use
    **/
    public void setURIResolver(URIResolver resolver) {
        if (resolver != null) 
            _uriResolver = resolver;
        else
            _uriResolver = new URIResolverImpl();
    } //-- setURIResolver
    
    /**
     * Sets whether or not to validate when reading an XML document.
     * <b>Note:</b>This will turn on validation for the DOMParser
     * returned by Configuration#getDOMParser(),
     * which is static, so this validation (on/off) will be shared by all
     * instances of XSL:P running in the same VM.
     * @param validate the boolean indicating whether to validate or not
     * @since 19990408 
    **/
    public void setValidation(boolean validate) {
        this.validate = validate;
        DOMParser domParser = Configuration.getDOMParser();
        if (domParser != null) 
            domParser.setValidation(validate);
    } //-- setValidation
    
      //-------------------/
     //- Private Methods -/
	//-------------------/
	
	/**
	 * Copies parameters set via a call to #setParameter to the
	 * given RuleProcessor
	 * @param ruleProcessor the RuleProcessor to copy parameters to
	**/
	private void copyParams(RuleProcessor ruleProcessor) {
	    Enumeration enum = params.keys();
	    while (enum.hasMoreElements()) {
	        String name = (String)enum.nextElement();
	        ruleProcessor.setParameter(name, params.getProperty(name));
	    }
	} //-- copyParams
	
	/**
	 * parses the arguments into a hashtable with the proper flag
	 * as the key
	**/
	private static Hashtable getOptions(String[] args, List flags) {
	    Hashtable options = new Hashtable();
	    String flag = null;
	    for (int i = 0; i < args.length; i++) {
	        
	        if (args[i].startsWith("-")) {
	            	
	            // clean up previous flag
	            if (flag != null) {
	                options.put(flag,args[i]);
	                options.put(new Integer(i),args[i]);
	            }
	            // get next flag
	            flag = args[i].substring(1);
	            
	            //-- check full flag, otherwise try to find
	            //-- flag within string
	            if (!flags.contains(flag)) {
	                int idx = 1;
	                while(idx <= flag.length()) {
	                    if (flags.contains(flag.substring(0,idx))) {
	                        if (idx < flag.length()) {
	                            options.put(flag.substring(0,idx),
	                                flag.substring(idx));
	                            break;
	                        }
	                    }
	                    else if (idx == flag.length()) {
	                        printError("invalid option: -" + flag, true);
	                    }
	                    ++idx;
	                }// end while
	            }
	            
	        }// if flag
	        else {
	            // Store both flag key and number key
	            if (flag != null) options.put(flag,args[i]);
	            options.put(new Integer(i),args[i]);
	            flag = null;
	        }
	        
	    }// end for
	    if (flag != null) options.put(flag, "no value");
	    return options;
	} //-- getOptions

    /**
     * Returns the XSLOutput properties object to use when
     * processing the given stylesheet. A null object may
     * be returned.
     *
     * @return the XSLOutput object to use during processing
     * the given stylesheet
     */
    private XSLOutput getOutputProperties(XSLTStylesheet stylesheet) {
        
        if ((stylesheet == null) || (stylesheet.getOutput() == null))
            return _output;
            
        if (_output == null) return stylesheet.getOutput();
        
        XSLOutput output = stylesheet.getOutput().copy();
        output.merge(_output);
        
        return output;
    } //-- getOutputProperties
    
    /**
     * Retrieves Processing Instructions from the given document
     * and hands them off to the given XSLPIHandler
    **/
    private void parsePIs
        (Document document, XSLPIHandler piHandler) {
        if ((document == null) || (piHandler == null)) return;
        
        NodeList nl    = document.getChildNodes();
        Node node;
        
        for (int i = 0; i < nl.getLength(); i++) {
            node = nl.item(i);
            if (node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
                ProcessingInstruction pi = (ProcessingInstruction)node;
                piHandler.handlePI(pi.getTarget(), pi.getData());
            }
        }
    } //-- parsePIs

    /**
     * Retrieves Processing Instructions from the given document
     * and hands them off to the given XSLPIHandler
    **/
    private void parsePIs
        (XPathNode source, XSLPIHandler piHandler) {
        if ((source == null) || (piHandler == null)) return;
        
        //-- make sure we are using the root node
        XPathNode node = source.getRootNode();
        
        node = node.getFirstChild();
        
        while(node != null) {
            if (node.getNodeType() == XPathNode.PI) {
                piHandler.handlePI(node.getLocalName(), node.getStringValue());
            }
            node = node.getNext();
        }
    } //-- parsePIs
    
	/**
	 * prints the usage information for this application
	 * @param ps the PrintStream to print the usage information to
	**/
	private static void printUsage(PrintStream ps) {
	    printUsage(ps,true);
	} //-- printUsage
	
	/**
	 * prints the usage information for this application
	 * @param ps the PrintStream to print the usage information to
	 * @param showAppInfo boolean to indicate whether or not to show
	 * the name and version of the application with the usage. If true
	 * the appInfo will be shown.
	**/
	private static void printUsage(PrintStream ps, boolean showAppInfo) {
	    
	    ps.println();
	    if (showAppInfo) ps.println(getAppInfo());
	    ps.println("usage:");
	    ps.print("java org.exolab.adaptx.xslt.XSLProcessor -");
	    ps.print(INPUT_FLAG + " xml-file [-");
	    ps.print(STYLESHEET_FLAG + " xsl-file] [-");
	    ps.println(OUTPUT_FLAG + " output-file] [-val][-f]");
	    ps.println();
	    ps.println("for more infomation use the -" + HELP_FLAG + " flag");
	} //-- printUsage
	
	/**
	 * prints the Help for this application
	 * @param ps the PrintStream to print the help information to
	**/
	private static void printHelp(PrintStream ps) {
	    ps.println();
	    ps.println(getAppInfo());
	    ps.print("The following flags are available for use with ");
	    ps.println(appName + " -");
	    ps.println();
	    ps.println("-i  filename       : The XML file to process");
	    ps.println("-o  filename       : The Output file to create");	    
	    ps.println("-s  filename       : The XSL file to use for processing  (Optional)");
	    ps.println("-handler classname : Formatter (for Formatting Objects)  (Optional)");
	    ps.println("-val               : Turns on xml validation if using    (Optional)");
	    ps.println("                     a validating DOM parser");
	    ps.println("-f                 : Force overwriting output file       (Optional)");
	    ps.println("-h                 : This help screen                    (Optional)");
	    ps.println();	    	    
	} // printHelp
	
	/**
	 * Prints an error message to the screen and if specified terminates
	 * the application.
	 * @param message the error message to display
	 * @param exit a boolean indicating whether to exit the application. If
	 * true, the application is terminated.
	**/
	private static void printError(String message, boolean exit) {
	    System.out.println();
	    System.out.println(getAppInfo());
	    System.out.println("#error: " + message);
	    printUsage(System.out, false);
	    if (exit) System.exit(0);
	} //--printError
	
	/**
	 * Reads an XML Document into an XPathNode from the given URILocation
	 *
	 * @return the XPathNode
	**/
	private XPathNode readXMLDocument(URILocation location) {
	        
		Parser parser = Configuration.getSAXParser();
		if (parser == null) {
		    receiveError("unable to create SAX parser. ",
		        ErrorObserver.FATAL);
		    return null;
		}
		XPNBuilder builder = new XPNBuilder(location.getAbsoluteURI());
		
        try {
            InputSource source = new InputSource();
            source.setSystemId(location.getAbsoluteURI());
            source.setCharacterStream(location.getReader());
            parser.setDocumentHandler(builder);
            parser.parse(source);
        }
        catch(SAXException sx) {
                    
            SAXParseException sxp = null;
            Exception nested = sx.getException();
                    
            if (sx instanceof SAXParseException)
                sxp = (SAXParseException)sx;
            else if ((nested != null) && 
                        (nested instanceof SAXParseException)) 
                sxp = (SAXParseException)nested;
                    
            if (sxp != null) {
                StringBuffer err = new StringBuffer(sxp.toString());
                err.append("\n - ");
                err.append(sxp.getSystemId());
                err.append("; line: ");
                err.append(sxp.getLineNumber());
                err.append(", column: ");
                err.append(sxp.getColumnNumber());
                receiveError(err.toString(), ErrorObserver.FATAL);
                return null;
            }
            else {
                receiveError(sx, ErrorObserver.FATAL);
                return null;
            }
        }
        catch(IOException iox) {
            receiveError(iox, ErrorObserver.FATAL);
            return null;
        }
        
        
		return builder.getRoot();
		    
	    /* <performance-testing> *
		Document document 
		    = domReader.readDocument(bin, filename, validate, errorWriter);
	    System.out.print("- done reading XML document: ");
	    System.out.print(System.currentTimeMillis()-stime);
	    System.out.println(" (ms)");
	    return document;
	    /* </performance-testing> */
	    
	} //-- readXMLDocument

	/**
	 * Reads an XSL Document from the given InputStream. 
	 * @param xslInput the XSL InputStream, if this stream is null,
	 * this method will check the XSLPIHandler for the stylesheet href.
	 * @param filename the filename to use during error reporting.
	 * @param piHandler the XSLPIHandler to use. If this is set to null, 
	 * no XSLPIHandler will be used.
	 * 
	 * <BR>Changes:</BR>
	 * -- Added Warren Hedley's fix for no stylesheet being
	 *    specified or possible error when opening the file
	**/
	private XSLTStylesheet readXSLStylesheet
	    (URILocation location, XSLPIHandler piHandler) 
	{
	    /* <performance-testing> *
	    System.out.println ("read XSL stylesheet: "+filename);
	    long stime = System.currentTimeMillis();
	    /* </performance-testing> */
	    
        // look for xml:stylesheet PI. Order of precedence is
        // * location - if it exists
        // * PI in xml document - contained in piHandler
        if ((location == null) && (piHandler != null)) {
                
            try {
                location = _uriResolver.resolve(piHandler.getStylesheetHref(),
                                                piHandler.getDocumentBase());
            }
            catch (URIException exception) {
                receiveError(exception);
                //-- if we make it here the error observers did
                //-- not throw any runtime exceptions...so
                //-- continue with processing, but warn user
                receiveError("continuing processing with default rules",
                    ErrorObserver.WARNING);
                return new XSLTStylesheet();
            }
        }
        
        if (location == null) {
            String err = "No stylesheet specified. Use `-s' option on" +
                "the command line, or `xml-stylesheet' PI in the " +
                "XML document";
            receiveError(err);
            //-- if we make it here the error observers did
            //-- not throw any runtime exceptions...so
            //-- continue with processing, but warn user
            receiveError("continuing processing with default rules",
                ErrorObserver.WARNING);
            return new XSLTStylesheet();
        }

        // create stylesheet
        XSLTStylesheet xsl = null;
        XSLTReader xslReader = new XSLTReader(_uriResolver);
        xslReader.addErrorObserver( (ErrorObserver) this);
        try {
            xsl = xslReader.read(location);
        }
        catch (Exception ex) {
            receiveError(ex);
        }
        if (xsl == null) {
            receiveError("Unable to resolve stylesheet: " + location);
            //-- if we make it here the error observers did
            //-- not throw any runtime exceptions...so
            //-- continue with processing, but warn user
            receiveError("continuing processing with default rules",
                ErrorObserver.WARNING);
            xsl = new XSLTStylesheet();
        }
        
	    /* <performance-testing> *
	    System.out.print("- done reading XSL stylesheet: ");
	    System.out.print(System.currentTimeMillis()-stime);
	    System.out.println(" (ms)");
	    /* </performance-testing> */
        
        return xsl;
    } //-- readXSLStylesheet

	/**
	 * Reads the given XSL Document into an XSLStylesheet
	 * @param xslDocument the Stylesheet Document to create
	 * an XSLStylesheet from
	 * @param filename the filename to use during error reporting.
	**/
	private XSLTStylesheet readXSLStylesheet
	    (Document xslDocument, String filename)
	{
        // create stylesheet
        XSLTStylesheet xsl = null;
        if (xslDocument != null) {
            XSLTReader xslReader = new XSLTReader();
            xslReader.addErrorObserver(this);
            try {
                xsl = xslReader.read(xslDocument, filename);
            }
            catch (XSLException xslx) {
                receiveError(xslx);
                //-- if we make it here the error observers did
                //-- not throw any runtime exceptions...so
                //-- continue with processing, but warn user
                receiveError("continuing processing with default rules",
                    ErrorObserver.WARNING);
                xsl = new XSLTStylesheet();
            }
        }
        else xsl = new XSLTStylesheet();
        
        return xsl;
  } //-- readXSLStylesheet
  
 
} //-- XSLProcessor