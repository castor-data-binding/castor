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

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.xml.sax.*;

/**
 * A class for Unmarshalling Archetypes
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$ 
**/
public class ArchetypeUnmarshaller extends SaxUnmarshaller {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * The current SaxUnmarshaller
    **/
    private SaxUnmarshaller unmarshaller;
    
    /**
     * The current branch depth
    **/
    private int depth = 0;
    
    /**
     * The Attribute reference for the Attribute we are constructing
    **/
    private Archetype _archetype = null;
    
    private CharacterUnmarshaller charUnmarshaller = null;
    
    boolean allowRefines = true;
    
    boolean allowContentModel = true;
    
    
      //----------------/
     //- Constructors -/
    //----------------/

    public ArchetypeUnmarshaller(AttributeList atts, Resolver resolver) {
        super();
        setResolver(resolver);
        
        _archetype = new Archetype();
        _archetype.useResolver(resolver);
        
        //-- handle attributes
        String attValue = null;
            
        _archetype.setName(atts.getValue("name"));
            
        //-- read contentType
        String content = atts.getValue(SchemaNames.CONTENT_ATTR);
        if (content != null) {
            _archetype.setContent(ContentType.valueOf(content));
        }
        
        charUnmarshaller = new CharacterUnmarshaller();
    } //-- ArchetypeUnmarshaller

      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the name of the element that this SaxUnmarshaller
     * handles
     * @return the name of the element that this SaxUnmarshaller
     * handles
    **/
    public String elementName() {
        return SchemaNames.ARCHETYPE;
    } //-- elementName

    /**
     * 
    **/
    public Archetype getArchetype() {
        return _archetype;
    } //-- getArchetype

    /**
     * @param name 
     * @param atts 
     * @see org.xml.sax.DocumentHandler
    **/
    public void startElement(String name, AttributeList atts) 
        throws org.xml.sax.SAXException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.startElement(name, atts);
            ++depth;
            return;
        }
        
        //-- Use JVM internal String
        name = name.intern();
                
        if (name == SchemaNames.ATTRIBUTE) {
            allowRefines = false;
            allowContentModel = false;
            unmarshaller = new AttributeUnmarshaller(atts, getResolver());
        }
        else if (name == SchemaNames.ELEMENT) {
            allowRefines = false;
            if (allowContentModel)
                unmarshaller = new ElementUnmarshaller(atts, getResolver());
            else 
                outOfOrder(name);
        }
        else if (name == SchemaNames.GROUP) {
            allowRefines = false;
            if (allowContentModel)
                unmarshaller = new GroupUnmarshaller(atts, getResolver());
            else
                outOfOrder(name);
        }
        else {
            StringBuffer err = new StringBuffer("illegal element <");
            err.append(name);
            err.append("> found in <archetype>.");
            throw new SAXException(err.toString());
        }
    
    } //-- startElement

    /**
     * 
     * @param name 
    **/
    public void endElement(String name) 
        throws org.xml.sax.SAXException
    {
        
        //-- Do delagation if necessary
        if ((unmarshaller != null) && (depth > 0)) {
            unmarshaller.endElement(name);
            --depth;
            return;
        }
        
        //-- check for name mismatches
        if ((unmarshaller != null) && (charUnmarshaller != unmarshaller)) {
            if (!name.equals(unmarshaller.elementName())) {
                String err = "missing end element for ";
                err += unmarshaller.elementName();
                throw new SAXException(err);
            }
        }
        
        //-- Use JVM internal String
        name = name.intern();
        
        //-- have unmarshaller perform any necessary clean up
        unmarshaller.finish();
        
        if (name == SchemaNames.ATTRIBUTE) {
            AttributeDecl attrDecl =
                ((AttributeUnmarshaller)unmarshaller).getAttribute();
                
            _archetype.addAttributeDecl(attrDecl);
        } 
        else if (name == SchemaNames.ELEMENT) {
            
            ElementDecl element = 
                ((ElementUnmarshaller)unmarshaller).getElement();
            _archetype.addElementDecl(element);
        }
        else if (name == SchemaNames.GROUP) {
            Group group = ((GroupUnmarshaller)unmarshaller).getGroup();
            _archetype.addGroup(group);
        }
    
        unmarshaller = null;
    } //-- endElement

    public void characters(char[] ch, int start, int length) 
        throws SAXException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.characters(ch, start, length);
        }
    } //-- characters

} //-- ArchetypeUnmarshaller
