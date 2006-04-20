/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
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
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.tests.framework.testDescriptor.types.CategoryType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class TestDescriptor.
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
     * Field _bugFix
     */
    private org.exolab.castor.tests.framework.testDescriptor.BugFix _bugFix;

    /**
     * Field _testDescriptorChoice
     */
    private org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice _testDescriptorChoice;


      //----------------/
     //- Constructors -/
    //----------------/

    public TestDescriptor() {
        super();
        _commentList = new Vector();
    } //-- org.exolab.castor.tests.framework.testDescriptor.TestDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addComment
     * 
     * @param vComment
     */
    public void addComment(java.lang.String vComment)
        throws java.lang.IndexOutOfBoundsException
    {
        _commentList.addElement(vComment);
    } //-- void addComment(java.lang.String) 

    /**
     * Method addComment
     * 
     * @param index
     * @param vComment
     */
    public void addComment(int index, java.lang.String vComment)
        throws java.lang.IndexOutOfBoundsException
    {
        _commentList.insertElementAt(vComment, index);
    } //-- void addComment(int, java.lang.String) 

    /**
     * Method enumerateComment
     */
    public java.util.Enumeration enumerateComment()
    {
        return _commentList.elements();
    } //-- java.util.Enumeration enumerateComment() 

    /**
     * Returns the value of field 'author'.
     * 
     * @return the value of field 'author'.
     */
    public java.lang.String getAuthor()
    {
        return this._author;
    } //-- java.lang.String getAuthor() 

    /**
     * Returns the value of field 'bugFix'.
     * 
     * @return the value of field 'bugFix'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.BugFix getBugFix()
    {
        return this._bugFix;
    } //-- org.exolab.castor.tests.framework.testDescriptor.BugFix getBugFix() 

    /**
     * Returns the value of field 'category'.
     * 
     * @return the value of field 'category'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.types.CategoryType getCategory()
    {
        return this._category;
    } //-- org.exolab.castor.tests.framework.testDescriptor.types.CategoryType getCategory() 

    /**
     * Method getComment
     * 
     * @param index
     */
    public java.lang.String getComment(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _commentList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_commentList.elementAt(index);
    } //-- java.lang.String getComment(int) 

    /**
     * Method getComment
     */
    public java.lang.String[] getComment()
    {
        int size = _commentList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_commentList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getComment() 

    /**
     * Method getCommentCount
     */
    public int getCommentCount()
    {
        return _commentList.size();
    } //-- int getCommentCount() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'testDescriptorChoice'.
     * 
     * @return the value of field 'testDescriptorChoice'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice getTestDescriptorChoice()
    {
        return this._testDescriptorChoice;
    } //-- org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice getTestDescriptorChoice() 

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
     * Method removeAllComment
     */
    public void removeAllComment()
    {
        _commentList.removeAllElements();
    } //-- void removeAllComment() 

    /**
     * Method removeComment
     * 
     * @param index
     */
    public java.lang.String removeComment(int index)
    {
        java.lang.Object obj = _commentList.elementAt(index);
        _commentList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeComment(int) 

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
     * Sets the value of field 'bugFix'.
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
     * Method setComment
     * 
     * @param index
     * @param vComment
     */
    public void setComment(int index, java.lang.String vComment)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _commentList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _commentList.setElementAt(vComment, index);
    } //-- void setComment(int, java.lang.String) 

    /**
     * Method setComment
     * 
     * @param commentArray
     */
    public void setComment(java.lang.String[] commentArray)
    {
        //-- copy array
        _commentList.removeAllElements();
        for (int i = 0; i < commentArray.length; i++) {
            _commentList.addElement(commentArray[i]);
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
     * Method unmarshalTestDescriptor
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTestDescriptor(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.TestDescriptor) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.TestDescriptor.class, reader);
    } //-- java.lang.Object unmarshalTestDescriptor(java.io.Reader) 

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
