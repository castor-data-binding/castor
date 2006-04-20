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
public class DirtyType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The check type
    **/
    public static final int CHECK_TYPE = 0;

    /**
     * The instance of the check type
    **/
    public static final DirtyType CHECK = new DirtyType(CHECK_TYPE, "check");

    /**
     * The ignore type
    **/
    public static final int IGNORE_TYPE = 1;

    /**
     * The instance of the ignore type
    **/
    public static final DirtyType IGNORE = new DirtyType(IGNORE_TYPE, "ignore");

    private static java.util.Hashtable _memberTable = init();

    private int type = -1;

    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private DirtyType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.mapping.xml.types.DirtyType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the type of this DirtyType
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
        members.put("check", CHECK);
        members.put("ignore", IGNORE);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Returns the String representation of this DirtyType
    **/
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Returns a new DirtyType based on the given String value.
     * @param string
    **/
    public static org.exolab.castor.mapping.xml.types.DirtyType valueOf(java.lang.String string)
    {
        Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid DirtyType";
            throw new IllegalArgumentException(err);
        }
        return (DirtyType) obj;
    } //-- org.exolab.castor.mapping.xml.types.DirtyType valueOf(java.lang.String) 

}
