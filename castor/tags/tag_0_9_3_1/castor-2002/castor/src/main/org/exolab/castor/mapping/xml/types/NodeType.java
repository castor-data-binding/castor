/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8.12</a>, using an
 * XML Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Hashtable;
import org.exolab.castor.xml.*;

/**
 * 
 * @version $Revision$ $Date$
**/
public class NodeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The attribute type
    **/
    public static final int ATTRIBUTE_TYPE = 0;

    /**
     * The instance of the attribute type
    **/
    public static final NodeType ATTRIBUTE = new NodeType(ATTRIBUTE_TYPE, "attribute");

    /**
     * The element type
    **/
    public static final int ELEMENT_TYPE = 1;

    /**
     * The instance of the element type
    **/
    public static final NodeType ELEMENT = new NodeType(ELEMENT_TYPE, "element");

    /**
     * The text type
    **/
    public static final int TEXT_TYPE = 2;

    /**
     * The instance of the text type
    **/
    public static final NodeType TEXT = new NodeType(TEXT_TYPE, "text");

    private static java.util.Hashtable _memberTable = init();

    private int type = -1;

    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private NodeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.mapping.xml.types.NodeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the type of this NodeType
    **/
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
    **/
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("attribute", ATTRIBUTE);
        members.put("element", ELEMENT);
        members.put("text", TEXT);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Returns the String representation of this NodeType
    **/
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Returns a new NodeType based on the given String value.
     * @param string
    **/
    public static org.exolab.castor.mapping.xml.types.NodeType valueOf(java.lang.String string)
    {
        Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid NodeType";
            throw new IllegalArgumentException(err);
        }
        return (NodeType) obj;
    } //-- org.exolab.castor.mapping.xml.types.NodeType valueOf(java.lang.String) 

}
