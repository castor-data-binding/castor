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
 * $Id$
 */

package org.exolab.javasource;

import java.util.Vector;

/**
 * A simple String to Object mapping which preserves order.
 * <p>
 * <B>Note:</B> This class is not synchronized. So be careful. :-)
 * 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 */
public final class JNamedMap {

    private Vector _names = null;
    private Vector _objects = null;

    /**
     * Creates a new JNamedMap
     */
    public JNamedMap() {
        _names   = new Vector();
        _objects = new Vector();
    } //-- JNamedMap

    /**
     * Creates a new JNamedMap with the given size.
     * 
     * @param size the initial size for this NamedMap
     */
    public JNamedMap(final int size) {
        _names   = new Vector(size);
        _objects = new Vector(size);
    } //-- JNamedMap
    
    /**
     * Returns the Object associated with the given name.
     * 
     * @param name the name to search for
     * @return the Object associated with the given name
     */
    public Object get(final String name) {
        int i = indexOf(name);
        if (i >= 0) { return _objects.elementAt(i); }
        return null;
    } //-- get

    /**
     * Returns the Object at the given index.
     * 
     * @param index the index of the Object to return
     * @return the Object at the given index
     */
    public Object get(final int index) {
        return _objects.elementAt(index);
    } //-- get
    
    /**
     * Returns the name associated with the given Object
     * 
     * @param obj the Object to search for
     * @return the name of the given Object
     */
    public String getNameByObject(final Object obj) {
        int i = _objects.indexOf(obj);
        if (i >= 0) { return (String) _names.elementAt(i); }
        return null;
    } //-- getNameByObject

    /**
     * Return a Vector of names
     * 
     * @return a Vector of names
     */
    public Vector getNames() {
        return (Vector) _names.clone();
    } //-- getNames

    /**
     * Return a Vector of Objects
     * 
     * @return a Vector of Objects
     */
    public Vector getObjects() {
        return (Vector) _objects.clone();
    } //-- getObjects

    /**
     * Returns the index of the Object which has been mapped (associated) with
     * the given name
     * 
     * @param name The name to get the index of
     * @return the index of the Object which has been mapped (associated) to the
     *         given name
     */
    public int indexOf(final String name) {
        for (int i = 0; i < _names.size(); i++) {
            String iName = (String) _names.elementAt(i);
            if (iName.equals(name)) { return i; }
        }
        return -1;
    } //-- indexOf

    /**
     * Maps (associates) an Object with a name
     * 
     * @param name the name to associate with the given Object
     * @param obj the Object to be mapped
     */
    public void put(final String name, final Object obj) {
        int idx = indexOf(name);
        
        if (idx >= 0) {
            _objects.setElementAt(obj, idx);
        } else {
            //-- we may need some synchronization here
            //-- if we are in a multithreaded environment
            _names.addElement(name);
            _objects.addElement(obj);
        }
    } //-- put

    /**
     * Removes and returns the Object located at the given index
     * 
     * @param index the index of the Object to remove
     * @return the object removed from the map
     */
    public Object remove(final int index) {
        Object obj = _objects.elementAt(index);
        _objects.removeElementAt(index);
        _names.removeElementAt(index);
        return obj;
    } //-- remove

    /**
     * Removes and returns the Object associated with the given name
     * 
     * @param name the name of the Object to remove
     * @return the object removed from the map
     */
    public Object remove(final String name) {
        Object obj = null;
        
        int idx = indexOf(name);
        if (idx >= 0) {
            obj = _objects.elementAt(idx);
            _objects.removeElementAt(idx);
            _names.removeElementAt(idx);
        }
        return obj;
    } //-- remove

    /**
     * Returns the number of Object associations currently in this named map
     * 
     * @return the number of Object associations currently in this named map
     */
    public int size() {
        return _names.size();
    } //-- size

} //-- JNamedMap
