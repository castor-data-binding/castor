/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding.xml.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * List of the allowed values for the binding type from an XML
 * schema
 *  to a java class. The type can either be 'element' or
 * 'complexType'.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class BindingType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The element type
     */
    public static final int ELEMENT_TYPE = 0;

    /**
     * The instance of the element type
     */
    public static final BindingType ELEMENT = new BindingType(ELEMENT_TYPE, "element");

    /**
     * The type type
     */
    public static final int TYPE_TYPE = 1;

    /**
     * The instance of the type type
     */
    public static final BindingType TYPE = new BindingType(TYPE_TYPE, "type");

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

    private BindingType(final int type, final java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.Returns an enumeration of all possible
     * instances of BindingType
     * 
     * @return an Enumeration over all possible instances of
     * BindingType
     */
    public static java.util.Enumeration enumerate(
    ) {
        return _memberTable.elements();
    }

    /**
     * Method getType.Returns the type of this BindingType
     * 
     * @return the type of this BindingType
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
        members.put("element", ELEMENT);
        members.put("type", TYPE);
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
     * BindingType
     * 
     * @return the String representation of this BindingType
     */
    public java.lang.String toString(
    ) {
        return this.stringValue;
    }

    /**
     * Method valueOf.Returns a new BindingType based on the given
     * String value.
     * 
     * @param string
     * @return the BindingType value of parameter 'string'
     */
    public static org.exolab.castor.builder.binding.xml.types.BindingType valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = _memberTable.get(string);
        }
        if (obj == null) {
            String err = "" + string + " is not a valid BindingType";
            throw new IllegalArgumentException(err);
        }
        return (BindingType) obj;
    }

}
