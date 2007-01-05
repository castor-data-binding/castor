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
     * Field _name.
     */
    private java.lang.String _name;

    /**
     * Field _unitTestCaseChoice.
     */
    private org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice _unitTestCaseChoice;

    /**
     * Field _customTest.
     */
    private org.exolab.castor.tests.framework.testDescriptor.CustomTest _customTest;

    /**
     */
    private org.exolab.castor.tests.framework.testDescriptor.Configuration _configuration;

    /**
     * Field _input.
     */
    private java.lang.String _input;

    /**
     * Field _goldFile.
     */
    private java.lang.String _goldFile;

    /**
     * Field _objectBuilder.
     */
    private java.lang.String _objectBuilder;

    /**
     * Field _failure.
     */
    private org.exolab.castor.tests.framework.testDescriptor.Failure _failure;

    /**
     * Field _skip.
     */
    private boolean _skip;

    /**
     * keeps track of state for field: _skip
     */
    private boolean _has_skip;

    /**
     * Field _listener.
     */
    private org.exolab.castor.tests.framework.testDescriptor.Listener _listener;

    /**
     * Field _schemaDifferencesList.
     */
    private java.util.Vector _schemaDifferencesList;

    /**
     * Field _commentList.
     */
    private java.util.Vector _commentList;

    /**
     * Field _javaSourceVersion.
     */
    private float _javaSourceVersion;

    /**
     * keeps track of state for field: _javaSourceVersion
     */
    private boolean _has_javaSourceVersion;


      //----------------/
     //- Constructors -/
    //----------------/

    public UnitTestCase() {
        super();
        this._schemaDifferencesList = new java.util.Vector();
        this._commentList = new java.util.Vector();
    }


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
    public void addComment(
            final java.lang.String vComment)
    throws java.lang.IndexOutOfBoundsException {
        this._commentList.addElement(vComment);
    }

    /**
     * 
     * 
     * @param index
     * @param vComment
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addComment(
            final int index,
            final java.lang.String vComment)
    throws java.lang.IndexOutOfBoundsException {
        this._commentList.add(index, vComment);
    }

    /**
     * 
     * 
     * @param vSchemaDifferences
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSchemaDifferences(
            final org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences vSchemaDifferences)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._schemaDifferencesList.size() >= 2) {
            throw new IndexOutOfBoundsException("addSchemaDifferences has a maximum of 2");
        }
        
        this._schemaDifferencesList.addElement(vSchemaDifferences);
    }

    /**
     * 
     * 
     * @param index
     * @param vSchemaDifferences
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSchemaDifferences(
            final int index,
            final org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences vSchemaDifferences)
    throws java.lang.IndexOutOfBoundsException {
        // check for the maximum size
        if (this._schemaDifferencesList.size() >= 2) {
            throw new IndexOutOfBoundsException("addSchemaDifferences has a maximum of 2");
        }
        
        this._schemaDifferencesList.add(index, vSchemaDifferences);
    }

    /**
     */
    public void deleteJavaSourceVersion(
    ) {
        this._has_javaSourceVersion= false;
    }

    /**
     */
    public void deleteSkip(
    ) {
        this._has_skip= false;
    }

    /**
     * Method enumerateComment.
     * 
     * @return an Enumeration over all java.lang.String elements
     */
    public java.util.Enumeration enumerateComment(
    ) {
        return this._commentList.elements();
    }

    /**
     * Method enumerateSchemaDifferences.
     * 
     * @return an Enumeration over all
     * org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences
     * elements
     */
    public java.util.Enumeration enumerateSchemaDifferences(
    ) {
        return this._schemaDifferencesList.elements();
    }

    /**
     * Method getComment.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.String at the given index
     */
    public java.lang.String getComment(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._commentList.size()) {
            throw new IndexOutOfBoundsException("getComment: Index value '" + index + "' not in range [0.." + (this._commentList.size() - 1) + "]");
        }
        
        return (java.lang.String) _commentList.get(index);
    }

    /**
     * Method getComment.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public java.lang.String[] getComment(
    ) {
        java.lang.String[] array = new java.lang.String[0];
        return (java.lang.String[]) this._commentList.toArray(array);
    }

    /**
     * Method getCommentCount.
     * 
     * @return the size of this collection
     */
    public int getCommentCount(
    ) {
        return this._commentList.size();
    }

    /**
     * Returns the value of field 'configuration'.
     * 
     * @return the value of field 'Configuration'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Configuration getConfiguration(
    ) {
        return this._configuration;
    }

    /**
     * Returns the value of field 'customTest'.
     * 
     * @return the value of field 'CustomTest'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.CustomTest getCustomTest(
    ) {
        return this._customTest;
    }

    /**
     * Returns the value of field 'failure'.
     * 
     * @return the value of field 'Failure'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Failure getFailure(
    ) {
        return this._failure;
    }

    /**
     * Returns the value of field 'goldFile'.
     * 
     * @return the value of field 'GoldFile'.
     */
    public java.lang.String getGoldFile(
    ) {
        return this._goldFile;
    }

    /**
     * Returns the value of field 'input'.
     * 
     * @return the value of field 'Input'.
     */
    public java.lang.String getInput(
    ) {
        return this._input;
    }

    /**
     * Returns the value of field 'javaSourceVersion'.
     * 
     * @return the value of field 'JavaSourceVersion'.
     */
    public float getJavaSourceVersion(
    ) {
        return this._javaSourceVersion;
    }

    /**
     * Returns the value of field 'listener'.
     * 
     * @return the value of field 'Listener'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Listener getListener(
    ) {
        return this._listener;
    }

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Returns the value of field 'objectBuilder'.
     * 
     * @return the value of field 'ObjectBuilder'.
     */
    public java.lang.String getObjectBuilder(
    ) {
        return this._objectBuilder;
    }

    /**
     * Method getSchemaDifferences.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences
     * at the given index
     */
    public org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences getSchemaDifferences(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._schemaDifferencesList.size()) {
            throw new IndexOutOfBoundsException("getSchemaDifferences: Index value '" + index + "' not in range [0.." + (this._schemaDifferencesList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences) _schemaDifferencesList.get(index);
    }

    /**
     * Method getSchemaDifferences.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences[] getSchemaDifferences(
    ) {
        org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences[] array = new org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences[0];
        return (org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences[]) this._schemaDifferencesList.toArray(array);
    }

    /**
     * Method getSchemaDifferencesCount.
     * 
     * @return the size of this collection
     */
    public int getSchemaDifferencesCount(
    ) {
        return this._schemaDifferencesList.size();
    }

    /**
     * Returns the value of field 'skip'.
     * 
     * @return the value of field 'Skip'.
     */
    public boolean getSkip(
    ) {
        return this._skip;
    }

    /**
     * Returns the value of field 'unitTestCaseChoice'.
     * 
     * @return the value of field 'UnitTestCaseChoice'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice getUnitTestCaseChoice(
    ) {
        return this._unitTestCaseChoice;
    }

    /**
     * Method hasJavaSourceVersion.
     * 
     * @return true if at least one JavaSourceVersion has been added
     */
    public boolean hasJavaSourceVersion(
    ) {
        return this._has_javaSourceVersion;
    }

    /**
     * Method hasSkip.
     * 
     * @return true if at least one Skip has been added
     */
    public boolean hasSkip(
    ) {
        return this._has_skip;
    }

    /**
     * Returns the value of field 'skip'.
     * 
     * @return the value of field 'Skip'.
     */
    public boolean isSkip(
    ) {
        return this._skip;
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
     */
    public void removeAllComment(
    ) {
        this._commentList.clear();
    }

    /**
     */
    public void removeAllSchemaDifferences(
    ) {
        this._schemaDifferencesList.clear();
    }

    /**
     * Method removeComment.
     * 
     * @param vComment
     * @return true if the object was removed from the collection.
     */
    public boolean removeComment(
            final java.lang.String vComment) {
        boolean removed = _commentList.remove(vComment);
        return removed;
    }

    /**
     * Method removeCommentAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.String removeCommentAt(
            final int index) {
        Object obj = this._commentList.remove(index);
        return (java.lang.String) obj;
    }

    /**
     * Method removeSchemaDifferences.
     * 
     * @param vSchemaDifferences
     * @return true if the object was removed from the collection.
     */
    public boolean removeSchemaDifferences(
            final org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences vSchemaDifferences) {
        boolean removed = _schemaDifferencesList.remove(vSchemaDifferences);
        return removed;
    }

    /**
     * Method removeSchemaDifferencesAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences removeSchemaDifferencesAt(
            final int index) {
        Object obj = this._schemaDifferencesList.remove(index);
        return (org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vComment
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setComment(
            final int index,
            final java.lang.String vComment)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._commentList.size()) {
            throw new IndexOutOfBoundsException("setComment: Index value '" + index + "' not in range [0.." + (this._commentList.size() - 1) + "]");
        }
        
        this._commentList.set(index, vComment);
    }

    /**
     * 
     * 
     * @param vCommentArray
     */
    public void setComment(
            final java.lang.String[] vCommentArray) {
        //-- copy array
        _commentList.clear();
        
        for (int i = 0; i < vCommentArray.length; i++) {
                this._commentList.add(vCommentArray[i]);
        }
    }

    /**
     * Sets the value of field 'configuration'.
     * 
     * @param configuration the value of field 'configuration'.
     */
    public void setConfiguration(
            final org.exolab.castor.tests.framework.testDescriptor.Configuration configuration) {
        this._configuration = configuration;
    }

    /**
     * Sets the value of field 'customTest'.
     * 
     * @param customTest the value of field 'customTest'.
     */
    public void setCustomTest(
            final org.exolab.castor.tests.framework.testDescriptor.CustomTest customTest) {
        this._customTest = customTest;
    }

    /**
     * Sets the value of field 'failure'.
     * 
     * @param failure the value of field 'failure'.
     */
    public void setFailure(
            final org.exolab.castor.tests.framework.testDescriptor.Failure failure) {
        this._failure = failure;
    }

    /**
     * Sets the value of field 'goldFile'.
     * 
     * @param goldFile the value of field 'goldFile'.
     */
    public void setGoldFile(
            final java.lang.String goldFile) {
        this._goldFile = goldFile;
    }

    /**
     * Sets the value of field 'input'.
     * 
     * @param input the value of field 'input'.
     */
    public void setInput(
            final java.lang.String input) {
        this._input = input;
    }

    /**
     * Sets the value of field 'javaSourceVersion'.
     * 
     * @param javaSourceVersion the value of field
     * 'javaSourceVersion'.
     */
    public void setJavaSourceVersion(
            final float javaSourceVersion) {
        this._javaSourceVersion = javaSourceVersion;
        this._has_javaSourceVersion = true;
    }

    /**
     * Sets the value of field 'listener'.
     * 
     * @param listener the value of field 'listener'.
     */
    public void setListener(
            final org.exolab.castor.tests.framework.testDescriptor.Listener listener) {
        this._listener = listener;
    }

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(
            final java.lang.String name) {
        this._name = name;
    }

    /**
     * Sets the value of field 'objectBuilder'.
     * 
     * @param objectBuilder the value of field 'objectBuilder'.
     */
    public void setObjectBuilder(
            final java.lang.String objectBuilder) {
        this._objectBuilder = objectBuilder;
    }

    /**
     * 
     * 
     * @param index
     * @param vSchemaDifferences
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setSchemaDifferences(
            final int index,
            final org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences vSchemaDifferences)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._schemaDifferencesList.size()) {
            throw new IndexOutOfBoundsException("setSchemaDifferences: Index value '" + index + "' not in range [0.." + (this._schemaDifferencesList.size() - 1) + "]");
        }
        
        this._schemaDifferencesList.set(index, vSchemaDifferences);
    }

    /**
     * 
     * 
     * @param vSchemaDifferencesArray
     */
    public void setSchemaDifferences(
            final org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences[] vSchemaDifferencesArray) {
        //-- copy array
        _schemaDifferencesList.clear();
        
        for (int i = 0; i < vSchemaDifferencesArray.length; i++) {
                this._schemaDifferencesList.add(vSchemaDifferencesArray[i]);
        }
    }

    /**
     * Sets the value of field 'skip'.
     * 
     * @param skip the value of field 'skip'.
     */
    public void setSkip(
            final boolean skip) {
        this._skip = skip;
        this._has_skip = true;
    }

    /**
     * Sets the value of field 'unitTestCaseChoice'.
     * 
     * @param unitTestCaseChoice the value of field
     * 'unitTestCaseChoice'.
     */
    public void setUnitTestCaseChoice(
            final org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice unitTestCaseChoice) {
        this._unitTestCaseChoice = unitTestCaseChoice;
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
     * org.exolab.castor.tests.framework.testDescriptor.UnitTestCase
     */
    public static org.exolab.castor.tests.framework.testDescriptor.UnitTestCase unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.UnitTestCase.class, reader);
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
