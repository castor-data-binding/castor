/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.21+</a>, using
 * an XML Schema.
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
import org.exolab.castor.tests.framework.testDescriptor.types.TypeType;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public abstract class ListenerType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _goldFile;

    private java.lang.String _className;

    private org.exolab.castor.tests.framework.testDescriptor.types.TypeType _type = org.exolab.castor.tests.framework.testDescriptor.types.TypeType.valueOf("Both");;


      //----------------/
     //- Constructors -/
    //----------------/

    public ListenerType() {
        super();
        setType(org.exolab.castor.tests.framework.testDescriptor.types.TypeType.valueOf("Both"));
    } //-- org.exolab.castor.tests.framework.testDescriptor.ListenerType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'className'.
     * 
     * @return the value of field 'className'.
    **/
    public java.lang.String getClassName()
    {
        return this._className;
    } //-- java.lang.String getClassName() 

    /**
     * Returns the value of field 'goldFile'.
     * 
     * @return the value of field 'goldFile'.
    **/
    public java.lang.String getGoldFile()
    {
        return this._goldFile;
    } //-- java.lang.String getGoldFile() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'type'.
    **/
    public org.exolab.castor.tests.framework.testDescriptor.types.TypeType getType()
    {
        return this._type;
    } //-- org.exolab.castor.tests.framework.testDescriptor.types.TypeType getType() 

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
     * 
     * @param out
    **/
    public abstract void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * 
     * 
     * @param handler
    **/
    public abstract void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * Sets the value of field 'className'.
     * 
     * @param className the value of field 'className'.
    **/
    public void setClassName(java.lang.String className)
    {
        this._className = className;
    } //-- void setClassName(java.lang.String) 

    /**
     * Sets the value of field 'goldFile'.
     * 
     * @param goldFile the value of field 'goldFile'.
    **/
    public void setGoldFile(java.lang.String goldFile)
    {
        this._goldFile = goldFile;
    } //-- void setGoldFile(java.lang.String) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
    **/
    public void setType(org.exolab.castor.tests.framework.testDescriptor.types.TypeType type)
    {
        this._type = type;
    } //-- void setType(org.exolab.castor.tests.framework.testDescriptor.types.TypeType) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
