/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.2</a>, using an
 * XML Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.tests.framework.testDescriptor.types.CategoryType;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class TestDescriptor implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private java.util.Vector _commentList;

    private org.exolab.castor.tests.framework.testDescriptor.types.CategoryType _category;

    private BugFix _bugFix;

    private SourceGeneratorTest _sourceGeneratorTest;

    private MarshallingTest _marshallingTest;


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
     * 
     * @param vComment
    **/
    public void addComment(java.lang.String vComment)
        throws java.lang.IndexOutOfBoundsException
    {
        _commentList.addElement(vComment);
    } //-- void addComment(java.lang.String) 

    /**
    **/
    public java.util.Enumeration enumerateComment()
    {
        return _commentList.elements();
    } //-- java.util.Enumeration enumerateComment() 

    /**
    **/
    public BugFix getBugFix()
    {
        return this._bugFix;
    } //-- BugFix getBugFix() 

    /**
    **/
    public org.exolab.castor.tests.framework.testDescriptor.types.CategoryType getCategory()
    {
        return this._category;
    } //-- org.exolab.castor.tests.framework.testDescriptor.types.CategoryType getCategory() 

    /**
     * 
     * @param index
    **/
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
    **/
    public java.lang.String[] getComment()
    {
        int size = _commentList.size();
        java.lang.String[] mArray = new String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_commentList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getComment() 

    /**
    **/
    public int getCommentCount()
    {
        return _commentList.size();
    } //-- int getCommentCount() 

    /**
    **/
    public MarshallingTest getMarshallingTest()
    {
        return this._marshallingTest;
    } //-- MarshallingTest getMarshallingTest() 

    /**
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
    **/
    public SourceGeneratorTest getSourceGeneratorTest()
    {
        return this._sourceGeneratorTest;
    } //-- SourceGeneratorTest getSourceGeneratorTest() 

    /**
    **/
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
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler) 

    /**
    **/
    public void removeAllComment()
    {
        _commentList.removeAllElements();
    } //-- void removeAllComment() 

    /**
     * 
     * @param index
    **/
    public java.lang.String removeComment(int index)
    {
        Object obj = _commentList.elementAt(index);
        _commentList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeComment(int) 

    /**
     * 
     * @param _bugFix
    **/
    public void setBugFix(BugFix _bugFix)
    {
        this._bugFix = _bugFix;
    } //-- void setBugFix(BugFix) 

    /**
     * 
     * @param _category
    **/
    public void setCategory(org.exolab.castor.tests.framework.testDescriptor.types.CategoryType _category)
    {
        this._category = _category;
    } //-- void setCategory(org.exolab.castor.tests.framework.testDescriptor.types.CategoryType) 

    /**
     * 
     * @param vComment
     * @param index
    **/
    public void setComment(java.lang.String vComment, int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _commentList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _commentList.setElementAt(vComment, index);
    } //-- void setComment(java.lang.String, int) 

    /**
     * 
     * @param commentArray
    **/
    public void setComment(java.lang.String[] commentArray)
    {
        //-- copy array
        _commentList.removeAllElements();
        for (int i = 0; i < commentArray.length; i++) {
            _commentList.addElement(commentArray[i]);
        }
    } //-- void setComment(java.lang.String) 

    /**
     * 
     * @param _marshallingTest
    **/
    public void setMarshallingTest(MarshallingTest _marshallingTest)
    {
        this._marshallingTest = _marshallingTest;
    } //-- void setMarshallingTest(MarshallingTest) 

    /**
     * 
     * @param _name
    **/
    public void setName(java.lang.String _name)
    {
        this._name = _name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * @param _sourceGeneratorTest
    **/
    public void setSourceGeneratorTest(SourceGeneratorTest _sourceGeneratorTest)
    {
        this._sourceGeneratorTest = _sourceGeneratorTest;
    } //-- void setSourceGeneratorTest(SourceGeneratorTest) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.tests.framework.testDescriptor.TestDescriptor unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.TestDescriptor) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.TestDescriptor.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.TestDescriptor unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
