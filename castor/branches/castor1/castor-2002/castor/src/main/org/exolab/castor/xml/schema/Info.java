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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.ValidationException;

import java.util.Enumeration;
import java.util.Vector;

/**
 * A class which represents the XML Schema Info element
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$ 
**/
public class Info extends Structure {
    
    
    /**
     * The text content of this AppInfo
    **/
    private String content = null;
    
    /**
     * List of any elements
    **/
    private Vector objects = null;
    
    /**
     * The base attribute
    **/
    private String base = null;
    
    /**
     * Creates a new Info
    **/
    public Info() {
        objects = new Vector(3);
    } //-- Annotation
    
    /**
     * Adds the given Object to this Info
     * @param object the Object to add
    **/
    public void add(Object object) {
        if (object != null)
            objects.addElement(object);
    } //-- add
    
    /**
     * Returns the String content of this Info
     * @return the String content of this Info
    **/
    public String getContent() {
        return content;
    } //-- getContent
    
    /**
     * Returns an Enumeration of all objects contained by this Info.
     * @return an Enumeration of all objects contained by this Info.
    **/
    public Enumeration getObjects() {
        return objects.elements();
    } //-- getObjects
    
    /**
     * Returns the base property of this Info
     * @return the base property of this Info
    **/
    public String getBase() {
        return base;
    } //-- getBase
    
    /**
     * Removes the given Object from this Info
     * @param object the Object to remove
    **/
    public void remove(Object object) {
        if (object != null) objects.removeElement(object);
    } //-- remove

    /**
     * Sets the String content for this Info
     * @param content the String content for this Info
    **/
    public void setContent(String content) {
        this.content = content;
    } //-- setContent
    
    /**
     * Sets the base property for this Info
     * @param base the value of the base property
    **/
    public void setBase(String base) {
        this.base = base;
    } //-- setBase
    
    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/
    
    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.INFO;
    } //-- getStructureType
    
    /**
     * Checks the validity of this Schema defintion.
     * @exception ValidationException when this Schema definition
     * is invalid.
    **/
    public void validate()
        throws ValidationException 
    {
        //-- do nothing
    } //-- validate
    
} //-- Info
