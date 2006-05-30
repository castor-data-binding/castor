/**
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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.util;

import org.exolab.castor.xml.XMLFieldDescriptor;

/**
 * A class which represents a collection of XMLFieldDescriptor 
 * instances.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
 * @see java.util.List
 * @see java.util.Collection
**/
public class XMLFieldDescriptors {

    private int DEFAULT_SIZE = 11;

    private XMLFieldDescriptor[] elements;

    /**
     * The next available location in the elements array
    **/
    private int elementCount = 0;

    /**
     * Creates a new XMLFieldDescriptors with the default Size
     */
    public XMLFieldDescriptors() {
        elements = new XMLFieldDescriptor[DEFAULT_SIZE];
    } //-- XMLFieldDescriptors

    /**
     * Creates a new XMLFieldDescriptors with the given size.
     *
     * @param size the initial size of the internal collection.
     */
    public XMLFieldDescriptors(int size) {
        elements = new XMLFieldDescriptor[size];
    } //-- XMLFieldDescriptors

    /**
     * Adds the specified XMLFieldDescriptor to the collection.
     * If the specified XMLFieldDescriptor is already contained
     * in the collection, it will not be re-added, false will
     * be returned.
     *
     * @param descriptor the XMLFieldDescriptor to add
     * @return true if the descriptor is added, false otherwise.
     */
    public boolean add(XMLFieldDescriptor descriptor) {
        
        for (int i = 0; i < elementCount; i++) {
            if (elements[i] == descriptor) return false;
        }
        if (elementCount == elements.length) increaseSize();
        elements[elementCount++] = descriptor;
        return true;
    } //-- add

    /**
     * Removes all descriptors from this collection.
     */
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
     * Returns true if the specified descriptor is contained in this
     * collection. If the descriptor is null, then if this collection
     * contains a null value, true will be returned.
     *
     * @param descriptor the XMLFieldDescriptor to search the list for
     * @return true if specified descriptor is contained in the list
    **/
    public boolean contains(XMLFieldDescriptor descriptor) {
        return (indexOf(descriptor) >= 0);
    } //-- contains

    /**
     * Compares the specified object with this list for equality.
     * Returns true if and only if the specified Object is a list
     * and all of its associated elements are equal to the elements
     * of this list
     *
     * @return true if the given object is considered equal to this list.
     */
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof XMLFieldDescriptors)) return false;
        XMLFieldDescriptors descs = (XMLFieldDescriptors)obj;
        if (descs.elementCount != elementCount) return false;
        for (int i = 0; i < elementCount; i++) {
            Object e1 = get(i);
            Object e2 = descs.elements[i];
            if (!((e1 == null) ? (e2 == null) : e1.equals(e2)) )
               return false;
        }
        return true;
    } //-- equals

    /**
     * Returns the XMLFieldDescriptor at the specified position in this list.
     *
     * @param index the position of the descriptor to return
     * @exception IndexOutOfBoundsException
     */
    public XMLFieldDescriptor get(int index) throws IndexOutOfBoundsException {
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
     */
    public int hashCode() {
       int hashCode = 1;
       for (int i = 0; i < elementCount; i++) {
           Object obj = elements[i];
           hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode());
       }
       return hashCode;
    } //-- hashCode

    /**
     * Returns the index of the first occurrence of the specified 
     * XMLFieldDescriptor, or -1 if the descriptor is not contained in 
     * the collection.
     *
     * @param descriptor the XMLFieldDescriptor to get the index of
     */
    public int indexOf(XMLFieldDescriptor descriptor) {
        if (descriptor == null) {
            for (int i = 0; i < elementCount; i++) {
                if (elements[i] == null) return i;
            }
        }
        else {
            for (int i = 0; i < elementCount; i++) {
                if (descriptor.equals(elements[i])) return i;
            }
        }
        return -1;
    } //-- indexOf

    /**
     * Returns true if there are no descriptors in the collection.
     *
     * @return true if the collection is empty. 
     */
    public boolean isEmpty() {
        return (elementCount == 0);
    } //-- isEmpty


    /**
     * Removes the descriptor at the specified index from the list.
     *
     * @param index the position in the list to remove the descriptor from.
     * @return the descriptor that was removed from the list.
     */
    public XMLFieldDescriptor remove(int index) {

        if ((index < 0) || (index > elementCount)) return null;
        XMLFieldDescriptor desc = elements[index];
        shiftDown(index+1);
        --elementCount;
        return desc;
    } //-- remove

    /**
     * Removes the given XMLFieldDescriptor from the list.
     *
     * @param descriptor the XMLFieldDescriptor to remove from the list.
     * @return true if the descriptor was removed from the list.
     */
    public boolean remove(XMLFieldDescriptor descriptor) {
        int index = indexOf(descriptor);
        if (index > -1) {
            remove(index);
            return true;
        }
        return false;
    } //-- remove

    /**
     * Reduces the capacity of the internal buffer to the current size
     * freeing up unused memory.
     */
    public void trimToSize() {
        if (elements.length == elementCount) return;
        XMLFieldDescriptor[] pointer = elements;
        elements = new XMLFieldDescriptor[elementCount];
        System.arraycopy(pointer, 0, elements, 0, elementCount);
        pointer = null;
    } //-- trimToSize


    /**
     * Returns the number of descriptors in the list.
     *
     * @return the number of descriptors in the list.
     */
    public int size() {
        return elementCount;
    } //-- size


    /**
     * Returns an array containing all of the descriptors in this list
     * in proper sequence.
     *
     * @return the array of descriptors of this List
     */
    public XMLFieldDescriptor[] toArray() {
        XMLFieldDescriptor[] objArray = new XMLFieldDescriptor[elementCount];
        System.arraycopy(elements,0,objArray,0,elementCount);
        return objArray;
    } //-- toArray

    /**
     * Returns an array containing all of the descriptors in this list
     * in proper sequence.
     *
     * @return the array of descriptors of this list.
     */
    public XMLFieldDescriptor[] toArray(XMLFieldDescriptor[] dst) {
        return toArray(dst,0);
    } //-- toArray

    /**
     * Returns an array containing all of the elements in this list
     * in proper sequence.
     *
     * @return the array of descriptors of this list.
     */
    public XMLFieldDescriptor[] toArray(XMLFieldDescriptor[] dst, int offset) {

        XMLFieldDescriptor[] objArray = null;

        if (dst.length >= elementCount) objArray = dst;
        else {
            objArray = new XMLFieldDescriptor[elementCount];
        }
        System.arraycopy(elements, 0, objArray, offset, elementCount);
        return objArray;
    } //-- toArray


      //-------------------/
     //- Private Methods -/
    //-------------------/

    /**
     * Basically the same as a vector,
     * increase the list by a factor of its initial size
     */
    private void increaseSize() {
        XMLFieldDescriptor[] pointer = elements;
        int length = (pointer.length > 0) ? pointer.length : 1;
        elements = new XMLFieldDescriptor[(length*3)/2 + 1];
        System.arraycopy(pointer, 0, elements, 0, pointer.length);
        pointer = null;
    } //-- increaseSize

    /**
     * Shifts all elements at the specified index to down by 1
     */
    private void shiftDown(int index) {
        if ((index <= 0) || (index >= elementCount)) return;
        System.arraycopy(elements, index, elements, index - 1, elementCount - index);
        // clean up for gc
        elements[elementCount-1] = null;
    } //-- shiftDown

    /**
     * Shifts all elements at the specified index up by 1
     */
    private void shiftUp(int index) {
        if (index == elementCount) return;
        if (elementCount == elements.length) increaseSize();
        System.arraycopy(elements, index, elements, index + 1, elementCount - index);
    } //-- shiftUp



} //-- XMLFieldDescriptors
