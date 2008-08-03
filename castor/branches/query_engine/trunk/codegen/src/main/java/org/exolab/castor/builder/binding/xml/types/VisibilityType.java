/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding.xml.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class VisibilityType.
 * 
 * @version $Revision$ $Date$
 */
public class VisibilityType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The public type
     */
    public static final int PUBLIC_TYPE = 0;

    /**
     * The instance of the public type
     */
    public static final VisibilityType PUBLIC = new VisibilityType(PUBLIC_TYPE, "public");

    /**
     * The protected type
     */
    public static final int PROTECTED_TYPE = 1;

    /**
     * The instance of the protected type
     */
    public static final VisibilityType PROTECTED = new VisibilityType(PROTECTED_TYPE, "protected");

    /**
     * The private type
     */
    public static final int PRIVATE_TYPE = 2;

    /**
     * The instance of the private type
     */
    public static final VisibilityType PRIVATE = new VisibilityType(PRIVATE_TYPE, "private");

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

    private VisibilityType(final int type, final java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.Returns an enumeration of all possible
     * instances of VisibilityType
     * 
     * @return an Enumeration over all possible instances of
     * VisibilityType
     */
    public static java.util.Enumeration enumerate(
    ) {
        return _memberTable.elements();
    }

    /**
     * Method getType.Returns the type of this VisibilityType
     * 
     * @return the type of this VisibilityType
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
        members.put("public", PUBLIC);
        members.put("protected", PROTECTED);
        members.put("private", PRIVATE);
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
     * VisibilityType
     * 
     * @return the String representation of this VisibilityType
     */
    public java.lang.String toString(
    ) {
        return this.stringValue;
    }

    /**
     * Method valueOf.Returns a new VisibilityType based on the
     * given String value.
     * 
     * @param string
     * @return the VisibilityType value of parameter 'string'
     */
    public static org.exolab.castor.builder.binding.xml.types.VisibilityType valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = _memberTable.get(string);
        }
        if (obj == null) {
            String err = "" + string + " is not a valid VisibilityType";
            throw new IllegalArgumentException(err);
        }
        return (VisibilityType) obj;
    }

}
