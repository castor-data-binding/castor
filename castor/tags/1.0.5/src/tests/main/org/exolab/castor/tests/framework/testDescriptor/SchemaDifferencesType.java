/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.4</a>, using an XML
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
 * If you expect a non-zero number of differences when comparing
 * schemas,
 *  add one of these elements and provide the FailureStep attribute
 *  to say which step this difference applies to.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class SchemaDifferencesType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private java.math.BigDecimal _content;

    /**
     * Field _failureStep
     */
    private org.exolab.castor.tests.framework.testDescriptor.types.FailureStepType _failureStep;


      //----------------/
     //- Constructors -/
    //----------------/

    public SchemaDifferencesType() 
     {
        super();
    } //-- org.exolab.castor.tests.framework.testDescriptor.SchemaDifferencesType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'Content'.
     */
    public java.math.BigDecimal getContent()
    {
        return this._content;
    } //-- java.math.BigDecimal getContent() 

    /**
     * Returns the value of field 'failureStep'.
     * 
     * @return the value of field 'FailureStep'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.types.FailureStepType getFailureStep()
    {
        return this._failureStep;
    } //-- org.exolab.castor.tests.framework.testDescriptor.types.FailureStepType getFailureStep() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

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
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(java.math.BigDecimal content)
    {
        this._content = content;
    } //-- void setContent(java.math.BigDecimal) 

    /**
     * Sets the value of field 'failureStep'.
     * 
     * @param failureStep the value of field 'failureStep'.
     */
    public void setFailureStep(org.exolab.castor.tests.framework.testDescriptor.types.FailureStepType failureStep)
    {
        this._failureStep = failureStep;
    } //-- void setFailureStep(org.exolab.castor.tests.framework.testDescriptor.types.FailureStepType) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.tests.framework.testDescriptor.SchemaDifferencesType
     */
    public static org.exolab.castor.tests.framework.testDescriptor.SchemaDifferencesType unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.SchemaDifferencesType) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.SchemaDifferencesType.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.SchemaDifferencesType unmarshal(java.io.Reader) 

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
