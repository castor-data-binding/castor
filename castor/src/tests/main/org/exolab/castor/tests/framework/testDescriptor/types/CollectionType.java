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
 * Class CollectionType.
 * 
 * @version $Revision$ $Date$
 */
public class CollectionType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The vector type
     */
    public static final int VECTOR_TYPE = 0;

    /**
     * The instance of the vector type
     */
    public static final CollectionType VECTOR = new CollectionType(VECTOR_TYPE, "vector");

    /**
     * The arraylist type
     */
    public static final int ARRAYLIST_TYPE = 1;

    /**
     * The instance of the arraylist type
     */
    public static final CollectionType ARRAYLIST = new CollectionType(ARRAYLIST_TYPE, "arraylist");

    /**
     * The odmg type
     */
    public static final int ODMG_TYPE = 2;

    /**
     * The instance of the odmg type
     */
    public static final CollectionType ODMG = new CollectionType(ODMG_TYPE, "odmg");

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

    private CollectionType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.tests.framework.testDescriptor.types.CollectionType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CollectionType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this CollectionType
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
        members.put("vector", VECTOR);
        members.put("arraylist", ARRAYLIST);
        members.put("odmg", ODMG);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * CollectionType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CollectionType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.exolab.castor.tests.framework.testDescriptor.types.CollectionType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CollectionType";
            throw new IllegalArgumentException(err);
        }
        return (CollectionType) obj;
    } //-- org.exolab.castor.tests.framework.testDescriptor.types.CollectionType valueOf(java.lang.String) 

}
