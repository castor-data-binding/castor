/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.2</a>, using an XML
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
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

/**
 * Class FailureType.
 * 
 * @version $Revision$ $Date$
 */
public abstract class FailureType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private boolean _content;

    /**
     * keeps track of state for field: _content
     */
    private boolean _has_content;

    /**
     * Field _exception
     */
    private java.lang.String _exception;


      //----------------/
     //- Constructors -/
    //----------------/

    public FailureType() {
        super();
    } //-- org.exolab.castor.tests.framework.testDescriptor.FailureType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteContent
     */
    public void deleteContent()
    {
        this._has_content= false;
    } //-- void deleteContent() 

    /**
     * Method getContentReturns the value of field 'content'. The
     * field 'content' has the following description: internal
     * content storage
     * 
     * @return the value of field 'content'.
     */
    public boolean getContent()
    {
        return this._content;
    } //-- boolean getContent() 

    /**
     * Method getExceptionReturns the value of field 'exception'.
     * 
     * @return the value of field 'exception'.
     */
    public java.lang.String getException()
    {
        return this._exception;
    } //-- java.lang.String getException() 

    /**
     * Method hasContent
     */
    public boolean hasContent()
    {
        return this._has_content;
    } //-- boolean hasContent() 

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
     * Method setContentSets the value of field 'content'. The
     * field 'content' has the following description: internal
     * content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(boolean content)
    {
        this._content = content;
        this._has_content = true;
    } //-- void setContent(boolean) 

    /**
     * Method setExceptionSets the value of field 'exception'.
     * 
     * @param exception the value of field 'exception'.
     */
    public void setException(java.lang.String exception)
    {
        this._exception = exception;
    } //-- void setException(java.lang.String) 

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
