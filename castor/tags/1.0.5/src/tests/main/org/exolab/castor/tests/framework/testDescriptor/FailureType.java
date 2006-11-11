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
 * True if this test is expected to throw an Exception and if it
 * would thus
 *  be an error if the test does not throw an Exception. False
 * otherwise.
 *  <p>
 *  If FailureType is true, then this element optionally contains
 * the attribute
 *  exception that contains the class of the Exception that is
 *  expected. If this attribute is not provided, then the presence
 * of any
 *  exception causes the test to pass. Otherwise, the specific
 * exception
 *  has to be thrown for the test to pass.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class FailureType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private boolean _content;

    /**
     * keeps track of state for field: _content
     */
    private boolean _has_content;

    /**
     * Field _exception
     */
    private java.lang.String _exception;

    /**
     * Field _failureStep
     */
    private org.exolab.castor.tests.framework.testDescriptor.types.FailureStepType _failureStep;


      //----------------/
     //- Constructors -/
    //----------------/

    public FailureType() 
     {
        super();
    } //-- org.exolab.castor.tests.framework.testDescriptor.FailureType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteContent()
    {
        this._has_content= false;
    } //-- void deleteContent() 

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'Content'.
     */
    public boolean getContent()
    {
        return this._content;
    } //-- boolean getContent() 

    /**
     * Returns the value of field 'exception'.
     * 
     * @return the value of field 'Exception'.
     */
    public java.lang.String getException()
    {
        return this._exception;
    } //-- java.lang.String getException() 

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
     * Method hasContent
     * 
     * 
     * 
     * @return true if at least one Content has been added
     */
    public boolean hasContent()
    {
        return this._has_content;
    } //-- boolean hasContent() 

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'Content'.
     */
    public boolean isContent()
    {
        return this._content;
    } //-- boolean isContent() 

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
    public void setContent(boolean content)
    {
        this._content = content;
        this._has_content = true;
    } //-- void setContent(boolean) 

    /**
     * Sets the value of field 'exception'.
     * 
     * @param exception the value of field 'exception'.
     */
    public void setException(java.lang.String exception)
    {
        this._exception = exception;
    } //-- void setException(java.lang.String) 

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
     * org.exolab.castor.tests.framework.testDescriptor.FailureType
     */
    public static org.exolab.castor.tests.framework.testDescriptor.FailureType unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.FailureType) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.FailureType.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.FailureType unmarshal(java.io.Reader) 

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
