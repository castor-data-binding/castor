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

//-- imported classes and packages
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.xml.sax.*;

/**
 * A class for Unmarshalling ModelGroups
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$ 
**/
public class GroupUnmarshaller extends SaxUnmarshaller {

      //--------------------------/
     //- Static Class Variables -/
    //--------------------------/
     
    private static final int ALL         = 1;
    private static final int CHOICE      = 2;
    private static final int MODEL_GROUP = 3;
    private static final int SEQUENCE    = 4;
        
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
     * The ModelGroup reference for the ModelGroup we are constructing
    **/
    private Group _group = null;
    
    /**
     * The Schema being "unmarshalled"
    **/
    private Schema _schema = null;
    
    /**
     * The element name of the Group to be unmarshalled.
    **/
    private String _element = SchemaNames.GROUP;

    
      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * Creates a new GroupUnmarshaller
     * @param schema the Schema to which the Group belongs
     * @param the element name for this type of group
     * @param atts the AttributeList
     * @param resolver the resolver being used for reference resolving
    **/
    public GroupUnmarshaller
        (Schema schema, String element, AttributeList atts, Resolver resolver) 
    {        
        super();
        setResolver(resolver);
        this._schema = schema;
        
        _group = new Group();
        
        //-- handle attributes
        String attValue = null;
        
        
        //-- MODEL GROUP
        if (SchemaNames.GROUP.equals(element)) {
            //-- set name
            _group.setName(atts.getValue("name"));
        }
        else {
            
            if (SchemaNames.SEQUENCE.equals(element)) {
                _group.setOrder(Order.seq);
            }
            else if (SchemaNames.CHOICE.equals(element)) {
                _group.setOrder(Order.choice);
            }
            else if (SchemaNames.ALL.equals(element)) {
                _group.setOrder(Order.all);
            }
            else {
                String err = "Invalid group element name: '" +
                    element + "'";
                throw new IllegalArgumentException(err); 
            }
        }
        
        _element = element;
        
        //-- maxOccurs
        attValue = atts.getValue("maxOccurs");
        if (attValue != null) 
            _group.setMaxOccurs(toInt(attValue));
            
        //-- minOccurs
        attValue = atts.getValue("minOccurs");
        if (attValue != null) 
            _group.setMaxOccurs(toInt(attValue));
        
        //-- id
        _group.setId(atts.getValue("id"));
        
        //-- ref
        //Not yet supported
        
    } //-- GroupUnmarshaller

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
        return _element;
    } //-- elementName

    
    /**
     * Returns the Group that was unmarshalled by this Unmarshaller.
     * This method should only be called after unmarshalling
     * has been completed.
     *
     * @return the unmarshalled Group
    **/
    public Group getGroup() {
        return _group;
    } //-- getGroup
    
    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        return getGroup();
    } //-- getObject

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
        
        if (SchemaNames.ELEMENT.equals(name)) {
            unmarshaller 
                = new ElementUnmarshaller(_schema, atts, getResolver());
        }
        else if (SchemaNames.isGroupName(name)) {
            unmarshaller 
                = new GroupUnmarshaller(_schema, name, atts, getResolver());
        }
        else {
            StringBuffer err = new StringBuffer("illegal element <");
            err.append(name);
            err.append("> found in <group>.");
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
        if (unmarshaller != null) {
            if (!name.equals(unmarshaller.elementName())) {
                String err = "missing end element for ";
                err += unmarshaller.elementName();
                throw new SAXException(err);
            }
        }
        
        //-- have unmarshaller perform any necessary clean up
        unmarshaller.finish();
        
        if (SchemaNames.ELEMENT.equals(name)) {
            ElementDecl element = 
                ((ElementUnmarshaller)unmarshaller).getElement();
            _group.addElementDecl(element);
        }
        else if (SchemaNames.isGroupName(name)) {
            Group group = ((GroupUnmarshaller)unmarshaller).getGroup();
            _group.addGroup(group);
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
    
} //-- GroupUnmarshaller
