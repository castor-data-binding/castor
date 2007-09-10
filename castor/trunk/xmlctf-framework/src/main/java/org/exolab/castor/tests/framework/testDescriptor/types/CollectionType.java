/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
 * Schema.
 * $Id: CollectionType.java 6721 2007-01-05 04:33:29Z ekuns $
 */

package org.exolab.castor.tests.framework.testDescriptor.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class CollectionType.
 * 
 * @version $Revision: 6721 $ $Date$
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

    private CollectionType(final int type, final java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.Returns an enumeration of all possible
     * instances of CollectionType
     * 
     * @return an Enumeration over all possible instances of
     * CollectionType
     */
    public static java.util.Enumeration enumerate(
    ) {
        return _memberTable.elements();
    }

    /**
     * Method getType.Returns the type of this CollectionType
     * 
     * @return the type of this CollectionType
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
        members.put("vector", VECTOR);
        members.put("arraylist", ARRAYLIST);
        members.put("odmg", ODMG);
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
     * CollectionType
     * 
     * @return the String representation of this CollectionType
     */
    public java.lang.String toString(
    ) {
        return this.stringValue;
    }

    /**
     * Method valueOf.Returns a new CollectionType based on the
     * given String value.
     * 
     * @param string
     * @return the CollectionType value of parameter 'string'
     */
    public static org.exolab.castor.tests.framework.testDescriptor.types.CollectionType valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = _memberTable.get(string);
        }
        if (obj == null) {
            String err = "" + string + " is not a valid CollectionType";
            throw new IllegalArgumentException(err);
        }
        return (CollectionType) obj;
    }

}
