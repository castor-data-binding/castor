/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
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
     * Field _reporter.
     */
    private java.lang.String _reporter;

    /**
     * Field _issue.
     */
    private java.lang.String _issue;

    /**
     * Field _date_Report.
     */
    private org.exolab.castor.types.Date _date_Report;

    /**
     * Field _date_Fix.
     */
    private org.exolab.castor.types.Date _date_Fix;

    /**
     * Field _commentList.
     */
    private java.util.Vector _commentList;


      //----------------/
     //- Constructors -/
    //----------------/

    public BugFix() {
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
     * Method enumerateComment.
     * 
     * @return an Enumeration over all java.lang.String elements
     */
    public java.util.Enumeration enumerateComment(
    ) {
        return this._commentList.elements();
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
     * Returns the value of field 'date_Fix'.
     * 
     * @return the value of field 'Date_Fix'.
     */
    public org.exolab.castor.types.Date getDate_Fix(
    ) {
        return this._date_Fix;
    }

    /**
     * Returns the value of field 'date_Report'.
     * 
     * @return the value of field 'Date_Report'.
     */
    public org.exolab.castor.types.Date getDate_Report(
    ) {
        return this._date_Report;
    }

    /**
     * Returns the value of field 'issue'.
     * 
     * @return the value of field 'Issue'.
     */
    public java.lang.String getIssue(
    ) {
        return this._issue;
    }

    /**
     * Returns the value of field 'reporter'.
     * 
     * @return the value of field 'Reporter'.
     */
    public java.lang.String getReporter(
    ) {
        return this._reporter;
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
     * Sets the value of field 'date_Fix'.
     * 
     * @param date_Fix the value of field 'date_Fix'.
     */
    public void setDate_Fix(
            final org.exolab.castor.types.Date date_Fix) {
        this._date_Fix = date_Fix;
    }

    /**
     * Sets the value of field 'date_Report'.
     * 
     * @param date_Report the value of field 'date_Report'.
     */
    public void setDate_Report(
            final org.exolab.castor.types.Date date_Report) {
        this._date_Report = date_Report;
    }

    /**
     * Sets the value of field 'issue'.
     * 
     * @param issue the value of field 'issue'.
     */
    public void setIssue(
            final java.lang.String issue) {
        this._issue = issue;
    }

    /**
     * Sets the value of field 'reporter'.
     * 
     * @param reporter the value of field 'reporter'.
     */
    public void setReporter(
            final java.lang.String reporter) {
        this._reporter = reporter;
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
     * org.exolab.castor.tests.framework.testDescriptor.BugFix
     */
    public static org.exolab.castor.tests.framework.testDescriptor.BugFix unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.tests.framework.testDescriptor.BugFix) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.BugFix.class, reader);
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
