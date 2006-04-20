/*
 * (C) Copyright Keith Visco 1998-2002. All rights reserved.
 *
 * The contents of this file are released under an Open Source
 * Definition (OSD) compliant license; you may not use this file
 * execpt in compliance with the license. Please see license.txt,
 * distributed with this file. You may also obtain a copy of the
 * license at http://kvisco.com/xslp/license.txt
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * The Copyright owner will not be liable for any damages suffered by
 * you as a result of using the Program. In no event will the Copyright
 * owner be liable for any special, indirect or consequential damages or
 * lost profits even if the Copyright owner has been advised of the
 * possibility of their occurrence.
 *
 * $Id$
 */

package org.exolab.castor.util;

/**
 * This is my implementation of the JDK 1.2 List interface.
 * I wrote this because I want people using 1.1.x to be able
 * to use my apps, but I don't want to use a "synchronized" Vector.
 * I also wanted to get a start in moving my source to JDK 1.2<BR>
 * I use the implementation of the hashCode method that is listed
 * in the JDK 1.2 API, so this List can be compared correctly to actual
 * JDK 1.2 lists using the equals method.
 * <B>Note: This is not a complete implementation yet,
 *    None of the methods that take a Collection have been imlplemented.
 * </B>
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 * @see java.util.List
 * @see java.util.Collection
**/
public class List implements Cloneable, java.io.Serializable {
    /** SerialVersionUID */
    private static final long serialVersionUID = 3116347649564356199L;

    private int DEFAULT_SIZE = 17;

    private Object[] elements = null;

    private int initialSize = DEFAULT_SIZE;

    /**
     * The next available location in the elements array
    **/
    private int elementCount = 0;

    /**
     * Creates a new BasicSet with the default Size
    **/
    public List() {
    } //-- List

    public List(int size) {
        initialSize = size;
    } //-- List

    /**
     * Adds the specified Object to the list
     * @param obj the Object to add to the list
     * @return true if the Object is added to the list
    **/
    public boolean add(Object obj) {
	    if(elements == null) elements = new Object[initialSize];
        if (elementCount == elements.length) increaseSize();
        elements[elementCount++] = obj;
        return true;
    } //-- add

    /**
     * Adds the specified Object to the list at the specified index
     * @param obj the Object to add to the list
     * @return true if the Object is added to the list
     * @exception IndexOutOfBoundsException
    **/
    public boolean add(int index, Object obj)
        throws IndexOutOfBoundsException
    {
        if ((index < 0) || (index > elementCount))
            throw new IndexOutOfBoundsException();

        // make Sure we have room to add the object
	    if(elements == null) elements = new Object[initialSize];
        if (elementCount == elements.length) increaseSize();

        if (index == elementCount) {
            elements[elementCount++] = obj;
        }
        else {
            shiftUp(index);
            elements[index] = obj;
            elementCount++;
        }
        return true;
    } //-- add

    /**
     * Removes all elements from the list
    **/
    public void clear() {
	for (int i = 0; i < elementCount; i++) {
	    elements[i] = null;
	}
        elementCount = 0;
    } //-- clear

    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        }
        catch (CloneNotSupportedException e) {
            // this should never happen
        }
        return obj;
    } //-- clone

    /**
     * Returns true if the specified element is contained in the list.
     * if the specfied element is null, then if the list contains a null
     * value, true will be returned.
     * @param obj the element to search the list for
     * @return true if specified element is contained in the list
    **/
    public boolean contains(Object obj) {
        return (indexOf(obj) >= 0);
    } //-- contains

    /**
     * Compares the specified object with this list for equality.
     * Returns true if and only if the specified Object is a list
     * and all of its associated elements are equal to the elements
     * of this list
    **/
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof List)) return false;
        List list = (List)obj;
        if (list.size() != size()) return false;
        for (int i = 0; i < size(); i++) {
            Object e1 = get(i);
            Object e2 = list.get(i);
            if (!((e1 == null) ? (e2 == null) : e1.equals(e2)) )
               return false;
        }
        return true;
    } //-- equals

    /**
     * Returns the element at the specified position in this list.
     * @param index the position of the element to return
     * @exception IndexOutOfBoundsException
    **/
    public Object get(int index) throws IndexOutOfBoundsException {
        if ((index < 0) || index >= elementCount) {
            throw new IndexOutOfBoundsException();
        }
        return elements[index];
    } //-- get

    /**
     * As defined by the JDK 1.2 API spec:<BR>
     * Returns the hash code value for this list.
     * The hash code of a list is defined to be the result of the following
     * calculation: <BR>
     * <code>
     * hashCode = 1;
     * Iterator i = list.iterator();
     * while (i.hasNext()) {
     *    Object obj = i.next();
     *    hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode());
     * }
     * </code>
     * @return the hash code value for this list
    **/
    public int hashCode() {
       int hashCode = 1;
       for (int i = 0; i < elementCount; i++) {
           Object obj = elements[i];
           hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode());
       }
       return hashCode;
    } //-- hashCode

    /**
     * Returns the index of the first occurrence of the specified element,
     * or -1 if the element is not contained in the List
     * @param obj the Object to get the index for
    **/
    public int indexOf(Object obj) {
        if (obj == null) {
            for (int i = 0; i < elementCount; i++) {
                if (elements[i] == null) return i;
            }
        }
        else {
            for (int i = 0; i < elementCount; i++) {
                if (obj.equals(elements[i])) return i;
            }
        }
        return -1;
    } //-- indexOf

    /**
     * Returns true if there are no elements in the List.
     * @return true if there are no elements in the List.
    **/
    public boolean isEmpty() {
        return (elementCount == 0);
    } //-- isEmpty

    /**
     * Returns the index of the last occurrence of the specified element,
     * or -1 if the element is not contained in the List
     * @param obj the Object to get the last index for
    **/
    public int lastIndexOf(Object obj) {
        if (obj == null) {
            for (int i = (elementCount-1); i >= 0; i--) {
                if (elements[i] == null) return i;
            }
        }
        else {
            for (int i = (elementCount-1); i >= 0; i--) {
                if (obj.equals(elements[i])) return i;
            }
        }
        return -1;
    } //-- lastIndexOf

    /**
     * Removes the element at the specified index from the List
     * @param index the position in the list tp remove the element from
     * @return the Object that was removed from the list
    **/
    public Object remove(int index) {

        if ((index < 0) || (index > elementCount)) return null;
        Object obj = elements[index];
        shiftDown(index+1);
        --elementCount;
        return obj;
    } //-- remove

    /**
     * Removes the first occurrence of the specified element from the List
     * @param obj the Object to remove from the List
     * @return true if the Object was removed from the list
    **/
    public boolean remove(Object obj) {
        int index = indexOf(obj);

        if (index > -1) {
            remove(index);
        }
        else return false;

        return true;
    } //-- remove

    /**
     * Reduces the capacity of the internal buffer to the current size
     * freeing up unused memory.
    **/
    public void trimToSize() {
	    if(elements == null)
		    return;
        Object[] pointer = elements;
        elements = new Object[elementCount];
        System.arraycopy(pointer, 0, elements, 0, elementCount);
        pointer = null;

    } //-- trimBuffer

    /**
     * Replaces the element at the specified position in this list
     * with the specified element.
     * @param index the position in the list to place the element at
     * @param element the element to add to the list
     * @exception IndexOutOfBoundsException
    **/

    public Object set(int index, Object element)
        throws IndexOutOfBoundsException
    {
        if ((index < 0) || (index > elementCount))
            throw new IndexOutOfBoundsException();
        Object oldElement = null;
        if (index == elementCount) add(element);
        else {
            oldElement = elements[index];
            elements[index] = element;
        }
        return oldElement;
    } //-- set


    /**
     * Returns the number of elements in the List
     * @return the number of elements in the List
    **/
    public int size() {
        return elementCount;
    } //-- size

    /**
     * Returns a new List which contains elements from a given section
     * of this list.
     * @param fromIndex the start index (inclusize) of elements
     * to add to the new list
     * @param toIndex the end index (exclusive)of the elements to add
     * to the new list
     * @return a new List which contains elements from a given section
     * of this list.
     * @exception IndexOutOfBoundsException for invalid index values
    **/
    public List subList(int fromIndex, int toIndex) {
        if ((fromIndex < 0) || (toIndex > size()) || (fromIndex > toIndex))
            throw new IndexOutOfBoundsException();
        List list = new List(toIndex-fromIndex);
        for (int i = fromIndex; i < toIndex; i++)
            list.add(elements[i]);
        return list;
    } //-- sublist

    /**
     * Returns an array containing all of the elements in this list
     * in proper sequence.
     * @return the array of elements of this List
    **/
    public Object[] toArray() {
        Object[] objArray = new Object[elementCount];
	if(elements != null)
		System.arraycopy(elements,0,objArray,0,elementCount);
        return objArray;
    } //-- toArray

    /**
     * Returns an array containing all of the elements in this list
     * in proper sequence.
     * @return the array of elements of this List
    **/
    public Object[] toArray(Object[] dst) {
        return toArray(dst,0);
    } //-- toArray

    /**
     * Returns an array containing all of the elements in this list
     * in proper sequence.
     * @return the array of elements of this List
    **/
    public Object[] toArray(Object[] dst, int offset) {

        Object[] objArray = null;

        if (dst.length >= elementCount) objArray = dst;
        else {
            Class dstType = dst.getClass();
            objArray = (Object[]) java.lang.reflect.Array.newInstance(dstType, elementCount);
        }
	if(elements != null)
		System.arraycopy(elements, 0, objArray, offset, elementCount);
        return objArray;
    } //-- toArray


      //-------------------/
     //- Private Methods -/
    //-------------------/

    /**
     * Basically the same as a vector,
     * increase the list by a factor of its initial size
    **/
    private void increaseSize() {
        Object[] pointer = elements;
        int length = (pointer.length > 0) ? pointer.length : 1;
        elements = new Object[length*2];
        System.arraycopy(pointer, 0, elements, 0, pointer.length);
        pointer = null;
    } //-- increaseSize

    /**
     * Shifts all elements at the specified index to down by 1
    **/
    private void shiftDown(int index) {
        if ((index <= 0) || (index >= elementCount)) return;
        System.arraycopy(elements, index, elements, index - 1, elementCount - index);
        // clean up for gc
        elements[elementCount-1] = null;
    } //-- shiftDown

    /**
     * Shifts all elements at the specified index up by 1
    **/
    private void shiftUp(int index) {
        if (index == elementCount) return;
        if (elementCount == elements.length) increaseSize();
        System.arraycopy(elements, index, elements, index + 1, elementCount - index);
    } //-- shiftUp



} //-- List
