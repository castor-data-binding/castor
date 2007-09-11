import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.exolab.castor.mapping.GeneralizedFieldHandler;
import org.exolab.castor.mapping.ValidityException;

public class CustomDateFieldHandler extends GeneralizedFieldHandler {

	
    private SimpleDateFormat formatter;

    public CustomDateFieldHandler() {
    	//
    }
    

    public Object convertUponGet(Object value) {
        if (value == null) return null;
        Date date = (Date)value;
        return formatter.format(date);
    }


    public Object convertUponSet(Object value) {
        Date date = null;
        try {
            date = formatter.parse((String)value);
        }
        catch(ParseException px) {
            throw new IllegalArgumentException(px.getMessage());
        }
        return date;
    }

    public Class getFieldType() {
        return Date.class;
    }

    public Object newInstance( Object parent )
        throws IllegalStateException
    {
        //-- Since it's marked as a string...just return null,
        //-- it's not needed.
        return null;
    }

    public void onConfiguration() throws ValidityException {
    	Properties config = getConfiguration();
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



}
