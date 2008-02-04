package base;

import java.awt.Color;
import org.exolab.castor.mapping.GeneralizedFieldHandler;

public class MySimpleHandler extends GeneralizedFieldHandler {

    public Object convertUponGet(Object value) {
        if (value == null) {
            return null;
        }
        Color c = (Color) value;
        return c.toString();
    }

    public Object convertUponSet(Object value) {
        String s = (String) value;
        Color c = Color.getColor(s);
        return c;
    }

    public Class getFieldType() {
        return java.awt.Color.class;
    }

    public Object newInstance(Object parent) throws IllegalStateException {
        return null;
    }

    public Object newInstance(Object parent, Object[] args) throws IllegalStateException {
        return null;
    }

}
