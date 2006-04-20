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
import org.exolab.castor.tests.framework.testDescriptor.types.TypeType;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

/**
 * Class ListenerType.
 * 
 * @version $Revision$ $Date$
 */
public abstract class ListenerType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _goldFile
     */
    private java.lang.String _goldFile;

    /**
     * Field _className
     */
    private java.lang.String _className;

    /**
     * Field _type
     */
    private org.exolab.castor.tests.framework.testDescriptor.types.TypeType _type = org.exolab.castor.tests.framework.testDescriptor.types.TypeType.valueOf("Both");


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
     * Method getClassNameReturns the value of field 'className'.
     * 
     * @return the value of field 'className'.
     */
    public java.lang.String getClassName()
    {
        return this._className;
    } //-- java.lang.String getClassName() 

    /**
     * Method getGoldFileReturns the value of field 'goldFile'.
     * 
     * @return the value of field 'goldFile'.
     */
    public java.lang.String getGoldFile()
    {
        return this._goldFile;
    } //-- java.lang.String getGoldFile() 

    /**
     * Method getTypeReturns the value of field 'type'.
     * 
     * @return the value of field 'type'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.types.TypeType getType()
    {
        return this._type;
    } //-- org.exolab.castor.tests.framework.testDescriptor.types.TypeType getType() 

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
     * Method setClassNameSets the value of field 'className'.
     * 
     * @param className the value of field 'className'.
     */
    public void setClassName(java.lang.String className)
    {
        this._className = className;
    } //-- void setClassName(java.lang.String) 

    /**
     * Method setGoldFileSets the value of field 'goldFile'.
     * 
     * @param goldFile the value of field 'goldFile'.
     */
    public void setGoldFile(java.lang.String goldFile)
    {
        this._goldFile = goldFile;
    } //-- void setGoldFile(java.lang.String) 

    /**
     * Method setTypeSets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(org.exolab.castor.tests.framework.testDescriptor.types.TypeType type)
    {
        this._type = type;
    } //-- void setType(org.exolab.castor.tests.framework.testDescriptor.types.TypeType) 

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
