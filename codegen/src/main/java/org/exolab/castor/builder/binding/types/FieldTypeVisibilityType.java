/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.5</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class FieldTypeVisibilityType.
 * 
 * @version $Revision$ $Date$
 */
public class FieldTypeVisibilityType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The public type.
     */
    public static final int PUBLIC_TYPE = 0;

    /**
     * The instance of the public type.
     */
    public static final FieldTypeVisibilityType PUBLIC = new FieldTypeVisibilityType(PUBLIC_TYPE, "public");

    /**
     * The protected type.
     */
    public static final int PROTECTED_TYPE = 1;

    /**
     * The instance of the protected type.
     */
    public static final FieldTypeVisibilityType PROTECTED = new FieldTypeVisibilityType(PROTECTED_TYPE, "protected");

    /**
     * The private type.
     */
    public static final int PRIVATE_TYPE = 2;

    /**
     * The instance of the private type.
     */
    public static final FieldTypeVisibilityType PRIVATE = new FieldTypeVisibilityType(PRIVATE_TYPE, "private");

    /**
     * Field _memberTable.
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type.
     */
    private int type = -1;

    /**
     * Field stringValue.
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private FieldTypeVisibilityType(int type, java.lang.String value) 
     {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.builder.binding.types.FieldTypeVisibilityType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.
     * 
     * Returns an enumeration of all possible instances of
     * FieldTypeVisibilityType
     * 
     * @return an Enumeration over all possible instances of
     * FieldTypeVisibilityType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getType.
     * 
     * Returns the type of this FieldTypeVisibilityType
     * 
     * @return the type of this FieldTypeVisibilityType
     */
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
     * Method init.
     * 
     * @return the initialized Hashtable for the member table
     */
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("public", PUBLIC);
        members.put("protected", PROTECTED);
        members.put("private", PRIVATE);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method readResolve.
     * 
     *  will be called during deserialization to replace the
     * deserialized object with the correct constant instance.
     * 
     * @return this deserialized object
     */
    private java.lang.Object readResolve()
    {
        return valueOf(this.stringValue);
    } //-- java.lang.Object readResolve() 

    /**
     * Method toString.
     * 
     * Returns the String representation of this
     * FieldTypeVisibilityType
     * 
     * @return the String representation of this
     * FieldTypeVisibilityType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOf
     * 
     * Returns a new FieldTypeVisibilityType based on the given
     * String value.
     * 
     * @param string
     * @return the FieldTypeVisibilityType value of parameter
     * 'string'
     */
    public static org.exolab.castor.builder.binding.types.FieldTypeVisibilityType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid FieldTypeVisibilityType";
            throw new IllegalArgumentException(err);
        }
        return (FieldTypeVisibilityType) obj;
    } //-- org.exolab.castor.builder.binding.types.FieldTypeVisibilityType valueOf(java.lang.String) 

}
