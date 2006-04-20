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
 * A class for Unmarshalling element definitions
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$ 
**/
public class ElementUnmarshaller extends SaxUnmarshaller {


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
     * The element reference for the element definition we are "unmarshalling".
    **/
    private ElementDecl _element = null;
    
    
    private CharacterUnmarshaller charUnmarshaller = null;
    
      //----------------/
     //- Constructors -/
    //----------------/

    public ElementUnmarshaller(AttributeList atts, Resolver resolver) {
        super();
        setResolver(resolver);
        
        _element = new ElementDecl();
        _element.useResolver(resolver);
        
        String attValue = null;
        
        //-- @ref
        attValue = atts.getValue("ref");
        if (attValue != null) {
            _element.setReference(attValue,resolver);
        }
            
            
        //-- @name
        _element.setName(atts.getValue("name"));
            
        //-- @type
        attValue = atts.getValue("type");
        if (attValue != null) _element.setTypeRef(atts.getValue("type"));
                
        //-- @minOccurs
        attValue = atts.getValue(SchemaNames.MIN_OCCURS_ATTR);
        if (attValue != null) {
            int minOccurs = toInt(attValue);
            _element.setMinimumOccurance(minOccurs);
        }
        //-- @maxOccurs
        attValue = atts.getValue(SchemaNames.MAX_OCCURS_ATTR);
        if (attValue != null) {
            int maxOccurs = toInt(attValue);
            _element.setMaximumOccurance(maxOccurs);
        }
        //-- @schemaAbbrev
        _element.setSchemaAbbrev(atts.getValue("schemaAbbrev"));
        
        //-- @schemaName
        _element.setSchemaName(atts.getValue("schemaName"));
        
        
        if (!_element.isReference())
            resolver.addResolvable(_element.getReferenceId(), _element);
        
        charUnmarshaller = new CharacterUnmarshaller();
    } //-- ElementUnmarshaller

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
        return SchemaNames.ELEMENT;
    } //-- elementName

    /**
     * 
    **/
    public ElementDecl getElement() {
        return _element;
    } //-- getElement

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
        if (name == SchemaNames.ARCHETYPE) {
            unmarshaller = new ArchetypeUnmarshaller(atts, getResolver());
        }
        else if (name == SchemaNames.DATATYPE) {
            throw new SAXException("<datatype> not yet supported for <element>.");
        }
        else {
            StringBuffer err = new StringBuffer("illegal element <");
            err.append(name);
            err.append("> found in <element>.");
            throw new SAXException(err.toString());
        }
        
        unmarshaller.setResolver(getResolver());
        
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
       
        //-- call finish for any necessary cleanup
        unmarshaller.finish();
        
        if (name == SchemaNames.ARCHETYPE) {
            
            Archetype archetype = _element.getArchetype();
            
            if (archetype != null) {
                StringBuffer err = new StringBuffer("archetype defined more than ");
                err.append("once for element definition: ");
                err.append(_element.getName());
                throw new SAXException(err.toString());
            }
            
            archetype = ((ArchetypeUnmarshaller)unmarshaller).getArchetype();
            _element.setArchetype(archetype);
            
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

} //-- ElementUnmarshaller
