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
 * @version $Revision$ $Date$
 */
public class TestDescriptor implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _author
     */
    private java.lang.String _author;

    /**
     * Field _commentList
     */
    private java.util.Vector _commentList;

    /**
     * Field _category
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
     * Field _testDescriptorChoice
     */
    private org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice _testDescriptorChoice;


      //----------------/
     //- Constructors -/
    //----------------/

    public TestDescriptor() 
     {
        super();
        this._commentList = new java.util.Vector();
    } //-- org.exolab.castor.tests.framework.testDescriptor.TestDescriptor()


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
     * Returns the value of field 'author'.
     * 
     * @return the value of field 'Author'.
     */
    public java.lang.String getAuthor()
    {
        return this._author;
    } //-- java.lang.String getAuthor() 

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
    public org.exolab.castor.tests.framework.testDescriptor.BugFix getBugFix()
    {
        return this._bugFix;
    } //-- org.exolab.castor.tests.framework.testDescriptor.BugFix getBugFix() 

    /**
     * Returns the value of field 'category'.
     * 
     * @return the value of field 'Category'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.types.CategoryType getCategory()
    {
        return this._category;
    } //-- org.exolab.castor.tests.framework.testDescriptor.types.CategoryType getCategory() 

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
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'testDescriptorChoice'.
     * 
     * @return the value of field 'TestDescriptorChoice'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice getTestDescriptorChoice()
    {
        return this._testDescriptorChoice;
    } //-- org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice getTestDescriptorChoice() 

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
     * Sets the value of field 'author'.
     * 
     * @param author the value of field 'author'.
     */
    public void setAuthor(java.lang.String author)
    {
        this._author = author;
    } //-- void setAuthor(java.lang.String) 

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
    public void setBugFix(org.exolab.castor.tests.framework.testDescriptor.BugFix bugFix)
    {
        this._bugFix = bugFix;
    } //-- void setBugFix(org.exolab.castor.tests.framework.testDescriptor.BugFix) 

    /**
     * Sets the value of field 'category'.
     * 
     * @param category the value of field 'category'.
     */
    public void setCategory(org.exolab.castor.tests.framework.testDescriptor.types.CategoryType category)
    {
        this._category = category;
    } //-- void setCategory(org.exolab.castor.tests.framework.testDescriptor.types.CategoryType) 

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
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'testDescriptorChoice'.
     * 
     * @param testDescriptorChoice the value of field
     * 'testDescriptorChoice'.
     */
    public void setTestDescriptorChoice(org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice testDescriptorChoice)
    {
        this._testDescriptorChoice = testDescriptorChoice;
    } //-- void setTestDescriptorChoice(org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice) 

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
     * org.exolab.castor.tests.framework.testDescriptor.TestDescriptor
     */
    public static org.exolab.castor.tests.framework.testDescriptor.TestDescriptor unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.TestDescriptor) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.TestDescriptor.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.TestDescriptor unmarshal(java.io.Reader) 

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
