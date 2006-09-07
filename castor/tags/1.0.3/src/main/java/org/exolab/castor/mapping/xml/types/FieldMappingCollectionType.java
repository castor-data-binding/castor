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
 * Class FieldMappingCollectionType.
 * 
 * @version $Revision$ $Date$
 */
public class FieldMappingCollectionType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The array type
     */
    public static final int ARRAY_TYPE = 0;

    /**
     * The instance of the array type
     */
    public static final FieldMappingCollectionType ARRAY = new FieldMappingCollectionType(ARRAY_TYPE, "array");

    /**
     * The vector type
     */
    public static final int VECTOR_TYPE = 1;

    /**
     * The instance of the vector type
     */
    public static final FieldMappingCollectionType VECTOR = new FieldMappingCollectionType(VECTOR_TYPE, "vector");

    /**
     * The arraylist type
     */
    public static final int ARRAYLIST_TYPE = 2;

    /**
     * The instance of the arraylist type
     */
    public static final FieldMappingCollectionType ARRAYLIST = new FieldMappingCollectionType(ARRAYLIST_TYPE, "arraylist");

    /**
     * The hashtable type
     */
    public static final int HASHTABLE_TYPE = 3;

    /**
     * The instance of the hashtable type
     */
    public static final FieldMappingCollectionType HASHTABLE = new FieldMappingCollectionType(HASHTABLE_TYPE, "hashtable");

    /**
     * The collection type
     */
    public static final int COLLECTION_TYPE = 4;

    /**
     * The instance of the collection type
     */
    public static final FieldMappingCollectionType COLLECTION = new FieldMappingCollectionType(COLLECTION_TYPE, "collection");

    /**
     * The set type
     */
    public static final int SET_TYPE = 5;

    /**
     * The instance of the set type
     */
    public static final FieldMappingCollectionType SET = new FieldMappingCollectionType(SET_TYPE, "set");

    /**
     * The map type
     */
    public static final int MAP_TYPE = 6;

    /**
     * The instance of the map type
     */
    public static final FieldMappingCollectionType MAP = new FieldMappingCollectionType(MAP_TYPE, "map");

    /**
     * The enumerate type
     */
    public static final int ENUMERATE_TYPE = 7;

    /**
     * The instance of the enumerate type
     */
    public static final FieldMappingCollectionType ENUMERATE = new FieldMappingCollectionType(ENUMERATE_TYPE, "enumerate");

    /**
     * The sortedset type
     */
    public static final int SORTEDSET_TYPE = 8;

    /**
     * The instance of the sortedset type
     */
    public static final FieldMappingCollectionType SORTEDSET = new FieldMappingCollectionType(SORTEDSET_TYPE, "sortedset");

    /**
     * The iterator type
     */
    public static final int ITERATOR_TYPE = 9;

    /**
     * The instance of the iterator type
     */
    public static final FieldMappingCollectionType ITERATOR = new FieldMappingCollectionType(ITERATOR_TYPE, "iterator");

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

    private FieldMappingCollectionType(int type, java.lang.String value) 
     {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.castor.mapping.xml.types.FieldMappingCollectionType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of
     * FieldMappingCollectionType
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
     * Returns the type of this FieldMappingCollectionType
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
        members.put("array", ARRAY);
        members.put("vector", VECTOR);
        members.put("arraylist", ARRAYLIST);
        members.put("hashtable", HASHTABLE);
        members.put("collection", COLLECTION);
        members.put("set", SET);
        members.put("map", MAP);
        members.put("enumerate", ENUMERATE);
        members.put("sortedset", SORTEDSET);
        members.put("iterator", ITERATOR);
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
     * FieldMappingCollectionType
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
     * Returns a new FieldMappingCollectionType based on the given
     * String value.
     * 
     * @param string
     * @return FieldMappingCollectionType
     */
    public static org.exolab.castor.mapping.xml.types.FieldMappingCollectionType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid FieldMappingCollectionType";
            throw new IllegalArgumentException(err);
        }
        return (FieldMappingCollectionType) obj;
    } //-- org.exolab.castor.mapping.xml.types.FieldMappingCollectionType valueOf(java.lang.String) 

}
