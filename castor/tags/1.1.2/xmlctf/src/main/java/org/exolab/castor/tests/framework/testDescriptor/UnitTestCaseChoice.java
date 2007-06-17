/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class UnitTestCaseChoice.
 * 
 * @version $Revision$ $Date$
 */
public class UnitTestCaseChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * This element is used only for a MarshallingTest and is
     *  optional. It specifies the mapping file to be used (if
     * any).
     *  
     */
    private java.lang.String _mapping_File;

    /**
     * This element is used only for a SchemaTest and is required
     *  for a SchemaTest. It gives the name of the schema to read +
     * write.
     *  If the value if this element is '' then the CTF will use
     * all of
     *  the schemas present in the directory or jar.
     *  
     */
    private java.lang.String _schema;


      //----------------/
     //- Constructors -/
    //----------------/

    public UnitTestCaseChoice() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'mapping_File'. The field
     * 'mapping_File' has the following description: This element
     * is used only for a MarshallingTest and is
     *  optional. It specifies the mapping file to be used (if
     * any).
     *  
     * 
     * @return the value of field 'Mapping_File'.
     */
    public java.lang.String getMapping_File(
    ) {
        return this._mapping_File;
    }

    /**
     * Returns the value of field 'schema'. The field 'schema' has
     * the following description: This element is used only for a
     * SchemaTest and is required
     *  for a SchemaTest. It gives the name of the schema to read +
     * write.
     *  If the value if this element is '' then the CTF will use
     * all of
     *  the schemas present in the directory or jar.
     *  
     * 
     * @return the value of field 'Schema'.
     */
    public java.lang.String getSchema(
    ) {
        return this._schema;
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
     * Sets the value of field 'mapping_File'. The field
     * 'mapping_File' has the following description: This element
     * is used only for a MarshallingTest and is
     *  optional. It specifies the mapping file to be used (if
     * any).
     *  
     * 
     * @param mapping_File the value of field 'mapping_File'.
     */
    public void setMapping_File(
            final java.lang.String mapping_File) {
        this._mapping_File = mapping_File;
    }

    /**
     * Sets the value of field 'schema'. The field 'schema' has the
     * following description: This element is used only for a
     * SchemaTest and is required
     *  for a SchemaTest. It gives the name of the schema to read +
     * write.
     *  If the value if this element is '' then the CTF will use
     * all of
     *  the schemas present in the directory or jar.
     *  
     * 
     * @param schema the value of field 'schema'.
     */
    public void setSchema(
            final java.lang.String schema) {
        this._schema = schema;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice
     */
    public static org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice.class, reader);
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
