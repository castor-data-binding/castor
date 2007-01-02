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
 * 
 * @version $Revision$ $Date: 2005-11-21 13:39:55 -0700 (Mon, 21 Nov 2005) $
**/
public class FieldTypeCollectionType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The array type.
    **/
    public static final int ARRAY_TYPE = 0;

    /**
     * The instance of the array type.
    **/
    public static final FieldTypeCollectionType ARRAY = new FieldTypeCollectionType(ARRAY_TYPE, "array");

    /**
     * The vector type.
    **/
    public static final int VECTOR_TYPE = 1;

    /**
     * The instance of the vector type.
    **/
    public static final FieldTypeCollectionType VECTOR = new FieldTypeCollectionType(VECTOR_TYPE, "vector");

    /**
     * The arraylist type.
    **/
    public static final int ARRAYLIST_TYPE = 2;

    /**
     * The instance of the arraylist type.
    **/
    public static final FieldTypeCollectionType ARRAYLIST = new FieldTypeCollectionType(ARRAYLIST_TYPE, "arraylist");

    /**
     * The hashtable type.
    **/
    public static final int HASHTABLE_TYPE = 3;

    /**
     * The instance of the hashtable type.
    **/
    public static final FieldTypeCollectionType HASHTABLE = new FieldTypeCollectionType(HASHTABLE_TYPE, "hashtable");

    /**
     * The collection type.
    **/
    public static final int COLLECTION_TYPE = 4;

    /**
     * The instance of the collection type.
    **/
    public static final FieldTypeCollectionType COLLECTION = new FieldTypeCollectionType(COLLECTION_TYPE, "collection");

    /**
     * The odmg type.
    **/
    public static final int ODMG_TYPE = 5;

    /**
     * The instance of the odmg type.
    **/
    public static final FieldTypeCollectionType ODMG = new FieldTypeCollectionType(ODMG_TYPE, "odmg");

    /**
     * The set type.
    **/
    public static final int SET_TYPE = 6;

    /**
     * The instance of the set type.
    **/
    public static final FieldTypeCollectionType SET = new FieldTypeCollectionType(SET_TYPE, "set");

    /**
     * The map type.
    **/
    public static final int MAP_TYPE = 7;

    /**
     * The instance of the map type.
    **/
    public static final FieldTypeCollectionType MAP = new FieldTypeCollectionType(MAP_TYPE, "map");

    /**
     * The set type.
    **/
    public static final int SORTED_SET_TYPE = 8;

    /**
     * The instance of the sorted set type.
    **/
    public static final FieldTypeCollectionType SORTED_SET = new FieldTypeCollectionType(SORTED_SET_TYPE, "sortedset");

    /**
     * The sorted map type.
    **/
    public static final int SORTED_MAP_TYPE = 9;

    /**
     * The instance of the sorted map type.
    **/
    public static final FieldTypeCollectionType SORTED_MAP = new FieldTypeCollectionType(SORTED_MAP_TYPE, "sortedmap");

    private static java.util.Hashtable _memberTable = init();

    private int type = -1;

    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private FieldTypeCollectionType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.builder.binding.types.FieldTypeCollectionType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns an enumeration of all possible instances of
     * FieldTypeCollectionType.
    **/
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Returns the type of this FieldTypeCollectionType.
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
        members.put("odmg", ODMG);
        members.put("set", SET);
        members.put("map", MAP);
        members.put("sortedset", SORTED_SET);
        members.put("sortedmap", SORTED_MAP);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Returns the String representation of this
     * FieldTypeCollectionType.
    **/
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Returns a new FieldTypeCollectionType based on the given
     * String value.
     * 
     * @param string
    **/
    public static org.exolab.castor.builder.binding.types.FieldTypeCollectionType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid FieldTypeCollectionType";
            throw new IllegalArgumentException(err);
        }
        return (FieldTypeCollectionType) obj;
    } //-- org.exolab.castor.builder.binding.types.FieldTypeCollectionType valueOf(java.lang.String) 

}
