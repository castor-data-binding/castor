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
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public abstract class RootType extends org.exolab.castor.tests.framework.testDescriptor.StringType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private boolean _random = false;

    /**
     * keeps track of state for field: _random
    **/
    private boolean _has_random;

    private boolean _dump = false;

    /**
     * keeps track of state for field: _dump
    **/
    private boolean _has_dump;


      //----------------/
     //- Constructors -/
    //----------------/

    public RootType() {
        super();
    } //-- org.exolab.castor.tests.framework.testDescriptor.RootType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public void deleteDump()
    {
        this._has_dump= false;
    } //-- void deleteDump() 

    /**
    **/
    public void deleteRandom()
    {
        this._has_random= false;
    } //-- void deleteRandom() 

    /**
     * Returns the value of field 'dump'.
     * @return the value of field 'dump'.
    **/
    public boolean getDump()
    {
        return this._dump;
    } //-- boolean getDump() 

    /**
     * Returns the value of field 'random'.
     * @return the value of field 'random'.
    **/
    public boolean getRandom()
    {
        return this._random;
    } //-- boolean getRandom() 

    /**
    **/
    public boolean hasDump()
    {
        return this._has_dump;
    } //-- boolean hasDump() 

    /**
    **/
    public boolean hasRandom()
    {
        return this._has_random;
    } //-- boolean hasRandom() 

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
    public abstract void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * 
     * @param handler
    **/
    public abstract void marshal(org.xml.sax.DocumentHandler handler)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * Sets the value of field 'dump'.
     * @param dump the value of field 'dump'.
    **/
    public void setDump(boolean dump)
    {
        this._dump = dump;
        this._has_dump = true;
    } //-- void setDump(boolean) 

    /**
     * Sets the value of field 'random'.
     * @param random the value of field 'random'.
    **/
    public void setRandom(boolean random)
    {
        this._random = random;
        this._has_random = true;
    } //-- void setRandom(boolean) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
