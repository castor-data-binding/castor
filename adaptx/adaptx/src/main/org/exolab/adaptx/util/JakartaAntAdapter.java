/*
 * (C) Copyright Keith Visco 2001  All rights reserved.
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

package org.exolab.adaptx.util;



//-- Adaptx Imports
import org.exolab.adaptx.xslt.XSLTProcessor;

//-- Jakarta Ant Imports
import org.apache.tools.ant.taskdefs.XSLTLiaison;

//-- Java imports
import java.io.File;
import java.io.FileWriter;

/**
 * A utility class which allows Adaptx to be used as the
 * XSLT Processor for the Jakarta ANT build utility. 
 * 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class JakartaAntAdapter implements XSLTLiaison {
    
    XSLTProcessor _processor = null;
    String _stylesheet = null;
    
    /**
     * Creates a new adapter class for Jakarta ANT.
    **/
    public JakartaAntAdapter() {
        _processor = new XSLTProcessor();
    } //-- JakartaAntAdapter
    
    
    /**
     * Sets the stylesheet to use for the transformation.
     *
     * @param stylesheet the stylesheet to be used for transformation.
    **/
    public void setStylesheet(File stylesheet) 
        throws Exception
    {
        _stylesheet = stylesheet.getAbsolutePath();
    } //-- setStylesheet

    /**
     * Add a parameter to be set during the XSL transformation.
     *
     * @param name the parameter name.
     * @param expression the parameter value as an expression string.
     * @exception Exception thrown if any problems happens.
    **/
    public void addParam(String name, String expression) 
        throws Exception
    {
        _processor.setProperty(name, expression);
        
    } //-- addParam

    /**
     * Perform the transformation of a file into another.
     *
     * @param infile the xml input file.
     * @param outfile the output file resulting from the transformation
     * @exception Exception thrown if any problems happens.
     * @see #setStylesheet(File)
    **/
    public void transform(File infile, File outfile) 
        throws Exception
    {   
        FileWriter writer = new FileWriter(outfile);
        _processor.process(infile.getAbsolutePath(), _stylesheet, writer);
        
    } //-- transform

} //-- JakartaAntAdapter