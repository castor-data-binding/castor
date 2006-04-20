/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

// Sax imports
import org.xml.sax.*;


/**
 * A utility class for Unmarshalling elements with only String content
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class CharacterUnmarshaller extends SaxUnmarshaller {

  
    private StringBuffer sb = null;
    
    private String currentName = null;
    /**
     * Creates a new StringUnmarshaller
    **/
    public CharacterUnmarshaller() {
        super();
        sb = new StringBuffer();
    } //-- CharacterUnmarshaller
    
    /**
     * Returns the name of the element that this SaxUnmarshaller
     * handles
     * @return the name of the element that this SaxUnmarshaller
     * handles
    **/
    public String elementName() {
        return currentName;
    } //-- elementName
    
    /**
     * Sets the name of the element that this CharacterUnmarshaller handles
     * @param name the name of the element that this character unmarshaller handles
    **/
    public void elementName(String name) {
        currentName = name;
    } //-- elementName
    
    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        return getString();
    } //-- getObject
    
    /**
     * Returns the set of characters recieved by this CharacterUnmarshaller
     * as a String
     * @return the set of characters recieved by this CharacterUnmarshaller
     * as a String
    **/
    public String getString() {
        return sb.toString();
    } //-- getString
    
    /**
     * The SAX characters method for recieving characters
     * @see org.xml.sax.DocumentHandler
    **/
    public void characters(char[] ch, int start, int length) 
        throws SAXException
    {
        sb.append(ch, start, length);
    } //-- characters

    /**
     * Clears the current buffer
    **/
    public void clear() {
        sb.setLength(0);    
    } //-- clear
    
} //-- CharacterUnmarshaller
