/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.5</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * The type of test case, either basic capability or a special
 * case.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class CategoryType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The basic capability type
     */
    public static final int BASIC_CAPABILITY_TYPE = 0;

    /**
     * The instance of the basic capability type
     */
    public static final CategoryType BASIC_CAPABILITY = new CategoryType(BASIC_CAPABILITY_TYPE, "basic capability");

    /**
     * The special case type
     */
    public static final int SPECIAL_CASE_TYPE = 1;

    /**
     * The instance of the special case type
     */
    public static final CategoryType SPECIAL_CASE = new CategoryType(SPECIAL_CASE_TYPE, "special case");

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

    private CategoryType(int type, java.lang.String value) 
     {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.tests.framework.testDescriptor.types.CategoryType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * CategoryType
     * 
     * @return an Enumeration over all possible instances of
     * CategoryType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getType
     * 
     * Returns the type of this CategoryType
     * 
     * @return the type of this CategoryType
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
     * @return the initialized Hashtable for the member table
     */
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("basic capability", BASIC_CAPABILITY);
        members.put("special case", SPECIAL_CASE);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method readResolve
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
     * Method toString
     * 
     * Returns the String representation of this CategoryType
     * 
     * @return the String representation of this CategoryType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOf
     * 
     * Returns a new CategoryType based on the given String value.
     * 
     * @param string
     * @return the CategoryType value of parameter 'string'
     */
    public static org.exolab.castor.tests.framework.testDescriptor.types.CategoryType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CategoryType";
            throw new IllegalArgumentException(err);
        }
        return (CategoryType) obj;
    } //-- org.exolab.castor.tests.framework.testDescriptor.types.CategoryType valueOf(java.lang.String) 

}
