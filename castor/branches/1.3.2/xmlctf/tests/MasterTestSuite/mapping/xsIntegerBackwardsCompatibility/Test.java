/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Test.
 * 
 * @version $Revision$ $Date$
 */
public class Test implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _firstInteger
     */
    private int _firstInteger;

    /**
     * keeps track of state for field: _firstInteger
     */
    private boolean _has_firstInteger;

    /**
     * Field _secondInteger
     */
    private int _secondInteger;

    /**
     * keeps track of state for field: _secondInteger
     */
    private boolean _has_secondInteger;

    /**
     * Field propertyChangeListeners
     */
    private java.util.Vector propertyChangeListeners;


      //----------------/
     //- Constructors -/
    //----------------/

    public Test() {
        super();
        propertyChangeListeners = new Vector();
    } //-- generated.castor1948.castor-version0952.Test()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addPropertyChangeListenerRegisters a
     * PropertyChangeListener with this class.
     * 
     * @param pcl The PropertyChangeListener to register.
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener pcl)
    {
        propertyChangeListeners.addElement(pcl);
    } //-- void addPropertyChangeListener(java.beans.PropertyChangeListener) 

    /**
     * Note: hashCode() has not been overriden
     * 
     * @param obj
     */
    public boolean equals(java.lang.Object obj)
    {
        if ( this == obj )
            return true;
        
        if (obj instanceof Test) {
        
            Test temp = (Test)obj;
            if (this._firstInteger != temp._firstInteger)
                return false;
            if (this._has_firstInteger != temp._has_firstInteger)
                return false;
            if (this._secondInteger != temp._secondInteger)
                return false;
            if (this._has_secondInteger != temp._has_secondInteger)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'firstInteger'.
     * 
     * @return the value of field 'firstInteger'.
     */
    public int getFirstInteger()
    {
        return this._firstInteger;
    } //-- int getFirstInteger() 

    /**
     * Returns the value of field 'secondInteger'.
     * 
     * @return the value of field 'secondInteger'.
     */
    public int getSecondInteger()
    {
        return this._secondInteger;
    } //-- int getSecondInteger() 

    /**
     * Method hasFirstInteger
     */
    public boolean hasFirstInteger()
    {
        return this._has_firstInteger;
    } //-- boolean hasFirstInteger() 

    /**
     * Method hasSecondInteger
     */
    public boolean hasSecondInteger()
    {
        return this._has_secondInteger;
    } //-- boolean hasSecondInteger() 

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
     * Method notifyPropertyChangeListenersNotifies all registered
     * PropertyChangeListeners when a bound property's value
     * changes.
     * 
     * @param fieldName the name of the property that has changed.
     * @param newValue the new value of the property.
     * @param oldValue the old value of the property.
     */
    protected void notifyPropertyChangeListeners(java.lang.String fieldName, java.lang.Object oldValue, java.lang.Object newValue)
    {
        if (propertyChangeListeners == null) return;
        java.beans.PropertyChangeEvent event = new java.beans.PropertyChangeEvent(this, fieldName, oldValue, newValue);
        
        for (int i = 0; i < propertyChangeListeners.size(); i++) {
            ((java.beans.PropertyChangeListener) propertyChangeListeners.elementAt(i)).propertyChange(event);
        }
    } //-- void notifyPropertyChangeListeners(java.lang.String, java.lang.Object, java.lang.Object) 

    /**
     * Method removePropertyChangeListenerRemoves the given
     * PropertyChangeListener from this classes list of
     * ProperyChangeListeners.
     * 
     * @param pcl The PropertyChangeListener to remove.
     * @return true if the given PropertyChangeListener was removed.
     */
    public boolean removePropertyChangeListener(java.beans.PropertyChangeListener pcl)
    {
        return propertyChangeListeners.removeElement(pcl);
    } //-- boolean removePropertyChangeListener(java.beans.PropertyChangeListener) 

    /**
     * Sets the value of field 'firstInteger'.
     * 
     * @param firstInteger the value of field 'firstInteger'.
     */
    public void setFirstInteger(int firstInteger)
    {
        this._firstInteger = firstInteger;
        this._has_firstInteger = true;
    } //-- void setFirstInteger(int) 

    /**
     * Sets the value of field 'secondInteger'.
     * 
     * @param secondInteger the value of field 'secondInteger'.
     */
    public void setSecondInteger(int secondInteger)
    {
        this._secondInteger = secondInteger;
        this._has_secondInteger = true;
    } //-- void setSecondInteger(int) 

    /**
     * Method unmarshalTest
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTest(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (Test) Unmarshaller.unmarshal(Test.class, reader);
    } //-- java.lang.Object unmarshalTest(java.io.Reader) 

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
