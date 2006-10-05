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
 * Encapsulates information about a bug fix, including the
 * reporter's
 *  name (or Email address), the date of the report and of the fix,
 *  and one or more comments about the bug.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class BugFix implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _reporter
     */
    private java.lang.String _reporter;

    /**
     * Field _date_Report
     */
    private org.exolab.castor.types.Date _date_Report;

    /**
     * Field _date_Fix
     */
    private org.exolab.castor.types.Date _date_Fix;

    /**
     * Field _commentList
     */
    private java.util.Vector _commentList;


      //----------------/
     //- Constructors -/
    //----------------/

    public BugFix() 
     {
        super();
        this._commentList = new java.util.Vector();
    } //-- org.exolab.castor.tests.framework.testDescriptor.BugFix()


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
     * Returns the value of field 'date_Fix'.
     * 
     * @return the value of field 'Date_Fix'.
     */
    public org.exolab.castor.types.Date getDate_Fix()
    {
        return this._date_Fix;
    } //-- org.exolab.castor.types.Date getDate_Fix() 

    /**
     * Returns the value of field 'date_Report'.
     * 
     * @return the value of field 'Date_Report'.
     */
    public org.exolab.castor.types.Date getDate_Report()
    {
        return this._date_Report;
    } //-- org.exolab.castor.types.Date getDate_Report() 

    /**
     * Returns the value of field 'reporter'.
     * 
     * @return the value of field 'Reporter'.
     */
    public java.lang.String getReporter()
    {
        return this._reporter;
    } //-- java.lang.String getReporter() 

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
     * Sets the value of field 'date_Fix'.
     * 
     * @param date_Fix the value of field 'date_Fix'.
     */
    public void setDate_Fix(org.exolab.castor.types.Date date_Fix)
    {
        this._date_Fix = date_Fix;
    } //-- void setDate_Fix(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'date_Report'.
     * 
     * @param date_Report the value of field 'date_Report'.
     */
    public void setDate_Report(org.exolab.castor.types.Date date_Report)
    {
        this._date_Report = date_Report;
    } //-- void setDate_Report(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'reporter'.
     * 
     * @param reporter the value of field 'reporter'.
     */
    public void setReporter(java.lang.String reporter)
    {
        this._reporter = reporter;
    } //-- void setReporter(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return the unmarshaled
     * org.exolab.castor.tests.framework.testDescriptor.BugFix
     */
    public static org.exolab.castor.tests.framework.testDescriptor.BugFix unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.BugFix) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.BugFix.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.BugFix unmarshal(java.io.Reader) 

    /**
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
