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
 * Class PackageTypeChoice.
 * 
 * @version $Revision$ $Date$
 */
public class PackageTypeChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _namespace.
     */
    private java.lang.String _namespace;

    /**
     * Field _schemaLocation.
     */
    private java.lang.String _schemaLocation;


      //----------------/
     //- Constructors -/
    //----------------/

    public PackageTypeChoice() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'namespace'.
     * 
     * @return the value of field 'Namespace'.
     */
    public java.lang.String getNamespace(
    ) {
        return this._namespace;
    }

    /**
     * Returns the value of field 'schemaLocation'.
     * 
     * @return the value of field 'SchemaLocation'.
     */
    public java.lang.String getSchemaLocation(
    ) {
        return this._schemaLocation;
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
     * Sets the value of field 'namespace'.
     * 
     * @param namespace the value of field 'namespace'.
     */
    public void setNamespace(
            final java.lang.String namespace) {
        this._namespace = namespace;
    }

    /**
     * Sets the value of field 'schemaLocation'.
     * 
     * @param schemaLocation the value of field 'schemaLocation'.
     */
    public void setSchemaLocation(
            final java.lang.String schemaLocation) {
        this._schemaLocation = schemaLocation;
    }

    /**
     * Method unmarshalPackageTypeChoice.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.builder.binding.PackageTypeChoice
     */
    public static org.exolab.castor.builder.binding.xml.PackageTypeChoice unmarshalPackageTypeChoice(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.builder.binding.xml.PackageTypeChoice) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.xml.PackageTypeChoice.class, reader);
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
