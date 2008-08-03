/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
 * Schema.
 * $Id: TypeType.java 6721 2007-01-05 04:33:29Z ekuns $
 */

package org.exolab.castor.tests.framework.testDescriptor.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class TypeType.
 * 
 * @version $Revision: 6721 $ $Date$
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

    private TypeType(final int type, final java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.Returns an enumeration of all possible
     * instances of TypeType
     * 
     * @return an Enumeration over all possible instances of TypeTyp
     */
    public static java.util.Enumeration enumerate(
    ) {
        return _memberTable.elements();
    }

    /**
     * Method getType.Returns the type of this TypeType
     * 
     * @return the type of this TypeType
     */
    public int getType(
    ) {
        return this.type;
    }

    /**
     * Method init.
     * 
     * @return the initialized Hashtable for the member table
     */
    private static java.util.Hashtable init(
    ) {
        Hashtable members = new Hashtable();
        members.put("Marshal", MARSHAL);
        members.put("Unmarshal", UNMARSHAL);
        members.put("Both", BOTH);
        return members;
    }

    /**
     * Method readResolve. will be called during deserialization to
     * replace the deserialized object with the correct constant
     * instance.
     * 
     * @return this deserialized object
     */
    private java.lang.Object readResolve(
    ) {
        return valueOf(this.stringValue);
    }

    /**
     * Method toString.Returns the String representation of this
     * TypeType
     * 
     * @return the String representation of this TypeType
     */
    public java.lang.String toString(
    ) {
        return this.stringValue;
    }

    /**
     * Method valueOf.Returns a new TypeType based on the given
     * String value.
     * 
     * @param string
     * @return the TypeType value of parameter 'string'
     */
    public static org.exolab.castor.tests.framework.testDescriptor.types.TypeType valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = _memberTable.get(string);
        }
        if (obj == null) {
            String err = "" + string + " is not a valid TypeType";
            throw new IllegalArgumentException(err);
        }
        return (TypeType) obj;
    }

}
