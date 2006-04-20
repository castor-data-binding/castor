package org.exolab.castor.xml.handlers;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;
import org.exolab.castor.xml.XMLFieldHandler;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;

/**
 * A specialized FieldHandler for the XML Schema decimal type
 * @author <a href="andrew.fawcett@coda.com">Andrew Fawcett</a>
**/
public class DecimalFieldHandler extends XMLFieldHandler {

    private FieldHandler handler = null;
    
    //----------------/
    //- Constructors -/
    //----------------/

    /**
     * Creates a new DecimalFieldHandler using the given
     * FieldHandler for delegation.
     * @param
    **/
    public DecimalFieldHandler(FieldHandler fieldHandler) {
        
        if (fieldHandler == null) {
            String err = "The FieldHandler argument passed to " +
                "the constructor of DecimalFieldHandler must not be null.";
            throw new IllegalArgumentException(err);
        }
        this.handler = fieldHandler;
    } //-- DecimalFieldHandler
    
    
    //------------------/
    //- Public Methods -/
    //------------------/
    
    
    /**
     * Returns the value of the field associated with this
     * descriptor from the given target object.
     * @param target the object to get the value from
     * @return the value of the field associated with this
     * descriptor from the given target object.
    **/
    public Object getValue(Object target) 
        throws java.lang.IllegalStateException
    {        
        Object val = handler.getValue(target);        
        if (val == null) return val;        
        return val.toString();		
    } //-- getValue

    /**
     * Sets the value of the field associated with this descriptor.
     * @param target the object in which to set the value
     * @param value the value of the field 
    **/
    public void setValue(Object target, Object value)
        throws java.lang.IllegalStateException
    {
        BigDecimal bigDecimal = null;        
        if (! (value instanceof BigDecimal) )
            bigDecimal = new BigDecimal(value.toString());
        else bigDecimal = (BigDecimal)value;        
        handler.setValue(target, bigDecimal);
        
    } //-- setValue
    
    public void resetValue(Object target)
        throws java.lang.IllegalStateException
    {
        handler.resetValue(target);
    }


    /**
     * Checks the field validity. Returns successfully if the field
     * can be stored, is valid, etc, throws an exception otherwise.
     *
     * @param object The object
     * @throws ValidityException The field is invalid, is required and
     *  null, or any other validity violation
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler
     *  is not compatiable with the Java object
     */
    public void checkValidity( Object object )
        throws ValidityException, IllegalStateException 
    {
        //-- do nothing for now
    } //-- checkValidity


    /**
     * Creates a new instance of the object described by this field.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple type and
     *  cannot be instantiated
     */
    public Object newInstance( Object parent )
        throws IllegalStateException
    {
        return new BigDecimal(0);
    } //-- newInstance
    
    
} //-- DecimalFieldHandler

