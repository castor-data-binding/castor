/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
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
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class BugFix implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _reporter;

    private org.exolab.castor.types.Date _date_Report;

    private java.lang.String _fixer;

    private org.exolab.castor.types.Date _date_Fix;

    private java.util.Vector _commentList;


      //----------------/
     //- Constructors -/
    //----------------/

    public BugFix() {
        super();
        _commentList = new Vector();
    } //-- org.exolab.castor.tests.framework.testDescriptor.BugFix()


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
     * 
     * @param index
     * @param vComment
    **/
    public void addComment(int index, java.lang.String vComment)
        throws java.lang.IndexOutOfBoundsException
    {
        _commentList.insertElementAt(vComment, index);
    } //-- void addComment(int, java.lang.String) 

    /**
    **/
    public java.util.Enumeration enumerateComment()
    {
        return _commentList.elements();
    } //-- java.util.Enumeration enumerateComment() 

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
        java.lang.String[] mArray = new java.lang.String[size];
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
     * Returns the value of field 'date_Fix'.
     * @return the value of field 'date_Fix'.
    **/
    public org.exolab.castor.types.Date getDate_Fix()
    {
        return this._date_Fix;
    } //-- org.exolab.castor.types.Date getDate_Fix() 

    /**
     * Returns the value of field 'date_Report'.
     * @return the value of field 'date_Report'.
    **/
    public org.exolab.castor.types.Date getDate_Report()
    {
        return this._date_Report;
    } //-- org.exolab.castor.types.Date getDate_Report() 

    /**
     * Returns the value of field 'fixer'.
     * @return the value of field 'fixer'.
    **/
    public java.lang.String getFixer()
    {
        return this._fixer;
    } //-- java.lang.String getFixer() 

    /**
     * Returns the value of field 'reporter'.
     * @return the value of field 'reporter'.
    **/
    public java.lang.String getReporter()
    {
        return this._reporter;
    } //-- java.lang.String getReporter() 

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
        java.lang.Object obj = _commentList.elementAt(index);
        _commentList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeComment(int) 

    /**
     * 
     * @param index
     * @param vComment
    **/
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
     * Sets the value of field 'date_Fix'.
     * @param date_Fix the value of field 'date_Fix'.
    **/
    public void setDate_Fix(org.exolab.castor.types.Date date_Fix)
    {
        this._date_Fix = date_Fix;
    } //-- void setDate_Fix(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'date_Report'.
     * @param date_Report the value of field 'date_Report'.
    **/
    public void setDate_Report(org.exolab.castor.types.Date date_Report)
    {
        this._date_Report = date_Report;
    } //-- void setDate_Report(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'fixer'.
     * @param fixer the value of field 'fixer'.
    **/
    public void setFixer(java.lang.String fixer)
    {
        this._fixer = fixer;
    } //-- void setFixer(java.lang.String) 

    /**
     * Sets the value of field 'reporter'.
     * @param reporter the value of field 'reporter'.
    **/
    public void setReporter(java.lang.String reporter)
    {
        this._reporter = reporter;
    } //-- void setReporter(java.lang.String) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.tests.framework.testDescriptor.BugFix unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.BugFix) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.BugFix.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.BugFix unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
