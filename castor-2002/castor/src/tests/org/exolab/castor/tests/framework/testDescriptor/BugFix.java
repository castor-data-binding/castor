/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Date;
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

    private java.util.Date _dateReport;

    private java.lang.String _fixer;

    private java.util.Date _dateFix;

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
    public java.util.Date getDateFix()
    {
        return this._dateFix;
    } //-- java.util.Date getDateFix() 

    /**
    **/
    public java.util.Date getDateReport()
    {
        return this._dateReport;
    } //-- java.util.Date getDateReport() 

    /**
    **/
    public java.lang.String getFixer()
    {
        return this._fixer;
    } //-- java.lang.String getFixer() 

    /**
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
        Object obj = _commentList.elementAt(index);
        _commentList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeComment(int) 

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
     * @param _dateFix
    **/
    public void setDateFix(java.util.Date _dateFix)
    {
        this._dateFix = _dateFix;
    } //-- void setDateFix(java.util.Date) 

    /**
     * 
     * @param _dateReport
    **/
    public void setDateReport(java.util.Date _dateReport)
    {
        this._dateReport = _dateReport;
    } //-- void setDateReport(java.util.Date) 

    /**
     * 
     * @param _fixer
    **/
    public void setFixer(java.lang.String _fixer)
    {
        this._fixer = _fixer;
    } //-- void setFixer(java.lang.String) 

    /**
     * 
     * @param _reporter
    **/
    public void setReporter(java.lang.String _reporter)
    {
        this._reporter = _reporter;
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
