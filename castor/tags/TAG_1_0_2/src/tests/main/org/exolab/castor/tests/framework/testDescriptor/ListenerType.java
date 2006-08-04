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

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ListenerType.
 * 
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public class ListenerType implements java.io.Serializable {


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
     * Returns the value of field 'className'.
     * 
     * @return the value of field 'className'.
     */
    public java.lang.String getClassName()
    {
        return this._className;
    } //-- java.lang.String getClassName() 

    /**
     * Returns the value of field 'goldFile'.
     * 
     * @return the value of field 'goldFile'.
     */
    public java.lang.String getGoldFile()
    {
        return this._goldFile;
    } //-- java.lang.String getGoldFile() 

    /**
     * Returns the value of field 'type'.
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
     * Sets the value of field 'className'.
     * 
     * @param className the value of field 'className'.
     */
    public void setClassName(java.lang.String className)
    {
        this._className = className;
    } //-- void setClassName(java.lang.String) 

    /**
     * Sets the value of field 'goldFile'.
     * 
     * @param goldFile the value of field 'goldFile'.
     */
    public void setGoldFile(java.lang.String goldFile)
    {
        this._goldFile = goldFile;
    } //-- void setGoldFile(java.lang.String) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(org.exolab.castor.tests.framework.testDescriptor.types.TypeType type)
    {
        this._type = type;
    } //-- void setType(org.exolab.castor.tests.framework.testDescriptor.types.TypeType) 

    /**
     * Method unmarshalListenerType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalListenerType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.ListenerType) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.ListenerType.class, reader);
    } //-- java.lang.Object unmarshalListenerType(java.io.Reader) 

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
