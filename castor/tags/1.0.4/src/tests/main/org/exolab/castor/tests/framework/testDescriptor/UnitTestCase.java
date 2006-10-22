/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.3</a>, using an XML
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
 * A definition of a single Unit Test testcase.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class UnitTestCase implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _unitTestCaseChoice
     */
    private org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice _unitTestCaseChoice;

    /**
     */
    private org.exolab.castor.tests.framework.testDescriptor.Configuration _configuration;

    /**
     * Field _input
     */
    private java.lang.String _input;

    /**
     * Field _goldFile
     */
    private java.lang.String _goldFile;

    /**
     * Field _objectBuilder
     */
    private java.lang.String _objectBuilder;

    /**
     * Field _failure
     */
    private org.exolab.castor.tests.framework.testDescriptor.Failure _failure;

    /**
     * Field _skip
     */
    private boolean _skip;

    /**
     * keeps track of state for field: _skip
     */
    private boolean _has_skip;

    /**
     * Field _listener
     */
    private org.exolab.castor.tests.framework.testDescriptor.Listener _listener;


      //----------------/
     //- Constructors -/
    //----------------/

    public UnitTestCase() 
     {
        super();
    } //-- org.exolab.castor.tests.framework.testDescriptor.UnitTestCase()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteSkip()
    {
        this._has_skip= false;
    } //-- void deleteSkip() 

    /**
     * Returns the value of field 'configuration'.
     * 
     * @return the value of field 'Configuration'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Configuration getConfiguration()
    {
        return this._configuration;
    } //-- org.exolab.castor.tests.framework.testDescriptor.Configuration getConfiguration() 

    /**
     * Returns the value of field 'failure'.
     * 
     * @return the value of field 'Failure'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Failure getFailure()
    {
        return this._failure;
    } //-- org.exolab.castor.tests.framework.testDescriptor.Failure getFailure() 

    /**
     * Returns the value of field 'goldFile'.
     * 
     * @return the value of field 'GoldFile'.
     */
    public java.lang.String getGoldFile()
    {
        return this._goldFile;
    } //-- java.lang.String getGoldFile() 

    /**
     * Returns the value of field 'input'.
     * 
     * @return the value of field 'Input'.
     */
    public java.lang.String getInput()
    {
        return this._input;
    } //-- java.lang.String getInput() 

    /**
     * Returns the value of field 'listener'.
     * 
     * @return the value of field 'Listener'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Listener getListener()
    {
        return this._listener;
    } //-- org.exolab.castor.tests.framework.testDescriptor.Listener getListener() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'objectBuilder'.
     * 
     * @return the value of field 'ObjectBuilder'.
     */
    public java.lang.String getObjectBuilder()
    {
        return this._objectBuilder;
    } //-- java.lang.String getObjectBuilder() 

    /**
     * Returns the value of field 'skip'.
     * 
     * @return the value of field 'Skip'.
     */
    public boolean getSkip()
    {
        return this._skip;
    } //-- boolean getSkip() 

    /**
     * Returns the value of field 'unitTestCaseChoice'.
     * 
     * @return the value of field 'UnitTestCaseChoice'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice getUnitTestCaseChoice()
    {
        return this._unitTestCaseChoice;
    } //-- org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice getUnitTestCaseChoice() 

    /**
     * Method hasSkip
     * 
     * 
     * 
     * @return true if at least one Skip has been added
     */
    public boolean hasSkip()
    {
        return this._has_skip;
    } //-- boolean hasSkip() 

    /**
     * Returns the value of field 'skip'.
     * 
     * @return the value of field 'Skip'.
     */
    public boolean isSkip()
    {
        return this._skip;
    } //-- boolean isSkip() 

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
     * Sets the value of field 'configuration'.
     * 
     * @param configuration the value of field 'configuration'.
     */
    public void setConfiguration(org.exolab.castor.tests.framework.testDescriptor.Configuration configuration)
    {
        this._configuration = configuration;
    } //-- void setConfiguration(org.exolab.castor.tests.framework.testDescriptor.Configuration) 

    /**
     * Sets the value of field 'failure'.
     * 
     * @param failure the value of field 'failure'.
     */
    public void setFailure(org.exolab.castor.tests.framework.testDescriptor.Failure failure)
    {
        this._failure = failure;
    } //-- void setFailure(org.exolab.castor.tests.framework.testDescriptor.Failure) 

    /**
     * Sets the value of field 'goldFile'.
     * 
     * @param goldFile the value of field 'goldFile'.
     */
    public void setGoldFile(java.lang.String goldFile)
    {
        this._goldFile = goldFile;
    } //-- void setGoldFile(java.lang.String) 

    /**
     * Sets the value of field 'input'.
     * 
     * @param input the value of field 'input'.
     */
    public void setInput(java.lang.String input)
    {
        this._input = input;
    } //-- void setInput(java.lang.String) 

    /**
     * Sets the value of field 'listener'.
     * 
     * @param listener the value of field 'listener'.
     */
    public void setListener(org.exolab.castor.tests.framework.testDescriptor.Listener listener)
    {
        this._listener = listener;
    } //-- void setListener(org.exolab.castor.tests.framework.testDescriptor.Listener) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'objectBuilder'.
     * 
     * @param objectBuilder the value of field 'objectBuilder'.
     */
    public void setObjectBuilder(java.lang.String objectBuilder)
    {
        this._objectBuilder = objectBuilder;
    } //-- void setObjectBuilder(java.lang.String) 

    /**
     * Sets the value of field 'skip'.
     * 
     * @param skip the value of field 'skip'.
     */
    public void setSkip(boolean skip)
    {
        this._skip = skip;
        this._has_skip = true;
    } //-- void setSkip(boolean) 

    /**
     * Sets the value of field 'unitTestCaseChoice'.
     * 
     * @param unitTestCaseChoice the value of field
     * 'unitTestCaseChoice'.
     */
    public void setUnitTestCaseChoice(org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice unitTestCaseChoice)
    {
        this._unitTestCaseChoice = unitTestCaseChoice;
    } //-- void setUnitTestCaseChoice(org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice) 

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
     * org.exolab.castor.tests.framework.testDescriptor.UnitTestCase
     */
    public static org.exolab.castor.tests.framework.testDescriptor.UnitTestCase unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.UnitTestCase.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.UnitTestCase unmarshal(java.io.Reader) 

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
