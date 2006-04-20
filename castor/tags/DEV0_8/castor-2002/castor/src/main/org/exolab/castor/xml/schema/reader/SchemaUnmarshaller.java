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
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SchemaUnmarshaller extends SaxUnmarshaller {
    
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
        
    boolean skipAll = false;
            
    /**
     * The ID Resolver
    **/
    Resolver _resolver = null;
    
    Schema _schema = null;
    
    private boolean foundSchemaDef = false;
    
      //----------------/
     //- Constructors -/
    //----------------/

    public SchemaUnmarshaller() {
        _schema = new Schema();
        _resolver = new ScopableResolver();
    }
    
    public SchemaUnmarshaller(AttributeList atts, Resolver resolver) {
        super();
        setResolver(resolver);        
        _schema = new Schema();
        foundSchemaDef = true;
        init(atts);
    } //-- SchemaUnmarshaller
    
    public Schema getSchema() {
        return _schema;
    }
    
    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        return getSchema();
    } //-- getObject
    
    /**
     * Returns the name of the element that this SaxUnmarshaller
     * handles
     * @return the name of the element that this SaxUnmarshaller
     * handles
    **/
    public String elementName() {
        return SchemaNames.SCHEMA;
    } //-- elementName
        
    
    /**
     * initializes the Schema object with the given attribute list
     * @param atts the AttributeList for the schema
    **/
    private void init(AttributeList atts) {
        if (atts == null) return;
        String nsURI = atts.getValue("targetNamespace");
        if ((nsURI != null) && (nsURI.length() > 0))
            _schema.setTargetNamespace(nsURI);
    } //-- init
    
    //-------------------------------------------------/
    //- implementation of org.xml.sax.DocumentHandler -/
    //-------------------------------------------------/
    
    public void startElement(String name, AttributeList atts)
        throws SAXException
    {
        
        if (skipAll) return;
        
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.startElement(name, atts);
            ++depth;
            return;
        }
        
        
        //-- use VM internal String of name
        name = name.intern();
                
        
        if (name == SchemaNames.SCHEMA) {
            
            if (foundSchemaDef)
                illegalElement(name);
                
            foundSchemaDef = true;
            init(atts);
            return;
        }
        
        //-- <type>
        if (name == SchemaNames.ARCHETYPE) {
            unmarshaller 
                = new ArchetypeUnmarshaller(_schema, atts, _resolver);
        } 
        //-- <element>
        else if (name == SchemaNames.ELEMENT) {
            unmarshaller 
                = new ElementUnmarshaller(_schema, atts, _resolver);
        }
        //-- <datatype>
        else if (name == SchemaNames.DATATYPE) {
            unmarshaller 
                = new DatatypeUnmarshaller(_schema, atts, _resolver);
        }
        else {
            //-- we should throw a new Exception here
            //-- but since we don't support everything
            //-- yet, simply add an UnknownDef object
            System.out.print(name);
            System.out.print(" elements are either currently unsupported ");
            System.out.println("or not valid schema elements.");
            unmarshaller = new UnknownUnmarshaller(name);
        }
        
    } //-- startElement

    public void endElement(String name) throws SAXException {
                
        if (skipAll) return;
        
        //-- Do delagation if necessary
        if ((unmarshaller != null) && (depth > 0)) {
            unmarshaller.endElement(name);
            --depth;
            return;
        }
        
        
        //-- use internal JVM String
        name = name.intern();        
                
        if (name == SchemaNames.SCHEMA) return;
        
        
        //-- check for name mismatches
        if ((unmarshaller != null)) {
            if (!name.equals(unmarshaller.elementName())) {
                String err = "error: missing end element for ";
                err += unmarshaller.elementName();
                throw new SAXException(err);
            }
        }
        else {
            String err = "error: missing start element for " + name;
            throw new SAXException(err);
        }
        
        //-- call unmarshaller.finish() to perform any necessary cleanup
        unmarshaller.finish();
        
        if (name == SchemaNames.ARCHETYPE) {
            Archetype archetype = null;
            archetype = ((ArchetypeUnmarshaller)unmarshaller).getArchetype();
            _schema.addArchetype(archetype);
            if (archetype.getName() != null) {
                _resolver.addResolvable(archetype.getReferenceId(), archetype);
            }
            else {
                System.out.println("warning: top-level archetype with no name.");
            }
        }
        else if (name == SchemaNames.DATATYPE) {
            Datatype datatype = null;
            datatype = ((DatatypeUnmarshaller)unmarshaller).getDatatype();
            _schema.addDatatype(datatype);
            _resolver.addResolvable(datatype.getReferenceId(), datatype);
        }
        else if (name == SchemaNames.ELEMENT) {
            ElementDecl element = null;
            element = ((ElementUnmarshaller)unmarshaller).getElement();
            _schema.addElementDecl(element);
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
    
} //-- SGDocumentHandler
 