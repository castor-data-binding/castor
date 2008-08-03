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
 * Class BindXmlAutoNamingType.
 * 
 * @version $Revision$ $Date: 2006-01-07 15:48:31 -0700 (Sat, 07 Jan 2006) $
 */
public class BindXmlAutoNamingType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The deriveByClass type
     */
    public static final int DERIVEBYCLASS_TYPE = 0;

    /**
     * The instance of the deriveByClass type
     */
    public static final BindXmlAutoNamingType DERIVEBYCLASS = new BindXmlAutoNamingType(DERIVEBYCLASS_TYPE, "deriveByClass");

    /**
     * The deriveByField type
     */
    public static final int DERIVEBYFIELD_TYPE = 1;

    /**
     * The instance of the deriveByField type
     */
    public static final BindXmlAutoNamingType DERIVEBYFIELD = new BindXmlAutoNamingType(DERIVEBYFIELD_TYPE, "deriveByField");

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

    private BindXmlAutoNamingType(int type, java.lang.String value) 
     {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * BindXmlAutoNamingType
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
     * Returns the type of this BindXmlAutoNamingType
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
        members.put("deriveByClass", DERIVEBYCLASS);
        members.put("deriveByField", DERIVEBYFIELD);
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
     * Returns the String representation of this
     * BindXmlAutoNamingType
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
     * Returns a new BindXmlAutoNamingType based on the given
     * String value.
     * 
     * @param string
     * @return BindXmlAutoNamingType
     */
    public static org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid BindXmlAutoNamingType";
            throw new IllegalArgumentException(err);
        }
        return (BindXmlAutoNamingType) obj;
    } //-- org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType valueOf(java.lang.String) 

}
