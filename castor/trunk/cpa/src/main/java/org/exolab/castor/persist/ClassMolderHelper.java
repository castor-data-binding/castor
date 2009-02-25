package org.exolab.castor.persist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.EnumerationIterator;
import org.castor.persist.TransactionContext;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.loader.AbstractMappingLoader;
import org.exolab.castor.mapping.xml.ClassMapping;
import org.exolab.castor.mapping.xml.FieldMapping;
import org.exolab.castor.persist.spi.Identity;

/**
 * Utility class that provides (mostly) static methods in relation to the functions
 * required by a {@link ClassMolder}.
 *
 */
public final class ClassMolderHelper {
    
    /**
     * Logger used for logging.
     */
    public static final Log LOG = LogFactory.getLog(ClassMolderHelper.class);
    
    private ClassMolderHelper() {
        // nothing to do
    }

    /**
     * A utility method which compare object.
     * @param o1 First object instance 
     * @param o2 Second object instance
     * @return True if the objects compared are equal
     */
    public static boolean isEquals(final Object o1, final Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (o1.equals(o2)) {
            return true;
        }
        // [oleg] is some special cases equals doesn't work properly
        if ((o1 instanceof java.math.BigDecimal)
                && ((java.math.BigDecimal) o1)
                        .compareTo((java.math.BigDecimal) o2) == 0) {
            return true;
        }
        if ((o1 instanceof java.sql.Timestamp)
                && (o2 instanceof java.sql.Timestamp)) {
            java.sql.Timestamp t1 = (java.sql.Timestamp) o1;
            java.sql.Timestamp t2 = (java.sql.Timestamp) o2;
            return (t1.getTime() == t2.getTime() && t1.getNanos() / 1000000 == t2
                    .getNanos() / 1000000);
        }

        if ((o1 instanceof byte[]) && (o2 instanceof byte[])) {
            return Arrays.equals((byte[]) o1, (byte[]) o2);
        }
        if ((o1 instanceof char[]) && (o2 instanceof char[])) {
            return Arrays.equals((char[]) o1, (char[]) o2);
        }
        return false;
    }

    /**
     * Utility method to compare collections for equality.
     * 
     * @param c1 collection one.
     * @param c2 collection two.
     * @return True if the collections are equal.
     */
    public static boolean isEquals(final Collection c1, final Collection c2) {
        if (c1 == c2) {
            return true;
        }
        if (c1 == null || c2 == null) {
            return false;
        }
        if (c1.containsAll(c2) && c2.containsAll(c1)) {
            return true;
        }
        return false;
    }

    /**
     * Return all the object identity of a Collection of object of the same
     * type.
     * 
     * @param tx the transaction context
     * @param molder class molder of the type of the objects
     * @param col a Collection or Vector containing
     * @return an <tt>ArrayList</tt>s which contains list of object identity
     */
    public static ArrayList extractIdentityList(final TransactionContext tx,
            final ClassMolder molder, final Object col) {
        if (col == null) {
            return new ArrayList();
        } else if (col instanceof Collection) {
            ArrayList<Object> idList = new ArrayList<Object>();
            Iterator itor = ((Collection) col).iterator();
            while (itor.hasNext()) {
                Object id = molder.getIdentity(tx, itor.next());
                if (id != null) {
                    idList.add(id);
                }
            }
            return idList;
        } else if (col instanceof Iterator) {
            ArrayList<Object> idList = new ArrayList<Object>();
            Iterator itor = (Iterator) col;
            while (itor.hasNext()) {
                Object id = molder.getIdentity(tx, itor.next());
                if (id != null) {
                    idList.add(id);
                }
            }
            return idList;
        } else if (col instanceof Enumeration) {
            ArrayList<Object> idList = new ArrayList<Object>();
            Enumeration enumeration = (Enumeration) col;
            while (enumeration.hasMoreElements()) {
                Object id = molder.getIdentity(tx, enumeration.nextElement());
                if (id != null) {
                    idList.add(id);
                }
            }
            return idList;
       } else if (col instanceof Map) {
            ArrayList<Object> idList = new ArrayList<Object>();
            Iterator itor = ((Map) col).values().iterator();
            while (itor.hasNext()) {
                Object id = molder.getIdentity(tx, itor.next());
                if (id != null) { idList.add(id); }
            }
            return idList;
        } else if (col.getClass().isArray()) {
            ArrayList<Object> idList = new ArrayList<Object>();
            Object[] arrayCol = (Object[]) col;
            for (int i = 0; i < arrayCol.length; i++) {
                Object id = molder.getIdentity(tx, arrayCol[i]);
                if (id != null) {
                    idList.add(id);
                }
            }
            return idList;
        } else {
            throw new IllegalArgumentException(
                    "A Collection or Map is expected!");
        }
    }

    /**
     * Return the iterator on values of the specified Collection
     * or, return the iterator on values of the specified Map.
     * 
     * @param object - a Collection instance.
     */
    public static Iterator getIterator(final Object object) {
        if (object == null) {
            return new Iterator() {
                public boolean hasNext() {
                    return false;
                }

                public Object next() {
                    throw new NoSuchElementException();
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        } else if (object instanceof Collection) {
            return ((Collection) object).iterator();
        } else if (object instanceof Enumeration) {
            return new EnumerationIterator((Enumeration) object);
        } else if (object instanceof Iterator) {
            return (Iterator) object;
        } else if (object instanceof Map) {
            return ((Map) object).values().iterator();
        } else if (object.getClass().isArray()) {
            final class ArrayIterator implements java.util.Iterator {
                private Object[] _array;

                private int _i = 0;

                ArrayIterator(final Object[] array) {
                    this._array = array;
                }

                public boolean hasNext() {
                    return _i < _array.length;
                }

                public Object next() {
                    return _array[_i++];
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            }
            return new ArrayIterator((Object[]) object);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Get all the field mapping, including all the field in extended class, but
     * id fields.
     * 
     * @param clsMap ClassMapping instance.
     * @return An array.
     * @throws MappingException
     */
    public static FieldMapping[] getFullFields(final ClassMapping clsMap)
            throws MappingException {
        FieldMapping[] extendFields;
        FieldMapping[] thisFields;
        FieldMapping[] fields = null;
        String[] identities;
        boolean idfield;
        ClassMapping extend = (ClassMapping) clsMap.getExtends();
        ClassMapping origin;
        ArrayList<FieldMapping> fieldList;

        if (extend != null) {
            origin = extend;
            while (origin.getExtends() != null) {
                origin = (ClassMapping) origin.getExtends();
            }
            identities = origin.getIdentity();
            identities = AbstractMappingLoader.getIdentityColumnNames (identities, origin);
            extendFields = getFullFields(extend);
            if (clsMap.getClassChoice() != null) {
                thisFields = clsMap.getClassChoice().getFieldMapping();
            } else {
                thisFields = new FieldMapping[0];
            }

            fieldList = new ArrayList<FieldMapping>(
                    extendFields.length + thisFields.length - identities.length);
            for (int i = 0; i < extendFields.length; i++) {
                fieldList.add(extendFields[i]);
            }
            for (int i = 0; i < thisFields.length; i++) {
                idfield = false;
                IDSEARCH:
                for (int k = 0; k < identities.length; k++) {
                    if (thisFields[i].getName().equals(identities[k])) {
                        idfield = true;
                        break IDSEARCH;
                    }
                }
                if (!idfield) {
                    fieldList.add(thisFields[i]);
                }
            }
            fields = new FieldMapping[fieldList.size()];
            fieldList.toArray(fields);
        } else {
            identities = clsMap.getIdentity();
            identities = AbstractMappingLoader.getIdentityColumnNames(identities, clsMap);
            if (identities == null || identities.length == 0) {
                throw new MappingException("Identity is null!");
            }

            // return array of fieldmapping without the id field
            thisFields = clsMap.getClassChoice().getFieldMapping();
            fields = new FieldMapping[thisFields.length - identities.length];

            int j = 0;
            for (int i = 0; i < thisFields.length; i++) {
                idfield = false;
                IDSEARCH: for (int k = 0; k < identities.length; k++) {
                    if (thisFields[i].getName().equals(identities[k])) {
                        idfield = true;
                        break IDSEARCH;
                    }
                }
                if (!idfield) {
                    fields[j] = thisFields[i];
                    j++;
                }
            }

        }
        return fields;
    }

    /**
     * Get the all the id fields of a class
     * If the class, C, is a dependent class, then
     * the depended class', D, id fields will be
     * appended at the back and returned.
     * If the class is an extended class, the id
     * fields of the extended class will be returned.
     */
    public static FieldMapping[] getIdFields(final ClassMapping clsMap)
            throws MappingException {
        ClassMapping base;
        FieldMapping[] fmDepended;
        FieldMapping[] fmResult;
        FieldMapping[] fmBase;
        FieldMapping[] fmIds;
        String[] identities;

        // start with the extended class
        base = clsMap;
        while (base.getExtends() != null) {
            base = (ClassMapping) base.getExtends();
        }
        fmDepended = null;

        identities = base.getIdentity();
        
        identities = AbstractMappingLoader.getIdentityColumnNames (identities, base);

        if (identities == null || identities.length == 0) {
            throw new MappingException("Identity is null!");
        }

        fmIds = new FieldMapping[identities.length];
        fmBase = base.getClassChoice().getFieldMapping();
        for (int i = 0; i < fmBase.length; i++) {
            for (int k = 0; k < identities.length; k++) {
                if (fmBase[i].getName().equals(identities[k])) {
                    fmIds[k] = fmBase[i];
                    break;
                }
            }
        }
        if (fmDepended == null) {
            return fmIds;
        }

        // join depend ids and class id
        fmResult = new FieldMapping[fmDepended.length + identities.length];
        System.arraycopy(fmIds, 0, fmResult, 0, fmIds.length);
        System.arraycopy(fmDepended, 0, fmResult, fmIds.length,
                fmDepended.length);
        return fmIds;
    }

    /**
     * It is assumed the returned collection will not be modified. Any modification
     * to the returned collection may or may not affect the original collection or map.
     */
    public static Collection getAddedValuesList(final TransactionContext tx,
            final ArrayList orgIds, final Object collection, final ClassMolder ch) {

        if (collection == null) {
            return new ArrayList(0);
        }

        if (collection instanceof Map) {
            if (orgIds == null || orgIds.size() == 0) {
                if (collection == null) {
                    return new ArrayList(0);
                }
                return ((Map) collection).values();
            }

            ArrayList<Object> added = new ArrayList<Object>(((Map) collection).size());
            Iterator newItor = ((Map) collection).values().iterator();
            while (newItor.hasNext()) {
                Object newValue = newItor.next();
                Identity newId = ch.getIdentity(tx, newValue);
                if (!orgIds.contains(newId)) { added.add(newValue); }
            }
            return added;
        }

        if (collection instanceof Collection) {
            if (orgIds == null || orgIds.size() == 0) {
                if (collection == null) {
                    return new ArrayList(0);
                }
                return (Collection) collection;
                
            }

            if (collection == null) {
                return new ArrayList(0);
            }

            Collection newValues = (Collection) collection;
            ArrayList<Object> added = new ArrayList<Object>(newValues.size());
            Iterator newItor = newValues.iterator();
            while (newItor.hasNext()) {
                Object newValue = newItor.next();
                Object newId = ch.getIdentity(tx, newValue);
                if (newId == null || !orgIds.contains(newId)) {
                    added.add(newValue);
                }
            }
            return added;
        }

        if (collection instanceof Iterator) {
            if (orgIds == null || orgIds.size() == 0) {
                if (collection == null) {
                    return new ArrayList(0);
                }
                
                Iterator iterator = (Iterator) collection;
                Collection returnCollection = new ArrayList();
                while (iterator.hasNext()) {
                    returnCollection.add(iterator.next());
                }
                return returnCollection;
                
            }

            if (collection == null) {
                return new ArrayList(0);
            }

            Iterator newValuesIterator = (Iterator) collection;
            ArrayList<Object> added = new ArrayList<Object>();
            while (newValuesIterator.hasNext()) {
                Object newValue = newValuesIterator.next();
                Object newId = ch.getIdentity(tx, newValue);
                if (newId == null || !orgIds.contains(newId)) {
                    added.add(newValue);
                }
            }
            return added;
        }

        if (collection instanceof Enumeration) {
            if (collection == null) { return new ArrayList(0); }

            Enumeration newValues = (Enumeration) collection;
            ArrayList<Object> added = new ArrayList<Object>();
            if (orgIds == null || orgIds.size() == 0) {
                while (newValues.hasMoreElements()) {
                    Object newValue = newValues.nextElement();
                    added.add(newValue);
                }
            } else {
                while (newValues.hasMoreElements()) {
                    Object newValue = newValues.nextElement();
                    Object newId = ch.getIdentity(tx, newValue);
                    if (newId == null || !orgIds.contains(newId)) {
                        added.add(newValue);
                    }
                }
            }
            return added;
        }

        if (collection.getClass().isArray()) {
            if (orgIds == null || orgIds.size() == 0) {
                if (collection == null) {
                    return new ArrayList(0);
                } 
                Object[] newValues = (Object[]) collection;
                ArrayList<Object> result = new ArrayList<Object>(newValues.length);
                for (int i = 0; i < newValues.length; i++) {
                    result.add(newValues[i]);
                }
                return result;
                
            }

            if (collection == null) {
                return new ArrayList(0);
            }

            Object[] newValues = (Object[]) collection;
            ArrayList<Object> added = new ArrayList<Object>(newValues.length);
            for (int i = 0; i < newValues.length; i++) {
                Object newValue = newValues[i];
                Object newId = ch.getIdentity(tx, newValue);
                if (newId == null || !orgIds.contains(newId)) {
                    added.add(newValue);
                }
            }
            return added;
        }

        throw new IllegalArgumentException("Collection type "
                + collection.getClass().getName() + " is not supported!");
     }

    /**
     * It is assumed the returned collection will not be modified. Any modification
     * to the returned collection may or may not affect the original collection or map.
     */
    public static Collection getRemovedIdsList(final TransactionContext tx,
            final ArrayList orgIds, final Object collection,
            final ClassMolder ch) {

        if (collection == null) {
            if (orgIds == null) {
                return new ArrayList(0);
            }
            
            return orgIds;
        }

        if (collection instanceof Map) {
            if (orgIds == null || orgIds.size() == 0) {
                return new ArrayList(0);
            }

            HashSet<Identity> newIds = new HashSet<Identity>(((Map) collection).size());
            Iterator newItor = ((Map) collection).values().iterator();
            while (newItor.hasNext()) {
                newIds.add(ch.getIdentity(tx, newItor.next()));
            }
            
            ArrayList<Object> removed = new ArrayList<Object>(orgIds.size());
            Iterator orgItor = orgIds.iterator();
            while (orgItor.hasNext()) {
                Object id = orgItor.next();
                if (!newIds.contains(id)) { removed.add(id); }
            }
            return removed;
        }

        if (collection instanceof Enumeration) {
            if (orgIds == null || orgIds.size() == 0) {
                return new ArrayList(0);
            }

            Enumeration newCol = (Enumeration) collection;
           Iterator orgItor = orgIds.iterator();
            ArrayList<Object> removed = new ArrayList<Object>(0);

            // make a new map of key and value of the new collection
            HashMap<Object, Object> newMap = new HashMap<Object, Object>();
            while (newCol.hasMoreElements()) {
                Object newObject = newCol.nextElement();
                Object newId = ch.getIdentity(tx, newObject);
                if (newId != null) {
                    newMap.put(newId, newObject);
                }
            }
            while (orgItor.hasNext()) {
                Object id = orgItor.next();
                if (!newMap.containsKey(id)) {
                    removed.add(id);
                }
            }
            return removed;
        }

        if (collection instanceof Collection) {
            if (orgIds == null || orgIds.size() == 0) {
                return new ArrayList(0);
            }

            Collection newCol = (Collection) collection;
            Iterator orgItor = orgIds.iterator();
            ArrayList<Object> removed = new ArrayList<Object>(0);

            // make a new map of key and value of the new collection
            HashMap<Object, Object> newMap = new HashMap<Object, Object>();
            Iterator newColItor = newCol.iterator();
            while (newColItor.hasNext()) {
                Object newObject = newColItor.next();
                Object newId = ch.getIdentity(tx, newObject);
                if (newId != null) {
                    newMap.put(newId, newObject);
                }
            }
            while (orgItor.hasNext()) {
                Object id = orgItor.next();
                if (!newMap.containsKey(id)) {
                    removed.add(id);
                }
            }
            return removed;
        }

        if (collection instanceof Iterator) {
            if (orgIds == null || orgIds.size() == 0) {
                return new ArrayList(0);
            }

            Iterator collectionIterator = (Iterator) collection;
            Iterator orgItor = orgIds.iterator();
            ArrayList<Object> removed = new ArrayList<Object>(0);

            // make a new map of key and value of the new collection
            HashMap<Object, Object> newMap = new HashMap<Object, Object>();
            while (collectionIterator.hasNext()) {
                Object newObject = collectionIterator.next();
                Object newId = ch.getIdentity(tx, newObject);
                if (newId != null) {
                    newMap.put(newId, newObject);
                }
            }
            while (orgItor.hasNext()) {
                Object id = orgItor.next();
                if (!newMap.containsKey(id)) {
                    removed.add(id);
                }
            }
            return removed;
        }

        if (collection.getClass().isArray()) {
            if (orgIds == null || orgIds.size() == 0) {
                return new ArrayList(0);
            }

            Object[] newCol = (Object[]) collection;
            Iterator orgItor = orgIds.iterator();
            ArrayList<Object> removed = new ArrayList<Object>(0);

            // make a new map of key and value of the new collection
            HashMap<Object, Object> newMap = new HashMap<Object, Object>();
            for (int i = 0; i < newCol.length; i++) {
                Object newObject = newCol[i];
                Object newId = ch.getIdentity(tx, newObject);
                if (newId != null) {
                    newMap.put(newId, newObject);
                }
            }
            while (orgItor.hasNext()) {
                Object id = orgItor.next();
                if (!newMap.containsKey(id)) {
                    removed.add(id);
                }
            }
            return removed;
        }

        throw new IllegalArgumentException("Collection type "
                + collection.getClass().getName() + " is not supported!");
    }

}
