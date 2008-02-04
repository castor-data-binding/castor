/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
 * Schema.
 * $Id: TestDescriptor.java 6767 2007-01-19 05:53:39Z ekuns $
 */

package org.exolab.castor.tests.framework.testDescriptor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Castor Testing Framework Test Descriptor XML Schema
 *  <p>
 *  Namespace: http://castor.exolab.org/Test
 *  <p>
 *  This schema is used to generate the
 *  org.exolab.castor.tests.framework.testdescriptor package
 *  Note: This schema is under evolution and subject to change.
 *  This schema is under the Exolab license.
 *  
 * 
 * @version $Revision: 6767 $ $Date$
 */
public class TestDescriptor implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name.
     */
    private java.lang.String _name;

    /**
     * Field _author.
     */
    private java.lang.String _author;

    /**
     * Field _commentList.
     */
    private java.util.Vector _commentList;

    /**
     * Field _category.
     */
    private org.exolab.castor.tests.framework.testDescriptor.types.CategoryType _category;

    /**
     * Encapsulates information about a bug fix, including the
     * reporter's
     *  name (or Email address), the date of the report and of the
     * fix,
     *  and one or more comments about the bug.
     *  
     */
    private org.exolab.castor.tests.framework.testDescriptor.BugFix _bugFix;

    /**
     * Field _minimumJavaVersion.
     */
    private float _minimumJavaVersion;

    /**
     * keeps track of state for field: _minimumJavaVersion
     */
    private boolean _has_minimumJavaVersion;

    /**
     * Field _testDescriptorChoice.
     */
    private org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice _testDescriptorChoice;


      //----------------/
     //- Constructors -/
    //----------------/

    public TestDescriptor() {
        super();
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
     */
    public void deleteMinimumJavaVersion(
    ) {
        this._has_minimumJavaVersion= false;
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
     * Returns the value of field 'author'.
     * 
     * @return the value of field 'Author'.
     */
    public java.lang.String getAuthor(
    ) {
        return this._author;
    }

    /**
     * Returns the value of field 'bugFix'. The field 'bugFix' has
     * the following description: Encapsulates information about a
     * bug fix, including the reporter's
     *  name (or Email address), the date of the report and of the
     * fix,
     *  and one or more comments about the bug.
     *  
     * 
     * @return the value of field 'BugFix'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.BugFix getBugFix(
    ) {
        return this._bugFix;
    }

    /**
     * Returns the value of field 'category'.
     * 
     * @return the value of field 'Category'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.types.CategoryType getCategory(
    ) {
        return this._category;
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
     * Returns the value of field 'minimumJavaVersion'.
     * 
     * @return the value of field 'MinimumJavaVersion'.
     */
    public float getMinimumJavaVersion(
    ) {
        return this._minimumJavaVersion;
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
     * Returns the value of field 'testDescriptorChoice'.
     * 
     * @return the value of field 'TestDescriptorChoice'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice getTestDescriptorChoice(
    ) {
        return this._testDescriptorChoice;
    }

    /**
     * Method hasMinimumJavaVersion.
     * 
     * @return true if at least one MinimumJavaVersion has been adde
     */
    public boolean hasMinimumJavaVersion(
    ) {
        return this._has_minimumJavaVersion;
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
        java.lang.Object obj = this._commentList.remove(index);
        return (java.lang.String) obj;
    }

    /**
     * Sets the value of field 'author'.
     * 
     * @param author the value of field 'author'.
     */
    public void setAuthor(
            final java.lang.String author) {
        this._author = author;
    }

    /**
     * Sets the value of field 'bugFix'. The field 'bugFix' has the
     * following description: Encapsulates information about a bug
     * fix, including the reporter's
     *  name (or Email address), the date of the report and of the
     * fix,
     *  and one or more comments about the bug.
     *  
     * 
     * @param bugFix the value of field 'bugFix'.
     */
    public void setBugFix(
            final org.exolab.castor.tests.framework.testDescriptor.BugFix bugFix) {
        this._bugFix = bugFix;
    }

    /**
     * Sets the value of field 'category'.
     * 
     * @param category the value of field 'category'.
     */
    public void setCategory(
            final org.exolab.castor.tests.framework.testDescriptor.types.CategoryType category) {
        this._category = category;
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
     * Sets the value of field 'minimumJavaVersion'.
     * 
     * @param minimumJavaVersion the value of field
     * 'minimumJavaVersion'.
     */
    public void setMinimumJavaVersion(
            final float minimumJavaVersion) {
        this._minimumJavaVersion = minimumJavaVersion;
        this._has_minimumJavaVersion = true;
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
     * Sets the value of field 'testDescriptorChoice'.
     * 
     * @param testDescriptorChoice the value of field
     * 'testDescriptorChoice'.
     */
    public void setTestDescriptorChoice(
            final org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice testDescriptorChoice) {
        this._testDescriptorChoice = testDescriptorChoice;
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
     * org.exolab.castor.tests.framework.testDescriptor.TestDescriptor
     */
    public static org.exolab.castor.tests.framework.testDescriptor.TestDescriptor unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.tests.framework.testDescriptor.TestDescriptor) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.TestDescriptor.class, reader);
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
