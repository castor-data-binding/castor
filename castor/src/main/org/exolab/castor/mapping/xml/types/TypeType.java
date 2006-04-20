/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8.12</a>, using an
 * XML Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Hashtable;
import org.exolab.castor.xml.*;

/**
 * 
 * @version $Revision$ $Date$
**/
public class TypeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The none type
    **/
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the none type
    **/
    public static final TypeType VALUE_0 = new TypeType(VALUE_0_TYPE, "none");

    /**
     * The count-limited type
    **/
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the count-limited type
    **/
    public static final TypeType VALUE_1 = new TypeType(VALUE_1_TYPE, "count-limited");

    /**
     * The time-limited type
    **/
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the time-limited type
    **/
    public static final TypeType VALUE_2 = new TypeType(VALUE_2_TYPE, "time-limited");

    /**
     * The unlimited type
    **/
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the unlimited type
    **/
    public static final TypeType VALUE_3 = new TypeType(VALUE_3_TYPE, "unlimited");

    private static java.util.Hashtable _memberTable = init();

    private int type = -1;

    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private TypeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.mapping.xml.types.TypeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the type of this TypeType
    **/
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
    **/
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("none", VALUE_0);
        members.put("count-limited", VALUE_1);
        members.put("time-limited", VALUE_2);
        members.put("unlimited", VALUE_3);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Returns the String representation of this TypeType
    **/
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Returns a new TypeType based on the given String value.
     * @param string
    **/
    public static org.exolab.castor.mapping.xml.types.TypeType valueOf(java.lang.String string)
    {
        Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid TypeType";
            throw new IllegalArgumentException(err);
        }
        return (TypeType) obj;
    } //-- org.exolab.castor.mapping.xml.types.TypeType valueOf(java.lang.String) 

}
