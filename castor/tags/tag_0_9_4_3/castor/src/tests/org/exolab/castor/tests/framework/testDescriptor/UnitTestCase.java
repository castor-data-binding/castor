/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class UnitTestCase.
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
     * Field _input
     */
    private java.lang.String _input;

    /**
     * Field _goldFile
     */
    private java.lang.String _goldFile;

    /**
     * Field _output
     */
    private java.lang.String _output;

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

    public UnitTestCase() {
        super();
    } //-- org.exolab.castor.tests.framework.testDescriptor.UnitTestCase()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteSkip
     */
    public void deleteSkip()
    {
        this._has_skip= false;
    } //-- void deleteSkip() 

    /**
     * Method getFailureReturns the value of field 'failure'.
     * 
     * @return the value of field 'failure'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Failure getFailure()
    {
        return this._failure;
    } //-- org.exolab.castor.tests.framework.testDescriptor.Failure getFailure() 

    /**
     * Method getGoldFileReturns the value of field 'goldFile'.
     * 
     * @return the value of field 'goldFile'.
     */
    public java.lang.String getGoldFile()
    {
        return this._goldFile;
    } //-- java.lang.String getGoldFile() 

    /**
     * Method getInputReturns the value of field 'input'.
     * 
     * @return the value of field 'input'.
     */
    public java.lang.String getInput()
    {
        return this._input;
    } //-- java.lang.String getInput() 

    /**
     * Method getListenerReturns the value of field 'listener'.
     * 
     * @return the value of field 'listener'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Listener getListener()
    {
        return this._listener;
    } //-- org.exolab.castor.tests.framework.testDescriptor.Listener getListener() 

    /**
     * Method getNameReturns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Method getObjectBuilderReturns the value of field
     * 'objectBuilder'.
     * 
     * @return the value of field 'objectBuilder'.
     */
    public java.lang.String getObjectBuilder()
    {
        return this._objectBuilder;
    } //-- java.lang.String getObjectBuilder() 

    /**
     * Method getOutputReturns the value of field 'output'.
     * 
     * @return the value of field 'output'.
     */
    public java.lang.String getOutput()
    {
        return this._output;
    } //-- java.lang.String getOutput() 

    /**
     * Method getSkipReturns the value of field 'skip'.
     * 
     * @return the value of field 'skip'.
     */
    public boolean getSkip()
    {
        return this._skip;
    } //-- boolean getSkip() 

    /**
     * Method getUnitTestCaseChoiceReturns the value of field
     * 'unitTestCaseChoice'.
     * 
     * @return the value of field 'unitTestCaseChoice'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice getUnitTestCaseChoice()
    {
        return this._unitTestCaseChoice;
    } //-- org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice getUnitTestCaseChoice() 

    /**
     * Method hasSkip
     */
    public boolean hasSkip()
    {
        return this._has_skip;
    } //-- boolean hasSkip() 

    /**
     * Method isValid
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
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method setFailureSets the value of field 'failure'.
     * 
     * @param failure the value of field 'failure'.
     */
    public void setFailure(org.exolab.castor.tests.framework.testDescriptor.Failure failure)
    {
        this._failure = failure;
    } //-- void setFailure(org.exolab.castor.tests.framework.testDescriptor.Failure) 

    /**
     * Method setGoldFileSets the value of field 'goldFile'.
     * 
     * @param goldFile the value of field 'goldFile'.
     */
    public void setGoldFile(java.lang.String goldFile)
    {
        this._goldFile = goldFile;
    } //-- void setGoldFile(java.lang.String) 

    /**
     * Method setInputSets the value of field 'input'.
     * 
     * @param input the value of field 'input'.
     */
    public void setInput(java.lang.String input)
    {
        this._input = input;
    } //-- void setInput(java.lang.String) 

    /**
     * Method setListenerSets the value of field 'listener'.
     * 
     * @param listener the value of field 'listener'.
     */
    public void setListener(org.exolab.castor.tests.framework.testDescriptor.Listener listener)
    {
        this._listener = listener;
    } //-- void setListener(org.exolab.castor.tests.framework.testDescriptor.Listener) 

    /**
     * Method setNameSets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Method setObjectBuilderSets the value of field
     * 'objectBuilder'.
     * 
     * @param objectBuilder the value of field 'objectBuilder'.
     */
    public void setObjectBuilder(java.lang.String objectBuilder)
    {
        this._objectBuilder = objectBuilder;
    } //-- void setObjectBuilder(java.lang.String) 

    /**
     * Method setOutputSets the value of field 'output'.
     * 
     * @param output the value of field 'output'.
     */
    public void setOutput(java.lang.String output)
    {
        this._output = output;
    } //-- void setOutput(java.lang.String) 

    /**
     * Method setSkipSets the value of field 'skip'.
     * 
     * @param skip the value of field 'skip'.
     */
    public void setSkip(boolean skip)
    {
        this._skip = skip;
        this._has_skip = true;
    } //-- void setSkip(boolean) 

    /**
     * Method setUnitTestCaseChoiceSets the value of field
     * 'unitTestCaseChoice'.
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
     * @param reader
     */
    public static org.exolab.castor.tests.framework.testDescriptor.UnitTestCase unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.UnitTestCase.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.UnitTestCase unmarshal(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
