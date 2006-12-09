/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.5</a>, using an XML
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
     * Field _customTest
     */
    private org.exolab.castor.tests.framework.testDescriptor.CustomTest _customTest;

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

    /**
     * Field _schemaDifferencesList
     */
    private java.util.Vector _schemaDifferencesList;

    /**
     * Field _commentList
     */
    private java.util.Vector _commentList;


      //----------------/
     //- Constructors -/
    //----------------/

    public UnitTestCase() 
     {
        super();
        this._schemaDifferencesList = new java.util.Vector();
        this._commentList = new java.util.Vector();
    } //-- org.exolab.castor.tests.framework.testDescriptor.UnitTestCase()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vComment
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addComment(java.lang.String vComment)
        throws java.lang.IndexOutOfBoundsException
    {
        this._commentList.addElement(vComment);
    } //-- void addComment(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vComment
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addComment(int index, java.lang.String vComment)
        throws java.lang.IndexOutOfBoundsException
    {
        this._commentList.add(index, vComment);
    } //-- void addComment(int, java.lang.String) 

    /**
     * 
     * 
     * @param vSchemaDifferences
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSchemaDifferences(org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences vSchemaDifferences)
        throws java.lang.IndexOutOfBoundsException
    {
        // check for the maximum size
        if (this._schemaDifferencesList.size() >= 2) {
            throw new IndexOutOfBoundsException("addSchemaDifferences has a maximum of 2");
        }
        
        this._schemaDifferencesList.addElement(vSchemaDifferences);
    } //-- void addSchemaDifferences(org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences) 

    /**
     * 
     * 
     * @param index
     * @param vSchemaDifferences
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSchemaDifferences(int index, org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences vSchemaDifferences)
        throws java.lang.IndexOutOfBoundsException
    {
        // check for the maximum size
        if (this._schemaDifferencesList.size() >= 2) {
            throw new IndexOutOfBoundsException("addSchemaDifferences has a maximum of 2");
        }
        
        this._schemaDifferencesList.add(index, vSchemaDifferences);
    } //-- void addSchemaDifferences(int, org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences) 

    /**
     */
    public void deleteSkip()
    {
        this._has_skip= false;
    } //-- void deleteSkip() 

    /**
     * Method enumerateComment
     * 
     * 
     * 
     * @return an Enumeration over all java.lang.String elements
     */
    public java.util.Enumeration enumerateComment()
    {
        return this._commentList.elements();
    } //-- java.util.Enumeration enumerateComment() 

    /**
     * Method enumerateSchemaDifferences
     * 
     * 
     * 
     * @return an Enumeration over all
     * org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences
     * elements
     */
    public java.util.Enumeration enumerateSchemaDifferences()
    {
        return this._schemaDifferencesList.elements();
    } //-- java.util.Enumeration enumerateSchemaDifferences() 

    /**
     * Method getComment
     * 
     * 
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.String at the given index
     */
    public java.lang.String getComment(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._commentList.size()) {
            throw new IndexOutOfBoundsException("getComment: Index value '" + index + "' not in range [0.." + (this._commentList.size() - 1) + "]");
        }
        
        return (String)_commentList.get(index);
    } //-- java.lang.String getComment(int) 

    /**
     * Method getComment
     * 
     * 
     * 
     * @return this collection as an Array
     */
    public java.lang.String[] getComment()
    {
        int size = this._commentList.size();
        java.lang.String[] array = new java.lang.String[size];
        for (int index = 0; index < size; index++){
            array[index] = (String)_commentList.get(index);
        }
        
        return array;
    } //-- java.lang.String[] getComment() 

    /**
     * Method getCommentCount
     * 
     * 
     * 
     * @return the size of this collection
     */
    public int getCommentCount()
    {
        return this._commentList.size();
    } //-- int getCommentCount() 

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
     * Returns the value of field 'customTest'.
     * 
     * @return the value of field 'CustomTest'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.CustomTest getCustomTest()
    {
        return this._customTest;
    } //-- org.exolab.castor.tests.framework.testDescriptor.CustomTest getCustomTest() 

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
     * Method getSchemaDifferences
     * 
     * 
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences
     * at the given index
     */
    public org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences getSchemaDifferences(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._schemaDifferencesList.size()) {
            throw new IndexOutOfBoundsException("getSchemaDifferences: Index value '" + index + "' not in range [0.." + (this._schemaDifferencesList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences) _schemaDifferencesList.get(index);
    } //-- org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences getSchemaDifferences(int) 

    /**
     * Method getSchemaDifferences
     * 
     * 
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences[] getSchemaDifferences()
    {
        int size = this._schemaDifferencesList.size();
        org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences[] array = new org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences[size];
        for (int index = 0; index < size; index++){
            array[index] = (org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences) _schemaDifferencesList.get(index);
        }
        
        return array;
    } //-- org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences[] getSchemaDifferences() 

    /**
     * Method getSchemaDifferencesCount
     * 
     * 
     * 
     * @return the size of this collection
     */
    public int getSchemaDifferencesCount()
    {
        return this._schemaDifferencesList.size();
    } //-- int getSchemaDifferencesCount() 

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
     */
    public void removeAllComment()
    {
        this._commentList.clear();
    } //-- void removeAllComment() 

    /**
     */
    public void removeAllSchemaDifferences()
    {
        this._schemaDifferencesList.clear();
    } //-- void removeAllSchemaDifferences() 

    /**
     * Method removeComment
     * 
     * 
     * 
     * @param vComment
     * @return true if the object was removed from the collection.
     */
    public boolean removeComment(java.lang.String vComment)
    {
        boolean removed = _commentList.remove(vComment);
        return removed;
    } //-- boolean removeComment(java.lang.String) 

    /**
     * Method removeCommentAt
     * 
     * 
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.String removeCommentAt(int index)
    {
        Object obj = this._commentList.remove(index);
        return (String)obj;
    } //-- java.lang.String removeCommentAt(int) 

    /**
     * Method removeSchemaDifferences
     * 
     * 
     * 
     * @param vSchemaDifferences
     * @return true if the object was removed from the collection.
     */
    public boolean removeSchemaDifferences(org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences vSchemaDifferences)
    {
        boolean removed = _schemaDifferencesList.remove(vSchemaDifferences);
        return removed;
    } //-- boolean removeSchemaDifferences(org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences) 

    /**
     * Method removeSchemaDifferencesAt
     * 
     * 
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences removeSchemaDifferencesAt(int index)
    {
        Object obj = this._schemaDifferencesList.remove(index);
        return (org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences) obj;
    } //-- org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences removeSchemaDifferencesAt(int) 

    /**
     * 
     * 
     * @param index
     * @param vComment
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setComment(int index, java.lang.String vComment)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._commentList.size()) {
            throw new IndexOutOfBoundsException("setComment: Index value '" + index + "' not in range [0.." + (this._commentList.size() - 1) + "]");
        }
        
        this._commentList.set(index, vComment);
    } //-- void setComment(int, java.lang.String) 

    /**
     * 
     * 
     * @param vCommentArray
     */
    public void setComment(java.lang.String[] vCommentArray)
    {
        //-- copy array
        _commentList.clear();
        
        for (int i = 0; i < vCommentArray.length; i++) {
                this._commentList.add(vCommentArray[i]);
        }
    } //-- void setComment(java.lang.String) 

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
     * Sets the value of field 'customTest'.
     * 
     * @param customTest the value of field 'customTest'.
     */
    public void setCustomTest(org.exolab.castor.tests.framework.testDescriptor.CustomTest customTest)
    {
        this._customTest = customTest;
    } //-- void setCustomTest(org.exolab.castor.tests.framework.testDescriptor.CustomTest) 

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
     * 
     * 
     * @param index
     * @param vSchemaDifferences
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setSchemaDifferences(int index, org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences vSchemaDifferences)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._schemaDifferencesList.size()) {
            throw new IndexOutOfBoundsException("setSchemaDifferences: Index value '" + index + "' not in range [0.." + (this._schemaDifferencesList.size() - 1) + "]");
        }
        
        this._schemaDifferencesList.set(index, vSchemaDifferences);
    } //-- void setSchemaDifferences(int, org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences) 

    /**
     * 
     * 
     * @param vSchemaDifferencesArray
     */
    public void setSchemaDifferences(org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences[] vSchemaDifferencesArray)
    {
        //-- copy array
        _schemaDifferencesList.clear();
        
        for (int i = 0; i < vSchemaDifferencesArray.length; i++) {
                this._schemaDifferencesList.add(vSchemaDifferencesArray[i]);
        }
    } //-- void setSchemaDifferences(org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences) 

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
