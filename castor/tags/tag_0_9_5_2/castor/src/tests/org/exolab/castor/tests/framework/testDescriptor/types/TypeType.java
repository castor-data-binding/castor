/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class TypeType.
 * 
 * @version $Revision$ $Date$
 */
public class TypeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Marshal type
     */
    public static final int MARSHAL_TYPE = 0;

    /**
     * The instance of the Marshal type
     */
    public static final TypeType MARSHAL = new TypeType(MARSHAL_TYPE, "Marshal");

    /**
     * The Unmarshal type
     */
    public static final int UNMARSHAL_TYPE = 1;

    /**
     * The instance of the Unmarshal type
     */
    public static final TypeType UNMARSHAL = new TypeType(UNMARSHAL_TYPE, "Unmarshal");

    /**
     * The Both type
     */
    public static final int BOTH_TYPE = 2;

    /**
     * The instance of the Both type
     */
    public static final TypeType BOTH = new TypeType(BOTH_TYPE, "Both");

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

    private TypeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.tests.framework.testDescriptor.types.TypeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of TypeType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this TypeType
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
        members.put("Marshal", MARSHAL);
        members.put("Unmarshal", UNMARSHAL);
        members.put("Both", BOTH);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * TypeType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new TypeType based on the given
     * String value.
     * 
     * @param string
     */
    public static org.exolab.castor.tests.framework.testDescriptor.types.TypeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid TypeType";
            throw new IllegalArgumentException(err);
        }
        return (TypeType) obj;
    } //-- org.exolab.castor.tests.framework.testDescriptor.types.TypeType valueOf(java.lang.String) 

}
