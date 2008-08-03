/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class BindXmlNodeType.
 * 
 * @version $Revision$ $Date: 2006-01-07 15:48:31 -0700 (Sat, 07 Jan 2006) $
 */
public class BindXmlNodeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The attribute type
     */
    public static final int ATTRIBUTE_TYPE = 0;

    /**
     * The instance of the attribute type
     */
    public static final BindXmlNodeType ATTRIBUTE = new BindXmlNodeType(ATTRIBUTE_TYPE, "attribute");

    /**
     * The element type
     */
    public static final int ELEMENT_TYPE = 1;

    /**
     * The instance of the element type
     */
    public static final BindXmlNodeType ELEMENT = new BindXmlNodeType(ELEMENT_TYPE, "element");

    /**
     * The namespace type
     */
    public static final int NAMESPACE_TYPE = 2;

    /**
     * The instance of the namespace type
     */
    public static final BindXmlNodeType NAMESPACE = new BindXmlNodeType(NAMESPACE_TYPE, "namespace");

    /**
     * The text type
     */
    public static final int TEXT_TYPE = 3;

    /**
     * The instance of the text type
     */
    public static final BindXmlNodeType TEXT = new BindXmlNodeType(TEXT_TYPE, "text");

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

    private BindXmlNodeType(int type, java.lang.String value) 
     {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.mapping.xml.types.BindXmlNodeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * BindXmlNodeType
     * 
     * @return Enumeration
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getType
     * 
     * Returns the type of this BindXmlNodeType
     * 
     * @return int
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
     * @return Hashtable
     */
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("attribute", ATTRIBUTE);
        members.put("element", ELEMENT);
        members.put("namespace", NAMESPACE);
        members.put("text", TEXT);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method readResolve
     * 
     *  will be called during deserialization to replace the
     * deserialized object with the correct constant instance.
     * <br/>
     * 
     * @return Object
     */
    private java.lang.Object readResolve()
    {
        return valueOf(this.stringValue);
    } //-- java.lang.Object readResolve() 

    /**
     * Method toString
     * 
     * Returns the String representation of this BindXmlNodeType
     * 
     * @return String
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOf
     * 
     * Returns a new BindXmlNodeType based on the given String
     * value.
     * 
     * @param string
     * @return BindXmlNodeType
     */
    public static org.exolab.castor.mapping.xml.types.BindXmlNodeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid BindXmlNodeType";
            throw new IllegalArgumentException(err);
        }
        return (BindXmlNodeType) obj;
    } //-- org.exolab.castor.mapping.xml.types.BindXmlNodeType valueOf(java.lang.String) 

}
