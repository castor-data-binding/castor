import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.exolab.castor.mapping.ConfigurableFieldHandler;
import org.exolab.castor.mapping.ValidityException;

public class FieldHandlerImpl implements ConfigurableFieldHandler {

    private SimpleDateFormat formatter;

    public FieldHandlerImpl() {
    	//
    }

    /**
     * Sets the configuration for this field handler. The config object is supposed to have one
     * property, named "date-format", with a date format pattern compatible with 
     * java.text.SimpleDateFormat.
     * 
     * @param config the configuration object.
     * @throws ValidityException if config doesn't have the required parameter, or if the parameter
     *      value is not a valid date pattern.
     */    
    public void setConfiguration(Properties config) throws ValidityException {
    	String pattern = config.getProperty("date-format");
    	if (pattern == null) {
    		throw new ValidityException("Required parameter \"date-format\" is missing for CustomDateFieldHandler.");
    	}
    	try {
    		formatter = new SimpleDateFormat(pattern);
    	} catch (IllegalArgumentException e) {
    		throw new ValidityException("Pattern \""+pattern+"\" is not a valid date format.");
    	}
    }

    /**
     * Returns the value of the field from the object.
     *
     * @param object The object
     * @return The value of the field
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler is not
     *  compatible with the Java object
     */
    public Object getValue( Object object )
        throws IllegalStateException
    {
        Foo root = (Foo)object;
        Date value = root.getDate();
        if (value == null) return null;
        return formatter.format(value);
    }

    /**
     * Sets the value of the field on the object.
     *
     * @param object The object
     * @param value The new value
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler is not
     *  compatible with the Java object
     * @throws IllegalArgumentException The value passed is not of
     *  a supported type
     */
    public void setValue( Object object, Object value )
        throws IllegalStateException, IllegalArgumentException
    {
        Foo root = (Foo)object;
        Date date = null;
        try {
            date = formatter.parse((String)value);
        }
        catch(ParseException px) {
            throw new IllegalArgumentException(px.getMessage());
        }
        root.setDate(date);

    }
    
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
        //-- Since it's marked as a string...just return null,
        //-- it's not needed.
        return null;
    }

    /**
     * Sets the value of the field to a default value.
     *
     * Reference fields are set to null, primitive fields are set to
     * their default value, collection fields are emptied of all
     * elements.
     *
     * @param object The object
     * @throws IllegalStateException The Java object has changed and
     *  is no longer supported by this handler, or the handler is not
     *  compatible with the Java object
     */
    public void resetValue( Object object )
        throws IllegalStateException, IllegalArgumentException
    {
        ((Foo)object).setDate(null);
    }

    /**
     * @deprecated No longer supported
     */
    public void checkValidity( Object object )
        throws ValidityException, IllegalStateException
    {
        // do nothing
    }
}
