/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0M1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class SqlDirtyType.
 * 
 * @version $Revision$ $Date$
 */
public class SqlDirtyType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The check type
     */
    public static final int CHECK_TYPE = 0;

    /**
     * The instance of the check type
     */
    public static final SqlDirtyType CHECK = new SqlDirtyType(CHECK_TYPE, "check");

    /**
     * The ignore type
     */
    public static final int IGNORE_TYPE = 1;

    /**
     * The instance of the ignore type
     */
    public static final SqlDirtyType IGNORE = new SqlDirtyType(IGNORE_TYPE, "ignore");

    /**
     * Field _memberTable
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type
     */
    private int type = -1;

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private SqlDirtyType(int type, java.lang.String value) 
     {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.mapping.xml.types.SqlDirtyType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * SqlDirtyType
     * 
     * @return Enumeration
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getType
     * 
     * Returns the type of this SqlDirtyType
     * 
     * @return int
     */
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
     * Method init
     * 
     * 
     * 
     * @return Hashtable
     */
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("check", CHECK);
        members.put("ignore", IGNORE);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method readResolve
     * 
     *  will be called during deserialization to replace the
     * deserialized object with the correct constant instance.
     * <br/>
     * 
     * @return Object
     */
    private java.lang.Object readResolve()
    {
        return valueOf(this.stringValue);
    } //-- java.lang.Object readResolve() 

    /**
     * Method toString
     * 
     * Returns the String representation of this SqlDirtyType
     * 
     * @return String
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOf
     * 
     * Returns a new SqlDirtyType based on the given String value.
     * 
     * @param string
     * @return SqlDirtyType
     */
    public static org.exolab.castor.mapping.xml.types.SqlDirtyType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid SqlDirtyType";
            throw new IllegalArgumentException(err);
        }
        return (SqlDirtyType) obj;
    } //-- org.exolab.castor.mapping.xml.types.SqlDirtyType valueOf(java.lang.String) 

}
