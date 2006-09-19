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
 * Copyright 1999-2002 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.xml.AttributeSet;
import org.exolab.castor.xml.Namespaces;
import org.exolab.castor.xml.XMLException;
import org.exolab.castor.xml.schema.Annotation;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaNames;
import org.exolab.castor.xml.schema.SimpleType;

/**
 * A class for Unmarshalling SimpleTypes
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-14 04:14:43 -0600 (Fri, 14 Apr 2006) $
**/
public class SimpleTypeUnmarshaller extends ComponentReader {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * The current ComponentReader
    **/
    private ComponentReader unmarshaller;

    /**
     * The current branch depth
    **/
    private int depth = 0;

    /**
     * The SimpleTypeDefinition we are unmarshalling
    **/
    private SimpleTypeDefinition _simpleTypeDef = null;

    /**
     * A reference to the SimpleType we are unmarshalling
    **/
    private SimpleType _simpleType = null;
    
    
    private boolean foundAnnotation   = false;
    private boolean foundList         = false;
    private boolean foundRestriction  = false;
    private boolean foundUnion        = false;

      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new SimpleTypeUnmarshaller
     * @param schema the Schema to which the SimpleType belongs
     * @param atts the AttributeList
    **/
    public SimpleTypeUnmarshaller
        (Schema schema, AttributeSet atts)
        throws XMLException
    {
        super();

        String name = atts.getValue(SchemaNames.NAME_ATTR);
        
        //-- strip off prefix if necessary
        if (name != null) {
            int idx = name.indexOf(':');
            if (idx >= 0) {
                String prefix = name.substring(0, idx);
                String ns = schema.getNamespace(prefix);
                if (ns == null) {
                    //-- should report an error here
                }
                else {
                    if (ns.equals(schema.getTargetNamespace())) {
                        name = name.substring(idx+1);
                    }
                    else {
                        //-- should report an error here
                    }
                }
            }
        }
        
        String id   = atts.getValue(SchemaNames.ID_ATTR);
        _simpleTypeDef = new SimpleTypeDefinition(schema, name, id);
        
        //-- @final
        _simpleTypeDef.setFinal(atts.getValue(SchemaNames.FINAL_ATTR));


    } //-- SimpleTypeUnmarshaller

      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the name of the element that this ComponentReader
     * handles
     * @return the name of the element that this ComponentReader
     * handles
    **/
    public String elementName() {
        return SchemaNames.SIMPLE_TYPE;
    } //-- elementName

    /**
     * Returns the SimpleType created
     * @return the SimpleType created
    **/
    public SimpleType getSimpleType() {
        if (_simpleType == null) {
            _simpleType = _simpleTypeDef.createSimpleType();
        }
        return _simpleType;
    } //-- getSimpletype

    /**
     * Returns the Object created by this ComponentReader
     * @return the Object created by this ComponentReader
    **/
    public Object getObject() {
        return getSimpleType();
    } //-- getObject

    public void finish() throws XMLException {
        if (!(foundList || foundUnion || foundRestriction))
            error ("Invalid 'simpleType'; missing 'restriction' "+
                "| 'union' | 'list'.");

    } //-- finish

    /**
     * Signals the start of an element with the given name.
     *
     * @param name the NCName of the element. It is an error
     * if the name is a QName (ie. contains a prefix).
     * @param namespace the namespace of the element. This may be null.
     * Note: A null namespace is not the same as the default namespace unless
     * the default namespace is also null.
     * @param atts the AttributeSet containing the attributes associated
     * with the element.
     * @param nsDecls the namespace declarations being declared for this 
     * element. This may be null.
    **/
    public void startElement(String name, String namespace, AttributeSet atts,
        Namespaces nsDecls)
        throws XMLException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.startElement(name, namespace, atts, nsDecls);
            ++depth;
            return;
        }

        if (SchemaNames.ANNOTATION.equals(name)) {

            if (foundAnnotation)
                error("Only one (1) annotation may appear as a child of "+
                    "'simpleType'.");

            if (foundList || foundUnion || foundRestriction)
                error("An annotation may only appear as the first child "+
                    "of 'simpleType'.");

            foundAnnotation = true;
            unmarshaller = new AnnotationUnmarshaller(atts);
        }
        else if (SchemaNames.RESTRICTION.equals(name)) {

            if (foundList)
                error("A 'simpleType' cannot have both a 'list' and a "+
                    "'restriction' in the same definition.");

            if (foundUnion)
                error("A 'simpleType' cannot have both a 'union' and a "+
                    "'restriction' in the same definition.");


            foundRestriction = true;

            unmarshaller
                = new SimpleTypeRestrictionUnmarshaller(_simpleTypeDef, atts);
        }
        else if (SchemaNames.LIST.equals(name)) {
            foundList = true;
            Schema schema = _simpleTypeDef.getSchema();
            unmarshaller = new SimpleTypeListUnmarshaller(schema, atts);
        }
        else if (SchemaNames.UNION.equals(name)) {
            foundUnion = true;
            Schema schema = _simpleTypeDef.getSchema();
            unmarshaller = new UnionUnmarshaller(schema, atts);
        }
        else illegalElement(name);

    } //-- startElement

    /**
     * Signals to end of the element with the given name.
     *
     * @param name the NCName of the element. It is an error
     * if the name is a QName (ie. contains a prefix).
     * @param namespace the namespace of the element.
    **/
    public void endElement(String name, String namespace)
        throws XMLException
    {

        //-- Do delagation if necessary
        if ((unmarshaller != null) && (depth > 0)) {
            unmarshaller.endElement(name, namespace);
            --depth;
            return;
        }

        //-- have unmarshaller perform any necessary clean up
        unmarshaller.finish();

        if (SchemaNames.ANNOTATION.equals(name)) {
            Annotation annotation = (Annotation)unmarshaller.getObject();
            _simpleTypeDef.setAnnotation(annotation);
        }
        else if (SchemaNames.LIST.equals(name)) {
            _simpleType = (SimpleType)unmarshaller.getObject();
            _simpleTypeDef.copyInto(_simpleType);
            
        }
        else if (SchemaNames.UNION.equals(name)) {
            _simpleType = (SimpleType)unmarshaller.getObject();
            _simpleTypeDef.copyInto(_simpleType);
            
        }

        unmarshaller = null;
    } //-- endElement

    public void characters(char[] ch, int start, int length)
        throws XMLException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.characters(ch, start, length);
        }
    } //-- characters

} //-- SimpleTypeUnmarshaller
