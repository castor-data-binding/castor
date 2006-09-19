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
 * Copyright 2001-2002 (C) Intalio Inc. All Rights Reserved.
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
import org.exolab.castor.xml.schema.Union;

import java.util.StringTokenizer;

/**
 * A class for Unmarshalling XML Schema Union types
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
**/
public class UnionUnmarshaller extends ComponentReader {

      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * The current ComponentReader
    **/
    private ComponentReader _unmarshaller;

    /**
     * The current branch depth
    **/
    private int _depth = 0;

    /**
     * The Union we are unmarshalling
    **/
    private Union _union = null;

    /**
     * The parent Schema for the Union
    **/
    private Schema _schema = null;
    
    private boolean _foundAnnotation   = false;
    private boolean _foundSimpleType   = false;
    
      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new IdentityConstraintUnmarshaller
     *
     * @param atts the AttributeList
    **/
    public UnionUnmarshaller(Schema schema, AttributeSet atts)
        throws XMLException
    {
        super();
        
        if (schema == null) {
            String err = "'schema' must not be null.";
            throw new IllegalStateException(err);
        }
        _schema = schema;
        
        _union = new Union(_schema);
        _union.setId(atts.getValue(SchemaNames.ID_ATTR));
        
        String memberTypes = atts.getValue(SchemaNames.MEMBER_TYPES_ATTR);
        processMemberTypes(memberTypes);
        
    } //-- UnionUnmarshaller

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
        return SchemaNames.UNION;
    } //-- elementName

    /**
     * Returns the Object created by this ComponentReader
     *
     * @return the Object created by this ComponentReader
    **/
    public Object getObject() {
        return _union;
    } //-- getObject

    public void finish() 
        throws XMLException 
    {
        //-- do nothing
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
        if (_unmarshaller != null) {
            _unmarshaller.startElement(name, namespace, atts, nsDecls);
            ++_depth;
            return;
        }

        if (SchemaNames.ANNOTATION.equals(name)) {

            if (_foundAnnotation)
                error("Only one (1) annotation may appear as a child of '" +
                    elementName() + "'.");

            if (_foundSimpleType)
                error("An annotation may only appear as the first child of '"+
                    elementName() + "'.");

            _foundAnnotation = true;
            _unmarshaller = new AnnotationUnmarshaller(atts);
        }
        else if (SchemaNames.SIMPLE_TYPE.equals(name)) {
            _foundSimpleType = true;
            _unmarshaller = new SimpleTypeUnmarshaller(_schema, atts);
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
        if ((_unmarshaller != null) && (_depth > 0)) {
            _unmarshaller.endElement(name, namespace);
            --_depth;
            return;
        }

        //-- have unmarshaller perform any necessary clean up
        _unmarshaller.finish();

        if (SchemaNames.ANNOTATION.equals(name)) {
            _union.setLocalAnnotation((Annotation)_unmarshaller.getObject());
        }
        else if (SchemaNames.SIMPLE_TYPE.equals(name)) {
            
            SimpleType simpleType = 
                (SimpleType)_unmarshaller.getObject();
                
            //-- make sure type is not another Union
            if (simpleType instanceof Union) {
                String err = "A 'union' may only contain SimpleTypes of "+
                    "the atomic or list variety.";
                error(err);
            }
            //-- make sure type is atomic or list
            // XXXX to be added later
            
            _union.addMemberType(simpleType);
        }
        

        _unmarshaller = null;
    } //-- endElement

    public void characters(char[] ch, int start, int length)
        throws XMLException
    {
        //-- Do delagation if necessary
        if (_unmarshaller != null) {
            _unmarshaller.characters(ch, start, length);
        }
    } //-- characters


    /**
     * Processes the given string into the referenced simpleTypes
     * for the Union being unmarshalled.
     *
     * @param memberTypes the memberTypes list.
    **/
    private void processMemberTypes(String memberTypes) {
        if ((memberTypes == null) || (memberTypes.length() == 0))
            return;
            
        StringTokenizer st = new StringTokenizer(memberTypes);
        while (st.hasMoreTokens()) {
            String typeName = st.nextToken();
            SimpleType simpleType = _schema.getSimpleType(typeName);
            if (simpleType != null) {
                _union.addMemberType(simpleType);
            }
            else {
                _union.addMemberType(typeName);
            }
            
        }
    } //-- processMemberTypes
    
} //-- UnionUnmarshaller
