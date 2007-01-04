/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * 
 *                  List of the allowed values for the binding type
 * from an XML schema
 *                  to a java class. The type can either be
 * 'element' or 'complexType'.
 *              
 * 
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
**/
public class BindingType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The element type.
    **/
    public static final int ELEMENT_TYPE = 0;

    /**
     * The instance of the element type.
    **/
    public static final BindingType ELEMENT = new BindingType(ELEMENT_TYPE, "element");

    /**
     * The type type.
    **/
    public static final int TYPE_TYPE = 1;

    /**
     * The instance of the type type.
    **/
    public static final BindingType TYPE = new BindingType(TYPE_TYPE, "type");

    private static java.util.Hashtable _memberTable = init();

    private int _type = -1;

    private java.lang.String _stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private BindingType(final int type, final java.lang.String value) {
        super();
        this._type = type;
        this._stringValue = value;
    } //-- org.exolab.castor.builder.binding.types.BindingType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns an enumeration of all possible instances of
     * BindingType.
    **/
    public static java.util.Enumeration enumerate() {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Returns the type of this BindingType.
    **/
    public int getType() {
        return this._type;
    } //-- int getType() 

    /**
    **/
    private static java.util.Hashtable init() {
        Hashtable members = new Hashtable();
        members.put("element", ELEMENT);
        members.put("type", TYPE);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Returns the String representation of this BindingType.
    **/
    public java.lang.String toString() {
        return this._stringValue;
    } //-- java.lang.String toString() 

    /**
     * Returns a new BindingType based on the given String value.
     * 
     * @param string
    **/
    public static org.exolab.castor.builder.binding.types.BindingType valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) { obj = _memberTable.get(string); }
        if (obj == null) {
            String err = "'" + string + "' is not a valid BindingType";
            throw new IllegalArgumentException(err);
        }
        return (BindingType) obj;
    } //-- org.exolab.castor.builder.binding.types.BindingType valueOf(java.lang.String) 

}
