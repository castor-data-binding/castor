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
 *
 * <BR/>
 * <B>Note:</B>
 * This class is not synchronized. So be careful. :-)
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
**/
public class JNamedMap {

    private Vector names = null;
    private Vector objects = null;

    /**
     * Creates a new JNamedMap
    **/
    public JNamedMap() {

        names   = new Vector();
        objects = new Vector();

    } //-- JNamedMap

    /**
     * Creates a new JNamedMap with the given size.
     *
     * @param size the initial size for this NamedMap
    **/
    public JNamedMap(int size) {

        names   = new Vector(size);
        objects = new Vector(size);

    } //-- JNamedMap

    
    /**
     * Returns the Object associated with the given name.
     *
     * @param name the name to search for
     * @return the Object associated with the given name
    **/
    public Object get(String name) {
        int i = indexOf(name);
        if (i >= 0) return objects.elementAt(i);
        return null;
    } //-- get

    /**
     * Returns the Object at the given index.
     *
     * @param index the index of the Object to return
     * @return the Object at the given index
    **/
    public Object get(int index) 
        throws IndexOutOfBoundsException 
    {
        return objects.elementAt(index);
    } //-- get
    
    /**
     * Returns the name associated with the given Object
     *
     * @param obj the Object to search for
     * @return the name of the given Object
    **/
    public String getNameByObject(Object obj) {
        int i = objects.indexOf(obj);
        if (i >= 0) return (String)names.elementAt(i);
        return null;
    } //-- getNameByObject

    /**
     * Return a Vector of names
     *
     * @return a Vector of names
    **/
    public Vector getNames() {
        return (Vector)names.clone();
    } //-- getNames

    /**
     * Return a Vector of Objects
     *
     * @return a Vector of Objects
    **/
    public Vector getObjects() {
        return (Vector)objects.clone();
    } //-- getObjects

    /**
     * Returns the index of the Object which has been 
     * mapped (associated) with the given name
     *
     * @return the index of the Object which has been mapped (associated)
     * to the given name
    **/
    public int indexOf(String name) {
        
        for (int i = 0; i < names.size(); i++) {
            String iName = (String)names.elementAt(i);
            if (iName.equals(name)) return i;
        }
        return -1;
        
    } //-- indexOf

    /**
     * Maps (associates) an Object with a name
     *
     * @param name the name to associate with the given Object
     * @param obj the Object to be mapped
    **/
    public void put(String name, Object obj) {

        int idx = indexOf(name);
        
        if (idx >= 0) objects.setElementAt(obj, idx);
        else {
            //-- we may need some synchronization here
            //-- if we are in a multithreaded environment
            names.addElement(name);
            objects.addElement(obj);
        }
    } //-- put

    /**
     * Removes and returns the Object located at the given index
     *
     * @param index the index of the Object to remove
     * @return the object removed from the map.
    **/
    public Object remove(int index) 
        throws IndexOutOfBoundsException
    {
        Object obj = objects.elementAt(index);
        objects.removeElementAt(index);
        names.removeElementAt(index);
        return obj;
    } //-- remove

    /**
     * Removes and returns the Object associated with the given name
     *
     * @param name the name of the Object to remove
     * @return the object removed from the map.
    **/
    public Object remove(String name) {

        Object obj = null;
        
        int idx = indexOf(name);
        if (idx >= 0) {
            obj = objects.elementAt(idx);
            objects.removeElementAt(idx);
            names.removeElementAt(idx);
        }
        return obj;
    } //-- remove

    /**
     * Returns the number of Object associations currently in
     * this named map
     *
     * @return the number of Object associations currently in
     * this named map
    **/
    public int size() {
        return names.size();
    } //-- size

} //-- JNamedMap