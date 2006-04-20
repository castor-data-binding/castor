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


package org.exolab.castor.mapping.handlers;

import org.exolab.castor.mapping.MapHandler;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * A Map handler for adding and retreiving key-value pairs from
 * A map. A map handler is instantiated only once, must be thread
 * safe and not use any synchronization.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public final class J1MapHandler
    implements MapHandler
{

    /**
     * Creates a new Instance of the map represented by this MapHandler.
     *
     * @return the new map.
     */
    public Object create() {
        return new Hashtable();
    } //-- create
    
    /**
     * Adds the given key-value pair to the map. Keys must be unique.
     * Adding a key-value pair to the map, when an existing association
     * for that key already exists will cause the existing association
     * to be overwritten.
     * 
     * The map is provided as a parameter and is returned as the return 
     * value if the returned map is a different object. That way the handler 
     * can create a new map if necessary.
     *
     * @param map the map, null if no map has been created yet.
     * @param key the key for the object.
     * @param object the object to add to the map.
     * @return The map with the new object if a different
     *  instance than the <tt>map</tt> parameter, null otherwise
     * @throws ClassCastException The MapHandler does not
     *  support maps of the given type.
     */
    public Object put( Object map, Object key, Object object )
        throws ClassCastException
    {   
        Object returnVal = null;
        if (map == null) {
            map = new Hashtable();
            returnVal = map;
        }
        ((Hashtable)map).put(key, object);        
        return returnVal;
    } //-- put


    /**
     * Returns an enumeration of all the objects in the Map.
     *
     * @param map The map instance for which to return the enumeration
     * of elements for.
     * @return An enumeration of all the elements in the Map.
     * @throws ClassCastException The MapHandler does not
     *  support collections of this type
     */
    public Enumeration elements( Object map )
        throws ClassCastException
    {
        if (map == null) map = new Hashtable();
        return ((Hashtable)map).elements();
    } //-- elements

    /**
     * Returns an enumeration of all the keys in the Map.
     *
     * @param map The map instance for which to return the enumeration
     * of keys.
     * @return An enumeration of all the keys in the Map.
     * @throws ClassCastException The MapHandler does not
     *  support collections of this type
     */
    public Enumeration keys( Object map )
        throws ClassCastException
    {
        if (map == null) map = new Hashtable();
        return ((Hashtable)map).keys();
    } //-- keys


    /**
     * Returns the number of elements (key-value) in the map.
     *
     * @param map the map.
     * @return Number of key-value associations in the Map
     * @throws ClassCastException The MapHandler does not
     *  support collections of the given type.
     */
    public int size( Object map )
        throws ClassCastException
    {
        if (map == null) return 0;
        return ((Hashtable)map).size();
    } //-- size


    /**
     * Clears the map of all key-value pairs. 
     *
     * @param map the map to clear.
     * @throws ClassCastException The MapHandler does not
     *  support collections of the given type.
     */
    public void clear( Object map)
        throws ClassCastException
    {
        if (map == null) return;
        ((Hashtable)map).clear();
    } //-- clear

    /**
     * Returns the object associated with the given key.
     *
     * @param map the map to return the object from.
     * @param key the key for the object.
     * @return the object associated with the given key, or null
     * if no association was found in the given map.
     * @throws ClassCastException The MapHandler does not
     *  support maps of the given type.
     */
    public Object get( Object map, Object key)
        throws ClassCastException 
    {
        if (map == null) return null;
        return ((Hashtable)map).get(key);
    } //-- get

} //-- J1MapHandler



