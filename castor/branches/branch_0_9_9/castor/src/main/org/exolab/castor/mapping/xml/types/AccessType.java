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
public class AccessType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The read-only type
    **/
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the read-only type
    **/
    public static final AccessType VALUE_0 = new AccessType(VALUE_0_TYPE, "read-only");

    /**
     * The shared type
    **/
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the shared type
    **/
    public static final AccessType VALUE_1 = new AccessType(VALUE_1_TYPE, "shared");

    /**
     * The exclusive type
    **/
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the exclusive type
    **/
    public static final AccessType VALUE_2 = new AccessType(VALUE_2_TYPE, "exclusive");

    /**
     * The db-locked type
    **/
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the db-locked type
    **/
    public static final AccessType VALUE_3 = new AccessType(VALUE_3_TYPE, "db-locked");

    private static java.util.Hashtable _memberTable = init();

    private int type = -1;

    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private AccessType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.mapping.xml.types.AccessType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the type of this AccessType
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
        members.put("read-only", VALUE_0);
        members.put("shared", VALUE_1);
        members.put("exclusive", VALUE_2);
        members.put("db-locked", VALUE_3);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Returns the String representation of this AccessType
    **/
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Returns a new AccessType based on the given String value.
     * @param string
    **/
    public static org.exolab.castor.mapping.xml.types.AccessType valueOf(java.lang.String string)
    {
        Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid AccessType";
            throw new IllegalArgumentException(err);
        }
        return (AccessType) obj;
    } //-- org.exolab.castor.mapping.xml.types.AccessType valueOf(java.lang.String) 

}
