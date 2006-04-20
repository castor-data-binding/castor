/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The Original Code is XSL:P XSLT processor.
 * 
 * The Initial Developer of the Original Code is Keith Visco.
 * Portions created by Keith Visco (C) 1999-2001 Keith Visco.
 * All Rights Reserved..
 *
 * Contributor(s): 
 * Keith Visco, kvisco@ziplink.net
 *    -- original author. 
 *
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.adaptx.util;

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
 * @see java.util.List
 * @see java.util.Collection 
**/
public class List implements Cloneable {
    
    private int DEFAULT_SIZE = 25;
    
    private Object[] elements;
    
    private int initialSize = DEFAULT_SIZE;
    
    /**
     * The next available location in the elements array
    **/
    private int elementCount = 0;
    
    /**
     * Creates a new BasicSet with the default Size
    **/
    public List() {
        elements = new Object[DEFAULT_SIZE];
    } //-- List
    
    public List(int size) {
        initialSize = size;
        elements = new Object[size];
    } //-- List
    
    /**
     * Adds the specified Object to the list
     * @param obj the Object to add to the list
     * @return true if the Object is added to the list
    **/
    public boolean add(Object obj) {
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
            
        // make sure we have room to add the object
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
        System.arraycopy(elements,0,objArray,0,elementCount);
        return objArray;
    } //-- toArray

    /**
     * Returns an array containing all of the elements in this list 
     * in proper sequence.
     * @return the array of elements of this List
    **/
    public Object[] toArray(Object[] dst) {
        
        Object[] objArray = null;
        
        if (dst.length >= elementCount) objArray = dst;
        else {
            Class dstType = dst.getClass();
            objArray = (Object[]) java.lang.reflect.Array.newInstance(dstType, elementCount);
        }
        System.arraycopy(elements,0,objArray,0,elementCount);
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
