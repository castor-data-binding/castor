/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class CategoryType.
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
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the basic capability type
     */
    public static final CategoryType VALUE_0 = new CategoryType(VALUE_0_TYPE, "basic capability");

    /**
     * The special case type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the special case type
     */
    public static final CategoryType VALUE_1 = new CategoryType(VALUE_1_TYPE, "special case");

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

    private CategoryType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.tests.framework.testDescriptor.types.CategoryType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CategoryType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this CategoryType
     */
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
     * Method init
     */
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("basic capability", VALUE_0);
        members.put("special case", VALUE_1);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * CategoryType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CategoryType based on the given
     * String value.
     * 
     * @param string
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
