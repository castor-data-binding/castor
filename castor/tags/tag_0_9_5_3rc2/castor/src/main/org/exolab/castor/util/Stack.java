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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * The Original Source for this file is XSL:P
 *
 * $Id$
 */

package org.exolab.castor.util;

/**
 * A representation of a Stack that does not use Synchronization.
 * For compatibility this class supports the same methods as a 
 * java.util.Stack (JDK)
 *
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Stack {

    /**
     * Maximum Size of the "free" StackItem pool
     */
    private static final int MAX_POOL_SIZE = 19;

    private int size = 0;
    
    private StackItem top = null;
    
    //-- To prevent excess object creation
    //-- we hold onto some free stack items
    private StackItem _freeItems = null;
    
    /**
     * Keep track of the number of free items 
     * to prevent going over the max
     */
    private int _freeItemsCount = 0;
    
      //----------------/
     //- Constructors -/
    //----------------/
    
    /**
     * Creates an empty Stack
    **/
    public Stack() {
        super();
    }
    
    /**
     * Tests for an empty Stack
     * @return true if there are no elements on the stack, otherwise false.
    **/
    public boolean empty() {
        return (size() == 0);
    } //-- empty
    
    /**
     * Returns an Iterator for this Stack. The iterator will iterate from
     * the top of the stack, to the bottom
     *
    **/
    public Iterator iterator() {
        return new StackIterator(top);
    } //-- iterator
    
    /**
     * Returns the Object that is currently on top of the Stack. 
     * Unlike #pop the Object is not removed from the Stack.
     * @return the Object that is currently the top of the stack
     * @exception java.util.EmptyStackException when there are no
     * elements currently on the Stack
    **/
    public Object peek() 
        throws java.util.EmptyStackException 
    {
        if (empty()) throw new java.util.EmptyStackException();
        return top.object;
    } //--peek
    
    /**
     * Removes and returns the Object that is currently on top of the Stack.
     * @return the Object that is currently the top of the stack
     * @exception java.util.EmptyStackException when there are no
     * elements currently on the Stack
    **/
    public Object pop()
        throws java.util.EmptyStackException 
    {
        if (empty()) throw new java.util.EmptyStackException();
        Object obj = top.object;
        StackItem tmp = top;
        top = top.previous;
        if (top != null) top.next = null;
        --size;
        releaseStackItem(tmp);
        return obj;
    } //-- pop
    
    /**
     * Adds the given Object to the top of the Stack
    **/
    public void push(Object object) {
        StackItem item = getAvailableStackItem();
        item.previous = top;
        item.next = null;
        item.object = object;
        if (top != null) top.next = item;
        top = item;
        ++size;
    } //-- push
    
    /**
     * Searches for the given Object in the stack and returns it's position
     * relative to the top of the Stack (ie the number of calls to #pop() 
     * before the object is returned by #pop())
    **/
    public int search(Object object) {
        int idx = 0;
        StackItem item = top;
        while (item != null) {
            if (item.object == object) return idx;
            item = item.previous;
            ++idx;
        }
        return -1;
    } //-- indexOf
    
    /**
     * Returns the number of items on the Stack
     * @return the number of items on the Stack
    **/
    public int size() { 
        return size; 
    }
    
    /**
     * Returns an available StackItem or creates
     * a new one if none are available
     */
    private StackItem getAvailableStackItem() {
        StackItem item = null;
        if (_freeItems == null) {
            item = new StackItem();
        }
        else {
            item = _freeItems;
            _freeItems = _freeItems.previous;
            if (_freeItems != null) 
                _freeItems.next = null;
            --_freeItemsCount;
            item.clear();
        }
        return item;
    } //-- getAvailableStackItem
    
    /**
     * Returns an available StackItem or creates
     * a new one if none are available
     */
    private void releaseStackItem(StackItem item) {
        if (_freeItemsCount < MAX_POOL_SIZE) {
            item.clear();
            item.previous = _freeItems;
            item.next = null;
            if (_freeItems != null) _freeItems.next = item;
            _freeItems = item;
            ++_freeItemsCount;
        }
    } //-- getAvailableStackItem
    
    
    private class StackItem {
        StackItem next = null;
        StackItem previous = null;
        Object object = null;
        
        void clear() {
            object = null;
            previous = null;
            next = null;
        }
    } //-- StackItem
    
    public class StackIterator implements Iterator {
        
        StackItem current = null;
        
        protected StackIterator(StackItem top) {
            this.current = top;
        }
        
        public boolean hasNext() {
            return (current != null);
        } //-- hasNext
        
        public Object next() {
            if (current != null) {
                Object obj = current.object;
                current = current.previous; //-- reverse iteration
                return obj;
            }
            return null;
        } //-- next;
        
        public Object remove() 
            throws IllegalStateException 
        {
            //-- not yet implemented
            return null;
        } //-- remove
    }
} //-- Stack
