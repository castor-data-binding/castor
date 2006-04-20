/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.*;

/**
 * 
 * @version $Revision$ $Date$
**/
public class AutoNamingType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The deriveByClass type
    **/
    public static final int DERIVEBYCLASS_TYPE = 0;

    /**
     * The instance of the deriveByClass type
    **/
    public static final AutoNamingType DERIVEBYCLASS = new AutoNamingType(DERIVEBYCLASS_TYPE, "deriveByClass");

    /**
     * The deriveByField type
    **/
    public static final int DERIVEBYFIELD_TYPE = 1;

    /**
     * The instance of the deriveByField type
    **/
    public static final AutoNamingType DERIVEBYFIELD = new AutoNamingType(DERIVEBYFIELD_TYPE, "deriveByField");

    private static java.util.Hashtable _memberTable = init();

    private int type = -1;

    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private AutoNamingType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.mapping.xml.types.AutoNamingType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns an enumeration of all possible instances of
     * AutoNamingType
    **/
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Returns the type of this AutoNamingType
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
        members.put("deriveByClass", DERIVEBYCLASS);
        members.put("deriveByField", DERIVEBYFIELD);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Returns the String representation of this AutoNamingType
    **/
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Returns a new AutoNamingType based on the given String
     * value.
     * @param string
    **/
    public static org.exolab.castor.mapping.xml.types.AutoNamingType valueOf(java.lang.String string)
    {
        Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid AutoNamingType";
            throw new IllegalArgumentException(err);
        }
        return (AutoNamingType) obj;
    } //-- org.exolab.castor.mapping.xml.types.AutoNamingType valueOf(java.lang.String) 

}
