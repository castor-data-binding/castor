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
 * Class ClassMappingAccessType.
 * 
 * @version $Revision$ $Date$
 */
public class ClassMappingAccessType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The read-only type
     */
    public static final int READ_ONLY_TYPE = 0;

    /**
     * The instance of the read-only type
     */
    public static final ClassMappingAccessType READ_ONLY = new ClassMappingAccessType(READ_ONLY_TYPE, "read-only");

    /**
     * The shared type
     */
    public static final int SHARED_TYPE = 1;

    /**
     * The instance of the shared type
     */
    public static final ClassMappingAccessType SHARED = new ClassMappingAccessType(SHARED_TYPE, "shared");

    /**
     * The exclusive type
     */
    public static final int EXCLUSIVE_TYPE = 2;

    /**
     * The instance of the exclusive type
     */
    public static final ClassMappingAccessType EXCLUSIVE = new ClassMappingAccessType(EXCLUSIVE_TYPE, "exclusive");

    /**
     * The db-locked type
     */
    public static final int DB_LOCKED_TYPE = 3;

    /**
     * The instance of the db-locked type
     */
    public static final ClassMappingAccessType DB_LOCKED = new ClassMappingAccessType(DB_LOCKED_TYPE, "db-locked");

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

    private ClassMappingAccessType(int type, java.lang.String value) 
     {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.mapping.xml.types.ClassMappingAccessType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * ClassMappingAccessType
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
     * Returns the type of this ClassMappingAccessType
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
        members.put("read-only", READ_ONLY);
        members.put("shared", SHARED);
        members.put("exclusive", EXCLUSIVE);
        members.put("db-locked", DB_LOCKED);
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
     * Returns the String representation of this
     * ClassMappingAccessType
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
     * Returns a new ClassMappingAccessType based on the given
     * String value.
     * 
     * @param string
     * @return ClassMappingAccessType
     */
    public static org.exolab.castor.mapping.xml.types.ClassMappingAccessType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ClassMappingAccessType";
            throw new IllegalArgumentException(err);
        }
        return (ClassMappingAccessType) obj;
    } //-- org.exolab.castor.mapping.xml.types.ClassMappingAccessType valueOf(java.lang.String) 

}
