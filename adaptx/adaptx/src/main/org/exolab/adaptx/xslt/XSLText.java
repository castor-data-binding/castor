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
 * 
 */
 
package org.exolab.adaptx.xslt;

import org.w3c.dom.*;

/**
 * Represents the xsl:text element
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
**/
public class XSLText extends XSLObject {
    
    private static final char[] EMPTY_CHAR_ARRAY = new char[0];
    
    /**
     * The String data of this XSLText 
    **/
    private char[] buffer = null;
    
    private boolean disableEscaping = false;
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * Creates a new XSLText element, with no data
     * @param parentStylesheet the XSL Stylesheet in which this
     * XSLText is to be added
    **/
    public XSLText() {
        super(XSLObject.TEXT);
    } //-- XSLText
    
    /**
     * Creates a new XSLText with the given data
     * @param parentStylesheet the XSL Stylesheet in which this
     * XSLText is to be added
     * @param data the value of this XSLText object
    **/
    public XSLText(String data) {
        super(XSLObject.TEXT);
        appendText(data);
    } //-- XSLText
    
    /**
     * Creates a new XSLText of the given type
     * @param parent the XSL parent stylesheet
    **/
    protected XSLText(short type) {
        super(type);
    } //-- XSLText
 
      //------------------/
     //- Public Methods -/
    //------------------/
    
    /**
     * Appends the given String to the existing data of
     * this XSLText
     * @param data the String to append
    **/
    public void appendText(String data) {
        if (buffer == null) buffer = data.toCharArray();
        else {
            int size = data.length();
            char[] oldbuffer = buffer;
            buffer = new char[oldbuffer.length+size];
            System.arraycopy(oldbuffer, 0, buffer, 0, oldbuffer.length);
            data.getChars(0, size, buffer, oldbuffer.length);
        }
    } //-- appendData
    
    /**
     * Appends the given String to the existing data of
     * this XSLText
     * @param chars an array of characters containing the data to
     *  apoend to this XSLText.
     * @param start the start index into the character array
     * @param length the number of characters 
    **/
    public void appendText(char[] chars, int start, int length) {
        
        int index = 0;
        if (buffer == null) 
            buffer = new char[length];
        else {
            int size = length+buffer.length;
            char[] oldbuffer = buffer;
            buffer = new char[size];
            System.arraycopy(oldbuffer, 0, buffer, 0, oldbuffer.length);
            index = oldbuffer.length;
        }
        System.arraycopy(chars, start, buffer, index, length);
    } //-- appendData
    
    /**
     * Returns true if the text should not be escaped
     * @return true if the text should not be escaped
    **/
    public boolean disableOutputEscaping() {
        return disableEscaping;
    } //-- disableOutputEscaping
    
    /**
     * Retrieves the text data of this XSLText
     * @return the data of this XSLText
    **/
    public String getText() {
        if (buffer == null) return new String();
        else return new String(buffer);
    } //-- getData
    
    /**
     * Sets the attribute with the given name to the given value.
     * @param name the name of the attribute to set
     * @param value the value to set the attribute to
     * @throws XSLException if this XSLObject does not allow attributes
     * with the given name, or if the attribute is read only
    **/
    public void setAttribute(String name, String value) 
        throws XSLException
    {
        if ((name != null) && (value != null)) {
            if (Names.DISABLE_ESCAPING_ATTR.equals(name)) {
                disableEscaping = Names.YES_VALUE.equals(value);
            }
        }
        super.setAttribute(name, value);
    } //-- setAttribute
    
    /**
     * Sets the text data of this XSLText
     * @param data the String to set the data of this XSLText
     * to.
    **/
    public void setText(String data) {
        buffer = null;
        appendText(data);
    } //--setData

    /**
     * Sets the text data of this XSLText
     * @param chars an array of characters containing the data for 
     *  this XSLText.
     * @param start the start index into the character array
     * @param length the number of characters 
    **/
    public void setText(char[] chars, int start, int length) {
        buffer = null;
        appendText(chars, start, length);
    } //--setText
    
    //---------------------/
    //- Protected Methods -/
    //---------------------/
    
    /**
     * Returns the internal buffer used by this XSLText
     * <BR/>
     * <B>Note:</B> This is only called by RuleProcessor
     * @return the char array
    **/
    protected char[] getCharArray() {
        if (buffer == null) return EMPTY_CHAR_ARRAY;
        return buffer;
    } //-- getCharArray
    
} //-- XSLText
