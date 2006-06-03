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
import org.exolab.castor.xml.schema.IdentityConstraint;
import org.exolab.castor.xml.schema.IdentityField;
import org.exolab.castor.xml.schema.IdentitySelector;
import org.exolab.castor.xml.schema.Key;
import org.exolab.castor.xml.schema.KeyRef;
import org.exolab.castor.xml.schema.SchemaNames;
import org.exolab.castor.xml.schema.Unique;

/**
 * A class for Unmarshalling Identity Constraints
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
**/
public class IdentityConstraintUnmarshaller extends ComponentReader {

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
     * The IdentityConstraint we are unmarshalling
    **/
    private IdentityConstraint _identityConstraint = null;

    private boolean _foundAnnotation   = false;
    private boolean _foundSelector     = false;
    private boolean _foundField        = false;

    private String _elementName = null;
    
      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new IdentityConstraintUnmarshaller
     *
     * @param atts the AttributeList
     * @param resolver the resolver being used for reference resolving
    **/
    public IdentityConstraintUnmarshaller
        (String elementName, AttributeSet atts)
        throws XMLException
    {
        super();
        
        _elementName = elementName;

        String name = atts.getValue(SchemaNames.NAME_ATTR);
        if (name == null) {
            error("The 'name' attribute for an identity-constraint must exist.");
        }
        
        atts.getValue(SchemaNames.ID_ATTR);
        
        //-- keyref
        if (SchemaNames.KEYREF.equals(elementName)) {
            String refer = atts.getValue("refer");
            if (refer == null) {
                error("The 'refer' attribute for keyref must exist.");
            }
            _identityConstraint = new KeyRef(name, refer);
        }
        //-- unique
        else if (SchemaNames.UNIQUE.equals(elementName)) {
            _identityConstraint = new Unique(name);
        }
        //-- key
        else {
            _identityConstraint = new Key(name);
        }

    } //-- IdentityConstraintUnmarshaller

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
        return _elementName;
    } //-- elementName

    /**
     * Returns the IdentityConstraint created.
     *
     * @return the IdentityConstraint created.
    **/
    public IdentityConstraint getIdentityConstraint() {
        return _identityConstraint;
    } //-- getIdentityConstraint

    /**
     * Returns the Object created by this ComponentReader
     *
     * @return the Object created by this ComponentReader
    **/
    public Object getObject() {
        return getIdentityConstraint();
    } //-- getObject

    public void finish() throws XMLException {
        if (!_foundSelector) {
            error ("Invalid " + _elementName + "; missing 'selector'.");
        }
        else if (!_foundField) {
            error ("Invalid " + _elementName + "; missing 'field'.");
        }
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
                    _elementName + "'.");

            if (_foundSelector || _foundField)
                error("An annotation may only appear as the first child of '"+
                    _elementName + "'.");

            _foundAnnotation = true;
            _unmarshaller = new AnnotationUnmarshaller(atts);
        }
        else if (SchemaNames.SELECTOR.equals(name)) {

            if (_foundField) {
                String err = "The 'selector' element of '" + _elementName +
                    "' must appear before any 'field' elements.";
                error(err);
            }
            if (_foundSelector)
                error("Only one (1) 'selector' may appear as a child of '" +
                    _elementName + "'.");

            _foundSelector = true;

            _unmarshaller = new FieldOrSelectorUnmarshaller(name, atts);
        }
        else if (SchemaNames.FIELD.equals(name)) {
           _foundField = true;
           _unmarshaller = new FieldOrSelectorUnmarshaller(name, atts);
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
            Annotation annotation = (Annotation)_unmarshaller.getObject();
            _identityConstraint.addAnnotation(annotation);
        }
        else if (SchemaNames.SELECTOR.equals(name)) {
            IdentitySelector selector 
                = (IdentitySelector)_unmarshaller.getObject();
            _identityConstraint.setSelector(selector);
        }
        else if (SchemaNames.FIELD.equals(name)) {
            IdentityField field 
                = (IdentityField)_unmarshaller.getObject();
            _identityConstraint.addField(field);
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

} //-- IdentityConstraintUnmarshaller
