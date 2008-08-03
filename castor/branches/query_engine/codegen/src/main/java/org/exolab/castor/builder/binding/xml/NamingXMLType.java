/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * This element allows to define naming convention when naming a
 * complexType, element or
 *  modelGroup. Indeed the user can decide of a prefix to add to
 * each class name as well
 *  as a suffix. This naming style won't affect the names entered
 * in the binding file but only 
 *  the XML Names.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class NamingXMLType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _elementName.
     */
    private org.exolab.castor.builder.binding.xml.NamingType _elementName;

    /**
     * Field _complexTypeName.
     */
    private org.exolab.castor.builder.binding.xml.NamingType _complexTypeName;

    /**
     * Field _modelGroupName.
     */
    private org.exolab.castor.builder.binding.xml.NamingType _modelGroupName;


      //----------------/
     //- Constructors -/
    //----------------/

    public NamingXMLType() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'complexTypeName'.
     * 
     * @return the value of field 'ComplexTypeName'.
     */
    public org.exolab.castor.builder.binding.xml.NamingType getComplexTypeName(
    ) {
        return this._complexTypeName;
    }

    /**
     * Returns the value of field 'elementName'.
     * 
     * @return the value of field 'ElementName'.
     */
    public org.exolab.castor.builder.binding.xml.NamingType getElementName(
    ) {
        return this._elementName;
    }

    /**
     * Returns the value of field 'modelGroupName'.
     * 
     * @return the value of field 'ModelGroupName'.
     */
    public org.exolab.castor.builder.binding.xml.NamingType getModelGroupName(
    ) {
        return this._modelGroupName;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'complexTypeName'.
     * 
     * @param complexTypeName the value of field 'complexTypeName'.
     */
    public void setComplexTypeName(
            final org.exolab.castor.builder.binding.xml.NamingType complexTypeName) {
        this._complexTypeName = complexTypeName;
    }

    /**
     * Sets the value of field 'elementName'.
     * 
     * @param elementName the value of field 'elementName'.
     */
    public void setElementName(
            final org.exolab.castor.builder.binding.xml.NamingType elementName) {
        this._elementName = elementName;
    }

    /**
     * Sets the value of field 'modelGroupName'.
     * 
     * @param modelGroupName the value of field 'modelGroupName'.
     */
    public void setModelGroupName(
            final org.exolab.castor.builder.binding.xml.NamingType modelGroupName) {
        this._modelGroupName = modelGroupName;
    }

    /**
     * Method unmarshalNamingXMLType.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.builder.binding.NamingXMLType
     */
    public static org.exolab.castor.builder.binding.xml.NamingXMLType unmarshalNamingXMLType(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.builder.binding.xml.NamingXMLType) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.xml.NamingXMLType.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
