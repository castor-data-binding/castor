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
 * Portions created by Keith Visco (C) 1998-2001 Keith Visco.
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
 * A simple String to Object mapping which preserves order
 * @author <a href="mailto:kvisco@ziplink.net">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class OrderedMap {
    
    /**
     * the list of names
    **/
    private List names   = null;
    
    /**
     * The list of objects
    **/
    private List objects = null;
    
    /**
     * Creates a new OrderedMap
    **/
    public OrderedMap() {
        names   = new List();
        objects = new List();
    } //-- OrderedMap

    /**
     * Creates a new OrderedMap with the given size
    **/
    public OrderedMap(int size) {
        names   = new List(size);
        objects = new List(size);
    } //-- OrderedMap
    
    
    
    public Object get(String name) {
        int i = indexOf(name);
        if (i >= 0) return objects.get(i);
        return null;
    } //-- get
    
    public Object get(int i) throws IndexOutOfBoundsException {
        return objects.get(i);
    } //-- get
    
    public String getNameByObject(Object obj) {
        int i = objects.indexOf(obj);
        if (i >= 0) return (String)names.get(i);
        return null;
    } //-- getNameByObject
    
    public List getNames() {
        return names;
    }
    
    public List getObjects() {
        return objects;
    }
    
    /**
     * Returns the index of the object with mapped to the given name
    **/
    public int indexOf(String name) {
        
        for (int i = 0; i < names.size(); i++) {
            String iName = (String)names.get(i);
            if (iName.equals(name)) return i;
        }
        return -1;
        
    } //-- indexOf
    
    public void put(String name, Object obj) {
        int idx = indexOf(name);
        if (idx >= 0) objects.add(idx, obj);
        else {
            //-- we may need some synchronization here
            //-- if we are in a multithreaded environment
            names.add(name);
            objects.add(obj);
        }
    } //-- put
    
    public int size() {
        return names.size();
    }
    
    
} //-- OrderedMap
