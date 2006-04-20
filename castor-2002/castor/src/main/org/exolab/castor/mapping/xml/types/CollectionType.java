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
public class CollectionType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The array type
    **/
    public static final int ARRAY_TYPE = 0;

    /**
     * The instance of the array type
    **/
    public static final CollectionType ARRAY = new CollectionType(ARRAY_TYPE, "array");

    /**
     * The vector type
    **/
    public static final int VECTOR_TYPE = 1;

    /**
     * The instance of the vector type
    **/
    public static final CollectionType VECTOR = new CollectionType(VECTOR_TYPE, "vector");

    /**
     * The arraylist type
    **/
    public static final int ARRAYLIST_TYPE = 2;

    /**
     * The instance of the arraylist type
    **/
    public static final CollectionType ARRAYLIST = new CollectionType(ARRAYLIST_TYPE, "arraylist");

    /**
     * The hashtable type
    **/
    public static final int HASHTABLE_TYPE = 3;

    /**
     * The instance of the hashtable type
    **/
    public static final CollectionType HASHTABLE = new CollectionType(HASHTABLE_TYPE, "hashtable");

    /**
     * The collection type
    **/
    public static final int COLLECTION_TYPE = 4;

    /**
     * The instance of the collection type
    **/
    public static final CollectionType COLLECTION = new CollectionType(COLLECTION_TYPE, "collection");

    /**
     * The set type
    **/
    public static final int SET_TYPE = 5;

    /**
     * The instance of the set type
    **/
    public static final CollectionType SET = new CollectionType(SET_TYPE, "set");

    /**
     * The map type
    **/
    public static final int MAP_TYPE = 6;

    /**
     * The instance of the map type
    **/
    public static final CollectionType MAP = new CollectionType(MAP_TYPE, "map");

    /**
     * The enumerate type
    **/
    public static final int ENUMERATE_TYPE = 7;

    /**
     * The instance of the enumerate type
    **/
    public static final CollectionType ENUMERATE = new CollectionType(ENUMERATE_TYPE, "enumerate");

    private static java.util.Hashtable _memberTable = init();

    private int type = -1;

    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private CollectionType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.mapping.xml.types.CollectionType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the type of this CollectionType
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
        members.put("array", ARRAY);
        members.put("vector", VECTOR);
        members.put("arraylist", ARRAYLIST);
        members.put("hashtable", HASHTABLE);
        members.put("collection", COLLECTION);
        members.put("set", SET);
        members.put("map", MAP);
        members.put("enumerate", ENUMERATE);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Returns the String representation of this CollectionType
    **/
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Returns a new CollectionType based on the given String
     * value.
     * @param string
    **/
    public static org.exolab.castor.mapping.xml.types.CollectionType valueOf(java.lang.String string)
    {
        Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CollectionType";
            throw new IllegalArgumentException(err);
        }
        return (CollectionType) obj;
    } //-- org.exolab.castor.mapping.xml.types.CollectionType valueOf(java.lang.String) 

}
